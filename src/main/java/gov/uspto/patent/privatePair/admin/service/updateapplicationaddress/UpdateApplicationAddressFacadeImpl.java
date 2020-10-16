package gov.uspto.patent.privatePair.admin.service.updateapplicationaddress;


import gov.uspto.patent.privatePair.admin.controller.ExternalController;
import gov.uspto.patent.privatePair.admin.dao.CustomerCorrAddressDao;
import gov.uspto.patent.privatePair.admin.dao.PairUserDnDao;
import gov.uspto.patent.privatePair.admin.dao.ViewRequestDao;
import gov.uspto.patent.privatePair.admin.dao.UpdateApplicationAddressDao;
import gov.uspto.patent.privatePair.admin.dao.UserRequestDao;
import gov.uspto.patent.privatePair.admin.dao.UserSignatureDao;
import gov.uspto.patent.privatePair.admin.domain.AddressChangeSupportData;
import gov.uspto.patent.privatePair.admin.domain.ApplicationAddressReqType;
import gov.uspto.patent.privatePair.admin.domain.ApplicationIdentifierType;
import gov.uspto.patent.privatePair.admin.domain.AuditAdmin;
import gov.uspto.patent.privatePair.admin.domain.Constants;
import gov.uspto.patent.privatePair.admin.domain.CreateNewCustomerNumberForm;
import gov.uspto.patent.privatePair.admin.domain.LifecycleAdmin;
import gov.uspto.patent.privatePair.admin.domain.UpdateApplicationAddressForm;
import gov.uspto.patent.privatePair.admin.domain.UpdateApplicationAddressNumbers;
import gov.uspto.patent.privatePair.admin.domain.ViewSaveCompleteForm;
import gov.uspto.patent.privatePair.admin.dto.CustomerCorrAddressDto;
import gov.uspto.patent.privatePair.admin.dto.UpdateApplicationAddressDto;
import gov.uspto.patent.privatePair.admin.dto.UserRequestDto;
import gov.uspto.patent.privatePair.admin.dto.UserSignatureDto;
import gov.uspto.patent.privatePair.admin.dto.ViewRequestDto;
import gov.uspto.patent.privatePair.exceptionhandlers.PairAdminDatabaseException;
import gov.uspto.patent.privatePair.portal.controller.ApplicationSearchTabController;
import gov.uspto.patent.privatePair.utils.SSLUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;


/**
 * Service layer class with helper methods used by all three subsystems Create
 * New Customer Number, Edit Entity Status and Update Application Address.
 * 
 */
@Component
public class UpdateApplicationAddressFacadeImpl implements UpdateApplicationAddressServices {

    @Autowired
    ViewRequestDao viewRequestDao;

    @Autowired
    PairUserDnDao pairUserDnDao;
    
    @Autowired
    UserRequestDao userRequestDao;
    
    @Autowired
    UserSignatureDao userSignatureDao;
    
    @Autowired
    UpdateApplicationAddressDao updateApplicationAddressDao;
    
    @Autowired
    CustomerCorrAddressDao customerCorrAddressDao;
    
    @Autowired
    ExternalController exController;
    
    @Autowired
    ApplicationSearchTabController appController;
    
    @Value("${opsgbp.webservice.url}")
    String OPSBPServiceUrl;
   
    private RestTemplate restTemplate;
    
