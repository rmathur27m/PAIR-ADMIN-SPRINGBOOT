package gov.uspto.patent.privatePair.PCSEJBApp.services;

import gov.uspto.patent.privatePair.PCSEJBApp.dao.EOfficeActionDAO;
import gov.uspto.patent.privatePair.utils.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EOfficeActionServiceTest {

    @InjectMocks
    EOfficeActionService eOfficeActionService;

    @Mock
    EOfficeActionDAO eOfficeActionDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getDisplayEOfficeActionByAppId() throws Exception {
        String appId = "dummyAppId";
        String sortBy = "appId";
        String sortOrder ="ASC";
        int dispRowIndex =10;
        String allOptedInCustNumbers = "testCustomers";

        HashMap mockResult = new HashMap();

        Mockito.when(eOfficeActionDAO.getDisplayEOfficeActionByAppId(appId,sortBy,sortOrder,dispRowIndex,allOptedInCustNumbers))
                .thenReturn(mockResult);
        HashMap expectedResult = eOfficeActionService.getDisplayEOfficeActionByAppId(appId,sortBy,sortOrder,dispRowIndex,allOptedInCustNumbers);
        assertEquals(mockResult,expectedResult);
        verify(eOfficeActionDAO, times(1)).getDisplayEOfficeActionByAppId(appId,sortBy,sortOrder,dispRowIndex,allOptedInCustNumbers);
    }
}