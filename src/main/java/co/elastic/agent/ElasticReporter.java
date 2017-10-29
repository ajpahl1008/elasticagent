package co.elastic.agent;

import co.elastic.agent.annotations.SkipMeasured;
import co.elastic.agent.models.Environment;
import co.elastic.agent.models.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@SkipMeasured
public class ElasticReporter {
    private final static Logger logger = LoggerFactory.getLogger(ElasticReporter.class);
    private static Configuration configuration;
    private static APMMessageFactory apmMessageFactory;
    private static Environment environment;

    private static void retrieveEnvironment() {
        if (environment == null) {
            environment = new Environment();
            environment.setProcessId(Integer.parseInt(java.lang.management.ManagementFactory.getRuntimeMXBean().getName().split("@")[0]));
            environment.setHostname(getLocalHostName());
            environment.setMainClassName(getJavaProgramName());
            environment.setOperatingSystemName(System.getProperty("os.name"));
            environment.setOperatingSystemPlatform(System.getProperty("os.arch"));
            environment.setMainArgs(getMainArgs());
        }
    }

    private static List<String> getMainArgs() {
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        return runtimeMxBean.getInputArguments();
    }

    private static void loadConfig() {
        if (configuration == null) {
            Yaml yaml = new Yaml();
            try {
                InputStream in = Files.newInputStream(Paths.get(System.getProperty("path.config")));
                configuration = yaml.loadAs(in, Configuration.class);
                logger.info(configuration.toString());
            } catch (Exception e) {
                logger.error("Error reading yml config file.");
            }
        }
    }

    public static void printTime(String name, long timeInMs) {
        loadConfig();
        retrieveEnvironment();
        apmMessageFactory = new APMMessageFactory(configuration,environment);
        apmMessageFactory.submitApmTransaction(name, timeInMs, Thread.currentThread().getStackTrace());
    }

    private static String getJavaProgramName() {
        for (final Map.Entry<String, String> entry : System.getenv().entrySet()) {
            if (entry.getKey().startsWith("JAVA_MAIN_CLASS")) {
                String[] chunks = entry.getValue().split("\\.");
                String mainClassName = chunks[chunks.length-1];
                return mainClassName;
            }
        }
        return "Unable to find Main Class name";
    }

    private static String getLocalHostName() {
        try  {
            return InetAddress.getLocalHost().getHostName();
        } catch (Exception e) {
            logger.info("Unable to determine HostName");
        }
        return "unknown";
    }
}
