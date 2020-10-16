package gov.uspto.patent.privatePair.admin.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import gov.uspto.patent.privatePair.admin.domain.CreateNewCustomerNumberForm;
import gov.uspto.patent.privatePair.admin.domain.JsonResponse;
import gov.uspto.patent.privatePair.admin.dto.PairUserDnDto;
import gov.uspto.patent.privatePair.admin.service.common.PairAdminHelperServices;
import gov.uspto.patent.privatePair.admin.service.common.RequestStatus;
import gov.uspto.patent.privatePair.admin.service.createcustomernumber.CreateNewCustomerNumberServices;
import gov.uspto.patent.privatePair.admin.validator.CreateNewCustomerNumberValidator;
import gov.uspto.patent.privatePair.common.UserType;
import gov.uspto.patent.privatePair.exceptionhandlers.PairAdminDatabaseException;
import gov.uspto.patent.privatePair.exceptionhandlers.UserNotFoundException;
import gov.uspto.patent.privatePair.utils.IpAddressUtil;
import gov.uspto.utils.SortMe;
import gov.uspto.utils.Statistics;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@RestController
@Slf4j
public class CreateNewCustomerNumberController {
	private static final Logger  logger = LoggerFactory.getLogger(CreateNewCustomerNumberController.class);
    /**
     * FAIL value
     */
    public static final String FAIL = "FAIL";
    /**
     * PAIR_SYSTEM value
     */
    public static final String PAIR_SYSTEM = "PAIR";
    /**
     * SUCCESS value
     */
    public static final String SUCCESS = "SUCCESS";

    public static final Gson gson = new Gson();

    public static final JsonResponse jsonResponse = new JsonResponse();
    @Autowired
    CreateNewCustomerNumberServices createNewCustomerNumberServices;
    @Autowired
    PairAdminHelperServices pairAdminHelperServices;
    @Autowired
    PrivatePairCommonController privatePairCommonController;
    @Autowired
    private CreateNewCustomerNumberValidator validator;


    /**
     * SpringMVC controller method to handle Create New Customer Number requests
     *
     * @return {@link ModelAndView}
     * @throws UserNotFoundException
     * @throws Exception
     */
    /**Needs to comment out, stale functionality*/
    @PostMapping(value = "/createNewCustomerNumber")
    public ModelAndView CreateNewCustomerNumber() throws UserNotFoundException, Exception {

        return new ModelAndView("CreateNewCustomerNumber");
    }

    /**
     * Deletes the Create New Customer Number request (Updates the delete
     * indicator to true - Soft Delete).
     *
     * @param jsonStr
     * @param request {@link HttpServletRequest}
     * @return {@link String} Status message indicating success or failure of
     * delete request
     * @throws Exception
     * @throws PairAdminDatabaseException
     */

    @PostMapping(value = "/deleteCreateNewCustomerNumberRequest")
    public CreateNewCustomerNumberForm deleteRequest(@RequestBody String sData, HttpServletRequest request) throws PairAdminDatabaseException, Exception {

        CreateNewCustomerNumberForm createNewCustomerNumberForm = gson.fromJson(sData, CreateNewCustomerNumberForm.class);
    	request.getSession().setAttribute("DnValue",createNewCustomerNumberForm.getDn());
       String dn =  (String) request.getSession().getAttribute("DnValue");
        String pairId = createNewCustomerNumberForm.getPairId();

        String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
        long startTimeMillis = System.currentTimeMillis();

        createNewCustomerNumberServices.deleteCreateNewCustomerNumberRequest(pairId);

        String activityEndTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();

        String statsActivity = Statistics.addPairAdminActivity(createNewCustomerNumberForm.getPairId(), dn,
                Statistics.DELETENEWCUSTOMERNUMBERREQUEST, activityStartTime, activityEndTime,
                "0", startTimeMillis, 0, "0",
                IpAddressUtil.getClientIP(request));

        log.info("Stats Activity: {}", statsActivity);

        createNewCustomerNumberForm.setRequestStatus("Deleted");
        createDeleteMessage(createNewCustomerNumberForm);

        return createNewCustomerNumberForm;
    }

