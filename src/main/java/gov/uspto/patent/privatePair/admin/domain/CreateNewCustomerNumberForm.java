package gov.uspto.patent.privatePair.admin.domain;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * POJO for the create new Customer Number HTML form.
 * 
 */
@Data
public class CreateNewCustomerNumberForm {

    private String userType;
    private String pairId;
    private String customerNumber = "";
    private String userRequestID;
    private String typeOfRequest; // NEW CUSTOMER NUMBER
    private String requestStatus;
    @DateTimeFormat(style="MM")
    private Date timeStamp;
    @DateTimeFormat(style="MM")
    private Date lastModifiedTimeStamp;
    private String firmorIndividualNameLine1;
    private String firmorIndividualNameLine2;
    private String addressLine1;
    private String addressLine2;
    private String state;
    private String city;
    private String zip;
    private String country;
    private String telephone1;
    private String telephone2;
    private String telephone3;
    private String eMail1;
    private String eMail2;
    private String eMail3;
    private String fax1;
    private String fax2;
    private String isAssociateMyPractitionerNumber;
    private String outgoingCorrespondence;

    private String pocFiledBy;
    private String pocRegistrationNumber;
    private String pocSignature;
    private String pocName;
    private String pocTelephone;
    private String pocEmail;
    private String dn;
    private String savedMessage;
    private String customerPractitionerInfo;
    private String privatepairCustomerPractitionerInfo;
    private String custNumberListinString;
    private String palmCustomerNumber;
    private String regCustomerNumber;
    private String isfromSearch;

    private List<String> countryCodes = Collections.emptyList();

    private List<String> stateCodes = Collections.emptyList();

    private List<CustomerPractitionerInfo> customerPractitionerInfolist = Collections.emptyList();

    private List<CustomerPractitionerInfo> existingCustomerPractitionerInfolist = Collections.emptyList();

    private String asJson;

