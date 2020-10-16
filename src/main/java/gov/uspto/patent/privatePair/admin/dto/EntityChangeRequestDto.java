package gov.uspto.patent.privatePair.admin.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Data Transfer Object (DTO) representing an Entity Change Request.
 * 
 */
@Data
public class EntityChangeRequestDto extends UserRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String application_id;
    private String curBusEntityCode;
    private String propBusEntityCode;
    private String fKeyUserRequestId;
    private String microEntityTypeCode;
    private String microEntityGrossCert;
    private String microEntityInstCert;
    private String attorneyDocketNumber;
    private String title;
    private Integer customerNumber;
}
