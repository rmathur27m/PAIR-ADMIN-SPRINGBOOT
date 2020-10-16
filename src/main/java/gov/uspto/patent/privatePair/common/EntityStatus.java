package gov.uspto.patent.privatePair.common;

/**
 * Enumerator class for Entity Status.
 * 
 */

public enum EntityStatus {
    UNDISCOUNTED("RegularEntityStatus"), SMALL("SmallEntityStatus"), INSTITUTION_OF_HIGHER_ED_BASIS_CERT1(
            "InstitutionOfHigherEdBasisCert1"), INSTITUTION_OF_HIGHER_ED_BASIS_CERT2("InstitutionOfHigherEdBasisCert2"), MICRO(
            "MicroEntityStatus");

    private String value;

    private EntityStatus(String value) {
        this.value = value;
    }

    public String getName() {
        return value;
    }
}