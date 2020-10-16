package gov.uspto.patent.privatePair.PCSEJBApp.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Data
public class CustomerVo implements Serializable {

    String pilotCustomerId;
    String customerNumber;
    String indicator;
    Date createdDate;
    Date transactionDate;
    Timestamp dBCreatedDate;
    Timestamp pilotAddedDate;
    Timestamp pilotRemovedDate;
    Timestamp lastModifyDate;
    String requestorName;
    String requestorRegNo;
    String requestorEmail;
    String requestorPhoneNumber;
    String requestorPhoneExt;
    String pocName;
    String pocRegNo;
    String pocEmail;
    String pocPhoneNumber;
    String pocPhoneExt;
    String userName;
    String lastModifyUserId;
    int lockControlNO;
}
