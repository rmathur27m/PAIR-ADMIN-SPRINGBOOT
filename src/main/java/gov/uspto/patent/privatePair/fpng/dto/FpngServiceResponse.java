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



/**
 * Class FpngServiceResponse is a value object for the FPNG Service level
 * Response fetched by the new FPNG Rest Services
 * 
 * @author "srayala"
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonPropertyOrder({ "success", "errorMessageText", "infoMessageText", "warnMessageText", "domain" })
public class FpngServiceResponse {

    private static final long serialVersionUID = 7652731353528919291L;
//    @JsonProperty("success")
    private Boolean success;
//    @JsonProperty("errorMessageText")
    private List<String> errorMessageText = new ArrayList<String>();
//    @JsonProperty("infoMessageText")
    private List<String> infoMessageText = new ArrayList<String>();
//    @JsonProperty("warnMessageText")
    private List<String> warnMessageText = new ArrayList<String>();
//    @JsonProperty("domain")
    private List<Model> model = new ArrayList<Model>();
//    @JsonIgnore
    private final Map<String, String> additionalProperties = new HashMap<String, String>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public FpngServiceResponse() {
    }

    /**
     * 
     * @param model
     * @param infoMessageText
     * @param errorMessageText
     * @param warnMessageText
     * @param success
     */
    public FpngServiceResponse(Boolean success, List<String> errorMessageText, List<String> infoMessageText,
            List<String> warnMessageText, List<Model> model) {
        this.success = success;
        this.errorMessageText = errorMessageText;
        this.infoMessageText = infoMessageText;
        this.warnMessageText = warnMessageText;
        this.model = model;
    }

    /**
     * 
     * @return The success
     */
//    @JsonProperty("success")
    public Boolean getSuccess() {
        return success;
    }

    /**
     * 
     * @param success
     *            The success
     */
//    @JsonProperty("success")
    public void setSuccess(Boolean success) {
        this.success = success;
    }

    /**
     * 
     * @return The errorMessageText
     */
//    @JsonProperty("errorMessageText")
    public List<String> getErrorMessageText() {
        return errorMessageText;
    }

    /**
     * 
     * @param errorMessageText
     *            The errorMessageText
     */
//    @JsonProperty("errorMessageText")
    public void setErrorMessageText(List<String> errorMessageText) {
        this.errorMessageText = errorMessageText;
    }

    /**
     * 
     * @return The infoMessageText
     */
//    @JsonProperty("infoMessageText")
    public List<String> getInfoMessageText() {
        return infoMessageText;
    }

    /**
     * 
     * @param infoMessageText
     *            The infoMessageText
     */
//    @JsonProperty("infoMessageText")
    public void setInfoMessageText(List<String> infoMessageText) {
        this.infoMessageText = infoMessageText;
    }

    /**
     * 
     * @return The warnMessageText
     */
//    @JsonProperty("warnMessageText")
    public List<String> getWarnMessageText() {
        return warnMessageText;
    }

    /**
     * 
     * @param warnMessageText
     *            The warnMessageText
     */
//    @JsonProperty("warnMessageText")
    public void setWarnMessageText(List<String> warnMessageText) {
        this.warnMessageText = warnMessageText;
    }

    /**
     * 
     * @return The domain
     */
//    @JsonProperty("domain")
    public List<Model> getModel() {
        return model;
    }

    /**
     * 
     * @param model
     *            The domain
     */
//    @JsonProperty("domain")
    public void setModel(List<Model> model) {
        this.model = model;
    }

//    @JsonAnyGetter
    public Map<String, String> getAdditionalProperties() {
        return this.additionalProperties;
    }

//    @JsonAnySetter
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
        builder.append("FpngServiceResponse [success=");
        builder.append(success);
        builder.append(", errorMessageText=");
        builder.append(errorMessageText);
        builder.append(", infoMessageText=");
        builder.append(infoMessageText);
        builder.append(", warnMessageText=");
        builder.append(warnMessageText);
        builder.append(", domain=");
        builder.append(model);
        builder.append(", additionalProperties=");
        builder.append(additionalProperties);
        builder.append("]");
        return builder.toString();
    }
}
