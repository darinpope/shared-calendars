package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class CorsApiController extends Controller {
    public Result eventsWithGeo(String startDate,String endDate,Double latitude,Double longitude) {
        return index();
    }

    public Result events(String startDate,String endDate) {
        return index();
    }

    public Result event(String uid) {
        return index();
    }

    public Result search(String term) {
        return index();
    }

    public Result index() {
        // add headers discussed in the following posts:
        // http://empirewindrush.com/tech/2013/12/17/cors-and-play.html
        // http://enable-cors.org/server.html
        response().setHeader("Access-Control-Allow-Origin","*");
        response().setHeader("Allow","*");
        response().setHeader("Access-Control-Allow-Methods","POST, GET, PUT, DELETE, OPTIONS");
        response().setHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept, Referer, User-Agent, x-authorization, x-authorization-date, x-access-key, Access-Control-Allow-Origin");
        return ok();
    }
}
