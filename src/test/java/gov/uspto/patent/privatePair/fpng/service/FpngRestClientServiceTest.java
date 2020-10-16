package gov.uspto.patent.privatePair.fpng.service;

import gov.uspto.patent.privatePair.exceptionhandlers.ApplicationException;
import gov.uspto.patent.privatePair.fpng.dao.FPNGFeePaymentHistoryDAO;
import gov.uspto.patent.privatePair.fpng.dto.FpngServiceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FpngRestClientServiceTest {

    @InjectMocks
    FpngRestClientService fpngRestClientService;

    @Mock
    FPNGFeePaymentHistoryDAO mockFeePaymentDao;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getFeePaymentRecsByAppNum() throws ApplicationException {
        FpngServiceResponse testResp = new FpngServiceResponse();
        testResp.setSuccess(true);
        testResp.setAdditionalProperty("flag", "yes");
        testResp.setErrorMessageText(new ArrayList<>());
        testResp.setInfoMessageText(new ArrayList<>());

        when(mockFeePaymentDao.getFeePaymentHistoryFromFpng("testAppId"))
                .thenReturn(testResp);

        FpngServiceResponse result = fpngRestClientService.getFeePaymentRecsByAppNum("testAppId");

        assertEquals(testResp,result);

        verify(mockFeePaymentDao, times(1)).getFeePaymentHistoryFromFpng("testAppId");
    }
}