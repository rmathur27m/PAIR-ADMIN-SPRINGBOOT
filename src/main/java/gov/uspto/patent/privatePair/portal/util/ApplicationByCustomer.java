package gov.uspto.patent.privatePair.portal.util;

import java.util.List;

public class ApplicationByCustomer {
	private String fileCreationTime;
	private List<ApplicationStatusData> applicationStatusData;
	public String getFileCreationTime() {
		return fileCreationTime;
	}
	public void setFileCreationTime(String fileCreationTime) {
		this.fileCreationTime = fileCreationTime;
	}
	public List<ApplicationStatusData> getApplicationStatusData() {
		return applicationStatusData;
	}
	public void setApplicationStatusData(List<ApplicationStatusData> applicationStatusData) {
		this.applicationStatusData = applicationStatusData;
	}
	
	

}
