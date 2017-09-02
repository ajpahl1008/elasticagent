package co.elastic.agent;

//import org.elasticsearch.action.index.IndexResponse;
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.InetSocketTransportAddress;
//import org.elasticsearch.transport.client.PreBuiltTransportClient;
//

import java.net.InetAddress;

public class ElasticSearchTransport {
//    private String server;
//    private int port;
//    private String index;
//    private String userId;
//    private String password;
//
//    private static TransportClient client;
//
//    public ElasticSearchTransport(String server, int port, String index, String userId, String password) {
//                this.server = server;
//                this.port = port;
//                this.index = index;
//                this.userId = userId;
//                this.password = password;
//    };
//
//    public int connect() throws Exception {
//        client = new PreBuiltTransportClient(Settings.EMPTY)
//                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(server), port));
//        return client.connectedNodes().size();
//    }
//
//    public int sendMessage(String jsonString) {
//        IndexResponse response = client.prepareIndex(index,"java").setSource(jsonString).get();
//        return response.status().getStatus();
//    }
//
//    public void disconnect() {
//        client.close();
//    }

}
