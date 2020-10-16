package gov.uspto.patent.privatePair.admin.controller;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import gov.uspto.patent.privatePair.admin.domain.EditEntityStatus;
import gov.uspto.patent.privatePair.admin.domain.JsonResponse;
import gov.uspto.patent.privatePair.admin.dto.PairUserDnDto;
import gov.uspto.patent.privatePair.admin.service.common.PairAdminHelperServices;
import gov.uspto.patent.privatePair.admin.service.entitystatus.EntityStatusServices;
import gov.uspto.patent.privatePair.admin.validator.EditEntityStatusValidator;
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
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a class for Entity Status specific business logic.
 */
@RestController
@Slf4j
public class EntitySaveController {

    /**
     * Distingushed name
     */
    public static final String DISTINGUISHED_NAME = "distinguishedName";
    private static final Gson gson = new Gson();
    private static final Logger  logger = LoggerFactory.getLogger(EntitySaveController.class); 
    
    @Autowired
    EntityStatusServices entityStatusServices;

    HttpHeaders headers;
    @Autowired
    MessageSource messageSource;

    //private static Logger logger = Logger.getRootLogger();
    @Autowired
    PairAdminHelperServices pairAdminServices;
    @Autowired
    PrivatePairCommonController privatePairCommonController;

    @Autowired
    private EditEntityStatusValidator validator;

    /**
     * Constructor.
     */
    public EntitySaveController() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    /**
     * Delete the entity request (Updates the delete indicator to true - Soft
     * Delete).
     *
     * @param jsonStr A JSON string
     * @param request {@link HttpServletRequest}
     */
    @PostMapping(value = "/deleteRequest")
    public ResponseEntity<String> deleteEntityStatus(@RequestParam("pairId") String pairId, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	 
         
        //logger.debug("userRequestId: " + userRequestId);
        String jsonResponseObj = null;

        EditEntityStatus editEntityStatusForm = new EditEntityStatus();
        request.getSession().setAttribute("DnValue",editEntityStatusForm.getDn());
        String dn = (String) request.getSession().getAttribute("DnValue");
        // Start time
        String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
        Integer status = null;
        try{
       status= entityStatusServices.deleteEntityStatus(pairId);

        if (status == 1)
        	editEntityStatusForm.setSavedMessage("Your request was successfully deleted.");
        else
        	editEntityStatusForm.setSavedMessage("Error deleting request.");

        // Convert Java object to json string.
        jsonResponseObj = new Gson().toJson(editEntityStatusForm);
        } catch (PairAdminDatabaseException pade) {
        	//logger.error("Failed to delete the address: " + pade);
        	return new ResponseEntity<String>(messageSource.getMessage("updateStatus.delete.error", null, null), headers,
        			HttpStatus.BAD_REQUEST);
        } 
        
        // End time
        String activityEndTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
        long startTimeMillis = System.currentTimeMillis();

        String statsActivity = Statistics.addPairAdminActivity(pairId, dn, Statistics.DELETEENTITYCHANGEREQUEST, activityStartTime, activityEndTime,
                "0", startTimeMillis, 0, "0", IpAddressUtil.getClientIP(request));

        log.info("Stats activity: {}",statsActivity);
        return new ResponseEntity<String>(jsonResponseObj, headers, HttpStatus.OK);
    }

    /**
     * Get the entity change request for the given pairId.
     *
     * @param entitypairId The userRequestId.
     * @param model        {@link Model}
     * @param request      {@link HttpServletRequest}
     * @return {@link ModelAndView}
     */
   
    @PostMapping(value = "/getEntityChangeRequest")
    public ResponseEntity<String> getEntityChangeRequestData(@RequestParam("entitypairId") String entitypairId, HttpServletRequest request,
            HttpServletResponse response) throws PairAdminDatabaseException, Exception {
    	 String jsonResponseObj = null;
    	   EditEntityStatus editEntityStatusForm = new EditEntityStatus();
        try {
        
         
            
            String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
            
            editEntityStatusForm = entityStatusServices.getEntityChangeRequest(Long.valueOf(entitypairId));
            // Convert Java object to json string.
            jsonResponseObj = new Gson().toJson(editEntityStatusForm);
            System.out.println(jsonResponseObj);
            String activityEndTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
            long startTimeMillis = System.currentTimeMillis();

            String statsActivity = Statistics.addPairAdminActivity(entitypairId, request.getSession().getAttribute("privatePAIRdn").toString(),
                    Statistics.VIEWENTITYSTATUSREQUEST, activityStartTime, activityEndTime, "0", startTimeMillis, 0, "0",
                    IpAddressUtil.getClientIP(request));

            log.info("Stats Activity: {}", statsActivity);


        } catch (PairAdminDatabaseException e) {
            logger.error("Failed to getEntityChangeRequest: " + e);
        } catch (Exception e) {
            logger.error("Failed to getEntityChangeRequest: " + e);
        }
        return new ResponseEntity<String>(jsonResponseObj, headers, HttpStatus.OK);
    }
    
    

