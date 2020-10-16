package gov.uspto.patent.privatePair.utils;

import javax.servlet.http.HttpServletRequest;

public class IpAddressUtil {
    /**
     * Retrieve IP address.
     *
     * @param request
     *            {@link HttpServletRequest}
     * @return
     */
    public static String getClientIP(HttpServletRequest request) {
        String[] ip_adds = null;
        String ip = "";
        if (null != request.getHeader("X-FORWARDED-FOR")) {
            // If testing is done through VIP
            ip_adds = request.getHeader("X-FORWARDED-FOR").split(",");
            ip = ip_adds[0];
        } else {
            // If testing is done through individual JVM
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
