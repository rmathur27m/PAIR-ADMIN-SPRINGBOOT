package gov.uspto.patent.privatePair.PCSCommon.dto;

import lombok.Data;

@Data
public class UserDTO implements java.io.Serializable {


	private String userId;
	private String userCommonName;
	private String userType;
    private String userDn;
    private String userStatus;
    private String userGivenName;
    private String userMiddleName;
    private String userFamilyName;
    private String userNameSuffix;

}
