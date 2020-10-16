package gov.uspto.patent.privatePair.oems.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class OEMSDTO {

    public String transactionId;
    public String documentId;
    public String validationCD;
    public String customerNO;
    public Date createdDate;
    public Date lastModifiedDate;
    public String lastModifiedUser;

}
