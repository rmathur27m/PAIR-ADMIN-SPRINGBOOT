package gov.uspto.patent.privatePair.score.client;

import javax.xml.bind.JAXBElement;

import gov.uspto.patent.privatePair.score.scoreService.MegaData.MegaItemRecordsType;
import gov.uspto.patent.privatePair.score.scoreService.MegaData.MegaItemsInputMessage;
import gov.uspto.patent.privatePair.score.scoreService.MetaData.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;

@Service
public class ScoreMegaItemImpl {

	@Autowired
	private Jaxb2Marshaller marshaller;

	@Value("score.webservice.megaitemsimpl")
	private String webServiceUrl_megaitems;
	
	private WebServiceTemplate template;
	
	public MegaItemRecordsType getMegaItems(MegaItemsInputMessage mIIM) {

		template = new WebServiceTemplate(marshaller);		
		WebServiceMessageCallback webServiceMessageCallback=null;
		MegaItemRecordsType megaItemRecordsType= new MegaItemRecordsType();
		
		JAXBElement<?> response = (JAXBElement<?>) template.marshalSendAndReceive("http://score-fqt.etc.uspto.gov/ScoreWSWeb/services/MegaItemsImpl",
				new gov.uspto.patent.privatePair.score.scoreService.MetaData.ObjectFactory().createElementMegaItemsInputMessage(mIIM)) ;
		megaItemRecordsType = (MegaItemRecordsType) response.getValue();
		return megaItemRecordsType;
	}
}
