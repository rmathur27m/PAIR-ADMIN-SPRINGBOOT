package gov.uspto.patent.privatePair.PCSCommon.service;

import gov.uspto.patent.privatePair.PCSCommon.dao.CustomerDetailsDAO;
import gov.uspto.patent.privatePair.oems.dto.OEMSDTO;
import gov.uspto.patent.privatePair.utils.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CustomerDetailsServiceTest {

    @InjectMocks
    CustomerDetailsService customerDetailsService;

    @Mock
    CustomerDetailsDAO customerDetailsDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getRegistrationNumber() throws Exception {
        final String pair_user_dn = "testDataNum";
        List<String> regNUM = new ArrayList<String>();
        regNUM.add("A4369");
        regNUM.add("B5454");

        Mockito.when(customerDetailsDAO.getRegistrationNumber(pair_user_dn)).thenReturn(regNUM);

        List<String> expectedResult = customerDetailsService.getRegistrationNumber(pair_user_dn);
        assertEquals(2,expectedResult.size());
        verify(customerDetailsDAO, times(1)).getRegistrationNumber(pair_user_dn);
    }

    @Test
    public void getCustRowsByDN() throws ServiceException, SQLException {
        final String pair_user_dn = "testDataNum";
        final String sensitivity_check = "testCheck";
        Vector userMappingList = new Vector();

        Mockito.when(customerDetailsDAO.getCustRowsByDN(pair_user_dn,sensitivity_check)).thenReturn(userMappingList);

        List<String> expectedResult = customerDetailsService.getRegistrationNumber(pair_user_dn);
        assertEquals(0,expectedResult.size());
    }
}