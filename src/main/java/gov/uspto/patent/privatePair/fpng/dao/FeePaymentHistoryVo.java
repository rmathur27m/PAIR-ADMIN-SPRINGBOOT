package gov.uspto.patent.privatePair.fpng.dao;

import lombok.Data;

import java.io.Serializable;

/**
 * Data holding class for fee history records.
 * 
 * @author sbandi
 */
@SuppressWarnings("serial")
@Data
public class FeePaymentHistoryVo implements Serializable {

    String accountingDate;
    String feeCode;
    Integer feeAmount;
    Integer feequantity;
    Integer totalAmount;
    String mailRoomDate;
    String paymentMethod;
}