    /**
     * Get the entity request json.
     *
     * @param jsonStr The form jsonStr.
     * @return {@link ResponseEntity}
     */
    @PostMapping(value = "/getEntityRequestJson")
    public EditEntityStatus getEntityRequestJson(@RequestBody String jsonStr) throws Exception {
        JsonObject newObj = new JsonParser().parse(jsonStr).getAsJsonObject();
        String pairId = newObj.get("pairId").getAsString();

        return entityStatusServices.getEntityChangeRequest(Long.valueOf(pairId));
    }

    /**
     * Validate/Preview the entity status change request.
     * 
     * @param sData
     *            The form object json string
     * @param bindingResult
     *            {@link BindingResult} Validation error result.
     * @param model
     *            {@link Model}
     * @param request
     *            {@link HttpServletRequest}
     * @param response
     *            {@link HttpServletResponse}
     * @return {@link ResponseEntity}
     */
    @RequestMapping(value = "/previewRequest", method = RequestMethod.POST)
    public ResponseEntity<JsonResponse> previewEntityStatus(@RequestBody String sData, BindingResult bindingResult,
            final Model model, HttpServletRequest request, HttpServletResponse response) {
        JsonResponse jsonResponse = new JsonResponse();
        try {
        	
         // Convert Json string to java object.
            Gson gson = new Gson();
            EditEntityStatus editEntityStatusForm = gson.fromJson(sData, EditEntityStatus.class);
            request.getSession().setAttribute("DnValue",editEntityStatusForm.getDn());
        	String dn = (String) request.getSession().getAttribute("DnValue");
            
            // Bind the form object with the binding result which is used for
            // the validations.
        	bindingResult = new BindException(editEntityStatusForm, "editEntityStatusForm");

            // Start time
            String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
            
            // Invoke the validations on the form object
            ValidationUtils.invokeValidator(validator, editEntityStatusForm, bindingResult);
            
            // End time
            String activityEndTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
            long startTimeMillis = System.currentTimeMillis();
            String statsActivity = Statistics.addPairAdminActivity(editEntityStatusForm.getPairId(), dn, Statistics.PRIVIEWENTITYSTATUSREQUEST,
                    activityStartTime, activityEndTime, "0", startTimeMillis, 0, "0",
                    IpAddressUtil.getClientIP(request));

            log.info("Stats Activity: {}", statsActivity);
            // Convert Java object to json string.
            String jsonResponseObj = new Gson().toJson(editEntityStatusForm);

            if (bindingResult.hasErrors()) {
                jsonResponse.setStatus("FAIL");
                jsonResponse.setValidationResult(bindingResult.getAllErrors());
                jsonResponse.setJsonResult(jsonResponseObj);

            } else {
                jsonResponse.setStatus("SUCCESS");
                jsonResponse.setValidationResult(bindingResult.getAllErrors());
                jsonResponse.setJsonResult(jsonResponseObj);
            }

        } catch (JsonSyntaxException e) {
            logger.error("Invalid json on preview entity request: " + e);
        } catch (Exception e) {
            logger.error("Failed to preview the entity request: " + e);
            jsonResponse.setStatus(messageSource.getMessage("entitystatus.preview.error", null, null));
            return new ResponseEntity<JsonResponse>(jsonResponse, headers, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<JsonResponse>(jsonResponse, headers, HttpStatus.OK);
    }
     /**
     * Saves the Entity request to the PAIR/PURM DB.
     *
     * @param sData   A JSON string with all the entity status details.
     * @param request {@link HttpServletRequest}
     * @return {@link String} A JSON string with the updated entity status
     * details including the PairID.
     */
    @PostMapping(value = "/saveRequest")
    public ResponseEntity<String> saveRequest(@RequestBody String sData, HttpServletRequest request, HttpServletResponse response)
            throws PairAdminDatabaseException, Exception {

        String jsonResponseObj = null;

        
           
          // Convert Json string to java object.
            Gson gson = new Gson();
            EditEntityStatus editEntityStatus = gson.fromJson(sData, EditEntityStatus.class);
            request.getSession().setAttribute("DnValue",editEntityStatus.getDn());
            String dn = (String) request.getSession().getAttribute("DnValue");
            // Start time
            String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();

            // Actual Save operation.
            editEntityStatus = entityStatusServices.saveEntityStatus(editEntityStatus, dn);

            // End time
            String activityEndTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
            long startTimeMillis = System.currentTimeMillis();

            String statsActivity = Statistics.addPairAdminActivity(editEntityStatus.getPairId(), dn, Statistics.SAVEENTITYCHANGEREQUEST,
                    activityStartTime, activityEndTime, "0", startTimeMillis, 0, "0",
                    IpAddressUtil.getClientIP(request));

            
            
            log.info("Stats Activity: {}",statsActivity);
            // Creates the save message which is to be displayed
            // once the request is saved in DB with a 7 day validity.
            createSavedMessage(editEntityStatus);

            // Convert Java object to json string.
            jsonResponseObj = new Gson().toJson(editEntityStatus);

        
        return new ResponseEntity<String>(jsonResponseObj, headers, HttpStatus.OK);
    }
    
    
    /**
     * Updates the Entity request in the PAIR/PURM DB.
     *
     * @param sData    The JSON String with entity details.
     * @param request  {@link HttpServletRequest}
     * @param response {@HttpServletResponse}
     * @return {@link String} The JSON string with the updated entity request
     * details.
     * @throws PairAdminDatabaseException
     */
    @PostMapping(value = "/updateRequest")
    public ResponseEntity<String> updateRequest(@RequestBody String sData, HttpServletRequest request,
                                          HttpServletResponse response) throws PairAdminDatabaseException, Exception {
    	 String jsonResponseObj = null;
       // Convert Json string to java object.
        EditEntityStatus editEntityStatus = gson.fromJson(sData, EditEntityStatus.class);
        request.getSession().setAttribute("DnValue",editEntityStatus.getDn());
        String dn = (String) request.getSession().getAttribute("DnValue");
        // Start time
        String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
        // Invoke the update operation.
        editEntityStatus = entityStatusServices.updateEntityStatus(editEntityStatus, dn);
        // End time
        String activityEndTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
        long startTimeMillis = System.currentTimeMillis();

        String statsActivity = Statistics.addPairAdminActivity(editEntityStatus.getPairId(), dn, Statistics.UPDATEENTITYCHANGEREQUEST,
                activityStartTime, activityEndTime, "0", startTimeMillis, 0, "0",
                IpAddressUtil.getClientIP(request));

        log.info("Stats Activity: {} ",statsActivity);

        createSavedMessage(editEntityStatus);
        // Convert Java object to json string.
        jsonResponseObj = new Gson().toJson(editEntityStatus);
        return new ResponseEntity<String>(jsonResponseObj, headers, HttpStatus.OK);
    }
    
    
    

    /**
     * Creates the saved message.
     *
     * @param editEntityStatus
     */
    private void createSavedMessage(EditEntityStatus editEntityStatus) throws Exception {

        // Calculate the 7 days by first retrieving the saved Date and plus
        // 7 days.
        Calendar c = Calendar.getInstance();
        c.setTime(editEntityStatus.getCreatedTs());
        c.add(Calendar.DATE, 7);

        Date validUntilDate = c.getTime();
        Format formatter = new SimpleDateFormat("MMM-dd-yyyy");
        String dateString = formatter.format(validUntilDate);

        String message = "Your request will be saved for your convenience until midnight Eastern Time on " + dateString
                + " and then will be deleted.";

        editEntityStatus.setSavedMessage(message);

    }

    /**
     * This method in invoked in order to load the main edit entity status form.
     * It populates Attorney Docket number, Title, Application Number and
     * Current entity status.
     *
     * @param applicationNumber    The application number for which we want to update the entity
     *                             status.
     * @param title                The title of the application.
     * @param attorneyDocketNumber The attorney docket number.
     * @param currentEntityStatus  The current entity status of the request.
     * @param distinguishedName    The distinguished name of the logged in user.
     * @param request              The {@link HttpServletRequest}
     * @return {@link ModelAndView}
     */
    @PostMapping(value = "/editEntityStatus")
    public String setUpForms(@RequestParam("adminApplicationNumber") String applicationNumber,
                             @RequestParam("adminTitle") String[] title, @RequestParam("adminAttorneyDocketNumber") String
                                     attorneyDocketNumber,
                             @RequestParam("adminCurrentEntityStatus") String currentEntityStatus,
                             @RequestParam("adminUserDN") String distinguishedName, @RequestParam("adminCustomerNumber") String
                                     customerNumber,
                             HttpServletRequest request, final Model model) {
        try {
            // Set all the params in the session.
            request.getSession().setAttribute("applicationNumber", applicationNumber);
            StringBuilder stringBuilder = new StringBuilder();
            for (String s : title) {
                stringBuilder.append(s);
            }

            request.getSession().setAttribute("title", stringBuilder.toString());
            request.getSession().setAttribute("attorneyDocketNumber", attorneyDocketNumber);
            request.getSession().setAttribute("currentEntityStatus", currentEntityStatus);
            request.getSession().setAttribute(DISTINGUISHED_NAME, distinguishedName);
            request.getSession().setAttribute("customerNumber", customerNumber);

            EditEntityStatus editEntityStatus = new EditEntityStatus();

            PairUserDnDto pairUserDnDto = null;
            try {
                try {
                    pairUserDnDto = pairAdminServices.getPairUserDtoByDn(distinguishedName);
                } catch (PairAdminDatabaseException e) {
                    model.addAttribute("exceptionMessage", messageSource.getMessage("database.connection.error", null, null));
                    //  logger.error("Failed to setup entity status form: " + e);
                } catch (UserNotFoundException e) {
                    // If pairUserDnDto = null for the given dn string.
                    model.addAttribute("exceptionMessage", messageSource.getMessage("usernotfound.error", null, null));
                    // logger.error("Failed to setup entity status form: " + e);
                }
            } catch (NoSuchMessageException nsme) {
                //  logger.error(nsme.getMessage());
            }

            if (pairUserDnDto != null) {
                editEntityStatus.setRegNumber(pairUserDnDto.getPairUserDnId().substring(2));
            } else {
                editEntityStatus.setRegNumber("");
            }

            Pattern pattern = Pattern.compile(UserType.INDEPENDENT_INVENTOR.getName());
            Matcher matcher = pattern.matcher(distinguishedName);
            if (matcher.find())
                request.getSession().setAttribute("userType", UserType.INDEPENDENT_INVENTOR.getName());
            else
                request.getSession().setAttribute("userType", UserType.REGISTERED_ATTORNEYS.getName());

            String editEntityStatusFormJsonStr = new Gson().toJson(editEntityStatus);

            // Adding the JSON string as an attribute to be accessed in the JSP
            // page.
            model.addAttribute("editEntityStatusFormJsonStr", editEntityStatusFormJsonStr);
            model.addAttribute("editEntityStatus", editEntityStatus);

        } catch (Exception e) {
            //  logger.error("Failed to setup entity status form: " + e);
        }

        return "EditEntityStatus";
    }

    
    
    
    /**
     * Transmits the entity request to PTO using the PALM WS.
     * 
     * @param sData
     *            A JSON String containing the Entity Status Change request
     *            information.
     * @param request
     *            {@link HttpServletRequest}
     * @param response
     *            {@link HttpServletResponse}
     * @return {@link ResponseEntity} A response containing the json string.
     * @throws PairAdminDatabaseException
     * @throws UserNotFoundException
     * 
     * @throws Exception
     */
    @RequestMapping(value = "/transmitRequest", method = RequestMethod.POST)
    public ResponseEntity<String> transmitToPTOEntityStatus(@RequestBody String sData, HttpServletRequest request,
            HttpServletResponse response) throws PairAdminDatabaseException, UserNotFoundException, Exception {
        String jsonResponseObj = null;
        try {
           
            // Convert Json string to java object.
            Gson gson = new Gson();
            EditEntityStatus editEntityStatusForm = gson.fromJson(sData, EditEntityStatus.class);
            request.getSession().setAttribute("DnValue",editEntityStatusForm.getDn());
            String dn = (String) request.getSession().getAttribute("DnValue");
            // Start time
            String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();

            String orgApplicationNumber = editEntityStatusForm.getApplicationNumber();

            editEntityStatusForm.setApplicationNumber(editEntityStatusForm.getApplicationNumber().replaceAll("[^0-9]+", ""));
            // Invoke the transmit operation.
            editEntityStatusForm = entityStatusServices.transmitToPTOEntityStatus(editEntityStatusForm, dn);

            editEntityStatusForm.setApplicationNumber(orgApplicationNumber);
            // End time
            String activityEndTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
            long startTimeMillis = System.currentTimeMillis();

            String statsActivity = Statistics.addPairAdminActivity(editEntityStatusForm.getPairId(), dn, Statistics.TRANSMITENTITYCHANGEREQUEST,
                    activityStartTime, activityEndTime, "0", startTimeMillis, 0, "0",
                    IpAddressUtil.getClientIP(request));
           
            // Convert Java object to json string.
            jsonResponseObj = new Gson().toJson(editEntityStatusForm);

        } catch (PairAdminDatabaseException e) {
            logger.error("Failed to transmit the entity request: " + e);
            return new ResponseEntity<String>(messageSource.getMessage("entitystatus.transmit.error", null, null), headers,
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Failed to transmit the entity request: " + e);
            return new ResponseEntity<String>(messageSource.getMessage("entitystatus.transmit.error", null, null), headers,
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<String>(jsonResponseObj, headers, HttpStatus.OK);
    }

    
    
    
}