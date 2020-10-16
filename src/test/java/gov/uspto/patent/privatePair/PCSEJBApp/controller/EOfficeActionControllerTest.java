package gov.uspto.patent.privatePair.PCSEJBApp.controller;

import gov.uspto.patent.privatePair.PCSEJBApp.services.EOfficeActionService;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = EOfficeActionController.class)
public class EOfficeActionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    EOfficeActionService eOfficeActionService;

    @Test
    public void getDisplayEOfficeActionByAppId() throws Exception{
        String url = "/getDisplayEOfficeActionByAppId";
        String appId = "dummyAppId";
        String sortBy = "appId";
        String sortOrder ="ASC";
        int dispRowIndex =10;
        String allOptedInCustNumbers = "testCustomers";

        HashMap mockResult = new HashMap();
        //given
        given(eOfficeActionService.getDisplayEOfficeActionByAppId(appId,sortBy,sortOrder,dispRowIndex,allOptedInCustNumbers))
                .willReturn(mockResult);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("appId", appId);
        requestParams.add("sortBy", sortBy);
        requestParams.add("sortOrder", sortOrder);
        requestParams.add("dispRowIndex", String.valueOf(dispRowIndex));
        requestParams.add("allOptedInCustNumbers", allOptedInCustNumbers);

        //when
        MockHttpServletResponse response = mockMvc.perform(get(url)
                .params(requestParams)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(String.valueOf(mockResult));
    }
}