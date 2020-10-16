package gov.uspto.patent.privatePair.PCSCommon.dto;

/**
 * This Interface defines all the message constants used in PCS. 
 * 
 * @author Jagadish Puttaswamy
 * @version $Revision: 1.0 $
 * @since $Date: 11/01/2005 11:42:10 $
 *
 */

public interface PcsCommonMessage {
	
	public static final String SERVICE_NOT_AVAIL_PUBLIC="This service is unavailable in Public mode.";
	public static final String NO_CUST_FOR_DN = "No customers found for the requested DN: ";
	public static final String NO_APPS_FOR_CN = "No applications found for the requested Customer Number: ";
	public static final String NO_APPS_FOR_CN_STATUS_DAYS = "None of the application status changed in the selected days range for Customer: ";
	public static final String NO_OUT_GOING_CORR_FOR_CN = "No out going correspondence available for the selected customer(s) in the selected days range: ";
	public static final String NO_APPS_FOR_DKT_NUM = "No applications found for the customers of the requested Attorney: ";
	public static final String NO_REFERENCE_FOR_APP = "No reference data found for the requested Application Number: ";
	public static final String NO_PRACTIONERS_FOR_RN = "No practitioners found for the requested list of Registered Number: ";
	public static final String NO_IFW_IMAGE_FOR_APP = "No IFW image found for the requested Application Number: ";
	public static final String UNABLE_TO_BUILD_PDF = "Unable to build the PDF for the requested Outgoing Correspondence Documents. ";
	public static final String NO_ART_INFO_CLASS = "Invalid class value: ";
	public static final String NO_ART_INFO_GAU = "Invalid GAU value: ";
	public static final String CUS_DTLS_INSERT_FAILURE = "Failed to insert Customer details data";
	public static final String PALM_REQUEST_FAILED = "Palm request failed";
	public static final String PCS_REQUEST_FAILED = "PCS request failed";
	public static final String PCS_INVALID_CUST = "Invalid Customer Number: ";
	public static final String PCS_UNDEFINED_SERVICE = "Undefined Service.";
	public static final String NO_APP_FOR_INPUT = "No application found for the requested input: ";
	
	public static final String PCS_DB_ERROR = "DB_ERROR: Unable to connect to PAIR database, Please try after some time.";
	public static final String OEMS_DB_ERROR = "DB_ERROR: Unable to connect to OEMS database, Please try after some time.";
	public static final String PALM_SERVICE_ERROR = "PALM_SERVICE_ERROR: ";
	
	public static final String PCS_REQ_ERROR = "REQUEST_ERROR: Invalid Request file.";
	public static final String PCS_INVALID_APP_INPUT = "Invalid Application input: ";
	public static final String NO_PUBLICATION_DTLS = "No Publication details found for the requested Application Number: ";
	
	public static final String TOO_MANY_RECS_ERROR= "Too many records found, please refine your criteria and try again";
	public static final String INVALID_PROPERTY_VALUE = "Invalid Property value, please check property table for property key: ";

	public static final String NO_USER_FOR_DN = "No matching user found for the requested DN.";
	public static final String NO_MAPPINGS_FOR_USER = "No matching sponserships found for this user.";
	public static final String REGISTEREDATTORNEY_INACTIVE_MESSAGE = "Registered Attorney is not in Active Staus.";


}
