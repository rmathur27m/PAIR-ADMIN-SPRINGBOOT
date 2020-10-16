package gov.uspto.patent.privatePair.PCSEJBApp.controller;

import gov.uspto.patent.privatePair.PCSEJBApp.dao.PairPropertiesDAO;
import gov.uspto.patent.privatePair.PCSEJBApp.services.PairPropertiesService;
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
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PairPropertiesController.class)
public class PairPropertiesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    PairPropertiesService propertiesService;

    @MockBean
    PairPropertiesDAO propertiesDAO;

    @Test
    void getPairPropertiesData() throws Exception {
        String url = "/getPairPropertiesData";
        HashMap mockResult = new HashMap();
        String mode = "Passive";
        String propertyName = "actualProperty";
        String sortBy = "custNum";
        String sortOrder = "desc";

        //given
        given(propertiesService.getPairPropertiesData(mode,propertyName,sortBy,sortOrder))
                .willReturn(mockResult);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("mode", mode);
        requestParams.add("propertyName", propertyName);
        requestParams.add("sortBy", sortBy);
        requestParams.add("sortOrder", sortOrder);

        //when
        MockHttpServletResponse response = mockMvc.perform(get(url).params(requestParams)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(String.valueOf(mockResult));
    }
}