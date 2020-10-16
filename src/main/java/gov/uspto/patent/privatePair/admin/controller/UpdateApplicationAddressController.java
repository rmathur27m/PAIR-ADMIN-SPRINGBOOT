package gov.uspto.patent.privatePair.admin.controller;

import com.google.gson.Gson;

import gov.uspto.patent.privatePair.admin.domain.Constants;
import gov.uspto.patent.privatePair.admin.domain.JsonResponse;
import gov.uspto.patent.privatePair.admin.domain.UpdateApplicationAddressForm;
import gov.uspto.patent.privatePair.admin.domain.UpdateApplicationAddressNumbers;
import gov.uspto.patent.privatePair.admin.dto.PairUserDnDto;
import gov.uspto.patent.privatePair.admin.service.common.PairAdminHelperServices;
import gov.uspto.patent.privatePair.admin.service.updateapplicationaddress.UpdateApplicationAddressServices;
import gov.uspto.patent.privatePair.admin.validator.UpdateApplicationAddressValidator;
import gov.uspto.patent.privatePair.common.UserType;
import gov.uspto.patent.privatePair.exceptionhandlers.PairAdminDatabaseException;
import gov.uspto.patent.privatePair.utils.IpAddressUtil;
import gov.uspto.utils.SortMe;
import gov.uspto.utils.Statistics;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper class; contains various methods to return single pieces of
 * information.
 */
@RestController
@Slf4j
public class UpdateApplicationAddressController {
	private static final Logger  logger = LoggerFactory.getLogger(UpdateApplicationAddressController.class);
    private final static Gson gson = new Gson();

    @Autowired
    PairAdminHelperServices pairAdminHelperServices;
    
    @Autowired
    private MessageSource messageSource;

    private HttpHeaders headers;

    @Autowired
    UpdateApplicationAddressServices updateApplicationAddressServices;
    @Autowired
    UpdateApplicationAddressValidator validator;

