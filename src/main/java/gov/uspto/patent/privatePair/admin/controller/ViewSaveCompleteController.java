package gov.uspto.patent.privatePair.admin.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import gov.uspto.patent.privatePair.admin.domain.Constants;
import gov.uspto.patent.privatePair.admin.domain.ViewSaveCompleteForm;
import gov.uspto.patent.privatePair.admin.domain.ViewSaveCompleteJson;
import gov.uspto.patent.privatePair.admin.service.createcustomernumber.CreateNewCustomerNumberServices;
import gov.uspto.patent.privatePair.admin.service.entitystatus.EntityStatusServices;
import gov.uspto.patent.privatePair.admin.service.updateapplicationaddress.UpdateApplicationAddressServices;
import gov.uspto.patent.privatePair.utils.IpAddressUtil;
import gov.uspto.utils.PairAdminUtil;
import gov.uspto.utils.SortMe;
import gov.uspto.utils.Statistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Helper class; contains various methods to return single pieces of
 * information.
 */
@RestController
@Slf4j
public class ViewSaveCompleteController {

    public static final String IDISPLAY_START = "iDisplayStart";
    @Autowired
    CreateNewCustomerNumberServices createNewCustomerNumberServices;
    @Autowired
    EntityStatusServices entityStatusServices;

    @Autowired
    UpdateApplicationAddressServices updateApplicationAddressServices;
    private String activityEndTime;
    private String activityStartTime;
    public String viewFormRequestDays = "7";
    public String viewFormRequestStatus = "All";
    public String viewFormRequestType = "All";
    public String privatePAIRdn="";

    @GetMapping(value = "/RenderViewSaveComplete")
    public ModelAndView RenderViewForm() {

        return new ModelAndView("ViewSaveCompleteRequest");
    }

    /**
     * Retrieve the currently selected ViewFormRequest type.
     *
     * @return {@link String} Currently selected response type.
     */
    @GetMapping(value = "/getRequestType")
    public String getApplicationId() {

        return this.viewFormRequestType;
    }
    
    

    /**
     * Retrieve the currently selected Attorney Docket Number
     *
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @return {@link String} Currently selected Search type
     */
    @GetMapping(value = "/getSearchType")
    public String getAttorneyDocketNumber() {

        return this.viewFormRequestStatus;
    }

    /**
     * Retrieve Create New Customer Number requests.
     *
     * @param request {@link HttpServletRequest}
     * @return {@link String} List of new customer number request id(s).
     */
    @GetMapping(value = "/getCustomerRequestList")
    public String getCustomerRequestList(@RequestParam("pairAdminrequestType") String pairAdminrequestType,
            @RequestParam("pairAdminrequesStatus") String pairAdminrequesStatus,
            @RequestParam("pairAdminrequestDaysCnt") String pairAdminrequestDaysCnt,
            @RequestParam("privatePAIRdn") String privatePAIRdn, HttpServletRequest request) {
    	
    	viewFormRequestType=pairAdminrequestType;
    	viewFormRequestStatus=pairAdminrequesStatus;
    	viewFormRequestDays=pairAdminrequestDaysCnt;
    	
        List<ViewSaveCompleteForm> viewCustomerList = new ArrayList<ViewSaveCompleteForm>();
        List<String> viewFormRequestStatusList = new ArrayList<String>(3);
        String customerListJson = "";

        int pageNumber = 0;      

        try {

            if (null != request.getParameter(IDISPLAY_START))
                pageNumber = (Integer.parseInt(request.getParameter(IDISPLAY_START)) / 10) + 1;

            if (null != viewFormRequestType
                    && (viewFormRequestType.equals(Constants.CUSTOMER_REQUEST) || viewFormRequestType
                    .equals(Constants.ALL_REQUEST))) {
                if (viewFormRequestStatus.equals(Constants.ALL_REQUEST)) {
                    viewFormRequestStatusList.add(Constants.SAVED_REQUEST);
                    viewFormRequestStatusList.add(Constants.SUBMITTED_REQUEST);
                    viewFormRequestStatusList.add(Constants.FAILED_REQUEST);
                } else
                    viewFormRequestStatusList.add(viewFormRequestStatus);
                System.out.println("CN"+viewFormRequestStatusList);
                System.out.println("CN"+viewFormRequestDays);
                System.out.println("CN"+viewFormRequestType);
                System.out.println("CN"+privatePAIRdn);
                viewCustomerList = createNewCustomerNumberServices.getCustomerRequestsforView(viewFormRequestStatusList,
                        viewFormRequestDays, viewFormRequestType, privatePAIRdn);
            }
            //Checking if pageNumber equal to 1 & 2, do shuffle implies if it's not equal to '0' shuffle the dataList
            if (pageNumber != 0 ) {
                Collections.shuffle(viewCustomerList);
            }

            ViewSaveCompleteJson viewSaveCompleteJson = new ViewSaveCompleteJson();
            viewSaveCompleteJson.setITotalDisplayRecords(viewCustomerList.size());
            viewSaveCompleteJson.setITotalRecords(viewCustomerList.size());
            viewSaveCompleteJson.setAaData(viewCustomerList);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            customerListJson = gson.toJson(viewSaveCompleteJson);

        } catch (JsonSyntaxException e) {
                log.error("Invalid json on getCustomerRequestList for View: " + e);
        } catch (Exception e) {
               log.error("Failed to retrieve getCustomerRequestList for View: " + e);
        }

        if (null != viewFormRequestType
                && (viewFormRequestType.equals(Constants.CUSTOMER_REQUEST) || viewFormRequestType.equals(Constants.ALL_REQUEST))) {
            activityEndTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
            long startTimeMillis = System.currentTimeMillis();

            String statsActivity = Statistics.addPairAdminActivity(viewFormRequestType, privatePAIRdn, Statistics.VIEWSAVECOMPLETEREQUESTLIST,
                    activityStartTime, activityEndTime, "0", startTimeMillis, 0, "0",
                    IpAddressUtil.getClientIP(request));

            log.info("Stats Activity: {} ", statsActivity);

        }

        return customerListJson;
    }

