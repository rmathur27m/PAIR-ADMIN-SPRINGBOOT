package gov.uspto.patent.privatePair.PCSCommon.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import gov.uspto.patent.privatePair.PCSCommon.dto.UserDTO;
import gov.uspto.patent.privatePair.PCSCommon.service.AttorneyAsuService;

@RestController
public class AttorneyAsuController {

    private static final Logger log = LoggerFactory.getLogger(AttorneyAsuController.class);

    @Autowired
    AttorneyAsuService attorneyAsuService;

    @GetMapping(value = "/getUserMappingsByUserId")
    public List<UserDTO> getUserMappingsByUserId(@RequestParam(name = "userId", required = true) String userId,
                                                 @RequestParam(name = "sortBy", required = true) String sortBy,
                                                 @RequestParam(name = "sortOrder", required = true) String sortOrder) {
        log.info("Inside AttorneyAsuService:");

        return attorneyAsuService.getUserMappingsByUserId(userId, sortBy, sortOrder);
    }

    @GetMapping(value = "/getAttorneyAsuByDN")
    public HashMap getAttorneyAsuByDN(@RequestParam(name = "p_dn", required = true) String p_dn,
                                      @RequestParam(name = "CASE_SENSITIVE_CHECK", required = true) String CASE_SENSITIVE_CHECK,
                                      @RequestParam(name = "sortBy", required = true) String sortBy,
                                      @RequestParam(name = "sortOrder", required = true) String sortOrder) {
        log.info("Inside AttorneyAsuService:");

        return attorneyAsuService.getAttorneyAsuByDN(p_dn, CASE_SENSITIVE_CHECK, sortBy, sortOrder);
    }
}