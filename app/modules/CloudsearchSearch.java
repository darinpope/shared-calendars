package modules;

import com.amazonaws.services.cloudsearchdomain.AmazonCloudSearchDomainClient;
import com.amazonaws.services.cloudsearchdomain.model.Hit;
import com.amazonaws.services.cloudsearchdomain.model.QueryParser;
import com.amazonaws.services.cloudsearchdomain.model.SearchRequest;
import com.amazonaws.services.cloudsearchdomain.model.SearchResult;
import models.api.ICalEvent;
import models.api.SearchResponse;
import models.cloudsearch.ICalDocument;
import org.springframework.beans.BeanUtils;
import play.Logger;
import play.inject.ApplicationLifecycle;
import play.libs.F;
import utils.Features;
import utils.Helper;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Singleton
public class CloudsearchSearch {

    private final AmazonCloudSearchDomainClient client;

    @Inject
    public CloudsearchSearch(ApplicationLifecycle lifecycle) {
        client = new AmazonCloudSearchDomainClient();
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
        return search(request);
    }

    public SearchResponse byGeo(Double latitude,Double longitude) {
        SearchRequest request = new SearchRequest();

        return search(request);
    }

    private SearchResponse search(SearchRequest request) {
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
                ICalDocument r = new ICalDocument();
                r.setUid(hit.getId());
                if(hit.getFields().get("start_time") != null && !hit.getFields().get("start_time").isEmpty()) {
                    r.setStartTime(Helper.getDateFromString(hit.getFields().get("start_time").get(0)));
                }
                if(hit.getFields().get("end_time") != null && !hit.getFields().get("end_time").isEmpty()) {
                    r.setEndTime(Helper.getDateFromString(hit.getFields().get("end_time").get(0)));
                }
                r.setCompanyName(hit.getFields().get("company_name").get(0));
                r.setCompanyCity(hit.getFields().get("company_city").get(0));
                r.setCompanyState(hit.getFields().get("company_state").get(0));
                r.setCompanyPostalCode(hit.getFields().get("company_postal_code").get(0));
                r.setCompanyCountry(hit.getFields().get("company_country").get(0));
                if(hit.getFields().get("summary") != null && !hit.getFields().get("summary").isEmpty()) {
                    r.setSummary(hit.getFields().get("summary").get(0));
                }
                if(hit.getFields().get("description") != null && !hit.getFields().get("description").isEmpty()) {
                    r.setDescription(hit.getFields().get("description").get(0));
                }
                ICalEvent event = new ICalEvent();
                BeanUtils.copyProperties(r,event);
                event.setAllDayEvent(Helper.isAllDayEvent(event.getStartTime(),event.getEndTime()));
                response.addICalEvent(event);
            }
        }
        return response;
    }

}
