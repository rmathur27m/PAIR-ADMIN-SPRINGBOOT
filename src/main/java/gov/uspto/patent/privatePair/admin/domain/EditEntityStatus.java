package gov.uspto.patent.privatePair.admin.domain;

import lombok.Data;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 
 * POJO for the Create New EditEntity Status HTML form.
 */
@Data
public class EditEntityStatus {

    private String userType;

    // ENTITY_CHANGE_REQUEST table

    private String statusType; // PROPOSED_BUSINESS_ENTITY_CT

    private String currentBusinessEntityCode;

    private String microEntityStatusOption;

    private String attorneyTypeOption;

    private String inventorTypeOption;

    private boolean grossIncomeBasisCert;

    private String institutionOfHigherEdBasisCert;

    // USER_REQUEST table

    /*
     * Name and Signature fields here are just for the form fields. They dont
     * service any purpose for saving the data.
     */
    private String name; // SUBMITTER_FULL_NAME

    private String signature; // SUBMITTER_SIGNATURE_TX

    private String regNumber; // SUBMITTER_REGISTRATION_NO

    private String pocName; // CONTACT_FULL_NM

    private String phoneNumber; // CONTACT_TELEPHONE_NO_TX

    private String emailAddress; // CONTACT_EMAIL_TX

    private String pairId;

    private Date createdTs;

    private Date lastModifiedTs;

    private String status;

    private String applicationNumber;

    private String title;

    private String attorneyDocketNumber;

    private List<UserSignature> signatureNameArray = Collections.emptyList();
    
    private String signatureNameArrayString;

    private String savedMessage;

    private String customerNumber;
    
    private String dn;

}
