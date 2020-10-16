package gov.uspto.patent.privatePair.portal.util;

import gov.uspto.patent.privatePair.score.scoreService.MegaData.MegaItemProcessCodeType;
import gov.uspto.patent.privatePair.score.scoreService.MegaData.MegaItemRecordType;

import java.util.ArrayList;
import java.util.List;

public class SupplementDataType {
    private String versionNumber;
//    List<MegaItemRecordType> megaItemRecordsTypeList;
    private String megaItemId;
    private String megaItemName;
    private String itemTotal;
    private List<MegaItemProcessCodeType> processCode;
    private long itemSize;
    private String mailroomDateText;
    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getMegaItemId() {
        return megaItemId;
    }

    public void setMegaItemId(String megaItemId) {
        this.megaItemId = megaItemId;
    }

    public String getMegaItemName() {
        return megaItemName;
    }

    public void setMegaItemName(String megaItemName) {
        this.megaItemName = megaItemName;
    }

    public String getItemTotal() {
        return itemTotal;
    }

    public void setItemTotal(String itemTotal) {
        this.itemTotal = itemTotal;
    }

    public List<MegaItemProcessCodeType> getProcessCode() {
        return processCode;
    }

    public void setProcessCode(List<MegaItemProcessCodeType> processCode) {
        this.processCode = processCode;
    }

    public long getItemSize() {
        return itemSize;
    }

    public void setItemSize(long itemSize) {
        this.itemSize = itemSize;
    }

    public String getMailroomDateText() {
        return mailroomDateText;
    }

    public void setMailroomDateText(String mailroomDateText) {
        this.mailroomDateText = mailroomDateText;
    }
/*public List<MegaItemRecordType> getMegaItemRecordsTypeList() {
        return megaItemRecordsTypeList;
    }

    public void setMegaItemRecordsTypeList(List<MegaItemRecordType> megaItemRecordsTypeList) {
        this.megaItemRecordsTypeList = megaItemRecordsTypeList;
    }*/
}
