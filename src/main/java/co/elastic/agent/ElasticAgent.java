package co.elastic.agent;

import co.elastic.agent.annotations.SkipMeasured;
import co.elastic.agent.transformers.TimedClassTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;


public class ElasticAgent {

    private static Logger LOG = LoggerFactory.getLogger(ElasticAgent.class);

    @SkipMeasured
    public static void premain(String agentArguments, Instrumentation instrumentation){
        RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
        LOG.info("Runtime: {}: {}", runtimeMxBean.getName(), runtimeMxBean.getInputArguments());
        LOG.info("Starting agent with arguments " + agentArguments);
        LOG.info("Initial Class Count: " + instrumentation.getAllLoadedClasses().length);
        instrumentation.addTransformer(new TimedClassTransformer());
        LOG.info("Transformer Added");
    }

}
