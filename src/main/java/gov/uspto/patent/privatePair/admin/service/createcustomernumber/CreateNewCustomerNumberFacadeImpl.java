
package gov.uspto.patent.privatePair.admin.service.createcustomernumber;


import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import gov.uspto.patent.privatePair.common.UserType;
import gov.uspto.patent.privatePair.admin.dao.CustomerPractitionerDao;
import gov.uspto.patent.privatePair.admin.dao.NewCustomerNumberDao;
import gov.uspto.patent.privatePair.admin.dao.PairCustomerOptDao;
import gov.uspto.patent.privatePair.admin.dao.PairUserCnDao;
import gov.uspto.patent.privatePair.admin.dao.PairUserDnDao;
import gov.uspto.patent.privatePair.admin.dao.UserRequestDao;
import gov.uspto.patent.privatePair.admin.dao.UserSignatureDao;
import gov.uspto.patent.privatePair.admin.dao.ViewRequestDao;
import gov.uspto.patent.privatePair.admin.domain.Constants;
import gov.uspto.patent.privatePair.admin.domain.CreateNewCustomerNumberForm;
import gov.uspto.patent.privatePair.admin.domain.CustomerPractitionerInfo;
import gov.uspto.patent.privatePair.admin.domain.ViewSaveCompleteForm;
import gov.uspto.patent.privatePair.admin.dto.CustomerPractitionerDto;
import gov.uspto.patent.privatePair.admin.dto.NewCustomerNumberDto;
import gov.uspto.patent.privatePair.admin.dto.PairCustomerOptDto;
import gov.uspto.patent.privatePair.admin.dto.PairUserCnDto;
import gov.uspto.patent.privatePair.admin.dto.PairUserDnDto;
import gov.uspto.patent.privatePair.admin.dto.UserRequestDto;
import gov.uspto.patent.privatePair.admin.dto.UserSignatureDto;
import gov.uspto.patent.privatePair.admin.dto.ViewRequestDto;
import gov.uspto.patent.privatePair.exceptionhandlers.PairAdminDatabaseException;
import gov.uspto.patent.privatePair.utils.SSLUtil;
import gov.uspto.patent.privatePair.admin.service.common.RequestStatus;

/**
 * Service layer class with helper methods CreateNewCustomerNumberFacadeImpl
 * 
 */
@Component
//@Service
public class CreateNewCustomerNumberFacadeImpl implements CreateNewCustomerNumberServices {

	
	  @Value("${opsg.webservice.url}")
		String urlString;
    /**
     * Static variable
     */
    public static final String EMPTY_STRING = "";
    /**
     * Static variable
     */
    public static final String ELECTRONIC = "electronic";
    /**
     * Static variable
     */
    public static final String PAPER = "paper";



    @Autowired
    UserRequestDao userRequestDao;

    @Autowired
    PairUserDnDao pairUserDnDao;

    @Autowired
    PairUserCnDao pairUserCnDao;
//
  @Autowired
  PairCustomerOptDao pairCustomerOptDao;

    @Autowired
    ViewRequestDao viewRequestDao;

    @Autowired
    CustomerPractitionerDao customerPractitionerDao;

    @Autowired
    NewCustomerNumberDao newCustomerNumberDao;

    @Autowired
    UserSignatureDao userSignatureDao;
	
    /**
     * {@inheritDoc}
     */
    