    /**
     * Creates the saved message.
     *
     * @param createNewCustomerNumberForm
     */
    private void createDeleteMessage(CreateNewCustomerNumberForm createNewCustomerNumberForm) {

        String message = "Your request " + createNewCustomerNumberForm.getPairId() + " has been deleted.";
        createNewCustomerNumberForm.setSavedMessage(message);
    }

    @PostMapping(value = "/getConfirmNewCustomerNumberRequestbyPOST")
    public CreateNewCustomerNumberForm getConfirmNewCustomerNumberRequestbyPOST(@RequestParam(value = "pairId") String pairId,@RequestParam(value = "dn") String dn,
                                                                                HttpServletRequest request) throws Exception {

        CreateNewCustomerNumberForm createNewCustomerNumberForm = new CreateNewCustomerNumberForm();
         String adminPAIRdn = (String) dn;
     String custNumberListinString = "['59 -> CN 59 test Mar 6']";
  
        request.getSession().setAttribute("adminUserDN", adminPAIRdn);

        ModelAndView modelAndView = new ModelAndView("CustomerNumberPreviewConfirm");

        return getCreateNewCustomerNumberRequestHelper(createNewCustomerNumberForm, pairId, adminPAIRdn, custNumberListinString,
                IpAddressUtil.getClientIP(request), modelAndView);
    }

    public CreateNewCustomerNumberForm getCreateNewCustomerNumberRequestHelper(CreateNewCustomerNumberForm createNewCustomerNumberForm,
                                                                               String pairId, String adminPAIRdn, String custNumberListinString, String Ip, final ModelAndView modelAndView) {
        try {
        	   System.out.println("getCreateNewCustomerNumberRequestHelper");
        	     System.out.println(adminPAIRdn);
            PairUserDnDto pairUserDtoDn = pairAdminHelperServices.getPairUserDtoByDn(adminPAIRdn);
            System.out.println(pairUserDtoDn);
            createNewCustomerNumberForm = createNewCustomerNumberServices.getNewCustomerNumberRequest(Long.valueOf(pairId));
            if (!StringUtils.isEmpty(pairUserDtoDn) && adminPAIRdn.contains(UserType.INDEPENDENT_INVENTOR.getName())) {

                createNewCustomerNumberForm.setUserType(UserType.INDEPENDENT_INVENTOR.getName());
            } else {

                createNewCustomerNumberForm.setUserType(UserType.REGISTERED_ATTORNEYS.getName());
            }
            createNewCustomerNumberForm.setDn(adminPAIRdn);
            createNewCustomerNumberForm.setCustNumberListinString(custNumberListinString);

            createNewCustomerNumberForm.setCustomerPractitionerInfo(gson.toJson(
                    createNewCustomerNumberForm.getCustomerPractitionerInfolist()));

            if (!createNewCustomerNumberForm.getUserType().equalsIgnoreCase(UserType.INDEPENDENT_INVENTOR.getName())) {
                createNewCustomerNumberForm.setPrivatepairCustomerPractitionerInfo(gson.toJson(
                        createNewCustomerNumberForm.getExistingCustomerPractitionerInfolist()));
            }
            createSavedMessage(createNewCustomerNumberForm);
//            String activityEndTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();


        } catch (PairAdminDatabaseException e) {
            createErrorMessage(createNewCustomerNumberForm,
                    e.getMessage().substring(e.getMessage().indexOf("PairAdminDatabaseException:") + 27));
            //  logger.error(e);
        } catch (Exception e) {
            createErrorMessage(createNewCustomerNumberForm);
            //  logger.error(e);
        } finally {

            modelAndView.addObject("createNewCustomerNumberForm", createNewCustomerNumberForm);
        }

        return createNewCustomerNumberForm;
    }

