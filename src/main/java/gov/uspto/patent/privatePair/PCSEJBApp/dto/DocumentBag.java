package gov.uspto.patent.privatePair.PCSEJBApp.dto;

import java.sql.Date;

public class DocumentBag {
    public String documentIdentifier;
    public String packageIdentifier;
    public String accessLevelCategory;
    public boolean closedIndicator;
    public int pageTotalQuantity;
    public String directionCategory;
    public String procedureName;
    public String documentCode;
    public String documentDescription;
    public String officialDate;
    public int packageStatusCode;
    public String lastModifiedDateTime;
    public String sourceSystemName;
    public int packageSequenceNumber;
    public boolean documentMigrationIndicator;
    public int startPageNumber;
    public int logicalPackageNumber;

    public String getDocumentIdentifier() {
        return documentIdentifier;
    }

    public void setDocumentIdentifier(String documentIdentifier) {
        this.documentIdentifier = documentIdentifier;
    }

    public String getPackageIdentifier() {
        return packageIdentifier;
    }

    public void setPackageIdentifier(String packageIdentifier) {
        this.packageIdentifier = packageIdentifier;
    }

    public String getAccessLevelCategory() {
        return accessLevelCategory;
    }

    public void setAccessLevelCategory(String accessLevelCategory) {
        this.accessLevelCategory = accessLevelCategory;
    }

    public boolean isClosedIndicator() {
        return closedIndicator;
    }

    public void setClosedIndicator(boolean closedIndicator) {
        this.closedIndicator = closedIndicator;
    }

    public int getPageTotalQuantity() {
        return pageTotalQuantity;
    }

    public void setPageTotalQuantity(int pageTotalQuantity) {
        this.pageTotalQuantity = pageTotalQuantity;
    }

    public String getDirectionCategory() {
        return directionCategory;
    }

    public void setDirectionCategory(String directionCategory) {
        this.directionCategory = directionCategory;
    }

    public String getProcedureName() {
        return procedureName;
    }

    public void setProcedureName(String procedureName) {
        this.procedureName = procedureName;
    }

    public String getDocumentCode() {
        return documentCode;
    }

    public void setDocumentCode(String documentCode) {
        this.documentCode = documentCode;
    }

    public String getDocumentDescription() {
        return documentDescription;
    }

    public void setDocumentDescription(String documentDescription) {
        this.documentDescription = documentDescription;
    }

    public String getOfficialDate() {
        return officialDate;
    }

    public void setOfficialDate(String officialDate) {
        this.officialDate = officialDate;
    }

    public int getPackageStatusCode() {
        return packageStatusCode;
    }

    public void setPackageStatusCode(int packageStatusCode) {
        this.packageStatusCode = packageStatusCode;
    }

    public String getLastModifiedDateTime() {
        return lastModifiedDateTime;
    }

    public void setLastModifiedDateTime(String lastModifiedDateTime) {
        this.lastModifiedDateTime = lastModifiedDateTime;
    }

    public String getSourceSystemName() {
        return sourceSystemName;
    }

    public void setSourceSystemName(String sourceSystemName) {
        this.sourceSystemName = sourceSystemName;
    }

    public int getPackageSequenceNumber() {
        return packageSequenceNumber;
    }

    public void setPackageSequenceNumber(int packageSequenceNumber) {
        this.packageSequenceNumber = packageSequenceNumber;
    }

    public boolean isDocumentMigrationIndicator() {
        return documentMigrationIndicator;
    }

    public void setDocumentMigrationIndicator(boolean documentMigrationIndicator) {
        this.documentMigrationIndicator = documentMigrationIndicator;
    }

    public int getStartPageNumber() {
        return startPageNumber;
    }

    public void setStartPageNumber(int startPageNumber) {
        this.startPageNumber = startPageNumber;
    }

    public int getLogicalPackageNumber() {
        return logicalPackageNumber;
    }

    public void setLogicalPackageNumber(int logicalPackageNumber) {
        this.logicalPackageNumber = logicalPackageNumber;
    }
}
