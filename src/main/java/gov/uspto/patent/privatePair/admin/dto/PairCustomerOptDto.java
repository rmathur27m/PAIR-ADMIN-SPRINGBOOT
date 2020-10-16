package gov.uspto.patent.privatePair.admin.dto;

import lombok.Data;

import java.util.Date;

/**
 * Data Transfer Object representing a PAIR Customer Notification Option.
 * 
 */
@Data
public class PairCustomerOptDto {

    private String customerNumber;
    private String optInIndicator;
    private String distinguishedName;
    private Date lastModifiedTs;
    private Date optDate;
    private String postCardMailOptInTransaction;
    private Date postCardMailOptDate;
    private String sequenceNumber;

}
