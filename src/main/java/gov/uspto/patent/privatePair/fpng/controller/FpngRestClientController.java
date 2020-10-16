package gov.uspto.patent.privatePair.fpng.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import gov.uspto.patent.privatePair.fpng.dto.FpngServiceResponse;
import gov.uspto.patent.privatePair.fpng.service.FpngRestClientService;

@RestController
public class FpngRestClientController {

	@Autowired
	FpngRestClientService fpngRestClientService;

	/*
	 * URL - http://localhost:8080/getFeePaymentRecsByAppNum?appOrPatentNumber=?
	 */
	@GetMapping(value = "/getFeePaymentRecsByAppNum",params = {"appOrPatentNumber"})
	public FpngServiceResponse getFeePaymentRecsByAppNum(@RequestParam(name="appOrPatentNumber") String appOrPatentNumber) {

		return fpngRestClientService.getFeePaymentRecsByAppNum(appOrPatentNumber);
	}

	/*
	 * URL - http://localhost:8080/getFeePaymentRecsByAppNumParams?appOrPatentNumber=?&issueDate=?&patentFlag=1
	 */
	@GetMapping(value = "/getFeePaymentRecsByAppNumParams", params = {"appOrPatentNumber","issueDate","patentFlag"})
	public FpngServiceResponse getFeePaymentRecsByAppNum(@RequestParam(name="appOrPatentNumber") String appOrPatentNumber,
														 @RequestParam(name="issueDate" )  String issueDate,
														 @RequestParam(name="patentFlag") int patentFlag) {

		return fpngRestClientService.getFeePaymentRecsByAppNum(appOrPatentNumber, issueDate, patentFlag);
	}
}
