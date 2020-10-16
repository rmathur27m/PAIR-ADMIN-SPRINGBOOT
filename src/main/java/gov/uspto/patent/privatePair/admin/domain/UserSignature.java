package gov.uspto.patent.privatePair.admin.domain;

import lombok.Data;

/**
 * DTO for User Signature object.
 * 
 */
@Data
public class UserSignature {

    private String name; // SUBMITTER_FULL_NAME

    private String signature; // SUBMITTER_SIGNATURE_TX

    private String validationErrorMessage;
}
