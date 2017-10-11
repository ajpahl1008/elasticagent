package co.elastic.agent.transformers;

import co.elastic.agent.annotations.SkipMeasured;
import javassist.ByteArrayClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.List;


@SkipMeasured
public class TimedClassTransformer implements ClassFileTransformer {
	private Logger logger = LoggerFactory.getLogger(TimedClassTransformer.class);
	private ClassPool classPool;
	private List<String> skipInstrumentationList;
	
	public TimedClassTransformer(List<String> skipInstrumentationList) {
		this.skipInstrumentationList = skipInstrumentationList;
		classPool = new ClassPool();
		classPool.appendSystemPath();
		try {
			classPool.appendPathList(System.getProperty("java.class.path"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	 
    public byte[] transform(ClassLoader loader, String fullyQualifiedClassName, Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain, byte[] classBytes) throws IllegalClassFormatException {
    	String className = fullyQualifiedClassName.replace("/", ".");

		classPool.appendClassPath(new ByteArrayClassPath(className, classBytes));
		
		try {
			CtClass ctClass = classPool.get(className);
			if (ctClass.isFrozen()) {
				logger.debug("Skip class {}: is frozen", className);
				return null;
			}

			if (ctClass.hasAnnotation(SkipMeasured.class)) {
				logger.debug("Skipping Class: " + ctClass.getName());
				return null;
			}
			
			if (ctClass.isPrimitive() || ctClass.isArray() || ctClass.isAnnotation()
					|| ctClass.isEnum() || ctClass.isInterface()) {
				logger.debug("Skip class {}: not a class", className);
				return null;
			}

			if (matchesSkipList(ctClass.getPackageName())) {
				logger.debug("Skipping By Config: " + ctClass.getPackageName());
				return null;
			}
			boolean isClassModified = false;
			for(CtMethod method: ctClass.getDeclaredMethods()) {
				if (method.getMethodInfo().getCodeAttribute() == null) {
					logger.info("Skip method " + method.getLongName());
					continue;
				}
				if (method.getMethodInfo().getName().contains("printTime")) {
					logger.debug("Skip method " + method.getLongName());
					continue;
				}
				if (method.hasAnnotation(SkipMeasured.class)) {
					logger.debug("Skipped (Annotated): " + method.getLongName());
					continue;
				}

				logger.debug("Instrumenting method " + method.getLongName());
				method.addLocalVariable("__metricStartTime", CtClass.longType);
				method.insertBefore("__metricStartTime = System.currentTimeMillis();");
				String metricName = ctClass.getName() + "." + method.getName();
				method.insertAfter("co.elastic.agent.ElasticReporter.printTime(\"" + metricName + "\", System.currentTimeMillis() - __metricStartTime);");
				isClassModified = true;

			}
			if (!isClassModified) {
				return null;
			}
			return ctClass.toBytecode();
		} catch (Exception e) {
			logger.debug("Exception Caught: class {}: ", className);
			return null;
		}
    }

	private boolean matchesSkipList(String packageName) {
		logger.debug("Searching SkipList for Package: " + packageName);
			for (String str : skipInstrumentationList) {
				logger.debug("Match: " + str + " With " + packageName);
				if (packageName.contains(str)) {
					return true;
				}
			}
		return false;
	}
}