package gov.uspto.patent.privatePair.PCSEJBApp.controller;

import com.google.gson.Gson;
import gov.uspto.patent.privatePair.PCSEJBApp.dto.*;
import gov.uspto.patent.privatePair.PCSEJBApp.services.OutGoingCorrService;
import gov.uspto.patent.privatePair.portal.controller.ApplicationSearchTabController;
import gov.uspto.patent.privatePair.portal.util.ApplicationInfo;
import gov.uspto.patent.privatePair.utils.IpAddressUtil;
import gov.uspto.patent.privatePair.utils.SSLUtil;
import gov.uspto.patent.privatePair.utils.ServiceException;
import gov.uspto.utils.SortMe;
import gov.uspto.utils.Statistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class OutGoingCorrController {
    private String pairId="";
    private String dn="";
	@Autowired
    OutGoingCorrService outGoingCorrService;
    @Value("${sdwp.webservice.url}")
    String sdwpURLString;
    int docCount=0;

    public List<OutGoingCorrespondenseXMLDownload> outGoingXMLList = new ArrayList<OutGoingCorrespondenseXMLDownload>();
    OutGoingCorrespondenseXMLDownload outGoingCorrespondenseXMLDownload = null;



    private void activityLog(String activityStartTime, int activityName,HttpServletRequest request) {
        String activityEndTime = SortMe.getDateAndTimeInMMDDYYYYHHMISSFormat();
        long startTimeMillis = System.currentTimeMillis();

        String statsActivity = Statistics.addPairAdminActivity(pairId, dn,
                activityName  , activityStartTime, activityEndTime, "0", startTimeMillis, 0, "0",
                IpAddressUtil.getClientIP(request));

        log.info("Stats Activity: {}",statsActivity);
    }

	//http://localhost:8080/updateOCNDocketedAction?customerNumber=1518
	@PostMapping(value = "/updateOCNDocketedAction")
    public String updateOCNDocketedAction(@RequestParam(name="docketed", required = true) String docketed,
                                         @RequestParam(name="daId", required = true) String daId)throws Exception {
        log.debug("inside updateOCNDocketedAction controller--->"+ docketed);

        return outGoingCorrService.updateOCNDocketedAction( docketed,  daId);
    }
    
    //http://localhost:8080/getDisplayOutGoingCorrByCN?customerNumber=1518
    @GetMapping(value = "/getDisplayOutGoingCorrByCN")
    public HashMap getDisplayOutGoingCorrByCN(@RequestParam(name="custNum", required = true) String custNum,
								    		@RequestParam(name="pastDays", required = true) int pastDays,
								    		@RequestParam(name="searchType") String searchType, 
								    		@RequestParam(name="sortBy", required = true) String sortBy,
								    		@RequestParam(name="sortOrder", required = true) String sortOrder, 
								    		@RequestParam(name="iDispRowIndex", required = true) int iDispRowIndex)
    											throws ServiceException, ClassNotFoundException, SQLException {
        System.out.println("inside getDisplayOutGoingCorrByCN controller--->"+ custNum);

        return outGoingCorrService.getDisplayOutGoingCorrByCN( custNum,  pastDays,  searchType,  sortBy,  sortOrder, iDispRowIndex);
    }
    
    @GetMapping(value = "/getDownloadOutGoingList")
    public HashMap getDownloadOutGoingList(@RequestParam(name="downloadBy") String downloadBy,
								    		@RequestParam(name="custNum") String custNum,
								    		@RequestParam(name="searchType") String searchType, 
								    		@RequestParam(name="pastDays") int pastDays,
								    		@RequestParam(name="multipleAppId") String multipleAppId,
								    		@RequestParam(name="sortBy") String sortBy,
								    		@RequestParam(name="sortOrder") String sortOrder)
    											throws ServiceException, ClassNotFoundException, SQLException {
        log.debug("inside getDownloadOutGoingList controller--->"+ custNum);

        return outGoingCorrService.getDownloadOutGoingList( downloadBy,  custNum,  searchType,  pastDays,  multipleAppId,  sortBy,  sortOrder);
    }


    public String getDisplayReferenceData( String applicationNumber) throws URISyntaxException {

        //URI DisplayReferenceuri = new URI(DisplayReferencebaseUrl);
        String appNumForDisplayRef=null;
        if(applicationNumber.contains("PCT")){//  PCT/US10/31909
            applicationNumber = applicationNumber.substring(4,8)+applicationNumber.substring(9,14);
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
            SSLUtil.turnOnSslChecking();
        } catch (Exception e) {
            log.error(e.getMessage());
            return "No Data";
        }
        return displayReferenceData.getBody();
    }

    @PostMapping(value = "/getXMLDownload")
    public OutGoingCorrespondenseXMLDownload  getXMLDownload(@RequestBody List<DisplayList> outGoingList, HttpServletRequest request, HttpServletResponse response)
            throws ServiceException, ClassNotFoundException, SQLException, URISyntaxException {
        log.debug("inside getDownloadOutGoingList controller--->"+ outGoingList.size());
        Date todaysDate;
        String resultJSON=null;
        IFWObject ifwObject = new IFWObject();
        List<DisplayList> disPlayLst= new ArrayList<DisplayList>();
        List<ApplicationCorrespondenseDate> appCorrList = new ArrayList<ApplicationCorrespondenseDate>();
        int correspondingCount=0;
        disPlayLst = outGoingList;
        docCount=0;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        outGoingXMLList.clear();
        outGoingCorrespondenseXMLDownload= new OutGoingCorrespondenseXMLDownload();
        outGoingCorrespondenseXMLDownload.setFileCreationTimeStamp(df.format(new Date()));

        for (DisplayList displayList:disPlayLst){

            resultJSON=this.getDisplayReferenceData(displayList.getAppNumber());
            Gson gson=new Gson();
            ifwObject = gson.fromJson(resultJSON, IFWObject.class);

            ApplicationCorrespondenseDate applicationCorrespondenseDate = new ApplicationCorrespondenseDate();

            applicationCorrespondenseDate.setApplicationNumber(displayList.getAppNumber());
            applicationCorrespondenseDate.setAttorneyDocketNumber(displayList.getAttyDocNum());
            applicationCorrespondenseDate.setCustomerNumber(displayList.getCustNum());
            applicationCorrespondenseDate.setFilingDate(this.convertDate(displayList.getFilingDate()));
            applicationCorrespondenseDate.setLastModifiedTimestamp(this.convertDateWithTimeStamp(displayList.getLastModifiedDt()));
            if(displayList.getPublicationKindCode()!=null){
                EarliestPublicationDate earliestPublicationDate = new EarliestPublicationDate();

                earliestPublicationDate.setPublicationDate(String.valueOf(this.convertDate(Long.toString(displayList.getPublicationDate()))));
                earliestPublicationDate.setPublicationKindCode(displayList.getPublicationKindCode());
                earliestPublicationDate.setPublicationSequenceNumber(displayList.getPublicationSeqNo());
                earliestPublicationDate.setEarliestPublicationYear(displayList.getPublicationYear());
                applicationCorrespondenseDate.setEarliestPublicationDate(earliestPublicationDate);
            }
            List<DocumentData>  docList = new ArrayList<DocumentData>();
            for (DocumentBag docBag:ifwObject.resultBag.get(0).documentBag){
                if(docBag.documentCode.equalsIgnoreCase(displayList.getDocCode())){
                    docList.add(this.prepareJSON(docBag, displayList));
                }

            }
            applicationCorrespondenseDate.setDocumentList(docList);
            appCorrList.add(applicationCorrespondenseDate);

            outGoingCorrespondenseXMLDownload.setApplicationCorrespondenseDate(appCorrList);
        }
        outGoingCorrespondenseXMLDownload.setDocumentCount(docCount);
        outGoingCorrespondenseXMLDownload.setCorrespondenceTimePeriod(disPlayLst.get(0).getCorrespondingCount());
        return outGoingCorrespondenseXMLDownload;
    }

    public String convertDate(String dt){
        long unix_seconds = Long.parseLong(dt);
        Date date = new Date(unix_seconds); // convert seconds to milliseconds
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // the format of your date
        String formattedDate = dateFormat.format(date);
        System.out.println(formattedDate);
        return formattedDate;
    }
    public String convertDateWithTimeStamp(String dt){
        long unix_seconds = Long.parseLong(dt);
        Date date = new Date(unix_seconds); // convert seconds to milliseconds
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // the format of your date
        String formattedDate = dateFormat.format(date);
        System.out.println(formattedDate);
        return formattedDate;
    }

    public DocumentData prepareJSON(DocumentBag docBag, DisplayList displayList){

        List<DocumentData>  docList = new ArrayList<DocumentData>();
        DocumentData documentList = new DocumentData();
        documentList.setDocumentCategory(docBag.getProcedureName());
        documentList.setDocumentDescription(docBag.getDocumentDescription());
        documentList.setFileWrapperDocumentCode(docBag.getDocumentCode());
        documentList.setPageQuantity(docBag.getPageTotalQuantity());
        documentList.setBeginPageNumber(docBag.getStartPageNumber());
        documentList.setMailRoomDate(docBag.getOfficialDate().toString().substring(0,10));
        documentList.setUploadDate(docBag.getLastModifiedDateTime().toString().substring(0,10));
        documentList.setPageOffsetQuantity(docBag.getPackageSequenceNumber());
        documentList.setEarliestViewBy(displayList.getDocViewBy());
        documentList.setEarliestViewDate(displayList.getDocViewDate());
        //documentList.setEarliestViewBy("-");
        docList.add(documentList);
        docCount++;
        return documentList;
    }
}
