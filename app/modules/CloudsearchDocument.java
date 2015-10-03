package modules;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.cloudsearchdomain.AmazonCloudSearchDomainClient;
import com.amazonaws.services.cloudsearchdomain.model.*;
import models.cloudsearch.AddICalDocument;
import models.cloudsearch.ICalDocument;
import play.Application;
import play.Configuration;
import play.Logger;
import play.Play;
import play.inject.ApplicationLifecycle;
import play.libs.F;
import play.libs.Json;
import utils.Features;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Singleton
public class CloudsearchDocument {

    private final AmazonCloudSearchDomainClient client;

    @Inject
    public CloudsearchDocument(ApplicationLifecycle lifecycle, Configuration configuration, Application app) {
        if(app.isProd()) {
            AWSCredentials credentials = new BasicAWSCredentials(
                    configuration.getString("aws.credentials.accessKey"),
                    configuration.getString("aws.credentials.secretKey")
            );
            client = new AmazonCloudSearchDomainClient(credentials);
        } else {
            client = new AmazonCloudSearchDomainClient();
        }
        client.setEndpoint(Features.getCloudSearchDocumentEndpoint());
        Logger.info("CloudsearchDocument: started");

        lifecycle.addStopHook(() -> {
            if(client != null) {
                client.shutdown();
            }
            Logger.info("CloudsearchDocument: shutdown");
            return F.Promise.pure(null);
        });
    }

    public void uploadItemDocument(ICalDocument itemDocument) {
        AddICalDocument addItemDocument = new AddICalDocument();
        addItemDocument.setFields(itemDocument);
        addItemDocument.setId(itemDocument.getUid());
        addItemDocument.setType("add");

        UploadDocumentsResult result = null;
        try {
            UploadDocumentsRequest request = new UploadDocumentsRequest();
            String body = "[" + Json.stringify(Json.toJson(addItemDocument)) + "]";
            Logger.info(body);
            byte[] bytes = body.getBytes("UTF-8");
            InputStream stream = new ByteArrayInputStream(bytes);
            request.setDocuments(stream);
            request.setContentType(ContentType.Applicationjson);
            request.setContentLength((long) bytes.length);
            result = client.uploadDocuments(request);
        } catch (Exception e) {
            Logger.error(e.getMessage(),e);
        }
    }
}
