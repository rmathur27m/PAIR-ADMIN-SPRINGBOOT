package gov.uspto.patent.privatePair.fpng.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

/**
 * Class Result is a value object for the line level metadata fetched by the new
 * FPNG Rest Service
 * 
 * @author "srayala"
 */
/*@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonPropertyOrder({ "version", "postingReferenceText", "parentId", "datePosted", "mailRoomDate", "receivedDate", "feeCode",
        "feeCodeDescription", "feeCodeShortName", "feeCodeCfrReferenceText", "feeReferenceGroupCode",
        "feeReferenceGroupItemName", "feeAmount", "quantity", "amount", "transactionStatus", "saleId", "refundId", "crdaId",
        "createUserId", "patronId", "attorneyDocketNumber", "swissFrancExchangeRate", "paymentDetails", "preFY10", "reversal" })*/
@Data
public class Result  {

    private static final long serialVersionUID = -6657743573862708141L;
    ////@JsonProperty("version")
    private Integer version;
   // //@JsonProperty("postingReferenceText")
    private String postingReferenceText;
   // //@JsonProperty("parentId")
    private String parentId;
   // //@JsonProperty("datePosted")
    private Long datePosted;
   // //@JsonProperty("mailRoomDate")
    private Long mailRoomDate;
   // //@JsonProperty("receivedDate")
    private Long receivedDate;
   // //@JsonProperty("feeCode")
    private String feeCode;
  //  //@JsonProperty("feeCodeDescription")
    private String feeCodeDescription;
    ////@JsonProperty("feeCodeShortName")
    private String feeCodeShortName;
   // //@JsonProperty("feeCodeCfrReferenceText")
    private String feeCodeCfrReferenceText;
   // //@JsonProperty("feeReferenceGroupCode")
    private String feeReferenceGroupCode;
   // //@JsonProperty("feeReferenceGroupItemName")
    private String feeReferenceGroupItemName;
   // //@JsonProperty("feeAmount")
    private Integer feeAmount;
   // //@JsonProperty("quantity")
    private Integer quantity;
   // //@JsonProperty("amount")
    private Integer amount;
   // //@JsonProperty("transactionStatus")
    private String transactionStatus;
   // //@JsonProperty("saleId")
    private String saleId;
   // //@JsonProperty("refundId")
    private String refundId;
   // //@JsonProperty("crdaId")
    private String crdaId;
   // //@JsonProperty("createUserId")
    private String createUserId;
   // //@JsonProperty("patronId")
    private String patronId;
  //  //@JsonProperty("attorneyDocketNumber")
    private String attorneyDocketNumber;
   // //@JsonProperty("swissFrancExchangeRate")
    private String swissFrancExchangeRate;
   // //@JsonProperty("paymentDetails")
    private List<PaymentDetail> paymentDetails = new ArrayList<PaymentDetail>();
   // //@JsonProperty("preFY10")
    private Boolean preFY10;
    ////@JsonProperty("reversal")
    private Boolean reversal;
   // @JsonIgnore
    private final Map<String, String> additionalProperties = new HashMap<String, String>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Result() {
    }

    /**
     * 
     * @param preFY10
     * @param datePosted
     * @param attorneyDocketNumber
     * @param saleId
     * @param crdaId
     * @param feeCodeCfrReferenceText
     * @param paymentDetails
     * @param patronId
     * @param feeCodeDescription
     * @param version
     * @param receivedDate
     * @param feeReferenceGroupItemName
     * @param amount
     * @param feeCodeShortName
     * @param parentId
     * @param refundId
     * @param feeCode
     * @param feeAmount
     * @param swissFrancExchangeRate
     * @param quantity
     * @param postingReferenceText
     * @param transactionStatus
     * @param reversal
     * @param createUserId
     * @param mailRoomDate
     * @param feeReferenceGroupCode
     */
    public Result(Integer version, String postingReferenceText, String parentId, Long datePosted, Long mailRoomDate,
            Long receivedDate, String feeCode, String feeCodeDescription, String feeCodeShortName,
            String feeCodeCfrReferenceText, String feeReferenceGroupCode, String feeReferenceGroupItemName, Integer feeAmount,
            Integer quantity, Integer amount, String transactionStatus, String saleId, String refundId, String crdaId,
            String createUserId, String patronId, String attorneyDocketNumber, String swissFrancExchangeRate,
            List<PaymentDetail> paymentDetails, Boolean preFY10, Boolean reversal) {
        this.version = version;
        this.postingReferenceText = postingReferenceText;
        this.parentId = parentId;
        this.datePosted = datePosted;
        this.mailRoomDate = mailRoomDate;
        this.receivedDate = receivedDate;
        this.feeCode = feeCode;
        this.feeCodeDescription = feeCodeDescription;
        this.feeCodeShortName = feeCodeShortName;
        this.feeCodeCfrReferenceText = feeCodeCfrReferenceText;
        this.feeReferenceGroupCode = feeReferenceGroupCode;
        this.feeReferenceGroupItemName = feeReferenceGroupItemName;
        this.feeAmount = feeAmount;
        this.quantity = quantity;
        this.amount = amount;
        this.transactionStatus = transactionStatus;
        this.saleId = saleId;
        this.refundId = refundId;
        this.crdaId = crdaId;
        this.createUserId = createUserId;
        this.patronId = patronId;
        this.attorneyDocketNumber = attorneyDocketNumber;
        this.swissFrancExchangeRate = swissFrancExchangeRate;
        this.paymentDetails = paymentDetails;
        this.preFY10 = preFY10;
        this.reversal = reversal;
    }

