package gov.uspto.patent.privatePair.PCSCommon.dto;

import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

/**
 * This class defines the utility methods required in PCS project
 * @modified to handle new return codes from PALM service - 06/19/2009
 * 
 * @author Jagadish Puttaswamy 
 * @version $Revision: 1.0 $
 * @since $Date: 2005/11/04 11:42:10 $
 * 
 */
@Data
public class PcsUtil {
	
	private static PcsUtil pcsUtilInstance;

	/**
	 * This method peforms the date difference and returns the number of calendar dates
	 * between the end date and start date.
	 * Returns negative value if end date is earlier than start date.
	 * @param String start
	 * @param String end
	 * @return int 
	 */
	
	private PcsUtil() {
		pcsUtilInstance = this;
	}
	
	public synchronized static PcsUtil getInstance() {
		if (pcsUtilInstance==null) {
			pcsUtilInstance = new PcsUtil();
		}
		return pcsUtilInstance;
	}
	
	public static int dayDiffStrDate(String start, String end)
	{
		try
		{
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = dateFormat.parse(start);
			Date endDate = dateFormat.parse(end);
			GregorianCalendar calStart = new GregorianCalendar();
			calStart.setTime(startDate);
			GregorianCalendar calEnd = new GregorianCalendar();
			calEnd.setTime(endDate);
			if (calStart.get(Calendar.YEAR) == calEnd.get(Calendar.YEAR))
			{
				return calEnd.get(Calendar.DAY_OF_YEAR) - calStart.get(Calendar.DAY_OF_YEAR);
			}
			else if ((calEnd.get(Calendar.YEAR) - calStart.get(Calendar.YEAR)) == 1)
			{
				int daysEndYear = calEnd.get(Calendar.DAY_OF_YEAR);
				int daysStartYear = calStart.getActualMaximum(Calendar.DAY_OF_YEAR) - calStart.get(Calendar.DAY_OF_YEAR);
				return daysEndYear + daysStartYear;
			}
			else
			{
				int startYear = calStart.get(Calendar.YEAR);
				int endYear = calEnd.get(Calendar.YEAR);
				GregorianCalendar cal = new GregorianCalendar();
				int days = 0;
				for (int i = startYear + 1; i < endYear; i++)
				{
				cal.set(Calendar.YEAR, i);
				days += cal.getActualMaximum(Calendar.DAY_OF_YEAR);
				}
				days += calEnd.get(Calendar.DAY_OF_YEAR);
				days += (calStart.getActualMaximum(Calendar.DAY_OF_YEAR) - calStart.get(Calendar.DAY_OF_YEAR));
				return days;
			}
		}
		catch (ParseException exp)
		{
			return -1;
		}
	} //public int dayDiffStrDate(String start, String end)


	/**
	 * This method peforms the date difference and returns the number of calendar dates
	 * between the end date and start date.
	 * Returns negative value if end date is earlier than start date.
	 * @param java.sql.Date startDt
	 * @param java.sql.Date endDt
	 * @return int 
	 */	
	
	public static int dayDiffSqlDate(java.sql.Date startDt, java.sql.Date endDt)
	{
		String strStartDate = startDt.toString();
		String strEndDate = endDt.toString();
		return dayDiffStrDate( strStartDate, strEndDate);		
	}
	
	 public static String timeDiff(Calendar start, Calendar end) {
        return (end.getTimeInMillis() - start.getTimeInMillis())/1000 +"."+ (end.getTimeInMillis() - start.getTimeInMillis())%1000;
    }

