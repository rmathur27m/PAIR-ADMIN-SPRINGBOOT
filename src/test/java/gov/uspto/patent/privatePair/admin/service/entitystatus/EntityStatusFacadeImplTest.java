package gov.uspto.patent.privatePair.admin.service.entitystatus;

import com.google.gson.Gson;
import gov.uspto.patent.privatePair.admin.controller.EntitySaveController;
import gov.uspto.patent.privatePair.admin.controller.PrivatePairCommonController;
import gov.uspto.patent.privatePair.admin.domain.EditEntityStatus;
import gov.uspto.utils.Statistics;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class EntityStatusFacadeImplTest {

    @Mock
    EntityStatusServices entityStatusServices;
    @Mock
    MockHttpServletRequest mockHttpServletRequest;
    @Mock
    PrivatePairCommonController pairCommonController;
    @Mock
    Statistics statistics;
    @Mock
    private EntitySaveController entitySaveController;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(entitySaveController, pairCommonController).build();
        MockitoAnnotations.initMocks(this);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSaveEntityStatusWithMultipleJointInventorsSigningJointly() throws Exception {

        // Create the pay load for the request body
        JSONObject parentObject = new JSONObject();

        JSONArray signatureArray = new JSONArray();

        JSONObject signatureObject1 = new JSONObject();
        signatureObject1.put("signature", "/Tuan A. Nguyen/");
        signatureArray.add(signatureObject1);

        JSONObject signatureObject2 = new JSONObject();
        signatureObject2.put("signature", "/Mahesh B. Rajeshwaraiah/");
        signatureArray.add(signatureObject2);

        parentObject.put("signatureNameArray", signatureArray);
        parentObject.put("title", "WIRELESS STATION WIRELESS TRANSMISSION METHOD");
        parentObject.put("attorneyDocketNumber", "2008_1321A");
        parentObject.put("customerNumber", "-");
        parentObject.put("currentBusinessEntityCode", "Undiscounted");
        parentObject.put("statusType", "MicroEntityStatus");
        parentObject.put("microEntityStatusOption", "GrossIncomeBasis");
        parentObject.put("grossIncomeBasisCert", "true");
        parentObject.put("inventorTypeOption", "Several Joint Inventors");
        parentObject.put("pocName", "jin");
        parentObject.put("phoneNumber", "74635");
        parentObject.put("emailAddress", "test@testMailServer.com");
        parentObject.put("userType", "Independent Inventor");

        // Define expected behavior of the controller
        EditEntityStatus editEntityStatus = new EditEntityStatus();
        editEntityStatus.setCreatedTs(new Date());
        Mockito.when(entityStatusServices.saveEntityStatus(Mockito.any(),
                Mockito.argThat(new IsDistinguishedName()))).thenReturn(editEntityStatus);

        // Invoke the controller
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/saveRequest")
                .sessionAttr("distinguishedName", "cn=Peter  Test Kauslick, ou=Independent Inventors, ou=Patent and Trademark Office, ou=Department of Commerce, o=U.S. Government, c=US")
                .sessionAttr("applicationNumber", "12162013")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new Gson().toJson(parentObject)))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        assertNotNull("Expected successful save message, but there was none returned", content);

        statistics.addPairAdminActivity(Mockito.anyString(), Mockito.argThat(new IsDistinguishedName()), Mockito.anyInt(), Mockito.anyString(),
                Mockito.anyString(), Mockito.anyString(), Mockito.anyLong(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyString());
    }


    class IsDistinguishedName implements ArgumentMatcher<String> {
        @Override
        public boolean matches(String argument) {
            return "cn=Peter  Test Kauslick, ou=Independent Inventors, ou=Patent and Trademark Office, ou=Department of Commerce, o=U.S. Government, c=US".
                    equalsIgnoreCase((String) argument);
        }
    }

}