    @Transactional
    @Override
    public CreateNewCustomerNumberForm saveNewCustomerNumber(CreateNewCustomerNumberForm createNewCustomerNumberForm, String dn)
            throws PairAdminDatabaseException {

        try {
            String userId = pairUserDnDao.getPairUserDnByDn(dn);

            UserRequestDto userRequestDto = mapUserRequestDto(createNewCustomerNumberForm, userId);

            Integer userRequestId = Math.toIntExact(userRequestDao.insertUserRequest(userRequestDto));

            saveUserSignatures(createNewCustomerNumberForm, userRequestId);

            NewCustomerNumberDto newCustomerNumberDto = mapNewCustomerNumberRequestDto(createNewCustomerNumberForm,
                    userRequestDto, userRequestId);

            newCustomerNumberDao.insertNewCustomerNumberRequest(newCustomerNumberDto);

            Integer newCustomerNumberRequestId = newCustomerNumberDao.getNewCustomerNumberRequestById(userRequestId)
                    .getCustomerNumberRequestId();

            if (createNewCustomerNumberForm.getUserType().startsWith(UserType.REGISTERED_ATTORNEYS.getName())) {
                List<CustomerPractitionerDto> custPracDtoList = saveCustomerPractitionerNumbers(
                        createNewCustomerNumberForm.getCustomerPractitionerInfolist(), newCustomerNumberRequestId);

                if (!custPracDtoList.isEmpty()) {
                    customerPractitionerDao.insertCustomerPractitionerList(custPracDtoList);
                }
            }

            createNewCustomerNumberForm.setPairId(userRequestId.toString());
            createNewCustomerNumberForm.setTimeStamp(userRequestDto.getCreateTimeStamp());
            createNewCustomerNumberForm.setLastModifiedTimeStamp(userRequestDto.getLastModifiedTimeStamp());

        } catch (Exception e) {
            throw new PairAdminDatabaseException(e);
        }
        return createNewCustomerNumberForm;
    }

    private void saveUserSignatures(CreateNewCustomerNumberForm createNewCustomerNumberForm, Integer userRequestId)
            throws Exception {
        // Insert the Name/Signature list (in this case only one) in the
        // UserSignature table.
        List<UserSignatureDto> userSignatureDtoList = new ArrayList<UserSignatureDto>();

        UserSignatureDto userSignatureDto = new UserSignatureDto();

        userSignatureDto.setFkUserRequestId(Integer.toString(userRequestId));
        userSignatureDto.setUserSignatureTx(createNewCustomerNumberForm.getPocSignature());
        userSignatureDto.setUserSignatureNmTx(createNewCustomerNumberForm.getPocFiledBy());

        userSignatureDtoList.add(userSignatureDto);

        userSignatureDao.insertUserSignatureList(userSignatureDtoList);
    }

    private List<CustomerPractitionerDto> saveCustomerPractitionerNumbers(
            List<CustomerPractitionerInfo> customerPractitionerInfolist, Integer newCustomerNumberRequestId) throws Exception {
        List<CustomerPractitionerDto> customerPractitionerDtoList = new ArrayList<CustomerPractitionerDto>();

        for (CustomerPractitionerInfo customerPractitionerInfo : customerPractitionerInfolist) {
            if (!customerPractitionerInfo.getRegistrationNumber().isEmpty()) {

                CustomerPractitionerDto customerPractitionerDto = new CustomerPractitionerDto();
                customerPractitionerDto.setFk_customer_number_request_id(newCustomerNumberRequestId);

                customerPractitionerDto.setRegistration_no(customerPractitionerInfo.getRegistrationNumber());
                customerPractitionerDto.setFamily_nm(customerPractitionerInfo.getFamilyName());
                customerPractitionerDto.setGiven_nm(customerPractitionerInfo.getGivenName());
                customerPractitionerDto.setMiddle_nm(customerPractitionerInfo.getMiddleName());
                customerPractitionerDto.setName_suffix(customerPractitionerInfo.getNameSuffix());
                customerPractitionerDto.setAssociated_purm_reg_no_in(customerPractitionerInfo.getAssociatedToPurm());
                customerPractitionerDtoList.add(customerPractitionerDto);
            }
        }

        return customerPractitionerDtoList;
    }

