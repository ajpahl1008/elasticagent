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

@SkipMeasured
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
				logger.debug("Skipping Class: " + ctClass.getName());
				return null;
			}
			
			if (ctClass.isPrimitive() || ctClass.isArray() || ctClass.isAnnotation()
					|| ctClass.isEnum() || ctClass.isInterface()) {
				logger.debug("Skip class {}: not a class", className);
				return null;
			}
			
			boolean isClassModified = false;
			for(CtMethod method: ctClass.getDeclaredMethods()) {
				//TODO: Need a skip class property loader and config file.
				// Almost all of this exclusion list is Websphere.
				if  (!ctClass.getPackageName().contains("java") && !ctClass.getPackageName().contains("sun")
															    && !ctClass.getPackageName().contains("com.google")
																&& !ctClass.getPackageName().contains("org.joda")
																&& !ctClass.getPackageName().contains("org.json")
																&& !ctClass.getPackageName().contains("org.xml")
																&& !ctClass.getPackageName().contains("com.ibm")
																&& !ctClass.getPackageName().contains("org.eclipse")
																&& !ctClass.getPackageName().contains("org.osgi")
																&& !ctClass.getPackageName().contains("org.apache")
																&& !ctClass.getPackageName().contains("org.objectweb")
																&& !ctClass.getPackageName().contains("org.kxml2")
																&& !ctClass.getPackageName().contains("org.jboss")
																&& !ctClass.getPackageName().contains("org.omg")
																&& !ctClass.getPackageName().contains("org.slf4j")
																&& !ctClass.getPackageName().contains("org.fusesource")
																&& !ctClass.getPackageName().contains("org.jcp")
																&& !ctClass.getPackageName().contains("org.aopalliance")
																&& !ctClass.getPackageName().contains("com.fasterxml")
																&& !ctClass.getPackageName().contains("org.springframework")
																&& !ctClass.getPackageName().contains("jdk")) {

					if (method.getMethodInfo().getCodeAttribute() == null) {
						logger.debug("Skip method " + method.getLongName());
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