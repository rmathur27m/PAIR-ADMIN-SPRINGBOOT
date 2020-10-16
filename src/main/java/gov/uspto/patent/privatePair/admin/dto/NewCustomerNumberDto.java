package gov.uspto.patent.privatePair.admin.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * This is a Base Class.
 */

@Data
public class NewCustomerNumberDto extends UserRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fKeyUserRequestId;
    private Integer customerNumber;
    private Integer customerNumberRequestId;
    private Date timeStamp;
    private String firmorIndividualNameLine1;
    private String firmorIndividualNameLine2;
    private String addressLine1;
    private String addressLine2;
    private String state;
    private String city;
    private String zip;
    private String country;
    private String telephone1;
    private String telephone2;
    private String telephone3;
    private String eMail1;
    private String eMail2;
    private String eMail3;
    private String fax1;
    private String fax2;
    private String isAssociateMyPNumber;
    private String outgoingCorrespondence;
}
