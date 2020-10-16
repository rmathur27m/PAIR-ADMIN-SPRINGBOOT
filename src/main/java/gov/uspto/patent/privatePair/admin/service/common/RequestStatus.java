package gov.uspto.patent.privatePair.admin.service.common;

/**
 * Enumerated type containing status codes for requests.
 * 
 */
public enum RequestStatus {
    SAVED("Saved"), SUBMITTED("Submitted"), FAILED("Failed");

    private String value;

    private RequestStatus(String value) {
        this.value = value;
    }

    public String getName() {
        return value;
    }
}