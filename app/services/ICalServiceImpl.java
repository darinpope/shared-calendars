package services;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.property.Categories;
import com.google.common.io.Files;
import forms.ICalDownload;
import models.cloudsearch.ICalDocument;
import modules.CloudsearchDocument;
import org.apache.commons.lang.StringUtils;
import play.Logger;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import utils.Helper;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Singleton
public class ICalServiceImpl implements ICalService {
    private final CloudsearchDocument cloudsearchDocument;
    private final WSClient ws;

    @Inject
    public ICalServiceImpl(CloudsearchDocument cloudsearchDocument,WSClient ws) {
        this.cloudsearchDocument = cloudsearchDocument;
        this.ws = ws;
    }

    @Override
    public void download(ICalDownload icd) {
        File file = null;
        WSResponse response;
        try {
            response = ws.url(icd.getUrl()).get().get(60, TimeUnit.SECONDS);
            file = new File(Helper.getTempDir() + UUID.randomUUID().toString());
            Files.write(response.asByteArray(),file);
            Logger.info("wrote " + file.getName() + " for " + icd.getCompanyName());
        } catch (Exception e) {
            Logger.error(e.getMessage(),e);
        }
        if(file == null) {
            Logger.error("unable to download file from " + icd.getUrl());
            return;
        }
        process(file,icd);
    }

    @Override
    public void process(File file,ICalDownload icd) {
        try {
            List<ICalendar> icals = null;
            try {
                icals = Biweekly.parse(file).all();
                if (icals == null || icals.isEmpty()) {
                    Logger.info("No records found for " + file);
                    return;
                }
            } catch (Exception e) {
                Logger.error(e.getMessage(), e);
                return;
            }
            Logger.info("number of icals found: " + icals.size());
            for (ICalendar ical : icals) {
                if (ical.getEvents() == null || ical.getEvents().isEmpty()) {
                    Logger.info("no events found for ical");
                    continue;
                }
                for (VEvent vEvent : ical.getEvents()) {
                    ICalDocument document = new ICalDocument();
                    document.setCompanyName(icd.getCompanyName());
                    document.setCompanyCity(icd.getCompanyCity());
                    document.setCompanyState(icd.getCompanyState());
                    document.setCompanyPostalCode(icd.getCompanyPostalCode());
                    document.setCompanyCountry(icd.getCompanyCountry());
                    if (vEvent.getDateStart() != null) {
                        document.setStartTime(vEvent.getDateStart().getValue());
                    }
                    if (vEvent.getDateEnd() != null) {
                        document.setEndTime(vEvent.getDateEnd().getValue());
                    }
                    if (vEvent.getDateTimeStamp() != null) {
                        document.setTimestamp(vEvent.getDateTimeStamp().getValue());
                    }
                    if (vEvent.getCreated() != null) {
                        document.setCreated(vEvent.getCreated().getValue());
                    }
                    if (vEvent.getLastModified() != null) {
                        document.setLastModified(vEvent.getLastModified().getValue());
                    }
                    if (vEvent.getUid() != null) {
                        document.setUid(StringUtils.trimToNull(vEvent.getUid().getValue()));
                    }
                    if (vEvent.getSummary() != null) {
                        document.setSummary(StringUtils.trimToNull(vEvent.getSummary().getValue()));
                    }
                    if (vEvent.getDescription() != null) {
                        document.setDescription(StringUtils.trimToNull(vEvent.getDescription().getValue()));
                    }
                    if (vEvent.getUrl() != null) {
                        document.setUrl(StringUtils.trimToNull(vEvent.getUrl().getValue()));
                    }
                    if (vEvent.getLocation() != null) {
                        document.setLocation(StringUtils.trimToNull(vEvent.getLocation().getValue()));
                    }
                    if (vEvent.getGeo() != null && vEvent.getGeo().getLatitude() != null && vEvent.getGeo().getLongitude() != null) {
                        document.setGeo(vEvent.getGeo().getLatitude() + ", " + vEvent.getGeo().getLongitude());
                    }

                    Set<String> categories = new TreeSet<>();
                    if (vEvent.getCategories() != null && !vEvent.getCategories().isEmpty()) {
                        for (Categories cs : vEvent.getCategories()) {
                            if (cs.getValues() != null && !cs.getValues().isEmpty()) {
                                categories.addAll(cs.getValues());
                            }
                        }
                    }
                    if (!categories.isEmpty()) {
                        document.setCategories(categories);
                    }
                    cloudsearchDocument.uploadItemDocument(document);
                }
            }
        } finally {
            try {
                file.delete();
            } catch (Exception e)  {
                Logger.error(e.getMessage(),e);
            }
        }
    }
}
