package gov.uspto.patent.privatePair.admin.service.createcustomernumber;



import java.util.Comparator;

import gov.uspto.patent.privatePair.admin.domain.CustomerPractitionerInfo;
import gov.uspto.patent.privatePair.admin.dto.CustomerPractitionerDto;

/**
 * Helper method used in Create New Customer Number request processing.
 * 
 */
final class CustomerPractitionerComparator implements Comparator<Object> {
    @Override
    public int compare(Object o1, Object o2) {
        String lRegNumber = "", rRegNumber = "";

        if (o1 == null) {
            return Integer.MAX_VALUE;
        } else if (o1 instanceof CustomerPractitionerInfo) {
            if (((CustomerPractitionerInfo) o1).isAssociatedToPairUserDn())
                return Integer.MIN_VALUE;

            lRegNumber = ((CustomerPractitionerInfo) o1).getRegistrationNumber();
        } else if (o1 instanceof CustomerPractitionerDto) {
            lRegNumber = ((CustomerPractitionerDto) o1).getRegistration_no();
        } else {
            throw new RuntimeException("Instance should be of type CustomerPractitionerInfo or CustomerPractitionerDto");
        }

        if (o2 == null) {
            return Integer.MIN_VALUE;
        } else if (o2 instanceof CustomerPractitionerInfo) {
            if (((CustomerPractitionerInfo) o2).isAssociatedToPairUserDn())
                return Integer.MAX_VALUE;

            rRegNumber = ((CustomerPractitionerInfo) o2).getRegistrationNumber();
        } else if (o2 instanceof CustomerPractitionerDto) {
            rRegNumber = ((CustomerPractitionerDto) o2).getRegistration_no();
        } else {
            throw new RuntimeException("Instance should be of type CustomerPractitionerInfo or CustomerPractitionerDto");
        }

        return lRegNumber.compareToIgnoreCase(rRegNumber);
    }
}