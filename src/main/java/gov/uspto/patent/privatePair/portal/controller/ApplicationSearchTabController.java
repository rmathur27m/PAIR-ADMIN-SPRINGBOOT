package gov.uspto.patent.privatePair.portal.controller;

import gov.uspto.patent.privatePair.portal.util.ApplicationByCustomer;
import gov.uspto.patent.privatePair.portal.util.ApplicationInfo;
import gov.uspto.patent.privatePair.portal.util.ApplicationStatusData;
import gov.uspto.patent.privatePair.portal.util.DisplayReferences;
import gov.uspto.patent.privatePair.portal.util.ExaminerName;
import gov.uspto.patent.privatePair.portal.util.LastFileHistoryTransaction;
import gov.uspto.patent.privatePair.portal.util.Results;
import gov.uspto.patent.privatePair.portal.util.ScoreResult;
import gov.uspto.patent.privatePair.PCSEJBApp.dto.AppListByCustNumRow;
import gov.uspto.patent.privatePair.PCSEJBApp.dto.ApplicationCorrespondenseDate;
import gov.uspto.patent.privatePair.PCSEJBApp.dto.DisplayList;
import gov.uspto.patent.privatePair.PCSEJBApp.dto.DocumentBag;
import gov.uspto.patent.privatePair.PCSEJBApp.dto.DocumentData;
import gov.uspto.patent.privatePair.PCSEJBApp.dto.EarliestPublicationDate;
import gov.uspto.patent.privatePair.PCSEJBApp.dto.IFWObject;
import gov.uspto.patent.privatePair.PCSEJBApp.dto.OutGoingCorrespondenseXMLDownload;
import gov.uspto.patent.privatePair.portal.util.SupplementDataType;
import gov.uspto.patent.privatePair.portal.util.TransactionHistory;
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
import gov.uspto.patent.privatePair.utils.ServiceException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import gov.uspto.patent.privatePair.admin.domain.ApplicationAddressReqType;
import gov.uspto.patent.privatePair.admin.domain.CreateNewCustomerNumberForm;
import gov.uspto.patent.privatePair.admin.domain.JsonResponse;
import gov.uspto.patent.privatePair.fpng.dto.FpngServiceResponse;
import gov.uspto.patent.privatePair.fpng.service.FpngRestClientService;
import gov.uspto.patent.privatePair.utils.IpAddressUtil;
import gov.uspto.patent.privatePair.utils.SSLUtil;
import gov.uspto.utils.SortMe;
import gov.uspto.utils.Statistics;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;

@RestController
@Configuration
@PropertySource("classpath:application.properties")
public class ApplicationSearchTabController {
	private String pairId="";
	private String dn="";
	private static final Logger log = LoggerFactory.getLogger(ApplicationSearchTabController.class);
	Gson gson = new Gson();
	@Autowired
	RestTemplate restTemplate;

	@Value("${pp.webservice.url}")
	String urlStringPP;
	
	@Value("${opsg.webservice.url}")
	String urlString;

	@Value("${sdwp.webservice.url}")
	String sdwpURLString;

	@Value("${opsg.webservice.searchapi.url}")
	String opsgSearchURLString;
	
	@Value("${infra.webservice.url}")
	String infraURLString;

	@Value("${sdwp.webservice.external.url}")
	String swdpExternalURL;
	
	@Value("${PatentNumberRegex}")
	String PatentNumberRegex;
		
	@Value("${FormatPatentNumber}")
	String FormatPatentNumber;

	@Autowired
	FpngRestClientService fpngRestClientService;

	@Autowired
	ScoreSoapClient scoreSoapClient;

	@Autowired
	ScoreMegaItemVersionHistoryImpl scoreMegaItemVersionHistoryImpl;

	@Autowired
	ScoreMegaItemImpl scoreMegaItemImpl;
	private static final String YELLOW_BOOK = "Yellow Book";
	private static final String RED_BOOK = "Red Book";
	private static final String MATHEMATICAL_FORMULAS = "Mathematical Formulas";
	private static final String CHEMICAL_FORMULAS = "Chemical Formulas";
	private static final String DRAWINGS = "Drawings";
	private static final String APPENDICES = "Appendices";
	private static final String CERTIFIED_FRP_RS_W_COLOR = "Certified FRPRs w/color";
	private static final String US_PATENTS_W_COLOR = "US Patents w/color";
	private static final String INTERIM_COPY_FRPR_TXT = "Interim Copy FRPR - TXT Files";
	private static final String COMPUTER_PROGRAM_LISTINGS_CPL = "Computer Program Listings (CPL)";
	private static final String SEARCH_RESULTS = "Search Results";
	private static final String MEGATABLES = "Megatables";
	private static final String SEQUENCES = "Sequences";
	private static final String _3D_PROTEIN_CRYSTALS = "3D Protein Crystals";

