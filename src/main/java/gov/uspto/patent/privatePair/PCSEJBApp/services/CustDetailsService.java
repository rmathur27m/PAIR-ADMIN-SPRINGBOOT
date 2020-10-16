package gov.uspto.patent.privatePair.PCSEJBApp.services;

import gov.uspto.patent.privatePair.PCSEJBApp.dao.CustDetailsDAO;
import gov.uspto.patent.privatePair.utils.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

@Service
@Slf4j
public class CustDetailsService {

    @Autowired
    CustDetailsDAO custDetailsDAO;
    public HashMap getDisplayAppList(String displayBy, String custNum, String appStatus, int intStatusDays, String sortBy, String sortOrder, int iDispRowIndex) throws ServiceException {
        log.info("Inside the getDisplayOutGoingCorrByCN Service call:-");
        HashMap resultHashMap = new HashMap();

        try {
            resultHashMap=custDetailsDAO.getDisplayAppList( displayBy,custNum,  appStatus,  intStatusDays,  sortBy,  sortOrder,  iDispRowIndex);
        }  catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Inside the updateOCNDocketedAction Service call Exception:-"+ e.getMessage());
            e.printStackTrace();
        }

        return resultHashMap;
    }
    
    
    
    public HashMap getDisplayAppListForCustSearch(String displayBy, String custNum, String appStatus, int intStatusDays, String sortBy, String sortOrder, int iDispRowIndex) throws ServiceException {
        log.info("Inside the getDisplayOutGoingCorrByCN Service call:-");
        HashMap resultHashMap = new HashMap();

        try {
            resultHashMap=custDetailsDAO.getDisplayAppListForCustSearch( displayBy,custNum,  appStatus,  intStatusDays,  sortBy,  sortOrder,  iDispRowIndex);
        }  catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Inside the updateOCNDocketedAction Service call Exception:-"+ e.getMessage());
            e.printStackTrace();
        }

        return resultHashMap;
    }
    
    
    public Vector getOptedInCustRowsByDN(String distinguishedName) throws ServiceException {
        log.info("Inside the getDisplayOutGoingCorrByCN Service call:-");
        Vector resultRowVect = new Vector();

        try {
        	resultRowVect=custDetailsDAO.getOptedInCustRowsByDN(distinguishedName);
        }  catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Inside the updateOCNDocketedAction Service call Exception:-"+ e.getMessage());
            e.printStackTrace();
        }

        return resultRowVect;
    }

    public HashMap getDownloadAppList(String downloadBy, String custNum,String appStatus, int statusDays, String multipleAppId, String sortBy, String sortOrder)
            throws ServiceException {
        HashMap resultHashMap = new HashMap();
        try {
            resultHashMap=custDetailsDAO.getDownloadAppList( downloadBy,custNum,  appStatus,  statusDays, multipleAppId, sortBy,  sortOrder);
        }  catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Inside the getDownloadAppList Service call Exception:-"+ e.getMessage());
            e.printStackTrace();
        }


        return resultHashMap;
    }

    public Map updateAttorneyDocketNumber(String oldAttDktNum, String newAttDktNum, List appList)
            throws ServiceException {
        //String result=null;
        Map map = new HashMap();
        try {
            map = custDetailsDAO.updateAttorneyDocketNumber(oldAttDktNum, newAttDktNum, appList);
            }catch(Exception ex){
                log.error("Exception Caught in Update Attorney service method-->",ex.getMessage());
                ex.printStackTrace();

            }
        return map;
    }
    
    public String updateOutGoingCorrViewStatus(List ifwOutgoingList) throws ServiceException {
    	String reusultFlag=null;
    	 try {
    		 reusultFlag = custDetailsDAO.updateOutGoingCorrViewStatus(ifwOutgoingList);
             }catch(Exception ex){
                 log.error("Exception Caught in updateOutGoingCorrViewStatus service method-->",ex.getMessage());
                 ex.printStackTrace();

             }
    	return reusultFlag;
    }
    
    
    public String outGoingCorrByAppId(String appId,String user) throws ServiceException {
    	String reusultFlag=null;
    	 try {
    		 reusultFlag = custDetailsDAO.outGoingCorrByAppId(appId,user);
             }catch(Exception ex){
                 log.error("Exception Caught in outGoingCorrByAppId service method-->",ex.getMessage());
                 ex.printStackTrace();

             }
    	return reusultFlag;
    }
}