    /**
     * Retrieve a list of Entity Status Change requests
     *
     * @param request {@link HttpServletRequest}
     * @return {@String} List of Entity Status Change request id(s).
     */
    @GetMapping(value = "/getEntityTableList")
    public ViewSaveCompleteJson getEntityTableList(@RequestParam("pairAdminrequestType") String pairAdminrequestType,
            @RequestParam("pairAdminrequesStatus") String pairAdminrequesStatus,
            @RequestParam("pairAdminrequestDaysCnt") String pairAdminrequestDaysCnt,
            @RequestParam("privatePAIRdn") String privatePAIRdn, HttpServletRequest request) throws Exception {
    
    	viewFormRequestType=pairAdminrequestType;
    	viewFormRequestStatus=pairAdminrequesStatus;
    	viewFormRequestDays=pairAdminrequestDaysCnt;
    	
        String entityListJson = "";
        List<String> viewFormRequestStatusList = new ArrayList<String>(3);


        List<ViewSaveCompleteForm> viewDataList = new ArrayList<ViewSaveCompleteForm>();

        int pageNumber = 0;

        if (null != request.getParameter(IDISPLAY_START))
            pageNumber = (Integer.parseInt(request.getParameter(IDISPLAY_START)) / 10) + 1;

        if (null != viewFormRequestType
                && (viewFormRequestType.equals(Constants.ENTITY_STATUS) || viewFormRequestType.equals(Constants.ALL_REQUEST))) // TO-DO
        {
            if (viewFormRequestStatus.equals(Constants.ALL_REQUEST)) {
                viewFormRequestStatusList.add(Constants.SAVED_REQUEST);
                viewFormRequestStatusList.add(Constants.SUBMITTED_REQUEST);
                viewFormRequestStatusList.add(Constants.FAILED_REQUEST);
            } else
                viewFormRequestStatusList.add(viewFormRequestStatus);
            System.out.println("ECR"+viewFormRequestStatusList);
            System.out.println("ECR"+viewFormRequestDays);
            System.out.println("ECR"+viewFormRequestType);
            System.out.println("ECR"+privatePAIRdn);
            viewDataList = entityStatusServices.getEntityRequestsforView(viewFormRequestStatusList, viewFormRequestDays,
                    viewFormRequestType, privatePAIRdn);

        }

        //Checking if pageNumber equal to 1 & 2, do shuffle implies if it's not equal to '0' shuffle the dataList
        if (pageNumber != 0) {
            Collections.shuffle(viewDataList);
        }

        ViewSaveCompleteJson viewSaveCompleteJson = new ViewSaveCompleteJson();
        viewSaveCompleteJson.setITotalDisplayRecords(viewDataList.size());
        viewSaveCompleteJson.setITotalRecords(viewDataList.size());
        viewSaveCompleteJson.setAaData(viewDataList);


        if (null != viewFormRequestType && (viewFormRequestType.equals(Constants.ENTITY_STATUS))) {
            activityEndTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
            long startTimeMillis = System.currentTimeMillis();

            String statsActivity = Statistics.addPairAdminActivity(viewFormRequestType, privatePAIRdn, Statistics.VIEWSAVECOMPLETEREQUESTLIST,
                    activityStartTime, activityEndTime, "0", startTimeMillis, 0, "0",
                    IpAddressUtil.getClientIP(request));

            log.info("Stats Activity: {}",statsActivity );

        }
        return viewSaveCompleteJson;
    }

    /**
     * Retrieve the currently selected caldendar date range
     *
     * @param request  {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @return {@link String} Current calendar date range.
     */
    @GetMapping(value = "/getDaysCount")
    public String getTitle() {

        return this.viewFormRequestDays;
    }