	@RequestMapping(value = "/OPSGApplicationGeneralInfoServices/getData", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, String> getTabsData(@RequestParam() String applicationNumber,@RequestParam() List custNumber,@RequestParam() String searchType, HttpServletRequest request)
			throws URISyntaxException, Exception {
		log.info("Inside getApplicationData:urlString--->>"+ urlString);
		String appNumForDisplayRef = null;
		String applicationNumber_str=null,  newApplicaitonNum=null;
		String applicationStatus="";
		applicationNumber = applicationNumber.toUpperCase();
		if(searchType.equalsIgnoreCase("dmNo")) {
			 applicationNumber_str = "internationalRegistrationNumber";
			appNumForDisplayRef= getApplicationNumberFrmService(applicationNumber, applicationNumber_str);
			}
		else if(searchType.equalsIgnoreCase("pair_pct_search")){//  PCT/US10/31909
			if(applicationNumber.length()==14) { // to avoid array out of bound err
			appNumForDisplayRef = applicationNumber.substring(4,8)+applicationNumber.substring(9,14);
			}
			if(applicationNumber.length()==17) { //  PCT/US2010/031909 // to avoid array out of bound err
				appNumForDisplayRef = applicationNumber.substring(4,6)+applicationNumber.substring(8,10)+applicationNumber.substring(12,17);
			}
			log.info("new PCT APP Number--> "+appNumForDisplayRef);
			applicationNumber_str = "applicationNumber";
		}
		else if(searchType.equalsIgnoreCase("pgPubsNo")){
			applicationNumber_str = "applicationNumber";
		//else if(searchType.equalsIgnoreCase("US")) && (applicationNumber.contains("A")) || applicationNumber.length()==11 || applicationNumber.contains("A") || applicationNumber.contains("A") ){
				System.out.println("inside for publication");
			 String applicationNumbertemp = "publicationNumber";
			 if((applicationNumber.contains("US")) && (applicationNumber.contains("A")) && applicationNumber.length()==18 ) {    //US 9999-9999999 A9
			applicationNumber=applicationNumber.substring(3,7)+applicationNumber.substring(8,15);
			 }
			 else if((applicationNumber.length()==15) && (applicationNumber.contains("A")) ) {    //9999-9999999 A9
					applicationNumber=applicationNumber.substring(0,4)+applicationNumber.substring(5,12);
					 }
			 else if(applicationNumber.length()==12) {    //9999-9999999
					applicationNumber=applicationNumber.substring(0,4)+applicationNumber.substring(5,12);
					 }
			 else if((applicationNumber.contains("US")) && (applicationNumber.contains("A")) && applicationNumber.length()==17 ) {    //US 99999999999 A9
					applicationNumber=applicationNumber.substring(3,14);
					 }
			 else if((applicationNumber.contains("A")) && applicationNumber.length()==14 ) {    //99999999999 A9
					applicationNumber=applicationNumber.substring(0,11);
					 }
			 else if(applicationNumber.length()==11) {    //99999999999
					applicationNumber=applicationNumber.substring(0,11);
					 }
			 System.out.println(applicationNumber);
			try {
				
				applicationNumber= getApplicationNumberFrmService(applicationNumber, applicationNumbertemp).substring(1);
			} catch (Exception e) {
				 System.out.println(e.getMessage());
				 if(null != applicationNumber) {
					 applicationNumber_str = "applicationNumber";
						newApplicaitonNum=applicationNumber;
				 }
			}
				System.out.println("PUBLICATION's Application Num"+applicationNumber);
				 System.out.println(applicationNumber_str);
			 
			 
		}
		else {
				applicationNumber_str = "applicationNumber";
				newApplicaitonNum=applicationNumber;
			}
		System.out.println("check space issue "+applicationNumber);
		applicationNumber=applicationNumber.replaceAll("\\s", ""); 
		applicationNumber=StringUtils.trimWhitespace(applicationNumber);
		 
		System.out.println("after fixing space issue "+applicationNumber);
		if(null != newApplicaitonNum) {
		newApplicaitonNum=newApplicaitonNum.replaceAll("\\s", ""); 
		}
		/******calling New Application Number for those application who doesn't work with regular one****/		
			
		/*End*/
		Map<String, String> finalResult = new HashMap<>();

		RestTemplate restTemplate = new RestTemplate();
		
		String customerNumberText="";
		// ---------------------------------------------------------------APPLICATION DATA----------------------------------------------------------------

		final String applicationBaseUrl = urlString+"OPSGPCDMServices/application-general-information?publicStatus=true&"+applicationNumber_str+"="+ applicationNumber;
		URI applicationUri = new URI(applicationBaseUrl);
		ResponseEntity<String> applicationData = null;

		// ---------------------------------------------------------------FILE CONTENT TRANSACTION DATA----------------------------------------------------------------

		final String transactionHistoryBaseUrl = urlString+"OPSGPCDMServices/file-content-transaction-history?"+applicationNumber_str+"="+ applicationNumber;
		URI transactionHistoryUri = new URI(transactionHistoryBaseUrl);
		ResponseEntity<String> transactionHistoryData = null;

		// ---------------------------------------------------------------Continuity Data ----------------------------------------------------------------

		final String continuityDatabaseUrl = urlString+"OPSGPCDMServices/domestic-continuity?"+applicationNumber_str+"="+ applicationNumber;
		URI continuityDataUri = new URI(continuityDatabaseUrl);
		ResponseEntity<String> continuityData = null;

		// ---------------------------------------------------------------CORRESPONDENCE ADDRESS DATA----------------------------------------------------------------

		final String correspondingAddressBaseUrl = urlString+"OPSGPCDMServices/correspondence-address?"+applicationNumber_str+"="+ applicationNumber;
		URI correspondingAddressUri = new URI(correspondingAddressBaseUrl);
		ResponseEntity<String> correspondingAddressData = null;

		// ---------------------------------------------------------------address And  AttorneyDATA----------------------------------------------------------------

		final String addressAndAttorneyDatabaseUrl = urlString+"OPSGPCDMServices/application-interested-parties/attorneys?"+applicationNumber_str+"="+ applicationNumber;
		URI addressAndAttorneyDatauri = new URI(addressAndAttorneyDatabaseUrl);
		ResponseEntity<String> addressAndAttorneyData = null;

		// ---------------------------------------------------------------Maintenance Fee DATA----------------------------------------------------------------

		final String maintenacneFeeDatabaseUrl = urlString+"OPSGPCDMServices/fee-address?"+applicationNumber_str+"="+ applicationNumber;
		URI maintenacneFeeDatauri = new URI(maintenacneFeeDatabaseUrl);
		ResponseEntity<String> maintenacneFeeData = null;

		// ---------------------------------------------------------------fee Payment History DATA----------------------------------------------------------------

		
		  FpngServiceResponse serviceResponse = new FpngServiceResponse();
		  
		

		// ---------------------------------------------------------------Foreign Priority DATA----------------------------------------------------------------

		final String foreignPrioritybaseUrl = urlString+"OPSGPCDMServices/foreign-priority?"+applicationNumber_str+"="+ applicationNumber;
		URI foreignPriorityuri = new URI(foreignPrioritybaseUrl);
		ResponseEntity<String> foreignPriorityData = null;
		

		// ---------------------------------------------------------------applicants DATA----------------------------------------------------------------

		final String applicantsbaseUrl = urlString+"OPSGPCDMServices/application-interested-parties/applicants?"+applicationNumber_str+"="+ applicationNumber;
		URI applicantsuri = new URI(applicantsbaseUrl);
		ResponseEntity<String> applicantsData = null;
		
		
		// ---------------------------------------------------------------Entity Status DATA----------------------------------------------------------------

				final String entityStatusbaseUrl = urlString+"OPSGPCDMServices/entity-status?"+applicationNumber_str+"="+ applicationNumber;
				URI entityStatusuri = new URI(entityStatusbaseUrl);
				ResponseEntity<String> entityStatusData = null;
				
		// ---------------------------------------------------------------patent Term DATA----------------------------------------------------------------

		final String patentTermDatabaseUrl = urlString+"OPSGPCDMServices/patent-term?"+applicationNumber_str+"="+ applicationNumber;
		URI patentTermuri = new URI(patentTermDatabaseUrl);
		ResponseEntity<String> patentTermData = null;

		// ---------------------------------------------------------------Display Reference DATA----------------------------------------------------------------

		final String DisplayReferencebaseUrl = sdwpURLString+"DocAccessService/services/v2/application/mimeType/"+ (applicationNumber.contains("PCT")?appNumForDisplayRef:newApplicaitonNum);
		URI DisplayReferenceuri = new URI(DisplayReferencebaseUrl);
		ResponseEntity<String> displayReferenceData = null;

		// ---------------------------------------------------------------assignments DATA----------------------------------------------------------------
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String requestJson = "{\"applicationNumber\":\"" + newApplicaitonNum
				+ "\",\"requestedData\":[\"applicationInventors\",\"applicationAssignees\"]}";
		String assignmentsbaseUrl = urlString+"OPSGPCDMServices/application-patent-case/all-bib-data";
		HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
		URI assignmentsuri = new URI(assignmentsbaseUrl);

		// ---------------------------------------------------------------FIRST ACTION DATA----------------------------------------------------------------

		final String firstActionPredictionDatabaseUrl = urlString+"OPSGPCDMServices/first-action-prediction?"+applicationNumber_str+"="+ applicationNumber;
		URI firstActionPredictionDatauri = new URI(firstActionPredictionDatabaseUrl);
		ResponseEntity<String> firstActionPredictionData = null;
		// ---------------------------------------------------------------pregrant Publication DATA----------------------------------------------------------------
		final String pregrantPublicationbaseUrl = urlString+"OPSGPCDMServices/pregrant-publication?"+applicationNumber_str+"="+ applicationNumber;
		URI pregrantPublicationuri = new URI(pregrantPublicationbaseUrl);
		ResponseEntity<String> pregrantPublicationData = null;

		// ---------------------------------------------------------------WIPO Data for  internationalPublicationNumber----------------------------------------------------------------
		final String wipoAapplicationbaseUrl = urlString+"OPSGPCDMServices/wipo-application?pctApplicationNumber="+ applicationNumber;
		URI wipoAapplicationbaseUri = new URI(wipoAapplicationbaseUrl);
		ResponseEntity<String> wipoAapplicationData = null;
		
		// ---------------------------------------------------------------international Registration Number (Hague)----------------------------------------------------------------
		final String haguebaseUrl = urlString+"OPSGPCDMServices/application-history/hague?"+applicationNumber_str+"="+ applicationNumber;
		System.out.println(haguebaseUrl);
		URI haguebaseUri = new URI(haguebaseUrl);
		ResponseEntity<String> hagueData = null;

		// ---------------------------------------------------------------Examiner name----------------------------------------------------------------
		
		final String workerNumberUrl = urlString+"OPSGPCDMServices/application-examiner-history?"+applicationNumber_str+"="+ applicationNumber;
		URI workerNumberUri = new URI(workerNumberUrl);
		ResponseEntity<String> workerNumberData = null;
		ResponseEntity<String> examinerData = null;
		//https://opsg-api.sit.uspto.gov/OPSGPCDMServices/application-examiner-history?applicationNumber=15784512 
		
		ResponseEntity<String> examiner = null;	
			//https://infra-services.fqt.uspto.gov/infra-services/workerDetails/82003
				

		// ---------------------------------------------------------------Supplement-Content DATA----------------------------------------------------------------


		MetaDataRecordsType metaDataRecordsType = new MetaDataRecordsType();
		
		
	// ********************************************************************************************
		try {
			SSLUtil.turnOffSslChecking();
			String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
			log.info("Inside try block");
			applicationData = restTemplate.getForEntity(applicationUri, String.class);
			finalResult.put("applicationData", applicationData.getBody());
			activityLog(activityStartTime,Statistics.APPLICATIONDATAREQUEST, request);
			SSLUtil.turnOnSslChecking();
			JSONObject objJsonObject1 = new JSONObject(applicationData.getBody().toString());
			 customerNumberText = objJsonObject1.getString("customerNumberText");
			 applicationStatus = objJsonObject1.getString("statusNumber");
			 
		System.out.println(customerNumberText);
		System.out.println(custNumber);
		System.out.println((custNumber.contains(customerNumberText)));
		if(!(custNumber.contains(customerNumberText))) {
			return null;
		}
		} catch (Exception e) {
			log.error(e.getMessage());
			finalResult.put("applicationData", "No Data");
		}
		try {
			SSLUtil.turnOffSslChecking();
			String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
			
			workerNumberData = restTemplate.getForEntity(workerNumberUri, String.class);
			System.out.println("WORKER DETAIL NUM"+workerNumberData.getBody());
			JSONObject objJsonObject1 = new JSONObject(workerNumberData.getBody().toString());
			System.out.println(objJsonObject1.getString("results"));
			
			String objJsonObjectMeta1 = objJsonObject1.getString("results");
			 JSONArray jsonArray = new JSONArray(objJsonObjectMeta1);
			    System.out.println(jsonArray.length());
			    
			       String val = jsonArray.getJSONObject(0).getString("partyIdentifier");
			System.out.println(val);
			final String examinerUrl = infraURLString+"/infra-services/workerDetails/"+val;
			URI examinerUri = new URI(examinerUrl);
			examinerData = restTemplate.getForEntity(examinerUri, String.class);
			finalResult.put("examinerData", examinerData.getBody());
			activityLog(activityStartTime,Statistics.PUBLICATIONREVIEWDATAREQUEST, request);
			SSLUtil.turnOnSslChecking();
		} catch (Exception e) {
			log.error(e.getMessage());
			//finalResult.put("wipoAapplicationData", "No Data");

		}
		
		try {
			SSLUtil.turnOffSslChecking();
			   String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
			transactionHistoryData = restTemplate.getForEntity(transactionHistoryUri, String.class);
			finalResult.put("transactionHistoryData", transactionHistoryData.getBody());
			activityLog(activityStartTime,Statistics.TRANSACTIONHISTORYREQUEST, request);
			SSLUtil.turnOnSslChecking();
			 
		} catch (Exception e) {
			log.error(e.getMessage());
			finalResult.put("transactionHistoryData", "No Data");
		}
		try {
			SSLUtil.turnOffSslChecking();
			   String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
			continuityData = restTemplate.getForEntity(continuityDataUri, String.class);
			finalResult.put("continuityData", continuityData.getBody());
			activityLog(activityStartTime,Statistics.CONTINUITYDATAREQUEST, request);
			SSLUtil.turnOnSslChecking();
			
		} catch (Exception e) {
			log.error(e.getMessage());
			finalResult.put("continuityData", "No Data");
		}
		try {
			SSLUtil.turnOffSslChecking();
			   String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
			correspondingAddressData = restTemplate.getForEntity(correspondingAddressUri, String.class);
			finalResult.put("correspondingAddressData", correspondingAddressData.getBody());
			activityLog(activityStartTime,Statistics.CORRESPONDENCEADDRESSDATAREQUEST, request);
			SSLUtil.turnOnSslChecking();
			
		} catch (Exception e) {
			log.error(e.getMessage());
			finalResult.put("correspondingAddressData", "No Data");
		}
		try {
			SSLUtil.turnOffSslChecking();
			   String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
			addressAndAttorneyData = restTemplate.getForEntity(addressAndAttorneyDatauri, String.class);
			finalResult.put("addressAndAttorneyData", addressAndAttorneyData.getBody());
			activityLog(activityStartTime,Statistics.ATTORNEYADDRESSDATAREQUEST, request);
			SSLUtil.turnOnSslChecking();
			
		} catch (Exception e) {
			log.error(e.getMessage());
			finalResult.put("addressAndAttorneyData", "No Data");
		}
		try {
			SSLUtil.turnOffSslChecking();
			String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
			maintenacneFeeData = restTemplate.getForEntity(maintenacneFeeDatauri, String.class);
			finalResult.put("maintenacneFeeData", maintenacneFeeData.getBody());
			activityLog(activityStartTime,Statistics.MAINTENANCEFEEDATAREQUEST, request);
			SSLUtil.turnOnSslChecking();

		} catch (Exception e) {
			log.error(e.getMessage());
			finalResult.put("maintenacneFeeData", "No Data");
		}
		try {

			SSLUtil.turnOffSslChecking();
			
			   String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
			   serviceResponse= fpngRestClientService.getFeePaymentRecsByAppNum(applicationNumber_str.equalsIgnoreCase("applicationNumber")?applicationNumber:newApplicaitonNum); 
				
			   String resultJson = gson.toJson(serviceResponse);

			   if(serviceResponse.getModel()!=null && serviceResponse.getModel().get(0).getNumResults()==0){
				   finalResult.put("feePaymentHistoryData", "No Data");
			   }else {
				finalResult.put("feePaymentHistoryData", resultJson);
				activityLog(activityStartTime,Statistics.FEESDATAREQUEST, request);
			   }
		}
	catch (Exception e) {
		log.error(e.getMessage());
		finalResult.put("feePaymentHistoryData", "No Data");

	}
		try {
			SSLUtil.turnOffSslChecking();
			 String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
							foreignPriorityData = restTemplate.getForEntity(foreignPriorityuri, String.class);
			finalResult.put("foreignPriorityData", foreignPriorityData.getBody());
			activityLog(activityStartTime,Statistics.FOREIGNPRIORITYDATAREQUEST, request);
			SSLUtil.turnOnSslChecking();
		} catch (Exception e) {
			log.error(e.getMessage());
			finalResult.put("foreignPriorityData", "No Data");

		}
		
		try {
			SSLUtil.turnOffSslChecking();
			 String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
							applicantsData = restTemplate.getForEntity(applicantsuri, String.class);
			finalResult.put("applicantsData", applicantsData.getBody());
			activityLog(activityStartTime,Statistics.FOREIGNPRIORITYDATAREQUEST, request);
			SSLUtil.turnOnSslChecking();
		} catch (Exception e) {
			log.error(e.getMessage());
			finalResult.put("applicantsData", "No Data");

		}
		
		try {
			SSLUtil.turnOffSslChecking();
			 String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
							entityStatusData = restTemplate.getForEntity(entityStatusuri, String.class);
			finalResult.put("entityStatusData", entityStatusData.getBody());
			activityLog(activityStartTime,Statistics.FOREIGNPRIORITYDATAREQUEST, request);
			SSLUtil.turnOnSslChecking();
		} catch (Exception e) {
			log.error(e.getMessage());
			finalResult.put("entityStatusData", "No Data");

		}
		try {
			SSLUtil.turnOffSslChecking();
			 String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
				
			patentTermData = restTemplate.getForEntity(patentTermuri, String.class);
			finalResult.put("patentTermData", patentTermData.getBody());
			activityLog(activityStartTime,Statistics.PATENTTERMDATAREQUEST, request);
			SSLUtil.turnOnSslChecking();
			
		} catch (Exception e) {
			log.error(e.getMessage());
			finalResult.put("patentTermData", "No Data");
		}
		try {
			SSLUtil.turnOffSslChecking();
			String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
			
			String assignmentsData = restTemplate.postForObject(assignmentsbaseUrl, entity, String.class);
			finalResult.put("inventorData", assignmentsData);
			if(assignmentsData.contains("organization")) {
			finalResult.put("assignmentData", assignmentsData);
			}else {
				finalResult.put("assignmentData", "No Data");
			}
			System.out.println("assignmentData"+assignmentsData.contains("organization"));
			activityLog(activityStartTime,Statistics.ASSIGNMENTSDATAREQUEST, request);
			SSLUtil.turnOnSslChecking();
		}

		catch (Exception e) {
			log.error(e.getMessage());
			finalResult.put("assignmentData", "No Data");
		}
		try {
			SSLUtil.turnOffSslChecking();
			metaDataRecordsType= scoreSoapClient.getMetaData(applicationNumber_str.equalsIgnoreCase("applicationNumber")?applicationNumber:newApplicaitonNum);
			List<MetaDataType> listMetaData = new ArrayList<MetaDataType>();
			listMetaData= metaDataRecordsType.getMetaDataRecord().stream().filter(res->res.getMailroomDateText()!=null).collect(Collectors.toList());

			Map map = new HashMap();
			List<ScoreResult> tempList= new ArrayList<ScoreResult>();
			for(MetaDataType mType:listMetaData) {
				ScoreResult sR= new ScoreResult();
				sR.setMetaDataType(getMegaItemName(mType.getMegaItem()));
				sR.setTotalCount(mType.getMegaItemTotal());
				sR.setMegaItem(mType.getMegaItem());
				tempList.add(sR);
			}
			String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
			//String resultJson = gson.toJson(listMetaData.get(0).getMegaItemTotal());
			String resultJson = gson.toJson(tempList);
			finalResult.put("supplementContentMetaData", resultJson);
			activityLog(activityStartTime,Statistics.SUPPLEMENTDATAREQUEST, request);
			//finalResult.put("supplementContentMetaData", "No Data");

		}
		catch (Exception e) {
			log.error(e.getMessage());
			finalResult.put("supplementContentMetaData", "No Data");

		}
		try {
			SSLUtil.turnOffSslChecking();

			String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
			displayReferenceData = restTemplate.getForEntity(DisplayReferenceuri, String.class);
			finalResult.put("displayReferenceData", displayReferenceData.getBody());
			try {
				Gson gson=new Gson();
				String[] stringArray = {"SE.ITEM.LIST", "1449", "IDS", "892","NPL","FOR"};
		        List<String> stringList = new ArrayList(Arrays.asList(stringArray));
				DisplayReferences displayReferenceObj = gson.fromJson(displayReferenceData.getBody(), DisplayReferences.class);	
				List<DocumentBag> documentList = new ArrayList<DocumentBag>();
				documentList=displayReferenceObj.resultBag.get(0).getDocumentBag().stream().filter(c-> stringList.contains(c.documentCode.toString())).collect(Collectors.toList());;
				if(documentList.size()<1) {
					finalResult.put("displayReferenceObjData", "No Data");
				}else {
					String jsondocumentList = gson.toJson(documentList);
					finalResult.put("displayReferenceObjData", jsondocumentList);
				}
				
			}catch(Exception ex) {
				log.error(ex.getMessage());				
				finalResult.put("displayReferenceObjData", "No Data");
			}
				
			activityLog(activityStartTime,Statistics.IMAGEFILEWRAPPERDATAREQUEST, request);
			SSLUtil.turnOnSslChecking();
			} catch (Exception e) {
			log.error(e.getMessage());
			finalResult.put("displayReferenceData", "No Data");			
		}
		try {
			SSLUtil.turnOffSslChecking();
			String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
			firstActionPredictionData = restTemplate.getForEntity(firstActionPredictionDatauri, String.class);
			System.out.println(Integer.parseInt(applicationStatus));
			if(applicationNumber.contains("PCT") || !(Integer.parseInt(applicationStatus)>19 && Integer.parseInt(applicationStatus)<31 )) {
				finalResult.put("firstActionPredictionData", "No Data");
			}else {
			finalResult.put("firstActionPredictionData", firstActionPredictionData.getBody());
			}
			activityLog(activityStartTime,Statistics.FIRSTACTIONPREDICTIONDATAREQUEST, request);
			SSLUtil.turnOnSslChecking();
			} catch (Exception e) {
			log.error(e.getMessage());
			finalResult.put("firstActionPredictionData", "No Data");
		}
		try {
			SSLUtil.turnOffSslChecking();
			String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
			pregrantPublicationData = restTemplate.getForEntity(pregrantPublicationuri, String.class);
			finalResult.put("pregrantPublicationData", pregrantPublicationData.getBody());
			activityLog(activityStartTime,Statistics.PUBLICATIONREVIEWDATAREQUEST, request);
			SSLUtil.turnOnSslChecking();
			} catch (Exception e) {
			log.error(e.getMessage());
			finalResult.put("pregrantPublicationData", "No Data");
			
		}
		try {
			SSLUtil.turnOffSslChecking();
			String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
			wipoAapplicationData = restTemplate.getForEntity(wipoAapplicationbaseUri, String.class);
			finalResult.put("wipoAapplicationData", wipoAapplicationData.getBody());
			activityLog(activityStartTime,Statistics.PUBLICATIONREVIEWDATAREQUEST, request);
			SSLUtil.turnOnSslChecking();
		} catch (Exception e) {
			log.error(e.getMessage());
			finalResult.put("wipoAapplicationData", "No Data");

		}
		try {
			SSLUtil.turnOffSslChecking();
			String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
			hagueData = restTemplate.getForEntity(haguebaseUri, String.class);
			finalResult.put("hagueData", hagueData.getBody());
			SSLUtil.turnOnSslChecking();
			} catch (Exception e) {
			log.error(e.getMessage());
			finalResult.put("hagueData", "No Data");
		}
		

		boolean allValuesAreNull = finalResult.values()
		        .stream()
		        .allMatch(str-> str.equalsIgnoreCase("No Data"));
		log.info("boolean"+allValuesAreNull);
		if (finalResult.isEmpty() || allValuesAreNull || (!(custNumber.contains(customerNumberText))) ) {
			finalResult = null;
		}
		return finalResult;
	}
	
	public String getMegaItemName(String num) {
		HashMap metaDataTypes = new HashMap();
		metaDataTypes.put("2", SEQUENCES);
		metaDataTypes.put("3", MEGATABLES);
		metaDataTypes.put("4", SEARCH_RESULTS);
		metaDataTypes.put("5", COMPUTER_PROGRAM_LISTINGS_CPL);
		metaDataTypes.put("6", INTERIM_COPY_FRPR_TXT);
		metaDataTypes.put("7", US_PATENTS_W_COLOR);
		metaDataTypes.put("8", CERTIFIED_FRP_RS_W_COLOR);			
		metaDataTypes.put("9", APPENDICES);
		metaDataTypes.put("10",DRAWINGS);
		metaDataTypes.put("11",CHEMICAL_FORMULAS);
		metaDataTypes.put("12",MATHEMATICAL_FORMULAS);
		metaDataTypes.put("13",RED_BOOK);
		metaDataTypes.put("14",YELLOW_BOOK);
		metaDataTypes.put("15",_3D_PROTEIN_CRYSTALS);
		
		return (String) metaDataTypes.get(num);
	}
	
	@RequestMapping(value = "/OPSGApplicationGeneralInfoServices/getApplicationNumberSearchNumber", method = RequestMethod.GET)
	@ResponseBody
	public String getApplicationNumberSearchNumber(@RequestParam() String applicationNumber,@RequestParam() String applicationNumberStr,HttpServletRequest request)
			throws URISyntaxException, Exception {
		log.info("Inside getApplicationNumberSearchNumber:");		
		return getApplicationNumberFrmService(applicationNumber, applicationNumberStr);
	}
	
	public String getApplicationNumberFrmService(String applicationNumber, String applicationNumber_str) {
		System.out.println("inside getApplicationNumberFrmService");
		ResponseEntity<String> applicationData = null;
		JSONObject jsonObj =null;
		String url = urlString+"OPSGPCDMServices/application-general-information?publicStatus=true&"+applicationNumber_str+"="+ applicationNumber;
		try {
			SSLUtil.turnOffSslChecking();
			String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
			log.info("Inside try block");
			applicationData = restTemplate.getForEntity(url, String.class);
			jsonObj = new JSONObject(applicationData.getBody());		
			System.out.println("ApplicationNumber->>"+jsonObj.getString("applicationIdentifier"));
			Gson gson=new Gson();
			ApplicationInfo appInfo = gson.fromJson(applicationData.getBody(), ApplicationInfo.class);			
			SSLUtil.turnOnSslChecking();
			java.util.Date issueDate=new java.util.Date((long)appInfo.getGrantDate()*1000);
			Date today = new Date();
			System.out.println("issueDate" + issueDate);
			System.out.println("currentDate"+ today);
			if((issueDate.getTime() > today.getTime())) {
			    System.out.println("Current patent issue date is greater");
			    SimpleDateFormat DateFor = new SimpleDateFormat("MM-dd-yyyy");
			    String stringDate= DateFor.format(issueDate);
			    return ResponseEntity.ok(gson.toJson(stringDate)).getBody();
			}
			System.out.println("8"+appInfo.getApplicationIdentifier().getApplicationNumberText().trim());
			return "8"+appInfo.getApplicationIdentifier().getApplicationNumberText().trim();
		} catch (Exception e) {
			log.error(e.getMessage());
			
		}
		
		return null;
		
	}
	
	
	  private void activityLog(String activityStartTime, int activityName,HttpServletRequest request) {
		  String activityEndTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
		  long startTimeMillis = System.currentTimeMillis();
	
		String statsActivity = Statistics.addPairAdminActivity(pairId, dn,
				activityName  , activityStartTime, activityEndTime, "0", startTimeMillis, 0, "0",
                  IpAddressUtil.getClientIP(request));

          log.info("Stats Activity: {}",statsActivity);
	    }

	@RequestMapping(value = "/Supplement-ScoreServices/getSupplementMegaDataFromMetaData", method = RequestMethod.GET)
	@ResponseBody
	public String getSupplementMegaDataFromMetaData(@RequestParam() String applicationNumber,
			@RequestParam() String megaItem, HttpServletRequest request)
			throws URISyntaxException, Exception {
		log.info("Inside getApplicationData:");
		String megaItemType= null;
		String newApplicaitonNum = null, applicationNumber_str=null;
		if(applicationNumber.contains("DM")) {
			 applicationNumber_str = "internationalRegistrationNumber";
			 newApplicaitonNum= getApplicationNumberFrmService(applicationNumber, applicationNumber_str);
			}
		Map<String, String> finalResult = new HashMap<>();
		List<SupplementDataType> SupplementDataTypeList = new ArrayList<SupplementDataType>();
		RestTemplate restTemplate = new RestTemplate();
		MetaDataRecordsType metaDataRecordsType = new MetaDataRecordsType();
		metaDataRecordsType= scoreSoapClient.getMetaData(applicationNumber.contains("DM")?newApplicaitonNum:applicationNumber);
		List<MetaDataType> listMetaData = new ArrayList<MetaDataType>();
		listMetaData= metaDataRecordsType.getMetaDataRecord().stream().filter(res->res.getMailroomDateText()!=null).collect(Collectors.toList());

		/*calling Mega Version History*/
		MegaItemVerHistoryInputMessage mIIM = new MegaItemVerHistoryInputMessage();
		mIIM.setApplicationNumber(applicationNumber.contains("DM")?newApplicaitonNum:applicationNumber);
		/*
		 * for(MetaDataType lst: listMetaData){ megaItemType= lst.getMegaItem();
		 * mIIM.setMegaItemType(lst.getMegaItem()); }
		 */
		mIIM.setMegaItemType(megaItem);
		MegaItemVersionsType mRecordsType= new MegaItemVersionsType();
		mRecordsType= scoreMegaItemVersionHistoryImpl.getMegaItemVersionHistory(mIIM);
		List<MegaItemVersionType> list = new ArrayList<MegaItemVersionType>();
		list= mRecordsType.getMegaItemVersion();

		for (MegaItemVersionType megaItemVersionType: list){
			MegaItemsInputMessage mIIMObj = new MegaItemsInputMessage();
			mIIMObj.setApplictionNumber(applicationNumber.contains("DM")?newApplicaitonNum:applicationNumber);
			mIIMObj.setMegaItemType(megaItem);
			mIIMObj.setVersionNumber(Integer.toString(megaItemVersionType.getVersionNumber()));
			//mIIM.setSubVersionNumber(mIIM_Map.get("SubVersionNumbe").toString());
			MegaItemRecordsType megaItemRecordsType= new MegaItemRecordsType();
			megaItemRecordsType= scoreMegaItemImpl.getMegaItems(mIIMObj);
			List<MegaItemRecordType> megaItemRecordsTypeList = new ArrayList<MegaItemRecordType>();
			megaItemRecordsTypeList= megaItemRecordsType.getMegaItemRecord();
			//SupplementDataType supplementDataType = new SupplementDataType();
			for (MegaItemRecordType megaItemRecordType:megaItemRecordsTypeList){
				SupplementDataType supplementDataType = new SupplementDataType();
				supplementDataType.setVersionNumber(Integer.toString(megaItemVersionType.getVersionNumber()));
				supplementDataType.setItemSize(megaItemRecordType.getItemSize());
				supplementDataType.setItemTotal(megaItemRecordType.getItemTotal());
				supplementDataType.setMailroomDateText(megaItemRecordType.getMailroomDateText());
				supplementDataType.setProcessCode(megaItemRecordType.getProcessCode());
				supplementDataType.setMegaItemId(megaItemRecordType.getMegaItemId());
				supplementDataType.setMegaItemName(megaItemRecordType.getMegaItemName());
				SupplementDataTypeList.add(supplementDataType);
			}
		}
		String resultJson = gson.toJson(SupplementDataTypeList);
		return resultJson;
	}


	@PostMapping(value = "/ImageWrapper_SDWP/getPDFServiceDocument")
	public ResponseEntity	getPDFServiceDocument(@RequestBody Map downloadDataMap, HttpServletRequest request, HttpServletResponse response)
			throws IOException{
		String applicationNumber=downloadDataMap.get("fileTitleText").toString();
		log.info("Inside getPDFServiceDocument:"+applicationNumber);
		if(applicationNumber.contains("PCT")){//  PCT/US10/31909
			applicationNumber = applicationNumber.substring(4,8)+applicationNumber.substring(9,14);
			log.info("new PCT APP Number inside getDisplayReferenceData method call--> "+applicationNumber);
		}
		File file = null;
		RestTemplate restTemplate = new RestTemplate();
		/*http://sdwp-external.fqt.uspto.gov/PdfService/services/pdf/35500001.pdf*/
		final String baseURLPDFDownlaod= swdpExternalURL+"PdfService/services/pdf/"+applicationNumber+".pdf";
		try {

			return ResponseEntity
					.ok(restTemplate.postForObject(baseURLPDFDownlaod, downloadDataMap, byte[].class));
		}catch(Exception ex){
			log.info("Exception caught in getPDFServiceDocument block-->  "+ ex.getMessage());
		}
		return ResponseEntity
		.ok("No Data Found".getBytes());
	}

	@PostMapping(value = "/outgoingCorrespondence_SDWP/getPDFServiceDocument")
	public ResponseEntity	getPDFServiceDocumentForAllApp(@RequestBody Map downloadDataMap, HttpServletRequest request, HttpServletResponse response)
			throws IOException{

		log.info("Inside getPDFServiceDocument:"+downloadDataMap.get("fileTitleText"));
		File file = null;
		RestTemplate restTemplate = new RestTemplate();
		/*http://sdwp-external.fqt.uspto.gov/PdfService/services/pdf/35500001.pdf*/
		final String baseURLPDFDownlaod= swdpExternalURL+"PdfService/services/pdf/outgoingCorrespondence.pdf";
		try {

			return ResponseEntity
					.ok(restTemplate.postForObject(baseURLPDFDownlaod, downloadDataMap, byte[].class));
		}catch(Exception ex){
			log.info("Exception caught in getPDFServiceDocument block-->  "+ ex.getMessage());
		}
		return ResponseEntity
				.ok("No Data Found".getBytes());
	}

	@PutMapping(value = "/OPSGBPServices/updateAttroneyDocketNum")
	public String	updateAttroneyDocketNum(@RequestBody Map updateDocketForm)
			throws IOException{

		ResponseEntity<String> response=null;
		log.info("Inside updateAttroneyDocketNum:"+updateDocketForm.get("applicationFileReference"));
		File file = null;
		RestTemplate restTemplate = new RestTemplate();
		/*  PUT : https://opsg-api.sit.uspto.gov/OPSGPCDMServices/application-general-information  */
		final String baseURLPDFDownlaod= urlString+"OPSGPCDMServices/application-general-information?publicStatus=true&applicationNumber";
		try {
			SSLUtil.turnOffSslChecking();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<Map> entity = new HttpEntity<Map>(updateDocketForm, headers);
			return restTemplate.exchange(baseURLPDFDownlaod,HttpMethod.PUT,entity, String.class).getBody();
		}catch(Exception ex){
			log.info("Exception caught in getPDFServiceDocument block-->  "+ ex.getMessage());
		}
		return null;
	}

	@RequestMapping(value = "/DocAccessService/services", method = RequestMethod.GET)
	@ResponseBody
	public String getDisplayReferenceData(@RequestParam() String applicationNumber,HttpServletRequest request) throws URISyntaxException {

		//URI DisplayReferenceuri = new URI(DisplayReferencebaseUrl);
		String appNumForDisplayRef=null;
		if(applicationNumber.contains("PCT")){//  PCT/US10/31909
			if(applicationNumber.length()==14) { // to avoid array out of bound err
				applicationNumber =  applicationNumber.substring(4,8)+applicationNumber.substring(9,14);
				}
				if(applicationNumber.length()==17) { //  PCT/US2010/031909 // to avoid array out of bound err
					applicationNumber =  applicationNumber.substring(4,6)+applicationNumber.substring(8,10)+applicationNumber.substring(12,17);
				}
			log.info("new PCT APP Number inside getDisplayReferenceData method call--> "+appNumForDisplayRef);
		}
		final String DisplayReferencebaseUrl = sdwpURLString+"DocAccessService/services/v2/application/mimeType/"+ applicationNumber;
		ResponseEntity<String> displayReferenceData = null;
		RestTemplate restTemplate = new RestTemplate();
		log.info("URL-->  "+ DisplayReferencebaseUrl);
		try {
			SSLUtil.turnOffSslChecking();

			String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
			displayReferenceData = restTemplate.getForEntity(DisplayReferencebaseUrl, String.class);

			activityLog(activityStartTime,Statistics.IMAGEFILEWRAPPERDATAREQUEST, request);
			SSLUtil.turnOnSslChecking();

		} catch (Exception e) {
			log.error(e.getMessage());
			return "No Data";
		}
		return displayReferenceData.getBody();
	}

	@PostMapping(value = "/XML_DocumentSDWP/getPDFServiceDocument")
	public ResponseEntity	getXMLServiceDocument(@RequestBody Map downloadDataMap, HttpServletRequest request, HttpServletResponse response)
			throws IOException{
		/*
		 * Map<String, String> mapSearch = new HashMap<String, String>();
		 * mapSearch.put("applicationId","35504789");
		 * mapSearch.put("customerNumber","2292");
		 * mapSearch.put("docCategory","OUTGOING"); mapSearch.put("documentCode","NOA");
		 * mapSearch.put("documentId","K2MFZNRJRXEAPX4");
		 * mapSearch.put("mailDate","2020-01-01");
		 */
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity response1=null;
		List<MediaType> mediaList = new ArrayList<MediaType>();
		mediaList.add(MediaType.APPLICATION_JSON);
		mediaList.add(MediaType.APPLICATION_OCTET_STREAM);
		List temp = new ArrayList();		
		temp = (List) downloadDataMap.get("documentBagDownload");
		Map newMap = new HashMap();
		newMap = (Map) temp.get(0);
		Map<String, String>[] mapSearchMain= (Map<String, String>[]) new Map[1]; 
		mapSearchMain[0]=downloadDataMap;
		//Map<String, Object>[] myArray = (Map<String, Object>[]) new Map[10];
		/*http://sdwp-external.fqt.uspto.gov/PdfService/services/pdf/35500001.pdf*/
		
		final String baseURLPDFDownlaod= "http://patentcenter-legacy.pvt.uspto.gov/services/v1/applications/"+newMap.get("applicationId")+"/documents?type="+newMap.get("docType");
		log.info("baseURLPDFDownlaod:"+baseURLPDFDownlaod);
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("X-AUTH-TOKEN",  "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJQQUlSIiwiZmlyc3RuYW1lIjoiUEFJUiIsImN1c3RvbWVybnVtYmVycyI6W3sibmFtZSI6IkFkb2JlIFN5c3RlbXMgSW5jLiIsImlkIjoiMTc0MjUifSx7Im5hbWUiOiJBbWdlbiBQaGFybWEiLCJpZCI6IjE3NjU5In0seyJuYW1lIjoiQTJaIEhlYWx0aGNhcmUiLCJpZCI6IjE3ODI1In0seyJuYW1lIjoiQXZpYXRvciBTaG9lcyIsImlkIjoiMTc5NTkifSx7Im5hbWUiOiJCYW5rIG9mIEJhcm9kYSIsImlkIjoiMTc5NjAifV0sInJvbGVzIjpbIlJPTEVfQURNSU4iLCJST0xFX1BBSVIiXSwidXNlcmlkIjoiUEFJUiIsImxhc3RuYW1lIjoiU3lzdGVtIn0.qSeA_pVJRJpDWRBm83kUAWQd8WWLrUfIF_oTYd6-Lbi_t3vls3qMqA9P5FQ2EQu48xcvpQev45l-yOo1--0tSQ " );
			headers.setAccept(mediaList);
			//HttpEntity<Map<String, String>[]> request1 = new HttpEntity<Map<String, String>[]>(mapSearchMain,headers);
			HttpEntity<List> request1 = new HttpEntity<List>(temp,headers);

			response1 = restTemplate.exchange(baseURLPDFDownlaod,HttpMethod.POST,request1,byte[].class);
					//.ok(restTemplate.postForObject(baseURLPDFDownlaod, mapSearch, byte[].class));
		}catch(Exception ex){
			log.info("Exception caught in getPDFServiceDocument block-->  "+ ex.getMessage());
			ex.printStackTrace();
		}
		return response1;
		//ResponseEntity.ok("No Data Found".getBytes());
	}
	
