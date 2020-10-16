package gov.uspto.patent.privatePair.admin.domain;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * POJO for the Update Application Address HTML form.
 * 
 */

@Data
public class UpdateApplicationAddressForm extends AddressTypeForm {

    private List<UpdateApplicationAddressNumbers> updateApplicationAddressNumbersArray;
    private String commonName;
    private String registrationNumber;
    private String customerNumber;
    private String contactFullName;
    private String contactTelephoneNoText;
    private String contactEmailText;
    private String submitterSignature;
    private String pairId;
    private Date createdTs;
    private String status;
    private String savedMessage;
    private String attorneyCertification;
    private Date lastModifiedTimeStamp;
    private String updateAddressApplicationPatentTableString;
    private String dn;
}
