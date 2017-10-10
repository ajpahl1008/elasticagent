package co.elastic.agent.models.configuration;

public class Connection {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return String.format( "\"%s\"", getUrl() );
    }
}
