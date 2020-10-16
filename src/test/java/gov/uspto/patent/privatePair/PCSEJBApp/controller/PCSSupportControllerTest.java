package gov.uspto.patent.privatePair.PCSEJBApp.controller;

import gov.uspto.patent.privatePair.PCSEJBApp.dto.OCNArchiveSearchResultVO;
import gov.uspto.patent.privatePair.PCSEJBApp.services.PCSSupportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;

import java.io.UnsupportedEncodingException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PCSSupportController.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PCSSupportControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PCSSupportService supportService;


    @Test
    public void validateUser() throws Exception {
        String url = "/validateUser";

        //given
        given(supportService.validateUser("testUser"))
                .willReturn(10);

        //when
        MockHttpServletResponse response = mockMvc.perform(get(url).param("userName","testUser")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(String.valueOf(10));
    }

/*This particular implementation has some does not have specific logic what to achieve, feels like there is something wrong in
*      passing the request body parameters for the post method
    @Test
    public void setSystemAnnouncement() throws Exception {
        String url = "/setSystemAnnouncement";

        HashMap requestMap = new HashMap();
        requestMap.put("msg","Success");
        requestMap.put("message","Success");
        String[] message=new String[1];
        message[0]=requestMap.get("message").toString();

        //given
        given(supportService.setSystemAnnouncement(message,"success"))
                .willReturn(10);

//        LinkedMultiValueMap<String, Object> requestParams = new LinkedMultiValueMap<>();
//        requestParams.add("map", requestMap);

        //when
        MockHttpServletResponse response = mockMvc.perform(post(url).param("map",String.valueOf(requestMap))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(String.valueOf(10));
    }
 */

    @Test
    public void getSystemAnnouncement() throws Exception {
        String url = "/getSystemAnnouncement";

        //given
        given(supportService.getSystemAnnouncement("fakeMessage"))
                .willReturn(new String[]{""});

        //when
        MockHttpServletResponse response = mockMvc.perform(get(url).param("msg","Success")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("");

    }

    @Test
    public void pcsSupportSearchOCNRows() throws Exception {
        String url = "/pcsSupportSearchOCNRows";
        Map<String,String> searchParams = new HashMap<>();

        //given
        given(supportService.pcsSupportSearchOCNRows(searchParams))
                .willReturn(new ArrayList<OCNArchiveSearchResultVO>());

        //when
        MockHttpServletResponse response = mockMvc.perform(get(url).param("searchParams",String.valueOf(searchParams))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }

    @Test
    public void pcsSupportSearchEnotifications() throws Exception {
        String url = "/pcsSupportSearchEnotifications";
        Map<String,String> searchParams = new HashMap<>();
        List<OCNArchiveSearchResultVO> mockResult = new ArrayList<OCNArchiveSearchResultVO>();
        //given
        given(supportService.pcsSupportSearchOCNRows(searchParams))
                .willReturn(mockResult);

        //when
        MockHttpServletResponse response = mockMvc.perform(get(url).param("searchParams",String.valueOf(searchParams))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn().getResponse();

        //then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertNotNull(response.getContentAsString());
    }
}