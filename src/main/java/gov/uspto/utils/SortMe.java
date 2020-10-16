package gov.uspto.utils;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Date format for log file.
 * 
 */
public class SortMe {

		/**
		 * Date format for log file.
		 */
    public static String getDateAndTimeInMMDDYYYYHHMISSFormat() {
        Date date = new Date();
        Format formatter = new SimpleDateFormat("MM-dd-yyyy::HH:mm:ss");
        String sysDate = formatter.format(date);
        return sysDate;
    }
}