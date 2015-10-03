package controllers;

import actions.AddResponseHeadersAction;
import models.api.SearchResponse;
import modules.CloudsearchSearch;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import utils.Helper;

import javax.inject.Inject;

@With({AddResponseHeadersAction.class})
public class API extends Controller {
    private final CloudsearchSearch cloudsearchSearch;

    @Inject
    public API(CloudsearchSearch cloudsearchSearch) {
        this.cloudsearchSearch = cloudsearchSearch;
    }

    public Result geo(Double latitude,Double longitude, Double distance) {
        SearchResponse result = null;
        try {
            result = cloudsearchSearch.byGeo(latitude, longitude, distance);
        } catch (Exception e) {
            Logger.error(e.getMessage(),e);
        }
        if(result == null) {
            return badRequest();
        }
        return ok(Json.toJson(result));
    }

    public Result byStartDate(String startDate,Long start,Long size) {
        SearchResponse result = null;
        try {
            result = cloudsearchSearch.byStartDate(Helper.getDateFromString(startDate),start,size);
        } catch (Exception e) {
            Logger.error(e.getMessage(),e);
        }
        if(result == null) {
            return badRequest();
        }
        return ok(Json.toJson(result));
    }

}
