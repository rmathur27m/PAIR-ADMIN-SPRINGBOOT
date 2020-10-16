package gov.uspto.patent.privatePair.admin.domain;

import java.util.List;

/**
 * @author kpatel5
 *
 */
public class ApplicationAddressReqType {
	
	private AuditAdmin audit;
    private LifecycleAdmin lifeCycle;
    private List<String> applicationNumbers;
    private String changeCustomerNumber; 
    private String noticeActionCd; 
    private List<String> failedApplicationNumbers;
    private String registrationNumber;
    private String requestIdentifier;
    private List<AddressChangeSupportData> supportData;
	public AuditAdmin getAudit() {
		return audit;
	}
	public void setAudit(AuditAdmin audit) {
		this.audit = audit;
	}
	public LifecycleAdmin getLifeCycle() {
		return lifeCycle;
	}
	public void setLifeCycle(LifecycleAdmin lifeCycle) {
		this.lifeCycle = lifeCycle;
	}
	public List<String> getApplicationNumbers() {
		return applicationNumbers;
	}
	public void setApplicationNumbers(List<String> applicationNumbers) {
		this.applicationNumbers = applicationNumbers;
	}
	public String getChangeCustomerNumber() {
		return changeCustomerNumber;
	}
	public void setChangeCustomerNumber(String changeCustomerNumber) {
		this.changeCustomerNumber = changeCustomerNumber;
	}
	public String getNoticeActionCd() {
		return noticeActionCd;
	}
	public void setNoticeActionCd(String noticeActionCd) {
		this.noticeActionCd = noticeActionCd;
	}
	public List<String> getFailedApplicationNumbers() {
		return failedApplicationNumbers;
	}
	public void setFailedApplicationNumbers(List<String> failedApplicationNumbers) {
		this.failedApplicationNumbers = failedApplicationNumbers;
	}
	public String getRegistrationNumber() {
		return registrationNumber;
	}
	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}
	public String getRequestIdentifier() {
		return requestIdentifier;
	}
	public void setRequestIdentifier(String requestIdentifier) {
		this.requestIdentifier = requestIdentifier;
	}
	public List<AddressChangeSupportData> getSupportData() {
		return supportData;
	}
	public void setSupportData(List<AddressChangeSupportData> supportData) {
		this.supportData = supportData;
	}
	
	
    
    
    
}
