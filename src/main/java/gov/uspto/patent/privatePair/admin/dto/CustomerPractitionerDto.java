package gov.uspto.patent.privatePair.admin.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 
 * Data Transfer Object (DTO) representing a Customer Practitioner
 * 
 */
@Data
public class CustomerPractitionerDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer fk_customer_number_request_id;
    private String registration_no;
    private String given_nm;
    private String family_nm;
    private String middle_nm;
    private String name_suffix;
    private String associated_purm_reg_no_in;
}
