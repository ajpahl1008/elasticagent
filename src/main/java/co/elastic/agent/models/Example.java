
package co.elastic.agent.models;

import java.util.List;

import co.elastic.agent.annotations.SkipMeasured;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.ToStringBuilder;

@SkipMeasured
public class Example {

    @SerializedName("app")
    @Expose
    public App app;
    @SerializedName("system")
    @Expose
    public System system;
    @SerializedName("transactions")
    @Expose
    public List<Transaction> transactions = null;

    public Example withApp(App app) {
        this.app = app;
        return this;
    }

    public Example withSystem(System system) {
        this.system = system;
        return this;
    }

    public Example withTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("app", app).append("system", system).append("transactions", transactions).toString();
    }

}
