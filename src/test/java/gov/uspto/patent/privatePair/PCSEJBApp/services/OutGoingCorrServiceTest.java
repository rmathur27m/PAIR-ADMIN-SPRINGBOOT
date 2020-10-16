package gov.uspto.patent.privatePair.PCSEJBApp.services;

import gov.uspto.patent.privatePair.PCSEJBApp.dao.OutGoingCorrDAO;
import gov.uspto.patent.privatePair.utils.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class OutGoingCorrServiceTest {

    @InjectMocks
    OutGoingCorrService outGoingCorrService;

    @Mock
    OutGoingCorrDAO outGoingCorrDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void updateOCNDocketedAction() throws Exception {

        Mockito.when(outGoingCorrDAO.updateOCNDocketedAction("testDocket", "A78545"))
                .thenReturn("mockResultUpdated");
        String expectedResult = outGoingCorrService.updateOCNDocketedAction("testDocket", "A78545");
        assertEquals("mockResultUpdated",expectedResult);
        verify(outGoingCorrDAO, times(1)).updateOCNDocketedAction("testDocket", "A78545");
    }

    @Test
    public void getDisplayOutGoingCorrByCN() throws Exception {
        HashMap mockResult = new HashMap();
        String custNum = "CUS420315";
        int pastDays = 20;
        String searchType = "byCustNum";
        String sortBy = "custNum";
        String sortOrder = "custNum";
        int iDispRowIndex = 5;

        Mockito.when(outGoingCorrDAO.getDisplayOutGoingCorrByCN(custNum,pastDays,searchType,sortBy,sortOrder,iDispRowIndex))
                .thenReturn(mockResult);
        HashMap expectedResult = outGoingCorrService.getDisplayOutGoingCorrByCN(custNum,pastDays,searchType,sortBy,sortOrder,iDispRowIndex);
        assertEquals(mockResult,expectedResult);
        verify(outGoingCorrDAO,times(1)).getDisplayOutGoingCorrByCN(custNum,pastDays,searchType,sortBy,sortOrder,iDispRowIndex);
    }

    @Test
    public void getDownloadOutGoingList() throws Exception {
        HashMap mockResult = new HashMap();
        String custNum = "CUS420315";
        String downloadBy = "pastDays";
        String multipleAppId = "{APP8234,APP0934}";
        int pastDays = 20;
        String searchType = "byCustNum";
        String sortBy = "custNum";
        String sortOrder = "custNum";

        Mockito.when(outGoingCorrDAO.getDownloadOutGoingList(downloadBy,custNum,searchType,pastDays,multipleAppId,sortBy,sortOrder))
                .thenReturn(mockResult);
        HashMap expectedResult = outGoingCorrService.getDownloadOutGoingList(downloadBy,custNum,searchType,pastDays,multipleAppId,sortBy,sortOrder);
        assertEquals(mockResult,expectedResult);
        verify(outGoingCorrDAO,times(1))
                .getDownloadOutGoingList(downloadBy,custNum,searchType,pastDays,multipleAppId,sortBy,sortOrder);
    }
}