package gov.uspto.patent.privatePair.admin.controller;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class PrivatePairCommonController {

    /**
     * Retrieve status currently in HTTP Session object.
     *
     * @param request
     *            {@link HttpServletRequest}
     * 
     * @return
     */
    @GetMapping(value = "/getEntityStatus")
    public String getEntityStatus(HttpServletRequest request) {
        return ((String) request.getSession().getAttribute("currentEntityStatus"));
    }

    /**
     * Retrieve application id currently in HTTP Session object.
     * 
     * @param request
     *            {@link HttpServletRequest}
     * @return
     */
    @GetMapping(value = "/getApplicationId")
    public String getApplicationId(HttpServletRequest request) {
        return ((String) request.getSession().getAttribute("applicationNumber"));
    }

    /**
     * Retrieve Attorney Docket Number id currently in HTTP Session object.
     * 
     * @param request
     *            {@link HttpServletRequest}
     * @return
     */
    @GetMapping(value = "/getAttorneyDocketNumber")
    public String getAttorneyDocketNumber(HttpServletRequest request) {
        return ((String) request.getSession().getAttribute("attorneyDocketNumber"));
    }

    /**
     * Retrieve application/patent title in HTTP Session object.
     * 
     * @param request
     *            {@link HttpServletRequest}
     * @return
     */
    @GetMapping(value = "/getTitle")
    public String getTitle(HttpServletRequest request) {
        return ((String) request.getSession().getAttribute("title"));
    }

    /**
     * TODO: May not be needed
     */
    @RequestMapping(value = "/getCommonName", method = RequestMethod.GET)
    @ResponseBody
    public String getCommonName(HttpServletRequest request) {
        return ((String) request.getSession().getAttribute("commonName"));
    }

    /**
     * Retrieve application id currently in HTTP Session object.
     * 
     * @param request
     *            {@link HttpServletRequest}
     * @return
     */
    @GetMapping(value = "/getRegistrationNumber")
    public String getRegistrationNumber(HttpServletRequest request) {
        return ((String) request.getSession().getAttribute("registrationNumber"));
    }


}
