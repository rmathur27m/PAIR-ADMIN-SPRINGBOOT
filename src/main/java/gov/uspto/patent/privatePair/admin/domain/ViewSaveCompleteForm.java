package gov.uspto.patent.privatePair.admin.domain;

import lombok.Data;

import java.util.Date;

/**
 * POJO for View Save Complete HTML Form.
 * 
 */
@Data
public class ViewSaveCompleteForm {

    // ENTITY_CHANGE_REQUEST table

    private String statusChangeType;

    private String customerNumber;

    private String docketNumber;

    private String appNumber;

    private String pairId;

    private Date lastUpdatedTs;

    private String status;

    // Update Address Table

    private long updateAddrPairId;

    private Date updateAddrTs;

    private String updateAddrStatus;

    private String updateAddrCN;

    // Customer Number Address Table

    private long customerPairId;

    private Date customerTs;

    private String customerAddress;

    private String customerStatus;

    private String newCustomerNumber;

}