    /**
     *
     * @return The version
   */
   // //@JsonProperty("version")
    public Integer getVersion() {
        return version;
    }

    /**
     *
     * @param version
     *            The version
     */
   // //@JsonProperty("version")
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     *
     * @return The postingReferenceText
     */
    //@JsonProperty("postingReferenceText")
    public String getPostingReferenceText() {
        return postingReferenceText;
    }

    /**
     *
     * @param postingReferenceText
     *            The postingReferenceText
     */
   // //@JsonProperty("postingReferenceText")
    public void setPostingReferenceText(String postingReferenceText) {
        this.postingReferenceText = postingReferenceText;
    }

    /**
     *
     * @return The parentId
     */
    //@JsonProperty("parentId")
    public String getParentId() {
        return parentId;
    }

    /**
     *
     * @param parentId
     *            The parentId
     */
    //@JsonProperty("parentId")
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    /**
     *
     * @return The datePosted
     */
    //@JsonProperty("datePosted")
    public Long getDatePosted() {
        return datePosted;
    }

    /**
     *
     * @param datePosted
     *            The datePosted
     */
    //@JsonProperty("datePosted")
    public void setDatePosted(Long datePosted) {
        this.datePosted = datePosted;
    }

    /**
     *
     * @return The mailRoomDate
     */
    //@JsonProperty("mailRoomDate")
    public Long getMailRoomDate() {
        return mailRoomDate;
    }

    /**
     *
     * @param mailRoomDate
     *            The mailRoomDate
     */
    //@JsonProperty("mailRoomDate")
    public void setMailRoomDate(Long mailRoomDate) {
        this.mailRoomDate = mailRoomDate;
    }

    /**
     *
     * @return The receivedDate
     */
    //@JsonProperty("receivedDate")
    public Long getReceivedDate() {
        return receivedDate;
    }

