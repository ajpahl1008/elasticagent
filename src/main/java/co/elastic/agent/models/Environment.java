package co.elastic.agent.models;

import co.elastic.agent.annotations.SkipMeasured;

import java.util.List;

@SkipMeasured
public class Environment {
    private int processId;
 private String hostname;
    private String mainClassName;
    private List<String> mainArgs;
    private String operatingSystemName;
    private String operatingSystemPlatform;

    public List<String> getMainArgs() {
        return mainArgs;
    }

    public void setMainArgs(List<String> mainArgs) {
        this.mainArgs = mainArgs;
    }

    public int getProcessId() {
        return processId;
    }

    public void setProcessId(int processId) {
        this.processId = processId;
    }
    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getMainClassName() {
        return mainClassName;
    }

    public void setMainClassName(String mainClassName) {
        this.mainClassName = mainClassName;
    }

    public String getOperatingSystemName() {
        return operatingSystemName;
    }

    public void setOperatingSystemName(String operatingSystemName) {
        this.operatingSystemName = operatingSystemName;
    }

    public String getOperatingSystemPlatform() {
        return operatingSystemPlatform;
    }

    public void setOperatingSystemPlatform(String operatingSystemPlatform) {
        this.operatingSystemPlatform = operatingSystemPlatform;
    }
}
