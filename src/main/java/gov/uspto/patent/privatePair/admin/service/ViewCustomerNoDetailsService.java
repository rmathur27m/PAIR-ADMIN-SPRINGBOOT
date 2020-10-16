package gov.uspto.patent.privatePair.admin.service;

import gov.uspto.patent.privatePair.PCSEJBApp.dao.CustDetailsDAO;
import gov.uspto.patent.privatePair.admin.dao.ViewCustomerNoDetailsDAO;
import gov.uspto.patent.privatePair.admin.domain.CustDataInfo;
import gov.uspto.patent.privatePair.utils.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class ViewCustomerNoDetailsService {

    @Autowired
    ViewCustomerNoDetailsDAO viewCustomerNoDetailsDAO;
    
    
    
    public List getLastPrivatePairUpdate(int custNum) throws ServiceException {
        log.info("Inside the getLastPrivatePairUpdate Service call:-");
      
        List resultList = new ArrayList();
        try {
        	resultList=viewCustomerNoDetailsDAO.getLastPrivatePairUpdate(custNum);
        	  log.info("result"+resultList);
        	
        }  catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Inside the getLastPrivatePairUpdate Service call Exception:-"+ e.getMessage());
            e.printStackTrace();
        }

        return resultList;
    }
    
    public List getOptedInDetails(int custNum) throws ServiceException {
        log.info("Inside the getLastPrivatePairUpdate Service call:-");
      
        List resultList = new ArrayList();
        try {
        	resultList=viewCustomerNoDetailsDAO.getOptedInDetails(custNum);
        	  log.info("result"+resultList);
        	
        }  catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Inside the getOptedInDetails Service call Exception:-"+ e.getMessage());
            e.printStackTrace();
        }

        return resultList;
    }
    
  
    public List insertOrUpdateAssociationDNtoCN(String custnum,ArrayList userIdList) throws ServiceException {
        log.info("Inside the insertOrUpdateAssociationDNtoCN Service call:-");
      
        List resultList=null;
        try {
        	resultList=viewCustomerNoDetailsDAO.insertOrUpdateAssociationDNtoCN(custnum,userIdList);
        }  catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Inside the insertOrUpdateAssociationDNtoCN Service call Exception:-"+ e.getMessage());
            e.printStackTrace();
        }

        return resultList;
    }
    
    public List deleteCustNumQuery(String custnum,ArrayList userIdList) throws ServiceException {
        log.info("Inside the deleteCustNumQuery Service call:-");
      
        List resultList=null;
        try {
        	resultList=viewCustomerNoDetailsDAO.deleteCustNumQuery(custnum,userIdList);
        }  catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Inside the deleteCustNumQuery Service call Exception:-"+ e.getMessage());
            e.printStackTrace();
        }

        return resultList;
    }
  
    public String insertCorrDissassociate(CustDataInfo custDataInfo,String success) throws ServiceException {
        log.info("Inside the insertCustNumDetailsAdmin Service call:-");
      
        String resultList=null;
        try {
        	resultList=viewCustomerNoDetailsDAO.insertCorrDissassociate(custDataInfo,success);
        }  catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Inside the insertCustNumDetailsAdmin Service call Exception:-"+ e.getMessage());
            e.printStackTrace();
        }

        return resultList;
    }
    
    public String insertAssociateDisAssociate(CustDataInfo custDataInfo,String success) throws ServiceException {
        log.info("Inside the insertAssociateDisAssociate Service call:-");
      
        String resultList=null;
        try {
        	resultList=viewCustomerNoDetailsDAO.insertAssociateDisAssociate(custDataInfo,success);
        }  catch (Exception e) {
            // TODO Auto-generated catch block
            log.error("Inside the insertAssociateDisAssociate Service call Exception:-"+ e.getMessage());
            e.printStackTrace();
        }

        return resultList;
    }
    
    
    
}