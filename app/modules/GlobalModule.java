package modules;

import com.google.inject.AbstractModule;
import play.libs.akka.AkkaGuiceSupport;
import services.ICalService;
import services.ICalServiceImpl;

public class GlobalModule extends AbstractModule implements AkkaGuiceSupport {
    protected void configure() {
        bind(Archaius.class).asEagerSingleton();
        bind(CloudsearchDocument.class).asEagerSingleton();
        bind(CloudsearchSearch.class).asEagerSingleton();
        bind(ICalService.class).to(ICalServiceImpl.class);
    }
}
