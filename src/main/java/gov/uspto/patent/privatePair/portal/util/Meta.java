package gov.uspto.patent.privatePair.portal.util;

import java.util.HashMap;
import java.util.Map;

public class Meta {

    private String applicationNumberText;

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    public String getApplicationNumberText() {
        return applicationNumberText;
    }


    public void setApplicationNumberText(String applicationNumberText) {
        this.applicationNumberText = applicationNumberText;
    }


    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }


    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
