package gov.uspto.patent.privatePair.oems.service.oems;

import gov.uspto.patent.privatePair.oems.dao.OemsTransactionDAO;
import gov.uspto.patent.privatePair.oems.dto.OEMSDTO;
import gov.uspto.patent.privatePair.exceptionhandlers.PairAdminDatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OEMSTransactionService {
	
	private static final Logger  log = LoggerFactory.getLogger(OEMSTransactionService.class);
	@Autowired//(required= false)
    OemsTransactionDAO oemsTransactionDAO;
    
	@Transactional
    public String  insertOemsTransactionData(String transactionId,
			 String documentID, 
			 int customerNumber) {
		log.info ("Inside the Service call:-");
		int resultCount=0;
		try {
			resultCount= oemsTransactionDAO.insertOemsTransactionData(transactionId, documentID, customerNumber);
		} catch (Exception e) {
			log.error("Inside the Service call Exception:-"+ e.getMessage());
		}
        return Integer.toString(resultCount) ;
    };
}
