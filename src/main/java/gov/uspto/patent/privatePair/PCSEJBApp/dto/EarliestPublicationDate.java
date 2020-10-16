package gov.uspto.patent.privatePair.PCSEJBApp.dto;

public class EarliestPublicationDate {
    private String PublicationDate;
    private String PublicationKindCode;
    private String PublicationSequenceNumber;
    private String EarliestPublicationYear;

    public String getPublicationDate() {
        return PublicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        PublicationDate = publicationDate;
    }

    public String getPublicationKindCode() {
        return PublicationKindCode;
    }

    public void setPublicationKindCode(String publicationKindCode) {
        PublicationKindCode = publicationKindCode;
    }

    public String getPublicationSequenceNumber() {
        return PublicationSequenceNumber;
    }

    public void setPublicationSequenceNumber(String publicationSequenceNumber) {
        PublicationSequenceNumber = publicationSequenceNumber;
    }

    public String getEarliestPublicationYear() {
        return EarliestPublicationYear;
    }

    public void setEarliestPublicationYear(String earliestPublicationYear) {
        EarliestPublicationYear = earliestPublicationYear;
    }
}
