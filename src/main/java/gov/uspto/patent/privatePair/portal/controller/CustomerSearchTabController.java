package gov.uspto.patent.privatePair.portal.controller;


import gov.uspto.patent.privatePair.portal.service.PortalService;
import gov.uspto.patent.privatePair.portal.util.SupplementDataType;
import gov.uspto.patent.privatePair.score.client.ScoreMegaItemImpl;
import gov.uspto.patent.privatePair.score.client.ScoreMegaItemVersionHistoryImpl;
import gov.uspto.patent.privatePair.score.client.ScoreSoapClient;
import gov.uspto.patent.privatePair.score.scoreService.MegaData.MegaItemRecordType;
import gov.uspto.patent.privatePair.score.scoreService.MegaData.MegaItemRecordsType;
import gov.uspto.patent.privatePair.score.scoreService.MegaData.MegaItemsInputMessage;
import gov.uspto.patent.privatePair.score.scoreService.MegaItemVersionHistory.MegaItemVerHistoryInputMessage;
import gov.uspto.patent.privatePair.score.scoreService.MegaItemVersionHistory.MegaItemVersionType;
import gov.uspto.patent.privatePair.score.scoreService.MegaItemVersionHistory.MegaItemVersionsType;
import gov.uspto.patent.privatePair.score.scoreService.MetaData.MetaDataRecordsType;
import gov.uspto.patent.privatePair.score.scoreService.MetaData.MetaDataType;
import gov.uspto.patent.privatePair.utils.SSLUtil;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import gov.uspto.patent.privatePair.admin.domain.CreateNewCustomerNumberForm;
import gov.uspto.patent.privatePair.admin.domain.JsonResponse;
import gov.uspto.patent.privatePair.fpng.dto.FpngServiceResponse;
import gov.uspto.patent.privatePair.fpng.service.FpngRestClientService;
import gov.uspto.patent.privatePair.utils.IpAddressUtil;
import gov.uspto.patent.privatePair.utils.SSLUtil;
import gov.uspto.utils.SortMe;
import gov.uspto.utils.Statistics;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

@RestController
@Configuration
@PropertySource("classpath:application.properties")
public class CustomerSearchTabController {
	private String pairId="";
	private String dn="";
	private static final Logger log = LoggerFactory.getLogger(CustomerSearchTabController.class);
	Gson gson = new Gson();
	@Value("${opsg.webservice.url}")
	String urlString;
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	PortalService portalService;
	

	/*@RequestMapping(value = "/OPSGApplicationGeneralInfoServicesByCustomer/getData", method = RequestMethod.GET)
	@ResponseBody
	public String getTabsData(@RequestParam() String customerNumber,HttpServletRequest request)
			throws URISyntaxException, Exception {
		 JsonResponse jsonResponse = new JsonResponse();

		log.info("Inside getApplicationData:");
		Map<String, String> finalResult = new HashMap<>();

		RestTemplate restTemplate = new RestTemplate();
		// ---------------------------------------------------------------APPLICATION DATA----------------------------------------------------------------
				int skip=0;
					int limit =500;
		final String applicationBaseUrl = urlString+"OPSGPCDMServices/application-general-information/customer?customerNumber="+ customerNumber+"&skip="+skip+"&limit="+limit;
		System.out.println(applicationBaseUrl);
		URI applicationUri = new URI(applicationBaseUrl);
		ResponseEntity<String> applicationData = null;
		String finalJson=null;
		 Instant start = Instant.now();
	        List<CompletableFuture<JsonNode>> allFutures = new ArrayList<>();

		
	// ********************************************************************************************
		try {
			SSLUtil.turnOffSslChecking();
			String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
			log.info("Inside try block");
			applicationData = restTemplate.getForEntity(applicationUri, String.class);
			JSONObject objJsonObject = new JSONObject(applicationData.getBody());
			JSONObject objJsonObjectMeta = new JSONObject(objJsonObject.getString("meta"));
			double total=objJsonObjectMeta.getInt("total");
			double callTimes=Math.ceil(total/500);
			ArrayList<String> temp = new ArrayList<>();
			for(int i=0;i<=callTimes;i++) {
				 allFutures.add(portalService.callOtherService(customerNumber,skip,limit));
				 skip=skip+500;
				}
			
			  CompletableFuture.allOf(allFutures.toArray(new CompletableFuture[0])).join();
			  
			  for(int i=0;i<=callTimes;i++) {
		          JsonNode jArray = allFutures.get(i).get().findParent("results");
				  ObjectMapper mapper = new ObjectMapper();
			      ArrayNode arrayNode = (ArrayNode) mapper.readTree(allFutures.get(i).get().toString()).get("results");
			      for(int j=0; j<arrayNode.size(); j++){
						temp.add(arrayNode.get(j).toString());
					}
		        }
		
			
		finalJson="{\"total\":"+total+",\"results\":"+temp+"}";
		System.out.println("Total time: " + Duration.between(start, Instant.now()).getSeconds());
			activityLog(activityStartTime,Statistics.APPLICATIONDATAREQUEST, request);
			
			SSLUtil.turnOnSslChecking();
			  
		} catch (Exception e) {
			log.error(e.getMessage());
			
		}
		return finalJson;
	}
	*/
	
	@RequestMapping(value = "/OPSGApplicationGeneralInfoServicesByCustomer/getDataWithSkipCount", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> getTabsDataWithSkipCount(@RequestParam() String customerNumber,@RequestParam() Integer skipCount,HttpServletRequest request)
			throws URISyntaxException, Exception {
		 JsonResponse jsonResponse = new JsonResponse();

		log.info("Inside getApplicationData:");
		Map<String, String> finalResult = new HashMap<>();

		RestTemplate restTemplate = new RestTemplate();
		// ---------------------------------------------------------------APPLICATION DATA----------------------------------------------------------------
				int skip=skipCount;
					int limit =100;
		final String applicationBaseUrl = urlString+"OPSGPCDMServices/application-general-information/customer?customerNumber="+ customerNumber+"&skip="+skip+"&limit="+limit;
		System.out.println(applicationBaseUrl);
		URI applicationUri = new URI(applicationBaseUrl);
		ResponseEntity<String> applicationData = null;
		String finalJson=null;
		
	// ********************************************************************************************
		try {
			SSLUtil.turnOffSslChecking();
			String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
			log.info("Inside try block");
			applicationData = restTemplate.getForEntity(applicationUri, String.class);
			
			
			activityLog(activityStartTime,Statistics.APPLICATIONDATAREQUEST, request);
			
			SSLUtil.turnOnSslChecking();
			  
		} catch (Exception e) {
			log.error(e.getMessage());
			
		}
		
	
	
		return applicationData;
	}
	
	
	
	
	  private void activityLog(String activityStartTime, int activityName,HttpServletRequest request) {
		  String activityEndTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
		  long startTimeMillis = System.currentTimeMillis();
	
		String statsActivity = Statistics.addPairAdminActivity(pairId, dn,
				activityName  , activityStartTime, activityEndTime, "0", startTimeMillis, 0, "0",
                  IpAddressUtil.getClientIP(request));

          log.info("Stats Activity: {}",statsActivity);
	    }


}
