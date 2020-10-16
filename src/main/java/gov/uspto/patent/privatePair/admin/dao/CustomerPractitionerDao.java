package gov.uspto.patent.privatePair.admin.dao;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.uspto.patent.privatePair.admin.dao.mapper.CustomerPractitionerMapper;
import gov.uspto.patent.privatePair.admin.dto.CustomerPractitionerDto;
import gov.uspto.patent.privatePair.exceptionhandlers.PairAdminDatabaseException;

/**
 * DAO for Customer Practitioner object.
 * 
 */

@Component
public class CustomerPractitionerDao {

	private static final Logger  log = LoggerFactory.getLogger(CustomerPractitionerDao.class);
    @Autowired
    private SqlSession sqlSession;

    /**
     * Insert list of Customer Practitioner DTO objects into database.
     * 
     * @param customerPractitionerDtoList
     *            list of Customer Practitioner objects to insert
     * 
     * @throws Exception
     */
    public void insertCustomerPractitionerList(
            @Param("customerPractitioner") List<CustomerPractitionerDto> customerPractitionerDtoList) throws Exception {

        try {
            CustomerPractitionerMapper customerPractitionerMapper = sqlSession.getMapper(CustomerPractitionerMapper.class);

            customerPractitionerMapper.insertCustomerPractitionerList(customerPractitionerDtoList);
        } catch (Exception e) {
        	log.error("Failed to insert the customer practitioner list: " + e.getMessage());
            throw new PairAdminDatabaseException("Failed to insert the customer practitioner list.", e);
        }
    }

    /**
     * Update Customer Practitioner DTO objects, in the database, that are
     * associated with a specific customer request id.
     * 
     * @param customerPractitionerDtoList
     *            list of Customer Practitioner objects to insert
     * 
     * @throws Exception
     */
    public void updateCustomerPractitionerListByRequestId(
            @Param("customerPractitioner") List<CustomerPractitionerDto> customerPractitionerDtoList,
            @Param("newCustomerNumberRequestId") Integer newCustomerNumberRequestId) throws Exception {

        try {
            CustomerPractitionerMapper customerPractitionerMapper = sqlSession.getMapper(CustomerPractitionerMapper.class);

            customerPractitionerMapper.updateCustomerPractitionerListByRequestId(customerPractitionerDtoList,
                    newCustomerNumberRequestId);
        } catch (Exception e) {
        	log.error("Failed to update the customer practitioner list: " + e.getMessage());
            throw new PairAdminDatabaseException("Failed to update the customer practitioner list.", e);
        }
    }

    /**
     * Retrieve Customer Practitioner DTO object based on new Customer Number
     * Request id
     * 
     * @param newCustomerNumberRequestId
     *            request id to search against
     * @return list of Customer Number DTOs associated with the passed in id
     * @throws Exception
     */
    public List<CustomerPractitionerDto> getAllCustomerPractitionerById(Integer newCustomerNumberRequestId) throws Exception {

        List<CustomerPractitionerDto> customerPractitionerDtoList = new ArrayList<CustomerPractitionerDto>();

        try {
            CustomerPractitionerMapper customerPractitionerMapper = sqlSession.getMapper(CustomerPractitionerMapper.class);

            customerPractitionerDtoList = customerPractitionerMapper.getAllCustomerPractitionerById(newCustomerNumberRequestId);

            return customerPractitionerDtoList;
        } catch (Exception e) {
        	log.error("Failed to get all customer practitioner list by Id: " + e.getMessage());
            throw new PairAdminDatabaseException("Failed to get all customer practitioner list by Id.", e);
        }
    }

    /**
     * Delete new Customer Number request by id
     * 
     * @param newCustomerNumberRequestId
     *            id of Customer Number request to delete
     * @throws Exception
     */
    public void deleteCustomerPractitionerByRequestId(Integer newCustomerNumberRequestId) throws Exception {

        try {
            CustomerPractitionerMapper customerPractitionerMapper = sqlSession.getMapper(CustomerPractitionerMapper.class);

            customerPractitionerMapper.deleteCustomerPractitionerByRequestId(newCustomerNumberRequestId);
        } catch (Exception e) {
        	log.error("Failed to get all customer practitioner list by Id: " + e.getMessage());
            throw new PairAdminDatabaseException("Failed to get all customer practitioner list by Id.", e);
        }

    }

}
