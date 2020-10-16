package gov.uspto.patent.privatePair.admin.service.entitystatus;

import gov.uspto.patent.privatePair.admin.domain.EditEntityStatus;
import gov.uspto.patent.privatePair.admin.domain.UserSignature;
import gov.uspto.patent.privatePair.admin.validator.EditEntityStatusValidator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class EditEntityStatusFormTest {

    private EditEntityStatusValidator validator ;

    private BindException errors = null;

    private EditEntityStatus editEntityStatus = null;

    @Before
    public void setUp(){
        validator = new EditEntityStatusValidator();
        editEntityStatus = new EditEntityStatus();
        editEntityStatus.setEmailAddress("test@test.com");
        List<UserSignature> userSignatures = new ArrayList<UserSignature>();
        UserSignature userSignature = new UserSignature();
        userSignature.setName("Test User");
        userSignature.setSignature("/Test Signature/");
        userSignatures.add(userSignature);

        editEntityStatus.setSignatureNameArray(userSignatures);
        errors = new BindException(editEntityStatus, "editEntityStatus");
    }

    @Test
    public void testMissingStatusTypeSelection() {

        // Set user type manually
        editEntityStatus.setUserType("Registered Attorneys");

        // Invoke validation
        ValidationUtils.invokeValidator(validator, editEntityStatus, errors);

        // Assert expected results
        assertTrue(errors.hasFieldErrors("statusType"));
    }

    /**
     * This test case is specific to "Registered Attorney" user type
     */
    @Test
    public void testMissingAttorneyTypSelection() {
        // Set user type manually
        editEntityStatus.setUserType("Registered Attorneys");

        // Set signature field
        editEntityStatus.setSignature("/Peter Kauslick/");

        // Set name field
        editEntityStatus.setName("Peter T. Kauslick");

        // Set registration number
        editEntityStatus.setRegNumber("123456");

        // Invoke validation
        ValidationUtils.invokeValidator(validator, editEntityStatus, errors);

        // Assert expected results
        /*
         * assertFalse(errors.hasFieldErrors("signature"));
         * assertFalse(errors.hasFieldErrors("name"));
         */
        assertFalse(errors.hasFieldErrors("regNumber"));
        assertTrue(errors.hasFieldErrors("attorneyTypeOption"));
    }

    /**
     * This test case is specific to "Registered Attorney" user type
     */
    @Test
    public void testMissingRequiredSignature() {
        // Set user type manually
        editEntityStatus.setUserType("Registered Attorneys");

        // Set attorney Type option
        editEntityStatus.setAttorneyTypeOption("POA");

        // Invoke validation
        ValidationUtils.invokeValidator(validator, editEntityStatus, errors);

        // Assert expected results
        assertFalse(errors.hasFieldErrors("attorneyTypeOption"));
    }

    /**
     * This test case is specific to "Registered Attorney" user type
     */
    @Test
    public void testMissingRequiredName() {
        // Set user type manually
        editEntityStatus.setUserType("Registered Attorneys");

        // Set attorney Type option
        editEntityStatus.setAttorneyTypeOption("Representative");

        // Invoke validation
        ValidationUtils.invokeValidator(validator, editEntityStatus, errors);

        assertFalse(errors.hasFieldErrors("attorneyTypeOption"));
    }

    /**
     * This test case is specific to "Registered Attorney" user type
     */
    @Test
    public void testMissingRequiredRegistrationNumber() {
        // Set user type manually
        editEntityStatus.setUserType("Registered Attorneys");

        // Set attorney Type option
        editEntityStatus.setAttorneyTypeOption("Representative");

        // INvoke validation
        ValidationUtils.invokeValidator(validator, editEntityStatus, errors);

        assertEquals(errors.hasFieldErrors("regNumber"), true);
        assertFalse(errors.hasFieldErrors("attorneyTypeOption"));
    }

    @Test
    public void testMissingRequiredPocName() {
        // Set user type manually
        editEntityStatus.setUserType("Independent Inventor");

        ValidationUtils.invokeValidator(validator, editEntityStatus, errors);
        assertEquals(errors.hasFieldErrors("pocName"), true);
    }

    @Test
    public void testMissingRequiredPhoneNumber() {
        // Set user type manually
        editEntityStatus.setUserType("Registered Attorneys");

        ValidationUtils.invokeValidator(validator, editEntityStatus, errors);
        assertEquals(errors.hasFieldErrors("phoneNumber"), true);
    }

    @Test
    public void MissingInventorTypeSelection() {
        // Set user type manually
        editEntityStatus.setUserType("Independent Inventor");

        // Invoke validation
        ValidationUtils.invokeValidator(validator, editEntityStatus, errors);

        // Assert expected results
        assertEquals(errors.hasFieldErrors("inventorTypeOption"), true);
    }
}
