package co.elastic.agent;

import co.elastic.agent.annotations.SkipMeasured;
import co.elastic.agent.models.configuration.Configuration;
import co.elastic.agent.transformers.TimedClassTransformer;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.nio.file.Files;
import java.nio.file.Paths;

@SkipMeasured
public class ElasticAgent {

    private static Logger LOG = LoggerFactory.getLogger(ElasticAgent.class);

    public static void premain(String agentArguments, Instrumentation instrumentation){

        LOG.info("=== Starting ElasticAgent ===");
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        LOG.info("Runtime: {}: {}", runtimeMxBean.getName(), runtimeMxBean.getInputArguments());

        if (System.getProperty("path.config") == null) {
            LOG.info("-Dpath.config=<config.yml> missing");
            System.exit(1);
        } else {
            LOG.info("path.config " + System.getProperty("path.config"));
        }
        LOG.info("Reading Elasticagent config File ");

        Yaml yaml = new Yaml();
        Configuration configuration = null;

        try {
            InputStream in = Files.newInputStream(Paths.get(System.getProperty("path.config")));
            configuration = yaml.loadAs(in, Configuration.class);
            LOG.debug(configuration.toString());
        } catch (Exception e) {
            LOG.error("Error Reading elasticagent.yml");
        }

        instrumentation.addTransformer(new TimedClassTransformer(configuration.getSkip()));

        LOG.info("Transformer Added");
    }

}
