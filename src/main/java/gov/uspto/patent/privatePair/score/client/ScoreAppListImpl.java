package gov.uspto.patent.privatePair.score.client;


import gov.uspto.patent.privatePair.score.scoreService.AppListImpl.ApplicationNumbersType;
import gov.uspto.patent.privatePair.score.scoreService.AppListImpl.DateRange;
import gov.uspto.patent.privatePair.score.scoreService.MegaData.MegaItemRecordsType;
import gov.uspto.patent.privatePair.score.scoreService.MegaData.MegaItemsInputMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;

import javax.xml.bind.JAXBElement;

@Service
public class ScoreAppListImpl {

    @Autowired
    private Jaxb2Marshaller marshaller;

    private WebServiceTemplate template;

    public ApplicationNumbersType getApplicationList(DateRange mIIM) {

        template = new WebServiceTemplate(marshaller);
        WebServiceMessageCallback webServiceMessageCallback=null;
        ApplicationNumbersType applicationNumbersType= new ApplicationNumbersType();

        JAXBElement<?> response = (JAXBElement<?>) template.marshalSendAndReceive("http://score-fqt.etc.uspto.gov/ScoreWSWeb/services/AppListImpl",
                new gov.uspto.patent.privatePair.score.scoreService.MetaData.ObjectFactory().createElementBeginEndDates(mIIM)) ;
        applicationNumbersType = (ApplicationNumbersType) response.getValue();
        return applicationNumbersType;
    }
}
