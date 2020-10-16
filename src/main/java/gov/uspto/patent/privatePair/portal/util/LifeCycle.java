package gov.uspto.patent.privatePair.portal.util;

import java.util.HashMap;
import java.util.Map;

public class LifeCycle {

    private Integer beginDate;

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    public Integer getBeginDate() {
        return beginDate;
    }


    public void setBeginDate(Integer beginDate) {
        this.beginDate = beginDate;
    }


    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }


    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }
}
