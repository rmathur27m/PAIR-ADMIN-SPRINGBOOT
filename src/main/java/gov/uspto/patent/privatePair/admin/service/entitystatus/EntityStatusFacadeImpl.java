package gov.uspto.patent.privatePair.admin.service.entitystatus;


import gov.uspto.patent.privatePair.common.InventorType;
import gov.uspto.patent.privatePair.common.EntityStatus;
import gov.uspto.patent.privatePair.admin.dao.EntityChangeRequestDao;
import gov.uspto.patent.privatePair.admin.dao.PairUserDnDao;
import gov.uspto.patent.privatePair.admin.dao.UserRequestDao;
import gov.uspto.patent.privatePair.admin.dao.UserSignatureDao;
import gov.uspto.patent.privatePair.admin.dao.ViewRequestDao;
import gov.uspto.patent.privatePair.admin.domain.Constants;
import gov.uspto.patent.privatePair.admin.domain.EditEntityStatus;
import gov.uspto.patent.privatePair.admin.domain.UserSignature;
import gov.uspto.patent.privatePair.admin.domain.ViewSaveCompleteForm;
import gov.uspto.patent.privatePair.admin.dto.EntityChangeRequestDto;
import gov.uspto.patent.privatePair.admin.dto.PairUserDnDto;
import gov.uspto.patent.privatePair.admin.dto.UserRequestDto;
import gov.uspto.patent.privatePair.admin.dto.UserSignatureDto;
import gov.uspto.patent.privatePair.admin.dto.ViewRequestDto;
import gov.uspto.patent.privatePair.admin.service.common.RequestStatus;
import gov.uspto.patent.privatePair.common.UserType;
import gov.uspto.patent.privatePair.exceptionhandlers.PairAdminDatabaseException;
import gov.uspto.patent.privatePair.exceptionhandlers.UserNotFoundException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



/**
 * Service layer interface to PALM Web Services.
 * 
 */
@Component
//@Service
public class EntityStatusFacadeImpl implements EntityStatusServices {

	 @Autowired
	 EntityChangeRequestDao entityChangeRequestDao;

	 @Autowired
	 UserRequestDao userRequestDao;

	 @Autowired
	 PairUserDnDao pairUserDnDao;

	 @Autowired
	 UserSignatureDao userSignatureDao;

