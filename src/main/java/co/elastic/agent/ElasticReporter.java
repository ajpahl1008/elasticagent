package co.elastic.agent;

import co.elastic.agent.annotations.SkipMeasured;
import co.elastic.agent.models.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@SkipMeasured
public class ElasticReporter {
    private final static Logger logger = LoggerFactory.getLogger(ElasticReporter.class);
    private final static int processId = Integer.parseInt(java.lang.management.ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);
    private static Configuration configuration;
    private static APMMessageFactory apmMessageFactory;


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
        apmMessageFactory = new APMMessageFactory(configuration);
        apmMessageFactory.submitApmTransaction(name, timeInMs, processId);
    }
}
