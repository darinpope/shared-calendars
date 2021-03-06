package modules;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudsearchdomain.AmazonCloudSearchDomainClient;
import com.amazonaws.services.cloudsearchdomain.model.Hit;
import com.amazonaws.services.cloudsearchdomain.model.QueryParser;
import com.amazonaws.services.cloudsearchdomain.model.SearchRequest;
import com.amazonaws.services.cloudsearchdomain.model.SearchResult;
import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;
import models.api.ICalEvent;
import models.api.SearchResponse;
import models.cloudsearch.ICalDocument;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.beans.BeanUtils;
import play.Application;
import play.Configuration;
import play.Logger;
import play.inject.ApplicationLifecycle;
import play.libs.F;
import utils.Features;
import utils.Helper;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Date;

@Singleton
public class CloudsearchSearch {

    private final AmazonCloudSearchDomainClient client;

    @Inject
    public CloudsearchSearch(ApplicationLifecycle lifecycle,Configuration configuration, Application app) {
        if(app.isProd()) {
            AWSCredentials credentials = new BasicAWSCredentials(
                    configuration.getString("aws.credentials.accessKey"),
                    configuration.getString("aws.credentials.secretKey")
            );
            client = new AmazonCloudSearchDomainClient(credentials);
        } else {
            client = new AmazonCloudSearchDomainClient();
        }
        client.setEndpoint(Features.getCloudSearchSearchEndpoint());
        Logger.info("CloudsearchSearch: started");

        lifecycle.addStopHook(() -> {
            if(client != null) {
                client.shutdown();
            }
            Logger.info("CloudsearchSearch: shutdown");
            return F.Promise.pure(null);
        });
    }

    public SearchResponse byStartDate(Date startDate,Long start,Long size) {
        SearchRequest request = new SearchRequest();
        request.setQueryParser(QueryParser.Structured);
        request.setQuery("start_time:['"+Helper.getStringFromDate(startDate)+"',}");
        request.setSort("start_time asc");
        request.setSize(size);
        request.setStart(start);
        return doSearch(request);
    }

    public SearchResponse search(String term,Long start,Long size) {
        SearchRequest request = new SearchRequest();
        request.setQuery(term);
        request.setFilterQuery("start_time:['"+Helper.getStringFromDate(DateTime.now(DateTimeZone.UTC).toDate())+"',}");
        request.setSort("start_time asc");
        request.setSize(size);
        request.setStart(start);
        return doSearch(request);
    }

    public SearchResponse events(Date startDate,Date endDate,Double latitude, Double longitude,Double distance,Long start, Long size) {
        SearchRequest request = new SearchRequest();
        request.setQueryParser(QueryParser.Structured);
        StringBuilder sb = new StringBuilder();
        sb.append("(and");
        sb.append(" start_time:['" + Helper.getStringFromDate(startDate) + "','"+Helper.getStringFromDate(endDate)+"']");
        if(latitude != null && longitude != null && distance != null) {
            LatLng latlng = new LatLng(latitude,longitude);
            LatLng upperLeft = LatLngTool.travel(latlng,315,distance, LengthUnit.MILE);
            LatLng lowerRight = LatLngTool.travel(latlng,135,distance, LengthUnit.MILE);
            sb.append(" geo:['"+upperLeft.getLatitude()+","+upperLeft.getLongitude()+"','"+lowerRight.getLatitude()+","+lowerRight.getLongitude()+"']");
        }
        sb.append(")");
        request.setQuery(sb.toString());
        request.setSort("start_time asc");
        request.setSize(size);
        request.setStart(start);
        return doSearch(request);
    }

