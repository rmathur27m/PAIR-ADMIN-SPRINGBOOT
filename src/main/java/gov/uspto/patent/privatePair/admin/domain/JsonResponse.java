package gov.uspto.patent.privatePair.admin.domain;


import lombok.Data;

/**
 * 
 * POJO to hold server responses as JSON string.
 */

@Data
public class JsonResponse {

    private String status = null;
    private Object validationResult = null;
    private String jsonResult;

}
