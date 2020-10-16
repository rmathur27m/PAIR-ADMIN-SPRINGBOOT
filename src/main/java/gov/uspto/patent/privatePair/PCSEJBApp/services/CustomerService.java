package gov.uspto.patent.privatePair.PCSEJBApp.services;

import gov.uspto.patent.privatePair.PCSEJBApp.dao.CustomerDao;
import gov.uspto.patent.privatePair.PCSEJBApp.dto.CustomerVo;
import gov.uspto.patent.privatePair.admin.dto.SearchCriteriaVo;
import gov.uspto.patent.privatePair.exceptionhandlers.PairAdminDatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

	private static final Logger  logger = LoggerFactory.getLogger(CustomerService.class);
    @Autowired//(required= false)
    CustomerDao customerDao;
   
    
    @Transactional
    public int  customerValidation(String customerNumber) throws PairAdminDatabaseException, Exception{
    	logger.info("Inside the customerValidation Service call:-");
       int resultCount=0;


        try {
            resultCount= customerDao.customerValidation(customerNumber);
        } catch (Exception e) {
            // TODO Auto-generated catch block
        	logger.error("Inside the Service call Exception:-"+ e.getMessage());
            e.printStackTrace();
        }
        return resultCount;
    };

    @Transactional
    public int  checkCustomer(String customerNumber) throws PairAdminDatabaseException, Exception{
    	logger.info("Inside the checkCustomer Service call:-");
        int resultCount=0;


        try {
            resultCount= customerDao.checkCustomer(customerNumber);
        } catch (Exception e) {
            // TODO Auto-generated catch block
        	logger.error("Inside the checkCustomer Service call Exception:-"+ e.getMessage());
            e.printStackTrace();
        }
        return resultCount;
    };

    @Transactional
    public int  saveCustomer(CustomerVo customerVO) throws PairAdminDatabaseException, Exception{
    	logger.info("Inside the checkCustomer Service call:-");
        int resultCount=0;


        try {
            resultCount= customerDao.saveCustomer(customerVO);
        } catch (Exception e) {
            // TODO Auto-generated catch block
        	logger.error("Inside the saveCustomer Service call Exception:-"+ e.getMessage());
            e.printStackTrace();
        }
        return resultCount;
    };

    @Transactional
    public int  updateCustomer(CustomerVo customerVO) throws PairAdminDatabaseException, Exception{
    	logger.info("Inside the checkCustomer Service call:-");
        int resultCount=0;


        try {
            resultCount= customerDao.updateCustomer(customerVO);
        } catch (Exception e) {
            // TODO Auto-generated catch block
        	logger.error("Inside the updateCustomer Service call Exception:-"+ e.getMessage());
            e.printStackTrace();
        }
        return resultCount;
    };

    @Transactional
    public List<CustomerVo> getCustomersList(SearchCriteriaVo searchCriteria) throws Exception {
    	List<CustomerVo> customerList = new ArrayList<CustomerVo>();
    	 try {
    		 customerList= customerDao.getCustomersList(searchCriteria);
    		 System.out.println(customerList);
         }catch (Exception e) {
             // TODO Auto-generated catch block
        	 logger.error("Inside the getCustomersList Service call Exception:-"+ e.getMessage());
             e.printStackTrace();
         }
    	return customerList;
    }

    @Transactional
    public List<CustomerVo> getCustomerHistoryList(String customerNo) throws Exception {
        List<CustomerVo> customerList = new ArrayList<CustomerVo>();
        try {
            customerList= customerDao.getCustomerHistoryList(customerNo);
        }catch (Exception e) {
            // TODO Auto-generated catch block
        	logger.error("Inside the getCustomersList Service call Exception:-"+ e.getMessage());
            e.printStackTrace();
        }
        return customerList;
    }
}
