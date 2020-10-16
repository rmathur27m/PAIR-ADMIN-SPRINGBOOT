package gov.uspto.patent.privatePair.score.client;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import gov.uspto.patent.privatePair.score.scoreService.MetaData.MetaDataRecordsType;
import gov.uspto.patent.privatePair.score.scoreService.MetaData.ObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;


@Service
public class ScoreSoapClient {
	
	private static final Logger  logger = LoggerFactory.getLogger(ScoreSoapClient.class);
	@Autowired
	Jaxb2Marshaller marshaller;
	
	private WebServiceTemplate template;

	@Value("score.webservice.metadata")
	String webService_metadata;
	
	public MetaDataRecordsType getMetaData(String ApplicationNum) {

		template = new WebServiceTemplate(marshaller);		
		WebServiceMessageCallback webServiceMessageCallback=null;
		
		JAXBElement<String> jaxbElement =  new JAXBElement(new QName("root-element"),  String.class, ApplicationNum);
		logger.info(jaxbElement.getName().toString());
		MetaDataRecordsType metaDataRecordsType=null;
		JAXBElement<String> jaxbElement1 =new JAXBElement(new QName("element-string"),  String.class, ApplicationNum);
		

		JAXBElement<?> response = (JAXBElement<?>) template.marshalSendAndReceive("http://score-fqt.etc.uspto.gov/ScoreWSWeb/services/MetaDataImpl",
				new gov.uspto.patent.privatePair.score.scoreService.MetaData.ObjectFactory().createElementString(ApplicationNum)) ;
		metaDataRecordsType = (MetaDataRecordsType) response.getValue();
		return metaDataRecordsType;
	}
	
}
