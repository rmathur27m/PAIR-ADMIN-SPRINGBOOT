package gov.uspto.patent.privatePair.admin.service.updateapplicationaddress;

import gov.uspto.patent.privatePair.admin.domain.UpdateApplicationAddressForm;
import gov.uspto.patent.privatePair.admin.domain.UpdateApplicationAddressNumbers;
import gov.uspto.patent.privatePair.admin.validator.UpdateApplicationAddressValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ValidationUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class UpdateApplicationAddressFormTest {

    private UpdateApplicationAddressValidator updateApplicationAddressValidator;

    private BindException errors = null;

    private UpdateApplicationAddressForm updateApplicationAddressForm = null;

    @Before
    public void setup() {
        updateApplicationAddressValidator = new UpdateApplicationAddressValidator();
        updateApplicationAddressForm = new UpdateApplicationAddressForm();
        errors = new BindException(updateApplicationAddressForm, "updateApplicationAddressForm");
    }

    @Test
    public void testInvalidCustomerNumberThrowsException() {
        // Setup the request body
        UpdateApplicationAddressForm updateApplicationAddressForm = new UpdateApplicationAddressForm();

        UpdateApplicationAddressNumbers updateApplicationAddressNumbers = new UpdateApplicationAddressNumbers();
        updateApplicationAddressNumbers.setApplicationNumber("99/999,999");
        updateApplicationAddressNumbers.setPatentNumber("0763136");
        updateApplicationAddressNumbers.setCorrespondenceAddressCheckBox("0");
        updateApplicationAddressNumbers.setMaintenanceFeeAddressCheckBox("1");

        List<UpdateApplicationAddressNumbers> updateApplicationAddressNumbersArray = new ArrayList<UpdateApplicationAddressNumbers>();
        updateApplicationAddressNumbersArray.add(updateApplicationAddressNumbers);

        updateApplicationAddressForm.setUpdateApplicationAddressNumbersArray(updateApplicationAddressNumbersArray);

        updateApplicationAddressForm.setCommonName("James John Dottavio");
        updateApplicationAddressForm.setRegistrationNumber("040360");
        updateApplicationAddressForm.setContactFullName("Point of Contact");
        updateApplicationAddressForm.setContactTelephoneNoText("657-345-0987");
        updateApplicationAddressForm.setSubmitterSignature("/Test Signature/");
        updateApplicationAddressForm.setCustomerNumber("invalid customer number");

        // Invoke validation
        ValidationUtils.invokeValidator(updateApplicationAddressValidator, updateApplicationAddressForm, errors);

        // Assert expected results
        List<FieldError> fieldErrors = errors.getFieldErrors("customerNumber");
        for (FieldError fieldError : fieldErrors) {
            assertEquals("Invalid Cust. Number", fieldError.getDefaultMessage());
        }
    }

    @Test
    public void testPreviewUpdateApplicationAddressRequestWithoutRequiredFieldsThrowsValidationError() {
        // Setup the request body
        UpdateApplicationAddressForm updateApplicationAddressForm = new UpdateApplicationAddressForm();

        UpdateApplicationAddressNumbers updateApplicationAddressNumbers = new UpdateApplicationAddressNumbers();
        updateApplicationAddressNumbers.setApplicationNumber("12162013");
        updateApplicationAddressNumbers.setPatentNumber("8101234");
        updateApplicationAddressNumbers.setCorrespondenceAddressCheckBox("0");
        updateApplicationAddressNumbers.setMaintenanceFeeAddressCheckBox("0");

        List<UpdateApplicationAddressNumbers> updateApplicationAddressNumbersArray = new ArrayList<>();
        updateApplicationAddressNumbersArray.add(updateApplicationAddressNumbers);

        updateApplicationAddressForm.setUpdateApplicationAddressNumbersArray(updateApplicationAddressNumbersArray);

        updateApplicationAddressForm.setCommonName("James John Dottavio");
        updateApplicationAddressForm.setRegistrationNumber("R040360");
        updateApplicationAddressForm.setContactFullName(null);
        updateApplicationAddressForm.setContactTelephoneNoText(null);
        updateApplicationAddressForm.setSubmitterSignature(null);
        updateApplicationAddressForm.setCustomerNumber(null);

        // Invoke validation
        ValidationUtils.invokeValidator(updateApplicationAddressValidator, updateApplicationAddressForm, errors);

        // Assert expected results
        assertTrue(errors.hasFieldErrors());

        List<FieldError> fieldErrors = errors.getFieldErrors();
        assertTrue(fieldErrors.size() > 0);
        for (FieldError fieldError : fieldErrors) {

            String defaultMessage = fieldError.getDefaultMessage();
            String field = fieldError.getField();

            if ("customerNumber".equals(field)) {
                assertEquals("Expected default message: 'Missing Cust. Number'; Actual: " + defaultMessage, "Missing Cust. Number", defaultMessage);
            }

            if ("submitterSignature".equals(field)) {
                assertEquals("Expected default message: 'Required field'; Actual: " + defaultMessage, "Required field", defaultMessage);
            }

            if ("dateSigned".equals(field)) {
                assertEquals("Expected default message: 'Date not entered DD-MM-YYYY'; Actual: " + defaultMessage, "Date not entered DD-MM-YYYY", defaultMessage);
            }

            if ("contactFullName".equals(field)) {
                assertEquals("Expected default message: 'Required field'; Actual: " + defaultMessage, "Required field", defaultMessage);
            }

            if ("contactTelephoneNoText".equals(field)) {
                assertEquals("Expected default message: 'Required field'; Actual: " + defaultMessage, "Required field", defaultMessage);
            }
        }

        FieldError fieldError = errors.getFieldError("updateApplicationAddressNumbersArray");
        Object[] objects = new Object[0];
        if (fieldError != null) {
            objects = fieldError.getArguments();
        }
        if (objects != null) {
            assertEquals(1, objects.length);
        }
        assert objects != null;
        UpdateApplicationAddressNumbers updateApplicationNumbers = (UpdateApplicationAddressNumbers) objects[0];
        assertNotNull(updateApplicationNumbers);
        assertEquals("Validation error message expected: '<ol><li>Correspondence and/or Maintenance Fee change not selected</li></ol>'; Actual: '"
                + updateApplicationNumbers.getValidationErrorMessage() + "'", "<ol><li>Correspondence and/or Maintenance Fee change not selected</li></ol>", updateApplicationNumbers
                .getValidationErrorMessage());
    }

    @Test
    public void testPreviewUpdateApplicationAddressRequestWithoutSelectingAddressBoxThrowsValidationError() {
        // Setup the request body
        UpdateApplicationAddressForm updateApplicationAddressForm = new UpdateApplicationAddressForm();

        UpdateApplicationAddressNumbers updateApplicationAddressNumbers = new UpdateApplicationAddressNumbers();
        updateApplicationAddressNumbers.setApplicationNumber("12162013");
        updateApplicationAddressNumbers.setPatentNumber("8101234");
        updateApplicationAddressNumbers.setCorrespondenceAddressCheckBox("0");
        updateApplicationAddressNumbers.setMaintenanceFeeAddressCheckBox("0");

        List<UpdateApplicationAddressNumbers> updateApplicationAddressNumbersArray = new ArrayList<UpdateApplicationAddressNumbers>();
        updateApplicationAddressNumbersArray.add(updateApplicationAddressNumbers);

        updateApplicationAddressForm.setUpdateApplicationAddressNumbersArray(updateApplicationAddressNumbersArray);

        updateApplicationAddressForm.setCommonName("James John Dottavio");
        updateApplicationAddressForm.setRegistrationNumber("040360");
        updateApplicationAddressForm.setContactFullName("Point of Contact");
        updateApplicationAddressForm.setContactTelephoneNoText("657-345-0987");
        updateApplicationAddressForm.setSubmitterSignature("/Test Signature/");

        // Invoke validation
        ValidationUtils.invokeValidator(updateApplicationAddressValidator, updateApplicationAddressForm, errors);

        // Assert expected results
        assertTrue(errors.hasFieldErrors("updateApplicationAddressNumbersArray"));

        FieldError fieldError = errors.getFieldError("updateApplicationAddressNumbersArray");
        Object[] objects = new Object[0];
        if (fieldError != null) {
            objects = fieldError.getArguments();
        }
        assert objects != null;
        assertEquals(1, objects.length);
        UpdateApplicationAddressNumbers updateApplicationNumbers = (UpdateApplicationAddressNumbers) objects[0];
        assertNotNull(updateApplicationNumbers);
        assertEquals("<ol><li>Correspondence and/or Maintenance Fee change not selected</li></ol>", "<ol><li>Correspondence and/or Maintenance Fee change not selected</li></ol>", updateApplicationNumbers.getValidationErrorMessage());
    }

    @Test
    public void testValidApplicationNumberFormat_1() {

        // Setup the request body
        UpdateApplicationAddressForm updateApplicationAddressForm = new UpdateApplicationAddressForm();

        UpdateApplicationAddressNumbers updateApplicationAddressNumbers = new UpdateApplicationAddressNumbers();
        updateApplicationAddressNumbers.setApplicationNumber("99/999,999");
        updateApplicationAddressNumbers.setPatentNumber("0763136");
        updateApplicationAddressNumbers.setCorrespondenceAddressCheckBox("0");
        updateApplicationAddressNumbers.setMaintenanceFeeAddressCheckBox("1");

        List<UpdateApplicationAddressNumbers> updateApplicationAddressNumbersArray = new ArrayList<UpdateApplicationAddressNumbers>();
        updateApplicationAddressNumbersArray.add(updateApplicationAddressNumbers);

        updateApplicationAddressForm.setUpdateApplicationAddressNumbersArray(updateApplicationAddressNumbersArray);

        updateApplicationAddressForm.setCommonName("James John Dottavio");
        updateApplicationAddressForm.setRegistrationNumber("040360");
        updateApplicationAddressForm.setContactFullName("Point of Contact");
        updateApplicationAddressForm.setContactTelephoneNoText("657-345-0987");
        updateApplicationAddressForm.setSubmitterSignature("/Test Signature/");

        // Invoke validation
        ValidationUtils.invokeValidator(updateApplicationAddressValidator, updateApplicationAddressForm, errors);

        // Assert expected results
        List<FieldError> fieldErrors = errors.getFieldErrors("updateApplicationAddressNumbersArray");
        for (FieldError fieldError : fieldErrors) {
            assertNull(fieldError.getRejectedValue());
        }
    }

    @Test
    public void testValidApplicationNumberFormat_2() {

        // Setup the request body
        UpdateApplicationAddressForm updateApplicationAddressForm = new UpdateApplicationAddressForm();

        UpdateApplicationAddressNumbers updateApplicationAddressNumbers = new UpdateApplicationAddressNumbers();
        updateApplicationAddressNumbers.setApplicationNumber("99/999999");
        updateApplicationAddressNumbers.setPatentNumber("0763136");
        updateApplicationAddressNumbers.setCorrespondenceAddressCheckBox("0");
        updateApplicationAddressNumbers.setMaintenanceFeeAddressCheckBox("1");

        List<UpdateApplicationAddressNumbers> updateApplicationAddressNumbersArray = new ArrayList<UpdateApplicationAddressNumbers>();
        updateApplicationAddressNumbersArray.add(updateApplicationAddressNumbers);

        updateApplicationAddressForm.setUpdateApplicationAddressNumbersArray(updateApplicationAddressNumbersArray);

        updateApplicationAddressForm.setCommonName("James John Dottavio");
        updateApplicationAddressForm.setRegistrationNumber("040360");
        updateApplicationAddressForm.setContactFullName("Point of Contact");
        updateApplicationAddressForm.setContactTelephoneNoText("657-345-0987");
        updateApplicationAddressForm.setSubmitterSignature("/Test Signature/");

        // Invoke validation
        ValidationUtils.invokeValidator(updateApplicationAddressValidator, updateApplicationAddressForm, errors);

        // Assert expected results
        List<FieldError> fieldErrors = errors.getFieldErrors("updateApplicationAddressNumbersArray");
        for (FieldError fieldError : fieldErrors) {
            assertNull(fieldError.getRejectedValue());
        }
    }

    @Test
    public void testValidApplicationNumberFormat_3() {

        // Setup the request body
        UpdateApplicationAddressForm updateApplicationAddressForm = new UpdateApplicationAddressForm();

        UpdateApplicationAddressNumbers updateApplicationAddressNumbers = new UpdateApplicationAddressNumbers();
        updateApplicationAddressNumbers.setApplicationNumber("99999999");
        updateApplicationAddressNumbers.setPatentNumber("0763136");
        updateApplicationAddressNumbers.setCorrespondenceAddressCheckBox("0");
        updateApplicationAddressNumbers.setMaintenanceFeeAddressCheckBox("1");

        List<UpdateApplicationAddressNumbers> updateApplicationAddressNumbersArray = new ArrayList<UpdateApplicationAddressNumbers>();
        updateApplicationAddressNumbersArray.add(updateApplicationAddressNumbers);

        updateApplicationAddressForm.setUpdateApplicationAddressNumbersArray(updateApplicationAddressNumbersArray);

        updateApplicationAddressForm.setCommonName("James John Dottavio");
        updateApplicationAddressForm.setRegistrationNumber("040360");
        updateApplicationAddressForm.setContactFullName("Point of Contact");
        updateApplicationAddressForm.setContactTelephoneNoText("657-345-0987");
        updateApplicationAddressForm.setSubmitterSignature("/Test Signature/");

        // Invoke validation
        ValidationUtils.invokeValidator(updateApplicationAddressValidator, updateApplicationAddressForm, errors);

        // Assert expected results
        List<FieldError> fieldErrors = errors.getFieldErrors("updateApplicationAddressNumbersArray");
        for (FieldError fieldError : fieldErrors) {
            assertNull(fieldError.getRejectedValue());
        }
    }


}
