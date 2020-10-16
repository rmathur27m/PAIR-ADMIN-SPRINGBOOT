package gov.uspto.patent.privatePair.utils;

//import gov.uspto.pcs.util.PcsConstant;

import java.io.Serializable;

/**
 * This exception is thrown when any problem occurs while rendering the requested service. 
 * 
 * @author jputtaswamy 
 * @version 1.0
 * @since Nov 1, 2005
 * 
 */
public class  ServiceException extends Exception implements Serializable {

	/**
	  * Exception Data holder.
	  */
	protected ExceptionData m_exceptionData;

	/**
	 * Constructor for XMLBaseException.
	 * @param message.
	 */
	public ServiceException(String message) {
		super(message);
		m_exceptionData = new ExceptionData();
		m_exceptionData.setIsLogged(false);
		m_exceptionData.setUserId("USER_ID");
		m_exceptionData.setErrorType("SERVICE_TYPE");
		m_exceptionData.setErrorMessage(message);
		m_exceptionData.setTimestampMillisecs(System.currentTimeMillis());

	}

	/**
	 * Gets the exceptionData.
	 * @return ExceptionData Returns a XMLServicesExceptionData.
	 */
	public ExceptionData getExceptionData() {
		return m_exceptionData;
	}
	/**
	 * Sets the exceptionData.
	 * @param exceptionData The exceptionData to set.
	 */
	public void setExceptionData(ExceptionData exceptionData) {
		m_exceptionData = exceptionData;
	}
}
