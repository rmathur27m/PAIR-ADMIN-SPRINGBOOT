package gov.uspto.patent.privatePair.PCSCommon.dto;


import gov.uspto.patent.privatePair.utils.ExceptionData;
import gov.uspto.patent.privatePair.utils.ServiceException;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static gov.uspto.patent.privatePair.admin.dao.DaoUtil.daoUtil;

/**
 * Singleton Constant class for PCS Components. This class loads all of the
 * resources and property files. It provides simple access methods for common
 * utilities.
 *
 * @author Jagadish Puttaswamy
 * @version $Revision: 1.0 $
 * @since $Date: 11/01/2005 11:42:10 $
 */
@Data
public class PcsCommonConstant {

    public static final String ACTION_TIME = "actionTime";
    public static final String ATTORNEY_LIST_BY_AUTH = "AttorneyListByAuth";
    public static final String ATTORNEY_LIST_BY_AUTH_DN = "AttorneyListByAuthDN";
    public static final String ATTORNEY_LIST_BY_AUTH_USERIDS = "AttorneyListByAuthUserIds";
    /**
     * Static variable that defines a String constant for HTTP request attribute
     * named client_id.
     */
    public static final String ATTRIBUTE_CLIENT_ID = "CLIENT_ID";
    /**
     * Static variable that defines a String constant for HTTP request attribute
     * named request.
     */
    public static final String ATTRIBUTE_REQUEST = "REQUEST";
    /**
     * Static variable that defines a String constant for HTTP request attribute
     * named request_d.
     */
    public static final String ATTRIBUTE_REQUEST_ID = "REQUEST_ID";
    /**
     * Static variable that defines a String constant for HTTP request attribute
     * named response.
     */
    public static final String ATTRIBUTE_RESPONSE = "RESPONSE";
    /**
     * Static variable that defines a String constant for HTTP request attribute
     * named service_type.
     */
    public static final String ATTRIBUTE_SERVICE_TYPE = "SERVICE_TYPE";
    /**
     * Static variable that defines a String constant for HTTP request attribute
     * named session_id.
     */
    public static final String ATTRIBUTE_SESSION_ID = "SESSION_ID";
    /**
     * Static variable that defines a String constant for HTTP request attribute
     * named user_id.
     */
    public static final String ATTRIBUTE_USER_ID = "USER_ID";
    public static final String AUTH_LIST_BY_ATTORNEY = "AuthListByAttorney";
    public static final String AUTH_LIST_BY_ATTORNEY_DN = "AuthListByAttorneyDN";
    public static final String AUTH_LIST_BY_ATTORNEY_RNS = "AuthListByAttorneyRNs";
    public static final String CERTIFICATE_TYPE = "CertificateType";
    public static final String CUST_NUM_AND_NAME_DATA = "custNoAndNameData";
    public static final String CUST_NUM_AND_NAME_FRM_DN = "custNoAndNameFromDN";
    public static final String CUST_NUM_FRM_DN = "custNoFromDN";
    public static final String EMPTY = "EMPTY";
    /**
     * Static variable that defines a String constant for empty string.
     */
    public static final String EMPTY_STRING = "";
    public static final String ERROR = "error";
    public static final String FAILURE = "FAILURE";
    public static final String FALSE = "FALSE";
    public static final String MODE = "MODE";
    public static final String PALM_BUSINESS_ERROR_RETURN_CODE = "-1";
    public static final String PALM_CONNECTION_ERROR_REASON_CODE = "9001";
    public static final String PALM_ERROR_RETURN_CODE = "-91";
    public static final String PALM_NO_DATA_FOUND_REASON_CODE = "10";
    //	PALM Return and Reason Codes
    public static final String PALM_SUCCESS_RETURN_CODE = "1";
    public static final String PALM_SYSTEM_ERROR_REASON_CODE = "9003";
    public static final String PALM_TIME_OUT_ERROR_REASON_CODE = "9002";
    public static final String PRIVATE = "PRIVATE";
    public static final String PUBLIC = "PUBLIC";
    public static final String REG_PRACTITIONER_LIST = "RegPractitionerList";
    public static final String REG_PRACTITIONER_LIST_DATA = "RegPractitionerListData";
    public static final String REQUEST_START = "requestStart";
    public static final String STATUS = "STATUS";
    public static final String STATUS_MESSAGE = "STATUS_MESSAGE";
    public static final String SUCCESS = "SUCCESS";
    public static final String SYS_INFO_LIST = "SysInfoList";
    public static final String TRUE = "TRUE";
    private static final Logger log = LoggerFactory.getLogger(PcsCommonConstant.class);
    private static PcsCommonConstant home;
    private static Properties serverProperties = null;

