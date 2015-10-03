package actions;

import play.libs.F;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

public class AddResponseHeadersAction extends Action.Simple {

    @Override
    public F.Promise<Result> call(Http.Context ctx) throws Throwable {
        F.Promise<Result> result = delegate.call(ctx);
        ctx.response().setHeader("X-XSS-Protection", "1; mode=block");
        ctx.response().setHeader("X-FRAME-OPTIONS", "SAMEORIGIN");
        ctx.response().setHeader("Access-Control-Allow-Origin", "*");
        ctx.response().setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE");
        ctx.response().setHeader("Access-Control-Allow-Headers", "x-requested-with,content-type,Cache-Control,Pragma,Date,x-authorization, x-authorization-date, x-access-key, accept, Access-Control-Allow-Origin");
        return result;
    }
}
