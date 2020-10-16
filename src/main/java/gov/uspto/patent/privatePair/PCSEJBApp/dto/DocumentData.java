package gov.uspto.patent.privatePair.PCSEJBApp.dto;

public class DocumentData {
    private String DocumentCategory;
    private String DocumentDescription;
    private String FileWrapperDocumentCode;
    private String MailRoomDate;
    private String UploadDate;
    private int BeginPageNumber;
    private int PageQuantity;
    private int PageOffsetQuantity;
    private String earliestViewDate;
    private String earliestViewBy;
    //private String EarliestViewBy;

    public String getDocumentCategory() {
        return DocumentCategory;
    }

    public void setDocumentCategory(String documentCategory) {
        DocumentCategory = documentCategory;
    }

    public String getDocumentDescription() {
        return DocumentDescription;
    }

    public void setDocumentDescription(String documentDescription) {
        DocumentDescription = documentDescription;
    }

    public String getFileWrapperDocumentCode() {
        return FileWrapperDocumentCode;
    }

    public void setFileWrapperDocumentCode(String fileWrapperDocumentCode) {
        FileWrapperDocumentCode = fileWrapperDocumentCode;
    }

    public String getMailRoomDate() {
        return MailRoomDate;
    }

    public void setMailRoomDate(String mailRoomDate) {
        MailRoomDate = mailRoomDate;
    }

    public String getUploadDate() {
        return UploadDate;
    }

    public void setUploadDate(String uploadDate) {
        UploadDate = uploadDate;
    }

    public int getBeginPageNumber() {
        return BeginPageNumber;
    }

    public void setBeginPageNumber(int beginPageNumber) {
        BeginPageNumber = beginPageNumber;
    }

    public int getPageQuantity() {
        return PageQuantity;
    }

    public void setPageQuantity(int pageQuantity) {
        PageQuantity = pageQuantity;
    }

    public int getPageOffsetQuantity() {
        return PageOffsetQuantity;
    }

    public void setPageOffsetQuantity(int pageOffsetQuantity) {
        PageOffsetQuantity = pageOffsetQuantity;
    }

    public String getEarliestViewDate() {
        return earliestViewDate;
    }

    public void setEarliestViewDate(String earliestViewDate) {
        this.earliestViewDate = earliestViewDate;
    }

    public String getEarliestViewBy() {
        return earliestViewBy;
    }

    public void setEarliestViewBy(String earliestViewBy) {
        this.earliestViewBy = earliestViewBy;
    }
    /*
	 * public String getEarliestViewBy() { return EarliestViewBy; }
	 * 
	 * public void setEarliestViewBy(String earliestViewBy) { EarliestViewBy =
	 * earliestViewBy; }
	 */
}
