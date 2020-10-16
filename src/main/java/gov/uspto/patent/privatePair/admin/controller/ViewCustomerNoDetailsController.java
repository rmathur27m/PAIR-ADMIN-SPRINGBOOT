package gov.uspto.patent.privatePair.admin.controller;

import gov.uspto.patent.privatePair.PCSEJBApp.services.CustDetailsService;
import gov.uspto.patent.privatePair.admin.domain.CreateNewCustomerNumberForm;
import gov.uspto.patent.privatePair.admin.domain.CustDataInfo;
import gov.uspto.patent.privatePair.admin.domain.JsonResponse;
import gov.uspto.patent.privatePair.admin.service.ViewCustomerNoDetailsService;
import gov.uspto.patent.privatePair.utils.SSLUtil;
import gov.uspto.patent.privatePair.utils.ServiceException;
import lombok.extern.slf4j.Slf4j;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.sun.xml.messaging.saaj.packaging.mime.MessagingException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


@RestController
@Slf4j
public class ViewCustomerNoDetailsController {

    @Autowired
    ViewCustomerNoDetailsService viewCustomerNoDetailsService;

    @Value("${opsg.webservice.url}")
	String urlString;
    

    
    @GetMapping(value = "/getLastPrivatePairUpdate")
    public List getLastPrivatePairUpdate(@RequestParam(name="custNum", required = true) int custNum)
            throws ServiceException, ClassNotFoundException, SQLException {
        System.out.println("inside getLastPrivatePairUpdate controller--->"+ custNum);

        return viewCustomerNoDetailsService.getLastPrivatePairUpdate(custNum);
    }
    
    
    
    @GetMapping(value = "/getOptedInDetails")
    public List getOptedInDetails(@RequestParam(name="custNum", required = true) int custNum)
            throws ServiceException, ClassNotFoundException, SQLException {
        System.out.println("inside getLastPrivatePairUpdate controller--->"+ custNum);

        return viewCustomerNoDetailsService.getOptedInDetails(custNum);
    }
    
    
    
    @PostMapping(value = "/insertCorrDissassociate")
    public String insertCorrDissassociate(@RequestBody String sdata)
    
            throws Exception, ServiceException, ClassNotFoundException, SQLException {
    	
    	 JSONObject obj = new JSONObject(sdata);
    	 String custDataInfoString = obj.getJSONObject("custDataInfo").toString();
    	 String success = obj.getString("success");
    	   Gson gson = new Gson();
    	   CustDataInfo custDataInfo = gson.fromJson(custDataInfoString, CustDataInfo.class);
        System.out.println("inside insertCustNumDetailsAdmin controller--->"+ custDataInfo);

        return viewCustomerNoDetailsService.insertCorrDissassociate(custDataInfo,success);
    }
    
    @PostMapping(value = "/insertAssociateDisAssociate")
    public String insertAssociateDisAssociate(@RequestBody String sdata)
    
            throws Exception, ServiceException, ClassNotFoundException, SQLException {
    	
    	 JSONObject obj = new JSONObject(sdata);
    	 String custDataInfoString = obj.getJSONObject("custDataInfo").toString();
    	 String success = obj.getString("success");
    	   Gson gson = new Gson();
    	   CustDataInfo custDataInfo = gson.fromJson(custDataInfoString, CustDataInfo.class);
        System.out.println("inside insertAssociateDisAssociate controller--->"+ custDataInfo);

        return viewCustomerNoDetailsService.insertAssociateDisAssociate(custDataInfo,success);
    }
    
    
    @GetMapping(value = "/insertOrUpdateAssociationDNtoCN")
    public List insertOrUpdateAssociationDNtoCN(@RequestParam("custnum") String custnum,@RequestParam("attorneyList") ArrayList<String> attorneyList)
    	       throws ServiceException, ClassNotFoundException, SQLException {
        System.out.println("inside insertOrUpdateAssociationDNtoCN controller--->"+ custnum);
        List attorneyListtemp = attorneyList;
        System.out.println("size"+attorneyList);
        System.out.println("0 element"+attorneyList.get(0));
		ArrayList userIdList = new ArrayList();
		Iterator itAttorney = attorneyListtemp.iterator();
		String tempUserId="";				
	while(itAttorney.hasNext())
		{
			tempUserId="R0"+itAttorney.next(); // Registration number being send in pplace of user id
			log.debug("**********tempUserId at line 199 is ****************" +tempUserId);							
			if(!tempUserId.equals(""))
			{
				log.debug("**********adding tempUserId to userIdList at line 202 is ****************");								
				userIdList.add(tempUserId);
			}
		}	
        return viewCustomerNoDetailsService.insertOrUpdateAssociationDNtoCN(custnum,userIdList);
    }
    