    private NewCustomerNumberDto mapNewCustomerNumberRequestDto(CreateNewCustomerNumberForm createNewCustomerNumberForm,
            UserRequestDto userRequestDto, Integer userRequestId) {
        NewCustomerNumberDto newCustomerNumberDto = new NewCustomerNumberDto();

        newCustomerNumberDto.setFKeyUserRequestId(userRequestId.toString());
        if (StringUtils.isNotBlank(createNewCustomerNumberForm.getCustomerNumber())
                && !("null".equalsIgnoreCase(createNewCustomerNumberForm.getCustomerNumber()))) {
            newCustomerNumberDto.setCustomerNumber(Integer.parseInt(createNewCustomerNumberForm.getCustomerNumber()));
        }

        newCustomerNumberDto.setTimeStamp(userRequestDto.getCreateTimeStamp());
        newCustomerNumberDto.setLastModifiedTimeStamp(userRequestDto.getLastModifiedTimeStamp());
        newCustomerNumberDto.setFirmorIndividualNameLine1(createNewCustomerNumberForm.getFirmorIndividualNameLine1());
        newCustomerNumberDto.setFirmorIndividualNameLine2(createNewCustomerNumberForm.getFirmorIndividualNameLine2());
        newCustomerNumberDto.setAddressLine1(createNewCustomerNumberForm.getAddressLine1());
        newCustomerNumberDto.setAddressLine2(createNewCustomerNumberForm.getAddressLine2());
        newCustomerNumberDto.setCity(createNewCustomerNumberForm.getCity());
        newCustomerNumberDto.setState(createNewCustomerNumberForm.getState());
        newCustomerNumberDto.setZip(createNewCustomerNumberForm.getZip());
        newCustomerNumberDto.setCountry(createNewCustomerNumberForm.getCountry());

        newCustomerNumberDto.setTelephone1(createNewCustomerNumberForm.getTelephone1());
        newCustomerNumberDto.setTelephone2(createNewCustomerNumberForm.getTelephone2());
        newCustomerNumberDto.setTelephone3(createNewCustomerNumberForm.getTelephone3());

        newCustomerNumberDto.setEMail1(createNewCustomerNumberForm.getEMail1());
        newCustomerNumberDto.setEMail2(createNewCustomerNumberForm.getEMail2());
        newCustomerNumberDto.setEMail3(createNewCustomerNumberForm.getEMail3());

        newCustomerNumberDto.setFax1(createNewCustomerNumberForm.getFax1());
        newCustomerNumberDto.setFax2(createNewCustomerNumberForm.getFax2());

        newCustomerNumberDto.setIsAssociateMyPNumber(createNewCustomerNumberForm.getIsAssociateMyPractitionerNumber());
        newCustomerNumberDto.setOutgoingCorrespondence(createNewCustomerNumberForm.getOutgoingCorrespondence());

        return newCustomerNumberDto;
    }

    private UserRequestDto mapUserRequestDto(CreateNewCustomerNumberForm createNewCustomerNumberForm, String userId) {
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setSubmitterRegistrationNo(createNewCustomerNumberForm.getPocRegistrationNumber());
        userRequestDto.setContactFullName(createNewCustomerNumberForm.getPocName());
        userRequestDto.setContactTelephoneNoText(createNewCustomerNumberForm.getPocTelephone());
        userRequestDto.setContactEmailText(createNewCustomerNumberForm.getPocEmail());
        userRequestDto.setFkUserId(userId);

        // The Dn always not null when it can come to here
        String loginName = createNewCustomerNumberForm.getDn().substring(createNewCustomerNumberForm.getDn().indexOf("cn=") + 3,
                createNewCustomerNumberForm.getDn().indexOf(", ou="));

        userRequestDto.setCreateUserId(loginName);
        userRequestDto.setLastModifiedUserId(loginName);

        if (createNewCustomerNumberForm.getPairId() == null || createNewCustomerNumberForm.getPairId().isEmpty()) {
            userRequestDto.setCreateTimeStamp(new Date());
        } else {
        	userRequestDto.setCreateTimeStamp(new Date());
            userRequestDto.setUserRequestId(Long.valueOf(createNewCustomerNumberForm.getPairId()));
        }

        if (RequestStatus.SUBMITTED.getName().equalsIgnoreCase(createNewCustomerNumberForm.getRequestStatus())) {
            userRequestDto.setRequestDescriptionText("New Customer Number Successfully created");
        } /*else if ((RequestStatus.FAILED.getName().equalsIgnoreCase(createNewCustomerNumberForm.getRequestStatus()))
                && (StringUtils.isNotEmpty(createNewCustomerNumberForm.getServiceErrorMessage()))
        ) {
            userRequestDto.setRequestDescriptionText(createNewCustomerNumberForm.getServiceErrorMessage());
        }*/

        userRequestDto.setLastModifiedTimeStamp(new Date());
        userRequestDto.setTypeOfRequest(Constants.CUSTOMER_REQUEST);
        userRequestDto.setRequestStatusCount(createNewCustomerNumberForm.getRequestStatus());

        return userRequestDto;
    }


