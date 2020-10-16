package gov.uspto.patent.privatePair.fpng.dao;

import gov.uspto.patent.privatePair.fpng.dto.FpngServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * FPNG rest client to fetch data from FPNG Rest services.
 *
 * @author
 */

@Component
public class FpngRestClient {

    private static final String DATE_FORMAT = "MM-dd-yyyy";
    private final String fpngWebServiceUrl;
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    public FpngRestClient(final RestTemplate restTemplate,
                          @Value("${fpng.service.getFeeHistorybyReferenceNumberURL}") final String fpngWebServiceUrl) {

        this.restTemplate = restTemplate;
        this.fpngWebServiceUrl = fpngWebServiceUrl;
    }


    public FpngServiceResponse getFeePaymentRecsByAppNum(String appOrPatentNumber, String urlString) {
        FpngServiceResponse serviceResponse = new FpngServiceResponse();
        long startTime = System.currentTimeMillis();
        HttpEntity<String> entity = new HttpEntity<String>("", getMetadataHeaders());
        serviceResponse = restTemplate
                .exchange(urlString + "?postingReferenceText=" + appOrPatentNumber,
                        HttpMethod.GET, entity, FpngServiceResponse.class)
                .getBody();

        return serviceResponse;
    }

    public HttpHeaders getMetadataHeaders() {
        HttpHeaders headers = new HttpHeaders();
        List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        headers.setAccept(supportedMediaTypes);
        return headers;
    }

    // This function will be call when the patent is more than 7 digits
    public FpngServiceResponse getFeePaymentRecsByAppNum(String appOrPatentNumber,
                                                         String issueDate, int patentFlag, String urlString) {
        String feeIssueDate = feeDateConvert(issueDate);
        long startTime = System.currentTimeMillis();
        HttpEntity<String> entity = new HttpEntity<String>("", getMetadataHeaders());

        if (patentFlag == 1) {
            FpngServiceResponse serviceResponse = restTemplate.exchange(
                    urlString + "?postingReferenceText=" + appOrPatentNumber + "&startDate="
                            + feeIssueDate + "&ranVar=" + Math.random(),
                    HttpMethod.GET, entity, FpngServiceResponse.class).getBody();
            return serviceResponse;
        } else {
            // fee_url = getFeeHistorybyReferenceNumberURL + "?postingReferenceText=" +
            // appOrPatentNumber + "&endDate=" + feeIssueDate + "&ranVar=" + Math.random();
            // log.info(fee_url);
            FpngServiceResponse serviceResponse = restTemplate.exchange(
                    urlString + "?postingReferenceText=" + appOrPatentNumber + "&endDate="
                            + feeIssueDate + "&ranVar=" + Math.random(),
                    HttpMethod.GET, entity, FpngServiceResponse.class).getBody();
            return serviceResponse;
        }

    }

    private String feeDateConvert(String issueDate) {
        // TODO Auto-generated method stub
        String feeDateConvert = null;
        feeDateConvert = issueDate.substring(0, 2) + "/" + issueDate.substring(3, 5) + "/" + issueDate.substring(6);
        return feeDateConvert;
    }


}
