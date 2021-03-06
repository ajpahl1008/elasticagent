
package co.elastic.agent.models;

import co.elastic.agent.annotations.SkipMeasured;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

@SkipMeasured
public class Transaction {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("duration")
    @Expose
    private double duration;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("context")
    @Expose
    private Context context;
    @SerializedName("traces")
    @Expose
    private StackTraceElement[] traces = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Transaction withId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Transaction withName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Transaction withType(String type) {
        this.type = type;
        return this;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public Transaction withDuration(double duration) {
        this.duration = duration;
        return this;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Transaction withTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Transaction withResult(String result) {
        this.result = result;
        return this;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Transaction withContext(Context context) {
        this.context = context;
        return this;
    }

    public StackTraceElement[] getTraces() {
        return traces;
    }

    public void setTraces(StackTraceElement[] traces) {
        this.traces = traces;
    }

    public Transaction withTraces(StackTraceElement[] traces) {
        this.traces = traces;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("name", name).append("type", type).append("duration", duration).append("timestamp", timestamp).append("result", result).append("context", context).append("traces", traces).toString();
    }

}
