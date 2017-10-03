package co.elastic.agent.transformers;

import co.elastic.agent.annotations.Measured;
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

public class TimedClassTransformer implements ClassFileTransformer {
	private Logger logger = LoggerFactory.getLogger(TimedClassTransformer.class);
	private ClassPool classPool;
	
	public TimedClassTransformer() {
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
				logger.info("Skipping Class: " + ctClass.getName());
				return null;
			}
			
			if (ctClass.isPrimitive() || ctClass.isArray() || ctClass.isAnnotation()
					|| ctClass.isEnum() || ctClass.isInterface()) {
				logger.debug("Skip class {}: not a class", className);
				return null;
			}
			
			boolean isClassModified = false;
			for(CtMethod method: ctClass.getDeclaredMethods()) {
				if  (!ctClass.getPackageName().contains("java") && !ctClass.getPackageName().contains("sun")
															    && !ctClass.getPackageName().contains("com.google")
																&& !ctClass.getPackageName().contains("co.elastic.agent")
																&& !ctClass.getPackageName().contains("org.elasticsearch")
                                                                && !ctClass.getPackageName().contains("org.joda")
																&& !ctClass.getPackageName().contains("org.json")
																&& !ctClass.getPackageName().contains("com.ibm")
																&& !ctClass.getPackageName().contains("org.eclipse")
																&& !ctClass.getPackageName().contains("org.apache")
																|| method.hasAnnotation(Measured.class)) {

					if (method.getMethodInfo().getCodeAttribute() == null) {
						logger.debug("Skip method " + method.getLongName());
						continue;
					}
					if (method.getMethodInfo().getName().contains("printTime") ) {
						logger.debug("Skip method " + method.getLongName());
						continue;
					}

					if (method.hasAnnotation(SkipMeasured.class)) {
						logger.info("Annotated skip: " + method.getLongName());
						continue;
					}

					logger.debug("Instrumenting method " + method.getLongName());
					method.addLocalVariable("__metricStartTime", CtClass.longType);
					method.insertBefore("__metricStartTime = System.currentTimeMillis();");
					String metricName = ctClass.getName() + "." + method.getName();
					method.insertAfter("co.elastic.agent.ElasticReporter.printTime(\"" + metricName + "\", System.currentTimeMillis() - __metricStartTime);");
					isClassModified = true;
				}
			}
			if (!isClassModified) {
				return null;
			}
			return ctClass.toBytecode();
		} catch (Exception e) {
			logger.debug("Skip class {}: ", className, e.getMessage());
			return null;
		}
    }
}