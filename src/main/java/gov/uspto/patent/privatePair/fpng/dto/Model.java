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
 * Class Model is a value object for the summary level fee data
 * 
 * @author "srayala"
 */
//@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonPropertyOrder({ "version", "postingReferenceText", "parentId", "feeReferenceGroupCode", "feeReferenceGroupItemName",
//        "feeCode", "startDate", "endDate", "createUserId", "createUserCustomerId", "attorneyDocketNumber", "numResults",
//        "results" })
@Data
public class Model  {

//    private static final long serialVersionUID = 1814177781049380051L;
//    @JsonProperty("version")
    private Integer version;
//    @JsonProperty("postingReferenceText")
    private String postingReferenceText;
//    @JsonProperty("parentId")
    private String parentId;
//    @JsonProperty("feeReferenceGroupCode")
    private String feeReferenceGroupCode;
//    @JsonProperty("feeReferenceGroupItemName")
    private String feeReferenceGroupItemName;
//    @JsonProperty("feeCode")
    private String feeCode;
//    @JsonProperty("startDate")
    private String startDate;
//    @JsonProperty("endDate")
    private String endDate;
//    @JsonProperty("createUserId")
    private String createUserId;
//    @JsonProperty("createUserCustomerId")
    private String createUserCustomerId;
//    @JsonProperty("attorneyDocketNumber")
    private String attorneyDocketNumber;
//    @JsonProperty("numResults")
    private Integer numResults;
//    @JsonProperty("results")
    private List<Result> results = new ArrayList<Result>();
//    @JsonIgnore
    private final Map<String, String> additionalProperties = new HashMap<String, String>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public Model() {
    }

