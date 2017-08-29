package co.elastic.agent;

import com.google.gson.Gson;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class ElasticReporter {
    private final static Logger logger = LoggerFactory.getLogger(ElasticReporter.class);

    public static void printTime(String name, long timeInMs) {

        Gson gsonObj = new Gson();

        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("method_name", name);
        inputMap.put("timing", Long.toString(timeInMs));

        String jsonStr = gsonObj.toJson(inputMap);
        System.out.println(jsonStr);
        logger.info(jsonStr);

        //TODO: Report Asynchronously to an ElasticSearch Instance
//        try {
//            TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
//                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.0.214"), 9300));
//            IndexResponse response = client.prepareIndex("javaagent", "java")
//                    .setSource(jsonStr)
//                    .get();
//
//        } catch (Exception e) {
//            System.out.println("Dude broke");
//        }
    }


}
