package gov.uspto.patent.privatePair.oems.controller;

import gov.uspto.patent.privatePair.oems.service.oems.OEMSTransactionService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = OEMSTransactionController.class)
public class OEMSTransactionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OEMSTransactionService mockOEMSService;

    @Test
    public void test_should_getUpdateAddressList() throws Exception {
        String url= "/insertOemsTransactionData";
        //given
        given(mockOEMSService.insertOemsTransactionData("A457","DOC65",58321))
                .willReturn("10");

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("transactionId", "A457");
        requestParams.add("documentId", "DOC65");
        requestParams.add("customerNumber", "30");

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