     @Autowired
     ViewRequestDao viewRequestDao;

    
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public EditEntityStatus saveEntityStatus(EditEntityStatus editEntityStatusForm, String dn)
            throws PairAdminDatabaseException, UserNotFoundException, Exception {

        // Gets the userId from the pairUserDn table.
        PairUserDnDto pairUserDnDto = pairUserDnDao.getPairUserDtoByDn(dn);

        editEntityStatusForm.setStatus(RequestStatus.SAVED.getName());
        // Maps the form object to the userRequestDto object.
        UserRequestDto userRequestDto = mapUserRequestDto(editEntityStatusForm, pairUserDnDto.getUserId(),
                pairUserDnDto.getCommonName());
       
        // Insert the above mapped userRequestDto object in the userRequest
        // table.
        Long userRequestId = userRequestDao.insertUserRequest(userRequestDto);

        // Insert the Name/Signature list in the UserSignature table.
        if(editEntityStatusForm.getSignatureNameArray() != null){
        	if(editEntityStatusForm.getSignatureNameArray().size() > 0) {
		        List<UserSignatureDto> userSignatureDtoList = new ArrayList<UserSignatureDto>();
		
		        for (UserSignature userSignature : editEntityStatusForm.getSignatureNameArray()) {
		            UserSignatureDto userSignatureDto = new UserSignatureDto();
		            userSignatureDto.setFkUserRequestId(Long.toString(userRequestId));
		            userSignatureDto.setUserSignatureNmTx(userSignature.getName());
		            userSignatureDto.setUserSignatureTx(userSignature.getSignature());
		            userSignatureDtoList.add(userSignatureDto);
		        }
		
		        userSignatureDao.insertUserSignatureList(userSignatureDtoList);
        	}
        }

        String saveUpdateFlag = "Save Entity";

        // Map the form object to the entityChangeRequestDto object.
        EntityChangeRequestDto entityChangeRequestDto = mapEntityChangeRequestDto(editEntityStatusForm, userRequestDto,
                userRequestId, pairUserDnDto, saveUpdateFlag);

        // Insert the entity request in the EntityChangeRequest table.
        entityChangeRequestDao.insertEntityChangeRequest(entityChangeRequestDto);

        // Set the below form fields after the above inserts are successfully
        // done.
        editEntityStatusForm.setPairId(userRequestId.toString());
        editEntityStatusForm.setCreatedTs(userRequestDto.getCreateTimeStamp());
        editEntityStatusForm.setLastModifiedTs(userRequestDto.getLastModifiedTimeStamp());
        editEntityStatusForm.setStatus(userRequestDto.getRequestStatusCount());
System.out.println(editEntityStatusForm.getStatus());
        return editEntityStatusForm;
    }
    
    
    /**
     * Maps the editEnityStatusForm object to the UserRequestDto object.
     * 
     * @param editEntityStatusForm
     * @param userId
     * @return {@link UserRequestDto}
     */
    private UserRequestDto mapUserRequestDto(EditEntityStatus editEntityStatusForm, String userId, String commonName) {
        UserRequestDto userRequestDto = new UserRequestDto();
        if (editEntityStatusForm.getPairId() != null) {
            userRequestDto.setUserRequestId(Long.valueOf(editEntityStatusForm.getPairId()));
        }
        userRequestDto.setSubmitterRegistrationNo(editEntityStatusForm.getRegNumber());
        userRequestDto.setContactFullName(editEntityStatusForm.getPocName());
        userRequestDto.setContactTelephoneNoText(editEntityStatusForm.getPhoneNumber());
        userRequestDto.setContactEmailText(editEntityStatusForm.getEmailAddress());
        userRequestDto.setFkUserId(userId);
        if (editEntityStatusForm.getPairId() == null) {
            userRequestDto.setCreateTimeStamp(new Date());
        } else {
            userRequestDto.setCreateTimeStamp(editEntityStatusForm.getCreatedTs());
        }

        userRequestDto.setLastModifiedTimeStamp(new Date());
        // TODO Create an Enum constant for the type of Requests and request
        // status.
        userRequestDto.setTypeOfRequest(Constants.ENTITY_STATUS);
        userRequestDto.setRequestStatusCount(editEntityStatusForm.getStatus());
        userRequestDto.setCreateUserId(commonName);
        userRequestDto.setLastModifiedUserId(commonName);
        System.out.println(editEntityStatusForm.getUserType());
   
        if (editEntityStatusForm.getUserType().equalsIgnoreCase(UserType.REGISTERED_ATTORNEYS.getName())) {
            userRequestDto.setRegisteredAttorneyType(editEntityStatusForm.getAttorneyTypeOption());
        } else if (editEntityStatusForm.getUserType().equalsIgnoreCase(UserType.INDEPENDENT_INVENTOR.getName())) {
        	if(editEntityStatusForm.getInventorTypeOption() != null) {
        		userRequestDto.setIndependentInventorType(InventorType.findByDisplayValue(
                        editEntityStatusForm.getInventorTypeOption()).getDbValue());
        	} 
        }
        System.out.println(userRequestDto.getIndependentInventorType());
        return userRequestDto;
    }
    
    
    /**
     * Maps the editEnityStatusForm object to the EntityChangeRequestDto object.
     * 
     * @param editEntityStatusForm
     * @param userId
     * @return {@link EntityChangeRequestDto}
     */
    private EntityChangeRequestDto mapEntityChangeRequestDto(EditEntityStatus editEntityStatusForm,
            UserRequestDto userRequestDto, Long userRequestId, PairUserDnDto pairUserDnDto, String saveUpdateFlag) {
        EntityChangeRequestDto entityChangeRequestDto = new EntityChangeRequestDto();

        entityChangeRequestDto.setFKeyUserRequestId(userRequestId.toString());
        entityChangeRequestDto.setCurBusEntityCode(editEntityStatusForm.getCurrentBusinessEntityCode());
        entityChangeRequestDto.setCreateTimeStamp(userRequestDto.getCreateTimeStamp());
        entityChangeRequestDto.setLastModifiedTimeStamp(userRequestDto.getLastModifiedTimeStamp());
        entityChangeRequestDto.setApplication_id(editEntityStatusForm.getApplicationNumber());
        entityChangeRequestDto.setAttorneyDocketNumber(editEntityStatusForm.getAttorneyDocketNumber());
        entityChangeRequestDto.setTitle(editEntityStatusForm.getTitle());
        entityChangeRequestDto.setPropBusEntityCode(editEntityStatusForm.getStatusType());
        entityChangeRequestDto.setCustomerNumber(Integer.parseInt((editEntityStatusForm.getCustomerNumber())));
        // If Save request then set the create user id.
        if (saveUpdateFlag.equalsIgnoreCase("Save Entity")) {
            entityChangeRequestDto.setCreateUserId(pairUserDnDto.getCommonName());
        }

        entityChangeRequestDto.setLastModifiedUserId(pairUserDnDto.getCommonName());

        if (editEntityStatusForm.getStatusType() != null) {
            if (editEntityStatusForm.getStatusType().equalsIgnoreCase(EntityStatus.MICRO.getName())) {
                entityChangeRequestDto.setMicroEntityTypeCode(editEntityStatusForm.getMicroEntityStatusOption());
                entityChangeRequestDto.setMicroEntityGrossCert(Boolean.toString(editEntityStatusForm.isGrossIncomeBasisCert()));
                entityChangeRequestDto.setMicroEntityInstCert(editEntityStatusForm.getInstitutionOfHigherEdBasisCert());
            }
        }

        return entityChangeRequestDto;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public EditEntityStatus updateEntityStatus(EditEntityStatus editEntityStatusForm, String dn)
            throws PairAdminDatabaseException, UserNotFoundException, Exception {

        // Get the userId for the given dn string.
        PairUserDnDto pairUserDnDto = pairUserDnDao.getPairUserDtoByDn(dn);

        // Map the form object to the userRequestDto object.
        UserRequestDto userRequestDto = mapUserRequestDto(editEntityStatusForm, pairUserDnDto.getUserId(),
                pairUserDnDto.getCommonName());

        // Before updating, first delete all the Name/Signature for the given
        // userRequestId.
        userSignatureDao.deleteUserSignatureByRequestId(userRequestDto.getUserRequestId());
        userRequestDao.updateUserRequest(userRequestDto);

        // After deletion, now perform the insertion of the Name/Signature for
        // the given userRequestId.
        if(editEntityStatusForm.getSignatureNameArray() != null){
        	if(editEntityStatusForm.getSignatureNameArray().size() > 0) {
        		List<UserSignatureDto> userSignatureDtoList = new ArrayList<UserSignatureDto>();

                for (UserSignature userSignature : editEntityStatusForm.getSignatureNameArray()) {
                    UserSignatureDto userSignatureDto = new UserSignatureDto();
                    userSignatureDto.setFkUserRequestId(String.valueOf(userRequestDto.getUserRequestId()));
                    userSignatureDto.setUserSignatureNmTx(userSignature.getName());
                    userSignatureDto.setUserSignatureTx(userSignature.getSignature());
                    userSignatureDtoList.add(userSignatureDto);
                }
                userSignatureDao.insertUserSignatureList(userSignatureDtoList);
        	}
        }
        
        String saveUpdateFlag = "Update Entity";

        // Map the form object to the entityChangeRequestDto object.
        EntityChangeRequestDto entityChangeRequestDto = mapEntityChangeRequestDto(editEntityStatusForm, userRequestDto,
                Long.parseLong(editEntityStatusForm.getPairId()), pairUserDnDto, saveUpdateFlag);

        // Update the entityChangeRequest table.
        entityChangeRequestDao.updateEntityChangeRequestById(entityChangeRequestDto);

        editEntityStatusForm.setPairId(editEntityStatusForm.getPairId());
        editEntityStatusForm.setCreatedTs(userRequestDto.getCreateTimeStamp());
        editEntityStatusForm.setLastModifiedTs(userRequestDto.getLastModifiedTimeStamp());
        editEntityStatusForm.setStatus(userRequestDto.getRequestStatusCount());

        return editEntityStatusForm;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public EditEntityStatus getEntityChangeRequest(Long userRequestId) throws PairAdminDatabaseException, Exception {

        UserRequestDto userRequestDto = userRequestDao.getUserRequestById(userRequestId);
        EntityChangeRequestDto entityChangeRequestDto = entityChangeRequestDao.getEntityChangeRequestById(userRequestId);
        System.out.println(entityChangeRequestDto);
        List<UserSignatureDto> userSignatureDtoList = userSignatureDao.getAllUserSignatureById(userRequestId);
        System.out.println(userSignatureDtoList.get(0));
        List<UserSignature> signatureArray = new ArrayList<UserSignature>();

        EditEntityStatus editEntityStatusForm = new EditEntityStatus();
        System.out.println("1");
        if (userRequestDto.getRegisteredAttorneyType() != null) {
            editEntityStatusForm.setUserType(UserType.REGISTERED_ATTORNEYS.getName());
        }
        System.out.println("2");
        if (userRequestDto.getIndependentInventorType() != null) {
            editEntityStatusForm.setUserType(UserType.INDEPENDENT_INVENTOR.getName());
        }
        System.out.println("3");
        editEntityStatusForm.setPairId(Long.toString(userRequestDto.getUserRequestId()));
        editEntityStatusForm.setCreatedTs(userRequestDto.getCreateTimeStamp());
        editEntityStatusForm.setLastModifiedTs(userRequestDto.getLastModifiedTimeStamp());
        editEntityStatusForm.setStatus(userRequestDto.getRequestStatusCount());
        System.out.println("4");
        editEntityStatusForm.setRegNumber(userRequestDto.getSubmitterRegistrationNo());
        editEntityStatusForm.setPocName(userRequestDto.getContactFullName());
        editEntityStatusForm.setPhoneNumber(userRequestDto.getContactTelephoneNoText());
        editEntityStatusForm.setEmailAddress(userRequestDto.getContactEmailText());

        editEntityStatusForm.setAttorneyTypeOption(userRequestDto.getRegisteredAttorneyType());
        System.out.println("5"+userRequestDto.getIndependentInventorType());
        
        if (userRequestDto.getIndependentInventorType() != null) {
            editEntityStatusForm.setInventorTypeOption(InventorType.findByDbValue(userRequestDto.getIndependentInventorType())
                    .getDisplayValue());
        }
        System.out.println("6");
        editEntityStatusForm.setApplicationNumber(entityChangeRequestDto.getApplication_id());
        editEntityStatusForm.setAttorneyDocketNumber(entityChangeRequestDto.getAttorneyDocketNumber());
        if (entityChangeRequestDto.getCustomerNumber() != null) {
            editEntityStatusForm.setCustomerNumber(Integer.toString(entityChangeRequestDto.getCustomerNumber()));
        }
        editEntityStatusForm.setCurrentBusinessEntityCode(entityChangeRequestDto.getCurBusEntityCode());
        editEntityStatusForm.setTitle(entityChangeRequestDto.getTitle());
        System.out.println("7");
        editEntityStatusForm.setStatusType(entityChangeRequestDto.getPropBusEntityCode());
        editEntityStatusForm.setMicroEntityStatusOption(entityChangeRequestDto.getMicroEntityTypeCode());
        System.out.println(editEntityStatusForm.getStatusType());
        System.out.println(editEntityStatusForm.getCurrentBusinessEntityCode());
        
        if(null != editEntityStatusForm.getStatusType()) {
        	 if (editEntityStatusForm.getStatusType().equalsIgnoreCase(EntityStatus.MICRO.getName())) {
        		 if(null != entityChangeRequestDto.getMicroEntityGrossCert()) {
        	 editEntityStatusForm.setGrossIncomeBasisCert(Boolean.valueOf(entityChangeRequestDto.getMicroEntityGrossCert().trim()));
        		 }
        		 if(null != entityChangeRequestDto.getMicroEntityInstCert()) {
        	 editEntityStatusForm.setInstitutionOfHigherEdBasisCert(entityChangeRequestDto.getMicroEntityInstCert().trim());
        		 }
        	 }
        }
        if(null != editEntityStatusForm.getCurrentBusinessEntityCode()) {
        if (editEntityStatusForm.getCurrentBusinessEntityCode().equalsIgnoreCase(EntityStatus.MICRO.getName())) {
        	 if(null != entityChangeRequestDto.getMicroEntityGrossCert()) {
            editEntityStatusForm
                    .setGrossIncomeBasisCert(Boolean.valueOf(entityChangeRequestDto.getMicroEntityGrossCert().trim()));
        	 }
        	 if(null != entityChangeRequestDto.getMicroEntityInstCert()) {
            editEntityStatusForm.setInstitutionOfHigherEdBasisCert(entityChangeRequestDto.getMicroEntityInstCert().trim());
        	 }
        }
        }
    
        System.out.println("7.2");
        System.out.println("8");
        for (UserSignatureDto userSignatureDto : userSignatureDtoList) {

            UserSignature userSignature = new UserSignature();

            userSignature.setName(userSignatureDto.getUserSignatureNmTx());
            userSignature.setSignature(userSignatureDto.getUserSignatureTx());
            signatureArray.add(userSignature);
        }
        System.out.println("9");
        editEntityStatusForm.setSignatureNameArray(signatureArray);
        System.out.println("10");
        
        return editEntityStatusForm;
    }
   
    /**
     * {@inheritDoc}
     * 
     */
    @Transactional
    @Override
    public List<ViewSaveCompleteForm> getEntityRequestsforView(@SuppressWarnings("rawtypes") List requestStatusList,
            String requestDays, String requestType, String privatePAIRdn) throws PairAdminDatabaseException, Exception {

        List<ViewRequestDto> viewRequestDtoList = viewRequestDao.getEntityRequestsforView(requestStatusList, requestDays,
                requestType, privatePAIRdn);

        List<ViewSaveCompleteForm> viewEntityList = new ArrayList<ViewSaveCompleteForm>();

        for (ViewRequestDto viewReqDto : viewRequestDtoList) {

            ViewSaveCompleteForm entityViewForm = new ViewSaveCompleteForm();

            entityViewForm.setPairId(Long.toString(viewReqDto.getPairId()));
            entityViewForm.setLastUpdatedTs(viewReqDto.getLastUpdatedTs());
            entityViewForm.setCustomerNumber(viewReqDto.getCustomerNumber());
            entityViewForm.setDocketNumber(viewReqDto.getDocketNumber());
            entityViewForm.setAppNumber(viewReqDto.getAppNumber());
            entityViewForm.setStatusChangeType(viewReqDto.getStatusChangeType());
            entityViewForm.setStatus(viewReqDto.getStatus());

            viewEntityList.add(entityViewForm);
        }
        return viewEntityList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer deleteEntityStatus(String pairId) throws PairAdminDatabaseException, Exception {
        UserRequestDto userRequestDto = userRequestDao.getUserRequestById(Long.valueOf(pairId));
        userRequestDto.setDeleteIndicator("Y");
        userRequestDto.setRequestStatusCount("Deleted");
        userRequestDto.setLastModifiedTimeStamp(new Date());
        // TODO: Set the last modified user name as well.
       return  userRequestDao.updateDeleteIndicatorByUserRequestId(userRequestDto);
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public EditEntityStatus transmitToPTOEntityStatus(EditEntityStatus editEntityStatusForm, String dn )
            throws PairAdminDatabaseException, UserNotFoundException, Exception {

    	 String message ="";
      if(editEntityStatusForm.getStatus()=="Submitted") {
            editEntityStatusForm.setStatus(RequestStatus.SUBMITTED.getName());
            message=   "The Entity Status of the application has been successfully changed";
      }
      if(editEntityStatusForm.getStatus()=="Failed") {
          editEntityStatusForm.setStatus(RequestStatus.FAILED.getName());
           message = "The application already has the requested entity status and can not be changed";
          
    }
          
            editEntityStatusForm.setSavedMessage(message);
       

        updateEntityStatus(editEntityStatusForm, dn);

        return editEntityStatusForm;
    }

  
}
