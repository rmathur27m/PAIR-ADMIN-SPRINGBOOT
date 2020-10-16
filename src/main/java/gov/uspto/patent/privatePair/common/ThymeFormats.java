package gov.uspto.patent.privatePair.common;

/**
 * Enumerator class for java.util.Date format.
 * 
 */
public enum ThymeFormats {

    GMT_PATTERN("dd MMM yyyy HH:mm:ss z");

    private String value;

    private ThymeFormats(String value) {
        this.value = value;
    }

    public String getName() {
        return value;
    }
}
