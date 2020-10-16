package gov.uspto.patent.privatePair.oems.service.oems;

import gov.uspto.patent.privatePair.oems.dao.OemsTransactionDAO;
import gov.uspto.patent.privatePair.oems.dto.OEMSDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBufferUtils;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OEMSTransactionServiceTest {

    @InjectMocks
    OEMSTransactionService oemsTransactionService;

    @Mock
    OemsTransactionDAO oemsTransactionDAO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void insertOemsTransactionData() throws Exception {
        OEMSDTO oemsdto = new OEMSDTO();
        oemsdto.setTransactionId("A78455");
        oemsdto.setCreatedDate(new Date(System.currentTimeMillis()));
        oemsdto.setCustomerNO("101301");
        oemsdto.setDocumentId("789");
        oemsdto.setLastModifiedUser("PPAIR");
        oemsdto.setValidationCD("DONE");
        List<OEMSDTO> listOems = new ArrayList<OEMSDTO>();
        listOems.add(oemsdto);

        when(oemsTransactionDAO.insertOemsTransactionData(oemsdto.getTransactionId(),oemsdto.getDocumentId(),
                Integer.parseInt(oemsdto.getCustomerNO()))).thenReturn(10);

        String result = oemsTransactionService.insertOemsTransactionData(oemsdto.getTransactionId(),oemsdto.getDocumentId(),
                Integer.parseInt(oemsdto.getCustomerNO()));

        assertEquals(10,Integer.parseInt(result));

        verify(oemsTransactionDAO, times(1)).insertOemsTransactionData(oemsdto.getTransactionId(),oemsdto.getDocumentId(),
                Integer.parseInt(oemsdto.getCustomerNO()));

    }
}