    /**
     * Creates the saved message.
     *
     * @param createNewCustomerNumberForm
     */
    private void createSavedMessage(CreateNewCustomerNumberForm createNewCustomerNumberForm) {
        // Calculate the 7 days by first retrieving the saved Date and plus
        // 7 days.
        Calendar c = Calendar.getInstance();
        c.setTime(createNewCustomerNumberForm.getTimeStamp());
        c.add(Calendar.DATE, 7);

        Date validUntilDate = c.getTime();
        Format formatter = new SimpleDateFormat("MMM-dd-yyyy");
        String dateString = formatter.format(validUntilDate);

        String message = "Your request will be saved for your convenience until midnight Eastern Time on " + dateString
                + " and then will be deleted.";
        createNewCustomerNumberForm.setSavedMessage(message);
    }

    /**
     * Creates the error message.
     *
     * @param createNewCustomerNumberForm
     */
    private void createErrorMessage(CreateNewCustomerNumberForm createNewCustomerNumberForm, String e) {
        String message = "Database Error. Please contact to the system administrator. " + e;

        if (!StringUtils.isEmpty(e)) {
            createNewCustomerNumberForm.setServiceErrorMessage(e);
        }

        createNewCustomerNumberForm.setSavedMessage(message);
    }

    /**
     * Creates the error message.
     *
     * @param createNewCustomerNumberForm
     */
    private void createErrorMessage(CreateNewCustomerNumberForm createNewCustomerNumberForm) {
        String message = "Database Error. Please contact to the system administrator. ";
        createNewCustomerNumberForm.setSavedMessage(message);
    }

    /**
     * Helper method to display a Create New Customer Number request so as to be
     * able to edit.
     *
     * @param createNewCustomerNumberForm {@link CreateNewCustomerNumberForm}
     * @param model                       {@link Model}
     * @param request                     {@link httpServletRequest}
     * @return {@link ModelAndView}
     */
    /*Needs to be commented out, stale functionality*/
    @PostMapping(value = "/showEdit")
    public ModelAndView renderEditForm(
            @ModelAttribute("createNewCustomerNumberForm") CreateNewCustomerNumberForm createNewCustomerNumberForm,
            Model model, HttpServletRequest request) {

        if (StringUtils.isEmpty(createNewCustomerNumberForm.getIsAssociateMyPractitionerNumber())) {
            createNewCustomerNumberForm.setIsAssociateMyPractitionerNumber("N");
        }

        if (StringUtils.isEmpty(createNewCustomerNumberForm.getPairId())) {
            createNewCustomerNumberForm.setPairId((String) request.getSession().getAttribute("pairId"));
        }
        if (StringUtils.isEmpty(createNewCustomerNumberForm.getRequestStatus())) {
            createNewCustomerNumberForm.setRequestStatus((String) request.getSession().getAttribute("requestStatus"));
        }
        if (StringUtils.isEmpty(createNewCustomerNumberForm.getTimeStamp())) {
            createNewCustomerNumberForm.setTimeStamp((Date) request.getSession().getAttribute("timeStamp"));
        }
        if (StringUtils.isEmpty(createNewCustomerNumberForm.getLastModifiedTimeStamp())) {
            createNewCustomerNumberForm.setLastModifiedTimeStamp((Date) request.getSession()
                    .getAttribute("lastModifiedTimeStamp"));
        }

        model.addAttribute("createNewCustomerNumberForm", createNewCustomerNumberForm);

        return new ModelAndView("CreateNewCustomerNumber");
    }

    /**
     * Helper method to display a Create New Customer Number request
     *
     * @param createNewCustomerNumberForm {@link CreateNewCustomerNumberForm}
     * @param model                       {@link Model}
     * @return {@link ModelAndView}
     */
    @PostMapping(value = "/showPreview")
    public ModelAndView renderPreviewForm(
            @ModelAttribute("createNewCustomerNumberForm") CreateNewCustomerNumberForm createNewCustomerNumberForm,
            Model model) {

        if (StringUtils.isEmpty(createNewCustomerNumberForm.getIsAssociateMyPractitionerNumber())) {
            createNewCustomerNumberForm.setIsAssociateMyPractitionerNumber("N");
        }


        createNewCustomerNumberForm.setAsJson(gson.toJson(createNewCustomerNumberForm));
        model.addAttribute("createNewCustomerNumberForm", createNewCustomerNumberForm);

        return new ModelAndView("CustomerNumberPreviewConfirm");
    }

