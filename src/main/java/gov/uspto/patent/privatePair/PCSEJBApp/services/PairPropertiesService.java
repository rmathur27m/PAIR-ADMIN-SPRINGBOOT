package gov.uspto.patent.privatePair.PCSEJBApp.services;


import gov.uspto.patent.privatePair.PCSEJBApp.dao.PairPropertiesDAO;
import gov.uspto.patent.privatePair.exceptionhandlers.PairAdminDatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
public class PairPropertiesService {

	private static final Logger  logger = LoggerFactory.getLogger(PairPropertiesService.class);
	@Autowired
   PairPropertiesDAO pairPropertiesDAO;
    
    @Transactional
    public HashMap getPairPropertiesData(String mode, String propertyName, String sortBy, String sortOrder) throws PairAdminDatabaseException, Exception{
    	logger.info("Inside the getPairPropertiesData Service call:-");

        HashMap resultHashMap = new HashMap();

        try {
            resultHashMap= pairPropertiesDAO.getPairPropertiesData( mode,  propertyName,  sortBy,  sortOrder);
        } catch (Exception e) {
            // TODO Auto-generated catch block
        	logger.error("Inside the getPairPropertiesData Service call Exception:-"+ e.getMessage());
            e.printStackTrace();
        }
        return resultHashMap;
    };
}
