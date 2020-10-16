package gov.uspto.patent.privatePair.PCSEJBApp.services;

import java.sql.SQLException;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gov.uspto.patent.privatePair.PCSEJBApp.dao.OutGoingCorrDAO;
import gov.uspto.patent.privatePair.exceptionhandlers.PairAdminDatabaseException;
import gov.uspto.patent.privatePair.utils.ServiceException;

@Service
public class OutGoingCorrService {

	private static final Logger  logger = LoggerFactory.getLogger(OutGoingCorrService.class);
	@Autowired
	OutGoingCorrDAO outGoingCorrDAO;
	 @Transactional
	    public String updateOCNDocketedAction(String docketed, String daId) throws PairAdminDatabaseException, Exception{
		 logger.info("Inside the updateOCNDocketedAction Service call:-");

	        String result =null;

	        try {
	        	result= outGoingCorrDAO.updateOCNDocketedAction(docketed,daId);
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	        	logger.error("Inside the updateOCNDocketedAction Service call Exception:-"+ e.getMessage());
	            e.printStackTrace();
	        }
	        return result;
	    };
	    
	    @Transactional
	    public HashMap getDisplayOutGoingCorrByCN(String custNum, int pastDays, String searchType, String sortBy, String sortOrder, int iDispRowIndex) throws ServiceException, ClassNotFoundException, SQLException {
	    	logger.info("Inside the getDisplayOutGoingCorrByCN Service call:-");
	    	 HashMap resultHashMap = new HashMap();

	    	 try {
				resultHashMap=outGoingCorrDAO.getDisplayOutGoingCorrByCN( custNum,  pastDays,  searchType,  sortBy,  sortOrder,  iDispRowIndex);
			}  catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("Inside the updateOCNDocketedAction Service call Exception:-"+ e.getMessage());
				e.printStackTrace();
			}
	    	 
	    	 return resultHashMap;
	    }
	    
	    @Transactional
	    public HashMap getDownloadOutGoingList(String downloadBy, String custNum, String searchType, int pastDays, String multipleAppId, String sortBy, String sortOrder) throws ServiceException, ClassNotFoundException, SQLException {
	    	logger.info("Inside the getDisplayOutGoingCorrByCN Service call:-");
	    	 HashMap resultHashMap = new HashMap();

	    	 try {
				resultHashMap=outGoingCorrDAO.getDownloadOutGoingList( downloadBy,  custNum,  searchType,  pastDays,  multipleAppId,  sortBy,  sortOrder);
			}  catch (SQLException e) {
				// TODO Auto-generated catch block
				logger.error("Inside the updateOCNDocketedAction Service call Exception:-"+ e.getMessage());
				e.printStackTrace();
			}
	    	 
	    	 return resultHashMap;
	    }
}