	@PostMapping(value = "/XML_DocumentSDWP/getXMLDOCXMultiApplicationCall")
	public ResponseEntity	getXMLDOCXMultiApplicationCall(@RequestBody Map downloadDataMap, HttpServletRequest request, HttpServletResponse response)
			throws IOException{
		/*
		 * Map<String, String> mapSearch = new HashMap<String, String>();
		 * mapSearch.put("applicationId","35504789");
		 * mapSearch.put("customerNumber","2292");
		 * mapSearch.put("docCategory","OUTGOING"); mapSearch.put("documentCode","NOA");
		 * mapSearch.put("documentId","K2MFZNRJRXEAPX4");
		 * mapSearch.put("mailDate","2020-01-01");
		 */
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity response1=null;
		List<MediaType> mediaList = new ArrayList<MediaType>();
		mediaList.add(MediaType.APPLICATION_JSON);
		mediaList.add(MediaType.APPLICATION_OCTET_STREAM);
		List temp = new ArrayList();		
		temp = (List) downloadDataMap.get("documentBagDownload");
		Map newMap = new HashMap();
		newMap = (Map) temp.get(0);
		Map<String, String>[] mapSearchMain= (Map<String, String>[]) new Map[1]; 
		mapSearchMain[0]=downloadDataMap;
		//Map<String, Object>[] myArray = (Map<String, Object>[]) new Map[10];
		/*http://sdwp-external.fqt.uspto.gov/PdfService/services/pdf/35500001.pdf*/
		
		final String baseURLPDFDownlaod= "http://patentcenter-legacy.pvt.uspto.gov/services/v1/applications/documents?type="+newMap.get("docType");
		log.info("baseURLPDFDownlaod:"+baseURLPDFDownlaod);
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.add("X-AUTH-TOKEN",  "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJQQUlSIiwiZmlyc3RuYW1lIjoiUEFJUiIsImN1c3RvbWVybnVtYmVycyI6W3sibmFtZSI6IkFkb2JlIFN5c3RlbXMgSW5jLiIsImlkIjoiMTc0MjUifSx7Im5hbWUiOiJBbWdlbiBQaGFybWEiLCJpZCI6IjE3NjU5In0seyJuYW1lIjoiQTJaIEhlYWx0aGNhcmUiLCJpZCI6IjE3ODI1In0seyJuYW1lIjoiQXZpYXRvciBTaG9lcyIsImlkIjoiMTc5NTkifSx7Im5hbWUiOiJCYW5rIG9mIEJhcm9kYSIsImlkIjoiMTc5NjAifV0sInJvbGVzIjpbIlJPTEVfQURNSU4iLCJST0xFX1BBSVIiXSwidXNlcmlkIjoiUEFJUiIsImxhc3RuYW1lIjoiU3lzdGVtIn0.qSeA_pVJRJpDWRBm83kUAWQd8WWLrUfIF_oTYd6-Lbi_t3vls3qMqA9P5FQ2EQu48xcvpQev45l-yOo1--0tSQ " );
			headers.setAccept(mediaList);
			//HttpEntity<Map<String, String>[]> request1 = new HttpEntity<Map<String, String>[]>(mapSearchMain,headers);
			HttpEntity<List> request1 = new HttpEntity<List>(temp,headers);

			response1 = restTemplate.exchange(baseURLPDFDownlaod,HttpMethod.POST,request1,byte[].class);
					//.ok(restTemplate.postForObject(baseURLPDFDownlaod, mapSearch, byte[].class));
		}catch(Exception ex){
			log.info("Exception caught in getPDFServiceDocument block-->  "+ ex.getMessage());
			ex.printStackTrace();
		}
		return response1;
		//ResponseEntity.ok("No Data Found".getBytes());
	}

	@PostMapping(value = "/OPSGSEARCHServices/search")
	public String getApplicationsByDocketNumber(@RequestBody String  downloadDataMap, HttpServletRequest request) throws URISyntaxException {
		final String ApplicationsByDocketNumberUrl = opsgSearchURLString+"OPSGSEARCHServices/search/search-documents/v1";
		ResponseEntity<String> applicationsByDocketNumberData = null;
		RestTemplate restTemplate = new RestTemplate();
		log.info("URL-->  "+ ApplicationsByDocketNumberUrl);
		try {
			SSLUtil.turnOffSslChecking();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> request1 = new HttpEntity<String>( downloadDataMap,headers);
			applicationsByDocketNumberData = restTemplate.exchange(ApplicationsByDocketNumberUrl,HttpMethod.POST,request1,String.class);
			SSLUtil.turnOnSslChecking();

		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			return e.getMessage().toString();
		}
		return applicationsByDocketNumberData.getBody();
	}

	@RequestMapping(value = "/PublicationData/services", method = RequestMethod.GET)
	@ResponseBody
	public String getPublicationData(@RequestParam String applicationNumber,HttpServletRequest request) {
		ResponseEntity<String> applicationData = null;
		JSONObject jsonObj =null;
		String url = urlString+"OPSGPCDMServices/pregrant-publication?applicationNumber="+ applicationNumber;
		try {
			SSLUtil.turnOffSslChecking();
			String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
			log.info("Inside try block");
			applicationData = restTemplate.getForEntity(url, String.class);

			SSLUtil.turnOnSslChecking();
			log.info("got Publication Data-->"+applicationData.getBody());
			return applicationData.getBody();
		} catch (Exception e) {
			log.error(e.getMessage());
			applicationData= ResponseEntity.ok(gson.toJson("No Data"));
			 return applicationData.getBody();
		}

	}
	
	@RequestMapping(value = "/wipoData/internationalPCTfor17", method = RequestMethod.GET)
	@ResponseBody
	public String internationalPCTfor17(@RequestParam String pctApplicationNumber,HttpServletRequest request) {
		ResponseEntity<String> applicationData = null;
		JSONObject jsonObj =null;
		String url = urlString+"OPSGPCDMServices/wipo-application?pctApplicationNumber="+ pctApplicationNumber;
		try {
			SSLUtil.turnOffSslChecking();
			String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
			log.info("Inside try block");
			applicationData = restTemplate.getForEntity(url, String.class);

			SSLUtil.turnOnSslChecking();
			log.info("got wipoData internationalPCT-->"+applicationData.getBody());
			return applicationData.getBody();
		} catch (Exception e) {
			log.error(e.getMessage());
			applicationData= ResponseEntity.ok(gson.toJson("No Data"));
			 return applicationData.getBody();
		}

	}
	
	@RequestMapping(value = "/wipoData/internationalPCTfor14", method = RequestMethod.GET)
	@ResponseBody
	public String internationalPCTfor14(@RequestParam String pctApplicationNumber,HttpServletRequest request) {
		ResponseEntity<String> applicationData = null;
		JSONObject jsonObj =null;
		String url = urlString+"OPSGPCDMServices/wipo-application?pctApplicationNumber="+ pctApplicationNumber;
		try {
			SSLUtil.turnOffSslChecking();
			String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
			log.info("Inside try block");
			applicationData = restTemplate.getForEntity(url, String.class);

			SSLUtil.turnOnSslChecking();
			log.info("got wipoData internationalPCT-->"+applicationData.getBody());
			return applicationData.getBody();
		} catch (Exception e) {
			log.error(e.getMessage());
			applicationData= ResponseEntity.ok(gson.toJson("No Data"));
			 return applicationData.getBody();
		}

	}
	
	public String getAppInfoFrmAppNumAndPatentNum(String applicationNumber, String patentNumber) {
		System.out.println("inside getAppInfoFrmAppNumAndPatentNum");
		ResponseEntity<String> applicationData = null;
		JSONObject jsonObj =null;
		log.info(urlString+"OPSGPCDMServices/application-general-information?publicStatus=true&applicationNumber="+ applicationNumber);
		String url = urlString+"OPSGPCDMServices/application-general-information?publicStatus=true&applicationNumber="+ applicationNumber;
		try {
			SSLUtil.turnOffSslChecking();
			String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
			log.info("Inside try block");
			applicationData = restTemplate.getForEntity(url, String.class);
			jsonObj = new JSONObject(applicationData.getBody());		
			System.out.println("ApplicationNumber->>"+jsonObj.getString("patentNumber"));
			Gson gson=new Gson();
			ApplicationInfo appInfo = gson.fromJson(applicationData.getBody(), ApplicationInfo.class);			
			SSLUtil.turnOnSslChecking();
			if(appInfo.getPatentNumber().replaceAll("(,)", "").trim()==patentNumber.replaceAll("(,)", "").trim()) {
			return appInfo.getApplicationIdentifier().getApplicationNumberText().trim();
			}else {
				return null;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			
		}
		
		return null;
		
	}
	
	public  List<String> getApplicationAttorneys(String applicationNumber, String patentNumber) {
		System.out.println("inside getApplicationAttorneys");
		ResponseEntity<String> attorneyData = null;
		JSONObject jsonObj =null;
		System.out.println("OPSGPCDMServices/application-interested-parties/attorneys?applicationNumber="+ applicationNumber +"&patentNumber="+ patentNumber);
		String url = urlString+"OPSGPCDMServices/application-interested-parties/attorneys?applicationNumber="+ applicationNumber +"&patentNumber="+ patentNumber;
		try {
			SSLUtil.turnOffSslChecking();
			String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
			log.info("Inside try block");
			attorneyData = restTemplate.getForEntity(url, String.class);
			jsonObj = new JSONObject(attorneyData.getBody());		
			System.out.println("ApplicationNumber->>"+jsonObj.getString("applicationIdentifier"));
			JSONArray jsonArray = new JSONArray(jsonObj.getString("attorneys").toString());
			System.out.println(jsonArray);
			  List<String> registrationNumbers = new ArrayList<String>();
	    
			  for (int i = 0, size = jsonArray.length(); i < size; i++)
			    {
				  System.out.println(jsonArray.getJSONObject(i).getString("registrationNumber").toString());
                registrationNumbers.add(jsonArray.getJSONObject(i).getString("registrationNumber").toString());
            }			
			SSLUtil.turnOnSslChecking();
			return registrationNumbers;
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}
		
		
		
	}
	
	public void printObject(Object object) {
	    Gson gson = new GsonBuilder().setPrettyPrinting().create();
	    System.out.println(gson.toJson(object));
	}
	
	public String associateApplicationCorrespondenceAddress(ApplicationAddressReqType req) {
		 Gson gson = new Gson();
		System.out.println("inside associateApplicationCorrespondenceAddress");
		String associateData = null;
		JSONObject jsonObj =null;
		String url = urlString+"OPSGBPServices/customer-number/associate-application-correspondence-address/v1";
		System.out.println(url);
		printObject(req);
		try {
			SSLUtil.turnOffSslChecking();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<ApplicationAddressReqType> entity = new HttpEntity<ApplicationAddressReqType>(req, headers);
			return restTemplate.exchange(url,HttpMethod.PUT,entity, String.class).getBody();
		}catch(Exception ex){
			
			log.info("Exception caught in associateApplicationCorrespondenceAddress block-->  "+ ex.getMessage());
			log.info("Exception caught in associateApplicationCorrespondenceAddress block-->  "+ ex.getMessage());
			if(ex.getMessage().contains("All applications have failed because they have already been used")) {
				log.info("check :"+ex.getMessage().contains("All applications have failed because they have already been used"));
				log.info("did go :All applications have failed because they have already been used");
				log.info("return :"+gson.toJson(req));
				return gson.toJson(req);
			}else {
				log.info("Didnt go : All applications have failed because they have already been used");
				return null;
			}
		}
		
	}
	
	
	public String associateApplicationCorrespondenceAddressMaintenanceFee(ApplicationAddressReqType req) {
		System.out.println("inside associateApplicationCorrespondenceAddressMaintenanceFee");
		String associateData = null;
		JSONObject jsonObj =null;
		String url = urlString+"OPSGBPServices/customer-number/associate-application-maintenance-fee-address/v1";
		System.out.println(url);
		printObject(req);
		try {
			SSLUtil.turnOffSslChecking();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<ApplicationAddressReqType> entity = new HttpEntity<ApplicationAddressReqType>(req, headers);
			return restTemplate.exchange(url,HttpMethod.PUT,entity, String.class).getBody();
		}catch(Exception ex){
			log.info("Exception caught in associateApplicationCorrespondenceAddressMaintenanceFee block-->  "+ ex.getMessage());
			log.info("check :"+ex.getMessage().contains("All applications have failed because they have already been used"));
			if(ex.getMessage().contains("All applications have failed because they have already been used")) {
				log.info("did go :All applications have failed because they have already been used");
				log.info("return :"+gson.toJson(req));
				return gson.toJson(req);
			}else {
				log.info("Didnt go : All applications have failed because they have already been used");
				return null;
			}
			
		}
		//return null;
	}
		
	


	@RequestMapping(value = "/getAllBibData", method = RequestMethod.GET)
	public String  getAllBibData( @RequestParam String applicationNumber, @RequestParam() List custNumber,
								  @RequestParam String applicationStr,   HttpServletRequest request) {
		ResponseEntity<String> applicationData = null;
		String applicationNum=null;
		String actualApplicationNum=null;
		if(applicationStr.equalsIgnoreCase("applicationNumber")){
			applicationNum="applicationNumber";
		}else if(applicationStr.equalsIgnoreCase("patentNumber")){
			applicationNum="patentNumber";
		}
		else if(applicationStr.equalsIgnoreCase("publicationNumber")){
			applicationNum="publicationNumber";
			applicationNumber=applicationNumber.substring(3,7)+applicationNumber.substring(8,15);

		}
		else if(applicationStr.equalsIgnoreCase("InternationalDesignRegistrationNumber")){

			applicationNum="applicationNumber";
			try {
				SSLUtil.turnOffSslChecking();
				String urlToGetApp = urlStringPP + "OPSGApplicationGeneralInfoServices/getApplicationNumberSearchNumber?applicationNumber=" + applicationNumber + "&applicationNumberStr=internationalRegistrationNumber";
				applicationNumber = restTemplate.getForEntity(urlToGetApp, String.class).getBody();
				SSLUtil.turnOnSslChecking();
			}catch(Exception ex){
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			}
		}
		RestTemplate restTemplate = new RestTemplate();
		String url = urlString+"OPSGPCDMServices/application-patent-case/all-bib-data";
		String customerNumberText;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String requestJson = "{\""+applicationNum+"\":\"" + applicationNumber
				+ "\",\"requestedData\":[\"all\"]}";
		HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
		try {
			SSLUtil.turnOffSslChecking();
			applicationData= restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
			JSONObject objJsonObject1 = new JSONObject(applicationData.getBody().toString());
			JSONObject applicationMetaData = objJsonObject1.getJSONObject("applicationMetaData");
			customerNumberText = applicationMetaData.getString("customerNumberText");
			//customerNumberText = objJsonObject1.getJSONObject("applicationMetaData");
			System.out.println(customerNumberText);
			System.out.println(custNumber);
			System.out.println((custNumber.contains(customerNumberText)));
			if(!custNumber.contains(customerNumberText)) {
				return null;
			}
			SSLUtil.turnOnSslChecking();
			return applicationData.getBody();
		}catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	
	@RequestMapping(value = "/getApplicationGeneralInfo", method = RequestMethod.GET)
	public String getApplicationGeneralInfo(@RequestParam  String applicationNumber,HttpServletRequest request) {
		System.out.println("inside getApplicationNumberFrmService");
		ResponseEntity<String> applicationData = null;
		JSONObject jsonObj =null;
		String url = urlString+"OPSGPCDMServices/application-general-information?applicationNumber="+ applicationNumber;
		try {
			SSLUtil.turnOffSslChecking();
			String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
			log.info("Inside try block");
			applicationData = restTemplate.getForEntity(url, String.class);
			/*jsonObj = new JSONObject(applicationData.getBody());
			System.out.println("ApplicationNumber->>"+jsonObj.getString("applicationIdentifier"));
			Gson gson=new Gson();
			ApplicationInfo appInfo = gson.fromJson(applicationData.getBody(), ApplicationInfo.class);*/
			SSLUtil.turnOnSslChecking();
			return applicationData.getBody();
		} catch (Exception e) {
			log.error(e.getMessage());
			return "No Data";
		}

	}
	
	@RequestMapping(value = "/getFormatInputPatentNumber", method = RequestMethod.GET)
	 public String formatInputPatentNumber(@RequestParam String patentNumber,   HttpServletRequest request) {
			/* This code will use a REGular EXpression to validate the Patent Number and then use another REGEX to format the Patent Number
			 * with commas.  The REGEX and messages for the Patent Number are now in the ofi4.properties file.
			 *
			 * Mark Wheeler		Salient CRGT		2017-11-07		Utility Patent Number Expansion (UPNE) Project
			 */

			String tobereturned = "-";
			
			try {
				if (patentNumber.matches(PatentNumberRegex)) {
					tobereturned = patentNumber.replaceAll(FormatPatentNumber, "$1,");
				}
			} catch( Exception e) {
				e.printStackTrace();
			}
			return ResponseEntity.ok(gson.toJson(tobereturned)).getBody();
	    }

	@RequestMapping(value = "/file-content-transaction-history", method = RequestMethod.GET)
	public String getApplicationFileContentTransactionHistory(@RequestParam  String applicationNumber,HttpServletRequest request) {
		System.out.println("inside getApplicationNumberFrmService");
		ResponseEntity<String> applicationData = null;
		JSONObject jsonObj =null;
		String url = urlString+"OPSGPCDMServices/file-content-transaction-history/v1?applicationNumber="+ applicationNumber;
		try {
			SSLUtil.turnOffSslChecking();
			String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
			log.info("Inside try block");
			applicationData = restTemplate.getForEntity(url, String.class);			
			SSLUtil.turnOnSslChecking();
			return applicationData.getBody();
		} catch (Exception e) {
			log.error(e.getMessage());
			return "No Data";
		}

	}
	
	@PostMapping(value = "/getCustomerStatusXMLDownload")
    public ApplicationByCustomer  getCustomerStatusXMLDownload(@RequestBody List<AppListByCustNumRow> outGoingList, HttpServletRequest request, HttpServletResponse response)
            throws ServiceException, ClassNotFoundException, SQLException, URISyntaxException {
        log.debug("inside getDownloadOutGoingList controller--->"+ outGoingList.size());
       
        ApplicationByCustomer ac=new ApplicationByCustomer();
        List<ApplicationStatusData> applicationStatusData=new ArrayList<ApplicationStatusData>();
        for(AppListByCustNumRow acRow:outGoingList) {
        	Gson gson=new Gson();
			ApplicationInfo appInfo = gson.fromJson(getApplicationGeneralInfo(acRow.getAppNumber(), null), ApplicationInfo.class);		
			TransactionHistory tranHistory=gson.fromJson(getApplicationFileContentTransactionHistory(acRow.getAppNumber(),null), TransactionHistory.class);
			Results rs = new Results();
			//Stream<Results> tResult=tranHistory.getResults().stream().filter(fil->fil.getAudit().getLastModifiedUser().equalsIgnoreCase(appInfo.getAudit().getLastModifiedUser()));
        	for(Results res:tranHistory.getResults() ) {
        		if(res.getAudit().getLastModifiedUser().equalsIgnoreCase(appInfo.getAudit().getLastModifiedUser())) {
        			rs=res;
        		}
        	}
        	ApplicationStatusData appStatusData = new ApplicationStatusData();
        	appStatusData.setApplicationNumber(acRow.getAppNumber());
        	appStatusData.setApplicationStatusCode(appInfo.getStatusNumber());
        	appStatusData.setApplicationStatusDate(subStringDate(acRow.getStatusDate()));
        	appStatusData.setApplicationStatusText(appInfo.getApplicationStatusDescriptionText());
        	appStatusData.setEarliestPublicationNumber(acRow.getEarliestPubNo());
        	appStatusData.setFilingDate(subStringDate(acRow.getFilingDate()));
        	appStatusData.setLastModifiedTimestamp(convertDateWithTimeStamp(String.valueOf(appInfo.getAudit().getLastModifiedTime())));
        	appStatusData.setCustomerNumber(acRow.getCustomerNumber());
        	appStatusData.setAttorneyDocketNumber(acRow.getAttorneyDocNo());

        	if(rs!=null&& rs.getAudit()!=null){
				LastFileHistoryTransaction lastTran= new LastFileHistoryTransaction();
				lastTran.setLastTransactionDate(convertDate(String.valueOf(rs.getAudit().getLastModifiedTime())));
				lastTran.setLastTransactionDescription(rs.getCaseActionDescriptionText());
				appStatusData.setLastFileHistoryTransaction(lastTran);
			}

			if(rs!=null&& rs.getPublicIndicator()!=null) {
				appStatusData.setImageAvailabilityIndicator(rs.getPublicIndicator());
			}
        	JSONObject objJsonObject1=null;
        	ExaminerName eName = new ExaminerName();
        	try {
				objJsonObject1 = new JSONObject(getExaminerName(acRow.getAppNumber()));
				String middleName = objJsonObject1.getString("middleName").equalsIgnoreCase("null")?" ":objJsonObject1.getString("middleName").toString();
	        	eName.setFullName(objJsonObject1.getString("familyName").toString()+","+objJsonObject1.getString("givenName").toString()+" "+middleName);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
        	appStatusData.setExaminerName(eName);
        	appStatusData.setGroupArtUnit(appInfo.getGroupArtUnitNumber());        	
        	appStatusData.setPatentSubclass(appInfo.getSubclass());
        	appStatusData.setPatentNumber(appInfo.getPatentNumber());
        	appStatusData.setIssueDate(convertDate(String.valueOf(appInfo.getGrantDate())));
        	appStatusData.setPatentClass(appInfo.getCurrentApplClass());
        	appStatusData.setContinuityInformation("-");
        	applicationStatusData.add(appStatusData);
        	
        }
        ac.setApplicationStatusData(applicationStatusData);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ac.setFileCreationTime(df.format(new Date()));
        return ac;
    }
	
	public String subStringDate(String str) {
		//20200921
		String res = str.substring(0, 4)+str.substring(4, 6)+str.substring(6, 8);
		return res;
	}
	
	public String convertDate(String tempDate) {
		
		String returnDate=null;
		long unix_seconds = Long.parseLong(tempDate);
		   //convert seconds to milliseconds
		   Date date = new Date(unix_seconds*1000L); 
		   // format of the date
		   SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd");
		   jdf.setTimeZone(TimeZone.getTimeZone("est"));
		   String java_date = jdf.format(date);
		   System.out.println("\n"+java_date+"\n");
		
		return java_date;
	}
	public String convertDateWithTimeStamp(String tempDate) {
		
		String returnDate=null;
		long unix_seconds = Long.parseLong(tempDate);
		   //convert seconds to milliseconds
		   Date date = new Date(unix_seconds*1000L); 
		   // format of the date
		   SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		   jdf.setTimeZone(TimeZone.getTimeZone("est"));
		   String java_date = jdf.format(date);
		   System.out.println("\n"+java_date+"\n");
		
		return java_date;
	}

	public String getExaminerName(String appNumber) {
		
		final String workerNumberUrl = urlString+"OPSGPCDMServices/application-examiner-history?applicationNumber="+ appNumber;
		
		ResponseEntity<String> workerNumberData = null;
		ResponseEntity<String> examinerData = null;
		//https://opsg-api.sit.uspto.gov/OPSGPCDMServices/application-examiner-history?applicationNumber=15784512 
		
		ResponseEntity<String> examiner = null;	
		try {
			SSLUtil.turnOffSslChecking();
			String activityStartTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
			
			workerNumberData = restTemplate.getForEntity(workerNumberUrl, String.class);
			System.out.println("WORKER DETAIL NUM"+workerNumberData.getBody());
			JSONObject objJsonObject1 = new JSONObject(workerNumberData.getBody().toString());
			System.out.println(objJsonObject1.getString("results"));
			
			String objJsonObjectMeta1 = objJsonObject1.getString("results");
			 JSONArray jsonArray = new JSONArray(objJsonObjectMeta1);
			    System.out.println(jsonArray.length());
			    
			       String val = jsonArray.getJSONObject(0).getString("partyIdentifier");
			System.out.println(val);
			final String examinerUrl = infraURLString+"/infra-services/workerDetails/"+val;
			URI examinerUri = new URI(examinerUrl);
			examinerData = restTemplate.getForEntity(examinerUri, String.class);
			//finalResult.put("examinerData", examinerData.getBody());
			activityLog(activityStartTime,Statistics.PUBLICATIONREVIEWDATAREQUEST, null);
			SSLUtil.turnOnSslChecking();
		} catch (Exception e) {
			log.error(e.getMessage());
			//finalResult.put("wipoAapplicationData", "No Data");

		}
		return examinerData.getBody();
	}

}
