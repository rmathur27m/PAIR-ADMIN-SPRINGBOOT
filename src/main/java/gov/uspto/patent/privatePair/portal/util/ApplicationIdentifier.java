package gov.uspto.patent.privatePair.portal.util;

public class ApplicationIdentifier {
    private String applicationNumberText;

    private String internationalRegistrationNumber;

    public void setApplicationNumberText(String applicationNumberText){
        this.applicationNumberText = applicationNumberText;
    }
    public String getApplicationNumberText(){
        return this.applicationNumberText;
    }
    public void setInternationalRegistrationNumber(String internationalRegistrationNumber){
        this.internationalRegistrationNumber = internationalRegistrationNumber;
    }
    public String getInternationalRegistrationNumber(){
        return this.internationalRegistrationNumber;
    }
}
