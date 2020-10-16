package gov.uspto.patent.privatePair.fpng.service;

import gov.uspto.patent.privatePair.exceptionhandlers.ApplicationException;
import gov.uspto.patent.privatePair.fpng.dao.FPNGFeePaymentHistoryDAO;
import gov.uspto.patent.privatePair.fpng.dto.FpngServiceResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FpngRestClientService {

    private static final Logger log = LoggerFactory.getLogger(FpngRestClientService.class);

    @Autowired
    FPNGFeePaymentHistoryDAO fPNGFeePaymentHistoryDAO;

    @Transactional
    public FpngServiceResponse getFeePaymentRecsByAppNum(String appOrPatentNumber) {
        FpngServiceResponse serviceResponse = new FpngServiceResponse();
        try {
            serviceResponse = fPNGFeePaymentHistoryDAO.getFeePaymentHistoryFromFpng(appOrPatentNumber);
        } catch (ApplicationException e) {
            log.error("Exception caught in getFeePaymentRecsByAppNum method--->", e);
        }

        return serviceResponse;
    }

    @Transactional
    public FpngServiceResponse getFeePaymentRecsByAppNum(String appOrPatentNumber,
                                                         String issueDate, int patentFlag) {
        FpngServiceResponse serviceResponse = new FpngServiceResponse();
        try {
            serviceResponse = fPNGFeePaymentHistoryDAO.getFeePaymentHistoryFromFpng(appOrPatentNumber, issueDate, patentFlag);
        } catch (ApplicationException e) {
            log.error("Exception caught in getFeePaymentRecsByAppNum method where issue date and patent flag been passed--->", e);
        }

        return serviceResponse;
    }
}
