package gov.uspto.patent.privatePair.portal.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import gov.uspto.patent.privatePair.utils.SSLUtil;
import gov.uspto.utils.SortMe;

@RestController
@Configuration
public class AuthenticateUserController {
	private String pairId="";
	private String dn="";
	HttpHeaders headers;
	
	@Value("${pctr.webservice.url}")
	String pctrURLString;
	
	@Autowired
	RestTemplate restTemplate;
	
	private static final Logger log = LoggerFactory.getLogger(ApplicationSearchTabController.class);
	
	@PostMapping(value = "/getAuthenticateData")
	 public ResponseEntity<String> getDnFromRbac(@RequestParam("patronId") String patronId,HttpServletRequest request) {
		
		ResponseEntity<String> authenticateData = null;
		String msg=null;
		Gson gson = new Gson();
		try {
			SSLUtil.turnOffSslChecking();
			String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
			String pctrurl = pctrURLString+"/authorization/services/v1/accounts?patronIdentifier="+patronId;
			//https://pctr.fqt.uspto.gov/authorization/services/v1/accounts?patronIdentifier=
			authenticateData = restTemplate.getForEntity(pctrurl, String.class);
			SSLUtil.turnOnSslChecking();
			log.info("got Authentication Data-->"+authenticateData.getBody());
			JSONArray jsonArray = new JSONArray(authenticateData.getBody().toString());
			
			
			    System.out.println(jsonArray.length());
			   // JSONObject val  = jsonArray.getJSONObject(0);
			// System.out.println(val);
			
			 JSONObject objJsonLegacy = new JSONObject(jsonArray.getJSONObject(0).getString("Legacy").toString());
			 System.out.println(objJsonLegacy);
			// JSONObject objJsonLegacy = new JSONObject(objJsonval.getString("Legacy"));
				String objJsonDnValue = objJsonLegacy.getString("dnText").toString();
				request.getSession().setAttribute("DnValue",objJsonDnValue);
				String objJsonRoleValue = objJsonLegacy.getString("roleText").toString();
				request.getSession().setAttribute("RoleValue",objJsonRoleValue);
				String objJsonNameValue = objJsonLegacy.getString("commonNameText").toString();
				request.getSession().setAttribute("NameValue",objJsonNameValue);
				 System.out.println(objJsonDnValue);
				JSONObject objJsonPatentCenter = new JSONObject(jsonArray.getJSONObject(0).getString("PatentCenter").toString());
				String objJsonpracRegNumValue = objJsonPatentCenter.getString("practitionerRegistrationNumber").toString();
				 System.out.println(objJsonpracRegNumValue);
				request.getSession().setAttribute("RegNumValue",objJsonpracRegNumValue);
		} catch (Exception e) {
			log.error(e.getMessage());
				msg=e.getMessage();
				
		}
		
        return authenticateData;
    }
	
	@GetMapping(value = "/getJVMArgValue")
	 public ResponseEntity<String>  getJVMArgValue(HttpServletRequest request) {
		
		Gson gson = new Gson();
		String value=System.getProperty("spring.profiles.active");
		return ResponseEntity.ok(gson.toJson(value));
		
		
	
	}
	
}