    public ICalEvent event(String uid) {
        SearchRequest request = new SearchRequest();
        request.setQueryParser(QueryParser.Structured);
        request.setQuery("uid:'"+uid+"'");
        SearchResult result = null;
        try {
            result = client.search(request);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        if (result == null) {
            return null;
        }
        ICalEvent event = null;
        if (result.getHits() != null && result.getHits().getHit() != null && !result.getHits().getHit().isEmpty()) {
            for (Hit hit : result.getHits().getHit()) {
                event = getICalEvent(hit,true);
            }
        }
        return event;
    }

    private SearchResponse doSearch(SearchRequest request) {
        SearchResponse response = null;
        SearchResult result = null;
        try {
            result = client.search(request);
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }
        if (result == null) {
            return response;
        }

        response = new SearchResponse();
        response.setFound(result.getHits().getFound());
        response.setStart(result.getHits().getStart());
        if (result.getHits() != null && result.getHits().getHit() != null && !result.getHits().getHit().isEmpty()) {
            for (Hit hit : result.getHits().getHit()) {
                ICalEvent event = getICalEvent(hit,false);
                response.addICalEvent(event);
            }
        }
        return response;
    }

    private ICalEvent getICalEvent(Hit hit, boolean fullData) {
        ICalDocument r = new ICalDocument();
        r.setUid(hit.getId());
        if (hit.getFields().get("start_time") != null && !hit.getFields().get("start_time").isEmpty()) {
            r.setStartTime(Helper.getDateFromString(hit.getFields().get("start_time").get(0)));
        }
        r.setCompanyName(StringUtils.trimToNull(hit.getFields().get("company_name").get(0)));
        r.setCompanyCity(StringUtils.trimToNull(hit.getFields().get("company_city").get(0)));
        if (hit.getFields().get("summary") != null && !hit.getFields().get("summary").isEmpty()) {
            r.setSummary(StringUtils.trimToNull(hit.getFields().get("summary").get(0)));
        }
        if (fullData) {
            if (hit.getFields().get("end_time") != null && !hit.getFields().get("end_time").isEmpty()) {
                r.setEndTime(Helper.getDateFromString(hit.getFields().get("end_time").get(0)));
            }
            if (hit.getFields().get("company_state") != null && !hit.getFields().get("company_state").isEmpty()) {
                r.setCompanyState(StringUtils.trimToNull(hit.getFields().get("company_state").get(0)));
            }
            if (hit.getFields().get("company_postal_code") != null && !hit.getFields().get("company_postal_code").isEmpty()) {
                r.setCompanyPostalCode(StringUtils.trimToNull(hit.getFields().get("company_postal_code").get(0)));
            }
            if (hit.getFields().get("company_country_code") != null && !hit.getFields().get("company_country_code").isEmpty()) {
                r.setCompanyCountry(StringUtils.trimToNull(hit.getFields().get("company_country_code").get(0)));
            }
            if (hit.getFields().get("description") != null && !hit.getFields().get("description").isEmpty()) {
                r.setDescription(StringUtils.trimToNull(hit.getFields().get("description").get(0)));
            }
            if(hit.getFields().get("url") != null) {
                r.setUrl(StringUtils.trimToNull(hit.getFields().get("url").get(0)));
            }
            if(hit.getFields().get("location") != null) {
                r.setLocation(StringUtils.trimToNull(hit.getFields().get("location").get(0)));
            }
        }

        ICalEvent event = new ICalEvent();
        BeanUtils.copyProperties(r, event);
        event.setAllDayEvent(Helper.isAllDayEvent(event.getStartTime(), event.getEndTime()));
        //TODO: pull the images from somewhere else
        if (StringUtils.equalsIgnoreCase("Hope Community Church", event.getCompanyName())) {
            event.setCompanyImageUrl("https://scontent-iad3-1.xx.fbcdn.net/hprofile-xpt1/v/t1.0-1/p160x160/12046843_10153703346638385_569716643470458757_n.jpg?oh=cc19f7e1035b0691b823fd351aacd20b&oe=56D00C52");
        } else if (StringUtils.equalsIgnoreCase("The Summit Church", event.getCompanyName())) {
            event.setCompanyImageUrl("https://pbs.twimg.com/profile_images/2405818259/lyp6dibz2m9yb6oqvyer_400x400.png");
        } else if (StringUtils.equalsIgnoreCase("Fellowship Raleigh", event.getCompanyName())) {
            event.setCompanyImageUrl("https://s3.amazonaws.com/thecity/accounts/1370/account_images/logo_fellowshipraleigh.gif");
        }
        return event;
    }
}
