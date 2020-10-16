package gov.uspto.patent.privatePair.fpng.dto;

import java.io.Serializable;
import java.util.HashMap;
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
 * Class PaymentDetail is a value object for the payment level metadata fetched
 * by the new FPNG Rest Services
 * 
 * @author "srayala"
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonPropertyOrder({ "version", "transactionId", "paymentMethodType", "paymentMethodDescription" })
@Data
public class PaymentDetail {

    private static final long serialVersionUID = 1409283857376602282L;
//    @JsonProperty("version")
    private Integer version;
//    @JsonProperty("transactionId")
    private String transactionId;
//    @JsonProperty("paymentMethodType")
    private String paymentMethodType;
//    @JsonProperty("paymentMethodDescription")
    private String paymentMethodDescription;
//    @JsonIgnore
    private final Map<String, String> additionalProperties = new HashMap<String, String>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public PaymentDetail() {
    }

    /**
     * 
     * @param transactionId
     * @param paymentMethodDescription
     * @param paymentMethodType
     * @param version
     */
    public PaymentDetail(Integer version, String transactionId, String paymentMethodType, String paymentMethodDescription) {
        this.version = version;
        this.transactionId = transactionId;
        this.paymentMethodType = paymentMethodType;
        this.paymentMethodDescription = paymentMethodDescription;
    }

    /**
     *
     * @return The version
     */
    //@JsonProperty("version")
    public Integer getVersion() {
        return version;
    }

    /**
     *
     * @param version
     *            The version
     */
    //@JsonProperty("version")
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     *
     * @return The transactionId
     */
    //@JsonProperty("transactionId")
    public String getTransactionId() {
        return transactionId;
    }

    /**
     *
     * @param transactionId
     *            The transactionId
     */
    //@JsonProperty("transactionId")
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    /**
     *
     * @return The paymentMethodType
     */
    //@JsonProperty("paymentMethodType")
    public String getPaymentMethodType() {
        return paymentMethodType;
    }

    /**
     *
     * @param paymentMethodType
     *            The paymentMethodType
     */
    //@JsonProperty("paymentMethodType")
    public void setPaymentMethodType(String paymentMethodType) {
        this.paymentMethodType = paymentMethodType;
    }

    /**
     *
     * @return The paymentMethodDescription
     */
    //@JsonProperty("paymentMethodDescription")
    public String getPaymentMethodDescription() {
        return paymentMethodDescription;
    }

    /**
     *
     * @param paymentMethodDescription
     *            The paymentMethodDescription
     */
   // @JsonProperty("paymentMethodDescription")
    public void setPaymentMethodDescription(String paymentMethodDescription) {
        this.paymentMethodDescription = paymentMethodDescription;
    }

    //@JsonAnyGetter
    public Map<String, String> getAdditionalProperties() {
        return this.additionalProperties;
    }

    //@JsonAnySetter
    public void setAdditionalProperty(String name, String value) {
        this.additionalProperties.put(name, value);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PaymentDetail [version=");
        builder.append(version);
        builder.append(", transactionId=");
        builder.append(transactionId);
        builder.append(", paymentMethodType=");
        builder.append(paymentMethodType);
        builder.append(", paymentMethodDescription=");
        builder.append(paymentMethodDescription);
        builder.append(", additionalProperties=");
        builder.append(additionalProperties);
        builder.append("]");
        return builder.toString();
    }
}
