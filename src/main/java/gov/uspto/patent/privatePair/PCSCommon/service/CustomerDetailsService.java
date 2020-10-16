package gov.uspto.patent.privatePair.PCSCommon.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.uspto.patent.privatePair.PCSCommon.dao.CustomerDetailsDAO;
import gov.uspto.patent.privatePair.utils.ServiceException;

@Service
public class CustomerDetailsService {

	
	private static final Logger  log = LoggerFactory.getLogger(CustomerDetailsService.class);
	@Autowired
	private CustomerDetailsDAO customerDetailsDAO;
	
	@Transactional
	public List<String> getRegistrationNumber(String dn){
		log.info("Inside AttorneyAsuService:");
		List<String> userMappingList = new ArrayList<String>();
		try {
			userMappingList=customerDetailsDAO.getRegistrationNumber(dn);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			log.error("Exception caught in  AttorneyAsuService:"+ e.getMessage());
			e.printStackTrace();
		}
		return userMappingList;
	}
	
	@Transactional
	public Vector getCustRowsByDN(String p_dn, String CASE_SENSITIVE_CHECK) {
		log.info("Inside AttorneyAsuService:");
		Vector userMappingList = new Vector();
		try {
			userMappingList=customerDetailsDAO.getCustRowsByDN(p_dn, CASE_SENSITIVE_CHECK);
		} catch (ServiceException | SQLException e) {
			// TODO Auto-generated catch block
			log.error("Exception caught in  getCustRowsByDN Service:"+ e.getMessage());
			e.printStackTrace();
		}
		return userMappingList;
	}
}
