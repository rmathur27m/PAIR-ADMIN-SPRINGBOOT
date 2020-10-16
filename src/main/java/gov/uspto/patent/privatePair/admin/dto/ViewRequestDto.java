package gov.uspto.patent.privatePair.admin.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Data Transfer Object used in viewing change requests.
 * 
 */
@Data
public class ViewRequestDto implements Serializable {

    // ENTITY_CHANGE_REQUEST table

    private static final long serialVersionUID = 6600441390455452923L;

    private String statusChangeType = "";

    private String customerNumber = "";

    private String docketNumber = "";

    private String appNumber = "";

    private long pairId;

    private Date lastUpdatedTs;

    private String status;

    // Update Address Table

    private long updateAddrPairId;

    private Date updateAddrTs;

    private String updateAddrStatus = "";

    private String updateAddrCN = "";

    // Customer Number Address Table

    private long customerPairId;

    private Date customerTs;

    private String customerStatus = "";

    private String newCustomerNumber = "";

    private String customerName = "";

    private String customerAddressLine = "";

    private String customerCity = "";

    private String customerState = "";

    private String customerPostalCode = "";
}
