/**
 * 
 */
package gov.uspto.patent.privatePair.admin.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gov.uspto.patent.privatePair.admin.dao.mapper.CustomerCorrAddressMapper;
import gov.uspto.patent.privatePair.admin.dto.CustomerCorrAddressDto;
import gov.uspto.patent.privatePair.exceptionhandlers.PairAdminDatabaseException;

/**
 * @author skosuri1 DAO object representing Customer Correspondence Address
 *         Request.
 * 
 */
@Component
public class CustomerCorrAddressDao implements CustomerCorrAddressMapper {
	
	private static final Logger  log = LoggerFactory.getLogger(CustomerCorrAddressDao.class);

    @Autowired
    private SqlSession sqlSession;

    /**
     * {@inheritDoc}
     */
    public void insertCorrAddress(CustomerCorrAddressDto customerCorrAddressDto) throws Exception {
        try {
            CustomerCorrAddressMapper customerCorrAddressMapper = sqlSession.getMapper(CustomerCorrAddressMapper.class);
            customerCorrAddressMapper.insertCorrAddress(customerCorrAddressDto);
        } catch (Exception e) {
        	log.error("Failed to insert the customer correspondence address: " + e.getMessage());
            throw new PairAdminDatabaseException("Failed to insert the customer correspondence address.", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public CustomerCorrAddressDto getCustomerCorrAddressById(long userRequestId) throws Exception {
        CustomerCorrAddressDto customerCorrAddressDto = null;
        try {
            CustomerCorrAddressMapper customerCorrAddressMapper = sqlSession.getMapper(CustomerCorrAddressMapper.class);
            customerCorrAddressDto = customerCorrAddressMapper.getCustomerCorrAddressById(userRequestId);
        } catch (Exception e) {
        	log.error("Failed to get the customer correspondence address for: " + e.getMessage());
            throw new PairAdminDatabaseException("Failed to get the customer correspondence address for: " + userRequestId, e);
        }
        return customerCorrAddressDto;
    }
    
    /**
     * {@inheritDoc}
     */
    public void deleteCustomerCorrAddressById(long userRequestId) throws Exception {
        
        try {
            CustomerCorrAddressMapper customerCorrAddressMapper = sqlSession.getMapper(CustomerCorrAddressMapper.class);
            customerCorrAddressMapper.deleteCustomerCorrAddressById(userRequestId);
        } catch (Exception e) {
        	log.error("Failed to delete the customer correspondence address for: " + e.getMessage());
            throw new PairAdminDatabaseException("Failed to delete the customer correspondence address for: " + userRequestId, e);
        }
    }

}
