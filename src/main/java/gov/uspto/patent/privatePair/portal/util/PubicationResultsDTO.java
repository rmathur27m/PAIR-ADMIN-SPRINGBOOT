package gov.uspto.patent.privatePair.portal.util;

import java.util.HashMap;
import java.util.Map;

public class PubicationResultsDTO {

    private LifeCycle lifeCycle;

    private Audit audit;

    private String versionType;

    private String publicationStatus;

    private Integer publicationStatusDate;

    private String ipOfficeCode;

    private Integer publicationYear;

    private String publicationSequenceNo;

    private String patentDocumentKindCode;

    private Integer publicationDate;

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    public LifeCycle getLifeCycle() {
        return lifeCycle;
    }


    public void setLifeCycle(LifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
    }


    public Audit getAudit() {
        return audit;
    }


    public void setAudit(Audit audit) {
        this.audit = audit;
    }


    public String getVersionType() {
        return versionType;
    }


    public void setVersionType(String versionType) {
        this.versionType = versionType;
    }


    public String getPublicationStatus() {
        return publicationStatus;
    }


    public void setPublicationStatus(String publicationStatus) {
        this.publicationStatus = publicationStatus;
    }


    public Integer getPublicationStatusDate() {
        return publicationStatusDate;
    }


    public void setPublicationStatusDate(Integer publicationStatusDate) {
        this.publicationStatusDate = publicationStatusDate;
    }


    public String getIpOfficeCode() {
        return ipOfficeCode;
    }


    public void setIpOfficeCode(String ipOfficeCode) {
        this.ipOfficeCode = ipOfficeCode;
    }


    public Integer getPublicationYear() {
        return publicationYear;
    }


    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }


    public String getPublicationSequenceNo() {
        return publicationSequenceNo;
    }


    public void setPublicationSequenceNo(String publicationSequenceNo) {
        this.publicationSequenceNo = publicationSequenceNo;
    }


    public String getPatentDocumentKindCode() {
        return patentDocumentKindCode;
    }


    public void setPatentDocumentKindCode(String patentDocumentKindCode) {
        this.patentDocumentKindCode = patentDocumentKindCode;
    }


    public Integer getPublicationDate() {
        return publicationDate;
    }


    public void setPublicationDate(Integer publicationDate) {
        this.publicationDate = publicationDate;
    }


    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }


    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
