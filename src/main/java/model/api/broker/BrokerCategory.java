package model.api.broker;

import java.util.List;

public class BrokerCategory {

    private String category;
    private List<Value> values;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<Value> getValues() {
        return values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }
}
