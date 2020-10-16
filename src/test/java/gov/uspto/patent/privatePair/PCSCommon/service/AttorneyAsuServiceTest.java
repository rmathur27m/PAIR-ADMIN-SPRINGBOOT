package gov.uspto.patent.privatePair.PCSCommon.service;

import gov.uspto.patent.privatePair.PCSCommon.dao.AttorneyAsuDAO;
import gov.uspto.patent.privatePair.PCSCommon.dto.PcsCommonConstant;
import gov.uspto.patent.privatePair.PCSCommon.dto.UserDTO;
import gov.uspto.patent.privatePair.utils.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AttorneyAsuServiceTest {

    @InjectMocks
    AttorneyAsuService attorneyAsuService;

    @Mock
    AttorneyAsuDAO attorneyAsuDAO;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void getUserMappingsByUserId() throws ServiceException, SQLException {
        UserDTO a = new UserDTO();
        a.setUserId("A017465");
        a.setUserType("CasualTest");
        a.setUserCommonName("commonNameTest");
        a.setUserDn("userDnTest");
        a.setUserGivenName("TestName");
        a.setUserFamilyName("TestFamily");
        a.setUserStatus("Active");
        List<UserDTO> userMappingList = new ArrayList<UserDTO>();
        userMappingList.add(a);

       when(attorneyAsuDAO.getUserMappingsByUserId(a.getUserId(),a.getUserType(),"ASC")).thenReturn(userMappingList);

       List<UserDTO> expectedResult = attorneyAsuService.getUserMappingsByUserId("A017465",a.getUserType(),"ASC");
       assertEquals(1,expectedResult.size());
       verify(attorneyAsuDAO, times(1)).getUserMappingsByUserId(a.getUserId(),a.getUserType(),"ASC");
    }

    @Test
    public void getAttorneyAsuByDN() throws ServiceException, SQLException {
        UserDTO a = new UserDTO();
        a.setUserId("A017465");
        a.setUserType("CasualTest");
        a.setUserCommonName("commonNameTest");
        a.setUserDn("userDnTest");
        a.setUserGivenName("TestName");
        a.setUserFamilyName("TestFamily");
        a.setUserStatus("Active");
        List<UserDTO> userMappingList = new ArrayList<UserDTO>();
        userMappingList.add(a);

        HashMap<Object, Object> testData = new HashMap<>();
        testData.put("MAPPINGS_LIST",userMappingList);
        testData.put(PcsCommonConstant.STATUS, PcsCommonConstant.SUCCESS);
        testData.put(PcsCommonConstant.STATUS_MESSAGE, PcsCommonConstant.SUCCESS);

        when(attorneyAsuDAO.getAttorneyAsuByDN(a.getUserId(),"CASE_SENSITIVE_CHECK",a.getUserType(),"ASC")).thenReturn(testData);
        HashMap<Object,Object> expectResult = attorneyAsuService.getAttorneyAsuByDN(a.getUserId(),"CASE_SENSITIVE_CHECK",a.getUserType(),"ASC");

        assertEquals(testData.get("MAPPINGS_LIST"),expectResult.get("MAPPINGS_LIST"));
        verify(attorneyAsuDAO, times(1)).getAttorneyAsuByDN(a.getUserId(),"CASE_SENSITIVE_CHECK",a.getUserType(),"ASC");
    }

}