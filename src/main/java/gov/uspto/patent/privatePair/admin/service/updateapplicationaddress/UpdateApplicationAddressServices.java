package gov.uspto.patent.privatePair.admin.service.updateapplicationaddress;

import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;

import gov.uspto.patent.privatePair.admin.domain.UpdateApplicationAddressForm;
import gov.uspto.patent.privatePair.admin.domain.ViewSaveCompleteForm;
import gov.uspto.patent.privatePair.exceptionhandlers.PairAdminDatabaseException;

/**
 * Service layer class with Update Application Address Services
 * 
 */
public interface UpdateApplicationAddressServices {

    public List<ViewSaveCompleteForm> getUpdateAddrRequestsforView(List<?> requestStatusList, String requestDays,
            String requestType, String privatePAIRdn) throws PairAdminDatabaseException, Exception;
    
    
    /**
     * This method is the service layer used in saving the Update Application
     * Address form data to the database.
     * 
     * @param UpdateApplicationAddressForm
     *            HTML form used on the Update Application Address screen.
     * 
     * @param dn
     *            Distinguished Name from the user's PKI Certificate issued by
     *            USPTO
     * 
     * @return Update Application Address form with updated status message
     * 
     *             This is exception is thrown if there is database related
     *             exception.
     */
    public UpdateApplicationAddressForm saveUpdateApplicationAddress(UpdateApplicationAddressForm updateApplicationAddressForm,
            String dn) throws PairAdminDatabaseException, Exception;
    
    public Integer deleteUpdateApplicationAddress(long userRequestId) throws PairAdminDatabaseException;
    
    public Date updateAnUpdateApplicationAddress(UpdateApplicationAddressForm updateApplicationAddressForm, String dn)
            throws PairAdminDatabaseException, Exception;
    
    public UpdateApplicationAddressForm getUpdateAddressRequest(Long userRequestId) throws Exception;
    
    /**
     * Insert a Data Transfer Object (DTO) representing a PAIR Customer
     * Correspondence Address details.
     * 
     * @param CustomerCorrAddressDto
     *            DTO representing the Customer Correspondence Address to
     *            insert.
     * @throws Exception
     */
    public void insertCustomerCorrAddress(UpdateApplicationAddressForm updateApplicationAddressForm)
            throws PairAdminDatabaseException, Exception;
    
    public void updateRequestStatusByUserRequestId(UpdateApplicationAddressForm updateApplicationAddressForm)
            throws PairAdminDatabaseException;

    /**
     * Retrieves the Correspondence Address associated with a given customer
     * number
     * 
     * @param UpdateApplicationAddressForm
     *            HTML form object
     * 
     */
    public UpdateApplicationAddressForm getCorrespondenceAddress(UpdateApplicationAddressForm updateApplicationAddressForm);

    /**
     * Validate Customer Number
     * 
     * @param customer
     *            number
     * 
     * @return true if customer number is valid; false otherwise
     * @throws URISyntaxException 
     */
    public boolean validCustomerNumber(String customerNumber);

    /**
     * Validate that application id associated with an Update Application
     * Address is a valid application
     * 
     * @param applicationNumber
     *            application number associated with request.
     * @return true if application id is valid; false otherwise.
     */
    public boolean validateApplicationId(String applicationNumber);

    /**
     * Validate that patent number associated with an Update Application Address
     * is a valid patent.
     * 
     * @param patentNumber
     *            application number associated with request.
     * @return true if patent number is valid; false otherwise.
     */
    public boolean validatePatentId(String patentId);
    
    /**
     * Validate that application id associated with an Update Application
     * Address is a valid application
     * 
     * @param applicationNumber
     *            application number associated with request.
     * @return true if application number not found in PALM; false otherwise.
     */
    public boolean applicationNumberNotFound(String applicationNumber);

    /**
     * Validate that patent number associated with an Update Application Address
     * is a valid patent number
     * 
     * @param patentNumber
     *            application number associated with request.
     * @return true if patent number not found in PALM; false otherwise.
     */
    public boolean patentNumberNotFound(String patentId);
    
    /**
     * Validate that application id and patent id are associated
     * 
     * @param expectedApplicationNumber
     *            application number
     * @param expectedPatentNumber
     *            patent number
     * @return true if associated; false otherwise
     */
    public boolean validateApplicationIdIsAssociatedWithPatentId(String expectedApplicationNumber, String expectedPatentNumber);
    
    /**
     * Validate that person requesting address change does indeed have Power of
     * Attorney over application/patent.
     * 
     * @param updateRegistrationNumber
     *            registration number of user requesting change.
     * @param applicationNumber
     *            application number associated with the change request.
     * @param patentNumber
     *            patent number associated with the change request.
     * @return true if user has power of attorney; false otherwise
     */
    public boolean validatePowerOfAttorney(String updateRegistrationNumber, String applicationNumber, String patentNumber);
    
    /**
     * Send Update Application Address Request to PALM Web Service
     * 
     * @param updateApplicationAddressForm
     *            {@link UpdateApplicationAddressForm}
     * @return
     */
    public void transmitUpdateApplicationAddressRequest(UpdateApplicationAddressForm updateApplicationAddressForm);
 }
