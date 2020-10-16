package gov.uspto.patent.privatePair.PCSEJBApp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import gov.uspto.patent.privatePair.PCSEJBApp.dto.CustomerVo;
import gov.uspto.patent.privatePair.PCSEJBApp.services.CustomerService;
import gov.uspto.patent.privatePair.admin.dto.SearchCriteriaVo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CustomerController.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CustomerControllerTest {

    @MockBean
    CustomerService customerService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void checkCustomer() throws Exception {
        String url = "/checkCustomer";
        int countResult = 15;
        //given
        given(customerService.checkCustomer("CUS524552"))
                .willReturn(countResult);

        //when
        MockHttpServletResponse response = mockMvc.perform(get(url).param("customerNumber", "CUS524552")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(String.valueOf(countResult));

    }

    @Test
    public void customerValidation() throws Exception {
        String url = "/customerValidation";
        int countResult = 10;
        //given
        given(customerService.customerValidation("CUS524552"))
                .willReturn(countResult);

        //when
        MockHttpServletResponse response = mockMvc.perform(get(url).param("customerNumber", "CUS524552")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(String.valueOf(countResult));
    }

    @Test
    public void saveCustomer() throws Exception {
        String url = "/saveCustomer";
        CustomerVo testCustData = new CustomerVo();
        testCustData.setPocName("Bob");
        //given
        given(customerService.saveCustomer(testCustData))
                .willReturn(0);

        //when
        MockHttpServletResponse response = mockMvc.perform(
                post(url).accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(testCustData)))
                        .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(String.valueOf(0));
    }


    @Test
    public void getCustomerHistoryList() throws Exception {
        String url = "/getCustomerHistoryList";
        CustomerVo testCustData = new CustomerVo();
        testCustData.setCustomerNumber("CUS125");
        testCustData.setPocName("Bob");

        List<CustomerVo> mockResult = new ArrayList<CustomerVo>();
        mockResult.add(testCustData);

        //given
        given(customerService.getCustomerHistoryList(testCustData.getCustomerNumber()))
                .willReturn(mockResult);

        //when
        MockHttpServletResponse response = mockMvc.perform(
                get(url).accept(MediaType.APPLICATION_JSON)
                        .param("customerNumber", testCustData.getCustomerNumber()))
                .andDo(print())
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isNotEmpty();

    }

    @Test
    public void getCustomersList() throws Exception {
        String url = "/getCustomersList";
        SearchCriteriaVo testSearchCriteria = new SearchCriteriaVo();
        testSearchCriteria.setCustomerNo("CUS258");

        List<CustomerVo> mockResult = new ArrayList<CustomerVo>();

        //given
        given(customerService.getCustomersList(testSearchCriteria))
                .willReturn(mockResult);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson=ow.writeValueAsString(testSearchCriteria );

        //when
        MockHttpServletResponse response = mockMvc.perform(
                get(url).accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andDo(print())
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isNotEmpty();
    }


    @Test
    public void updateCustomer() throws Exception {
        String url = "/updateCustomer";
        CustomerVo testCustData = new CustomerVo();
        testCustData.setCustomerNumber("CUS125");
        testCustData.setPocName("Bob");

        //given
        given(customerService.updateCustomer(testCustData))
                .willReturn(10);

        //when
        MockHttpServletResponse response = mockMvc.perform(
                post(url).accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new Gson().toJson(testCustData)))
                .andDo(print())
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("success");
    }
}