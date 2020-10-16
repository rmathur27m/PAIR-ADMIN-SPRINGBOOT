package gov.uspto.patent.privatePair.admin.validator;


import gov.uspto.patent.privatePair.admin.domain.EditEntityStatus;
import gov.uspto.patent.privatePair.admin.domain.UserSignature;
import gov.uspto.patent.privatePair.common.UserType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Implementation of the Spring Framework Validator
 * 
 */

@Component
public class EditEntityStatusValidator implements Validator {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return EditEntityStatus.class.equals(aClass);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object obj, Errors errors) {
        EditEntityStatus editEntityStatusForm = (EditEntityStatus) obj;

		checkForStatusTypeSelection(editEntityStatusForm, errors);

		checkForRequiredValues(editEntityStatusForm, errors);

		checkForMissingMicroEntityStatusOptionSelection(editEntityStatusForm,
				errors);

		checkForGrossIncomeBasisCertSelection(editEntityStatusForm, errors);

		checkForInstutionOfHigherEducationBasis(editEntityStatusForm, errors);

		validateMultipleSignatureNameTable(editEntityStatusForm, errors);

		validateEmailAddressFormat(editEntityStatusForm, errors);

		if ("Registered Attorneys".equalsIgnoreCase(editEntityStatusForm
				.getUserType()))
			checkForMissingAttorneyTypeOptionSelection(editEntityStatusForm,
					errors);
		else
			checkForMissingInventorOptionSelection(editEntityStatusForm, errors);
	}

	private void checkForStatusTypeSelection(
			EditEntityStatus editEntityStatusForm, Errors errors) {
		if ((editEntityStatusForm.getStatusType() == null)
				|| (editEntityStatusForm.getStatusType().length() == 0)) {
			errors.rejectValue("statusType", "statusType.required",
					"Must select option");
		}
	}

	private void checkForRequiredValues(
			EditEntityStatus editEntityStatusForm, Errors errors) {
		if (editEntityStatusForm.getUserType().equals("Registered Attorneys")) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "regNumber",
					"regNumber.required", "Required field");
		}
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pocName",
				"pocName.required", "Required field");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phoneNumber",
				"phoneNumber.required", "Required field");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "emailAddress",
				"emailAddress.required", "Required field");
	}

	private void checkForMissingMicroEntityStatusOptionSelection(
			EditEntityStatus editEntityStatusForm, Errors errors) {
		if ((editEntityStatusForm.getStatusType() != null)
				&& (editEntityStatusForm.getStatusType()
						.equalsIgnoreCase("MicroEntityStatus"))) {
			if ((editEntityStatusForm.getMicroEntityStatusOption() == null)
					|| (editEntityStatusForm.getMicroEntityStatusOption()
							.length() == 0)) {
				errors.rejectValue("microEntityStatusOption",
						"microEntityStatusOption.required",
						"Must select option");
			}
		}
	}

	private void checkForGrossIncomeBasisCertSelection(
			EditEntityStatus editEntityStatusForm, Errors errors) {
		if ((editEntityStatusForm.getMicroEntityStatusOption() != null)
				&& (editEntityStatusForm.getMicroEntityStatusOption()
						.equalsIgnoreCase("GrossIncomeBasis"))) {
			if (!editEntityStatusForm.isGrossIncomeBasisCert()) {
				errors.rejectValue("grossIncomeBasisCert",
						"grossIncomeBasisCert.required",
						"Missing certification");
			}
		}
	}

	private void checkForInstutionOfHigherEducationBasis(
			EditEntityStatus editEntityStatusForm, Errors errors) {
		if ((editEntityStatusForm.getMicroEntityStatusOption() != null)
				&& (editEntityStatusForm.getMicroEntityStatusOption()
						.equalsIgnoreCase("InstitutionOfHigherEdBasis"))) {

			if (StringUtils.isEmpty(editEntityStatusForm
					.getInstitutionOfHigherEdBasisCert())) {
				errors.rejectValue("institutionOfHigherEdBasisCert",
						"institutionOfHigherEdBasisCert.required",
						"Missing certification");
			}
		}
	}

	private void checkForMissingAttorneyTypeOptionSelection(
			EditEntityStatus editEntityStatusForm, Errors errors) {
		// Check if user is an attorney, if so then make sure attorney type
		// option is selected
		if ((editEntityStatusForm.getAttorneyTypeOption() == null)
				|| (editEntityStatusForm.getAttorneyTypeOption().isEmpty())) {
			errors.rejectValue("attorneyTypeOption",
					"attorneyTypeOption.required", "Must select option");
		}
	}

	private void checkForMissingInventorOptionSelection(
			EditEntityStatus editEntityStatusForm, Errors errors) {
		// Check if user is a sole inventor, if so then make sure "correct"
		// option is selected
		if (StringUtils.isEmpty(editEntityStatusForm.getInventorTypeOption())) {
			errors.rejectValue("inventorTypeOption",
					"inventorTypeOption.required", "Must select option");
		}
	}

	private void validateMultipleSignatureNameTable(
			EditEntityStatus editEntityStatusForm, Errors errors) {
		List<UserSignature> userSignatures = editEntityStatusForm
				.getSignatureNameArray();
		//Declared the validation errors flag on the method scope instead of conditional block scope
		boolean validationErrors = false;
		if (userSignatures.isEmpty()) {
			errors.rejectValue("signatureNameArray",
					"multipleSignatureNameTable.required",
					"*** At least one Name/Signature pair must be submitted ***");
		} else if (editEntityStatusForm.getUserType().equalsIgnoreCase(UserType.REGISTERED_ATTORNEYS.getName())) {

				for (UserSignature userSignature : userSignatures) {

					if (StringUtils.isEmpty(userSignature.getSignature())) {
						userSignature
								.setValidationErrorMessage("Signature is required field.");
						validationErrors = true;
						if (StringUtils.isEmpty(userSignature.getName())) {
							if (userSignature.getValidationErrorMessage() != null) {
								userSignature
										.setValidationErrorMessage(userSignature.getValidationErrorMessage() + " Name is required field");
							} else {
								userSignature
										.setValidationErrorMessage("Name is required field");
							}
						}

					} else {
						if (userSignature.getSignature().length() < 3 || !(StringUtils.startsWith(userSignature.getSignature(), "/") && StringUtils.endsWith(userSignature.getSignature(), "/"))) {
							userSignature
									.setValidationErrorMessage("Invalid signature format, expected signature format is /Signature/.");
							validationErrors = true;
						}
						if (StringUtils.isEmpty(userSignature.getName())) {
							if (userSignature.getValidationErrorMessage() != null) {
								userSignature
										.setValidationErrorMessage(userSignature.getValidationErrorMessage() + " Name is required field");
							} else {
								userSignature
										.setValidationErrorMessage("Name is required field");
							}
							validationErrors = true;
						}
					}
				}
			} else {
				for (UserSignature userSignature : userSignatures) {

					if (StringUtils.isEmpty(userSignature.getSignature())) {
						userSignature
								.setValidationErrorMessage("Signature is required field.");
						validationErrors = true;
						if (StringUtils.isEmpty(userSignature.getName())) {
							if (userSignature.getValidationErrorMessage() != null) {
								userSignature
										.setValidationErrorMessage(userSignature.getValidationErrorMessage() + " Name is required field");
							} else {
								userSignature
										.setValidationErrorMessage("Name is required field");
							}
						}


					} else {
						if (userSignature.getSignature().length() < 3 || !(StringUtils.startsWith(userSignature.getSignature(), "/") && StringUtils.endsWith(userSignature.getSignature(), "/"))) {
							userSignature
									.setValidationErrorMessage("Invalid signature format, expected signature format is /Signature/.");
							validationErrors = true;
						}
						if (StringUtils.isEmpty(userSignature.getName())) {
							if (userSignature.getValidationErrorMessage() != null) {
								userSignature
										.setValidationErrorMessage(userSignature.getValidationErrorMessage() + " Name is required field");
							} else {
								userSignature
										.setValidationErrorMessage("Name is required field");
							}
							validationErrors = true;
						}
					}
				}
			}

			if (validationErrors)
				errors.rejectValue("signatureNameArray", null,
						userSignatures.toArray(), null);
		}

	private void validateEmailAddressFormat(
			EditEntityStatus editEntityStatusForm, Errors errors) {
		if (!StringUtils.isEmpty(editEntityStatusForm.getEmailAddress())) {
			Pattern pattern = Pattern.compile("^.+@.+\\..+$");
			Matcher matcher = pattern.matcher(editEntityStatusForm
					.getEmailAddress());
			if (!matcher.matches()) {
				errors.rejectValue("emailAddress",
						"emailAddress.invalidformat", "Invalid email address");
			}
		}
	}
}
