package models.cloudsearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddICalDocument implements Serializable {
    private String type;
    private String id;
    private ICalDocument fields;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ICalDocument getFields() {
        return fields;
    }

    public void setFields(ICalDocument fields) {
        this.fields = fields;
    }
}