    /**
     * Retrieve a list of Update Application Address Change requests
     *
     * @param request {@link HttpServletRequest}
     * @return {@link String} List of Update Application Address Change request
     * id(s).
     */

    @GetMapping(value = "/getUpdateAddressList")
    public ViewSaveCompleteJson getUpdateAddressList(@RequestParam("pairAdminrequestType") String pairAdminrequestType,
            @RequestParam("pairAdminrequesStatus") String pairAdminrequesStatus,
            @RequestParam("pairAdminrequestDaysCnt") String pairAdminrequestDaysCnt,
            @RequestParam("privatePAIRdn") String privatePAIRdn, HttpServletRequest request) throws Exception {
    	
    	viewFormRequestType=pairAdminrequestType;
    	viewFormRequestStatus=pairAdminrequesStatus;
    	viewFormRequestDays=pairAdminrequestDaysCnt;
    	
        List<ViewSaveCompleteForm> viewDataList = new ArrayList<ViewSaveCompleteForm>();
        List<String> viewFormRequestStatusList = new ArrayList<String>();
        int pageNumber = 0;

        if (null != request.getParameter(IDISPLAY_START))
            pageNumber = (Integer.parseInt(request.getParameter(IDISPLAY_START)) / 10) + 1;

        if (null != viewFormRequestType
                && (viewFormRequestType.equals(Constants.ADDRESS_CHANGE_REQUEST) || viewFormRequestType
                .equals(Constants.ALL_REQUEST))) {
            if (viewFormRequestStatus.equals(Constants.ALL_REQUEST)) {
                viewFormRequestStatusList.add(Constants.SAVED_REQUEST);
                viewFormRequestStatusList.add(Constants.SUBMITTED_REQUEST);
                viewFormRequestStatusList.add(Constants.FAILED_REQUEST);
            } else
                viewFormRequestStatusList.add(viewFormRequestStatus);
            System.out.println("UA"+viewFormRequestStatusList);
            System.out.println("UA"+viewFormRequestDays);
            System.out.println("UA"+viewFormRequestType);
            System.out.println("UA"+privatePAIRdn);
            viewDataList = updateApplicationAddressServices.getUpdateAddrRequestsforView(viewFormRequestStatusList,
                    viewFormRequestDays, viewFormRequestType, privatePAIRdn);
        }

        //Checking if pageNumber equal to 1 & 2, do shuffle implies if it's not equal to '0' shuffle the dataList
        if (pageNumber != 0) {
            Collections.shuffle(viewDataList);
        }

        ViewSaveCompleteJson viewSaveCompleteJson = new ViewSaveCompleteJson();
        viewSaveCompleteJson.setITotalDisplayRecords(viewDataList.size());
        System.out.println(viewDataList.size());
        viewSaveCompleteJson.setITotalRecords(viewDataList.size());
        viewSaveCompleteJson.setAaData(viewDataList);

        activityEndTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
        long startTimeMillis = System.currentTimeMillis();

        String statsActivity = Statistics.addPairAdminActivity(viewFormRequestType, privatePAIRdn, Statistics.VIEWSAVECOMPLETEREQUESTLIST,
                activityStartTime, activityEndTime, "0", startTimeMillis, 0, "0",
                IpAddressUtil.getClientIP(request));

        log.info("Stats Activity: {} ", statsActivity);

        return viewSaveCompleteJson;
    }

    /**
     * Retrieve the
     *
     * @param pairAdminrequestType
     * @param pairAdminrequesStatus
     * @param pairAdminrequestDaysCnt
     * @param privatePAIRdn
     * @param request
     * @return
     */
    @GetMapping(value = "/getViewRequestVal")
    public String getViewRequestVal(@RequestParam("pairAdminrequestType") String pairAdminrequestType,
                                  @RequestParam("pairAdminrequesStatus") String pairAdminrequesStatus,
                                  @RequestParam("pairAdminrequestDaysCnt") String pairAdminrequestDaysCnt,
                                  @RequestParam("privatePAIRdn") String privatePAIRdn, HttpServletRequest request) {

        // Start time
    	request.getSession().setAttribute("DnValue",privatePAIRdn);
    	//privatePAIRdn = (String) request.getSession().getAttribute("DnValue");
        activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();

        

        this.viewFormRequestType = pairAdminrequestType;
        this.viewFormRequestStatus = pairAdminrequesStatus;
        this.viewFormRequestDays = pairAdminrequestDaysCnt;
        this.privatePAIRdn = privatePAIRdn;
System.out.println(this.viewFormRequestType);
System.out.println(this.viewFormRequestStatus);
System.out.println(this.viewFormRequestDays);
System.out.println(this.privatePAIRdn);
        PairAdminUtil.setUserType(privatePAIRdn, request);

        return "values Passed";

    }


}
