package gov.uspto.patent.privatePair.admin.validator;



import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import gov.uspto.patent.privatePair.admin.domain.UpdateApplicationAddressForm;
import gov.uspto.patent.privatePair.admin.domain.UpdateApplicationAddressNumbers;
import gov.uspto.patent.privatePair.admin.service.updateapplicationaddress.UpdateApplicationAddressServices;

/**
 * Implementation of the Spring Framework Validator
 *
 */
@Component
public class UpdateApplicationAddressValidator implements Validator {

    @Autowired
    UpdateApplicationAddressServices updateApplicationAddressFacadeImpl;

    private boolean applicationNumberFailsValidation;
    private boolean patentNumberFailsValidation;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return UpdateApplicationAddressForm.class.equals(clazz);
    }

    /**
     * Application number starts with either a '90' or a '95'
     *
     */
    public boolean isReexaminationApplicationNumber(String applicationNumber) {

        Pattern p = Pattern.compile("^(90|95)");

        Matcher matcher = p.matcher(applicationNumber);

        if (matcher.find()) {
            return true;
        }

        return false;
    }

    /**
     * Application number starts with a '96'
     *
     */
    public boolean isSupplementalExaminationApplicationNumber(String applicationNumber) {

        Pattern p = Pattern.compile("^(96)");

        Matcher matcher = p.matcher(applicationNumber);

        if (matcher.find()) {
            return true;
        }

        return false;
    }

    /*
     * 99/999,999 pattern
     */
    public boolean isValidApplicationNumberPattern_1(String applicationNumber) {

        if (applicationNumber.matches("[0-9][0-9]/[0-9][0-9][0-9][,][0-9][0-9][0-9]"))
            return true;
        else
            return false;
    }

    /*
     * 99/999999 pattern
     */
    public boolean isValidApplicationNumberPattern_2(String applicationNumber) {

        if (applicationNumber.matches("[0-9][0-9]/[0-9][0-9][0-9][0-9][0-9][0-9]"))
            return true;
        else
            return false;
    }

    /*
     * 99999999 pattern
     */
    public boolean isValidApplicationNumberPattern_3(String applicationNumber) {

        if (applicationNumber.matches("[0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]"))
            return true;
        else
            return false;
    }

    public boolean isValidPatentPrefix(String signature) {

        Pattern p = Pattern.compile("^(RE|PP|D|[0-9]|P)");

        Matcher matcher = p.matcher(signature);

        if (matcher.find()) {
            return true;
        }

        return false;

    }

    public boolean isPctId(String s) {

        String pattern = "^PCT(.*)";

        if (s.matches(pattern))
            return true;
        else
            return false;
    }

    /*
     * Regular Expression to validate email address. <p> Matches:
     * something@someserver.com | firstname.lastname@mailserver.domain.com |
     * username-something@some-server. <p> Fails: username@someserver.domain.c |
     * somename@server.domain-com | someone@something.se_eo
     */
    public boolean validEmailAddress(String string) {

        String pattern = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$";

        if (string.matches(pattern))
            return true;
        else
            return false;
    }

    public boolean signatureStartsWithMultipleSlashes(String signature) {

        Pattern p = Pattern.compile("^(/)\\1+");

        Matcher matcher = p.matcher(signature);

        if (matcher.find()) {
            return true;
        }

        return false;
    }

    public boolean signatureEndsWithMultipleSlashes(String signature) {

        Pattern p = Pattern.compile("(/)\\1+$");

        Matcher matcher = p.matcher(signature);

        if (matcher.find()) {
            return true;
        }

        return false;
    }

    public boolean signatureStartsWithSlash(String signature) {

        return StringUtils.startsWith(signature, "/");
    }

    public boolean signatureEndsWithSlash(String signature) {

        return StringUtils.endsWith(signature, "/");
    }

    public String removeSpecialCharacters(String string) {

        return (string.replaceAll("[R/E,PD]", ""));
    }

    public boolean isNumeric(String string) {

        String numericPattern = "^[0-9]*$";

        if (string.matches(numericPattern)) {
            return true;
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object target, Errors errors) {

        UpdateApplicationAddressForm updateApplicationAddressForm = (UpdateApplicationAddressForm) target;

        if (StringUtils.isEmpty(updateApplicationAddressForm.getCommonName())) {
            errors.rejectValue("commonName", "name.required", "Required field");
        }

        if (StringUtils.isEmpty(updateApplicationAddressForm.getCustomerNumber())) {
            errors.rejectValue("customerNumber", "customer.number", "Missing Cust. Number");
        } 
        else if (!updateApplicationAddressFacadeImpl.validCustomerNumber(updateApplicationAddressForm.getCustomerNumber()
                .trim())) {
            errors.rejectValue("customerNumber", "invalid.customer.number", "Invalid Cust. Number");
        }

        if (StringUtils.isEmpty(updateApplicationAddressForm.getSubmitterSignature())) {
            errors.rejectValue("submitterSignature", "submitter.signature.required", "Required field");
        } else if (updateApplicationAddressForm.getSubmitterSignature().length() == 1
                || signatureEndsWithMultipleSlashes(StringUtils.trim(updateApplicationAddressForm.getSubmitterSignature()))
                || signatureStartsWithMultipleSlashes(StringUtils.trim(updateApplicationAddressForm.getSubmitterSignature()))
                || !signatureStartsWithSlash(StringUtils.trim(updateApplicationAddressForm.getSubmitterSignature()))
                || !signatureEndsWithSlash(StringUtils.trim(updateApplicationAddressForm.getSubmitterSignature()))) {
            errors.rejectValue("submitterSignature", "submitter.signature.required",
                    "Invalid signature format. Expected signature format is /Signature/");
        }

        if (StringUtils.isEmpty(updateApplicationAddressForm.getContactFullName())) {
            errors.rejectValue("contactFullName", "contact.full.name.required", "Required field");
        }

        if (StringUtils.isEmpty(updateApplicationAddressForm.getContactTelephoneNoText())) {
            errors.rejectValue("contactTelephoneNoText", "contact.telephone.number.required", "Required field");
        }

        if (StringUtils.isEmpty(updateApplicationAddressForm.getContactEmailText())) {
            errors.rejectValue("contactEmailText", "contact.email.address", "Required field");
        } else if (!validEmailAddress(StringUtils.trim(updateApplicationAddressForm.getContactEmailText()))) {
            errors.rejectValue("contactEmailText", "contact.email.address", "Valid Email Address is required");
        }

        List<UpdateApplicationAddressNumbers> updateApplicationAddressNumbersList = updateApplicationAddressForm
                .getUpdateApplicationAddressNumbersArray();
        if (updateApplicationAddressNumbersList.isEmpty()) {
            errors.rejectValue("updateApplicationAddressNumbersArray", "application.address.table.required",
                    "*** At least one application number and/or patent number must be submitted ***");
        } else {
            // List is non-empty. So, scan through the list of application
            // numbers looking for duplicates
            Set<String> updateApplicationNumbersSet = new HashSet<String>();
            Set<String> updateApplicationNumberDuplicateSet = new HashSet<String>();
            for (UpdateApplicationAddressNumbers updateApplicationAddressNumbers : updateApplicationAddressNumbersList) {

                String applicationNumber = StringUtils.trim(updateApplicationAddressNumbers.getApplicationNumber()
                        .replaceAll("[/,]", "").trim());
                if (StringUtils.isNoneEmpty(applicationNumber)) {
                    if (!updateApplicationNumbersSet.add(applicationNumber)) {
                        // Add to duplicate set
                        updateApplicationNumberDuplicateSet.add(applicationNumber);
                        updateApplicationAddressNumbers.setValidationErrorMessage("<ol><li>" + applicationNumber
                                + " Duplicated</li></ol>");
                    } else {
                        updateApplicationAddressNumbers.setValidationErrorMessage("<ol></ol>");
                    }
                } else {
                    updateApplicationAddressNumbers.setValidationErrorMessage("<ol></ol>");
                }
            }

            // List is non-empty. So, scan through the list of patent numbers
            // looking for duplicates
            Set<String> updatePatentNumbersSet = new HashSet<String>();
            Set<String> updatePatentNumberDuplicateSet = new HashSet<String>();
            for (UpdateApplicationAddressNumbers updateApplicationAddressNumbers : updateApplicationAddressNumbersList) {
            	 String patentNumber = StringUtils.trim(updateApplicationAddressNumbers.getPatentNumber()).replaceAll("(,)", "");

            	 
                if (StringUtils.isNoneEmpty(patentNumber)) {
                	
                    if (!updatePatentNumbersSet.add(patentNumber)) {
                        // Add to duplicate set
                        updatePatentNumberDuplicateSet.add(patentNumber);
                        if (StringUtils.contains(updateApplicationAddressNumbers.getValidationErrorMessage(), "Duplicated")) {
                            updateApplicationAddressNumbers.setValidationErrorMessage("<ol><li>"
                                    + updateApplicationAddressNumbers.getApplicationNumber() + " and " + patentNumber
                                    + " are Duplicated.</li></ol>");
                        } else {
                            updateApplicationAddressNumbers.setValidationErrorMessage("<ol><li>" + patentNumber
                                    + " Duplicated</li></ol>");
                        }
                    } else {
                        // Don't overwrite a previous duplicate message
                        if (!StringUtils.contains(updateApplicationAddressNumbers.getValidationErrorMessage(), "Duplicated"))
                            updateApplicationAddressNumbers.setValidationErrorMessage("<ol></ol>");
                    }
                } else {
                    // Don't overwrite a previous duplicate message
                    if (!StringUtils.contains(updateApplicationAddressNumbers.getValidationErrorMessage(), "Duplicated"))
                        updateApplicationAddressNumbers.setValidationErrorMessage("<ol></ol>");
                }
            }

            if (!CollectionUtils.isEmpty(updateApplicationNumberDuplicateSet)
                    || !CollectionUtils.isEmpty(updatePatentNumberDuplicateSet)) {

                errors.rejectValue("updateApplicationAddressNumbersArray", null, updateApplicationAddressNumbersList.toArray(),
                        null);

            } else {
                // No duplicates found.
                for (UpdateApplicationAddressNumbers updateApplicationAddressNumbers : updateApplicationAddressNumbersList) {

                    // Initialize each loop through
                    // Assume the application number and the patent have not
                    // failed validation
                    applicationNumberFailsValidation = false;
                    patentNumberFailsValidation = false;

                    // Assemble all validation errors, on each row, as an
                    // unordered list
                    StringBuilder rowLevelValidationErrors = new StringBuilder();
                    rowLevelValidationErrors.append("<ol>");

                    if (isSupplementalExaminationApplicationNumber(StringUtils.trim(updateApplicationAddressNumbers
                            .getApplicationNumber()))) {

                        rowLevelValidationErrors
                                .append("<li>Sorry, Supplemental Examination proceedings are not accepted for Update Address at this time</li>");

                    } else if (isReexaminationApplicationNumber(StringUtils.trim(updateApplicationAddressNumbers
                            .getApplicationNumber()))) {

                        // 90 & 95-series (Reexamination) Application numbers
                        // are not allowed
                        rowLevelValidationErrors
                                .append("<li>Sorry, Reexamination proceedings are not accepted for Update Address at this time</li>");

                    } else if ((null!= updateApplicationAddressNumbers.getApplicationNumber() && isPctId(StringUtils.trim(updateApplicationAddressNumbers.getApplicationNumber())))
                            || (null!= updateApplicationAddressNumbers.getPatentNumber() && isPctId(StringUtils.trim(updateApplicationAddressNumbers.getPatentNumber())))) {

                        // PCT numbers in the application id and/or patent field
                        // not allowed

                        rowLevelValidationErrors
                                .append("<li>Sorry, PCT applications are not accepted for Update Address at this time</li>");

                    } else {

                        // Application id has to be numeric, valid format,
                        // correct length and be a valid application number
                        if (StringUtils.isNotEmpty(updateApplicationAddressNumbers.getApplicationNumber())) {
                            if (failsApplicationFormatCheck(updateApplicationAddressNumbers)
                                    || failsApplicationNumberLengthCheck(updateApplicationAddressNumbers)
                                    || failsApplicationPatternCheck(updateApplicationAddressNumbers)) {
                                rowLevelValidationErrors
                                        .append("<li>Incorrect application format, only 99/999,999 99/999999 99999999 accepted</li>");
                                applicationNumberFailsValidation = true;
                            } 
                            else if (failsValidateApplicationId(updateApplicationAddressNumbers)) {
                                if (updateApplicationAddressFacadeImpl
                                        .applicationNumberNotFound(removeSpecialCharacters(StringUtils
                                                .trim(updateApplicationAddressNumbers.getApplicationNumber())))) {
                                    rowLevelValidationErrors
                                           .append("<li>Sorry, the entered Application Number '"
                                                   + updateApplicationAddressNumbers.getApplicationNumber()
                                                   + "' is invalid. The number may have been incorrectly typed or is not yet in the database.</li>");
                                    applicationNumberFailsValidation = true;
                               } else {
                                   rowLevelValidationErrors.append("<li>Invalid application number</li>");
                                   applicationNumberFailsValidation = true;
                               }
                           }
                        }

                        // Patent number has to be numeric, valid format,
                        // correct length, and be a valid patent number
                        if (StringUtils.isNotEmpty(updateApplicationAddressNumbers.getPatentNumber())) {
                            if (failsValidPatentPrefixCheck(updateApplicationAddressNumbers)
                                    || failsPatentLengthCheck(updateApplicationAddressNumbers)) {
                                patentNumberFailsValidation = true;
                                rowLevelValidationErrors
                                        .append("<li>Incorrect patent format, only 9,999,999 9999999 RE99999 RE99,999 PP99,999 PP99999 D999,999 D999999 accepted</li>");
                            } 
                            else if (failsValidPatentNumberCheck(updateApplicationAddressNumbers)) {
                                if (updateApplicationAddressFacadeImpl.patentNumberNotFound(StringUtils.trim(
                                        updateApplicationAddressNumbers.getPatentNumber()).replaceAll("(,)", ""))) {
                                    patentNumberFailsValidation = true;
                                    rowLevelValidationErrors
                                            .append("<li>Sorry, the entered Patent Number '"
                                                    + updateApplicationAddressNumbers.getPatentNumber()
                                                    + "' is invalid. The number may have been incorrectly typed or is not yet in the database.</li>");
                                } else {
                                    patentNumberFailsValidation = true;
                                    rowLevelValidationErrors.append("<li>Invalid patent number</li>");
                                }
                            }
                        }

                        // Application Number and Patent Number (if both are
                        // present) must be associated
                        if (StringUtils.isNotEmpty(updateApplicationAddressNumbers.getApplicationNumber())
                                && StringUtils.isNotEmpty(updateApplicationAddressNumbers.getPatentNumber())) {
                            if (failsApplicationNumberPatentNumberAssociation(updateApplicationAddressNumbers)) {
                                rowLevelValidationErrors.append("<li>Application "
                                        + updateApplicationAddressNumbers.getApplicationNumber() + " does not match Patent "
                                        + updateApplicationAddressNumbers.getPatentNumber() + "</li>");
                                applicationNumberFailsValidation = true;
                                patentNumberFailsValidation = true;
                            }
                        }

                        // Either Correspondence and/or Maintenance Fee must be
                        // selected
                        if ("0".equals(updateApplicationAddressNumbers.getCorrespondenceAddressCheckBox())
                                && "0".equals(updateApplicationAddressNumbers.getMaintenanceFeeAddressCheckBox())) {
                            rowLevelValidationErrors.append("<li>Correspondence and/or Maintenance Fee change not selected</li>");
                        }

                        // Don't bother to run the Power of Attorney test if
                        // either the application number and/or patent number
                        // have failed validation
                       if (!applicationNumberFailsValidation && !patentNumberFailsValidation) {
                            powerOfAttorneyTest(updateApplicationAddressForm, updateApplicationAddressNumbers,
                                    rowLevelValidationErrors);
                        }
                    }

                    rowLevelValidationErrors.append("</ol>");

                    updateApplicationAddressNumbers.setValidationErrorMessage(rowLevelValidationErrors.toString());
                }

                errors.rejectValue("updateApplicationAddressNumbersArray", null, updateApplicationAddressNumbersList.toArray(),
                        null);
            }
        }

        if (StringUtils.isEmpty(updateApplicationAddressForm.getAttorneyCertification())) {
            errors.rejectValue("attorneyCertification", "attorney.Certification", "Attorney Certification must be checked");
        }
    } 

    private void powerOfAttorneyTest(UpdateApplicationAddressForm updateApplicationAddressForm,
            UpdateApplicationAddressNumbers updateApplicationAddressNumbers, StringBuilder rowLevelValidationErrors) {
        // Power of Attorney must be established for either
        // Application Number and/or Patent Number
        if (failsPowerOfAttorneyTest(updateApplicationAddressForm, updateApplicationAddressNumbers)) {
            rowLevelValidationErrors.append("<li>Power of Attorney not established</li>");
            updateApplicationAddressNumbers.setPowerOfAttorney("N");
        } else {
            updateApplicationAddressNumbers.setPowerOfAttorney("Y");
        }
    }

    private boolean failsPowerOfAttorneyTest(UpdateApplicationAddressForm updateApplicationAddressForm,
            UpdateApplicationAddressNumbers updateApplicationAddressNumbers) {
    	
    	if(null!= updateApplicationAddressNumbers.getApplicationNumber()) {
    		updateApplicationAddressNumbers.setApplicationNumber(updateApplicationAddressNumbers.getApplicationNumber().trim());
    	}
    	if(null!= updateApplicationAddressNumbers.getPatentNumber()) {
    		updateApplicationAddressNumbers.setPatentNumber(updateApplicationAddressNumbers.getPatentNumber().trim().replaceAll("(,)", ""));
    	}
        return !updateApplicationAddressFacadeImpl.validatePowerOfAttorney(updateApplicationAddressForm.getRegistrationNumber(),
                removeSpecialCharacters(updateApplicationAddressNumbers.getApplicationNumber()),
                updateApplicationAddressNumbers.getPatentNumber());
    }

    private boolean failsApplicationNumberPatentNumberAssociation(UpdateApplicationAddressNumbers updateApplicationAddressNumbers) {
        return !updateApplicationAddressFacadeImpl.validateApplicationIdIsAssociatedWithPatentId(
                removeSpecialCharacters(updateApplicationAddressNumbers.getApplicationNumber().trim()),
                updateApplicationAddressNumbers.getPatentNumber().trim().replaceAll("(,)", ""));
    }

    private boolean failsValidPatentNumberCheck(UpdateApplicationAddressNumbers updateApplicationAddressNumbers) {
        return !updateApplicationAddressFacadeImpl.validatePatentId(StringUtils.trim(
                updateApplicationAddressNumbers.getPatentNumber()).replaceAll("(,)", ""));
    }

    public boolean failsPatentLengthCheck(UpdateApplicationAddressNumbers updateApplicationAddressNumbers) {
        return StringUtils.length(StringUtils.trim(updateApplicationAddressNumbers.getPatentNumber()).replaceAll("(,)", "")) <= 6
                || StringUtils.length(StringUtils.trim(updateApplicationAddressNumbers.getPatentNumber()).replaceAll("(,)", "")) > 13;
    }

    public boolean failsValidPatentPrefixCheck(UpdateApplicationAddressNumbers updateApplicationAddressNumbers) {
        return !isValidPatentPrefix(StringUtils.trim(updateApplicationAddressNumbers.getPatentNumber()).replaceAll("(,)", ""));
    }

    public boolean failsApplicationPatternCheck(UpdateApplicationAddressNumbers updateApplicationAddressNumbers) {
        return !isValidApplicationNumberPattern_1(StringUtils.trim(updateApplicationAddressNumbers.getApplicationNumber()))
                && !isValidApplicationNumberPattern_2(StringUtils.trim(updateApplicationAddressNumbers.getApplicationNumber()))
                && !isValidApplicationNumberPattern_3(StringUtils.trim(updateApplicationAddressNumbers.getApplicationNumber()));
    }

    private boolean failsValidateApplicationId(UpdateApplicationAddressNumbers updateApplicationAddressNumbers) {
        return !updateApplicationAddressFacadeImpl.validateApplicationId(removeSpecialCharacters(StringUtils
                .trim(updateApplicationAddressNumbers.getApplicationNumber())));
    }

    public boolean failsApplicationNumberLengthCheck(UpdateApplicationAddressNumbers updateApplicationAddressNumbers) {
        return StringUtils
                .length(removeSpecialCharacters(StringUtils.trim(updateApplicationAddressNumbers.getApplicationNumber()))) <= 6
                || StringUtils.length(removeSpecialCharacters(StringUtils.trim(updateApplicationAddressNumbers
                        .getApplicationNumber()))) > 8;
    }

    public boolean failsApplicationFormatCheck(UpdateApplicationAddressNumbers updateApplicationAddressNumbers) {
        return !isNumeric(removeSpecialCharacters(StringUtils.trim(updateApplicationAddressNumbers.getApplicationNumber())));
    }

	
}
