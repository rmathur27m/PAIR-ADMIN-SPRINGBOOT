package gov.uspto.patent.privatePair.PCSEJBApp.controller;

import gov.uspto.patent.privatePair.PCSEJBApp.services.OutGoingCorrService;
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

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = OutGoingCorrController.class)
public class OutGoingCorrControllerTest {


    @MockBean
    OutGoingCorrService outGoingCorrService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getDisplayOutGoingCorrByCN() throws Exception {
        String url = "/getDisplayOutGoingCorrByCN";
        HashMap mockResult = new HashMap();
        String custNum = "CUS420315";
        int pastDays = 20;
        String searchType = "byCustNum";
        String sortBy = "custNum";
        String sortOrder = "custNum";
        int iDispRowIndex = 5;

        //given
        given(outGoingCorrService.getDisplayOutGoingCorrByCN(custNum,pastDays,searchType,sortBy,sortOrder,iDispRowIndex))
                .willReturn(mockResult);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("custNum", custNum);
        requestParams.add("pastDays", String.valueOf(pastDays));
        requestParams.add("searchType", searchType);
        requestParams.add("sortBy", sortBy);
        requestParams.add("sortOrder", sortOrder);
        requestParams.add("iDispRowIndex", String.valueOf(iDispRowIndex));

        //when
        MockHttpServletResponse response = mockMvc.perform(get(url).params(requestParams)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(String.valueOf(mockResult));
    }

    @Test
    public void updateOCNDocketedAction() throws Exception {
        String url = "/updateOCNDocketedAction";
        //given
        given(outGoingCorrService.updateOCNDocketedAction("testDocket", "A78545"))
                .willReturn("mockResultUpdated");

        //when
        MockHttpServletResponse response = mockMvc.perform(post(url).param("docketed", "testDocket")
                .param("daId", "A78545")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(String.valueOf("mockResultUpdated"));
    }

    @Test
    public void getDownloadOutGoingList() throws Exception {
        String url = "/getDownloadOutGoingList";
        HashMap mockResult = new HashMap();
        String custNum = "CUS420315";
        String downloadBy = "pastDays";
        String multipleAppId = "{APP8234,APP0934}";
        int pastDays = 20;
        String searchType = "byCustNum";
        String sortBy = "custNum";
        String sortOrder = "custNum";

        //given
        given(outGoingCorrService.getDownloadOutGoingList(downloadBy,custNum,searchType,pastDays,multipleAppId,sortBy,sortOrder))
                .willReturn(mockResult);

        LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
        requestParams.add("custNum", custNum);
        requestParams.add("downloadBy", custNum);
        requestParams.add("multipleAppId", custNum);
        requestParams.add("pastDays", String.valueOf(pastDays));
        requestParams.add("searchType", searchType);
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