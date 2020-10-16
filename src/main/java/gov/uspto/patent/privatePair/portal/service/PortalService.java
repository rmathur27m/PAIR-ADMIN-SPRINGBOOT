package gov.uspto.patent.privatePair.portal.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;

@Service
public class PortalService {
	@Value("${opsgbp.webservice.url}")
	String OPSBPServiceUrl;
	@Value("${opsg.webservice.url}")
	String urlString;
	@Autowired
	RestTemplate restTemplate;

	 @Async
	    public CompletableFuture<JsonNode> callOtherService(String customerNumber) throws URISyntaxException {
		 final String baseUrl = OPSBPServiceUrl + customerNumber;
		    URI uri = new URI(baseUrl);
		    JsonNode responseObj = restTemplate.getForObject(uri, JsonNode.class);
	        return CompletableFuture.completedFuture(responseObj);
	    }

}

