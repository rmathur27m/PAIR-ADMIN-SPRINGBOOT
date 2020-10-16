package gov.uspto.patent.privatePair.PCSEJBApp.services;

import gov.uspto.patent.privatePair.PCSEJBApp.dao.PCSSupportDAO;
import gov.uspto.patent.privatePair.PCSEJBApp.dto.EofficeNotificationStatusCheckVO;
import gov.uspto.patent.privatePair.PCSEJBApp.dto.OCNArchiveSearchResultVO;
import gov.uspto.patent.privatePair.utils.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PCSSupportServiceTest {

    @InjectMocks
    PCSSupportService supportService;

    @Mock
    PCSSupportDAO supportDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void validateUser() throws Exception {
        final String userName = "testCustNum";
        Mockito.when(supportDAO.validateUser(userName)).thenReturn(2);
        int expectedResult = supportService.validateUser(userName);
        assertEquals(2,expectedResult);
        verify(supportDAO, times(1)).validateUser(userName);
    }

  /*  @Test
    public void setSystemAnnouncement() throws SQLException, ServiceException, ClassNotFoundException {
        String message = "fakeMsg";
        final String msg = "testCustNum";
        Mockito.when(supportDAO.setSystemAnnouncement(message,msg)).thenReturn(2);
        int expectedResult = supportService.setSystemAnnouncement(message,msg);
        assertEquals(2,expectedResult);
        verify(supportDAO, times(1)).setSystemAnnouncement(message,msg);
    }*/

    @Test
    public void getSystemAnnouncement() throws SQLException, ServiceException, ClassNotFoundException {
        String[] message = new String[]{"fakeMsg","dummyMsg"};
        final String msg = "testCustNum";
        Mockito.when(supportDAO.getSystemAnnouncement(msg)).thenReturn(message);
        String[] expectedResult = supportService.getSystemAnnouncement(msg);
        assertEquals(message,expectedResult);
        verify(supportDAO, times(1)).getSystemAnnouncement(msg);
    }

    @Test
    public void pcsSupportSearchOCNRows() throws SQLException, ServiceException, ClassNotFoundException {
        List<OCNArchiveSearchResultVO> mockResult = new ArrayList<OCNArchiveSearchResultVO>();
        Map<String,String> searchParams = new HashMap<>();

        Mockito.when(supportDAO.pcsSupportSearchOCNRows(searchParams)).thenReturn(mockResult);
        List<OCNArchiveSearchResultVO> expectedResult = supportService.pcsSupportSearchOCNRows(searchParams);
        assertEquals(mockResult,expectedResult);
        verify(supportDAO, times(1)).pcsSupportSearchOCNRows(searchParams);
    }

    @Test
    public void pcsSupportSearchEnotifications() throws SQLException, ServiceException, ClassNotFoundException {
        List<EofficeNotificationStatusCheckVO> mockResult = new ArrayList<>();
        Map<String,String> searchParams = new HashMap<>();

        Mockito.when(supportDAO.pcsSupportSearchEnotifications(searchParams)).thenReturn(mockResult);
        List<EofficeNotificationStatusCheckVO> expectedResult = supportService.pcsSupportSearchEnotifications(searchParams);
        assertEquals(mockResult,expectedResult);
        verify(supportDAO, times(1)).pcsSupportSearchEnotifications(searchParams);
    }
}