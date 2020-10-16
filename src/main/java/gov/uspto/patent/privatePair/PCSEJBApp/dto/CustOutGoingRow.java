package gov.uspto.patent.privatePair.PCSEJBApp.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
@Data
public class CustOutGoingRow implements Serializable {
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	private String appNumber;
	private String patentNo; 
	private String mailRoomDate;
	private String docID;
	private String docDescription;
	private String startPageNum;
	private String pageCount;
	private String offSet; 
	private String attyDocNum; 
	private String uploadDate;
	private String docViewDate;
	private String custNum;
	private String documentCategory;
	private String docCode;
	private Timestamp lastModifiedDt;
	private Timestamp filingDate;
	private Timestamp publicationDate;
	private String publicationKindCode;
	private String publicationSeqNo;
	private String publicationYear;
	private String statusGrpCd;
	private String objectId;
	//Added by dthakkar for DR # 33 - Add Docketed Check Box on OCN Tab
	private String docketed;
	private String daId;
	private String docViewBy; //MM DR 24


	public CustOutGoingRow() {}

	public CustOutGoingRow(String appNumber,
							String patentNo,
							String mailRoomDate,
							String docID, 				//package_id is stored here
							String docDescription,
							String startPageNum,
							String pageCount,
							String offSet,
							String attyDocNum,
							String uploadDate,
							String docViewDate,
							String custNum,
							String documentCategory,
							String docCode,
							Timestamp lastModifiedDt,
							Timestamp filingDate,
							Timestamp publicationDate,
							String publicationKindCode,
							String publicationSeqNo,
							String publicationYear,
							String statusGrpCd,
							String _objectId,
							String docketed,
							String daId,	//DR 33
							String docViewBy) //For DR # 24
{
			this.appNumber = appNumber;
			this.patentNo = patentNo;
			this.mailRoomDate = mailRoomDate;
			this.docID =docID;
			this.docDescription = docDescription;
			this.startPageNum = startPageNum;
			this.pageCount =pageCount;
			this.offSet = offSet;
			this.attyDocNum = attyDocNum;
			this.uploadDate = uploadDate;
			this.docViewDate = docViewDate;
			this.custNum = custNum;
			this.documentCategory = documentCategory;
			this.docCode = docCode;
			this.lastModifiedDt = lastModifiedDt;
			this.filingDate = filingDate;
			this.publicationDate = publicationDate;
			this.publicationKindCode = publicationKindCode;
			this.publicationSeqNo = publicationSeqNo;
			this.publicationYear = publicationYear;
			this.statusGrpCd = statusGrpCd;
			this.docketed = docketed;
			this.daId = daId;
			this.docViewBy = docViewBy;
			objectId = _objectId ;

}

}
