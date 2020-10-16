package gov.uspto.patent.privatePair.score.client;

import gov.uspto.patent.privatePair.score.scoreService.MegaItemDetailImpl.MegaItemDetailInputMessage;
import gov.uspto.patent.privatePair.score.scoreService.MegaItemDetailImpl.MegaItemDetailsType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;

import javax.xml.bind.JAXBElement;

@Service
public class ScoreMegaDetailImpl {

    @Autowired
    Jaxb2Marshaller marshaller;

    private WebServiceTemplate template;

    public MegaItemDetailsType getMegaItemDetail(MegaItemDetailInputMessage mIIM) {

        template = new WebServiceTemplate(marshaller);
        MegaItemDetailsType megaItemDetailsType= new MegaItemDetailsType();

        JAXBElement<?> response = (JAXBElement<?>) template.marshalSendAndReceive("http://score-fqt.etc.uspto.gov/ScoreWSWeb/services/MegaItemDetailImpl",
                new gov.uspto.patent.privatePair.score.scoreService.MetaData.ObjectFactory().createElementMegaItemDetailInputMessage(mIIM)) ;
        megaItemDetailsType = (MegaItemDetailsType) response.getValue();
        return megaItemDetailsType;
    }
}
