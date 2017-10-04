
package co.elastic.agent.models;

import co.elastic.agent.annotations.SkipMeasured;
import org.apache.commons.lang3.builder.ToStringBuilder;

@SkipMeasured
public class User {


    @Override
    public String toString() {
        return new ToStringBuilder(this).toString();
    }

}