    private PcsCommonConstant() throws IOException {
        home = this;
        init();
    }

    public void init() throws IOException {
        FileInputStream fIn=null;
        try {

            InitialContext initialcontext = new InitialContext();
            String propertyFile = (String) initialcontext.lookup("java:comp/env/PCS_COMMON_PROPERTY_FILE");
            log.info("PCSCommon property file path: " + propertyFile);
            System.out.println("PCSCommon property file path: " + propertyFile);

             fIn = new FileInputStream(propertyFile);
            serverProperties = new Properties();
            serverProperties.load(fIn);

        } catch (Exception exp) {
            log.error("PcsCommonContant:init(): Error occured while initializing the properties." + exp);
            System.out.println("PcsCommonContant:init(): Error occured while initializing the properties." + exp);
        }
        finally {

            if(fIn !=null){
                fIn.close();
            }

        }
    }

    /**
     * All classes that want to throw an exception should use this Home method.
     *
     * @param e        Exception that has to be thrown.
     * @param request  HttpRequest Object.
     * @param response HttpResponse Object.
     */

    public void throwException(final Exception e,
                               final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {

        String userID = (String) request
                .getAttribute(PcsCommonConstant.ATTRIBUTE_USER_ID);
        String serviceType = (String) request
                .getAttribute(PcsCommonConstant.ATTRIBUTE_SERVICE_TYPE);
        String errorMessage = e.getMessage();
        if (errorMessage == null) {
            errorMessage = "no message";
        }


        if (e instanceof ServiceException) {
            ServiceException e2 = (ServiceException) e;
            ExceptionData exceptionData = e2.getExceptionData();
            if (!exceptionData.getIsLogged()) {
                log.error("service error [" + userID + "," + serviceType
                        + "]: " + errorMessage);
            }
            request.setAttribute("exceptionList", exceptionData);
        } else {
            log.error("service error [" + userID + "," + serviceType + "]: "
                    + errorMessage);
            //build Exception data object
            ExceptionData exceptionData = new ExceptionData();
            if (null == e.getMessage()) {
                exceptionData
                        .setErrorMessage("Unable to process request at this time.");
            } else {
				
				/*if(e.getMessage().indexOf("DBERROR:") != -1)
					exceptionData.setErrorMessage(e.getMessage().substring(errorMessage.indexOf("DBERROR:")));
				else */

                exceptionData.setErrorMessage(e.getMessage());
            }
            exceptionData.setIsLogged(true);
            exceptionData.setErrorNumber("-1");
            exceptionData.setErrorType((String) request
                    .getAttribute(PcsCommonConstant.ATTRIBUTE_SERVICE_TYPE));
            exceptionData.setServerName(request.getServerName());
            exceptionData.setUserId((String) request
                    .getAttribute(PcsCommonConstant.ATTRIBUTE_USER_ID));
            exceptionData.setTimestampMillisecs(System.currentTimeMillis());
            request.setAttribute("exceptionList", exceptionData);
        }
        //add the XMLDataObject to the HTTPServletRequest object
        request.setAttribute(PcsCommonConstant.ATTRIBUTE_RESPONSE, e);
        return;
    }

}

