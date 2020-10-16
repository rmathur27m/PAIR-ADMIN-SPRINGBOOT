package gov.uspto.patent.privatePair.utils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import gov.uspto.pcs.util.PcsLogger;

/**
 *  This exception is thrown when a particular data cannot be found.
 * 
 * @author jputtaswamy 
 * @version 1.0
 * @since Nov 1, 2005
 * 
 */


public class ExceptionData implements Serializable {
	
	private static final Logger  logger = LoggerFactory.getLogger(ExceptionData.class);
	/**
	 * The error number.
	 */
	private String m_errorNumber;

	/**
	 * The error type.
	 */
	private String m_errorType;

	/**
	 * The error message.
	 */
	private String m_errorMessage;

	/**
	 * Has is been logged.
	 */
	private boolean m_isLogged;

	/**
	 * The error Value.
	 */
	private String m_errorValue;

	/**
	 * The user Id.
	 */
	private String m_userId;

	/**
	 * The servername.
	 */
	private String m_serverName;

	/**
	 * The time of the error.
	 */
	private Long m_timestampMillisecs;

	/**
	 * The hashmap to hold data.
	 */
	private static HashMap m_setMethods;

	/**
	 * Static block for holding setMethods HashMap.
	 */
	static {
		m_setMethods = new HashMap();
		// defaults to fastmode=false for intialization speed

		try {
			Class[] clsString = new Class[] { String.class };
			m_setMethods.put(
				"ErrorNumber",
				ExceptionData.class.getMethod("setErrorNumber", clsString));
			m_setMethods.put(
				"ErrorType",
				ExceptionData.class.getMethod("setErrorType", clsString));
			m_setMethods.put(
				"ErrorMessage",
				ExceptionData.class.getMethod("setErrorMessage", clsString));
			m_setMethods.put(
				"ErrorValue",
				ExceptionData.class.getMethod("setErrorValue", clsString));
			m_setMethods.put(
				"UserId",
				ExceptionData.class.getMethod("setUserId", clsString));
			m_setMethods.put(
				"ServerName",
				ExceptionData.class.getMethod("setServerName", clsString));
			m_setMethods.put(
				"TimestampMillisecs",
				ExceptionData.class.getMethod(
					"setTimestampMillisecs",
					clsString));

		} catch (NoSuchMethodException e) {
			logger.error("failed to initialize exception data" + e.getMessage());
		} catch (SecurityException e) {
			//LOG.error("failed to initialize exception data", e);
			logger.error("failed to initialize exception data" + e.getMessage());
		}
	}

	/**
	 * Constructor.
	 */
	public ExceptionData() {
		m_errorNumber = new String();
		m_errorType = new String();
		m_errorMessage = new String();
		m_isLogged = false;

		m_userId = new String(); // intialize with session user ID
		m_serverName = new String(); // intialize with session server name
		m_timestampMillisecs = new Long(System.currentTimeMillis());
		m_errorValue = new String();
	}

	/**
	 * Gets the errorNumber.
	 * 
	 * @return String Returns a String.
	 */

	public String getErrorNumber() {
		return m_errorNumber;
	}

	/**
	 * Sets the errorNumber.
	 * 
	 * @param errorNumber The errorNumber to set.
	 */
	public void setErrorNumber(String errorNumber) {
		m_errorNumber = errorNumber;
	}

	/**
	 * Gets the errorMessage.
	 * 
	 * @return String Returns a String.
	 */
	public String getErrorMessage() {
		return m_errorMessage;
	}

	/**
	 * Sets the errorMessage.
	 * 
	 * @param errorMessage The errorMessage to set.
	 */
	public void setErrorMessage(String errorMessage) {
		m_errorMessage = errorMessage;
	}

	/**
	 * Gets the m_isLogged.
	 * 
	 * @return boolean Returns a boolean.
	 */
	public boolean getIsLogged() {
		return m_isLogged;
	}

	/**
	 * Sets the isLogged.
	 * 
	 * @param isLogged The isLogged to set.
	 */
	public void setIsLogged(boolean isLogged) {
		m_isLogged = isLogged;
	}

