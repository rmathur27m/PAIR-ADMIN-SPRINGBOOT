package gov.uspto.patent.privatePair.PCSEJBApp.controller;

import gov.uspto.patent.privatePair.PCSEJBApp.services.EOfficeActionService;
import gov.uspto.patent.privatePair.utils.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.HashMap;

@RestController
@Slf4j
public class EOfficeActionController {

	@Autowired
    EOfficeActionService eOfficeActionService;

	 //http://localhost:8080/getDisplayEOfficeActionByAppId?customerNumber=1518
    @GetMapping(value = "/getDisplayEOfficeActionByAppId")
    public HashMap getDisplayEOfficeActionByAppId(@RequestParam(name="appId", required = true) String appId,
                                         @RequestParam(name="sortBy", required = true) String sortBy,
                                         @RequestParam(name="sortOrder", required = true) String sortOrder,
                                         @RequestParam(name="dispRowIndex", required = true) int dispRowIndex,
                                         @RequestParam(name="allOptedInCustNumbers", required = true) String allOptedInCustNumbers)throws Exception {
        log.debug("inside getDisplayEOfficeActionByAppId controller--->"+ appId);

        return eOfficeActionService.getDisplayEOfficeActionByAppId(appId,sortBy, sortOrder, dispRowIndex, allOptedInCustNumbers);
    }
    
    //http://localhost:8080/getDisplayEOfficeActionByCn?custNum=22850&startDate='12-APR-20'&endDate='10-MAY-20'&sortBy=1&sortOrder=ASC&dispRowIndex=1 
    @GetMapping(value = "/getDisplayEOfficeActionByCn")
    public HashMap getDisplayEOfficeActionByCn(@RequestParam(name="custNum", required = true) String custNum,
                                         @RequestParam(name="startDate", required = true) String startDate,
                                         @RequestParam(name="endDate", required = false) String endDate,
                                         @RequestParam(name="sortBy", required = true) String sortBy,
                                         @RequestParam(name="sortOrder", required = true) String sortOrder,
                                         @RequestParam(name="dispRowIndex", required = true) int dispRowIndex) throws Exception {
        log.debug("inside getDisplayEOfficeActionByCn controller--->"+ custNum);
        
        return eOfficeActionService.getDisplayEOfficeActionByCn(custNum, startDate, endDate, sortBy, sortOrder, dispRowIndex);
    }
}

