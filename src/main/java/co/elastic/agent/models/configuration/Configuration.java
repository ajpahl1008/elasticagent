package co.elastic.agent.models.configuration;

import java.util.ArrayList;
import static java.lang.String.format;


public class Configuration {
    private Connection connection;
    private ArrayList<String> skip;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public ArrayList<String> getSkip() {
        return skip;
    }

    public void setSkip(ArrayList<String> skip) {
        this.skip = skip;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(format( "Connecting to apm-server: %s\n", connection ) )
                .append(format("Skipping packages: %s\n", skip))
                .toString();
    }
}
