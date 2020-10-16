package gov.uspto.patent.privatePair.admin.domain;

import lombok.Data;

import java.util.List;


/**
 * POJO for the Address Type HTML form.
 * 
 */

@Data
public class AddressTypeForm {

    private String nameLineOne;
    private String nameLineTwo;
    private String streetLineOne;
    private String streetLineTwo;
    private String cityName;
    private String countryCode;
    private String countryName;
    private String postalCode;
    private String state;

}
