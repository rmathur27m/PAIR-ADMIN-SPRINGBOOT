package gov.uspto.patent.privatePair.PCSCommon.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import gov.uspto.patent.privatePair.PCSCommon.service.CustomerDetailsService;

@RestController
@Slf4j
public class CustomerDetailsController {


	@Autowired
	CustomerDetailsService customerDetailsService;

	@GetMapping(value = "/getRegistrationNumberCustomer")
	public List<String> getRegistrationNumberCustomer(@RequestParam(name="dn", required = true) String dn){
		log.info("Inside AttorneyAsuService:");

		return customerDetailsService.getRegistrationNumber(dn);
	}

	@GetMapping(value = "/getCustRowsByDN")
	public Vector getCustRowsByDN(@RequestParam(name="p_dn", required = true) String p_dn,
								  @RequestParam(name="CASE_SENSITIVE_CHECK", required = true) String CASE_SENSITIVE_CHECK){
		log.info("Inside AttorneyAsuService:");

		return customerDetailsService.getCustRowsByDN(p_dn, CASE_SENSITIVE_CHECK);
	}
}
