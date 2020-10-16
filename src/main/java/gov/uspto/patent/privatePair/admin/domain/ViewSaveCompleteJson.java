package gov.uspto.patent.privatePair.admin.domain;

import lombok.Data;

import java.util.List;

/**
 * 
 * POJO for server response where the response is stored as a JSON string.
 * 
 */
@Data
public class ViewSaveCompleteJson {

    private int iTotalRecords;
    private int iTotalDisplayRecords;
    private String sEcho;
    private String sColumns;
    private List<ViewSaveCompleteForm> aaData;
}