    /**
     * Constructor.
     */
    public UpdateApplicationAddressController() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }


    //private static Logger logger = Logger.getRootLogger();

    /**
     * Delete an existing Update Application Address Change request
     *
     * @param userRequestId {@link long} Id of Update Application Address Change request
     *                      to delete
     * @param request       {@link HttpServletRequest}
     * @return {@link String} Operation success/fail status message.
     * @throws PairAdminDatabaseException
     */
    @PostMapping(value = "/deleteUpdateApplicationAddressRequest")
    public UpdateApplicationAddressForm deleteUpdateApplicationAddressRequest(@RequestParam("userRequestId") Long userRequestId, HttpServletRequest request) throws PairAdminDatabaseException {

        UpdateApplicationAddressForm updateApplicationAddressForm = new UpdateApplicationAddressForm();
        // Start time
        String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();

        Integer status = updateApplicationAddressServices.deleteUpdateApplicationAddress(userRequestId);

        if (status == 1)
            updateApplicationAddressForm.setSavedMessage("Your request was successfully deleted.");
        else
            updateApplicationAddressForm.setSavedMessage("Error deleting request.");

        // End time
        String activityEndTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
        long startTimeMillis = System.currentTimeMillis();

        String statsActivity = Statistics.addPairAdminActivity(updateApplicationAddressForm.getPairId(),
                (String) request.getSession().getAttribute("distinguishedName"),
                Statistics.DELETEUPDATEAPPLICATIONADDRESSREQUEST, activityStartTime, activityEndTime, "0", startTimeMillis, 0,
                "0", IpAddressUtil.getClientIP(request));

        log.info("Stats Activity: {}", statsActivity);

        return updateApplicationAddressForm;
    }

    /**
     * Retrieve an existing Update Application Address Request for review
     *
     * @param upadateAddresspairId {@link String} Id of Update Application Address Change request
     *                             to delete.
     * @return {@link ModelAndView}
     * @throws PairAdminDatabaseException
     * @throws Exception
     */
    @PostMapping(value = "/viewUpdateAddressRequest")
    public UpdateApplicationAddressForm getUpdateAddressRequest(@RequestParam("upadateAddresspairId") String upadateAddresspairId) throws PairAdminDatabaseException,
            Exception {

        UpdateApplicationAddressForm updateApplicationAddressForm = new UpdateApplicationAddressForm();

        updateApplicationAddressForm = updateApplicationAddressServices.getUpdateAddressRequest(Long
                .valueOf(upadateAddresspairId));

        return updateApplicationAddressForm;
    }

    @SuppressWarnings("unused")
	@PostMapping(value = "/previewUpdateApplicationAddressRequest")
   public ResponseEntity<JsonResponse> previewUpdateApplicationAddressRequest(@RequestBody String sData,
            BindingResult bindingResult, final Model model, HttpServletRequest request) {

   //     logger.debug("sData: " + sData);

        // Start time
        String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();

        JsonResponse jsonResponse = new JsonResponse();

        Gson gson = new Gson();
        UpdateApplicationAddressForm updateApplicationAddressForm = gson.fromJson(sData, UpdateApplicationAddressForm.class);
    	request.getSession().setAttribute("DnValue",updateApplicationAddressForm.getDn());
        // Bind the form object with the binding result which is used for the
        // validations.
    	bindingResult = new BindException(updateApplicationAddressForm, "updateApplicationAddressForm");

        // Invoke the validations on the form object
        ValidationUtils.invokeValidator(validator, updateApplicationAddressForm, bindingResult);
       // logger.info(bindingResult.getFieldError("commonName").toString() );
        // Check for validation errors on the form fields
        if (bindingResult.getFieldError("commonName") != null || bindingResult.getFieldError("customerNumber") != null
                || bindingResult.getFieldError("attorneyCertification") != null
                || bindingResult.getFieldError("submitterSignature") != null
                || bindingResult.getFieldError("contactFullName") != null
                || bindingResult.getFieldError("contactTelephoneNoText") != null
                || bindingResult.getFieldError("contactEmailText") != null) {

            jsonResponse.setStatus("FAIL");
            jsonResponse.setValidationResult(bindingResult.getAllErrors());
            String jsonResponseObj = new Gson().toJson(updateApplicationAddressForm);
            jsonResponse.setJsonResult(jsonResponseObj);

        } else {

            // There are none.
            // So, check for validation errors in the application numbers/patent
            // numbers array
            FieldError fieldError = bindingResult.getFieldError("updateApplicationAddressNumbersArray");

            if (fieldError != null) {
                @SuppressWarnings("unchecked")
                List<UpdateApplicationAddressNumbers> updateApplicationAddressNumbers = (List<UpdateApplicationAddressNumbers>) fieldError
                        .getRejectedValue();

                boolean anyValidationErrors = false;
                
                if(updateApplicationAddressNumbers.size() != 0 &&  updateApplicationAddressNumbers != null) {
                	for (UpdateApplicationAddressNumbers updateApplicationAddressNumber : updateApplicationAddressNumbers) {
                        if (validationErrorMessages(updateApplicationAddressNumber.getValidationErrorMessage())
                                || ("N".equals(updateApplicationAddressNumber.getPowerOfAttorney()))) {
                            anyValidationErrors = true;
                            break;
                        }
                    }
                } else {
                	anyValidationErrors = true;
                }
                
                if (!anyValidationErrors) {
                	
                    // Get the correspondence address associated with the
                    // new customer number (if the form is not already
                    // populated with the correspondence address)
                   if (StringUtils.isEmpty(updateApplicationAddressForm.getNameLineOne())
                           && StringUtils.isEmpty(updateApplicationAddressForm.getStreetLineOne()))
                        updateApplicationAddressForm = updateApplicationAddressServices
                                .getCorrespondenceAddress(updateApplicationAddressForm);

                    jsonResponse.setStatus("SUCCESS");

                //    logger.debug("status = " + updateApplicationAddressForm.getStatus());

                    String jsonResponseObj = new Gson().toJson(updateApplicationAddressForm);
                    jsonResponse.setJsonResult(jsonResponseObj);

                } else {

                    jsonResponse.setStatus("FAIL");
                    jsonResponse.setValidationResult(bindingResult.getAllErrors());
                    String jsonResponseObj = new Gson().toJson(updateApplicationAddressForm);
                    jsonResponse.setJsonResult(jsonResponseObj);

                }
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // End time
        String activityEndTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
        long startTimeMillis = System.currentTimeMillis();

     //   Statistics.addPairAdminActivity(updateApplicationAddressForm.getPairId(),
       //         (String) request.getSession().getAttribute("distinguishedName"),
        //        Statistics.PREVIEWUPDATEAPPLICATIONADDRESSREQUEST, activityStartTime, activityEndTime, "0", startTimeMillis, 0,
        //        "0", privatePairCommonController.getClientIP(request));

        return new ResponseEntity<JsonResponse>(jsonResponse, headers, HttpStatus.OK);
    }


    private boolean validationErrorMessages(String validationErrorMessage) {

        return !validationErrorMessage.matches("<ol></ol>");
    }

    /**
     * Save a new Update Application Address Change request.
     *
     * @param sData   {@link String} A JSON String containing the Update Application
     *                Address Change request information.
     * @param request {@link HttpServletRequest}
     * @return {@link String} Save operation success/fail status message.
     * @throws Exception
     */
    @PostMapping(value = "/saveUpdateApplicationAddressRequest")
    public ResponseEntity<String> saveUpdateApplicationAddressRequest(@RequestBody String sData, HttpServletRequest request,
    		HttpServletResponse response) throws PairAdminDatabaseException, Exception {

    //	logger.debug("sData: " + sData);

    	String jsonResponseObj = null;

    	Gson gson = new Gson();
    	UpdateApplicationAddressForm updateApplicationAddressForm = gson.fromJson(sData, UpdateApplicationAddressForm.class);
    	request.getSession().setAttribute("DnValue",updateApplicationAddressForm.getDn());
    	// Start time
    	String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();

    	String dn = (String) request.getSession().getAttribute("DnValue");
if(dn != null){
    			logger.info("going to call service now");
    			updateApplicationAddressForm = updateApplicationAddressServices.saveUpdateApplicationAddress(
    					updateApplicationAddressForm, dn);
    			createSavedMessage(updateApplicationAddressForm);
    			// Convert Java object to json string.
    			jsonResponseObj = new Gson().toJson(updateApplicationAddressForm);
    		}
    		else{
        	//	return new ResponseEntity<String>(messageSource.getMessage("session.expire.error", null, null), headers,
        	//		HttpStatus.BAD_REQUEST);
    		}
    	
    	
    	// End time
    	String activityEndTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
    	long startTimeMillis = System.currentTimeMillis();

    	 String statsActivity = Statistics.addPairAdminActivity(updateApplicationAddressForm.getPairId(),
                 (String) request.getSession().getAttribute("distinguishedName"), Statistics.SAVEUPDATEAPPLICATIONADDRESSREQUEST,
                 activityStartTime, activityEndTime, "0", startTimeMillis, 0, "0",
                 IpAddressUtil.getClientIP(request));

         log.info("Stats Activity:  {}",statsActivity);
         
    	return new ResponseEntity<String>(jsonResponseObj, headers, HttpStatus.OK);        
    }


    
    
   
    private void createSavedMessage(UpdateApplicationAddressForm updateApplicationAddressForm) {
        // Calculate the 7 days by first retrieving the saved Date and plus
        // 7 days.
        Calendar c = Calendar.getInstance();
        //     c.setTime(updateApplicationAddressForm.getCreatedTs());
        c.add(Calendar.DATE, 7);

        Date validUntilDate = c.getTime();
        Format formatter = new SimpleDateFormat("MMM-dd-yyyy");
        String dateString = formatter.format(validUntilDate);

        String message = "Your request will be saved for your convenience until midnight Eastern Time on " +
                dateString + " and then will be deleted.";
        updateApplicationAddressForm.setSavedMessage(message);
    }

  

    /**
     * Update an already saved Update Application Address request.
     *
     * @param sData   {@link String} A JSON string containing the updated Update
     *                Application Address request.
     * @param request {@link HttpServletRequest}
     * @return {@link String} Update operation success/fail status message.
     * @throws PairAdminDatabaseException
     * @throws Exception
     */
    @PostMapping(value = "/updateApplicationAddress")
    public ResponseEntity<String> updateAnUpdateApplicationAddressRequest(@RequestBody String sData, HttpServletRequest request) throws PairAdminDatabaseException, Exception {
    	String jsonResponseObj = null;
        // Start time
        String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();

        UpdateApplicationAddressForm updateApplicationAddressForm = new Gson()
                .fromJson(sData, UpdateApplicationAddressForm.class);
    	request.getSession().setAttribute("DnValue",updateApplicationAddressForm.getDn());
        String dn = (String) request.getSession().getAttribute("DnValue");
           if (dn != null) {
            Date lastModifiedTimeStamp = updateApplicationAddressServices.updateAnUpdateApplicationAddress(
                    updateApplicationAddressForm, dn);

            createSavedMessage(updateApplicationAddressForm);

            updateApplicationAddressForm.setLastModifiedTimeStamp(new Date());
            jsonResponseObj = new Gson().toJson(updateApplicationAddressForm);
        }
        // End time
        String activityEndTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
        long startTimeMillis = System.currentTimeMillis();

        String statsActivity = Statistics.addPairAdminActivity(updateApplicationAddressForm.getPairId(),
                (String) request.getSession().getAttribute("distinguishedName"),
                Statistics.UPDATEANUPDATEAPPLICATIONADDRESSREQUEST, activityStartTime, activityEndTime, "0", startTimeMillis, 0,
                "0", IpAddressUtil.getClientIP(request));

        log.info("Stats Activity: {} ", statsActivity);

        return new ResponseEntity<String>(jsonResponseObj, headers, HttpStatus.OK); 
    }
    
    
    
    @PostMapping(value = "/transmitUpdateApplicationAddressRequest")
   public ResponseEntity<String> transmitUpdateApplicationAddressRequest(@RequestBody String sData, HttpServletRequest request,
    		HttpServletResponse response) throws PairAdminDatabaseException {

    	logger.debug("sData: " + sData);

    	String jsonResponseObj = null;

    	// Start time
    	String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();

    	UpdateApplicationAddressForm updateApplicationAddressForm = new Gson()
    	.fromJson(sData, UpdateApplicationAddressForm.class);
    	request.getSession().setAttribute("DnValue",updateApplicationAddressForm.getDn());
    	 String dn = (String) request.getSession().getAttribute("DnValue");
    	 System.out.println(dn);
    	try {
    		if(dn != null){
    			System.out.println("inside if dn is not null"+dn);
    			// Save the address first in PURM database, and forward it to PALM.
    			// Either update or save the application depending on whether or
    			// not the request has already been saved
    			if (!(StringUtils.isEmpty(updateApplicationAddressForm.getPairId()))) {
    				// Update -- The request was previously saved
    				System.out.println("inside update");
    				updateApplicationAddressForm.setLastModifiedTimeStamp(updateApplicationAddressServices
    						.updateAnUpdateApplicationAddress(updateApplicationAddressForm, dn));
    		}
    			else {
    				System.out.println("inside save");
    				// Save -- The request was not previously saved
    				updateApplicationAddressForm = updateApplicationAddressServices.saveUpdateApplicationAddress(
    						updateApplicationAddressForm, dn);
    			
    			}

    			// Send the request to PALM web services
    			updateApplicationAddressServices.transmitUpdateApplicationAddressRequest(updateApplicationAddressForm);

    			// Retrieve the list of update application address requests
    			List<UpdateApplicationAddressNumbers> updateApplicationAddressNumbers = updateApplicationAddressForm
    					.getUpdateApplicationAddressNumbersArray();

    			// Cycle through the list
    			boolean anyRejections = false;
    			for (UpdateApplicationAddressNumbers updateApplicationAddressNumber : updateApplicationAddressNumbers) {
    				if ("Rejected".equals(updateApplicationAddressNumber.getValidationErrorMessage())) {
    					anyRejections = true;
    					break;
    				}
    			}

    			if (anyRejections) {
    				updateApplicationAddressForm
    				.setSavedMessage("Correspondence and/or Maintenance Fee Addresses of the below-identified application(s) were successfully changed to the "
    						+ "address associated to Customer Number "
    						+ updateApplicationAddressForm.getCustomerNumber()
    						+ " listed below.  For applications that were rejected, there was a system problem. Please try the rejected cases again or contact the EBC.");

    				// Update the status on the form to submitted
    				updateApplicationAddressForm.setStatus(Constants.FAILED_REQUEST);

    			} else {
    				updateApplicationAddressForm
    				.setSavedMessage("Correspondence and/or Maintenance Fee Addresses of the below-identified application(s) were successfully changed to the address associated to Customer Number "
    						+ updateApplicationAddressForm.getCustomerNumber() + " listed below.");

    				// Update the status on the form to submitted
    				updateApplicationAddressForm.setStatus(Constants.SUBMITTED_REQUEST);

    			}

    			// Either update or save the application depending on whether or
    			// not the request has already been saved
    			if (StringUtils.isEmpty(updateApplicationAddressForm.getPairId()))
    				// Save -- The request was not previously saved
    				updateApplicationAddressForm = updateApplicationAddressServices.saveUpdateApplicationAddress(
    						updateApplicationAddressForm, dn);
    			else {
    				// Update -- The request was previously saved
    				updateApplicationAddressForm.setLastModifiedTimeStamp(updateApplicationAddressServices
    						.updateAnUpdateApplicationAddress(updateApplicationAddressForm, dn));
    			}

    			updateApplicationAddressServices.updateRequestStatusByUserRequestId(updateApplicationAddressForm);

    			if (Constants.SUBMITTED_REQUEST.equals(updateApplicationAddressForm.getStatus())) {
    				// Insert Customer Correspondence details when it is submitted
    				updateApplicationAddressServices.insertCustomerCorrAddress(updateApplicationAddressForm);
    			}
    		}
    		else{
        		return new ResponseEntity<String>(messageSource.getMessage("session.expire.error", null, null), headers,
        				HttpStatus.BAD_REQUEST);
    		}
    	} catch (PairAdminDatabaseException pade) {
    		logger.error("Failed to update the saved address: " + pade);
    		return new ResponseEntity<String>(messageSource.getMessage("updateStatus.update.error", null, null), headers,
    				HttpStatus.BAD_REQUEST);
    	} catch (Exception e) {
    		logger.error("Failed to update the saved address: " + e);
    		return new ResponseEntity<String>(messageSource.getMessage("updateStatus.update.error", null, null), headers,
    				HttpStatus.BAD_REQUEST);
    	}

    	// End time
    	String activityEndTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
    	long startTimeMillis = System.currentTimeMillis();

    	
    	 String statsActivity = Statistics.addPairAdminActivity(updateApplicationAddressForm.getPairId(),
                 (String) request.getSession().getAttribute("distinguishedName"),
                 Statistics.TRANSMITUPDATEAPPLICATIONADDRESSREQUEST, activityStartTime, activityEndTime, "0", startTimeMillis, 0,
                 "0", IpAddressUtil.getClientIP(request));
    	 
    	 
    	jsonResponseObj = new Gson().toJson(updateApplicationAddressForm);

    	return new ResponseEntity<String>(jsonResponseObj, headers, HttpStatus.OK);
    }

    
    
    
}
