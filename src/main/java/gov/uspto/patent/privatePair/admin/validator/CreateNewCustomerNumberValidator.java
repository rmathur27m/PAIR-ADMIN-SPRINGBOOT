package gov.uspto.patent.privatePair.admin.validator;


import gov.uspto.patent.privatePair.admin.domain.CreateNewCustomerNumberForm;
import gov.uspto.patent.privatePair.admin.domain.CustomerPractitionerInfo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
/**
 * Implementation of the Spring Framework Validator
 * 
 */

@Component
public class CreateNewCustomerNumberValidator implements Validator {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return CreateNewCustomerNumberForm.class.equals(aClass);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object obj, Errors errors) {

        CreateNewCustomerNumberForm createNewCustomerNumberForm = (CreateNewCustomerNumberForm) obj;
        checkForRequiredValues(createNewCustomerNumberForm, errors);

        if ((createNewCustomerNumberForm.getEMail1()) != null && !createNewCustomerNumberForm.getEMail1().isEmpty()) {
            if (isThisAValidPrimaryEmailAddress(createNewCustomerNumberForm.getEMail1()).equalsIgnoreCase("false")) {
                errors.rejectValue("eMail1", "eMail1.invalid", "Valid Email Address is required");
            }
        }

        if ((createNewCustomerNumberForm.getEMail2()) != null && !createNewCustomerNumberForm.getEMail2().isEmpty()) {
            if (isThisAValidPrimaryEmailAddress(createNewCustomerNumberForm.getEMail2()).equalsIgnoreCase("false")) {
                errors.rejectValue("eMail2", "eMail2.invalid", "Valid Email Address is required");
            }
        }

        if ((createNewCustomerNumberForm.getEMail3()) != null && !createNewCustomerNumberForm.getEMail3().isEmpty()) {
            if (isThisAValidPrimaryEmailAddress(createNewCustomerNumberForm.getEMail3()).equalsIgnoreCase("false")) {
                errors.rejectValue("eMail3", "eMail3.invalid", "Valid Email Address is required");
            }
        }

        if ((createNewCustomerNumberForm.getPocEmail()) != null && !createNewCustomerNumberForm.getPocEmail().isEmpty()) {
            if (isThisAValidPrimaryEmailAddress(createNewCustomerNumberForm.getPocEmail()).equalsIgnoreCase("false")) {
                errors.rejectValue("pocEmail", "pocEmail.invalid", "Valid Email Address is required");
            }
        }

        if (createNewCustomerNumberForm.getCountry() != null
                && (createNewCustomerNumberForm.getCountry().equalsIgnoreCase("US")
                        || createNewCustomerNumberForm.getCountry().equalsIgnoreCase("CA") || createNewCustomerNumberForm
                        .getCountry().equalsIgnoreCase("GB"))) {
            if (createNewCustomerNumberForm.getState() == null || createNewCustomerNumberForm.getState().isEmpty()) {
                errors.rejectValue("state", "state.invalid", "State is required if the Country is US, CA or GB");
            }
        }

        if ((createNewCustomerNumberForm.getCustomerPractitionerInfolist()) != null
                && !createNewCustomerNumberForm.getCustomerPractitionerInfolist().isEmpty()) {

            List<String> checkForDuplicates = new ArrayList<String>();
            for (CustomerPractitionerInfo custInfo : createNewCustomerNumberForm.getCustomerPractitionerInfolist()) {

                if (custInfo.getRegistrationNumber().trim().length() != 5) {
                    custInfo.setCommonErrorMessage("Registration number length should be 5");
                    errors.rejectValue("customerPractitionerInfo", "customerPractitionerInfo.length",
                            "Registration number length should be 5, please fix them");
                }

                if (StringUtils.isNotEmpty(custInfo.getCommonErrorMessage())) {
                    if (custInfo.getCommonErrorMessage().startsWith("WARNING:")) {
                        errors.rejectValue("customerPractitionerInfo", "customerPractitionerInfo.invalid",
                                custInfo.getCommonErrorMessage());
                        custInfo.setCommonErrorMessage(null);
                    } else {
                        errors.rejectValue("customerPractitionerInfo", "customerPractitionerInfo.invalid",
                                "Inactive Registration number(s)");
                    }
                }

                if (!checkForDuplicates.contains(custInfo.getRegistrationNumber())) {
                    checkForDuplicates.add(custInfo.getRegistrationNumber());
                } else {
                    custInfo.setCommonErrorMessage("Duplicate");
                    errors.rejectValue("customerPractitionerInfo", "customerPractitionerInfo.duplicate",
                            "Duplicate Registration numbers found, please remove them");
                }
            }
        }
								if (!createNewCustomerNumberForm.getPocSignature().isEmpty() && (createNewCustomerNumberForm.getPocSignature().length() < 3
										|| !(StringUtils.startsWith(
												createNewCustomerNumberForm.getPocSignature(), "/") && StringUtils
												.endsWith(createNewCustomerNumberForm.getPocSignature(), "/")))) {
									errors.rejectValue("pocSignature", "pocSignature.invalid",
											"Invalid signature format.Expected signature format is /Signature/");
								}
    }

    private void checkForRequiredValues(CreateNewCustomerNumberForm createNewCustomerNumberForm, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firmorIndividualNameLine1", "firmorIndividualNameLine1.required",
                "Required field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "addressLine1", "addressLine1.required", "Required field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "city", "city.required", "Required field");
        // ValidationUtils.rejectIfEmptyOrWhitespace(errors, "country",
        // "country.required", "Required field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "telephone1", "telephone1.required", "Required field");
        if (createNewCustomerNumberForm.getOutgoingCorrespondence().equalsIgnoreCase("Email")) {
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "eMail1", "eMail1.required", "Required field");
        }
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pocSignature", "pocSignature.required", "Required field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pocFiledBy", "pocFiledBy.required", "Required field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pocName", "pocName.required", "Required field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pocTelephone", "pocTelephone.required", "Required field");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pocEmail", "pocEmail.required", "Required field");
    }

    private String isThisAValidPrimaryEmailAddress(String eMailAddress) {

        String isValid = "true";
            if (null != eMailAddress) {
                if (eMailAddress.trim().length() > 0) {
                    int indexOfAmpersand = 0;
                    int indexOfLastDot = 0;
                    indexOfAmpersand = eMailAddress.indexOf("@");
                    indexOfLastDot = eMailAddress.lastIndexOf(".");
                    int countAtsign = StringUtils.countMatches(eMailAddress, "@");
                    int countforSemicolon = StringUtils.countMatches(eMailAddress, ";");
                    if (countAtsign < 1 || countAtsign > 1 || countforSemicolon > 0) {
                        isValid = "false";
                    }// if(-1 == indexOfAmpersand)
                    if ((indexOfLastDot >= eMailAddress.trim().length() - 1)) {
                        isValid = "false";
                    }// if((indexOfLastDot >= eMailAddress.trim().length() -1))
                    if ((indexOfLastDot < indexOfAmpersand)) {
                        isValid = "false";
                    }// if((indexOfLastDot < indexOfAmpersand))
                }// if(eMailAddress.trim().length() >0)
            }// if( null != eMailAddress)
        return isValid;
    }
}