	/**
	 * Gets the UserId. Used in combination with ServerName and
	 * TimestampMillsecs to obtain a unique identifier reference used in the
	 * server logger AND the client error message for cross reference purposes.
	 * 
	 * @return String Returns a String.
	 */
	public String getUserId() {
		return m_userId;
	}

	/**
	 * Sets the userId.
	 * 
	 * @param userId The userId to set.
	 */
	public void setUserId(String userId) {
		m_userId = userId;
	}

	/**
	 * Gets the serverName. Used in combination with userId and
	 * TimestampMillsecs to obtain a unique identifier reference used in the
	 * server logger AND the client error message for cross reference purposes.
	 * 
	 * @return String Returns a String.
	 */
	public String getServerName() {
		return m_serverName;
	}

	/**
	 * Sets the ServerName.
	 * 
	 * @param serverName The servername to set.
	 */
	public void setServerName(String serverName) {
		m_serverName = serverName;
	}

	/**
	 * Gets the timestampMillisecs. Used in combination with ServerName and
	 * UserId to obtain a unique identifier reference used in the server logger
	 * AND the client error message for cross reference purposes.
	 * 
	 * @return long Returns a Long.
	 */
	public Long getTimestampMillisecs() {
		return m_timestampMillisecs;
	}

	/**
	 * Sets the timestampMillisecs.
	 * 
	 * @param timestampMillisecs The timestampMillisecs to set. usage:
	 *            myObj.setTimestampMillsecs( System.currentTimeMillis() ).
	 */
	public void setTimestampMillisecs(long timestampMillisecs) {
		m_timestampMillisecs = (Long) m_timestampMillisecs;
	}

	/**
	 * Sets the timestampMillisecs.
	 * 
	 * @param timestampMillisecs The timestampMillisecs to set. usage:
	 *            myObj.setTimestampMillsecs( someString );.
	 */
	public void setTimestampMillisecs(String timestampMillisecs) {
		m_timestampMillisecs = Long.valueOf(timestampMillisecs);
	}

	/**
	 * Gets the UniqueId - a helper method. return value created by string
	 * concatination of userId, ServerName and timestampMillisecs. There is no
	 * setter method. Use the setter methods for userId, Servername and
	 * timestampMillisecs for this instead.
	 * 
	 * @return String Returns a String.
	 */
	public String getUniqueId() {
		return m_serverName + " - " + m_userId + " - " + m_timestampMillisecs;
	}

	/**
	 * Gets the errorValue.
	 * 
	 * @return String Returns a String.
	 */
	public String getErrorValue() {
		return m_errorValue;
	}

	/**
	 * Sets the errorValue.
	 * 
	 * @param errorValue The errorValue to set.
	 */
	public void setErrorValue(String errorValue) {
		m_errorValue = errorValue;
	}

	/**
	 * Gets the errorType.
	 * 
	 * @return String Returns a String.
	 */
	public String getErrorType() {
		return m_errorType;
	}

	/**
	 * Sets the errorType.
	 * 
	 * @param errorType The errorType to set.
	 */
	public void setErrorType(String errorType) {
		m_errorType = errorType;
	}

	/**
	 * Sets the data attribute of this ExceptionData object, specified by
	 * memberName param to that specified in the value param.
	 * 
	 * @param String The 'setter' Method to call.
	 * @param String The value parameter.
	 * @see PacrSAXHandler The PacrSAXHandler calls this method based on the XML
	 *      end tag parsed.
	 */
	public void setMemberValue(String memberName, String value) {

		Method method = (Method) m_setMethods.get(memberName);
		try {
			if (method != null) {
				method.invoke(this, (new String[] { value }));
			}
		} catch (IllegalAccessException e) {
			//LOG.error("failed to set member value", e);
			logger.error("failed to set member value" + e.getMessage());
		} catch (IllegalArgumentException e) {
			//LOG.error("failed to set member value", e);
			logger.error("failed to set member value" + e.getMessage());
		} catch (InvocationTargetException e) {
			//LOG.error("failed to set member value", e);
			logger.error("failed to set member value" + e.getMessage());
		}
	}

}
