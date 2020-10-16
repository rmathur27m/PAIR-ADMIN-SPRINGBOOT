package gov.uspto.patent.privatePair.PCSEJBApp.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class OCNArchiveSearchResultVO {

    String appId = null;
    String attyDktNo = null;
    String docCat = null;
    String docCd = null;
    String docDesc = null;
    String docId = null;
    String docViewBy = null;//MM DR 24 May 2012 Added User Name to Customer Correspondence
    Date docViewDt = null;
    Date lstModDt = null;
    Date mailRoomDt = null;
    Date patIssDt = null;
    String patNum = null;
    String postCardNotfctnIn = null;
    Date uploadDt = null;
    int custNum = 0;

}
