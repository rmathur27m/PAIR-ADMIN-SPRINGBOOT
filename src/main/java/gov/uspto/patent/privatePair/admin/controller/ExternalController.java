package gov.uspto.patent.privatePair.admin.controller;

import lombok.extern.slf4j.Slf4j;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import gov.uspto.patent.privatePair.admin.domain.ApplicationAddressReqType;
import gov.uspto.patent.privatePair.portal.service.PortalService;
import gov.uspto.patent.privatePair.utils.SSLUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
public class ExternalController{

    @Value("${opsgbp.webservice.url}")
    String OPSBPServiceUrl;
    @Value("${opsgpcdm.webservice.url}")
    String OPSGPCDMServicesUrl;
    @Value("${opsg.webservice.url}")
	String urlString;
    private RestTemplate restTemplate;
    @Autowired
	PortalService portalService;
    @Autowired
    public ExternalController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping(value = "/OPSGBPServices/customer-number/v1")
    public String getCustDetails(@RequestParam("customerNumber") Long customerNumber) throws URISyntaxException {

        final String baseUrl = OPSBPServiceUrl + customerNumber;
        URI uri = new URI(baseUrl);
        ResponseEntity<String> result=null;
        try {
        SSLUtil.turnOffSslChecking();
        result = restTemplate.getForEntity(uri, String.class);
        SSLUtil.turnOnSslChecking();
        }catch (Exception e) {
			log.error(e.getMessage());
			
		}
        if (result != null) {
          return result.getBody();
        }
        return null;
    }

    @PostMapping(value = "/OPSGPCDMServices/registered-practitioners")
    public String getRegisteredPracDetails(@RequestParam("customerNumber") Long customerNumber) throws URISyntaxException {

        final String baseUrl = OPSGPCDMServicesUrl + customerNumber;
        URI uri = new URI(baseUrl);
        ResponseEntity<String> result=null;
        try {
            SSLUtil.turnOffSslChecking();
    result = restTemplate.getForEntity(uri, String.class);
        SSLUtil.turnOnSslChecking();
        }catch (Exception e) {
			log.error(e.getMessage());
			
		}
        if (result != null) {
          log.debug(result.getBody());
          return result.getBody();
        }
        return null;
    }
    
    
    
   
    public String getCustDetails(@RequestParam("customerNumber") String customerNumber) throws URISyntaxException {

        final String baseUrl = OPSBPServiceUrl + customerNumber;
        URI uri = new URI(baseUrl);
        ResponseEntity<String> result=null;
        try {
        SSLUtil.turnOffSslChecking();
        result = restTemplate.getForEntity(uri, String.class);
        SSLUtil.turnOnSslChecking();
        return result.getBody();
        }catch (Exception e) {
			log.error(e.getMessage());
			return e.getMessage();
		}
       
        
    }
    
    @GetMapping(value = "/getStates")
    public List getStates(@RequestParam("countryCode") String countryCode) throws URISyntaxException {
log.info("http://ict.uspto.gov/ICTServices/ICT/PTO_GEO_REGION?parentValue="+countryCode+"&aisName=EFSWeb");
        final String baseUrl = "http://ict.uspto.gov/ICTServices/ICT/PTO_GEO_REGION?parentValue="+countryCode+"&aisName=EFSWeb";
        URI uri = new URI(baseUrl);
        ResponseEntity<List> resultList = null;
        List dummy= new ArrayList();
        try {
        SSLUtil.turnOffSslChecking();
        resultList = restTemplate.getForEntity(uri, List.class);
        SSLUtil.turnOnSslChecking();
        return resultList.getBody();
        }catch (Exception e) {
			log.error(e.getMessage());
			return dummy;
		}
       
        
    }
    
    
    @PostMapping(value = "/OPSGPCDMServices/entity-status")
    public String submitEntityStatus(@RequestBody String data) {
		System.out.println("submitEntityStatus");
		String associateData = null;
		JSONObject jsonObj =null;
		String url = urlString+"OPSGPCDMServices/entity-status/v1";
		try { 
			SSLUtil.turnOffSslChecking();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(data, headers);
			return restTemplate.exchange(url,HttpMethod.POST,entity, String.class).getBody();
		}catch(Exception ex){
			log.info("Exception caught in associateApplicationCorrespondenceAddress block-->  "+ ex.getMessage());
			return ex.getMessage();
		}
		
	}
    
    
    @PostMapping(value = "/customer-number/dropdown")
    public List getCustDetailsDropdown(@RequestParam("customerNumber") List customerNumber) throws URISyntaxException {
    	
    	List<CompletableFuture<JsonNode>> allFutures = new ArrayList<>();
        
      
       // ResponseEntity<String> result=null;
      
        List list=new ArrayList<>();
        
        try {
        SSLUtil.turnOffSslChecking();
        System.out.println(customerNumber.size());
        for(int i=0;i<customerNumber.size();i++) {
			 allFutures.add(portalService.callOtherService((String) customerNumber.get(i)));
			}
        for(int i=0;i<customerNumber.size();i++) {
        	System.out.println(allFutures.get(i).get().findParent("physicalAddress"));
	          JsonNode jArray = allFutures.get(i).get().findParent("physicalAddress");
			 // ObjectMapper mapper = new ObjectMapper();
		     // ArrayNode arrayNode = (ArrayNode) mapper.readTree(allFutures.get(i).get().toString()).get("physicalAddress");
		      System.out.println(jArray.get("physicalAddress"));
		      System.out.println(customerNumber.get(i));
		      System.out.println(jArray.get("physicalAddress").get(0));
		      System.out.println(jArray.get("physicalAddress").get(0).get("nameLineOneText"));
		      Map<String,String> map = new HashMap<String,String>();
		      map.put("val", customerNumber.get(i).toString());
		      map.put("full", customerNumber.get(i) +  " -> "+ jArray.get("physicalAddress").get(0).get("nameLineOneText").asText());
		     
		   
		//	String val= "{\"val\" : "+customerNumber.get(i).toString()+",\"full\" :"+ customerNumber.get(i) +" -> "+ jArray.get("physicalAddress").get(0).get("nameLineOneText").asText()+"}";
		      list.add(map);
		      System.out.println(list);
	        }
      //  map.putAll(map);
        	SSLUtil.turnOnSslChecking();
        }catch (Exception e) {
			log.error(e.getMessage());
			
		}
       
        return list;
        
    }
    
    
    
}