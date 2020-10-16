package gov.uspto.patent.privatePair.PCSCommon.controller;

import gov.uspto.patent.privatePair.PCSCommon.dto.UserDTO;
import gov.uspto.patent.privatePair.PCSCommon.service.AttorneyAsuService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Arrays;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AttorneyAsuController.class)
public class AttorneyAsuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AttorneyAsuService attorneyAsuService;

    @Test
    public void getUserMappingsByUserId() throws Exception {
        String url = "/getUserMappingsByUserId";
        //given
        given(attorneyAsuService.getUserMappingsByUserId("A457", "Id", "ASC"))
                .willReturn(Arrays.asList(new UserDTO()));

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("userId", "A457");
        requestParams.add("sortBy", "docId");
        requestParams.add("sortOrder", "ASC");

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
    public void getAttorneyAsuByDN() throws Exception {
        String url = "/getAttorneyAsuByDN";
        //given
        given(attorneyAsuService.getAttorneyAsuByDN("test_pdn", "Sensitive_check", "docId", "ASC"))
                .willReturn(new HashMap<>());

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("p_dn", "A457");
        requestParams.add("CASE_SENSITIVE_CHECK", "sensitive_check");
        requestParams.add("sortBy", "docId");
        requestParams.add("sortOrder", "ASC");

        //when
        MockHttpServletResponse response = mockMvc.perform(get(url)
                .params(requestParams)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("{}");

    }


}