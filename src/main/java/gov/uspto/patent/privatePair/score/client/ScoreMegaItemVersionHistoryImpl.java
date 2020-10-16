package gov.uspto.patent.privatePair.score.client;

import javax.xml.bind.JAXBElement;

import gov.uspto.patent.privatePair.score.scoreService.MegaItemVersionHistory.MegaItemVerHistoryInputMessage;
import gov.uspto.patent.privatePair.score.scoreService.MegaItemVersionHistory.MegaItemVersionsType;
import gov.uspto.patent.privatePair.score.scoreService.MetaData.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;



@Service
public class ScoreMegaItemVersionHistoryImpl {
	@Autowired
	Jaxb2Marshaller marshaller;

	@Value("score.webservice.megaitemversionhistory")
	String webServiceUrl_megaItemHistory;
	
	private WebServiceTemplate template;
	
	public MegaItemVersionsType getMegaItemVersionHistory(MegaItemVerHistoryInputMessage mIVHIM) {

		template = new WebServiceTemplate(marshaller);		
		WebServiceMessageCallback webServiceMessageCallback=null;
		MegaItemVersionsType megaItemRecordsType= new MegaItemVersionsType();
		
		JAXBElement<?> response = (JAXBElement<?>) template.marshalSendAndReceive("http://score-fqt.etc.uspto.gov/ScoreWSWeb/services/MegaItemVersionHistoryImpl",
				new gov.uspto.patent.privatePair.score.scoreService.MetaData.ObjectFactory().createElementMegaItemVerHistoryInputMessage(mIVHIM)) ;
		megaItemRecordsType = (MegaItemVersionsType) response.getValue();
		return megaItemRecordsType;
	}
}
