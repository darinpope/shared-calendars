package services;

import forms.ICalDownload;

import java.io.File;

public interface ICalService {
    void download(ICalDownload icd);
    void process(File file,ICalDownload icd);
}
