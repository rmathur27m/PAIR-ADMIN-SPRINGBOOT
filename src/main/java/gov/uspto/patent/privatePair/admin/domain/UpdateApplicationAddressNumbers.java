package gov.uspto.patent.privatePair.admin.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * POJO for the Update Application Address HTML form.
 * 
 */

@Data
public class UpdateApplicationAddressNumbers {

    private String applicationNumber;
    private String patentNumber;
    private String correspondenceAddressCheckBox;
    private String maintenanceFeeAddressCheckBox;
    private String validationErrorMessage;
    private String powerOfAttorney;
    private String addRowButton;
    private String deleteRowButton;

    @Override
    public int hashCode() {
        // Two randomly chosen prime numbers: 17, 31
        return new HashCodeBuilder(17, 31).append(applicationNumber).append(patentNumber).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UpdateApplicationAddressNumbers))
            return false;

        if (obj == this)
            return true;

        UpdateApplicationAddressNumbers rhs = (UpdateApplicationAddressNumbers) obj;

        return new EqualsBuilder().append(applicationNumber, rhs.applicationNumber).append(patentNumber, rhs.patentNumber)
                .isEquals();
    }
}
