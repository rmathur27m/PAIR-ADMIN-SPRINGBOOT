package gov.uspto.patent.privatePair.admin.service.entitystatus;

import gov.uspto.patent.privatePair.admin.domain.EditEntityStatus;
import gov.uspto.patent.privatePair.admin.domain.ViewSaveCompleteForm;
import gov.uspto.patent.privatePair.exceptionhandlers.PairAdminDatabaseException;
import gov.uspto.patent.privatePair.exceptionhandlers.UserNotFoundException;

import java.util.List;

/**
 * Helper class; contains various methods to return single pieces of
 * information.
 * 
 */
public interface EntityStatusServices {

	/**
     * Saves the entity status request to the PAIR/PURM DB.
     * 
     * @param editEntityStatusForm
     *            The entity status form.
     * @param dn
     *            The distinguished name of the logged in user.
     * @return {@link EditEntityStatus} The form object with the updated
     *         data.
     * @throws PairAdminDatabaseException
     *             Any database related exception.
     * @throws UserNotFoundException
     *             It is thrown when the logged in user dn is not found in the
     *             pairUser table.
     * @throws Exception
     * 
     */
    public EditEntityStatus saveEntityStatus(EditEntityStatus editEntityStatusForm, String dn)
            throws PairAdminDatabaseException, UserNotFoundException, Exception;

    

    /**
     * Retrieves the entity change request for the given userRequestId from the
     * PAIR/PURM DB.
     * 
     * @param userRequestId
     * 
     * @return {@link EditEntityStatus}
     * @throws PairAdminDatabaseException
     *             Any database related exception.
     * @throws Exception
     */
    public EditEntityStatus getEntityChangeRequest(Long userRequestId) throws PairAdminDatabaseException, Exception;

    /**
     * Retrieves a list of entity requests for the given request status, days
     * and type.
     * 
     * @param requestStatus
     *            Request status (Completed/Saved).
     * @param requestDays
     *            Requests created for the given days.
     * @param requestType
     *            Type of the request (Entity request)
     * @return {@link List} ViewSaveCompleteForm object.
     * @throws Exception
     * @throws PairAdminDatabaseException
     */
    public List<ViewSaveCompleteForm> getEntityRequestsforView(List requestStatusList, String requestDays, String requestType,
            String privatePAIRdn) throws PairAdminDatabaseException, Exception;

    /**
     * Updates the entity status request in the PAIR/PURM DB.
     * 
     * @param editEntityStatusForm
     *            The {@link EditEntityStatus} object.
     * @param dn
     *            The distinguished name of the logged in user.
     * @return {@link EditEntityStatus}
     * @throws PairAdminDatabaseException
     *             Any database related exception.
     * @throws Exception
     */
    public EditEntityStatus updateEntityStatus(EditEntityStatus editEntityStatusForm, String dn)
            throws PairAdminDatabaseException, Exception;

    /**
     * Delete(Soft delete) the given entity Status request from the PAIR/PURM
     * DB.
     * 
     * @param pairId
     *            The pairId to be deleted.
     * 
     * @throws PairAdminDatabaseException
     *             Any database related exception.
     * @throws Exception
     */
    public Integer deleteEntityStatus(String pairId) throws PairAdminDatabaseException, Exception;

    /**
     * Preview / Validate the entity status for the given form object.
     * 
     * @param editEntityStatusForm
     *            The form object to be validated.
     * @param dn
     *            The distinguished name of the logged in user.
     * @return {@link EditEntityStatus}
     * @throws PairAdminDatabaseException
     *             Any database related exception.
     * @throws UserNotFoundException
     *             It is thrown when the logged in user dn is not found in the
     *             pairUser table.
     * @throws Exception
     */
   /* public EditEntityStatus previewEntityStatus(EditEntityStatus editEntityStatusForm, String dn)
            throws PairAdminDatabaseException, UserNotFoundException, Exception;*/
    
    /**
     * Transmit the entity status request using the palm WS to PTO.
     * 
     * @param editEntityStatusForm
     *            The entity status form object.
     * @param dn
     *            The distinguished name of the logged in user.
     * @return {@link EditEntityStatusForm} The final updated form object.
     * @throws PairAdminDatabaseException
     *             Any database related exception.
     * @throws UserNotFoundException
     *             It is thrown when the logged in user dn is not found in the
     *             pairUser table.
     * @throws Exception
     */
    public EditEntityStatus transmitToPTOEntityStatus(EditEntityStatus editEntityStatusForm, String dn)
            throws PairAdminDatabaseException, UserNotFoundException, Exception;
}
