package gov.uspto.patent.privatePair.PCSEJBApp.controller;

import gov.uspto.patent.privatePair.PCSEJBApp.dto.IfwOutgoingVO;
import gov.uspto.patent.privatePair.PCSEJBApp.services.CustDetailsService;
import gov.uspto.patent.privatePair.portal.controller.ApplicationSearchTabController;
import gov.uspto.patent.privatePair.utils.ServiceException;
import gov.uspto.utils.SortMe;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;


@RestController
@Slf4j
public class CustDetailsController {

    @Autowired
    CustDetailsService custDetailsService;

    @GetMapping(value = "/getDisplayAppListByCustNumber")
    public HashMap getDisplayAppList(@RequestParam(name="custNum", required = true) String custNum,
                                              @RequestParam(name="intStatusDays", required = true) int intStatusDays,
                                              @RequestParam(name="displayBy") String displayBy,
                                              @RequestParam(name="appStatus") String appStatus,
                                              @RequestParam(name="sortBy", required = true) String sortBy,
                                              @RequestParam(name="sortOrder", required = true) String sortOrder,
                                              @RequestParam(name="iDispRowIndex", required = true) int iDispRowIndex)
            throws ServiceException, ClassNotFoundException, SQLException {
        System.out.println("inside getDisplayOutGoingCorrByCN controller--->"+ custNum);

        log.info("getDisplayAppList");
        return custDetailsService.getDisplayAppList( displayBy,custNum,  appStatus,  intStatusDays,  sortBy,  sortOrder,  iDispRowIndex);
    }
    
    @GetMapping(value = "/getDisplayAppListByCustNumberForCustSearch")
    public HashMap getDisplayAppListForCustSearch(@RequestParam(name="custNum", required = true) String custNum,
                                              @RequestParam(name="intStatusDays", required = true) int intStatusDays,
                                              @RequestParam(name="displayBy") String displayBy,
                                              @RequestParam(name="appStatus") String appStatus,
                                              @RequestParam(name="sortBy", required = true) String sortBy,
                                              @RequestParam(name="sortOrder", required = true) String sortOrder,
                                              @RequestParam(name="iDispRowIndex", required = true) int iDispRowIndex)
            throws ServiceException, ClassNotFoundException, SQLException {
        System.out.println("inside getDisplayOutGoingCorrByCN controller--->"+ custNum);

        log.info("getDisplayAppList");
        return custDetailsService.getDisplayAppListForCustSearch( displayBy,custNum,  appStatus,  intStatusDays,  sortBy,  sortOrder,  iDispRowIndex);
    }
    
    @GetMapping(value = "/getOptedInCustRowsByDN")
    public Vector getOptedInCustRowsByDN(@RequestParam(name="distinguishedName", required = true) String distinguishedName,
    		@RequestParam(name="includeName", required = true) String includeName)
            throws ServiceException, ClassNotFoundException, SQLException {
        System.out.println("inside getDisplayOutGoingCorrByCN controller--->"+ distinguishedName);

        log.info("getDisplayAppList");
        return custDetailsService.getOptedInCustRowsByDN(distinguishedName);
    }

    @GetMapping(value = "/getDownloadAppList")
    public HashMap getDownloadAppList(
                                @RequestParam(name="downloadBy", required = true) String downloadBy,
                                @RequestParam(name="custNum", required = true) String custNum,
                                @RequestParam(name="appStatus") String appStatus,
                                @RequestParam(name="statusDays", required = true) int statusDays,
                                @RequestParam(name="multipleAppId") String multipleAppId,
                                @RequestParam(name="sortBy", required = true) String sortBy,
                                @RequestParam(name="sortOrder", required = true) String sortOrder)
            throws ServiceException, ClassNotFoundException, SQLException {
        System.out.println("inside getDownloadAppList controller--->"+ custNum);

        log.info("getDownloadAppList");
        return custDetailsService.getDownloadAppList(  downloadBy,custNum,  appStatus,  statusDays, multipleAppId, sortBy,  sortOrder);
    }
    @PostMapping(value = "/updateAttorneyDocketNumber")
    @ResponseBody
    public Map updateAttorneyDocketNumber(@RequestBody Map downloadDataMap) throws ServiceException {
        String result=null;
        Map map = new HashMap();
        ResponseEntity<String> applicationsByDocketNumberData = null;
        String oldAttDktNum= downloadDataMap.get("oldAttDktNum").toString();
        String newAttDktNum= downloadDataMap.get("newAttDktNum").toString();
        List appList = (List)downloadDataMap.get("appList");
        map = custDetailsService.updateAttorneyDocketNumber(oldAttDktNum, newAttDktNum, appList);
         return map;
    }
    
    @PostMapping(value = "/updateOutGoingCorrViewStatus")
    @ResponseBody
    //@RequestMapping(value = "/updateOutGoingCorrViewStatus", method = RequestMethod.GET)
    public String updateOutGoingCorrViewStatus(@RequestBody List<IfwOutgoingVO> ifwOutgoingList) throws ServiceException {
    	ResponseEntity<String> reusultFlag=null; 
    	Gson gson = new Gson();
    	String result = custDetailsService.updateOutGoingCorrViewStatus(ifwOutgoingList);   
    	
    	return ResponseEntity.ok(gson.toJson(result)).getBody();
    }
    
    @GetMapping(value = "/outGoingCorrByAppId")  
    @ResponseBody
    //@RequestMapping(value = "/updateOutGoingCorrViewStatus", method = RequestMethod.GET)
    public String outGoingCorrByAppId(@RequestParam(name="appId", required = true) String appId,@RequestParam(name="user", required = true) String user) throws ServiceException {
    	ResponseEntity<String> reusultFlag=null; 
    	Gson gson = new Gson();
    	String result = custDetailsService.outGoingCorrByAppId(appId,user);   
    	
    	return ResponseEntity.ok(gson.toJson(result)).getBody();
    }
}
