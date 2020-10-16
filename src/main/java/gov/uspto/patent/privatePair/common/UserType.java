package gov.uspto.patent.privatePair.common;

/**
 * Enumerator class for the possible User Type codes used by PAIRAdmin.
 */
public enum UserType {
    REGISTERED_ATTORNEYS("Registered Attorneys"), INDEPENDENT_INVENTOR("Independent Inventor"), TESTER("Tester");

    private String value;

    private UserType(String value) {
        this.value = value;
    }

    public String getName() {
        return value;
    }
}