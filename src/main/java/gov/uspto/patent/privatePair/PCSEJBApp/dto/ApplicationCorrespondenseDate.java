package gov.uspto.patent.privatePair.PCSEJBApp.dto;

import java.util.List;

public class ApplicationCorrespondenseDate {
    private String LastModifiedTimestamp;
    private String ApplicationNumber;
    private EarliestPublicationDate earliestPublicationDate;
    private String FilingDate;
    private String AttorneyDocketNumber;
    private String CustomerNumber;
    private List<DocumentData> DocumentList;

    public String getLastModifiedTimestamp() {
        return LastModifiedTimestamp;
    }

    public void setLastModifiedTimestamp(String lastModifiedTimestamp) {
        LastModifiedTimestamp = lastModifiedTimestamp;
    }

    public String getApplicationNumber() {
        return ApplicationNumber;
    }

    public void setApplicationNumber(String applicationNumber) {
        ApplicationNumber = applicationNumber;
    }

    public EarliestPublicationDate getEarliestPublicationDate() {
        return earliestPublicationDate;
    }

    public void setEarliestPublicationDate(EarliestPublicationDate earliestPublicationDate) {
        this.earliestPublicationDate = earliestPublicationDate;
    }

    public String getFilingDate() {
        return FilingDate;
    }

    public void setFilingDate(String filingDate) {
        FilingDate = filingDate;
    }

    public String getAttorneyDocketNumber() {
        return AttorneyDocketNumber;
    }

    public void setAttorneyDocketNumber(String attorneyDocketNumber) {
        AttorneyDocketNumber = attorneyDocketNumber;
    }

    public String getCustomerNumber() {
        return CustomerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        CustomerNumber = customerNumber;
    }

    public List<DocumentData> getDocumentList() {
        return DocumentList;
    }

    public void setDocumentList(List<DocumentData> documentList) {
        DocumentList = documentList;
    }
}