    @GetMapping(value = "/deleteCustNumQuery")
    public List deleteCustNumQuery(@RequestParam("custnum") String custnum,@RequestParam("attorneyList") ArrayList<String> attorneyList)
    	       throws ServiceException, ClassNotFoundException, SQLException {
        System.out.println("inside deleteCustNumQuery controller--->"+ custnum);
        List attorneyListtemp = attorneyList;
        System.out.println("size"+attorneyList);
        System.out.println("0 element"+attorneyList.get(0));
		ArrayList userIdList = new ArrayList();
		Iterator itAttorney = attorneyListtemp.iterator();
		String tempUserId="";				
	while(itAttorney.hasNext())
		{
			tempUserId="R0"+itAttorney.next(); // Registration number being send in pplace of user id
			log.debug("**********tempUserId at line 199 is ****************" +tempUserId);							
			if(!tempUserId.equals(""))
			{
				log.debug("**********adding tempUserId to userIdList at line 202 is ****************");								
				userIdList.add(tempUserId);
			}
		}	
        return viewCustomerNoDetailsService.deleteCustNumQuery(custnum,userIdList);
    }
    
    
    	
   
  
    // To EDIT CUSTOMER NUMBER DETAILS IN CUSTOMER DETAILS SCREEN OPSG CALL
    