    /**
     * 
     * @param startDate
     * @param attorneyDocketNumber
     * @param numResults
     * @param endDate
     * @param createUserCustomerId
     * @param version
     * @param feeReferenceGroupItemName
     * @param parentId
     * @param results
     * @param feeCode
     * @param postingReferenceText
     * @param feeReferenceGroupCode
     * @param createUserId
     */
    public Model(Integer version, String postingReferenceText, String parentId, String feeReferenceGroupCode,
            String feeReferenceGroupItemName, String feeCode, String startDate, String endDate, String createUserId,
            String createUserCustomerId, String attorneyDocketNumber, Integer numResults, List<Result> results) {
        this.version = version;
        this.postingReferenceText = postingReferenceText;
        this.parentId = parentId;
        this.feeReferenceGroupCode = feeReferenceGroupCode;
        this.feeReferenceGroupItemName = feeReferenceGroupItemName;
        this.feeCode = feeCode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createUserId = createUserId;
        this.createUserCustomerId = createUserCustomerId;
        this.attorneyDocketNumber = attorneyDocketNumber;
        this.numResults = numResults;
        this.results = results;
    }
//
//    /**
//     *
//     * @return The version
//     */
//    @JsonProperty("version")
//    public Integer getVersion() {
//        return version;
//    }
//
//    /**
//     *
//     * @param version
//     *            The version
//     */
//    @JsonProperty("version")
//    public void setVersion(Integer version) {
//        this.version = version;
//    }
//
//    /**
//     *
//     * @return The postingReferenceText
//     */
//    @JsonProperty("postingReferenceText")
//    public String getPostingReferenceText() {
//        return postingReferenceText;
//    }
//
//    /**
//     *
//     * @param postingReferenceText
//     *            The postingReferenceText
//     */
//    @JsonProperty("postingReferenceText")
//    public void setPostingReferenceText(String postingReferenceText) {
//        this.postingReferenceText = postingReferenceText;
//    }
//
//    /**
//     *
//     * @return The parentId
//     */
//    @JsonProperty("parentId")
//    public String getParentId() {
//        return parentId;
//    }
//
//    /**
//     *
//     * @param parentId
//     *            The parentId
//     */
//    @JsonProperty("parentId")
//    public void setParentId(String parentId) {
//        this.parentId = parentId;
//    }
//
//    /**
//     *
//     * @return The feeReferenceGroupCode
//     */
//    @JsonProperty("feeReferenceGroupCode")
//    public String getFeeReferenceGroupCode() {
//        return feeReferenceGroupCode;
//    }
//
//    /**
//     *
//     * @param feeReferenceGroupCode
//     *            The feeReferenceGroupCode
//     */
//    @JsonProperty("feeReferenceGroupCode")
//    public void setFeeReferenceGroupCode(String feeReferenceGroupCode) {
//        this.feeReferenceGroupCode = feeReferenceGroupCode;
//    }
//
//    /**
//     *
//     * @return The feeReferenceGroupItemName
//     */
//    @JsonProperty("feeReferenceGroupItemName")
//    public String getFeeReferenceGroupItemName() {
//        return feeReferenceGroupItemName;
//    }
//
//    /**
//     *
//     * @param feeReferenceGroupItemName
//     *            The feeReferenceGroupItemName
//     */
//    @JsonProperty("feeReferenceGroupItemName")
//    public void setFeeReferenceGroupItemName(String feeReferenceGroupItemName) {
//        this.feeReferenceGroupItemName = feeReferenceGroupItemName;
//    }
//
//    /**
//     *
//     * @return The feeCode
//     */
//    @JsonProperty("feeCode")
//    public String getFeeCode() {
//        return feeCode;
//    }
//
//    /**
//     *
//     * @param feeCode
//     *            The feeCode
//     */
//    @JsonProperty("feeCode")
//    public void setFeeCode(String feeCode) {
//        this.feeCode = feeCode;
//    }
//
//    /**
//     *
//     * @return The startDate
//     */
//    @JsonProperty("startDate")
//    public String getStartDate() {
//        return startDate;
//    }
//
//    /**
//     *
//     * @param startDate
//     *            The startDate
//     */
//    @JsonProperty("startDate")
//    public void setStartDate(String startDate) {
//        this.startDate = startDate;
//    }
//
//    /**
//     *
//     * @return The endDate
//     */
//    @JsonProperty("endDate")
//    public String getEndDate() {
//        return endDate;
//    }
//
//    /**
//     *
//     * @param endDate
//     *            The endDate
//     */
//    @JsonProperty("endDate")
//    public void setEndDate(String endDate) {
//        this.endDate = endDate;
//    }
//
//    /**
//     *
//     * @return The createUserId
//     */
//    @JsonProperty("createUserId")
//    public String getCreateUserId() {
//        return createUserId;
//    }
//
//    /**
//     *
//     * @param createUserId
//     *            The createUserId
//     */
//    @JsonProperty("createUserId")
//    public void setCreateUserId(String createUserId) {
//        this.createUserId = createUserId;
//    }
//
//    /**
//     *
//     * @return The createUserCustomerId
//     */
//    @JsonProperty("createUserCustomerId")
//    public String getCreateUserCustomerId() {
//        return createUserCustomerId;
//    }
//
//    /**
//     *
//     * @param createUserCustomerId
//     *            The createUserCustomerId
//     */
//    @JsonProperty("createUserCustomerId")
//    public void setCreateUserCustomerId(String createUserCustomerId) {
//        this.createUserCustomerId = createUserCustomerId;
//    }
//
//    /**
//     *
//     * @return The attorneyDocketNumber
//     */
//    @JsonProperty("attorneyDocketNumber")
//    public String getAttorneyDocketNumber() {
//        return attorneyDocketNumber;
//    }
//
//    /**
//     *
//     * @param attorneyDocketNumber
//     *            The attorneyDocketNumber
//     */
//    @JsonProperty("attorneyDocketNumber")
//    public void setAttorneyDocketNumber(String attorneyDocketNumber) {
//        this.attorneyDocketNumber = attorneyDocketNumber;
//    }
//
//    /**
//     *
//     * @return The numResults
//     */
//    @JsonProperty("numResults")
//    public Integer getNumResults() {
//        return numResults;
//    }
//
//    /**
//     *
//     * @param numResults
//     *            The numResults
//     */
//    @JsonProperty("numResults")
//    public void setNumResults(Integer numResults) {
//        this.numResults = numResults;
//    }
//
//    /**
//     *
//     * @return The results
//     */
//    @JsonProperty("results")
//    public List<Result> getResults() {
//        return results;
//    }
//
//    /**
//     *
//     * @param results
//     *            The results
//     */
//    @JsonProperty("results")
//    public void setResults(List<Result> results) {
//        this.results = results;
//    }
//
//    @JsonAnyGetter
//    public Map<String, String> getAdditionalProperties() {
//        return this.additionalProperties;
//    }
//
//    @JsonAnySetter
//    public void setAdditionalProperty(String name, String value) {
//        this.additionalProperties.put(name, value);
//    }
//
//    /*
//     * (non-Javadoc)
//     *
//     * @see java.lang.Object#toString()
//     */
//    @Override
//    public String toString() {
//        StringBuilder builder = new StringBuilder();
//        builder.append("Model [version=");
//        builder.append(version);
//        builder.append(", postingReferenceText=");
//        builder.append(postingReferenceText);
//        builder.append(", parentId=");
//        builder.append(parentId);
//        builder.append(", feeReferenceGroupCode=");
//        builder.append(feeReferenceGroupCode);
//        builder.append(", feeReferenceGroupItemName=");
//        builder.append(feeReferenceGroupItemName);
//        builder.append(", feeCode=");
//        builder.append(feeCode);
//        builder.append(", startDate=");
//        builder.append(startDate);
//        builder.append(", endDate=");
//        builder.append(endDate);
//        builder.append(", createUserId=");
//        builder.append(createUserId);
//        builder.append(", createUserCustomerId=");
//        builder.append(createUserCustomerId);
//        builder.append(", attorneyDocketNumber=");
//        builder.append(attorneyDocketNumber);
//        builder.append(", numResults=");
//        builder.append(numResults);
//        builder.append(", results=");
//        builder.append(results);
//        builder.append(", additionalProperties=");
//        builder.append(additionalProperties);
//        builder.append("]");
//        return builder.toString();
//    }
}
