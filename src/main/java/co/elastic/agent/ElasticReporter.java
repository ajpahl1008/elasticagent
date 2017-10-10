package co.elastic.agent;

import co.elastic.agent.annotations.SkipMeasured;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@SkipMeasured
public class ElasticReporter {
    private final static Logger logger = LoggerFactory.getLogger(ElasticReporter.class);
    private final static int processId = Integer.parseInt(java.lang.management.ManagementFactory.getRuntimeMXBean().getName().split("@")[0]);


    public static void printTime(String name, long timeInMs) {
        Gson gsonObj = new Gson();

        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("method_name", name);
        inputMap.put("timing", Long.toString(timeInMs));

        String jsonStr = gsonObj.toJson(inputMap);

        logger.debug(jsonStr);

        APMMessageFactory.submitApmMessage(name, timeInMs, processId);
    }
}
