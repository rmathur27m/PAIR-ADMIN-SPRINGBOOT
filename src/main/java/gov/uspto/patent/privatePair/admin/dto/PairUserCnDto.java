package gov.uspto.patent.privatePair.admin.dto;

import lombok.Data;

import java.util.Date;

/**
 * Data Transfer Object (DTO) representing a new Customer Number request.
 * 
 */
@Data
public class PairUserCnDto {

    private String userId;
    private String custNum;
    private String sysAdminId;
    private Date insDate;
    private Date updDate;
    private String deleteFlag;
    private String pairUserDnId;
}
