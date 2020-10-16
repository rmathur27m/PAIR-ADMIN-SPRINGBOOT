package gov.uspto.patent.privatePair.portal.util;

import java.util.HashMap;
import java.util.Map;

public class SearchResult {

        private String inventionTitle;

        private String applicationStatusDate;

        private String publicStatusDate;

        private String secrecyCode;

        private String applicationFileReference;

        private String subclass;

        private String lrClassificationIn;

        private String nationalStageIndicator;

        private String currentApplClass;

        private String physicalObjectStatusCode;

        private String applicationTypeCategory;

        private String abandonDt;

        private String applicationConfirmationNumber;

        private String groupArtUnitNumber;

        private String fileDt;

        private String publicStatusNumber;

        private String statusNumber;

        private String publicIndicator;

        private String applicationNumberText;

        private String inventionSubjectMatterCategory;

        private String specialExaminationIndicator;

        private String activeIndicator;

        private String customerNumberText;

        private String partyIdentifier;

        private String effectiveFilingDate;

        private Map<String, Object> additionalProperties = new HashMap<String, Object>();


        public String getInventionTitle() {
            return inventionTitle;
        }


        public void setInventionTitle(String inventionTitle) {
            this.inventionTitle = inventionTitle;
        }


        public String getApplicationStatusDate() {
            return applicationStatusDate;
        }


        public void setApplicationStatusDate(String applicationStatusDate) {
            this.applicationStatusDate = applicationStatusDate;
        }


        public String getPublicStatusDate() {
            return publicStatusDate;
        }


        public void setPublicStatusDate(String publicStatusDate) {
            this.publicStatusDate = publicStatusDate;
        }


        public String getSecrecyCode() {
            return secrecyCode;
        }


        public void setSecrecyCode(String secrecyCode) {
            this.secrecyCode = secrecyCode;
        }


        public String getApplicationFileReference() {
            return applicationFileReference;
        }


        public void setApplicationFileReference(String applicationFileReference) {
            this.applicationFileReference = applicationFileReference;
        }

        public String getSubclass() {
            return subclass;
        }


        public void setSubclass(String subclass) {
            this.subclass = subclass;
        }


        public String getLrClassificationIn() {
            return lrClassificationIn;
        }


        public void setLrClassificationIn(String lrClassificationIn) {
            this.lrClassificationIn = lrClassificationIn;
        }


        public String getNationalStageIndicator() {
            return nationalStageIndicator;
        }


        public void setNationalStageIndicator(String nationalStageIndicator) {
            this.nationalStageIndicator = nationalStageIndicator;
        }


        public String getCurrentApplClass() {
            return currentApplClass;
        }


        public void setCurrentApplClass(String currentApplClass) {
            this.currentApplClass = currentApplClass;
        }


        public String getPhysicalObjectStatusCode() {
            return physicalObjectStatusCode;
        }


        public void setPhysicalObjectStatusCode(String physicalObjectStatusCode) {
            this.physicalObjectStatusCode = physicalObjectStatusCode;
        }


        public String getApplicationTypeCategory() {
            return applicationTypeCategory;
        }


        public void setApplicationTypeCategory(String applicationTypeCategory) {
            this.applicationTypeCategory = applicationTypeCategory;
        }


        public String getAbandonDt() {
            return abandonDt;
        }


        public void setAbandonDt(String abandonDt) {
            this.abandonDt = abandonDt;
        }


        public String getApplicationConfirmationNumber() {
            return applicationConfirmationNumber;
        }


        public void setApplicationConfirmationNumber(String applicationConfirmationNumber) {
            this.applicationConfirmationNumber = applicationConfirmationNumber;
        }


        public String getGroupArtUnitNumber() {
            return groupArtUnitNumber;
        }


        public void setGroupArtUnitNumber(String groupArtUnitNumber) {
            this.groupArtUnitNumber = groupArtUnitNumber;
        }


        public String getFileDt() {
            return fileDt;
        }


        public void setFileDt(String fileDt) {
            this.fileDt = fileDt;
        }


        public String getPublicStatusNumber() {
            return publicStatusNumber;
        }


        public void setPublicStatusNumber(String publicStatusNumber) {
            this.publicStatusNumber = publicStatusNumber;
        }


        public String getStatusNumber() {
            return statusNumber;
        }


        public void setStatusNumber(String statusNumber) {
            this.statusNumber = statusNumber;
        }


        public String getPublicIndicator() {
            return publicIndicator;
        }


        public void setPublicIndicator(String publicIndicator) {
            this.publicIndicator = publicIndicator;
        }


        public String getApplicationNumberText() {
            return applicationNumberText;
        }


        public void setApplicationNumberText(String applicationNumberText) {
            this.applicationNumberText = applicationNumberText;
        }


        public String getInventionSubjectMatterCategory() {
            return inventionSubjectMatterCategory;
        }


        public void setInventionSubjectMatterCategory(String inventionSubjectMatterCategory) {
            this.inventionSubjectMatterCategory = inventionSubjectMatterCategory;
        }


        public String getSpecialExaminationIndicator() {
            return specialExaminationIndicator;
        }


        public void setSpecialExaminationIndicator(String specialExaminationIndicator) {
            this.specialExaminationIndicator = specialExaminationIndicator;
        }


        public String getActiveIndicator() {
            return activeIndicator;
        }


        public void setActiveIndicator(String activeIndicator) {
            this.activeIndicator = activeIndicator;
        }


        public String getCustomerNumberText() {
            return customerNumberText;
        }


        public void setCustomerNumberText(String customerNumberText) {
            this.customerNumberText = customerNumberText;
        }


        public String getPartyIdentifier() {
            return partyIdentifier;
        }


        public void setPartyIdentifier(String partyIdentifier) {
            this.partyIdentifier = partyIdentifier;
        }


        public String getEffectiveFilingDate() {
            return effectiveFilingDate;
        }


        public void setEffectiveFilingDate(String effectiveFilingDate) {
            this.effectiveFilingDate = effectiveFilingDate;
        }


        public Map<String, Object> getAdditionalProperties() {
            return this.additionalProperties;
        }


        public void setAdditionalProperty(String name, Object value) {
            this.additionalProperties.put(name, value);
        }
}
