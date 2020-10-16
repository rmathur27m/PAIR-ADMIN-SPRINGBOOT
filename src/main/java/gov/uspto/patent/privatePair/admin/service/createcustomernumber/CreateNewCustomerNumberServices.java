package gov.uspto.patent.privatePair.admin.service.createcustomernumber;

import java.util.List;

import gov.uspto.patent.privatePair.admin.domain.CreateNewCustomerNumberForm;
import gov.uspto.patent.privatePair.admin.domain.ViewSaveCompleteForm;
import gov.uspto.patent.privatePair.exceptionhandlers.PairAdminDatabaseException;

/**
 * 
 * Create New Customer Number Services
 * 
 */
public interface CreateNewCustomerNumberServices {

//    public enum GetCustomerNumberDetailsFilter {
//        ApplicationNumbers(1), Attorneys(2), CorrespondenceAddress(3), ALL(4);
//
//        private int value;
//
//        private GetCustomerNumberDetailsFilter(int value) {
//            this.value = value;
//        }
//
//        public int getValue() {
//            return value;
//        }
//    }

    /**
     * Save new Create Customer Number request.
     * 
     * @param createNewCustomerNumberForm
     *            Create new customer number details
     * @param dn
     *            distinguished name
     * @return {@link CreateNewCustomerNumberForm}
     * @throws PairAdminDatabaseException
     * @throws Exception
     */
    public CreateNewCustomerNumberForm saveNewCustomerNumber(CreateNewCustomerNumberForm createNewCustomerNumberForm, String dn)
            throws PairAdminDatabaseException, Exception;

    /**
     * Update existing Create new customer number request
     * 
     * @param createNewCustomerNumberForm
     *            Create new customer number details
     * @param dn
     *            distinguished name
     * @return {@link CreateNewCustomerNumberForm}
     * @throws PairAdminDatabaseException
     * @throws Exception
     */
    public CreateNewCustomerNumberForm updateNewCustomerNumber(CreateNewCustomerNumberForm createNewCustomerNumberForm, String dn)
            throws PairAdminDatabaseException, Exception;

    /**
     * Retrieve existing Create New Customer Number request
     * 
     * @param valueOf
     *            Create new customer number request id
     * @return {@link CreateNewCustomerNumberForm}
     * @throws Exception
     */
    public CreateNewCustomerNumberForm getNewCustomerNumberRequest(Long valueOf) throws Exception;

    /**
     * Retrieve list of registered attorneys associated with a create new
     * customer number request
     * 
     * @param createNewCustomerNumberForm
     *            {@link CreateNewCustomerNumberForm}
     * @return {@link CreateNewCustomerNumberForm}
     */
    //public CreateNewCustomerNumberForm listRegisteredPractitioners(CreateNewCustomerNumberForm createNewCustomerNumberForm);

    /**
     * Create New Customer Number request
     * 
     * @param createNewCustomerNumberForm
     *            {@link CreateNewCustomerNumberForm}
     * @return {@link CreateNewCustomerNumberForm}
     * @throws PairAdminDatabaseException
     */
    //public CreateNewCustomerNumberForm createNewCustomerNumber(CreateNewCustomerNumberForm createNewCustomerNumberForm)
    //        throws PairAdminDatabaseException;

    /**
     * Return list of registered attorney associated with Create New Customer
     * Number request
     * 
     * @param createNewCustomerNumberForm
     *            {@link CreateNewCustomerNumberForm}
     * @return {@link CreateNewCustomerNumberForm}
     */
    //public CreateNewCustomerNumberForm validateRegisteredPractitioners(CreateNewCustomerNumberForm createNewCustomerNumberForm);

    /**
     * Retrieve existing Create New Customer Number request
     * 
     * @param requestStatusList
     *            List of application statuses to search for
     * @param requestDays
     *            Time frame to search within
     * @param requestType
     *            Type of requests to search for
     * @param privatePAIRdn
     *            distinguished name
     * @return {@link ViewSaveCompleteForm}
     * @throws PairAdminDatabaseException
     * @throws Exception
     */
    public List<ViewSaveCompleteForm> getCustomerRequestsforView(List<?> requestStatusList, String requestDays,
          String requestType, String privatePAIRdn) throws PairAdminDatabaseException, Exception;

    /**
     * Delete an existing Create New Customer Number request
     * 
     * @param pairId
     *            Request id to delete
     * @throws PairAdminDatabaseException
     * @throws Exception
     */
    public void deleteCreateNewCustomerNumberRequest(String pairId) throws PairAdminDatabaseException, Exception;

    /**
     * Retrieve an existing customer number details
     * 
     * @param custNo
     *            Customer Number whose details to import
     * @param createNewCustomerNumberForm
     *            {@link CreateNewCustomerNumberForm}
     * @return
     */
    //public CreateNewCustomerNumberForm importCustomerDetails(String custNo,
    //        CreateNewCustomerNumberForm createNewCustomerNumberForm);

    /**
     * Update the status for the create new customer number request
     * 
     * @param createNewCustomerNumberForm
     *            {@link CreateNewCustomerNumberForm}
     * @return
     */
    //public void updateRequestStatus(CreateNewCustomerNumberForm createNewCustomerNumberForm) throws PairAdminDatabaseException;
    
    /**
     * Create New Customer Number request
     * 
     * @param createNewCustomerNumberForm
     *            {@link CreateNewCustomerNumberForm}
     * @return {@link CreateNewCustomerNumberForm}
     * @throws PairAdminDatabaseException
     */
    public CreateNewCustomerNumberForm createNewCustomerNumber(CreateNewCustomerNumberForm createNewCustomerNumberForm)
            throws PairAdminDatabaseException;
    
    /**
     * Update the status for the create new customer number request
     * 
     * @param createNewCustomerNumberForm
     *            {@link CreateNewCustomerNumberForm}
     * @return
     */
    public void updateRequestStatus(CreateNewCustomerNumberForm createNewCustomerNumberForm) throws PairAdminDatabaseException;
    
   
}
