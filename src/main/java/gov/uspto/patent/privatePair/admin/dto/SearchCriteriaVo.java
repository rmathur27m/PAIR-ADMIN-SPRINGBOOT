package gov.uspto.patent.privatePair.admin.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
@Data
public class SearchCriteriaVo implements Serializable{
	 private String customerNo = null;
	    private Date startDate = null;
	    private Date endDate = null;
	    private String sortOrder = "DESC";
	    private String errorMessage = null;
}
