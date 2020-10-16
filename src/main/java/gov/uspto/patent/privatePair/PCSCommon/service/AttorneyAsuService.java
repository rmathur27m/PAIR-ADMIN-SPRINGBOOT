package gov.uspto.patent.privatePair.PCSCommon.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gov.uspto.patent.privatePair.PCSCommon.dao.AttorneyAsuDAO;
import gov.uspto.patent.privatePair.PCSCommon.dto.UserDTO;
import gov.uspto.patent.privatePair.utils.ServiceException;

@Service
public class AttorneyAsuService {
	private static final Logger  log = LoggerFactory.getLogger(AttorneyAsuService.class);
	@Autowired
	private AttorneyAsuDAO attorneyAsuDAO;
	
	@Transactional
	public List<UserDTO> getUserMappingsByUserId(String userId, String sortBy, String sortOrder){
		log.info("Inside AttorneyAsuService:");
		List<UserDTO> userMappingList = new ArrayList<UserDTO>(); 
		try {
			userMappingList=attorneyAsuDAO.getUserMappingsByUserId(userId, sortBy, sortOrder);
		} catch (ServiceException | SQLException e) {
			// TODO Auto-generated catch block
			log.error("Exception caught in  AttorneyAsuService:"+ e.getMessage());
			e.printStackTrace();
		}
		return userMappingList;
	}
	
	@Transactional
	public HashMap getAttorneyAsuByDN(String p_dn, String CASE_SENSITIVE_CHECK, String sortBy, String sortOrder){
		log.info("Inside AttorneyAsuService:");
		HashMap resultHashMap = new HashMap();
		try {
			resultHashMap=attorneyAsuDAO.getAttorneyAsuByDN(p_dn, CASE_SENSITIVE_CHECK, sortBy, sortOrder);
		} catch (ServiceException | SQLException e) {
			// TODO Auto-generated catch block
			log.error("Exception caught in  AttorneyAsuService:"+ e.getMessage());
			e.printStackTrace();
		}
		return resultHashMap;
	}
}
