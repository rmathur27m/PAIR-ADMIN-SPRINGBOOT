package gov.uspto.patent.privatePair.admin.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

import gov.uspto.patent.privatePair.common.ThymeFormats;
import lombok.Data;

/**
 * Data Transfer Object representing an Update Application Address request.
 * Extends {@link UserRequestDto}
 * 
 */
@Data
public class UpdateApplicationAddressDto extends UserRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String gmt = "GMT";

    private String fKeyUserRequestId;
    private Date createTimeStamp;
    private String createUserId;
    private Integer customerNumber;
    private String application_id;
    private String patentNumber;
    private String corrAddrChaneIn;
    private String maintFeeAddrChaneIn;
    private String attrAuthIn;
    private String lastModifiedUserId;
    private Integer userId;
    private String inputSourceCt;
    private String corrAddrChangeIn;
    private String requestErrorTx;
    private String powerOfAttorneyIndicator;

}
