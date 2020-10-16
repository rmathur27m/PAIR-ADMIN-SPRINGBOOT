package gov.uspto.patent.privatePair.PCSEJBApp.dto;

public class ElectrnDocStagingTableVO {
	private String fkPcId= null;
	private String mailRmDte= null;
	private String uploadDt= null;
	private String packageId= null;
	private String documentCode= null;
	private String offset= null;
	private String documentId= null;
	private String pageCount= null;

	/**
	 * @return Returns the documentCode.
	 */
	public String getDocumentCode() {
		return documentCode;
	}
	/**
	 * @param documentCode The documentCode to set.
	 */
	public void setDocumentCode(String documentCode) {
		this.documentCode = documentCode;
	}
	/**
	 * @return Returns the fkPcId.
	 */
	public String getFkPcId() {
		return fkPcId;
	}
	/**
	 * @param fkPcId The fkPcId to set.
	 */
	public void setFkPcId(String fkPcId) {
		this.fkPcId = fkPcId;
	}
	/**
	 * @return Returns the mailRmDte.
	 */
	public String getMailRmDte() {
		return mailRmDte;
	}
	/**
	 * @param mailRmDte The mailRmDte to set.
	 */
	public void setMailRmDte(String mailRmDte) {
		this.mailRmDte = mailRmDte;
	}
	/**
	 * @return Returns the offset.
	 */
	public String getOffset() {
		return offset;
	}
	/**
	 * @param offset The offset to set.
	 */
	public void setOffset(String offset) {
		this.offset = offset;
	}
	/**
	 * @return Returns the packageId.
	 */
	public String getPackageId() {
		return packageId;
	}
	/**
	 * @param packageId The packageId to set.
	 */
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	/**
	 * @return Returns the uploadDt.
	 */
	public String getUploadDt() {
		return uploadDt;
	}
	/**
	 * @param uploadDt The uploadDt to set.
	 */
	public void setUploadDt(String uploadDt) {
		this.uploadDt = uploadDt;
	}
	/**
	 * @return Returns the documentId.
	 */
	public String getDocumentId() {
		return documentId;
	}
	/**
	 * @param documentId The documentId to set.
	 */
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}
	/**
	 * @return Returns the pageCount.
	 */
	public String getPageCount() {
		return pageCount;
	}
	/**
	 * @param pageCount The pageCount to set.
	 */
	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}
}
