/**
 * 
 */
package gov.uspto.patent.privatePair.admin.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author skosuri1
 * 
 */
@Data
public class CustomerCorrAddressDto {

    private String fKeyUserRequestId;
    private Integer customerNumber;
    private Integer customerCorrAddressId;
    private Date timeStamp;
    private String firmorIndividualNameLine1;
    private String firmorIndividualNameLine2;
    private String addressLine1;
    private String addressLine2;
    private String state;
    private String city;
    private String zip;
    private String country;

}
