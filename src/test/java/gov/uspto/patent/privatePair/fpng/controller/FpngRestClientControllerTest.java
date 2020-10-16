package gov.uspto.patent.privatePair.fpng.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.uspto.patent.privatePair.admin.controller.EntitySaveController;
import gov.uspto.patent.privatePair.fpng.dto.FpngServiceResponse;
import gov.uspto.patent.privatePair.fpng.service.FpngRestClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = FpngRestClientController.class)
public class FpngRestClientControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    FpngRestClientService mockFpngRestClientService;

    private final String testappOrPatentNumber = "randomTestNum1234";

    private final String FEE_PAY_RECS_BY_URL = "/getFeePaymentRecsByAppNum";

    private final String FEE_PAY_RECS_BY_URL_withParams = "/getFeePaymentRecsByAppNumParams";

    @Test
    public void getFeePaymentRecsByAppNum() throws Exception {
        FpngServiceResponse testResp = new FpngServiceResponse();
        testResp.setSuccess(true);
        testResp.setAdditionalProperty("flag", "yes");
        testResp.setErrorMessageText(new ArrayList<>());
        testResp.setInfoMessageText(new ArrayList<>());

        // given
        given(mockFpngRestClientService.getFeePaymentRecsByAppNum("testAppNumber"))
                .willReturn(testResp);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                (get(FEE_PAY_RECS_BY_URL)).param("appOrPatentNumber", testappOrPatentNumber)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("");
    }

    @Test
    public void getFeePaymentRecsByAppNumWithParams() throws Exception {
        FpngServiceResponse testResp = new FpngServiceResponse();
        testResp.setSuccess(true);
        testResp.setAdditionalProperty("flag", "yes");
        testResp.setErrorMessageText(new ArrayList<>());
        testResp.setInfoMessageText(new ArrayList<>());

        // given
        given(mockFpngRestClientService.getFeePaymentRecsByAppNum("testAppNumber",new Date().toString(),10))
                .willReturn(testResp);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                (get(FEE_PAY_RECS_BY_URL_withParams)).param("appOrPatentNumber", testappOrPatentNumber)
                        .param("issueDate", new Date().toString())
                        .param("patentFlag","10")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("");
    }

}