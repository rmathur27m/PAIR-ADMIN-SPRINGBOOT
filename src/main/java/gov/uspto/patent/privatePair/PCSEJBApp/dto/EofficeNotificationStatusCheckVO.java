package gov.uspto.patent.privatePair.PCSEJBApp.dto;

import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;
@Data
public class EofficeNotificationStatusCheckVO {

	
	String applID;
	int customerNumber;
	Timestamp createTs;
	Date mailRoomDt;
	String pageCnt;
	String docId;
	String packId;
	String procesStatusCode;
	Date processTs;
	String prcsReasonCd;
	String prcsRetunCd;
	Date imageUploadDt;
	String actionStatusCd;
	String fkPcId;
	Date reviewDt;
	String lastModify;
	Timestamp lastMoifyTs;
	
	String errorMesage;
}
