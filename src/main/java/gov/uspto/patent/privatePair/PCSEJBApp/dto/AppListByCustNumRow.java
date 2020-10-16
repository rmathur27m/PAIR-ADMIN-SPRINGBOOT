package gov.uspto.patent.privatePair.PCSEJBApp.dto;

import java.sql.Timestamp;
import java.util.Date;

public class AppListByCustNumRow {
    private String appNumber;
    private String patentNo;
    private String attorneyDocNo;
    private String earliestPubNo;
    private String statusDate;
    private String filingDate;
    private String ifwStatus;
    private String statusNo;
    private String statusDesc;

    //Newly added columns on and after Private PAIR 7.1
    private String customerNumber;
    private String examinerName;
    private String gauCd;
    private String artClassNo;
    private String artSubClassNo;
    private String continuityType;
    private Date actualPubDt;
    private Date pcaActionDt;
    private String pcaActionDesc;
    //private String efwStatusIn;
    private String statusGrpCd;
    private Date patentIssueDt;
    private Timestamp lastModifiedDt;
    private String lastModifiedUserId;





    public AppListByCustNumRow(){}




    //constructor ror AppListByCustNumRow

    //Custructor used to create valueObject to display Mutliple Applications
    public AppListByCustNumRow(String appNumber,
                               String customerNumber,
                               String patentNo,String attorneyDocNo,
                               String earliestPubNo,String statusDate,
                               String filingDate,String ifwStatus,
                               String statusNo, String statusDesc, String statusGrpCd)
    {
        this.appNumber = appNumber;
        this.customerNumber = customerNumber;
        this.patentNo = patentNo;
        this.attorneyDocNo = attorneyDocNo;
        this.earliestPubNo = earliestPubNo;
        this.statusDate = statusDate;
        this.filingDate = filingDate;
        this.ifwStatus = ifwStatus;
        this.statusNo = statusNo;
        this.statusDesc = statusDesc;
        this.statusDesc = statusDesc;
        this.statusGrpCd = statusGrpCd;
    }

    //Custructor used to create valueObject to download Mutliple Applications
    public AppListByCustNumRow(String appNumber,
                               String patentNo,String attorneyDocNo,
                               String earliestPubNo,String statusDate,
                               String filingDate,String ifwStatus, String statusNo, String statusDesc,
                               String customerNumber, String examinerName, String gauCd, String artClassNo,
                               String artSubClassNo, String continuityType, Date actualPubDt, Date pcaActionDt,
                               String pcaActionDesc, Date patentIssueDt, Timestamp lastModifiedDt,
                               String lastModifiedUserId)
    {
        this.appNumber = appNumber;
        this.customerNumber = customerNumber;
        this.patentNo = patentNo;
        this.attorneyDocNo = attorneyDocNo;
        this.earliestPubNo = earliestPubNo;
        this.statusDate = statusDate;
        this.filingDate = filingDate;
        this.ifwStatus = ifwStatus;
        this.statusNo = statusNo;
        this.statusDesc = statusDesc;
        this.customerNumber = customerNumber;
        this.examinerName = examinerName;
        this.gauCd = gauCd ;
        this.artClassNo = artClassNo;
        this.artSubClassNo = artSubClassNo;
        this.continuityType = continuityType;
        this.actualPubDt = actualPubDt;
        this.pcaActionDt =  pcaActionDt;
        this.pcaActionDesc = pcaActionDesc;
        this.patentIssueDt = patentIssueDt;
        this.lastModifiedDt = lastModifiedDt;
        this.lastModifiedUserId = lastModifiedUserId;

    }

    public String getAppNumber() {
        return appNumber;
    }

    public String getAttorneyDocNo() {
        return attorneyDocNo;
    }


    public String getEarliestPubNo() {
        return earliestPubNo;
    }

    public String getFilingDate() {
        return filingDate;
    }

    public String getIfwStatus() {
        return ifwStatus;
    }

    public String getPatentNo() {
        return patentNo;
    }

    public String getStatusDate() {
        return statusDate;
    }

    /**
     * @param string
     */
    public void setAppNumber(String string) {
        appNumber = string;
    }

    /**
     * @param string
     */
    public void setAttorneyDocNo(String string) {
        attorneyDocNo = string;
    }


    /**
     * @param string
     */
    public void setEarliestPubNo(String string) {
        earliestPubNo = string;
    }

    /**
     * @param string
     */
    public void setFilingDate(String string) {
        filingDate = string;
    }

    /**
     * @param string
     */
    public void setIfwStatus(String string) {
        ifwStatus = string;
    }

    /**
     * @param string
     */
    public void setPatentNo(String string) {
        patentNo = string;
    }

    /**
     * @param string
     */
    public void setStatusDate(String string) {
        statusDate = string;
    }

    public String getStatusDesc() {
        return statusDesc;
    }
    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }
    public String getStatusNo() {
        return statusNo;
    }
    public void setStatusNo(String statusNo) {
        this.statusNo = statusNo;
    }


    public Date getActualPubDt() {
        return actualPubDt;
    }
    public void setActualPubDt(Date actualPubDt) {
        this.actualPubDt = actualPubDt;
    }
    public String getArtClassNo() {
        return artClassNo;
    }
    public void setArtClassNo(String artClassNo) {
        this.artClassNo = artClassNo;
    }
    public String getArtSubClassNo() {
        return artSubClassNo;
    }
    public void setArtSubClassNo(String artSubClassNo) {
        this.artSubClassNo = artSubClassNo;
    }
    public String getContinuityType() {
        return continuityType;
    }
    public void setContinuityType(String continuityType) {
        this.continuityType = continuityType;
    }
    public String getCustomerNumber() {
        return customerNumber;
    }
    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }
/*	public String getEfwStatusIn() {
		return efwStatusIn;
	}
	public void setEfwStatusIn(String efwStatusIn) {
		this.efwStatusIn = efwStatusIn;
	}*/


    public String getExaminerName() {
        return examinerName;
    }
    public void setExaminerName(String examinerName) {
        this.examinerName = examinerName;
    }
    public String getGauCd() {
        return gauCd;
    }
    public void setGauCd(String gauCd) {
        this.gauCd = gauCd;
    }
    public Timestamp getLastModifiedDt() {
        return lastModifiedDt;
    }
    public void setLastModifiedDt(Timestamp lastModifiedDt) {
        this.lastModifiedDt = lastModifiedDt;
    }
    public String getLastModifiedUserId() {
        return lastModifiedUserId;
    }
    public void setLastModifiedUserId(String lastModifiedUserId) {
        this.lastModifiedUserId = lastModifiedUserId;
    }


    public Date getPatentIssueDt() {
        return patentIssueDt;
    }
    public void setPatentIssueDt(Date patentIssueDt) {
        this.patentIssueDt = patentIssueDt;
    }
    public String getPcaActionDesc() {
        return pcaActionDesc;
    }
    public void setPcaActionDesc(String pcaActionDesc) {
        this.pcaActionDesc = pcaActionDesc;
    }
    public Date getPcaActionDt() {
        return pcaActionDt;
    }
    public void setPcaActionDt(Date pcaActionDt) {
        this.pcaActionDt = pcaActionDt;
    }

    public String getStatusGrpCd() {
        return statusGrpCd;
    }
    public void setStatusGrpCd(String statusGrpCd) {
        this.statusGrpCd = statusGrpCd;
    }
}
