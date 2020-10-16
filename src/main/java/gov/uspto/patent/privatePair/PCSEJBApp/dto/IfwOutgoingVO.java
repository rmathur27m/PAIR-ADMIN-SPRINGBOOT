package gov.uspto.patent.privatePair.PCSEJBApp.dto;

import java.sql.Timestamp;

public class IfwOutgoingVO {
	
	
	

	
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
	private String lastModifiedDt;
	private String filingDate;
	private long publicationDate;
	private String publicationKindCode;
	private String publicationSeqNo;
	private String publicationYear;
	private String statusGrpCd;
	private String objectId;
	private String docketed;
	private String daId;
	private String docViewBy;
	private boolean isSelectedVal;
	private boolean isSelectedDocketVal;
	private String mailRoomTemPDate;
	private long correspondingCount;
public IfwOutgoingVO(String appNumber,
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
			String lastModifiedDt,
			String filingDate,
			long publicationDate,
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
	public IfwOutgoingVO() {
	super();
}
	public String getAppNumber() {
		return appNumber;
	}
	public void setAppNumber(String appNumber) {
		this.appNumber = appNumber;
	}
	public String getPatentNo() {
		return patentNo;
	}
	public void setPatentNo(String patentNo) {
		this.patentNo = patentNo;
	}
	public String getMailRoomDate() {
		return mailRoomDate;
	}
	public void setMailRoomDate(String mailRoomDate) {
		this.mailRoomDate = mailRoomDate;
	}
	public String getDocID() {
		return docID;
	}
	public void setDocID(String docID) {
		this.docID = docID;
	}
	public String getDocDescription() {
		return docDescription;
	}
	public void setDocDescription(String docDescription) {
		this.docDescription = docDescription;
	}
	public String getStartPageNum() {
		return startPageNum;
	}
	public void setStartPageNum(String startPageNum) {
		this.startPageNum = startPageNum;
	}
	public String getPageCount() {
		return pageCount;
	}
	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}
	public String getOffSet() {
		return offSet;
	}
	public void setOffSet(String offSet) {
		this.offSet = offSet;
	}
	public String getAttyDocNum() {
		return attyDocNum;
	}
	public void setAttyDocNum(String attyDocNum) {
		this.attyDocNum = attyDocNum;
	}
	public String getUploadDate() {
		return uploadDate;
	}
	public void setUploadDate(String uploadDate) {
		this.uploadDate = uploadDate;
	}
	public String getDocViewDate() {
		return docViewDate;
	}
	public void setDocViewDate(String docViewDate) {
		this.docViewDate = docViewDate;
	}
	public String getCustNum() {
		return custNum;
	}
	public void setCustNum(String custNum) {
		this.custNum = custNum;
	}
	public String getDocumentCategory() {
		return documentCategory;
	}
	public void setDocumentCategory(String documentCategory) {
		this.documentCategory = documentCategory;
	}
	public String getDocCode() {
		return docCode;
	}
	public void setDocCode(String docCode) {
		this.docCode = docCode;
	}
	public String getLastModifiedDt() {
		return lastModifiedDt;
	}
	public void setLastModifiedDt(String lastModifiedDt) {
		this.lastModifiedDt = lastModifiedDt;
	}
	public String getFilingDate() {
		return filingDate;
	}
	public void setFilingDate(String filingDate) {
		this.filingDate = filingDate;
	}
	public long getPublicationDate() {
		return publicationDate;
	}
	public void setPublicationDate(long publicationDate) {
		this.publicationDate = publicationDate;
	}
	public String getPublicationKindCode() {
		return publicationKindCode;
	}
	public void setPublicationKindCode(String publicationKindCode) {
		this.publicationKindCode = publicationKindCode;
	}
	public String getPublicationSeqNo() {
		return publicationSeqNo;
	}
	public void setPublicationSeqNo(String publicationSeqNo) {
		this.publicationSeqNo = publicationSeqNo;
	}
	public String getPublicationYear() {
		return publicationYear;
	}
	public void setPublicationYear(String publicationYear) {
		this.publicationYear = publicationYear;
	}
	public String getStatusGrpCd() {
		return statusGrpCd;
	}
	public void setStatusGrpCd(String statusGrpCd) {
		this.statusGrpCd = statusGrpCd;
	}
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public String getDocketed() {
		return docketed;
	}
	public void setDocketed(String docketed) {
		this.docketed = docketed;
	}
	public String getDaId() {
		return daId;
	}
	public void setDaId(String daId) {
		this.daId = daId;
	}
	public String getDocViewBy() {
		return docViewBy;
	}
	public void setDocViewBy(String docViewBy) {
		this.docViewBy = docViewBy;
	}
	public boolean isSelectedVal() {
		return isSelectedVal;
	}
	public void setSelectedVal(boolean isSelectedVal) {
		this.isSelectedVal = isSelectedVal;
	}
	public boolean isSelectedDocketVal() {
		return isSelectedDocketVal;
	}
	public void setSelectedDocketVal(boolean isSelectedDocketVal) {
		this.isSelectedDocketVal = isSelectedDocketVal;
	}
	public String getMailRoomTemPDate() {
		return mailRoomTemPDate;
	}
	public void setMailRoomTemPDate(String mailRoomTemPDate) {
		this.mailRoomTemPDate = mailRoomTemPDate;
	}
	public long getCorrespondingCount() {
		return correspondingCount;
	}
	public void setCorrespondingCount(long correspondingCount) {
		this.correspondingCount = correspondingCount;
	}
	
}
