package models.cloudsearch;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ICalDocument implements Serializable {
    /**
     * BEGIN:VEVENT
     * DTSTART:20151026T230000Z
     * DTEND:20151027T003000Z
     * DTSTAMP:20151003T045642Z
     * CREATED:20150218T222634Z
     * LAST-MODIFIED:20150830T145031Z
     * UID:27156-1445900400-1445905800@www.gethope.net
     * SUMMARY:Financial Coaching
     * DESCRIPTION:Hope’s Financial Coaching Program is an 8-week learning experience designed to help you manage your money to the best of your ability. Whether just getting started with a budget\, looking to develop a savings plan or seeking guidance on eliminating debt\, our experienced coaching team is trained to help you apply biblical financial concepts to your daily life.\n\nFinancial Coaching Program covers:\n\nUnderstanding your current spending behavior.\nDeveloping a plan for your monthly cash flow.\nLearning the dangers associated with debt and how to eliminate debt for good.\nPreparing a strategy to save for emergencies and future expenses.\nImproving your financial decision-making process.\n\nClass is limited to the first 20 families to complete the registration process.\n\nFor more information contact the stewardship team.\n\nFill out my Wufoo form!
     * URL:http://www.gethope.net/financialcoaching/
     * LOCATION:821 Buck Jones Road\, Raleigh\, NC\, 27606\, United States
     * GEO:35.7716334;-78.7368404
     * X-APPLE-STRUCTURED-LOCATION;VALUE=URI;X-ADDRESS=821 Buck Jones Road Raleigh NC 27606 United States;X-APPLE-RADIUS=500;X-TITLE=821 Buck Jones Road:geo:-78.7368404,35.7716334
     * CATEGORIES:Finance,Financial Coaching
     * ATTACH;FMTTYPE=image/jpeg:http://www.gethope.net/wp-content/uploads/2014/09/FinancialCoaching.jpg
     * ORGANIZER;CN="Finance":MAILTO:stewardship@gethope.net
     * END:VEVENT
     */

    private String companyName;
    private String companyCity;
    private String companyState;
    private String companyPostalCode;
    private String companyCountry;
    private Date startTime;
    private Date endTime;
    private Date timestamp;
    private Date created;
    private Date lastModified;
    private String uid;
    private String summary;
    private String description;
    private String url;
    private String location;
    private String geo;
    private Set<String> categories;

    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    @JsonProperty("start_time")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @JsonProperty("end_time")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @JsonProperty("dt_timestamp")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @JsonProperty("created")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @JsonProperty("last_modified")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGeo() {
        return geo;
    }

    public void setGeo(String geo) {
        this.geo = geo;
    }

    public Set<String> getCategories() {
        return categories;
    }

    public void setCategories(Set<String> categories) {
        this.categories = categories;
    }

    @JsonProperty("company_name")
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @JsonProperty("company_city")
    public String getCompanyCity() {
        return companyCity;
    }

    public void setCompanyCity(String companyCity) {
        this.companyCity = companyCity;
    }

    @JsonProperty("company_state")
    public String getCompanyState() {
        return companyState;
    }

    public void setCompanyState(String companyState) {
        this.companyState = companyState;
    }

    @JsonProperty("company_postal_code")
    public String getCompanyPostalCode() {
        return companyPostalCode;
    }

    public void setCompanyPostalCode(String companyPostalCode) {
        this.companyPostalCode = companyPostalCode;
    }

    @JsonProperty("company_country")
    public String getCompanyCountry() {
        return companyCountry;
    }

    public void setCompanyCountry(String companyCountry) {
        this.companyCountry = companyCountry;
    }
}