    /**
     *
     * @param receivedDate
     *            The receivedDate
     */
    //@JsonProperty("receivedDate")
    public void setReceivedDate(Long receivedDate) {
        this.receivedDate = receivedDate;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Result [version=");
        builder.append(version);
        builder.append(", postingReferenceText=");
        builder.append(postingReferenceText);
        builder.append(", parentId=");
        builder.append(parentId);
        builder.append(", datePosted=");
        builder.append(datePosted);
        builder.append(", mailRoomDate=");
        builder.append(mailRoomDate);
        builder.append(", receivedDate=");
        builder.append(receivedDate);
        builder.append(", feeCode=");
        builder.append(feeCode);
        builder.append(", feeCodeDescription=");
        builder.append(feeCodeDescription);
        builder.append(", feeCodeShortName=");
        builder.append(feeCodeShortName);
        builder.append(", feeCodeCfrReferenceText=");
        builder.append(feeCodeCfrReferenceText);
        builder.append(", feeReferenceGroupCode=");
        builder.append(feeReferenceGroupCode);
        builder.append(", feeReferenceGroupItemName=");
        builder.append(feeReferenceGroupItemName);
        builder.append(", feeAmount=");
        builder.append(feeAmount);
        builder.append(", quantity=");
        builder.append(quantity);
        builder.append(", amount=");
        builder.append(amount);
        builder.append(", transactionStatus=");
        builder.append(transactionStatus);
        builder.append(", saleId=");
        builder.append(saleId);
        builder.append(", refundId=");
        builder.append(refundId);
        builder.append(", crdaId=");
        builder.append(crdaId);
        builder.append(", createUserId=");
        builder.append(createUserId);
        builder.append(", patronId=");
        builder.append(patronId);
        builder.append(", attorneyDocketNumber=");
        builder.append(attorneyDocketNumber);
        builder.append(", swissFrancExchangeRate=");
        builder.append(swissFrancExchangeRate);
        builder.append(", paymentDetails=");
        builder.append(paymentDetails);
        builder.append(", preFY10=");
        builder.append(preFY10);
        builder.append(", reversal=");
        builder.append(reversal);
        builder.append(", additionalProperties=");
        builder.append(additionalProperties);
        builder.append("]");
        return builder.toString();
    }

    /**
     * 
     * @return The feeCode
     */
    //@JsonProperty("feeCode")
    public String getFeeCode() {
        return feeCode;
    }

    /**
     *
     * @param feeCode
     *            The feeCode
     */
    //@JsonProperty("feeCode")
    public void setFeeCode(String feeCode) {
        this.feeCode = feeCode;
    }

    /**
     *
     * @return The feeCodeDescription
     */
    //@JsonProperty("feeCodeDescription")
    public String getFeeCodeDescription() {
        return feeCodeDescription;
    }

    /**
     *
     * @param feeCodeDescription
     *            The feeCodeDescription
     */
    //@JsonProperty("feeCodeDescription")
    public void setFeeCodeDescription(String feeCodeDescription) {
        this.feeCodeDescription = feeCodeDescription;
    }

    /**
     *
     * @return The feeCodeShortName
     */
    //@JsonProperty("feeCodeShortName")
    public String getFeeCodeShortName() {
        return feeCodeShortName;
    }

    /**
     *
     * @param feeCodeShortName
     *            The feeCodeShortName
     */
    //@JsonProperty("feeCodeShortName")
    public void setFeeCodeShortName(String feeCodeShortName) {
        this.feeCodeShortName = feeCodeShortName;
    }

    /**
     *
     * @return The feeCodeCfrReferenceText
     */
    //@JsonProperty("feeCodeCfrReferenceText")
    public String getFeeCodeCfrReferenceText() {
        return feeCodeCfrReferenceText;
    }

    /**
     *
     * @param feeCodeCfrReferenceText
     *            The feeCodeCfrReferenceText
     */
    //@JsonProperty("feeCodeCfrReferenceText")
    public void setFeeCodeCfrReferenceText(String feeCodeCfrReferenceText) {
        this.feeCodeCfrReferenceText = feeCodeCfrReferenceText;
    }

    /**
     *
     * @return The feeReferenceGroupCode
     */
    //@JsonProperty("feeReferenceGroupCode")
    public String getFeeReferenceGroupCode() {
        return feeReferenceGroupCode;
    }

    /**
     *
     * @param feeReferenceGroupCode
     *            The feeReferenceGroupCode
     */
    //@JsonProperty("feeReferenceGroupCode")
    public void setFeeReferenceGroupCode(String feeReferenceGroupCode) {
        this.feeReferenceGroupCode = feeReferenceGroupCode;
    }

    /**
     *
     * @return The feeReferenceGroupItemName
     */
    //@JsonProperty("feeReferenceGroupItemName")
    public String getFeeReferenceGroupItemName() {
        return feeReferenceGroupItemName;
    }

    /**
     *
     * @param feeReferenceGroupItemName
     *            The feeReferenceGroupItemName
     */
    //@JsonProperty("feeReferenceGroupItemName")
    public void setFeeReferenceGroupItemName(String feeReferenceGroupItemName) {
        this.feeReferenceGroupItemName = feeReferenceGroupItemName;
    }

    /**
     *
     * @return The feeAmount
     */
    //@JsonProperty("feeAmount")
    public Integer getFeeAmount() {
        return feeAmount;
    }

    /**
     *
     * @param feeAmount
     *            The feeAmount
     */
    //@JsonProperty("feeAmount")
    public void setFeeAmount(Integer feeAmount) {
        this.feeAmount = feeAmount;
    }

    /**
     *
     * @return The quantity
     */
    //@JsonProperty("quantity")
    public Integer getQuantity() {
        return quantity;
    }

    /**
     *
     * @param quantity
     *            The quantity
     */
    //@JsonProperty("quantity")
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     *
     * @return The amount
     */
    //@JsonProperty("amount")
    public Integer getAmount() {
        return amount;
    }

    /**
     *
     * @param amount
     *            The amount
     */
    //@JsonProperty("amount")
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     *
     * @return The transactionStatus
     */
    //@JsonProperty("transactionStatus")
    public String getTransactionStatus() {
        return transactionStatus;
    }

    /**
     *
     * @param transactionStatus
     *            The transactionStatus
     */
    //@JsonProperty("transactionStatus")
    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    /**
     *
     * @return The saleId
     */
    //@JsonProperty("saleId")
    public String getSaleId() {
        return saleId;
    }

    /**
     *
     * @param saleId
     *            The saleId
     */
    //@JsonProperty("saleId")
    public void setSaleId(String saleId) {
        this.saleId = saleId;
    }

    /**
     *
     * @return The refundId
     */
    //@JsonProperty("refundId")
    public String getRefundId() {
        return refundId;
    }

    /**
     *
     * @param refundId
     *            The refundId
     */
    //@JsonProperty("refundId")
    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    /**
     *
     * @return The crdaId
     */
    //@JsonProperty("crdaId")
    public String getCrdaId() {
        return crdaId;
    }

    /**
     *
     * @param crdaId
     *            The crdaId
     */
    //@JsonProperty("crdaId")
    public void setCrdaId(String crdaId) {
        this.crdaId = crdaId;
    }

    /**
     *
     * @return The createUserId
     */
    //@JsonProperty("createUserId")
    public String getCreateUserId() {
        return createUserId;
    }

    /**
     *
     * @param createUserId
     *            The createUserId
     */
    //@JsonProperty("createUserId")
    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    /**
     *
     * @return The patronId
     */
    //@JsonProperty("patronId")
    public String getPatronId() {
        return patronId;
    }

    /**
     *
     * @param patronId
     *            The patronId
     */
    //@JsonProperty("patronId")
    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }

