package controllers;

import models.api.SearchResponse;
import modules.CloudsearchSearch;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Helper;

import javax.inject.Inject;

public class API extends Controller {
    private final CloudsearchSearch cloudsearchSearch;

    @Inject
    public API(CloudsearchSearch cloudsearchSearch) {
        this.cloudsearchSearch = cloudsearchSearch;
    }

    public Result geo(Double latitude,Double longitude) {
        return ok();
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
