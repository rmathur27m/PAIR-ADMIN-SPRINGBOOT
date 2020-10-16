package gov.uspto.patent.privatePair.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author jsapre
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class StringUtil
{
	
	private static final Logger  logger = LoggerFactory.getLogger(StringUtil.class);
    // Assumed that inStrArray will have exactly one String which is the string we are tokenizing and mutating
    public static String tokenize(String [] inStrArr, String separatorStr)
    {
        String retStr = null;
        try
        {
            if(inStrArr[0] == null || inStrArr[0].equals(""))
            {
                return retStr;
            }

            int sepLen = separatorStr.length();

            int sepIndex = inStrArr[0].indexOf(separatorStr);

            if(sepIndex != -1)
            {
                retStr = inStrArr[0].substring(0, sepIndex);
                int nextIndex = sepIndex + sepLen;
                inStrArr[0] = inStrArr[0].substring(nextIndex);
            }
            else
            {
                retStr = inStrArr[0]; // take a copy to return
                inStrArr[0] = null;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return retStr;
    } //

    public static String replace(String srcStr, String oldStr, String newStr)
    {
        String retStr = "";
    	try
		{
			if(srcStr != null && !srcStr.equals("") && oldStr != null && !oldStr.equals(""))
			{
                StringBuffer retStrBuf = new StringBuffer(srcStr.length());
				int oldLength = oldStr.length();
				int startIndex = 0;
				int index = srcStr.indexOf(oldStr, startIndex);
				int length = srcStr.length();

				while(index != -1)
				{
					retStrBuf.append( srcStr.substring(startIndex, index) + newStr );
					startIndex = index + oldLength;
					index = srcStr.indexOf(oldStr, startIndex);
				}

				retStrBuf.append(srcStr.substring(startIndex));
                retStr = retStrBuf.toString();
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return retStr;
    }


    public String getFormattedApplicationNo(String getAppNumber)
	{
		if( null == getAppNumber || getAppNumber.trim().length() ==0)
			return "-";

		String  appNum = getAppNumber;
		String formattedNo = "";

		if( getAppNumber.length() > 8)
		{
			formattedNo = getAppNumber;
			//If the lenght of Appl number is more than 8 then it should be a PCT case and in that
			//case dont change the format.
/*			String tempStr = "";
			for( int i = 0; i < appNum.length(); i++)
			{
				if( appNum.substring( i,(i+1)).equalsIgnoreCase("/"))
				{
				}
				else
				{
					tempStr = tempStr+appNum.substring( i,( i+1));
				}
			}
			StringBuffer str = new StringBuffer();
			str.append(tempStr.substring(0,3));
			str.append("/");
			str.append(tempStr.substring(3,7));
			str.append("/");
			str.append(tempStr.substring(7,tempStr.length()));
			formattedNo = str.toString();                              */
		}
		else{
			//If it is equal to 8 format it.. If it is less than 8 append left zeros and format it.
			String tempString = "00000000";
			if(getAppNumber.length()<8)
				getAppNumber = tempString.substring(0,(8-getAppNumber.length()))+getAppNumber;

			formattedNo = getAppNumber.substring(0, 2)
			+ "/"
			+ getAppNumber.substring(2,5)
			+ ","
			+ getAppNumber.substring(5,getAppNumber.length());
		}
		return formattedNo;
	} //private String getFormattedApplicationNo(String getAppNumber)


	public String formatInputPatentNumber(String number) {
		/* This code will use a REGular EXpression to validate the Patent Number and then use another REGEX to format the Patent Number
		 * with commas.  The REGEX and messages for the Patent Number are now in the PCS.properties file.
		 *
		 * Mark Wheeler		Salient CRGT		2017-08-07		Utility Patent Number Expansion (UPNE) Project
		 */
		String toBeReturned = "-";

		if (null != number && !"".equalsIgnoreCase(number) && !"null".equalsIgnoreCase(number)&& !"-".equalsIgnoreCase(number)&& number.trim().length() != 0) {
			try {
				if (number.matches(PcsEJBConstant.PATENT_NUMBER_REGEX)) {
					toBeReturned = number.replaceAll(PcsEJBConstant.FORMAT_PATENT_NUMBER, "$1,");
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return toBeReturned;
	}

	public String dateDisplayFormat(String strInputDate)
	{
		String strOuputDate = "-";
		try{

			SimpleDateFormat inputDateFormatter = new SimpleDateFormat("yyyyMMdd");
			inputDateFormatter.setCalendar(Calendar.getInstance());

			if (null != strInputDate && !"".equalsIgnoreCase( strInputDate) && !"null".equalsIgnoreCase( strInputDate) && !"-".equalsIgnoreCase( strInputDate) && strInputDate.trim().length() != 0)
			{
				Date fileDate= inputDateFormatter.parse(strInputDate);
				SimpleDateFormat outPutDateFormatter = new SimpleDateFormat("MM-dd-yyyy");
				strOuputDate = outPutDateFormatter.format(fileDate);
			}

		}catch(ParseException pe)
		{
			logger.error("ERROR: CustdetailsDAO.dateDisplayFormat() :"+pe.getMessage());
		}
		return strOuputDate;

	}

	public static List getTokenizedList(String strTemp, String delim)
	{
		List listTemp = new ArrayList();
		StringTokenizer stTokens = new StringTokenizer(strTemp, delim);
		while(stTokens.hasMoreTokens())
		{
			listTemp.add(stTokens.nextToken());
		}

		return listTemp;
	}

	 public static long diffDates(Date start, Date end) {
        Calendar calStart = Calendar.getInstance();
        calStart.setTime(start);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(end);
        Calendar d = Calendar.getInstance();
        d.setTimeInMillis(calEnd.getTimeInMillis() - calStart.getTimeInMillis());
        return d.getTime().getTime()/86400000;
    }

	 public static String diffDates(Calendar start, Calendar end) {
        Calendar d = Calendar.getInstance();
        return (end.getTimeInMillis() - start.getTimeInMillis())/1000 +"."+ (end.getTimeInMillis() - start.getTimeInMillis())%1000;
    }

    /**
	 * Returns a trimed value of the specified string if it is not null, an empty string otherwise
	 *
	 * @param data - The String that need to be checked for null value
	 * @return
	 * @modified on 6/19/2009 - eOffice Action
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