   /**
   * {@inheritDoc}
   */
     
    @Transactional
    @Override
    public List<ViewSaveCompleteForm> getCustomerRequestsforView(@SuppressWarnings("rawtypes") List requestStatusList,
            String requestDays, String requestType, String privatePAIRdn) throws PairAdminDatabaseException, Exception {

        String customerAddress = EMPTY_STRING;
        String customerAddressLine = EMPTY_STRING;
        String separator = "<br />";// System.getProperty( "line.separator" );

        List<ViewRequestDto> viewRequestDtoList = viewRequestDao.getCustomerRequestsforView(requestStatusList, requestDays,
                requestType, privatePAIRdn);

        List<ViewSaveCompleteForm> viewCustomerList = new ArrayList<ViewSaveCompleteForm>();

        for (ViewRequestDto viewReqDto : viewRequestDtoList) {

            ViewSaveCompleteForm viewForm = new ViewSaveCompleteForm();

            viewForm.setCustomerPairId(viewReqDto.getCustomerPairId());
            viewForm.setCustomerTs(viewReqDto.getCustomerTs());
            viewForm.setNewCustomerNumber(viewReqDto.getNewCustomerNumber());
            viewForm.setCustomerStatus(viewReqDto.getCustomerStatus());
            customerAddressLine = viewReqDto.getCustomerCity() + " " + viewReqDto.getCustomerState() + " "
                    + viewReqDto.getCustomerPostalCode();
            customerAddress = viewReqDto.getCustomerName() + separator + viewReqDto.getCustomerAddressLine() + separator
                    + customerAddressLine;
            viewForm.setCustomerAddress(customerAddress);

            viewCustomerList.add(viewForm);
        }
        return viewCustomerList;
    }




    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public CreateNewCustomerNumberForm updateNewCustomerNumber(CreateNewCustomerNumberForm createNewCustomerNumberForm, String dn)
            throws PairAdminDatabaseException {

        try {
            String userId = pairUserDnDao.getPairUserDnByDn(dn);

            UserRequestDto userRequestDto = mapUserRequestDto(createNewCustomerNumberForm, userId);

            userRequestDao.updateUserRequest(userRequestDto);

            long userRequestId = userRequestDto.getUserRequestId();

            Integer newCustomerNumberRequestId = newCustomerNumberDao.getNewCustomerNumberRequestById(userRequestId)
                    .getCustomerNumberRequestId();

            customerPractitionerDao.deleteCustomerPractitionerByRequestId(newCustomerNumberRequestId);

            if (createNewCustomerNumberForm.getUserType().startsWith(UserType.REGISTERED_ATTORNEYS.getName())) {
                List<CustomerPractitionerDto> custPracDtoList = saveCustomerPractitionerNumbers(
                        createNewCustomerNumberForm.getCustomerPractitionerInfolist(), newCustomerNumberRequestId);

                if (!custPracDtoList.isEmpty()) {
                    customerPractitionerDao.insertCustomerPractitionerList(custPracDtoList);
                }
            }

            // Before updating, first delete all the Name/Signature for the
            // given userRequestId.
            userSignatureDao.deleteUserSignatureByRequestId((long) userRequestId);

            saveUserSignatures(createNewCustomerNumberForm, (int) userRequestId);

            NewCustomerNumberDto newCustomerNumberDto = mapNewCustomerNumberRequestDto(createNewCustomerNumberForm,
                    userRequestDto, Integer.parseInt(createNewCustomerNumberForm.getPairId()));

            newCustomerNumberDao.updateNewCustomerNumberRequestById(newCustomerNumberDto);

            createNewCustomerNumberForm.setTimeStamp(userRequestDto.getCreateTimeStamp());
            createNewCustomerNumberForm.setLastModifiedTimeStamp(userRequestDto.getLastModifiedTimeStamp());
        } catch (PairAdminDatabaseException e) {

            throw e;
        } catch (Exception e) {
            throw new PairAdminDatabaseException(e);
        }
        return createNewCustomerNumberForm;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public CreateNewCustomerNumberForm getNewCustomerNumberRequest(Long userRequestId) throws Exception,
            PairAdminDatabaseException {

        CreateNewCustomerNumberForm createNewCustomerNumberForm = new CreateNewCustomerNumberForm();

        try {
            UserRequestDto userRequestDto = userRequestDao.getUserRequestById(userRequestId);
            NewCustomerNumberDto newCustomerNumberDto = newCustomerNumberDao.getNewCustomerNumberRequestById(userRequestId);

            List<UserSignatureDto> userSignatureDtoList = userSignatureDao.getAllUserSignatureById(userRequestId);

            Integer newCustomerNumberRequestId = newCustomerNumberDto.getCustomerNumberRequestId();

            List<CustomerPractitionerDto> customerPractitionerDtoList = customerPractitionerDao
                    .getAllCustomerPractitionerById(newCustomerNumberRequestId);

            createNewCustomerNumberForm.setCustomerNumber(String.valueOf(newCustomerNumberDto.getCustomerNumber()));
            createNewCustomerNumberForm.setPairId(Long.toString(userRequestDto.getUserRequestId()));

            createNewCustomerNumberForm.setLastModifiedTimeStamp(userRequestDto.getLastModifiedTimeStamp());
            createNewCustomerNumberForm.setTimeStamp(userRequestDto.getCreateTimeStamp());
            createNewCustomerNumberForm.setRequestStatus(userRequestDto.getRequestStatusCount());

            createNewCustomerNumberForm.setFirmorIndividualNameLine1(newCustomerNumberDto.getFirmorIndividualNameLine1());
            createNewCustomerNumberForm.setFirmorIndividualNameLine2(newCustomerNumberDto.getFirmorIndividualNameLine2());
            createNewCustomerNumberForm.setAddressLine1(newCustomerNumberDto.getAddressLine1());
            createNewCustomerNumberForm.setAddressLine2(newCustomerNumberDto.getAddressLine2());
            createNewCustomerNumberForm.setCity(newCustomerNumberDto.getCity());
            createNewCustomerNumberForm.setState(newCustomerNumberDto.getState());
            createNewCustomerNumberForm.setZip(newCustomerNumberDto.getZip());
            createNewCustomerNumberForm.setCountry(newCustomerNumberDto.getCountry());

            createNewCustomerNumberForm.setTelephone1(newCustomerNumberDto.getTelephone1());
            createNewCustomerNumberForm.setTelephone2(newCustomerNumberDto.getTelephone2());
            createNewCustomerNumberForm.setTelephone3(newCustomerNumberDto.getTelephone3());

            createNewCustomerNumberForm.setEMail1(newCustomerNumberDto.getEMail1());
            createNewCustomerNumberForm.setEMail2(newCustomerNumberDto.getEMail2());
            createNewCustomerNumberForm.setEMail3(newCustomerNumberDto.getEMail3());

            createNewCustomerNumberForm.setFax1(newCustomerNumberDto.getFax1());
            createNewCustomerNumberForm.setFax2(newCustomerNumberDto.getFax2());

            createNewCustomerNumberForm.setIsAssociateMyPractitionerNumber(newCustomerNumberDto
                    .getIsAssociateMyPNumber());
            createNewCustomerNumberForm.setOutgoingCorrespondence(newCustomerNumberDto.getOutgoingCorrespondence());

            createNewCustomerNumberForm.setPocRegistrationNumber(userRequestDto.getSubmitterRegistrationNo());
            createNewCustomerNumberForm.setPocName(userRequestDto.getContactFullName());
            createNewCustomerNumberForm.setPocTelephone(userRequestDto.getContactTelephoneNoText());
            createNewCustomerNumberForm.setPocEmail(userRequestDto.getContactEmailText());

            List<CustomerPractitionerInfo> customerPractitionerInfoArray = new ArrayList<CustomerPractitionerInfo>();
            for (CustomerPractitionerDto customerPractitionerDto : customerPractitionerDtoList) {

                CustomerPractitionerInfo customerPractitionerInfo = new CustomerPractitionerInfo();

                customerPractitionerInfo.setRegistrationNumber(customerPractitionerDto.getRegistration_no());
                customerPractitionerInfo.setFamilyName(customerPractitionerDto.getFamily_nm());
                customerPractitionerInfo.setGivenName(customerPractitionerDto.getGiven_nm());
                customerPractitionerInfo.setMiddleName(customerPractitionerDto.getMiddle_nm());
                customerPractitionerInfo.setNameSuffix(customerPractitionerDto.getName_suffix());

                customerPractitionerInfoArray.add(customerPractitionerInfo);
            }

            createNewCustomerNumberForm.setCustomerPractitionerInfolist(customerPractitionerInfoArray);

            List<CustomerPractitionerInfo> existingCustomerPractitionerInfoArray = new ArrayList<CustomerPractitionerInfo>();
            for (CustomerPractitionerDto customerPractitionerDto : customerPractitionerDtoList) {

                if ((customerPractitionerDto.getAssociated_purm_reg_no_in() != null)
                        && "Y".compareToIgnoreCase(customerPractitionerDto.getAssociated_purm_reg_no_in().trim()) == 0) {
                    CustomerPractitionerInfo customerPractitionerInfo = new CustomerPractitionerInfo();

                    customerPractitionerInfo.setRegistrationNumber(customerPractitionerDto.getRegistration_no());
                    customerPractitionerInfo.setFamilyName(customerPractitionerDto.getFamily_nm());
                    customerPractitionerInfo.setGivenName(customerPractitionerDto.getGiven_nm());
                    customerPractitionerInfo.setMiddleName(customerPractitionerDto.getMiddle_nm());
                    customerPractitionerInfo.setNameSuffix(customerPractitionerDto.getName_suffix());
                    customerPractitionerInfo.setAssociatedToPurm(customerPractitionerDto.getAssociated_purm_reg_no_in());

                    existingCustomerPractitionerInfoArray.add(customerPractitionerInfo);
                }
            }

            if (!existingCustomerPractitionerInfoArray.isEmpty()) {
                createNewCustomerNumberForm.setExistingCustomerPractitionerInfolist(existingCustomerPractitionerInfoArray);
            }

            createNewCustomerNumberForm.setPocFiledBy(userSignatureDtoList.get(0).getUserSignatureNmTx());
            createNewCustomerNumberForm.setPocSignature(userSignatureDtoList.get(0).getUserSignatureTx());

        } catch (PairAdminDatabaseException e) {
            throw new PairAdminDatabaseException(e);

        } catch (Exception e) {
            throw new Exception(e);
        }
        return createNewCustomerNumberForm;
    }
    
    
    
    
    /**
     * {@inheritDoc}
     */
    @Transactional(rollbackFor = PairAdminDatabaseException.class, propagation = Propagation.REQUIRED)
    @Override
    public CreateNewCustomerNumberForm createNewCustomerNumber(CreateNewCustomerNumberForm createNewCustomerNumberForm)
            throws PairAdminDatabaseException {

        try {
        	System.out.println("inside createNewCustomerNumber service impl");
 
        	String customerNumber=createNewCustomerNumberForm.getCustomerNumber();
        	  createNewCustomerNumberForm.setRequestStatus(RequestStatus.SUBMITTED.getName());
               makePairDbEntry(createNewCustomerNumberForm);

                if (createNewCustomerNumberForm.getUserType().startsWith(UserType.REGISTERED_ATTORNEYS.getName())) {
                    insertPracGivenPrivatePairAccess(createNewCustomerNumberForm, customerNumber);
                } else {
                    PairUserDnDto pairUserDnDto = pairUserDnDao.getPairUserDtoByDn(createNewCustomerNumberForm.getDn());
                    addPairUserCn(getNormalizedCustomerNumber(customerNumber), pairUserDnDto);
                  
                }
              
                PairCustomerOptDto pairCustomerOptDto = new PairCustomerOptDto();
                pairCustomerOptDto.setCustomerNumber(createNewCustomerNumberForm.getCustomerNumber());
                pairCustomerOptDto.setOptInIndicator(createNewCustomerNumberForm.getOutgoingCorrespondence().toUpperCase());
                pairCustomerOptDto.setLastModifiedTs(createNewCustomerNumberForm.getLastModifiedTimeStamp());
                pairCustomerOptDto.setDistinguishedName(createNewCustomerNumberForm.getDn());
                pairCustomerOptDto.setPostCardMailOptDate(createNewCustomerNumberForm.getTimeStamp());
                // pairCustomerOptDto.setPostCardMailOptInTransaction(postCardMailOptInTransaction);
                pairCustomerOptDao.insertPairCustomerOpt(pairCustomerOptDto);

            

        } catch (Exception e) {
            /*
             * createNewCustomerNumberForm.setRequestStatus(RequestStatus.FAILED.
             * getName());
             * createNewCustomerNumberForm.setServiceErrorMessage(e.getMessage
             * ());
             * 
             * updateRequestStatus(createNewCustomerNumberForm);
             */
            if (e instanceof PairAdminDatabaseException) {
                throw (PairAdminDatabaseException) e;
            }

            throw new PairAdminDatabaseException(e);
        }

        return createNewCustomerNumberForm;
    }
    
    private void makePairDbEntry(CreateNewCustomerNumberForm createNewCustomerNumberForm) throws PairAdminDatabaseException {
System.out.println("make DB entry");
System.out.println("pairID"+createNewCustomerNumberForm.getPairId());
        if (StringUtils.isEmpty(createNewCustomerNumberForm.getPairId())) {
        	System.out.println("make DB entry - inside save");
        	System.out.println("customer Number"+ createNewCustomerNumberForm.getCustomerNumber());
            createNewCustomerNumberForm.setRequestStatus(RequestStatus.SAVED.getName());
            saveNewCustomerNumber(createNewCustomerNumberForm, createNewCustomerNumberForm.getDn());
        } else {
        	System.out.println("make DB entry - inside update");
            updateNewCustomerNumber(createNewCustomerNumberForm, createNewCustomerNumberForm.getDn());
        }

    }


    
    
    private void insertPracGivenPrivatePairAccess(CreateNewCustomerNumberForm createNewCustomerNumberForm,String customerNumber) throws Exception {

        if (createNewCustomerNumberForm.getCustomerPractitionerInfolist().isEmpty()) {
            return;
        }

        String normalizedCustNum = getNormalizedCustomerNumber(customerNumber);
        

        Iterator<CustomerPractitionerInfo> custPracIter = createNewCustomerNumberForm.getCustomerPractitionerInfolist()
                .iterator();
        List<String> regNos = new ArrayList<String>();
        while (custPracIter.hasNext()) {
            regNos.add("R0" + custPracIter.next().getRegistrationNumber().trim());
        }

        List<PairUserDnDto> pairUserDnDtoList = pairUserDnDao.hasPairUserDn(regNos);

        if (pairUserDnDtoList != null) {
            Iterator<PairUserDnDto> pairUserDnDtoIter = pairUserDnDtoList.iterator();
            createNewCustomerNumberForm.setExistingCustomerPractitionerInfolist(new ArrayList<CustomerPractitionerInfo>());

            while (pairUserDnDtoIter.hasNext()) {

                PairUserDnDto pairUserDnDto = addPairUserCn(normalizedCustNum, pairUserDnDtoIter.next());

                CustomerPractitionerInfo customerPractitionerInfo = new CustomerPractitionerInfo();
                customerPractitionerInfo.setRegistrationNumber(pairUserDnDto.getPairUserDnId().substring(2));
                int index = createNewCustomerNumberForm.getCustomerPractitionerInfolist().indexOf(customerPractitionerInfo);
                if (index >= 0) {

                    CustomerPractitionerInfo customerPractitionerInfoFromPALM = createNewCustomerNumberForm
                            .getCustomerPractitionerInfolist().get(index);
                    customerPractitionerInfo.setGivenName(customerPractitionerInfoFromPALM.getGivenName());
                    customerPractitionerInfo.setMiddleName(customerPractitionerInfoFromPALM.getMiddleName());
                    customerPractitionerInfo.setFamilyName(customerPractitionerInfoFromPALM.getFamilyName());
                    customerPractitionerInfo.setNameSuffix(customerPractitionerInfoFromPALM.getNameSuffix());

                    // customerPractitionerInfo.setNameField(customerPractitionerInfoFromPALM.getNameField());
                    customerPractitionerInfo.setFullDisplayName();
                    setIsAssociatedWithMyPractitionerNumber(createNewCustomerNumberForm, customerPractitionerInfo);

                    customerPractitionerInfo.setAssociatedToPurm("Y");
                } else {
                    // customerPractitionerInfo.setNameField(pairUserDnDto.getCommonName());
                }

                createNewCustomerNumberForm.getExistingCustomerPractitionerInfolist().add(customerPractitionerInfo);
            }

            Collections.sort(createNewCustomerNumberForm.getExistingCustomerPractitionerInfolist(),
                    new CustomerPractitionerComparator());

            Integer newCustomerNumberRequestId = newCustomerNumberDao.getNewCustomerNumberRequestById(
                    Long.parseLong(createNewCustomerNumberForm.getPairId())).getCustomerNumberRequestId();

            List<CustomerPractitionerDto> custPracDtoList = saveCustomerPractitionerNumbers(
                    createNewCustomerNumberForm.getExistingCustomerPractitionerInfolist(), newCustomerNumberRequestId);

            if (!custPracDtoList.isEmpty()) {
                customerPractitionerDao.updateCustomerPractitionerListByRequestId(custPracDtoList, newCustomerNumberRequestId);
            }
        }
    }
    
    
    private void setIsAssociatedWithMyPractitionerNumber(CreateNewCustomerNumberForm form,
            CustomerPractitionerInfo customerPractitionerInfo) {
        if ("Y".equalsIgnoreCase(form.getIsAssociateMyPractitionerNumber())
                && customerPractitionerInfo.getRegistrationNumber().trim()
                        .equalsIgnoreCase(form.getPocRegistrationNumber().trim())) {
            customerPractitionerInfo.setAssociatedToPairUserDn(true);
        } else {
            customerPractitionerInfo.setAssociatedToPairUserDn(false);
        }
    }
    
    private String getNormalizedCustomerNumber(String custNo) {

        return "55" + StringUtils.leftPad(custNo, 6, '0');
    }
    
    private PairUserDnDto addPairUserCn(String normalizedCustNum, PairUserDnDto pairUserDnDto) throws Exception {
        PairUserCnDto pairUserCnDto = new PairUserCnDto();

        pairUserCnDto.setCustNum(normalizedCustNum);
        pairUserCnDto.setUserId(pairUserDnDto.getUserId());
        pairUserCnDto.setPairUserDnId(pairUserDnDto.getPairUserDnId());
        pairUserCnDto.setSysAdminId(pairUserDnDto.getSysAdminId());
        pairUserCnDto.setInsDate(new java.sql.Date(System.currentTimeMillis()));

        pairUserCnDao.insertPairUserCn(pairUserCnDto);
        return pairUserDnDto;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteCreateNewCustomerNumberRequest(String pairId) throws PairAdminDatabaseException, Exception {
        UserRequestDto userRequestDto = userRequestDao.getUserRequestById(Long.valueOf(pairId));
        userRequestDto.setDeleteIndicator("Y");
        userRequestDto.setRequestStatusCount("Deleted");
        userRequestDto.setLastModifiedTimeStamp(new Date());
        userRequestDao.updateDeleteIndicatorByUserRequestId(userRequestDto);
    }
    
    
    @Transactional
    @Override
    public void updateRequestStatus(CreateNewCustomerNumberForm createNewCustomerNumberForm) throws PairAdminDatabaseException {
        UserRequestDto userRequestDto = new UserRequestDto();

        userRequestDto.setUserRequestId(Long.valueOf(createNewCustomerNumberForm.getPairId()));
        userRequestDto.setRequestDescriptionText(createNewCustomerNumberForm.getServiceErrorMessage());
        userRequestDto.setLastModifiedTimeStamp(new Date());
        userRequestDto.setRequestStatusCount(createNewCustomerNumberForm.getRequestStatus());

        try {
            userRequestDao.updateRequestStatusByUserRequestId(userRequestDto);
        } catch (Exception e1) {
            throw new PairAdminDatabaseException("Failed to update Create Customer Number Request Status", e1.getCause());
        }
    }

	
    
   
    
    
   
    

}
