
package co.elastic.agent.models;

import co.elastic.agent.annotations.SkipMeasured;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class App {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("pid")
    @Expose
    private long pid;
    @SerializedName("process_title")
    @Expose
    private String processTitle;
    @SerializedName("argv")
    @Expose
    private List<String> argv = null;
    @SerializedName("runTime")
    @Expose
    private co.elastic.agent.models.RunTime runTime;
    @SerializedName("agent")
    @Expose
    private Agent agent;
    @SerializedName("framework")
    @Expose
    private Framework framework;

    public String getName() {
        return name;
    }

    @SkipMeasured
    public void setName(String name) {
        this.name = name;
    }

    public App withName(String name) {
        this.name = name;
        return this;
    }

    public long getPid() {
        return pid;
    }

    @SkipMeasured
    public void setPid(long pid) {
        this.pid = pid;
    }

    public App withPid(long pid) {
        this.pid = pid;
        return this;
    }

    public String getProcessTitle() {
        return processTitle;
    }

    @SkipMeasured
    public void setProcessTitle(String processTitle) {
        this.processTitle = processTitle;
    }

    public App withProcessTitle(String processTitle) {
        this.processTitle = processTitle;
        return this;
    }

    public List<String> getArgv() {
        return argv;
    }

    @SkipMeasured
    public void setArgv(List<String> argv) {
        this.argv = argv;
    }

    public App withArgv(List<String> argv) {
        this.argv = argv;
        return this;
    }

    public RunTime getRuntime() {
        return runTime;
    }

    @SkipMeasured
    public void setRuntime(RunTime runTime) {
        this.runTime = runTime;
    }

    public App withRuntime(RunTime runTime) {
        this.runTime = runTime;
        return this;
    }

    @SkipMeasured
    public Agent getAgent() {
        return agent;
    }
    @SkipMeasured
    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public App withAgent(Agent agent) {
        this.agent = agent;
        return this;
    }

    public Framework getFramework() {
        return framework;
    }
    @SkipMeasured
    public void setFramework(Framework framework) {
        this.framework = framework;
    }

    public App withFramework(Framework framework) {
        this.framework = framework;
        return this;
    }

    @Override
    @SkipMeasured
    public String toString() {
        return new ToStringBuilder(this).append("name", name).append("pid", pid).append("processTitle", processTitle).append("argv", argv).append("runtime", runTime).append("agent", agent).append("framework", framework).toString();
    }

}
