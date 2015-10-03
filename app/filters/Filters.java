package filters;

import play.api.mvc.EssentialFilter;
import play.filters.csrf.CSRFFilter;
import play.filters.gzip.GzipFilter;
import play.filters.headers.SecurityHeadersFilter;
import play.http.HttpFilters;

import javax.inject.Inject;

public class Filters implements HttpFilters {
    @Inject
    CSRFFilter csrfFilter;

    @Inject
    GzipFilter gzipFilter;

    @Override
    public EssentialFilter[] filters() {
        return new EssentialFilter[] {
            csrfFilter,
            gzipFilter
        };
    }
}
