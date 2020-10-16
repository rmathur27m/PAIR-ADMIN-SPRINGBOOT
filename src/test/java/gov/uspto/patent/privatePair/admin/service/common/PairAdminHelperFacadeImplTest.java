package gov.uspto.patent.privatePair.admin.service.common;

import gov.uspto.patent.privatePair.admin.dao.PairUserDnDao;
import gov.uspto.patent.privatePair.admin.dto.PairUserDnDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PairAdminHelperFacadeImplTest {

    @Mock
    PairUserDnDao pairUserDnDao;

    @Mock
    PairAdminHelperFacadeImpl helperFacade;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getPairUserDtoByDn() throws Exception {
        String testDn = "takeDn";
        PairUserDnDto testData = new PairUserDnDto();
        testData.setDn("testDn");
        testData.setCommonName("TestPairFacade");
        testData.setPairUserDnId("BamDamFam");

        when(pairUserDnDao.getPairUserDnByDn(testDn)).thenReturn(String.valueOf(testData));

        PairUserDnDto result = helperFacade.getPairUserDtoByDn(testDn);

        assertNull(result);

        verify(helperFacade, times(1)).getPairUserDtoByDn(testDn);

    }
}