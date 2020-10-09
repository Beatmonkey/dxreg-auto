
package model.api.broker;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;


public class Value {

    @Expose
    private String value;

    public Value(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