    private String serviceErrorMessage;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((addressLine1 == null) ? 0 : addressLine1.hashCode());
        result = prime * result + ((addressLine2 == null) ? 0 : addressLine2.hashCode());
        result = prime * result + ((asJson == null) ? 0 : asJson.hashCode());
        result = prime * result + ((city == null) ? 0 : city.hashCode());
        result = prime * result + ((country == null) ? 0 : country.hashCode());
        result = prime * result + ((countryCodes == null) ? 0 : countryCodes.hashCode());
        result = prime * result + ((custNumberListinString == null) ? 0 : custNumberListinString.hashCode());
        result = prime * result + ((customerNumber == null) ? 0 : customerNumber.hashCode());
        result = prime * result + ((customerPractitionerInfo == null) ? 0 : customerPractitionerInfo.hashCode());
        result = prime * result + ((customerPractitionerInfolist == null) ? 0 : customerPractitionerInfolist.hashCode());
        result = prime * result + ((dn == null) ? 0 : dn.hashCode());
        result = prime * result + ((eMail1 == null) ? 0 : eMail1.hashCode());
        result = prime * result + ((eMail2 == null) ? 0 : eMail2.hashCode());
        result = prime * result + ((eMail3 == null) ? 0 : eMail3.hashCode());
        result = prime * result
                + ((existingCustomerPractitionerInfolist == null) ? 0 : existingCustomerPractitionerInfolist.hashCode());
        result = prime * result + ((fax1 == null) ? 0 : fax1.hashCode());
        result = prime * result + ((fax2 == null) ? 0 : fax2.hashCode());
        result = prime * result + ((firmorIndividualNameLine1 == null) ? 0 : firmorIndividualNameLine1.hashCode());
        result = prime * result + ((firmorIndividualNameLine2 == null) ? 0 : firmorIndividualNameLine2.hashCode());
        result = prime * result + ((isAssociateMyPractitionerNumber == null) ? 0 : isAssociateMyPractitionerNumber.hashCode());
        result = prime * result + ((isfromSearch == null) ? 0 : isfromSearch.hashCode());
        result = prime * result + ((lastModifiedTimeStamp == null) ? 0 : lastModifiedTimeStamp.hashCode());
        result = prime * result + ((outgoingCorrespondence == null) ? 0 : outgoingCorrespondence.hashCode());
        result = prime * result + ((pairId == null) ? 0 : pairId.hashCode());
        result = prime * result + ((palmCustomerNumber == null) ? 0 : palmCustomerNumber.hashCode());
        result = prime * result + ((pocEmail == null) ? 0 : pocEmail.hashCode());
        result = prime * result + ((pocFiledBy == null) ? 0 : pocFiledBy.hashCode());
        result = prime * result + ((pocName == null) ? 0 : pocName.hashCode());
        result = prime * result + ((pocRegistrationNumber == null) ? 0 : pocRegistrationNumber.hashCode());
        result = prime * result + ((pocSignature == null) ? 0 : pocSignature.hashCode());
        result = prime * result + ((pocTelephone == null) ? 0 : pocTelephone.hashCode());
        result = prime * result
                + ((privatepairCustomerPractitionerInfo == null) ? 0 : privatepairCustomerPractitionerInfo.hashCode());
        result = prime * result + ((regCustomerNumber == null) ? 0 : regCustomerNumber.hashCode());
        result = prime * result + ((requestStatus == null) ? 0 : requestStatus.hashCode());
        result = prime * result + ((savedMessage == null) ? 0 : savedMessage.hashCode());
        result = prime * result + ((serviceErrorMessage == null) ? 0 : serviceErrorMessage.hashCode());
        result = prime * result + ((state == null) ? 0 : state.hashCode());
        result = prime * result + ((stateCodes == null) ? 0 : stateCodes.hashCode());
        result = prime * result + ((telephone1 == null) ? 0 : telephone1.hashCode());
        result = prime * result + ((telephone2 == null) ? 0 : telephone2.hashCode());
        result = prime * result + ((telephone3 == null) ? 0 : telephone3.hashCode());
        result = prime * result + ((timeStamp == null) ? 0 : timeStamp.hashCode());
        result = prime * result + ((typeOfRequest == null) ? 0 : typeOfRequest.hashCode());
        result = prime * result + ((userRequestID == null) ? 0 : userRequestID.hashCode());
        result = prime * result + ((userType == null) ? 0 : userType.hashCode());
        result = prime * result + ((zip == null) ? 0 : zip.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof CreateNewCustomerNumberForm))
            return false;
        CreateNewCustomerNumberForm other = (CreateNewCustomerNumberForm) obj;
        if (addressLine1 == null) {
            if (other.addressLine1 != null)
                return false;
        } else if (!addressLine1.equals(other.addressLine1))
            return false;
        if (addressLine2 == null) {
            if (other.addressLine2 != null)
                return false;
        } else if (!addressLine2.equals(other.addressLine2))
            return false;
        if (asJson == null) {
            if (other.asJson != null)
                return false;
        } else if (!asJson.equals(other.asJson))
            return false;
        if (city == null) {
            if (other.city != null)
                return false;
        } else if (!city.equals(other.city))
            return false;
        if (country == null) {
            if (other.country != null)
                return false;
        } else if (!country.equals(other.country))
            return false;
        if (countryCodes == null) {
            if (other.countryCodes != null)
                return false;
        } else if (!countryCodes.equals(other.countryCodes))
            return false;
        if (custNumberListinString == null) {
            if (other.custNumberListinString != null)
                return false;
        } else if (!custNumberListinString.equals(other.custNumberListinString))
            return false;
        if (customerNumber == null) {
            if (other.customerNumber != null)
                return false;
        } else if (!customerNumber.equals(other.customerNumber))
            return false;
        if (customerPractitionerInfo == null) {
            if (other.customerPractitionerInfo != null)
                return false;
        } else if (!customerPractitionerInfo.equals(other.customerPractitionerInfo))
            return false;
        if (customerPractitionerInfolist == null) {
            if (other.customerPractitionerInfolist != null)
                return false;
        } else if (!customerPractitionerInfolist.equals(other.customerPractitionerInfolist))
            return false;
        if (dn == null) {
            if (other.dn != null)
                return false;
        } else if (!dn.equals(other.dn))
            return false;
        if (eMail1 == null) {
            if (other.eMail1 != null)
                return false;
        } else if (!eMail1.equals(other.eMail1))
            return false;
        if (eMail2 == null) {
            if (other.eMail2 != null)
                return false;
        } else if (!eMail2.equals(other.eMail2))
            return false;
        if (eMail3 == null) {
            if (other.eMail3 != null)
                return false;
        } else if (!eMail3.equals(other.eMail3))
            return false;
        if (existingCustomerPractitionerInfolist == null) {
            if (other.existingCustomerPractitionerInfolist != null)
                return false;
        } else if (!existingCustomerPractitionerInfolist.equals(other.existingCustomerPractitionerInfolist))
            return false;
        if (fax1 == null) {
            if (other.fax1 != null)
                return false;
        } else if (!fax1.equals(other.fax1))
            return false;
        if (fax2 == null) {
            if (other.fax2 != null)
                return false;
        } else if (!fax2.equals(other.fax2))
            return false;
        if (firmorIndividualNameLine1 == null) {
            if (other.firmorIndividualNameLine1 != null)
                return false;
        } else if (!firmorIndividualNameLine1.equals(other.firmorIndividualNameLine1))
            return false;
        if (firmorIndividualNameLine2 == null) {
            if (other.firmorIndividualNameLine2 != null)
                return false;
        } else if (!firmorIndividualNameLine2.equals(other.firmorIndividualNameLine2))
            return false;
        if (isAssociateMyPractitionerNumber == null) {
            if (other.isAssociateMyPractitionerNumber != null)
                return false;
        } else if (!isAssociateMyPractitionerNumber.equals(other.isAssociateMyPractitionerNumber))
            return false;
        if (isfromSearch == null) {
            if (other.isfromSearch != null)
                return false;
        } else if (!isfromSearch.equals(other.isfromSearch))
            return false;
        if (lastModifiedTimeStamp == null) {
            if (other.lastModifiedTimeStamp != null)
                return false;
        } else if (!lastModifiedTimeStamp.equals(other.lastModifiedTimeStamp))
            return false;
        if (outgoingCorrespondence == null) {
            if (other.outgoingCorrespondence != null)
                return false;
        } else if (!outgoingCorrespondence.equals(other.outgoingCorrespondence))
            return false;
        if (pairId == null) {
            if (other.pairId != null)
                return false;
        } else if (!pairId.equals(other.pairId))
            return false;
        if (palmCustomerNumber == null) {
            if (other.palmCustomerNumber != null)
                return false;
        } else if (!palmCustomerNumber.equals(other.palmCustomerNumber))
            return false;
        if (pocEmail == null) {
            if (other.pocEmail != null)
                return false;
        } else if (!pocEmail.equals(other.pocEmail))
            return false;
        if (pocFiledBy == null) {
            if (other.pocFiledBy != null)
                return false;
        } else if (!pocFiledBy.equals(other.pocFiledBy))
            return false;
        if (pocName == null) {
            if (other.pocName != null)
                return false;
        } else if (!pocName.equals(other.pocName))
            return false;
        if (pocRegistrationNumber == null) {
            if (other.pocRegistrationNumber != null)
                return false;
        } else if (!pocRegistrationNumber.equals(other.pocRegistrationNumber))
            return false;
        if (pocSignature == null) {
            if (other.pocSignature != null)
                return false;
        } else if (!pocSignature.equals(other.pocSignature))
            return false;
        if (pocTelephone == null) {
            if (other.pocTelephone != null)
                return false;
        } else if (!pocTelephone.equals(other.pocTelephone))
            return false;
        if (privatepairCustomerPractitionerInfo == null) {
            if (other.privatepairCustomerPractitionerInfo != null)
                return false;
        } else if (!privatepairCustomerPractitionerInfo.equals(other.privatepairCustomerPractitionerInfo))
            return false;
        if (regCustomerNumber == null) {
            if (other.regCustomerNumber != null)
                return false;
        } else if (!regCustomerNumber.equals(other.regCustomerNumber))
            return false;
        if (requestStatus == null) {
            if (other.requestStatus != null)
                return false;
        } else if (!requestStatus.equals(other.requestStatus))
            return false;
        if (savedMessage == null) {
            if (other.savedMessage != null)
                return false;
        } else if (!savedMessage.equals(other.savedMessage))
            return false;
        if (serviceErrorMessage == null) {
            if (other.serviceErrorMessage != null)
                return false;
        } else if (!serviceErrorMessage.equals(other.serviceErrorMessage))
            return false;
        if (state == null) {
            if (other.state != null)
                return false;
        } else if (!state.equals(other.state))
            return false;
        if (stateCodes == null) {
            if (other.stateCodes != null)
                return false;
        } else if (!stateCodes.equals(other.stateCodes))
            return false;
        if (telephone1 == null) {
            if (other.telephone1 != null)
                return false;
        } else if (!telephone1.equals(other.telephone1))
            return false;
        if (telephone2 == null) {
            if (other.telephone2 != null)
                return false;
        } else if (!telephone2.equals(other.telephone2))
            return false;
        if (telephone3 == null) {
            if (other.telephone3 != null)
                return false;
        } else if (!telephone3.equals(other.telephone3))
            return false;
        if (timeStamp == null) {
            if (other.timeStamp != null)
                return false;
        } else if (!timeStamp.equals(other.timeStamp))
            return false;
        if (typeOfRequest == null) {
            if (other.typeOfRequest != null)
                return false;
        } else if (!typeOfRequest.equals(other.typeOfRequest))
            return false;
        if (userRequestID == null) {
            if (other.userRequestID != null)
                return false;
        } else if (!userRequestID.equals(other.userRequestID))
            return false;
        if (userType == null) {
            if (other.userType != null)
                return false;
        } else if (!userType.equals(other.userType))
            return false;
        if (zip == null) {
            if (other.zip != null)
                return false;
        } else if (!zip.equals(other.zip))
            return false;
        return true;
    }
}
