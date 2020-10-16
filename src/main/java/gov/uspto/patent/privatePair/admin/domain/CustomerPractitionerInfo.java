package gov.uspto.patent.privatePair.admin.domain;


import org.apache.commons.lang3.text.WordUtils;

import lombok.Data;

/**
 * POJO for the Customer Practitioner HTML form.
 * 
 */
@Data
public class CustomerPractitionerInfo {
    private String familyName = null;
    private String givenName = null;
    private String middleName = null;
    private String nameSuffix = null;
    private String registrationNumber = null;

    private int id;
    private String nameField;
    private String commonErrorMessage;
    private boolean associatedToPairUserDn;
    private String associatedToPurm = "N";

    private String fullName;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (associatedToPairUserDn ? 1231 : 1237);
        result = prime * result + ((associatedToPurm == null) ? 0 : associatedToPurm.hashCode());
        result = prime * result + ((commonErrorMessage == null) ? 0 : commonErrorMessage.hashCode());
        result = prime * result + ((familyName == null) ? 0 : familyName.hashCode());
        result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
        result = prime * result + ((givenName == null) ? 0 : givenName.hashCode());
        result = prime * result + id;
        result = prime * result + ((middleName == null) ? 0 : middleName.hashCode());
        result = prime * result + ((nameField == null) ? 0 : nameField.hashCode());
        result = prime * result + ((nameSuffix == null) ? 0 : nameSuffix.hashCode());
        result = prime * result + ((registrationNumber == null) ? 0 : registrationNumber.hashCode());
        return result;
    }
    
    public void setFullDisplayName() {
        fullName = WordUtils.capitalizeFully(getGivenName() + getMiddleName() + getFamilyName() + getNameSuffix());
    }
    
    public String getFullDisplayName() {

        try {
            if (null != familyName && familyName.trim().length() > 0) {
                fullName = familyName.trim() + "\n";
            }
            if (null != givenName && givenName.trim().length() > 0) {
                fullName = fullName + givenName.trim() + "\n";
            }

            if (null != middleName && middleName.trim().length() > 0) {
                fullName = fullName + middleName.trim() + "\n";
            }

            if (null != nameSuffix && nameSuffix.trim().length() > 0) {
                fullName = fullName + nameSuffix.trim();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return fullName.trim();
    }

    @Override
    public boolean equals(Object cpInfo) {

        if (this == cpInfo)
            return true;

        // only registration number is common across systems, other data can
        // vary so equality relies on this primary key
        if (this.registrationNumber != null) {

            if ((cpInfo instanceof CustomerPractitionerInfo) && (((CustomerPractitionerInfo) cpInfo).registrationNumber != null))
                return this.registrationNumber.trim().equalsIgnoreCase(
                        ((CustomerPractitionerInfo) cpInfo).registrationNumber.trim());
        }

        return false;
    }
}
