package models.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResponse implements Serializable {
    private Long found;
    private Long start;
    private List<ICalEvent> events;

    public List<ICalEvent> getEvents() {
        return events;
    }

    public void setEvents(List<ICalEvent> events) {
        this.events = events;
    }

    public Long getFound() {
        return found;
    }

    public void setFound(Long found) {
        this.found = found;
    }

    public Long getStart() {
        return start;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public void addICalEvent(ICalEvent event) {
        if(events == null) {
            events = new ArrayList<>();
        }
        events.add(event);
    }
}