	/**
	 * This method is used in alignment of string for proper display on screen. 
	 * It replace the space nearest to 80 char length with line break(<BR>).
	 * 
	 * @param String inputStr
	 * @return String 
	 */		
	public static String includeLineBreak(String inputStr)
	{
		StringBuffer strBuf = new StringBuffer();
		int iBreak=0,i=1;
		StringTokenizer strToken = new StringTokenizer(inputStr);
		while(strToken.hasMoreTokens())
		{
			String tempToken=strToken.nextToken();
						
			if(iBreak+tempToken.length()>(80))
			{
				strBuf.append(tempToken).append("<BR>");
				iBreak=0;
			}else
				strBuf.append(tempToken).append(" ");
			
			iBreak+=tempToken.length();
		} //while(strTxt1Token.hasMoreTokens())
					
		return strBuf.toString();
		
	}//includeLineBreak(String str)

	/*
	public static String getCertificateType(String dn)
	{
		String certificateType="";
		try
		{
			StringTokenizer stDn = new StringTokenizer(dn,",");
			int tokenCount=1;
			while(stDn.hasMoreTokens())
			{
				String tempString=stDn.nextToken();
				if(tokenCount==2)
				{
					certificateType=tempString.trim().substring(3);
					break;
				}
				tokenCount++;
			}
			}catch(Exception exp)
			{
				//Ignroe exception
				System.out.println("ERROR: " + exp.getClass().getName() + ": getCertificateType(DN):" + exp.getLocalizedMessage() );
			}
			
		return certificateType;
	}
	*/
	
	 public static String[] getCNAndGroup(String dn)
	    {
	    	String [] retArray = {null, null};
	    	String commonName = "";
	    	String group = "";
			try
			{
				int commaIndex = dn.indexOf(',');
				if(commaIndex == -1) // failure to find ,
				{
				    commonName = "BAD_DATA";
				    group      = "BAD_DATA";
				}
				else // found the ,
				{
					int equaltoIndex = dn.indexOf('=');
					if (equaltoIndex > commaIndex)
					{
						equaltoIndex = -1;
					}
				    commonName = dn.substring(equaltoIndex + 1, commaIndex);
				    int secondCommaIndex = dn.indexOf(',', commaIndex + 1);
				    if(secondCommaIndex == -1) // failure to find ,
				    {
				        group      = "BAD_DATA";
				    }
				    else
				    {
				    	equaltoIndex = dn.indexOf('=', commaIndex + 1);
						if (equaltoIndex > secondCommaIndex)
						{
							equaltoIndex = commaIndex;
						}			    	
				        group = dn.substring(equaltoIndex + 1, secondCommaIndex);
				    }
				}
				retArray[0] = commonName;
				retArray[1] = group;
			}
			catch (Exception e)
			{
				e.printStackTrace();			
			}
			return retArray;
	    }
	 
	 
	public static String stripUserIdPrefix(String userIdwithPrefix)
	{
		//If the Prefix letter is R then first 2 character is stripped, as we are appending 'R0' to the
		//PALM registration number. For all other types first character is stipped
		if(userIdwithPrefix.substring(0,1).equalsIgnoreCase("R")){
			return userIdwithPrefix.substring(2);
		}else{
			return userIdwithPrefix.substring(1);
		}
	}

	
	/*
	 * If the 2nd parameter ifOnlyR is true then strip the prefix only if the first letter is R
	 */
	public static String stripUserIdPrefix(String userIdwithPrefix, boolean ifOnlyR)
	{
		//If the Prefix letter is R then first 2 character is stripped, as we are appending 'R0' to the
		//PALM registration number. For all other types first character is stipped
		if(userIdwithPrefix.substring(0,1).equalsIgnoreCase("R")){
			return userIdwithPrefix.substring(2);
		}else{
			if(ifOnlyR){
				//Returns without stripping anything
				return userIdwithPrefix;
			}else
			{
				return userIdwithPrefix.substring(1);
			}
		}
	}
	
	/**
	 * Returns a trimed value of the specified string if it is not null, an empty string otherwise 
	 * 
	 * @param data - The String that need to be checked for null value
	 * @return
	 */
	public static String noNulls( String data ) 
	{
		if (data == null) 
	    {
			return "";
	    } 
		else 
		{
			return data.trim();
	    }
	}
}