    private String SYSTEM_ID = "ppairusr";

   
    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public List<ViewSaveCompleteForm> getUpdateAddrRequestsforView(List<?> requestStatusList, String requestDays,
            String requestType, String privatePAIRdn) throws PairAdminDatabaseException, Exception {
    	 System.out.println("callingDao");
        List<ViewRequestDto> viewRequestDtoList = viewRequestDao.getUpdateAddrRequestsforView(requestStatusList, requestDays,
                requestType, privatePAIRdn);

        List<ViewSaveCompleteForm> viewUpdateAddrList = new ArrayList<ViewSaveCompleteForm>();
        System.out.println("ViewUpdateAddrList Objects:" +viewUpdateAddrList);
        for (ViewRequestDto viewReqDto : viewRequestDtoList) {

            ViewSaveCompleteForm UpdateAddrViewForm = new ViewSaveCompleteForm();

            UpdateAddrViewForm.setUpdateAddrPairId(viewReqDto.getUpdateAddrPairId());
            UpdateAddrViewForm.setUpdateAddrTs(viewReqDto.getUpdateAddrTs());
            UpdateAddrViewForm.setUpdateAddrCN(viewReqDto.getUpdateAddrCN());
            UpdateAddrViewForm.setUpdateAddrStatus(viewReqDto.getUpdateAddrStatus());

            viewUpdateAddrList.add(UpdateAddrViewForm);
        }
        return viewUpdateAddrList;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public UpdateApplicationAddressForm saveUpdateApplicationAddress(UpdateApplicationAddressForm updateApplicationAddressForm,
            String dn) throws Exception {

        String userId = "";
        String commonName = "";

        // Extract the "Common Name" from the "Distinguished Name"
        if (StringUtils.isNotEmpty(dn)) {
            userId = pairUserDnDao.getPairUserDnByDn(dn);
            System.out.println(userId);
            int startIndex = dn.indexOf("cn=") + 3;
            int stopIndex = dn.indexOf(", ou=");
            commonName = dn.substring(startIndex, stopIndex);
            System.out.println(commonName);
        } else {
            throw new Exception("distinguished name is null; cannot retrieve common nameee");
        }

        UserRequestDto userRequestDto = new UserRequestDto();

        if (StringUtils.isNotEmpty(updateApplicationAddressForm.getPairId())) {
            userRequestDto.setUserRequestId(Long.valueOf(updateApplicationAddressForm.getPairId()));
        }

        userRequestDto.setSubmitterRegistrationNo(updateApplicationAddressForm.getRegistrationNumber());
        userRequestDto.setContactFullName(updateApplicationAddressForm.getContactFullName());
        userRequestDto.setContactTelephoneNoText(updateApplicationAddressForm.getContactTelephoneNoText());
        userRequestDto.setContactEmailText(StringUtils.trim(updateApplicationAddressForm.getContactEmailText()));
        userRequestDto.setFkUserId(userId);
        userRequestDto.setCreateUserId(commonName);
        userRequestDto.setLastModifiedUserId(commonName);
        System.out.println(updateApplicationAddressForm.getRegistrationNumber());
        System.out.println(userRequestDto.getCreateUserId());
        if (StringUtils.isEmpty(updateApplicationAddressForm.getPairId())) {
            userRequestDto.setCreateTimeStamp(new Date());
        } else {
            userRequestDto.setCreateTimeStamp(updateApplicationAddressForm.getCreatedTs());
        }

        userRequestDto.setLastModifiedTimeStamp(new Date());

        /**
         * TODO: Create an Enum constant for the type of Requests and request
         * status.
         */
        userRequestDto.setTypeOfRequest("Address Change");

        String status = updateApplicationAddressForm.getStatus();
        if (StringUtils.isEmpty(status)) {
            userRequestDto.setRequestStatusCount(Constants.SAVED_REQUEST);
        } else if (Constants.SUBMITTED_REQUEST.equals(status)) {
            userRequestDto.setRequestStatusCount(Constants.SUBMITTED_REQUEST);
        } else if (Constants.FAILED_REQUEST.equals(status)) {
            userRequestDto.setRequestStatusCount(Constants.FAILED_REQUEST);
        } else {
            userRequestDto.setRequestStatusCount(Constants.SAVED_REQUEST);
        }
        System.out.println(userRequestDto.getRequestStatusCount());
        String dto=userRequestDto.toString();
        System.out.println("userRequestId");
        long userRequestId = userRequestDao.insertUserRequest(userRequestDto);
        System.out.println("userRequestId");
        System.out.println(userRequestId);
        if (updateApplicationAddressForm.getUpdateApplicationAddressNumbersArray().size() > 0) {
            List<UpdateApplicationAddressDto> updateApplicationAddressDtoList = new ArrayList<UpdateApplicationAddressDto>();
            System.out.println("inside if");
            // Insert the Name/Signature list in the UserSignature table.
            List<UserSignatureDto> userSignatureDtoList = new ArrayList<UserSignatureDto>();

            UserSignatureDto userSignatureDto = new UserSignatureDto();

            userSignatureDto.setFkUserRequestId(Long.toString(userRequestId));
            userSignatureDto.setUserSignatureTx(StringUtils.trim(updateApplicationAddressForm.getSubmitterSignature()));
            userSignatureDto.setUserSignatureNmTx(updateApplicationAddressForm.getCommonName());

            userSignatureDtoList.add(userSignatureDto);

            userSignatureDao.insertUserSignatureList(userSignatureDtoList);

            for (UpdateApplicationAddressNumbers updateApplicationAddressNumbers : updateApplicationAddressForm
                    .getUpdateApplicationAddressNumbersArray()) {
                UpdateApplicationAddressDto updateApplicationAddressDto = new UpdateApplicationAddressDto();

                updateApplicationAddressDto.setFKeyUserRequestId(Long.toString(userRequestId));
                updateApplicationAddressDto.setCreateTimeStamp(new Date());
                updateApplicationAddressDto.setCreateUserId(updateApplicationAddressForm.getCommonName());

                String customerNumber = updateApplicationAddressForm.getCustomerNumber();
                updateApplicationAddressDto.setCustomerNumber(StringUtils.isNotEmpty(customerNumber) ? Integer
                        .valueOf(customerNumber) : null);

                updateApplicationAddressDto.setApplication_id(updateApplicationAddressNumbers.getApplicationNumber()
                        .replaceAll("[/,]", "").trim());
                updateApplicationAddressDto.setPatentNumber(updateApplicationAddressNumbers.getPatentNumber()
                        .replaceAll("[,]", "").trim());
                updateApplicationAddressDto
                        .setCorrAddrChaneIn(updateApplicationAddressNumbers.getCorrespondenceAddressCheckBox());
                updateApplicationAddressDto.setMaintFeeAddrChaneIn(updateApplicationAddressNumbers
                        .getMaintenanceFeeAddressCheckBox());
                updateApplicationAddressDto
                        .setAttrAuthIn("1".equals(updateApplicationAddressForm.getAttorneyCertification()) ? "Y" : "N");
                updateApplicationAddressDto.setLastModifiedTimeStamp(new Date());
                updateApplicationAddressDto.setLastModifiedUserId(updateApplicationAddressForm.getCommonName());
                updateApplicationAddressDto.setRequestErrorTx(updateApplicationAddressNumbers.getValidationErrorMessage());
                updateApplicationAddressDto.setPowerOfAttorneyIndicator(updateApplicationAddressNumbers.getPowerOfAttorney());

                updateApplicationAddressDtoList.add(updateApplicationAddressDto);
            }

            updateApplicationAddressDao.insertUpdateAppAddressRequestList(updateApplicationAddressDtoList);

        } else {
        	
        	System.out.println("inside else");
            UpdateApplicationAddressDto updateApplicationAddressDto = new UpdateApplicationAddressDto();

            updateApplicationAddressDto.setFKeyUserRequestId(Long.toString(userRequestId));
            updateApplicationAddressDto.setCreateUserId(updateApplicationAddressForm.getCommonName());

            updateApplicationAddressDto
                    .setCustomerNumber(StringUtils.isEmpty(updateApplicationAddressForm.getCustomerNumber()) ? null : Integer
                            .valueOf(updateApplicationAddressForm.getCustomerNumber()));

            updateApplicationAddressDto.setLastModifiedUserId(updateApplicationAddressForm.getCommonName());

            // Insert the Name/Signature list (in this case only one) in the
            // UserSignature table.
            List<UserSignatureDto> userSignatureDtoList = new ArrayList<UserSignatureDto>();

            UserSignatureDto userSignatureDto = new UserSignatureDto();

            userSignatureDto.setFkUserRequestId(Long.toString(userRequestId));
            userSignatureDto.setUserSignatureTx(StringUtils.trim(updateApplicationAddressForm.getSubmitterSignature()));
            userSignatureDto.setUserSignatureNmTx(updateApplicationAddressForm.getCommonName());

            userSignatureDtoList.add(userSignatureDto);

            userSignatureDao.insertUserSignatureList(userSignatureDtoList);

            updateApplicationAddressDao.insertUpdateAppAddressRequestWhenListIsEmpty(
                    Long.valueOf(updateApplicationAddressDto.getFKeyUserRequestId()),
                    updateApplicationAddressDto.getCustomerNumber(), updateApplicationAddressDto.getApplication_id(),
                    updateApplicationAddressDto.getPatentNumber(), updateApplicationAddressDto.getCorrAddrChaneIn(),
                    updateApplicationAddressDto.getMaintFeeAddrChaneIn(), updateApplicationAddressDto.getAttrAuthIn(),
                    userRequestId, updateApplicationAddressDto.getRequestErrorTx(),
                    updateApplicationAddressDto.getPowerOfAttorneyIndicator());
        }

        updateApplicationAddressForm.setPairId(String.valueOf(userRequestId));
        updateApplicationAddressForm.setCreatedTs(userRequestDto.getCreateTimeStamp());
        updateApplicationAddressForm.setStatus(userRequestDto.getRequestStatusCount());
        System.out.println("returning back to controller");
        System.out.println(updateApplicationAddressForm);
        return updateApplicationAddressForm;
    }
    
    /**
     * {@inheritDoc}
     */
    @Transactional
    public UpdateApplicationAddressForm getUpdateAddressRequest(Long userRequestId) throws Exception {

        UserRequestDto userRequestDto = userRequestDao.getUserRequestById(userRequestId);
        List<UpdateApplicationAddressDto> updateAppAddrDtoList = updateApplicationAddressDao
                .getUpdateAppAddressRequestById(userRequestId);

        List<UpdateApplicationAddressNumbers> updateAppAddrNumbersArray = new ArrayList<UpdateApplicationAddressNumbers>();

        UpdateApplicationAddressForm updateApplicationAddressForm = new UpdateApplicationAddressForm();

        updateApplicationAddressForm.setPairId(Long.toString(userRequestDto.getUserRequestId()));
        updateApplicationAddressForm.setCreatedTs(userRequestDto.getCreateTimeStamp());
        updateApplicationAddressForm.setStatus(userRequestDto.getRequestStatusCount());

        updateApplicationAddressForm.setRegistrationNumber(userRequestDto.getSubmitterRegistrationNo());
        updateApplicationAddressForm.setContactFullName(userRequestDto.getContactFullName());
        updateApplicationAddressForm.setContactTelephoneNoText(userRequestDto.getContactTelephoneNoText());
        updateApplicationAddressForm.setContactEmailText(StringUtils.trim(userRequestDto.getContactEmailText()));

        // Get the Name/Signature list (in this case only one) in the
        // UserSignature table.
System.out.println(userRequestId.intValue());
        List<UserSignatureDto> userSignatureDtoList = userSignatureDao.getAllUserSignatureById(userRequestId);
        System.out.println(userSignatureDtoList.get(0).getUserSignatureNmTx());
        updateApplicationAddressForm.setCommonName(userSignatureDtoList.get(0).getUserSignatureNmTx());
        updateApplicationAddressForm.setSubmitterSignature(userSignatureDtoList.get(0).getUserSignatureTx());
        updateApplicationAddressForm.setCustomerNumber(String.valueOf(updateAppAddrDtoList.get(0).getCustomerNumber()));
        updateApplicationAddressForm.setAttorneyCertification(updateAppAddrDtoList.get(0).getAttrAuthIn());

        for (UpdateApplicationAddressDto updateApplicationAddressDto : updateAppAddrDtoList) {

            UpdateApplicationAddressNumbers updateAppAddrNumbers = new UpdateApplicationAddressNumbers();

            updateAppAddrNumbers.setApplicationNumber(updateApplicationAddressDto.getApplication_id());
            updateAppAddrNumbers.setPatentNumber(updateApplicationAddressDto.getPatentNumber());
            updateAppAddrNumbers.setCorrespondenceAddressCheckBox(updateApplicationAddressDto.getCorrAddrChaneIn());
            updateAppAddrNumbers.setMaintenanceFeeAddressCheckBox(updateApplicationAddressDto.getMaintFeeAddrChaneIn());
            updateAppAddrNumbers.setValidationErrorMessage(updateApplicationAddressDto.getRequestErrorTx());
            updateAppAddrNumbers.setPowerOfAttorney(updateApplicationAddressDto.getPowerOfAttorneyIndicator());

            updateAppAddrNumbersArray.add(updateAppAddrNumbers);
        }

        updateApplicationAddressForm.setUpdateApplicationAddressNumbersArray(updateAppAddrNumbersArray);
        System.out.println(updateApplicationAddressForm.getStatus());
        if (updateApplicationAddressForm.getStatus().equalsIgnoreCase("Submitted")) {

            CustomerCorrAddressDto customerCorrAddressDto = customerCorrAddressDao.getCustomerCorrAddressById(userRequestId);

            if (customerCorrAddressDto != null) {
                updateApplicationAddressForm.setNameLineOne(customerCorrAddressDto.getFirmorIndividualNameLine1());
                updateApplicationAddressForm.setNameLineTwo(customerCorrAddressDto.getFirmorIndividualNameLine2());
                updateApplicationAddressForm.setStreetLineOne(customerCorrAddressDto.getAddressLine1());
                updateApplicationAddressForm.setStreetLineTwo(customerCorrAddressDto.getAddressLine2());
                updateApplicationAddressForm.setCityName(customerCorrAddressDto.getCity());
                updateApplicationAddressForm.setState(customerCorrAddressDto.getState());
                updateApplicationAddressForm.setCountryName(customerCorrAddressDto.getCountry());
                updateApplicationAddressForm.setPostalCode(customerCorrAddressDto.getZip());
            }
        }

        return updateApplicationAddressForm;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Integer deleteUpdateApplicationAddress(long userRequestId) throws PairAdminDatabaseException {

        UpdateApplicationAddressDto updateApplicationAddressDto = new UpdateApplicationAddressDto();
        updateApplicationAddressDto.setUserRequestId(userRequestId);
        updateApplicationAddressDto.setDeleteIndicator("Y");
        updateApplicationAddressDto.setLastModifiedTimeStamp(new Date());
        updateApplicationAddressDto.setRequestStatusCount("Deleted");

        int resultCount = updateApplicationAddressDao.deleteUpdateApplicationAddressByUserRequestId(
                updateApplicationAddressDto.getLastModifiedTimeStamp(), updateApplicationAddressDto.getDeleteIndicator(),
                updateApplicationAddressDto.getRequestStatusCount(), updateApplicationAddressDto.getUserRequestId());

        return resultCount;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Date updateAnUpdateApplicationAddress(UpdateApplicationAddressForm updateApplicationAddressForm, String dn)
            throws PairAdminDatabaseException, Exception {
    	System.out.println("inside updateAnUpdateApplicationAddress");
    	System.out.println(dn);
        String userId = "";
        String commonName = "";

        if (StringUtils.isNotEmpty(dn)) {
            userId = pairUserDnDao.getPairUserDnByDn(dn);
            System.out.println(userId);
            commonName = dn.substring(dn.indexOf("cn=") + 3, dn.indexOf(", ou="));
            System.out.println(commonName);
        } else {
            throw new Exception("distinguished name is null; cannot retrieve common namex");
        }

        UserRequestDto userRequestDto = new UserRequestDto();
        if (StringUtils.isNotEmpty(updateApplicationAddressForm.getPairId())) {
            userRequestDto.setUserRequestId(Long.valueOf(updateApplicationAddressForm.getPairId()));
        }

        userRequestDto.setSubmitterRegistrationNo(updateApplicationAddressForm.getRegistrationNumber());
        userRequestDto.setContactFullName(updateApplicationAddressForm.getContactFullName());
        userRequestDto.setContactTelephoneNoText(updateApplicationAddressForm.getContactTelephoneNoText());
        userRequestDto.setContactEmailText(StringUtils.trim(updateApplicationAddressForm.getContactEmailText()));
        userRequestDto.setFkUserId(userId);
        userRequestDto.setLastModifiedUserId(commonName);

        if (StringUtils.isEmpty(updateApplicationAddressForm.getPairId())) {
            userRequestDto.setCreateTimeStamp(new Date());
        } else {
            userRequestDto.setCreateTimeStamp(updateApplicationAddressForm.getCreatedTs());
        }

        userRequestDto.setLastModifiedTimeStamp(new Date());

        /**
         * TODO: Create an Enum constant for the type of Requests and request
         * status.
         */
        userRequestDto.setTypeOfRequest("Address Change");
        userRequestDto.setRequestStatusCount("Saved");

        // Update the user request table
        try {

            userRequestDao.updateUserRequest(userRequestDto);

            long userRequestId = userRequestDto.getUserRequestId();

            // Before updating, first delete all the Name/Signature for the
            // given userRequestId.
            userSignatureDao.deleteUserSignatureByRequestId(userRequestId);

            // Insert the Name/Signature list (in this case only one) in the
            // UserSignature table.
            List<UserSignatureDto> userSignatureDtoList = new ArrayList<UserSignatureDto>();

            UserSignatureDto userSignatureDto = new UserSignatureDto();

            userSignatureDto.setFkUserRequestId(String.valueOf(userRequestId));
            userSignatureDto.setUserSignatureTx(StringUtils.trim(updateApplicationAddressForm.getSubmitterSignature()));
            userSignatureDto.setUserSignatureNmTx(updateApplicationAddressForm.getCommonName());

            userSignatureDtoList.add(userSignatureDto);

            userSignatureDao.insertUserSignatureList(userSignatureDtoList);

            if (updateApplicationAddressForm.getUpdateApplicationAddressNumbersArray().size() > 0) {

                // Delete all existing rows
                updateApplicationAddressDao.deleteUpdateAppAddressByRequestId(userRequestDto.getUserRequestId());

                List<UpdateApplicationAddressDto> updateApplicationAddressDtoList = new ArrayList<UpdateApplicationAddressDto>();

                for (UpdateApplicationAddressNumbers updateApplicationAddressNumbers : updateApplicationAddressForm
                        .getUpdateApplicationAddressNumbersArray()) {
                    UpdateApplicationAddressDto updateApplicationAddressDto = new UpdateApplicationAddressDto();

                    updateApplicationAddressDto.setFKeyUserRequestId(String.valueOf(userRequestId));
                    updateApplicationAddressDto.setCreateTimeStamp(new Date());
                    updateApplicationAddressDto.setCreateUserId(updateApplicationAddressForm.getCommonName());

                    String customerNumber = updateApplicationAddressForm.getCustomerNumber();
                    updateApplicationAddressDto.setCustomerNumber(!StringUtils.isEmpty(customerNumber) ? Integer
                            .valueOf(customerNumber) : null);

                    updateApplicationAddressDto.setApplication_id(updateApplicationAddressNumbers.getApplicationNumber()
                            .replaceAll("[/,]", "").trim());
                    updateApplicationAddressDto.setPatentNumber(updateApplicationAddressNumbers.getPatentNumber()
                            .replaceAll("(,)", "").trim());
                    updateApplicationAddressDto.setCorrAddrChaneIn(updateApplicationAddressNumbers
                            .getCorrespondenceAddressCheckBox());
                    updateApplicationAddressDto.setMaintFeeAddrChaneIn(updateApplicationAddressNumbers
                            .getMaintenanceFeeAddressCheckBox());
                    updateApplicationAddressDto
                            .setAttrAuthIn("1".equals(updateApplicationAddressForm.getAttorneyCertification()) ? "Y" : "N");
                    updateApplicationAddressDto.setLastModifiedTimeStamp(new Date());
                    updateApplicationAddressDto.setLastModifiedUserId(updateApplicationAddressForm.getCommonName());
                    updateApplicationAddressDto.setRequestErrorTx(updateApplicationAddressNumbers.getValidationErrorMessage());
                    updateApplicationAddressDto.setPowerOfAttorneyIndicator(updateApplicationAddressNumbers.getPowerOfAttorney());

                    updateApplicationAddressDtoList.add(updateApplicationAddressDto);
                }

                try {
                    updateApplicationAddressDao.insertUpdateAppAddressRequestList(updateApplicationAddressDtoList);
                } catch (PairAdminDatabaseException pade) {
                    throw new PairAdminDatabaseException("Failed to update Update Application Request", pade);
                }
            } else {

                // Delete all existing rows
                updateApplicationAddressDao.deleteUpdateAppAddressByRequestId(userRequestDto.getUserRequestId());

                /*
                 * There are no application/patent number rows. However, there
                 * is other information that may need to be updated. Like
                 * 'Customer Number'
                 */
                UpdateApplicationAddressDto updateApplicationAddressDto = new UpdateApplicationAddressDto();

                updateApplicationAddressDto.setFKeyUserRequestId(Integer.toString(Math.toIntExact(userRequestId)));
                updateApplicationAddressDto.setCustomerNumber(StringUtils.isEmpty(updateApplicationAddressForm
                        .getCustomerNumber()) ? null : Integer.valueOf(updateApplicationAddressForm.getCustomerNumber()));

                updateApplicationAddressDao.insertUpdateAppAddressRequestWhenListIsEmpty(
                        Long.valueOf(Integer.valueOf(updateApplicationAddressDto.getFKeyUserRequestId())),
                        updateApplicationAddressDto.getCustomerNumber(), updateApplicationAddressDto.getApplication_id(),
                        updateApplicationAddressDto.getPatentNumber(), updateApplicationAddressDto.getCorrAddrChaneIn(),
                        updateApplicationAddressDto.getMaintFeeAddrChaneIn(), updateApplicationAddressDto.getAttrAuthIn(),
                        userRequestId, updateApplicationAddressDto.getRequestErrorTx(),
                        updateApplicationAddressDto.getPowerOfAttorneyIndicator());
            }
        } catch (Exception e) {
            throw new PairAdminDatabaseException("Failed to update Update Application Request", e.getCause());
        }

        return userRequestDto.getLastModifiedTimeStamp();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void insertCustomerCorrAddress(UpdateApplicationAddressForm updateApplicationAddressForm)
            throws PairAdminDatabaseException, Exception {

        // Insert the Customer Correspondence Address table
        try {
            CustomerCorrAddressDto customerCorrAddressDto = new CustomerCorrAddressDto();
            customerCorrAddressDto.setFKeyUserRequestId(updateApplicationAddressForm.getPairId());
            customerCorrAddressDto.setCustomerNumber(Integer.parseInt(updateApplicationAddressForm.getCustomerNumber()));
            customerCorrAddressDto.setFirmorIndividualNameLine1(updateApplicationAddressForm.getNameLineOne());
            customerCorrAddressDto.setFirmorIndividualNameLine2(updateApplicationAddressForm.getNameLineTwo());
            customerCorrAddressDto.setAddressLine1(updateApplicationAddressForm.getStreetLineOne());
            customerCorrAddressDto.setAddressLine2(updateApplicationAddressForm.getStreetLineTwo());
            customerCorrAddressDto.setCity(updateApplicationAddressForm.getCityName());
            customerCorrAddressDto.setState(updateApplicationAddressForm.getState());
            customerCorrAddressDto.setCountry(updateApplicationAddressForm.getCountryName());
            customerCorrAddressDto.setZip(updateApplicationAddressForm.getPostalCode());

            // Before inserting delete first
            customerCorrAddressDao.deleteCustomerCorrAddressById(Long.valueOf(updateApplicationAddressForm.getPairId()));
            customerCorrAddressDao.insertCorrAddress(customerCorrAddressDto);

        } catch (Exception e) {
            throw new PairAdminDatabaseException("Failed to insert Customer Correspondence Address", e.getCause());
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void updateRequestStatusByUserRequestId(UpdateApplicationAddressForm updateApplicationAddressForm)
            throws PairAdminDatabaseException {

        // Update the user request table
        try {
            UserRequestDto userRequestDto = new UserRequestDto();
            userRequestDto.setUserRequestId(Long.valueOf(updateApplicationAddressForm.getPairId()));
            userRequestDto.setRequestStatusCount(updateApplicationAddressForm.getStatus());
            userRequestDto.setRequestDescriptionText(updateApplicationAddressForm.getSavedMessage());
            userRequestDto.setLastModifiedTimeStamp(new Date());

            userRequestDao.updateRequestStatusByUserRequestId(userRequestDto);

        } catch (Exception e) {
            throw new PairAdminDatabaseException("Failed to update Update Application Request Status", e.getCause());
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public UpdateApplicationAddressForm getCorrespondenceAddress(UpdateApplicationAddressForm updateApplicationAddressForm) {

        // Fetch the correspondence address using the Customer Number and assign
        // to the UpdateApplicationAddressForm
      
        try {
        	String res = exController.getCustDetails(updateApplicationAddressForm.getCustomerNumber());
        	
        JSONObject jsonObj = new JSONObject(res);
        JSONObject objcustAddr = new JSONObject(jsonObj.getString("customerAddress").toString());
    	JSONArray jsonArrayPhysicalAddress = new JSONArray(objcustAddr.getString("physicalAddress").toString());
    	 String nameLineOneText = jsonArrayPhysicalAddress.getJSONObject(0).getString("nameLineOneText").toString();
    	 updateApplicationAddressForm.setNameLineOne(nameLineOneText);
    	 String nameLineTwoText = jsonArrayPhysicalAddress.getJSONObject(0).getString("nameLineTwoText").toString();
     	 updateApplicationAddressForm.setNameLineTwo(nameLineTwoText);
    	 String addressLineOneText = jsonArrayPhysicalAddress.getJSONObject(0).getString("addressLineOneText").toString();
      	 updateApplicationAddressForm.setStreetLineOne(addressLineOneText);
      	String addressLineTwoText = jsonArrayPhysicalAddress.getJSONObject(0).getString("addressLineTwoText").toString();
      	updateApplicationAddressForm.setStreetLineTwo(addressLineTwoText);
      	 String cityName = jsonArrayPhysicalAddress.getJSONObject(0).getString("cityName").toString();
       	 updateApplicationAddressForm.setCityName(cityName);
       	 String geographicRegionName = jsonArrayPhysicalAddress.getJSONObject(0).getString("geographicRegionName").toString();
      	 updateApplicationAddressForm.setState(geographicRegionName);
      	 String countryName = jsonArrayPhysicalAddress.getJSONObject(0).getString("countryName").toString();
     	 updateApplicationAddressForm.setCountryCode(countryName);
     	 String postalCode = jsonArrayPhysicalAddress.getJSONObject(0).getString("postalCode").toString();
    	 updateApplicationAddressForm.setPostalCode(postalCode);

        }catch (Exception e) {
			System.out.println(e.getMessage());
			
        }

        return updateApplicationAddressForm;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validCustomerNumber(String customerNumber)  {
    	// Validate that the customer number is valid
    	 try {
    	String res = exController.getCustDetails(customerNumber);
    	if(res.contains("No CustomerNumber found that match the query")) {
    		return false;
    	}else {
    		return true;
    	}
         }catch (Exception e) {
 			System.out.println(e);
 			return false;
 		}
     }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateApplicationId(String applicationId) {

    	String appRes= appController.getApplicationNumberFrmService(applicationId, "applicationNumber");

        if (appRes!=null) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean applicationNumberNotFound(String applicationNumber) {


    	String appRes= appController.getApplicationNumberFrmService(applicationNumber, "applicationNumber");

        if (appRes!=null) {
            return false;
        } else {
            return true;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validatePatentId(String patentId) {

    	String patentRes= appController.getApplicationNumberFrmService(patentId, "patentNumber");

        if (patentRes!=null) {
            return true;
        } else {
            return false;
        }
    }
    

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean patentNumberNotFound(String patentId) {

    	String patentRes= appController.getApplicationNumberFrmService(patentId, "patentNumber");

        if (patentRes!=null) {
            return false;
        } else {
            return true;
        }
    }
    

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validateApplicationIdIsAssociatedWithPatentId(String expectedApplicationNumber, String expectedPatentNumber) {

    	String InfoRes= appController.getAppInfoFrmAppNumAndPatentNum(expectedApplicationNumber,expectedPatentNumber);

        if (InfoRes!=null) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validatePowerOfAttorney(String updateRegistrationNumber, String applicationNumber, String patentNumber) {

      
            List<String> registrationNumbers = new ArrayList<String>();
            registrationNumbers=appController.getApplicationAttorneys(applicationNumber, patentNumber);
            System.out.println(registrationNumbers);
            System.out.println(updateRegistrationNumber);
            if (null !=registrationNumbers && registrationNumbers.contains(updateRegistrationNumber)) {
                return true;
            }
            else {
                return false;

        }
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void transmitUpdateApplicationAddressRequest(UpdateApplicationAddressForm updateApplicationAddressForm) {
System.out.println("inside transmitUpdateApplicationAddressRequest service");
       List<String> updateCorrespondenceAddressReturnTypeList = null;
      List<String> updateMaintenanceFeeAddressReturnTypeList = null;

        // Get the list of updates
        List<UpdateApplicationAddressNumbers> updateApplicationAddressNumbersList = updateApplicationAddressForm
                .getUpdateApplicationAddressNumbersArray();

        // Build the Web Service request preface
        ApplicationAddressReqType addressApplicationAddressReqType = buildUpdateApplicationAddressChangeRequest(updateApplicationAddressForm);

        List<String> correspondenceAddressChangeList = buildCorrespondenceAddressChangeList(
                updateApplicationAddressNumbersList, addressApplicationAddressReqType);
System.out.println(correspondenceAddressChangeList);
System.out.println(correspondenceAddressChangeList.size() );
        if (correspondenceAddressChangeList.size() > 0) {

            // Submit the Correspondence Address Changes
        	String responseData = appController.associateApplicationCorrespondenceAddress(addressApplicationAddressReqType);
        			 Gson gson = new Gson();		
        	ApplicationAddressReqType updateApplicationCorrespondenceAddressRespType = gson.fromJson(responseData, ApplicationAddressReqType.class);	
            // Retrieve the response
            updateCorrespondenceAddressReturnTypeList = updateApplicationCorrespondenceAddressRespType
                    .getApplicationNumbers();
        }

        // Reset the request list
        addressApplicationAddressReqType.getApplicationNumbers().clear();

        List<String> maintenanceFeeAddressChangeList = buildMaintenanceFeeAddressChangeList(
                updateApplicationAddressNumbersList, addressApplicationAddressReqType);

        if (maintenanceFeeAddressChangeList.size() > 0) {

            // Submit the Maintenance Fee Address Changes
        	String responseData = appController.associateApplicationCorrespondenceAddressMaintenanceFee(addressApplicationAddressReqType);
       	 Gson gson = new Gson();		
     	ApplicationAddressReqType updateApplicationMaintenanceFeeAddressRespType = gson.fromJson(responseData, ApplicationAddressReqType.class);	
       
            // Retrieve the response
            updateMaintenanceFeeAddressReturnTypeList = updateApplicationMaintenanceFeeAddressRespType
                    .getApplicationNumbers();
        }

        // Cycle through the list of application number/patent numbers and
        // assign the status to each request
        for (UpdateApplicationAddressNumbers updateApplicationAddressNumber : updateApplicationAddressNumbersList) {
            assignStatusMessageToEachRequest(updateApplicationAddressNumber, updateCorrespondenceAddressReturnTypeList,
                    updateMaintenanceFeeAddressReturnTypeList);
        }
    }
    
    private void assignStatusMessageToEachRequest(UpdateApplicationAddressNumbers updateApplicationAddressNumber,
            List<String> updateCorrespondenceAddressReturnTypeList,
            List<String> updateMaintenanceFeeAddressReturnTypeList) {

        if ("1".equals(updateApplicationAddressNumber.getCorrespondenceAddressCheckBox())
                && "1".equals(updateApplicationAddressNumber.getMaintenanceFeeAddressCheckBox())) {

            updateStatusMessageFromBothLists(updateApplicationAddressNumber, updateCorrespondenceAddressReturnTypeList,
                    updateMaintenanceFeeAddressReturnTypeList);

        } else if ("1".equals(updateApplicationAddressNumber.getCorrespondenceAddressCheckBox())) {

            updateStatusMessageFromOneList(updateApplicationAddressNumber, updateCorrespondenceAddressReturnTypeList);

        } else {

            updateStatusMessageFromOneList(updateApplicationAddressNumber, updateMaintenanceFeeAddressReturnTypeList);
        }
    }
    
    
    
    
    private void updateStatusMessageFromBothLists(UpdateApplicationAddressNumbers updateApplicationAddressNumber,
            List<String> updateCorrespondenceAddressReturnTypeList,
            List<String> updateMaintenanceFeeAddressReturnTypeList) {

        String formApplicationNumber = updateApplicationAddressNumber.getApplicationNumber().replaceAll("[/,]", "").trim();
     //   logger.debug("formApplicationNumber is <" + formApplicationNumber + ">");

        String formPatentNumber = updateApplicationAddressNumber.getPatentNumber().replaceAll("(,)", "").trim();
    //   logger.debug("formPatentNumber is <" + formPatentNumber + ">");

        boolean coorespondenceAddressChangeSucceded = false;
        boolean maintenanceFeeAddressChangeSucceded = false;

        for (String applicationIdentifierReturnType : updateCorrespondenceAddressReturnTypeList) {

          /*  String responsePatentNumber = applicationIdentifierReturnType.getApplicationIdentifiers().getPatentNumber()
                    .replaceAll("(,)", "").trim();
            logger.debug("repsonsePatentNumber is <" + responsePatentNumber + ">");*/

            String responseApplicationNumber = applicationIdentifierReturnType.replaceAll("[/,]", "").trim();
          //  logger.debug("responseApplicationNumber is <" + responseApplicationNumber + ">");

            if (formApplicationNumber.equals(responseApplicationNumber)) {

               
                    coorespondenceAddressChangeSucceded = true;
                 

            }
        }

        // Search the Maintenance Fee Update Address List
        for (String applicationIdentifierReturnType : updateMaintenanceFeeAddressReturnTypeList) {

         /*   String responsePatentNumber = applicationIdentifierReturnType.getApplicationIdentifiers().getPatentNumber()
                    .replaceAll("(,)", "").trim();
            logger.debug("repsonsePatentNumber is <" + responsePatentNumber + ">");*/

            String responseApplicationNumber = applicationIdentifierReturnType.replaceAll("[/,]", "").trim();
           // logger.debug("responseApplicationNumber is <" + responseApplicationNumber + ">");

            if (formApplicationNumber.equals(responseApplicationNumber)) {

              
                    maintenanceFeeAddressChangeSucceded = true;
                 
            }
        }

        if (coorespondenceAddressChangeSucceded && maintenanceFeeAddressChangeSucceded)
            updateApplicationAddressNumber.setValidationErrorMessage("Accepted");
        else
            updateApplicationAddressNumber.setValidationErrorMessage("Rejected");
    }
    
    private void updateStatusMessageFromOneList(UpdateApplicationAddressNumbers updateApplicationAddressNumber,
            List<String> updateAddressReturnTypeList) {

        String applicationNumber = updateApplicationAddressNumber.getApplicationNumber().replaceAll("[/,]", "").trim();
        String patentNumber = updateApplicationAddressNumber.getPatentNumber().replaceAll("(,)", "").trim();

        for (String applicationIdentifierReturnType : updateAddressReturnTypeList) {
           if (applicationNumber.equals(applicationIdentifierReturnType.replaceAll("[/,]", "").trim())) {

             
                    updateApplicationAddressNumber.setValidationErrorMessage("Accepted");
                   
            }
        }
    }

    
    
    
  
	private ApplicationAddressReqType buildUpdateApplicationAddressChangeRequest(
            UpdateApplicationAddressForm updateApplicationAddressForm) {
        // Start to build the request object
       System.out.println("inside buildUpdateApplicationAddressChangeRequest");
        ApplicationAddressReqType applicationAddressReqType = new ApplicationAddressReqType();
        AuditAdmin audit = new AuditAdmin();
        audit.setLastModifiedUser("erinkklein FQT Attor");
        applicationAddressReqType.setAudit(audit);
        System.out.println(audit.getLastModifiedUser());
        
        LifecycleAdmin lifecycle= new LifecycleAdmin();
        lifecycle.setBeginDate("1595704744");
        lifecycle.setEndDate("1595704744");
        applicationAddressReqType.setLifeCycle(lifecycle);
        applicationAddressReqType.setChangeCustomerNumber(updateApplicationAddressForm.getCustomerNumber());
        applicationAddressReqType.setNoticeActionCd("addressChange");
        applicationAddressReqType.setRegistrationNumber(updateApplicationAddressForm.getRegistrationNumber());
        applicationAddressReqType.setRequestIdentifier(updateApplicationAddressForm.getPairId());
        
        List<AddressChangeSupportData> addressSupportingDataArray = new ArrayList<AddressChangeSupportData>();
        
        AddressChangeSupportData addressSupportingData = new AddressChangeSupportData();
        addressSupportingData.setNameLineText(updateApplicationAddressForm.getCommonName());
       
        addressSupportingData.setSignature(updateApplicationAddressForm.getSubmitterSignature());
        addressSupportingDataArray.add(addressSupportingData);
        applicationAddressReqType.setSupportData(addressSupportingDataArray);
        System.out.println(applicationAddressReqType.getChangeCustomerNumber());
        
        return applicationAddressReqType;
    }
    
    private List<String> buildCorrespondenceAddressChangeList(
            List<UpdateApplicationAddressNumbers> updateApplicationAddressNumbersList,
            ApplicationAddressReqType applicationAddressReqType) {
    	 List<String> applicationNumbers= new ArrayList<String>();
        // Cycle through the list of application number/patent number pairs
        // building a request list for Correspondence Address changes
        for (UpdateApplicationAddressNumbers updateApplicationAddressNumber : updateApplicationAddressNumbersList) {

            // Correspondence Address Changes
            if ("1".equals(updateApplicationAddressNumber.getCorrespondenceAddressCheckBox())) {
            	System.out.println(updateApplicationAddressNumber.getApplicationNumber().isBlank());
            	if(updateApplicationAddressNumber.getApplicationNumber().isBlank()) {
            			String InfoRes= appController.getAppInfoFrmAppNumAndPatentNum(updateApplicationAddressNumber.getApplicationNumber(),updateApplicationAddressNumber.getPatentNumber().replaceAll("[/,]", "").trim());
            			updateApplicationAddressNumber.setApplicationNumber(InfoRes);
            			System.out.println(updateApplicationAddressNumber.getApplicationNumber());
            		}
            			
            		applicationNumbers.add(updateApplicationAddressNumber.getApplicationNumber()
                        .replaceAll("[/,]", "").trim());

               
            }
        }
        System.out.println(applicationNumbers);
        applicationAddressReqType.setApplicationNumbers(applicationNumbers);

        return applicationAddressReqType.getApplicationNumbers();
    }

    private List<String> buildMaintenanceFeeAddressChangeList(
            List<UpdateApplicationAddressNumbers> updateApplicationAddressNumbersList,
            ApplicationAddressReqType applicationAddressReqType) {
    	 List<String> applicationNumbers=new ArrayList<String>();;
        // Cycle through the list of application number/patent number pairs
        // building a request list for Correspondence Address changes
        for (UpdateApplicationAddressNumbers updateApplicationAddressNumber : updateApplicationAddressNumbersList) {

            // Correspondence Address Changes
            if ("1".equals(updateApplicationAddressNumber.getMaintenanceFeeAddressCheckBox())) {
            	System.out.println(updateApplicationAddressNumber.getApplicationNumber().isBlank());
            	if(updateApplicationAddressNumber.getApplicationNumber().isBlank()) {
            			String InfoRes= appController.getAppInfoFrmAppNumAndPatentNum(updateApplicationAddressNumber.getApplicationNumber(),updateApplicationAddressNumber.getPatentNumber().replaceAll("[/,]", "").trim());
            			updateApplicationAddressNumber.setApplicationNumber(InfoRes);
            			System.out.println(updateApplicationAddressNumber.getApplicationNumber());
            		}
            	applicationNumbers.add(updateApplicationAddressNumber.getApplicationNumber()
                        .replaceAll("[/,]", "").trim());
            }
        }
        applicationAddressReqType.setApplicationNumbers(applicationNumbers);

        return applicationAddressReqType.getApplicationNumbers();
    }

  }
