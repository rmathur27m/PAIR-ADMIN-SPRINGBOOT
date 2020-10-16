package gov.uspto.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpServletRequest;

/**
 * Helper class used with PAIRAdmin processing. Contains constants and two
 * helper methods.
 * 
 * 
 */
public class PairAdminUtil {

	private static final Logger  logger = LoggerFactory.getLogger(PairAdminUtil.class);
    public static final String USER_TYPE = "userType";
    public static final String REGISTERED_ATTORNEYS = "Registered Attorneys";
    public static final String INDEPENDENT_INVENTOR = "Independent Inventor";
    public static Pattern prosePattern = Pattern.compile(INDEPENDENT_INVENTOR);

    /**
     * setUserType
     */
    public static void setUserType(String pair_dn, HttpServletRequest request) {
        Matcher matcher = prosePattern.matcher(pair_dn);
        if (matcher.find()) {
            request.getSession().setAttribute(USER_TYPE, INDEPENDENT_INVENTOR);
            logger.info(request.getSession().getAttribute("userType").toString());
    }
        else {
            request.getSession().setAttribute(USER_TYPE, REGISTERED_ATTORNEYS);
            logger.info(request.getSession().getAttribute("userType").toString());
        }
    }

    /**
     * check isProse
     */
    public static boolean isProse(String pair_dn) {
        Matcher matcher = prosePattern.matcher(pair_dn);
        if (matcher.find())
            return true;
        else
            return false;
    }
}
