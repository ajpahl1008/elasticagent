
package co.elastic.agent.models;

import co.elastic.agent.annotations.SkipMeasured;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

@SkipMeasured
public class APMMessage {

    @SerializedName("app")
    @Expose
    private App app;
    @SerializedName("system")
    @Expose
    private System system;
    @SerializedName("transactions")
    @Expose
    private List<Transaction> transactions = null;

    public App getApp() {
        return app;
    }

    @SkipMeasured
    public void setApp(App app) {
        this.app = app;
    }

    public APMMessage withApp(App app) {
        this.app = app;
        return this;
    }

    public System getSystem() {
        return system;
    }
    @SkipMeasured
    public void setSystem(System system) {
        this.system = system;
    }

    public APMMessage withSystem(System system) {
        this.system = system;
        return this;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
    @SkipMeasured
    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public APMMessage withTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        return this;
    }

    @Override
    @SkipMeasured
    public String toString() {
        return new ToStringBuilder(this).append("app", app).append("system", system).append("transactions", transactions).toString();
    }

}
