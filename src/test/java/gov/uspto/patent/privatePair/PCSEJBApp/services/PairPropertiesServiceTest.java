package gov.uspto.patent.privatePair.PCSEJBApp.services;

import gov.uspto.patent.privatePair.PCSEJBApp.dao.PairPropertiesDAO;
import gov.uspto.patent.privatePair.utils.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PairPropertiesServiceTest {

    @InjectMocks
    PairPropertiesService propertiesService;

    @Mock
    PairPropertiesDAO propertiesDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getPairPropertiesData() throws Exception {
        HashMap mockResult = new HashMap();
        String mode = "Passive";
        String propertyName = "actualProperty";
        String sortBy = "custNum";
        String sortOrder = "desc";

        Mockito.when(propertiesDAO.getPairPropertiesData(mode,propertyName,sortBy,sortOrder))
                .thenReturn(mockResult);
        HashMap expectedResult = propertiesService.getPairPropertiesData(mode,propertyName,sortBy,sortOrder);
        assertEquals(mockResult,expectedResult);
        verify(propertiesDAO,times(1)).getPairPropertiesData(mode,propertyName,sortBy,sortOrder);

    }
}