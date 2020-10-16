package gov.uspto.patent.privatePair.admin.service.common;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
//import gov.uspto.patent.dao.PairUserDnDao;
//import gov.uspto.patent.dao.UpdateApplicationAddressDao;
//import gov.uspto.patent.dao.UserRequestDao;
//import gov.uspto.patent.dao.UserSignatureDao;
//import gov.uspto.patent.dao.ViewRequestDao;
//import gov.uspto.patent.dto.PairUserDnDto;
//import gov.uspto.patent.dto.UpdateApplicationAddressDto;
//import gov.uspto.patent.dto.UserRequestDto;
//import gov.uspto.patent.pairadmin.domain.UpdateApplicationAddressForm;
//import gov.uspto.patent.pairadmin.domain.UpdateApplicationAddressNumbers;
//import gov.uspto.patent.pairadmin.services.common.PairAdminHelperFacadeImpl;
//import gov.uspto.patent.pairadmin.services.updateapplicationaddress.UpdateApplicationAddressFacadeImpl;
//import gov.uspto.patent.palm.services.PalmSoapClient;
import static org.mockito.Mockito.when;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;


import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import gov.uspto.patent.privatePair.admin.dao.PairUserDnDao;
import gov.uspto.patent.privatePair.admin.dao.UpdateApplicationAddressDao;
import gov.uspto.patent.privatePair.admin.dao.UserRequestDao;
import gov.uspto.patent.privatePair.admin.dao.UserSignatureDao;
import gov.uspto.patent.privatePair.admin.dao.ViewRequestDao;
import gov.uspto.patent.privatePair.admin.domain.UpdateApplicationAddressForm;
import gov.uspto.patent.privatePair.admin.domain.UpdateApplicationAddressNumbers;
import gov.uspto.patent.privatePair.admin.dto.UserRequestDto;
import gov.uspto.patent.privatePair.admin.service.updateapplicationaddress.UpdateApplicationAddressFacadeImpl;

//import us.gov.doc.uspto.common.AuthenticationType;
//import us.gov.doc.uspto.common.GeographicRegionNameType;
//import us.gov.doc.uspto.common.PostalStructuredAddressType;
//import us.gov.doc.uspto.common.TelephoneType;
//import us.gov.doc.uspto.patent.ApplicationAddressReqType;
//import us.gov.doc.uspto.patent.ApplicationAddressRespType;
//import us.gov.doc.uspto.patent.ApplicationIdentifierReturnType;
//import us.gov.doc.uspto.patent.ApplicationIdentifierType;
//import us.gov.doc.uspto.patent.GetCorrespondenceAddressRespType;
//import us.gov.uspto.enterprise.AddressType;
//import us.gov.uspto.enterprise.GeographicRegionType;
//import us.gov.uspto.enterprise.OrganizationNameType;
//import us.gov.uspto.enterprise.PersonNameType;
//import us.gov.uspto.enterprise.YesOrNoIndicatorType;
//import us.gov.uspto.patent.palm.bibdata.ApplicationEntityStausType;
//import us.gov.uspto.patent.palm.bibdata.PatentCase;
//import us.gov.uspto.patent.palm.bibdata.RegisteredPractionerType;
//import us.gov.uspto.patent.palm.bibdata.v1.ApplicationInfoResponse;
//import us.gov.uspto.patent.palm.bibdata.v1.ApplicationRegisteredPractitionerResponse;
//import _int.wipo.standards.xmlschema.st96.common.AddressLineTextType;
//import _int.wipo.standards.xmlschema.st96.common.IdentifierType;
//import _int.wipo.standards.xmlschema.st96.common.PhoneNumberType;


public class PairAdminServicesTest {

	@InjectMocks
    PairAdminHelperFacadeImpl pairAdminHelperFacadeImpl;

    @InjectMocks
    UpdateApplicationAddressFacadeImpl updateApplicationAddressFacadeImpl;

    @Mock
    PairUserDnDao pairUserDnDao;

    @Mock
    UpdateApplicationAddressDao updateApplicationAddressDao;

//    @Mock
//    PalmSoapClient palmSoapClient;

    @Mock
    ViewRequestDao viewRequestDao;

    @Mock
    UserRequestDao userRequestDao;

    @Mock
    UserSignatureDao userSignatureDao;
    
    @Mock
    UserRequestDto userRequestDto;

//    @Mock
//    _int.wipo.standards.xmlschema.st96.common.ObjectFactory wipoCommonFactory;
//
//    @Mock
//    us.gov.doc.uspto.common.ObjectFactory customerCommonFactory;
//
//    @Mock
//    us.gov.doc.uspto.patent.ObjectFactory usPatentFactory;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testUpdateRequestStatusByUserRequestId() throws Exception {

    	UpdateApplicationAddressForm updateApplicationAddressForm = new UpdateApplicationAddressForm();
        updateApplicationAddressForm.setRegistrationNumber("498653214");
        updateApplicationAddressForm.setContactFullName("Tuan A. Nguyen");
        updateApplicationAddressForm.setPairId("12081");
        updateApplicationAddressForm.setCreatedTs(new Date());
        updateApplicationAddressForm.setCustomerNumber("720");
        updateApplicationAddressForm.setSubmitterSignature("/Tuan Nguyen/");
        updateApplicationAddressForm.setStatus("Submitted");
        updateApplicationAddressForm
                .setSavedMessage("The Correspondence and/or Fee Address of the below-identified application(s) were successfully changed to the "
                        + "Customer Number 834 listed below.");
        updateApplicationAddressForm.setLastModifiedTimeStamp(new Date());
        
        
        UserRequestDto userRequestDto = new UserRequestDto();
        userRequestDto.setUserRequestId(Long.valueOf(updateApplicationAddressForm.getPairId()));
        userRequestDto.setRequestStatusCount(updateApplicationAddressForm.getStatus());
        userRequestDto.setRequestDescriptionText(updateApplicationAddressForm.getSavedMessage());
        userRequestDto.setLastModifiedTimeStamp(new Date());
        
        //when(userRequestDao.updateRequestStatusByUserRequestId(userRequestDto)).thenReturn(String.valueOf(userRequestDto));
        
        
        //add in the failure test method, would probably be easier
  
    }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
