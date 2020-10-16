package gov.uspto.patent.privatePair.admin.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Data Transfer Object representing a User Signature.
 * 
 */
@Data
public class UserSignatureDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fkUserRequestId;
    private String userSignatureTx;
    private String userSignatureNmTx;

}