    /**
     * Saves the Create New Customer Number request to the PAIR DB.
     *
     * @param sData   A JSON string with all the Create New Customer Form Number
     *                details.
     * @param request {@link HttpServletRequest}
     * @return {@link String} A JSON string with the updated Create New Customer
     * Number Form details including the PairID, time stamp and status .
     */
    
    @PostMapping(value = "/saveNewCustomerNumber")
    public ResponseEntity<JsonResponse> saveNewCustomerNumber(@RequestBody String sData, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Gson gson = new Gson();
        CreateNewCustomerNumberForm createNewCustomerNumberForm = gson.fromJson(sData, CreateNewCustomerNumberForm.class);
        request.getSession().setAttribute("DnValue",createNewCustomerNumberForm.getDn());
        String dn =  (String) request.getSession().getAttribute("DnValue");
        System.out.println(dn);
      createNewCustomerNumberForm.setRequestStatus(RequestStatus.SAVED.getName());

        JsonResponse jsonResponse = new JsonResponse();

        
            String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();

            createNewCustomerNumberForm = createNewCustomerNumberServices.saveNewCustomerNumber(createNewCustomerNumberForm, dn);

            String activityEndTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
            long startTimeMillis = System.currentTimeMillis();

            String statsActivity = Statistics.addPairAdminActivity(createNewCustomerNumberForm.getPairId(), dn, Statistics.SAVENEWCUSTOMERNUMBERREQUEST,
                    activityStartTime, activityEndTime, "0", startTimeMillis, 0, "0",
                    IpAddressUtil.getClientIP(request));
            log.info("Stats Activity:{} ",statsActivity);
            // Creates the save message which is to be displayed once the
            // request is saved in DB.
            // Message indicates that request will be held for 7 consecutive
            // days and then deleted.
            createSavedMessage(createNewCustomerNumberForm);

            request.getSession().setAttribute("pairId", createNewCustomerNumberForm.getPairId());
            request.getSession().setAttribute("requestStatus", createNewCustomerNumberForm.getRequestStatus());
            request.getSession().setAttribute("timeStamp", createNewCustomerNumberForm.getTimeStamp());
            request.getSession().setAttribute("lastModifiedTimeStamp", createNewCustomerNumberForm.getLastModifiedTimeStamp());

       

            jsonResponse.setJsonResult(gson.toJson(createNewCustomerNumberForm));
        

        return getFinalJsonResponse(jsonResponse);
    }
    private ResponseEntity<JsonResponse> getFinalJsonResponse(JsonResponse jsonResponse) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<JsonResponse>(jsonResponse, headers, HttpStatus.OK);
    }
   

    /**
     * Updates the Create New Customer Number request to the PAIR DB.
     *
     * @param sData    The JSON String.
     * @param request  {@link HttpServletRequest}
     * @return {@link String} The JSON string with the updated all the Create
     * New Customer Form Number details.
     */
    @PostMapping(value = "/updateCreateNewCustomerNumberRequest")
    public ResponseEntity<JsonResponse> updateCreateNewCustomerNumberRequest(@RequestBody String sData,
                                                                            HttpServletRequest request) throws Exception {

        CreateNewCustomerNumberForm createNewCustomerNumberForm = gson.fromJson(sData, CreateNewCustomerNumberForm.class);
        request.getSession().setAttribute("DnValue",createNewCustomerNumberForm.getDn());
       String dn =  (String) request.getSession().getAttribute("DnValue");
       String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
            long startTimeMillis = System.currentTimeMillis();
            JsonResponse jsonResponse = new JsonResponse();
            createNewCustomerNumberForm = createNewCustomerNumberServices
                    .updateNewCustomerNumber(createNewCustomerNumberForm, dn);

            String activityEndTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();

        String statsActivity = Statistics.addPairAdminActivity(createNewCustomerNumberForm.getPairId(), dn,
                    Statistics.UPDATENEWCUSTOMERNUMBERREQUEST, activityStartTime, activityEndTime, "0", startTimeMillis, 0, "0",
                    IpAddressUtil.getClientIP(request));

            log.info("Stats Activity: {}",statsActivity);

            createSavedMessage(createNewCustomerNumberForm);
            jsonResponse.setJsonResult(gson.toJson(createNewCustomerNumberForm));
            return getFinalJsonResponse(jsonResponse);
    }
    
    
    @PostMapping(value = "/previewCreateCustomerNumberRequest")
    public ResponseEntity<JsonResponse> previewCreateCustomerNumberRequest(@RequestBody String sData,
            BindingResult bindingResult, final Model model, HttpServletRequest request, HttpServletResponse response) {

        logger.debug("sData: " + sData);

        JsonResponse jsonResponse = new JsonResponse();

        String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
        long startTimeMillis = System.currentTimeMillis();
        Gson gson = new Gson();
        CreateNewCustomerNumberForm createNewCustomerNumberForm = gson.fromJson(sData, CreateNewCustomerNumberForm.class);
        request.getSession().setAttribute("DnValue",createNewCustomerNumberForm.getDn());
       String dn =  (String) request.getSession().getAttribute("DnValue");
       
        try {
            // Bind the form object with the binding result which is used for
            // the validations.
            bindingResult = new BindException(createNewCustomerNumberForm, "createNewCustomerNumberForm");

            // Invoke the validations on the form object
            ValidationUtils.invokeValidator(validator, createNewCustomerNumberForm, bindingResult);

            if (!createNewCustomerNumberForm.getUserType().startsWith(UserType.INDEPENDENT_INVENTOR.getName())
                    && StringUtils.isEmpty(bindingResult.getFieldError("customerPractitionerInfo"))
                    && !createNewCustomerNumberForm.getCustomerPractitionerInfolist().isEmpty()) {

         //       createNewCustomerNumberForm = createNewCustomerNumberServices
         //               .validateRegisteredPractitioners(createNewCustomerNumberForm);

                if (StringUtils.hasText(createNewCustomerNumberForm.getServiceErrorMessage())) {

                    jsonResponse.setStatus(FAIL);
                    // Invoke the validations on the form object
                    ValidationUtils.invokeValidator(validator, createNewCustomerNumberForm, bindingResult);
                }
            }

        } catch (Exception e) {
            createErrorMessage(createNewCustomerNumberForm, e.getMessage());
            logger.debug("err"+e);

            jsonResponse.setStatus(FAIL);
        } finally {
            getValidatedJsonResponse(bindingResult, jsonResponse, gson, createNewCustomerNumberForm);
        }

        String activityEndTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
        String statsActivity = Statistics.addPairAdminActivity(createNewCustomerNumberForm.getPairId(), dn,
                Statistics.PRIVIEWNEWCUSTOMERNUMBERREQUEST, activityStartTime, activityEndTime, "0", startTimeMillis, 0, "0",
                IpAddressUtil.getClientIP(request));

        log.info("Stats Activity: {}",statsActivity);

        return getFinalJsonResponse(jsonResponse);
    }
    
    private void getValidatedJsonResponse(BindingResult bindingResult, JsonResponse jsonResponse, Gson gson,
            CreateNewCustomerNumberForm createNewCustomerNumberForm) {

        String jsonResponseObj = gson.toJson(createNewCustomerNumberForm);
System.out.println(jsonResponse.getStatus());
System.out.println(bindingResult.hasErrors());
        if (bindingResult.hasErrors() || (null!=jsonResponse.getStatus() && jsonResponse.getStatus().equals(FAIL))) {
            jsonResponse.setStatus(FAIL);
            jsonResponse.setValidationResult(bindingResult.getAllErrors());
            jsonResponse.setJsonResult(jsonResponseObj);

        } else {
            jsonResponse.setStatus(SUCCESS);
            jsonResponse.setValidationResult(bindingResult.getAllErrors());
            jsonResponse.setJsonResult(jsonResponseObj);
        }
    }
    
    /**
     * @return
     */
    private Gson getCustomGson() {
        // Convert Json string to java object.
        GsonBuilder gsonBuilder = new GsonBuilder();
        // gsonBuilder.serializeNulls();
        gsonBuilder.registerTypeAdapter(String.class, new StringConverter());
        Gson gson = gsonBuilder.create();// new Gson();
        return gson;
    }
    
    private class StringConverter implements JsonSerializer<String>, JsonDeserializer<String> {

        public JsonElement serialize(String src, Type typeOfSrc, JsonSerializationContext context) {
            if (src == null) {
                return new JsonPrimitive("");
            } else {
                return new JsonPrimitive(src.toString());
            }
        }

        public String deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return json.getAsJsonPrimitive().getAsString();
        }
    }

		

		
    
    /**
     * Transmit Create New Customer Number Request to PALM Web Service
     * 
     * @param sData
     *            {@link String} A JSON string.
     * @param request
     *            {@link HttpServletRequest}
     * @param response
     *            {@link HttpServletResponse}
     * @return {@link ResponseEntity}
     */
    @RequestMapping(value = "/transmitCreateCustomerNumberRequest", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<JsonResponse> transmitCreateCustomerNumberRequest(@RequestBody String sData,
            HttpServletRequest request, HttpServletResponse response) {

        logger.debug("sData: " + sData);

        String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
        long startTimeMillis = System.currentTimeMillis();

        JsonResponse jsonResponse = new JsonResponse();

        // Convert Json string to java object.
        Gson gson = getCustomGson();
        CreateNewCustomerNumberForm createNewCustomerNumberForm = gson.fromJson(sData, CreateNewCustomerNumberForm.class);

        try {
            // create or update the request first
            if (StringUtils.isEmpty(createNewCustomerNumberForm.getPairId())) {
                createNewCustomerNumberForm.setRequestStatus(RequestStatus.SAVED.getName());
                createNewCustomerNumberServices.saveNewCustomerNumber(createNewCustomerNumberForm,
                        createNewCustomerNumberForm.getDn());
            } else {
                createNewCustomerNumberServices.updateNewCustomerNumber(createNewCustomerNumberForm,
                        createNewCustomerNumberForm.getDn());
            }

            createNewCustomerNumberForm = createNewCustomerNumberServices.createNewCustomerNumber(createNewCustomerNumberForm);

            createNewCustomerNumberForm.setTimeStamp(Calendar.getInstance().getTime());

            if (StringUtils.hasText(createNewCustomerNumberForm.getServiceErrorMessage())) {
                jsonResponse.setStatus(FAIL);
            } else {
                jsonResponse.setStatus(SUCCESS);
            }
        } catch (Exception e) {
        	logger.debug("msg" + e);

            if (e instanceof PairAdminDatabaseException) {
                createErrorMessage(createNewCustomerNumberForm,
                        e.getMessage().substring(e.getMessage().indexOf("PairAdminDatabaseException:") + 27));
            } else {
                createErrorMessage(createNewCustomerNumberForm, e.getMessage());
            }

            jsonResponse.setStatus(FAIL);

            createNewCustomerNumberForm.setRequestStatus(RequestStatus.FAILED.getName());
            // createNewCustomerNumberForm.setServiceErrorMessage(e.getMessage());

            try {
                createNewCustomerNumberServices.updateRequestStatus(createNewCustomerNumberForm);
            } catch (PairAdminDatabaseException e1) {
                logger.debug("msg"+e1);
            }
        } finally {
            // Convert Java object to json string.
            String jsonResponseObj = gson.toJson(createNewCustomerNumberForm);
            jsonResponse.setJsonResult(jsonResponseObj);
        }

        String activityEndTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();

   
        String statsActivity = Statistics.addPairAdminActivity(createNewCustomerNumberForm.getPairId(), createNewCustomerNumberForm.getDn(),
                Statistics.TRANSMITNEWCUSTOMERNUMBERREQUEST, activityStartTime, activityEndTime, "0", startTimeMillis, 0, "0",
                IpAddressUtil.getClientIP(request));
        return getFinalJsonResponse(jsonResponse);
    }
  
  
 	
    




}