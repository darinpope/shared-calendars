package modules;

import com.amazonaws.services.cloudsearchdomain.AmazonCloudSearchDomainClient;
import play.Logger;
import play.inject.ApplicationLifecycle;
import play.libs.F;
import utils.Features;

import javax.inject.Inject;
import javax.inject.Singleton;

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
}
