package gov.uspto.patent.privatePair.PCSCommon.controller;

import gov.uspto.patent.privatePair.PCSCommon.service.CustomerDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;

import java.util.ArrayList;
import java.util.Vector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CustomerDetailsController.class)
public class CustomerDetailsControllerTest {

    @MockBean
    CustomerDetailsService customerDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getRegistrationNumberCustomer() throws Exception {
        String url = "/getRegistrationNumberCustomer";

        //given
        given(customerDetailsService.getRegistrationNumber("testDn"))
                .willReturn(new ArrayList<String>());

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("dn", "testDn");

        //when
        MockHttpServletResponse response = mockMvc.perform(get(url)
                .params(requestParams)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }

    @Test
    public void getCustRowsByDN() throws Exception {
        String url = "/getCustRowsByDN";

        //given
        given(customerDetailsService.getCustRowsByDN("testDn", "SENSITVITY_CHECK"))
                .willReturn(new Vector());

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("p_dn", "testDn");
        requestParams.add("CASE_SENSITIVE_CHECK", "sensitivity_check");

        //when
        MockHttpServletResponse response = mockMvc.perform(get(url)
                .params(requestParams)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("");
    }
}