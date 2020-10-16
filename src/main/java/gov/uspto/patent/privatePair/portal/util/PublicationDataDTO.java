package gov.uspto.patent.privatePair.portal.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublicationDataDTO {


    private Meta meta;

    private List<PubicationResultsDTO> results = null;

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    public Meta getMeta() {
        return meta;
    }


    public void setMeta(Meta meta) {
        this.meta = meta;
    }


    public List<PubicationResultsDTO> getResults() {
        return results;
    }


    public void setResults(List<PubicationResultsDTO> results) {
        this.results = results;
    }


    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }


    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
