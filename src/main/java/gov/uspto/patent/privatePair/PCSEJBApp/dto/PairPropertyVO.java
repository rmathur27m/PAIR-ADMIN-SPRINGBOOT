package gov.uspto.patent.privatePair.PCSEJBApp.dto;

import lombok.Data;

@Data
public class PairPropertyVO {
    private String pairMode;
    private String propertyGroup;
    private String propertyKey;
    private String propertyName;
    private Object valueList;
}