    /**
     *
     * @return The attorneyDocketNumber
     */
    //@JsonProperty("attorneyDocketNumber")
    public String getAttorneyDocketNumber() {
        return attorneyDocketNumber;
    }

    /**
     *
     * @param attorneyDocketNumber
     *            The attorneyDocketNumber
     */
    //@JsonProperty("attorneyDocketNumber")
    public void setAttorneyDocketNumber(String attorneyDocketNumber) {
        this.attorneyDocketNumber = attorneyDocketNumber;
    }

    /**
     *
     * @return The swissFrancExchangeRate
     */
    //@JsonProperty("swissFrancExchangeRate")
    public String getSwissFrancExchangeRate() {
        return swissFrancExchangeRate;
    }

    /**
     *
     * @param swissFrancExchangeRate
     *            The swissFrancExchangeRate
     */
    //@JsonProperty("swissFrancExchangeRate")
    public void setSwissFrancExchangeRate(String swissFrancExchangeRate) {
        this.swissFrancExchangeRate = swissFrancExchangeRate;
    }

    /**
     *
     * @return The paymentDetails
     */
    //@JsonProperty("paymentDetails")
    public List<PaymentDetail> getPaymentDetails() {
        return paymentDetails;
    }

    /**
     *
     * @param paymentDetails
     *            The paymentDetails
     */
    //@JsonProperty("paymentDetails")
    public void setPaymentDetails(List<PaymentDetail> paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    /**
     *
     * @return The preFY10
     */
    //@JsonProperty("preFY10")
    public Boolean getPreFY10() {
        return preFY10;
    }

    /**
     *
     * @param preFY10
     *            The preFY10
     */
    //@JsonProperty("preFY10")
    public void setPreFY10(Boolean preFY10) {
        this.preFY10 = preFY10;
    }

    /**
     *
     * @return The reversal
     */
    //@JsonProperty("reversal")
    public Boolean getReversal() {
        return reversal;
    }

    /**
     *
     * @param reversal
     *            The reversal
     */
    //@JsonProperty("reversal")
    public void setReversal(Boolean reversal) {
        this.reversal = reversal;
    }

    //@JsonAnyGetter
    public Map<String, String> getAdditionalProperties() {
        return this.additionalProperties;
    }

   // @JsonAnySetter
    public void setAdditionalProperty(String name, String value) {
        this.additionalProperties.put(name, value);
    }
}
