package gov.uspto.patent.privatePair.PCSEJBApp.services;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gov.uspto.patent.privatePair.PCSEJBApp.dao.EOfficeActionDAO;
import gov.uspto.patent.privatePair.exceptionhandlers.PairAdminDatabaseException;

@Service
public class EOfficeActionService {

	private static final Logger  logger = LoggerFactory.getLogger(EOfficeActionService.class);
	
	@Autowired
	EOfficeActionDAO eOfficeActionDAO;
	
	 @Transactional
	    public HashMap getDisplayEOfficeActionByAppId(String appId, String sortBy, String sortOrder, int dispRowIndex,
                String allOptedInCustNumbers) throws PairAdminDatabaseException, Exception{
		 logger.info("Inside the getDisplayEOfficeActionByAppId Service call:-");

	        HashMap resultHashMap = new HashMap();

	        try {
	            resultHashMap= eOfficeActionDAO.getDisplayEOfficeActionByAppId(appId,sortBy, sortOrder, dispRowIndex, allOptedInCustNumbers);
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	        	logger.error("Inside the getDisplayEOfficeActionByAppId Service call Exception:-"+ e.getMessage());
	            e.printStackTrace();
	        }
	        return resultHashMap;
	    }
	 
	 @Transactional
	 public HashMap getDisplayEOfficeActionByCn(String custNum, String startDate, String endDate, String sortBy,
			String sortOrder, int dispRowIndex) throws PairAdminDatabaseException, Exception {
		 logger.info("Inside the getDisplayEOfficeActionByCn Service call:-");

	        HashMap resultHashMap = new HashMap();

	        try {
	            resultHashMap= eOfficeActionDAO.getDisplayEOfficeActionByCn(custNum, startDate, endDate,  sortBy,
	        			sortOrder, dispRowIndex);
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	        	logger.error("Inside the getDisplayEOfficeActionByCn Service call Exception:-"+ e.getMessage());
	            e.printStackTrace();
	        }
	        return resultHashMap;
	    }
}
