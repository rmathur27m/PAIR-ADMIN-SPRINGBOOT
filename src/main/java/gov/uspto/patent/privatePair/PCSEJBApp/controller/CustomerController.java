package gov.uspto.patent.privatePair.PCSEJBApp.controller;

import gov.uspto.patent.privatePair.PCSEJBApp.dto.CustomerVo;
import gov.uspto.patent.privatePair.PCSEJBApp.services.CustomerService;
import gov.uspto.patent.privatePair.admin.dto.SearchCriteriaVo;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestController
@Slf4j
public class CustomerController {

    @Autowired
    CustomerService customerService;

    //http://localhost:8080/checkCustomer?customerNumber=42009
    @GetMapping(value = "/checkCustomer")
    public Integer checkCustomer(@RequestParam(name = "customerNumber", required = true) String customerNumber)
            throws Exception {
        log.info("inside checkCustomer controller--->" + customerNumber);

        return customerService.checkCustomer(customerNumber);
    }

    //http://localhost:8080/customerValidation?customerNumber=1518
    @GetMapping(value = "/customerValidation")
    public Integer customerValidation(@RequestParam(name = "customerNumber", required = true) String customerNumber)
            throws Exception {
        log.info("inside customerValidation controller--->" + customerNumber);

        return customerService.customerValidation(customerNumber);
    }

    //http://localhost:8080/getCustomersList?customerNumber=42009
    @GetMapping(value = "/getCustomerHistoryList")
    public List<CustomerVo> getCustomerHistoryList(@RequestParam(name = "customerNumber", required = true) String customerNumber)
            throws Exception {
        log.info("inside getCustomersList controller--->");

        return customerService.getCustomerHistoryList(customerNumber);
    }

    //http://localhost:8080/getCustomersList?customerNumber=42009
    @PostMapping(value = "/getCustomersList", produces="application/json")
    public List<CustomerVo> getCustomersList(@RequestBody SearchCriteriaVo searchCriteria, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        log.info("inside getCustomersList controller--->" + searchCriteria);

        return customerService.getCustomersList(searchCriteria);
    }

    //http://localhost:8080/saveCustomer?customerVO=customerVOObject
    @PostMapping(value = "/saveCustomer")
    public Integer saveCustomer(@RequestBody CustomerVo customerVO)
            throws Exception {
        log.info("inside saveCustomer controller--->" + customerVO);

        return customerService.saveCustomer(customerVO);
    }

    //http://localhost:8080/saveCustomer?customerVO=customerVOObject
    @PostMapping(path = "/updateCustomer")
    public String updateCustomer(@RequestBody CustomerVo customerVO)
            throws Exception {
        log.info("inside updateCustomer controller--->" + customerVO);

        int count = customerService.updateCustomer(customerVO);

        return count > 0 ? "success" : "fail";
    }
}
