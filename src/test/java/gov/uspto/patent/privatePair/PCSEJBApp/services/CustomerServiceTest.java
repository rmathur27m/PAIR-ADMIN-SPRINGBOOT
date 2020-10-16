package gov.uspto.patent.privatePair.PCSEJBApp.services;

import gov.uspto.patent.privatePair.PCSEJBApp.dao.CustomerDao;
import gov.uspto.patent.privatePair.PCSEJBApp.dto.CustomerVo;
import gov.uspto.patent.privatePair.admin.dto.SearchCriteriaVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CustomerServiceTest {

    @InjectMocks
    CustomerService customerService;

    @Mock
    CustomerDao customerDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void customerValidation() throws Exception {
        final String custNum = "testDataNum";
        Mockito.when(customerDao.customerValidation(custNum)).thenReturn(2);
        int expectedResult = customerService.customerValidation(custNum);
        assertEquals(2,expectedResult);
        verify(customerDao, times(1)).customerValidation(custNum);
    }

    @Test
    public void checkCustomer() throws Exception {
        final String custNum = "testDataNum";
        Mockito.when(customerDao.customerValidation(custNum)).thenReturn(2);
        int expectedResult = customerService.customerValidation(custNum);
        assertEquals(2,expectedResult);
        verify(customerDao, times(1)).customerValidation(custNum);

    }

    @Test
    public void saveCustomer() throws Exception{
        final CustomerVo testCustomerVo = new CustomerVo();
        testCustomerVo.setPocName("testCall");
        testCustomerVo.setPilotCustomerId("A0154");

        Mockito.when(customerDao.saveCustomer(testCustomerVo)).thenReturn(5);
        int expectedResult = customerService.saveCustomer(testCustomerVo);
        assertEquals(5,expectedResult);
        verify(customerDao,times(1)).saveCustomer(testCustomerVo);
    }

    @Test
    public void updateCustomer() throws Exception {
        final CustomerVo testCustomerVo = new CustomerVo();
        testCustomerVo.setPocName("testCall");
        testCustomerVo.setPilotCustomerId("A0154");

        Mockito.when(customerDao.updateCustomer(testCustomerVo)).thenReturn(10);
        int expectedResult = customerService.updateCustomer(testCustomerVo);
        assertEquals(10,expectedResult);
        verify(customerDao,times(1)).updateCustomer(testCustomerVo);
    }

    @Test
    public void getCustomersList() throws Exception{
        final SearchCriteriaVo testSearchCriteria = new SearchCriteriaVo();
        testSearchCriteria.setCustomerNo("CUS2584");
        testSearchCriteria.setSortOrder("ASC");

        Mockito.when(customerDao.getCustomersList(testSearchCriteria)).thenReturn(new ArrayList<CustomerVo>());
        List<CustomerVo> expectedResult = customerService.getCustomersList(testSearchCriteria);
        assertEquals(0,expectedResult.size());
        verify(customerDao,times(1)).getCustomersList(testSearchCriteria);
    }

    @Test
    public void getCustomerHistoryList() throws Exception {
        final String customerNo = "CUS2589";

        Mockito.when(customerDao.getCustomerHistoryList(customerNo)).thenReturn(new ArrayList<CustomerVo>());
        List<CustomerVo> expectedResult = customerService.getCustomerHistoryList(customerNo);
        assertEquals(0,expectedResult.size());
        verify(customerDao,times(1)).getCustomerHistoryList(customerNo);

    }
}