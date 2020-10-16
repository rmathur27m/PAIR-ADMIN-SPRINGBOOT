package gov.uspto.patent.privatePair.PCSEJBApp.controller;

import gov.uspto.patent.privatePair.PCSEJBApp.dto.EofficeNotificationStatusCheckVO;
import gov.uspto.patent.privatePair.PCSEJBApp.dto.OCNArchiveSearchResultVO;
import gov.uspto.patent.privatePair.PCSEJBApp.services.PCSSupportService;
import gov.uspto.patent.privatePair.utils.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class PCSSupportController {

    @Autowired
    PCSSupportService pCSSupportService;

    //http://localhost:8080/getSystemAnnouncement?userName=''
    @GetMapping(value = "/getSystemAnnouncement")
    public String[] getSystemAnnouncement(@RequestParam(name = "msg", required = true) String msg) {
        log.info("inside validateUser controller--->" + msg);

        return pCSSupportService.getSystemAnnouncement(msg);
    }

    //http://localhost:8080/pcsSupportSearchEnotifications
    @GetMapping(value = "/pcsSupportSearchEnotifications")
    public List pcsSupportSearchEnotifications(@RequestParam Map<String, String> searchParams)
            throws ServiceException, ClassNotFoundException, SQLException {
        log.info("inside pcsSupportSearchEnotifications controller--->" + searchParams);
        List resultList = new ArrayList();
        EofficeNotificationStatusCheckVO statusCheckVO = new EofficeNotificationStatusCheckVO();

        resultList = pCSSupportService.pcsSupportSearchEnotifications(searchParams);

        if (resultList.size() == 0) {
            statusCheckVO.setErrorMesage("No Record Found");
            resultList.add(statusCheckVO);
            return resultList;
        }
        return resultList;
    }

    //http://localhost:8080/pcsSupportSearchOCNRows
    @GetMapping(value = "/pcsSupportSearchOCNRows")
    public List<OCNArchiveSearchResultVO> pcsSupportSearchOCNRows(@RequestParam Map<String, String> searchParams)
            throws ServiceException, ClassNotFoundException, SQLException {
        log.info("inside pcsSupportSearchOCNRows controller--->" + searchParams);

        return pCSSupportService.pcsSupportSearchOCNRows(searchParams);
    }

    //http://localhost:8080/setSystemAnnouncement
    @PostMapping(value = "/setSystemAnnouncement")
    public String setSystemAnnouncement(@RequestBody Map<String, String> map) throws ServiceException {
    	Gson gson = new Gson();
        log.info("inside updateOCNDocketedAction controller--->" + map.size());
       int resultCount = 0;
        String message = "";
        message = map.get("message").toString();
       message= insertPeriodically(message,"','",200);
       System.out.println(message);
        resultCount = pCSSupportService.setSystemAnnouncement(message, map.get("msg").toString());

        return resultCount > 0 ? ResponseEntity.ok(gson.toJson("Success")).getBody(): ResponseEntity.ok(gson.toJson("Failure")).getBody();
    }
    
    public static String insertPeriodically(
    	    String text, String insert, int period)
    	{
    	    StringBuilder builder = new StringBuilder(
    	         text.length() + insert.length() * (text.length()/period)+1);

    	    int index = 0;
    	    String prefix = "";
    	    while (index < text.length())
    	    {
    	        // Don't put the insert in the very first iteration.
    	        // This is easier than appending it *after* each substring
    	        builder.append(prefix);
    	        prefix = insert;
    	        builder.append(text.substring(index, 
    	            Math.min(index + period, text.length())));
    	        index += period;
    	    }
    	    return builder.toString();
    	}
    
    
   

    //http://localhost:8080/validateUser?userName=''
    @GetMapping(value = "/validateUser")
    public int validateUser(@RequestParam(name = "userName", required = true) String userName) {
        log.info("inside validateUser controller--->" + userName);

        return pCSSupportService.validateUser(userName);
    }


}
