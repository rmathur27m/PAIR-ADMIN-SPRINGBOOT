package gov.uspto.patent.privatePair.admin.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;

/**
 * This is a Base Class.
 */

public class UserRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    protected static final String dateFormatPattern = "dd MMM yyyy HH:mm:ss z";

    private long userRequestId; // PAIR_ID , sequence
    private String fkUserId;
    private Date createTimeStamp;
    private Date lastModifiedTimeStamp;
    private String typeOfRequest;
    private String requestStatusCount;
    private String submitterRegistrationNo;
    private String contactFullName;
    private String contactTelephoneNoText;
    private String contactEmailText;
    private String userCommentsText;
    private String createUserId;
    private String lastModifiedUserId;
    private long lockControlNo;
    private String deleteIndicator;
    private String requestDescriptionText;
    private String registeredAttorneyType;
    private String independentInventorType;

    public String getRequestDescriptionText() {
        return requestDescriptionText;
    }

    public void setRequestDescriptionText(String requestDescriptionText) {
        this.requestDescriptionText = requestDescriptionText;
    }

    public String getContactTelephoneNoText() {
        return contactTelephoneNoText;
    }

    public void setContactTelephoneNoText(String contactTelephoneNoText) {
        this.contactTelephoneNoText = contactTelephoneNoText;
    }

    public String getContactEmailText() {
        return contactEmailText;
    }

    public void setContactEmailText(String contactEmailText) {
        this.contactEmailText = contactEmailText;
    }

    public long getUserRequestId() {
        return userRequestId;
    }

    public void setUserRequestId(long userRequestId) {
        this.userRequestId = userRequestId;
    }

    public String getFkUserId() {
        return fkUserId;
    }

    public void setFkUserId(String fkUserId) {
        this.fkUserId = fkUserId;
    }

    public Date getCreateTimeStamp() {
        return createTimeStamp;
    }

    public void setCreateTimeStamp(Date createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    public Date getLastModifiedTimeStamp() {
        return lastModifiedTimeStamp;
    }

    public void setLastModifiedTimeStamp(Date lastModifiedTimeStamp) {
        this.lastModifiedTimeStamp = lastModifiedTimeStamp;
    }

    public String getTypeOfRequest() {
        return typeOfRequest;
    }

    public void setTypeOfRequest(String typeOfRequest) {
        this.typeOfRequest = typeOfRequest;
    }

    public String getRequestStatusCount() {
        return requestStatusCount;
    }

    public void setRequestStatusCount(String requestStatusCount) {
        this.requestStatusCount = requestStatusCount;
    }

    public String getSubmitterRegistrationNo() {
        return submitterRegistrationNo;
    }

    public void setSubmitterRegistrationNo(String submitterRegistrationNo) {
        this.submitterRegistrationNo = submitterRegistrationNo;
    }

    public String getContactFullName() {
        return contactFullName;
    }

    public void setContactFullName(String contactFullName) {
        this.contactFullName = contactFullName;
    }

    public String getUserCommentsText() {
        return userCommentsText;
    }

    public void setUserCommentsText(String userCommentsText) {
        this.userCommentsText = userCommentsText;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getLastModifiedUserId() {
        return lastModifiedUserId;
    }

    public void setLastModifiedUserId(String lastModifiedUserId) {
        this.lastModifiedUserId = lastModifiedUserId;
    }

    public long getLockControlNo() {
        return lockControlNo;
    }

    public void setLockControlNo(long lockControlNo) {
        this.lockControlNo = lockControlNo;
    }

    public String getDeleteIndicator() {
        return deleteIndicator;
    }

    public void setDeleteIndicator(String deleteIndicator) {
        this.deleteIndicator = deleteIndicator;
    }

    public String getRegisteredAttorneyType() {
        return registeredAttorneyType;
    }

    public void setRegisteredAttorneyType(String registeredAttorneyType) {
        this.registeredAttorneyType = registeredAttorneyType;
    }

    public String getIndependentInventorType() {
        return independentInventorType;
    }

    public void setIndependentInventorType(String independentInventorType) {
        this.independentInventorType = independentInventorType;
    }

    private String gmt(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.setTimeZone(new SimpleTimeZone(0, "GMT"));
        sdf.applyPattern(dateFormatPattern);

        return (sdf.format(date));
    }

    @Override
    public String toString() {
        return "User_Request [userRequestId = " + userRequestId + ", " + "fkUserId = " + fkUserId + ", createTimeStamp = "
                + gmt(createTimeStamp) + ", last_modified_ts = " + gmt(lastModifiedTimeStamp) + ", typeOfRequest = "
                + typeOfRequest + ", requestStatusCount = " + requestStatusCount + ", submitterRegistrationNo = "
                + submitterRegistrationNo + ", contactFullName = " + contactFullName + ", contactTelephoneNoText = "
                + contactTelephoneNoText + ", contactEmailText = " + contactEmailText + ", userCommentsText = "
                + userCommentsText + ", createUserId = " + createUserId + ", lastModifiedUserId = " + lastModifiedUserId
                + ", lockControlNo = " + lockControlNo + ", deleteIndicator = " + deleteIndicator + "]";
    }

    public UserRequestDto() {
        this.userRequestId = 0;
        this.fkUserId = "";
        this.createTimeStamp = null;
        this.lastModifiedTimeStamp = null;
        this.typeOfRequest = "";
        this.requestStatusCount = "";
        this.submitterRegistrationNo = "";
        this.contactFullName = "";
        this.contactTelephoneNoText = "";
        this.contactEmailText = "";
        this.userCommentsText = "";
        this.createUserId = "";
        this.lastModifiedUserId = "";
        this.lockControlNo = 0;
        this.deleteIndicator = "";
    }
}
