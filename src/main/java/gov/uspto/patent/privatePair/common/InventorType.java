package gov.uspto.patent.privatePair.common;

/**
 * POJO representing the different types of inventors as an enumerator.
 * 
 */

public enum InventorType {

    SINGLE_JOINT_INVENTOR("Single Joint Inventor", "JIN"), SEVERAL_JOINT_INVENTORS("Several Joint Inventors", "JINS"), SOLE_INVENTOR(
            "Sole Inventor", "INV");

    private String displayValue;
    private String dbValue;

    private InventorType(String displayValue, String dbValue) {
        this.displayValue = displayValue;
        this.dbValue = dbValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    /**
     * Search for a specific inventor type based on what is displayed on the
     * screen
     * 
     * @param displayValue
     *            inventor type to search for
     * 
     * @return if found, enumerated object representing inventor type; null
     *         otherwise.
     * 
     */
    public static InventorType findByDisplayValue(String displayValue) {
        for (InventorType iType : InventorType.values()) {
            if (iType.getDisplayValue().equalsIgnoreCase(displayValue)) {
                return iType;
            }
        }

        return null;
    }

    public String getDbValue() {
        return dbValue;
    }

    /**
     * Search for a specific inventor type based on database code
     * 
     * @param dbValue
     *            database code representing inventor type to search for
     * 
     * @return if found, enumerated object representing inventor type; null
     *         otherwise.
     * 
     */
    public static InventorType findByDbValue(String dbValue) {
        for (InventorType iType : InventorType.values()) {
            if (iType.getDbValue().equalsIgnoreCase(dbValue)) {
                return iType;
            }
        }

        return null;
    }
}