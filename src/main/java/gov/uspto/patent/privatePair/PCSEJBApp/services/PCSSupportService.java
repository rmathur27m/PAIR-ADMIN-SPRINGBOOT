package gov.uspto.patent.privatePair.PCSEJBApp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gov.uspto.patent.privatePair.PCSEJBApp.dao.PCSSupportDAO;
import gov.uspto.patent.privatePair.PCSEJBApp.dto.EofficeNotificationStatusCheckVO;
import gov.uspto.patent.privatePair.PCSEJBApp.dto.OCNArchiveSearchResultVO;
import gov.uspto.patent.privatePair.utils.ServiceException;

@Service
public class PCSSupportService {

	private static final Logger  logger = LoggerFactory.getLogger(PCSSupportService.class);
	@Autowired
	   PCSSupportDAO pCSSupportDAO;
	    
	    @Transactional
	    public int validateUser(String userName){
	    	logger.info("Inside the validateUser Service call:-");
	        int resultHashMap = 0;
	        try {
	            resultHashMap= pCSSupportDAO.validateUser(userName);
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	        	logger.error("Inside the validateUser Service call Exception:-"+ e.getMessage());
	            e.printStackTrace();
	        }
	        return resultHashMap;
	    };
	    
	    @Transactional
	    public int setSystemAnnouncement(String message, String msg) throws ServiceException{
	    	logger.info("Inside the setSystemAnnouncement Service call:-");
	        int resultHashMap = 0;
	        try {
	            resultHashMap= pCSSupportDAO.setSystemAnnouncement(message, msg);
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	        	logger.error("Inside the setSystemAnnouncement Service call Exception:-"+ e.getMessage());
	            e.printStackTrace();
	        }
	        return resultHashMap;
	    };
	    
	    @Transactional
	    public String[] getSystemAnnouncement(String msg) {
	        logger.info("Inside the getSystemAnnouncement Service call:-");
	        String[] resultHashMap = {};
	        try {
	            resultHashMap= pCSSupportDAO.getSystemAnnouncement(msg);
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	        	logger.error("Inside the getSystemAnnouncement Service call Exception:-"+ e.getMessage());
	            e.printStackTrace();
	        }
	        return resultHashMap;
	    };
	    
	    @Transactional
	    public List<OCNArchiveSearchResultVO>  pcsSupportSearchOCNRows(Map searchParams) {
	    	logger.info("Inside the pcsSupportSearchOCNRows Service call:-");
	        List<OCNArchiveSearchResultVO> dtolist = new ArrayList<OCNArchiveSearchResultVO>();
	        try {
	        	dtolist= pCSSupportDAO.pcsSupportSearchOCNRows(searchParams);
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	        	logger.error("Inside the pcsSupportSearchOCNRows Service call Exception:-"+ e.getMessage());
	            e.printStackTrace();
	        }
	        return dtolist;
	    }
	    
	    @Transactional
	    public List pcsSupportSearchEnotifications(Map searchParams) {
	    	logger.info("Inside the pcsSupportSearchEnotifications Service call:-");
			List<EofficeNotificationStatusCheckVO> searchResults = new ArrayList<EofficeNotificationStatusCheckVO>();
	        try {
	        	searchResults=pCSSupportDAO.pcsSupportSearchEnotifications(searchParams);
	        } catch (Exception e) {
	            // TODO Auto-generated catch block
	        	logger.error("Inside the pcsSupportSearchEnotifications Service call Exception:-"+ e.getMessage());
	            e.printStackTrace();
	        }
	        return searchResults;
	    }
}
