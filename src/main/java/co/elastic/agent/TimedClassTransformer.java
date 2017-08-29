package co.elastic.agent;

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
	private final static Logger logger = LoggerFactory.getLogger(TimedClassTransformer.class);
	private ClassPool classPool;
	
	public TimedClassTransformer() {
		classPool = new ClassPool();
		classPool.appendSystemPath();
		try {
			classPool.appendPathList(System.getProperty("java.class.path"));
			
			// make sure that MetricReporter is loaded
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
			
			if (ctClass.isPrimitive() || ctClass.isArray() || ctClass.isAnnotation()
					|| ctClass.isEnum() || ctClass.isInterface() || ctClass.getName().contains("ElasticMetricObject")) {
				logger.debug("Skip class {}: not a class", className);
				return null;
			}
			
			boolean isClassModified = false;
			for(CtMethod method: ctClass.getDeclaredMethods()) {
				if  (!ctClass.getPackageName().contains("java") && !ctClass.getPackageName().contains("sun")
															    && !ctClass.getPackageName().contains("google")
																&& !ctClass.getPackageName().contains("co.elastic")
																&& !ctClass.getPackageName().contains("elasticsearch")
                                                                && !ctClass.getPackageName().contains("org.joda")
																|| method.hasAnnotation(Measured.class)) {
					if (method.getMethodInfo().getCodeAttribute() == null) {
						logger.debug("Skip method " + method.getLongName());
						continue;
					}
					if (method.getMethodInfo().getName().contains("printTime") ) {
						logger.debug("Skip method " + method.getLongName());
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