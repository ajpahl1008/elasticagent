package co.elastic.agent;

import co.elastic.agent.annotations.SkipMeasured;
import co.elastic.agent.models.*;
import co.elastic.agent.models.configuration.Configuration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@SkipMeasured
public class APMMessageFactory {
    private final static Logger logger = LoggerFactory.getLogger(APMMessageFactory.class);


    private static App app;
    private static co.elastic.agent.models.System system;
    private static List<Transaction> transactions;
    private static Configuration configuration;
    private static Environment environment;

    public APMMessageFactory(Configuration configuration, Environment environment) {
        this.configuration = configuration;
        this.environment = environment;
    }

    public boolean submitApmError() {
        return false;  //TODO: Stubbed for future use
    }

    public boolean submitApmTransaction( String className, long executionTime, StackTraceElement[] stackTraceElements ) {
        app = new App();
        app.setName(environment.getMainClassName());
        app.setPid(environment.getProcessId());
        app.setProcessTitle("java");
        app.setArgv(environment.getMainArgs());

        RunTime runTime = new RunTime();
        runTime.setName("java");
        runTime.setVersion("jdk_1.8.0_144");
        app.setRuntime(runTime);

        Agent agent = new Agent();
        agent.setName("elasticagent");
        agent.setVersion("1.0-SNAPSHOT");
        app.setAgent(agent);

        Framework framework = new Framework();
        framework.setName("gradle");
        framework.setVersion("4.0.2");
        app.setFramework(framework);

        system = new co.elastic.agent.models.System();
        system.setHostname(environment.getHostname());
        system.setArchitecture(environment.getOperatingSystemPlatform());
        system.setPlatform(environment.getOperatingSystemName());

        transactions = new ArrayList<>();
        Transaction transaction = new Transaction();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setName(className);
        transaction.setType("setScopeHere");
        transaction.setDuration(executionTime);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'");
        Date date = new Date();
        transaction.setTimestamp(dateFormat.format(date));
        transaction.setResult("PASS");

        Context context = new Context();
        context.setCustom(new Custom());
        context.setTags(new Tags());
        context.setUser(new User());
        transaction.setContext(context);
        transaction.setTraces(null);  //TODO: This is where the calling class/method are going to be captured.

        transactions.add(transaction);

        Gson  gson = new GsonBuilder().create();

        JSONObject combined = new JSONObject();
        combined.put("app", gson.toJson(app));
        combined.put("system", gson.toJson(system));
        combined.put("transactions", gson.toJson(transactions));

        APMMessage apmMessage = new APMMessage();
        apmMessage.setApp(app);
        apmMessage.setSystem(system);
        apmMessage.setTransactions(transactions);

        try {
            URL url = new URL(configuration.getConnection().getUrl() + "/v1/transactions"); //TODO: Change this to a constant somewhere
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-type", "application/json");
            String input = gson.toJson(apmMessage);
            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_ACCEPTED) {
                logger.info("Failed : HTTP error code : "
                        + conn.getResponseCode() + " " + conn.getResponseMessage());
                return false;
            } else {
                logger.info(conn.getResponseMessage() + " " + input);
                return true;
            }
        } catch (MalformedURLException e) {
            logger.error("Malformed URL Exception: " + e.getCause());
            return false;
        } catch (IOException e) {
            logger.error("IOException: " + e.getStackTrace());
            return false;
        }

    }

}
