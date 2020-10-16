package gov.uspto.patent.privatePair.portal.util;

public class Results {
	private Audit audit;
	
	private String caseActionCode;

	private String caseActionDescriptionText;
	
	private String applicationStatusNumber;

	private String applicationStatusDescriptionText;
	
	private Integer applicationStatusDate;
	
	private Integer recordedDate;
	
	private Double sequenceNumber;
	
	private String entityName;
	
	private Boolean publicIndicator;
	
	private String transactionType;
	
	private Integer actionNumber;
	
	private Integer patentCaseActionDataIdentifier;
	
	private Integer patentCaseLinkedIdentifier;
	
	private String statusCode;

	public Audit getAudit() {
		return audit;
	}

	public void setAudit(Audit audit) {
		this.audit = audit;
	}

	public String getCaseActionCode() {
		return caseActionCode;
	}

	public void setCaseActionCode(String caseActionCode) {
		this.caseActionCode = caseActionCode;
	}

	public String getCaseActionDescriptionText() {
		return caseActionDescriptionText;
	}

	public void setCaseActionDescriptionText(String caseActionDescriptionText) {
		this.caseActionDescriptionText = caseActionDescriptionText;
	}

	public String getApplicationStatusNumber() {
		return applicationStatusNumber;
	}

	public void setApplicationStatusNumber(String applicationStatusNumber) {
		this.applicationStatusNumber = applicationStatusNumber;
	}

	public String getApplicationStatusDescriptionText() {
		return applicationStatusDescriptionText;
	}

	public void setApplicationStatusDescriptionText(String applicationStatusDescriptionText) {
		this.applicationStatusDescriptionText = applicationStatusDescriptionText;
	}

	public Integer getApplicationStatusDate() {
		return applicationStatusDate;
	}

	public void setApplicationStatusDate(Integer applicationStatusDate) {
		this.applicationStatusDate = applicationStatusDate;
	}

	public Integer getRecordedDate() {
		return recordedDate;
	}

	public void setRecordedDate(Integer recordedDate) {
		this.recordedDate = recordedDate;
	}

	public Double getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Double sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public Boolean getPublicIndicator() {
		return publicIndicator;
	}

	public void setPublicIndicator(Boolean publicIndicator) {
		this.publicIndicator = publicIndicator;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public Integer getActionNumber() {
		return actionNumber;
	}

	public void setActionNumber(Integer actionNumber) {
		this.actionNumber = actionNumber;
	}

	public Integer getPatentCaseActionDataIdentifier() {
		return patentCaseActionDataIdentifier;
	}

	public void setPatentCaseActionDataIdentifier(Integer patentCaseActionDataIdentifier) {
		this.patentCaseActionDataIdentifier = patentCaseActionDataIdentifier;
	}

	public Integer getPatentCaseLinkedIdentifier() {
		return patentCaseLinkedIdentifier;
	}

	public void setPatentCaseLinkedIdentifier(Integer patentCaseLinkedIdentifier) {
		this.patentCaseLinkedIdentifier = patentCaseLinkedIdentifier;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	
}
