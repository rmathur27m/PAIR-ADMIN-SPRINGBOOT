package gov.uspto.patent.privatePair.utils;

/**
 * This Interface defines all the message constants used in PCS EJB Project. 
 * 
 * @author Jagadish Puttaswamy
 * @version $Revision: 1.0 $
 * @since $Date: 12/12/2005 11:42:10 $
 *
 */

public interface PcsEJBConstant {

	//DB connection error message.
	public static final String PCS_JDBC_LOOKUP = "java:comp/env/jdbc/pcs";
	public static final String OEMS_JDBC_LOOKUP = "java:comp/env/jdbc/oems";
	public static final String DB_INVALID_CONNECTION_DETAILS = "DB_ERROR: Invalid credentials.";
	public static final String DB_CONNECTION_ERROR = "DB_ERROR: Unable to connect to PAIR database.";
	public static final String DB_OEMS_CONNECTION_ERROR = "DB_ERROR: Unable to connect to OEMS database.";
	//public static final String DB_INVALID_USERNAME_PASSWORD = "DB_ERROR: Invalid username/password.";
	public static final String DB_ERROR_CODE = "DB_ERROR: ORACLE ERROR CODE: ";
	
	public static final String STATUS = "STATUS";
	public static final String RECORD_COUNT ="RECORD_COUNT";
	public static final String RESULT_LIST = "RESULT_LIST";
	public static final String DISPLAY_LIST = "DISPLAY_LIST";
	public static final String RANGE_LIST = "RANGE_LIST";
	public static final String STATUS_MESSAGE ="STATUS_MESSAGE";
	public static final String SUCCESS = "SUCCESS";
	public static final String ERROR = "ERROR";
	public static final String SUCCESS_RETRIEVE	= "Successfully retrieved";
	public static final String NO_REC_FOUND = "No record found for this search";
	public static final String LIMIT_EXCEED_MSG = "Resultant list exceeded the limit, please refine the search";
	public static final String PCS_DB_ERROR = "DB_ERROR: Unable to connect to PAIR database.";

	public static final String PCS_REQUEST_FAILED =	"Request failed. Please try after some time.";
	public static final String PATENT_NUMBER_REGEX="(?i)^(D|X|H|T)(\\d{6}|\\d{3},\\d{3})$|^(PP|RE|AI)(\\d{5}|\\d{2},\\d{3})$|^[1-9]\\d{0,2}(,\\d{3}){2,3}$|^[1-9](,\\d{3}){4}$|^[1-9]\\d{6,12}$";
	public static final String FORMAT_PATENT_NUMBER="(\\d)(?=(\\d{3})+$)";
}
