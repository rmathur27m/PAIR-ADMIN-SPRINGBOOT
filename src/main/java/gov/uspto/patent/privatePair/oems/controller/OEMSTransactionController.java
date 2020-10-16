package gov.uspto.patent.privatePair.oems.controller;


import com.google.gson.JsonSyntaxException;
import gov.uspto.patent.privatePair.oems.service.oems.OEMSTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OEMSTransactionController {

    private static final Logger logger = LoggerFactory.getLogger(OEMSTransactionController.class);

    private final OEMSTransactionService oemsTransactionService;

    @Autowired
    public OEMSTransactionController(OEMSTransactionService oemsTransactionService) {
        this.oemsTransactionService = oemsTransactionService;
    }

    @GetMapping(value = "/insertOemsTransactionData",params = {"transactionId", "documentId", "customerNumber"})
    public String getUpdateAddressList(@RequestParam("transactionId") String transId,@RequestParam("documentId") String docId,
                                       @RequestParam("customerNumber") int custNum) throws Exception {

        return oemsTransactionService.insertOemsTransactionData(transId,docId,custNum);
    }
}

