
package model.api.client;

import java.util.List;

import com.google.gson.annotations.Expose;
import lombok.Builder;
import model.api.broker.Value;

@Builder
public class Category {

    @Expose
    private String category;
    @Expose
    private String value;

    public String getCategory() {
        return category;
    }


    public void setCategory(String category) {
        this.category = category;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
