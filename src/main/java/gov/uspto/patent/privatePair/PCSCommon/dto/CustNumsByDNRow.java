package gov.uspto.patent.privatePair.PCSCommon.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * CustNumsByDNRow class is serializable value object. 
 * Customer numbers that are associated  with the distinguished name
 * are queried from the database. The result rows that returned from the database
 * are stored in CustNumsByDNRow object. These objects are inturn added to  
 * to the vector collection object and returned to the client
 */

@Data
public class CustNumsByDNRow implements Serializable
{
	String custNumStr;
	String insertDate;
	String updateDate;
	String pilotTxt;

	public CustNumsByDNRow()
	{
	}
	/**
	 * Constructor for CustNumsByDNRow.
	 */
	public CustNumsByDNRow(String custNumStr, String insertDate, String updateDate) {
        this.custNumStr = custNumStr;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
    }

    public CustNumsByDNRow(String custNumStr, String insertDate, String updateDate, String pilotTxt) {
        this.custNumStr = custNumStr;
        this.insertDate = insertDate;
        this.updateDate = updateDate;
        this.pilotTxt = pilotTxt != null ? pilotTxt : "N";
    }
}

