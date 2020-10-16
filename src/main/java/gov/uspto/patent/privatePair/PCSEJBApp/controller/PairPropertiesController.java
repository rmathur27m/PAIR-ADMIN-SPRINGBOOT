package gov.uspto.patent.privatePair.PCSEJBApp.controller;


import gov.uspto.patent.privatePair.PCSEJBApp.dao.PairPropertiesDAO;
import gov.uspto.patent.privatePair.PCSEJBApp.services.PairPropertiesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@Slf4j
public class PairPropertiesController {

	@Autowired
    PairPropertiesDAO pairPropertiesDAO;
	
    @Autowired
    PairPropertiesService pairPropertiesService;

    //http://localhost:8080/getPairPropertiesData?customerNumber=1518
    @GetMapping(value = "/getPairPropertiesData")
    public HashMap getPairPropertiesData(@RequestParam(name="mode", required = true) String mode,
                                         @RequestParam(name="propertyName", required = true) String propertyName,
                                         @RequestParam(name="sortBy", required = true) String sortBy,
                                         @RequestParam(name="sortOrder", required = true) String sortOrder)throws Exception {
        log.debug("inside getPairPropertiesData controller--->"+ mode);

        return pairPropertiesService.getPairPropertiesData( mode,  propertyName,  sortBy,  sortOrder);
    }
}