    @PutMapping(value = "/EditCustomerDetails")
    public String getCustDetails(@RequestBody String data) throws URISyntaxException {
    	 HttpHeaders headers = new HttpHeaders();
    	    headers.setContentType(MediaType.APPLICATION_JSON); 
    	    HttpEntity<String> entity = new HttpEntity<String>(data, headers); 
    	    RestTemplate restTemplate = new RestTemplate();
    	    final String baseUrl = urlString+"OPSGBPServices/customer-number/v1";
    	    String responseBody = null;
    	    URI uri = new URI(baseUrl);
			        try {
			        		SSLUtil.turnOffSslChecking();
				        	ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);
				        	responseBody = response.getBody();
				        	SSLUtil.turnOnSslChecking();
			        }catch (Exception e) {
			        		log.error(e.getMessage());
			        		System.out.println(e.getMessage());
			        		responseBody = e.getMessage().toString();
			        }
	return responseBody;
    }
    
    
    // To create new ACTUAL CUST NUM for CREATE NEW CUSTOMER NUMBER SCREEN OPSG CALL
    
    @PostMapping(value = "/getNewCustomerNumber")
    public String getNewCustomerNumber(@RequestBody String data) throws URISyntaxException {
    	 HttpHeaders headers = new HttpHeaders();
    	    headers.setContentType(MediaType.APPLICATION_JSON); 
    	    HttpEntity<String> entity = new HttpEntity<String>(data, headers); 
    	    RestTemplate restTemplate = new RestTemplate();
    	    final String baseUrl = urlString+"OPSGBPServices/customer-number/v1";
    	    String responseBody = null;
    	    URI uri = new URI(baseUrl);
			        try {
			        		SSLUtil.turnOffSslChecking();
				        	ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
				        	responseBody = response.getBody();
				        	SSLUtil.turnOnSslChecking();
			        }catch (Exception e) {
			        		log.error(e.getMessage());
			        		System.out.println(e.getMessage());
			        		responseBody = e.getMessage().toString();
			        }
	return responseBody;
    }
    
    // CUSTOMER DETAILS SCREEN -- ADD ASSOCIATE PRAC OPSG CALL
    
    @PutMapping(value = "/addAssociatePrac")
    public String addAssociatePrac(@RequestBody String data) throws URISyntaxException {
    	 HttpHeaders headers = new HttpHeaders();
    	    headers.setContentType(MediaType.APPLICATION_JSON); 
    	    HttpEntity<String> entity = new HttpEntity<String>(data, headers); 
    	    RestTemplate restTemplate = new RestTemplate();
    	    final String baseUrl = urlString+"OPSGBPServices/customer-number/associate-practitioners/v1";
    	    System.out.println(baseUrl);
    	    String responseBody = null;
    	    URI uri = new URI(baseUrl);
    	    System.out.println(uri);
			        try {
			        		SSLUtil.turnOffSslChecking();
				        	ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);
				        	responseBody = response.getBody();
				        	SSLUtil.turnOnSslChecking();
			        }catch (Exception e) {
			        		log.error(e.getMessage());
			        		System.out.println(e.getMessage());
			        		responseBody = e.getMessage().toString();
			        }
	return responseBody;
    }
    
    // CUSTOMER DETAILS SCREEN -- DISASSOCIATE PRAC OPSG CALL
    
    @PutMapping(value = "/disAssociatePrac")
    public String disAssociatePrac(@RequestBody String data) throws URISyntaxException {
    	 HttpHeaders headers = new HttpHeaders();
    	    headers.setContentType(MediaType.APPLICATION_JSON); 
    	    HttpEntity<String> entity = new HttpEntity<String>(data, headers); 
    	    RestTemplate restTemplate = new RestTemplate();
    	    final String baseUrl = urlString+"OPSGBPServices/customer-number/disassociate-practitioners/v1";
    	    String responseBody = null;
    	    URI uri = new URI(baseUrl);
			        try {
			        		SSLUtil.turnOffSslChecking();
				        	ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.PUT, entity, String.class);
				        	responseBody = response.getBody();
				        	SSLUtil.turnOnSslChecking();
			        }catch (Exception e) {
			        		log.error(e.getMessage());
			        		System.out.println(e.getMessage());
			        		responseBody = e.getMessage().toString();
			        }
	return responseBody;
    }
    
    // CUSTOMER DETAILS SCREEN --VALIDATION REGISTERED PRAC NUM OPSG CALL
    
    @PostMapping(value = "/getValidRegPracValues")
    public String getValidRegPracValues(@RequestBody String data) throws URISyntaxException {
    	 HttpHeaders headers = new HttpHeaders();
    	    headers.setContentType(MediaType.APPLICATION_JSON); 
    	    HttpEntity<String> entity = new HttpEntity<String>(data, headers); 
    	    RestTemplate restTemplate = new RestTemplate();
    	    final String baseUrl = urlString+"OPSGPCDMServices/registered-practitioners";
    	    String responseBody = null;
    	    URI uri = new URI(baseUrl);
			        try {
			        		SSLUtil.turnOffSslChecking();
				        	ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
				        	responseBody = response.getBody();
				        	SSLUtil.turnOnSslChecking();
			        }catch (Exception e) {
			        		log.error(e.getMessage());
			        		System.out.println(e.getMessage());
			        		responseBody = e.getMessage().toString();
			        }
	return responseBody;
    }
    
    
   
    @PostMapping(value = "/sendemail")
    public String sendEmail(@RequestBody String sdata) throws Exception, ServiceException, ClassNotFoundException, SQLException {
    	 JSONObject obj = new JSONObject(sdata);
    	 String custDataInfoString = obj.getJSONObject("custDataInfo").toString();
    	 String email = obj.getString("email");
    	   Gson gson = new Gson();
    	   CustDataInfo custDataInfo = gson.fromJson(custDataInfoString, CustDataInfo.class);
    	String s="";
    	try{
    	s=	sendTestEmail(email,custDataInfo);
    	return s;
    	}catch (Exception e) {
		System.out.println(e);
		return "failure";
		}
    	
    } 

    private String sendTestEmail(String email,CustDataInfo custDataInfo) throws AddressException, javax.mail.MessagingException{
        String destinationEmail = email;
        String senderEmail      = "PALM_email_1@USPTO.GOV";
        String host             = "localhost";
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        Session session = Session.getDefaultInstance(properties);
        MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(senderEmail));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(destinationEmail));
		message.setSubject("Email Address Change for Customer Number:"+custDataInfo.getCustomer_no()+" via Private PAIR");
		String msg = "<br><br>";
		msg += "Dear PAIR Customer:<br><br>";
		msg += custDataInfo.getName_line_one_n()+" "+custDataInfo.getName_line_two_n()+"<br>" ;
		msg += custDataInfo.getStreet_line_one_n()+" "+custDataInfo.getStreet_line_two_n()+"<br>";
		msg += custDataInfo.getCity_nm_n()+", "+custDataInfo.getState_nm_n()+", "+custDataInfo.getPostal_cd_n()+"<br>";
        msg += custDataInfo.getCountry_nm_n()+"<br><br>";
        msg += "New Email Address: <a href=\"mailto:"+email+"\">"+email+"</a> <br>";
        msg += "Greetings!  You are receiving this email because you have chosen to add the above <br>";
        msg += "identified  email address as a new email address or as an update  to an existing <br>";
        msg += "email address associated  with your customer number:"+custDataInfo.getCustomer_no()+". <br><br>";
        msg += "To view your correspondence online or to update your email addresses, please <br>";
        msg += "visit us at: <a href=\"https://sportal.uspto.gov/secure/myportal/privatepair\">https://sportal.uspto.gov/secure/myportal/privatepair</a> <br><br>";
        msg += "If you have any questions, please email the Electronic Business Center (EBC) at <br>";
        msg += "<a href=\"mailto:EBC@uspto.gov\">EBC@uspto.gov</a> , or call 1-866-217-9197, Monday-Friday 6:00 a.m. to 12:00 a.m. <br>";
        msg += "Eastern Standard Time (EST). <br><br>";
        msg += "If you have received this email in error, please contact the PAIR Team at <a href=\"mailto:PAIR@USPTO.gov\">PAIR@USPTO.gov</a> <br><br>"; 
        msg += "Sincerely, <br>"; 
        msg += "The PAIR Team <br>"; 
        message.setContent(msg,"text/html");
        Transport.send(message);
		System.out.println("Sent Successfully");
        return "success";
    }
  
    
    
  
}
