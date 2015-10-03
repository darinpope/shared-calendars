import forms.ICalDownload;
import org.junit.Test;
import play.test.WithApplication;
import services.ICalService;

import java.io.File;

public class ICalReadTest extends WithApplication {

    @Test
    public void testHopeICal() {
        ICalService service = app.injector().instanceOf(ICalService.class);
        File file = new File("test/ical-hope.ics");
        ICalDownload icd = new ICalDownload();
        icd.setCompanyName("Hope Community Church");
        icd.setCompanyCity("Raleigh");
        icd.setCompanyState("North Carolina");
        icd.setCompanyPostalCode("27606");
        icd.setCompanyCountry("United States");
        service.process(file,icd);
    }
}
