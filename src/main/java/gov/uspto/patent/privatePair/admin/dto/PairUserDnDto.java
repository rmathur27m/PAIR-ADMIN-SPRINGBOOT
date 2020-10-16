package gov.uspto.patent.privatePair.admin.dto;

import lombok.Data;

import java.util.Date;

/**
 * Data Transfer Object (DTO) representing a PAIR User.
 * 
 */
@Data
public class PairUserDnDto {

    private String userId;
    private String username;
    private String password;
    private String sysAdminId;
    private Date insDate;
    private Date updDate;
    private String deleteFlag;
    private String accessFlag;
    private String dn;
    private String commonName;
    private String pairUserDnId;
}
