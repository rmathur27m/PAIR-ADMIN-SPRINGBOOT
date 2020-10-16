package gov.uspto.patent.privatePair.PCSEJBApp.dto;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class EOfficeActionRow implements Serializable {

    private String appNumber;
    private String patentNo;
    private String mailRoomDate;
    private String docID;
    private String docDescription;
    private String startPageNum;
    private String pageCount;
    private String offSet;
    private String attyDocNum;
    private String eNotificationDate;
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

    private String docketed;
    private String daId;
    private String docViewBy;

    public EOfficeActionRow(String appNumber, String patentNo, String mailRoomDate, String docID, String docDescription,
                            String startPageNum, String pageCount, String offSet, String attyDocNum, String eNotificationDate,
                            String docViewDate, String custNum, String documentCategory, String docCode,
                            Timestamp lastModifiedDt, Timestamp filingDate, Timestamp publicationDate,
                            String publicationKindCode, String publicationSeqNo, String publicationYear, String statusGrpCd,
                            String _objectId, String docketed, String daId, String docViewBy) {
        this.appNumber = appNumber;
        this.patentNo = patentNo;
        this.mailRoomDate = mailRoomDate;
        this.docID = docID;
        this.docDescription = docDescription;
        this.startPageNum = startPageNum;
        this.pageCount = pageCount;
        this.offSet = offSet;
        this.attyDocNum = attyDocNum;
        this.eNotificationDate = eNotificationDate;
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
        objectId = _objectId;

    }

}
