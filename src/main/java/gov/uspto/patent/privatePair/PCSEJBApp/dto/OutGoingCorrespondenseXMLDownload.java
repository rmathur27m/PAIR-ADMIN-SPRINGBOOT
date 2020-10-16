package gov.uspto.patent.privatePair.PCSEJBApp.dto;

import java.util.Date;
import java.util.List;

public class OutGoingCorrespondenseXMLDownload {

    private String FileCreationTimeStamp;
    private int DocumentCount;
    private int CorrespondenceTimePeriod;
    private List<ApplicationCorrespondenseDate> applicationCorrespondenseDate;

    public String getFileCreationTimeStamp() {
        return FileCreationTimeStamp;
    }

    public void setFileCreationTimeStamp(String fileCreationTimeStamp) {
        FileCreationTimeStamp = fileCreationTimeStamp;
    }

    public int getDocumentCount() {
        return DocumentCount;
    }

    public void setDocumentCount(int documentCount) {
        DocumentCount = documentCount;
    }

    public int getCorrespondenceTimePeriod() {
        return CorrespondenceTimePeriod;
    }

    public void setCorrespondenceTimePeriod(int correspondenceTimePeriod) {
        CorrespondenceTimePeriod = correspondenceTimePeriod;
    }

    public List<ApplicationCorrespondenseDate>  getApplicationCorrespondenseDate() {
        return applicationCorrespondenseDate;
    }

    public void setApplicationCorrespondenseDate(List<ApplicationCorrespondenseDate> applicationCorrespondenseDate) {
        this.applicationCorrespondenseDate = applicationCorrespondenseDate;
    }
}
