package gov.uspto.patent.privatePair.PCSCommon.dto;

/**
 * This Interface defines all the message constants used in PCS EJB Project. 
 * 
 * @author Jagadish Puttaswamy
 * @version $Revision: 1.0 $
 * @since $Date: 12/12/2005 11:42:10 $
 *
 */

public interface PcsCommonDataConstant {

	//DB connection error message.
	public static final String PCS_JDBC_LOOKUP = "java:comp/env/jdbc/pcsCommon";
	public static final String OEMS_JDBC_LOOKUP = "java:comp/env/jdbc/oems";

	public static final String DB_CONNECTION_ERROR = "DB_ERROR: Unable to connect to PAIR database.";
	public static final String SYSTEM_ERROR = "System Error: Please contact the system admin.";
	public static final String DB_OEMS_CONNECTION_ERROR = "DB_ERROR: Unable to connect to OEMS database.";
	
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
		
}
