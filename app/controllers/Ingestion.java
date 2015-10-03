package controllers;

import forms.ICalDownload;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import services.ICalService;

import javax.inject.Inject;

public class Ingestion extends Controller {
    private final ICalService iCalService;

    @Inject
    public Ingestion(ICalService iCalService) {
        this.iCalService = iCalService;
    }

    public Result loadFile() {
        Form<ICalDownload> form = Form.form(ICalDownload.class);
        return ok(views.html.Ingestion.loadFile.render(form));
    }
    public Result doLoadFile() {
        Form<ICalDownload> form = Form.form(ICalDownload.class).bindFromRequest();
        ICalDownload icd = form.get();
        iCalService.download(icd);
        return redirect(routes.Ingestion.loadFile());
    }

}
