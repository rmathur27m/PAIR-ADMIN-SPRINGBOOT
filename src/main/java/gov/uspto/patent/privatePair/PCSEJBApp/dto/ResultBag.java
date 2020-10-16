package gov.uspto.patent.privatePair.PCSEJBApp.dto;

import java.util.List;

public class ResultBag {
    public String applicationNumberText;
    public List<DocumentBag> documentBag;

    public String getApplicationNumberText() {
        return applicationNumberText;
    }

    public void setApplicationNumberText(String applicationNumberText) {
        this.applicationNumberText = applicationNumberText;
    }

    public List<DocumentBag> getDocumentBag() {
        return documentBag;
    }

    public void setDocumentBag(List<DocumentBag> documentBag) {
        this.documentBag = documentBag;
    }
}
