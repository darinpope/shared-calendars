package modules;

import com.amazonaws.services.s3.AmazonS3Client;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DynamicConfiguration;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.FixedDelayPollingScheduler;
import com.netflix.config.sources.JDBCConfigurationSource;
import com.netflix.config.sources.S3ConfigurationSource;
import org.apache.commons.configuration.AbstractConfiguration;
import play.Configuration;
import play.Logger;
import play.db.Database;
import play.inject.ApplicationLifecycle;
import play.libs.F;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;

@Singleton
public class Archaius {
    private final FixedDelayPollingScheduler pollingScheduler;
    private final AmazonS3Client s3Client;

    @Inject
    public Archaius(ApplicationLifecycle lifecycle, Configuration configuration) {
        s3Client = new AmazonS3Client();
        System.setProperty(DynamicPropertyFactory.DISABLE_DEFAULT_CONFIG, "true");
        System.setProperty(ConfigurationManager.DISABLE_DEFAULT_SYS_CONFIG, "true");
        Logger.info("s3Source.bucket: " + configuration.getString("s3Source.bucket"));
        Logger.info("s3Source.key: " + configuration.getString("s3Source.key"));
        Logger.info("Archaius: starting");
        S3ConfigurationSource s3Source = new S3ConfigurationSource(s3Client, configuration.getString("s3Source.bucket"),configuration.getString("s3Source.key"));
        pollingScheduler = new FixedDelayPollingScheduler();
        try {
            AbstractConfiguration config = new DynamicConfiguration(s3Source, pollingScheduler);
            if(!ConfigurationManager.isConfigurationInstalled() || config == ConfigurationManager.getConfigInstance()){
                DynamicPropertyFactory.initWithConfigurationSource(config);
            }
            Logger.info("Archaius: started");
        } catch (Exception e) {
            Logger.error(e.getMessage(), e);
        }

        lifecycle.addStopHook(() -> {
            if (pollingScheduler != null) {
                try {
                    pollingScheduler.stop();
                    Logger.info("Archaius: shutdown");
                } catch (Exception e) {
                }
            }
            if(s3Client != null) {
                try {
                    s3Client.shutdown();
                } catch (Exception e) {
                }
            }
            return F.Promise.pure(null);
        });
    }
}
