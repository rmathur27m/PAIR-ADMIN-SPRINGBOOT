package gov.uspto.patent.privatePair.portal.util;

public class Audit {
	private String lastModifiedUser;	
	private Integer lastModifiedTime;
	public String getLastModifiedUser() {
		return lastModifiedUser;
	}
	public void setLastModifiedUser(String lastModifiedUser) {
		this.lastModifiedUser = lastModifiedUser;
	}
	public Integer getLastModifiedTime() {
		return lastModifiedTime;
	}
	public void setLastModifiedTime(Integer lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}
	
}
