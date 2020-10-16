package gov.uspto.patent.privatePair.admin.service.createcustomernumber;

import gov.uspto.patent.privatePair.admin.dao.*;
import gov.uspto.patent.privatePair.admin.domain.CreateNewCustomerNumberForm;
import gov.uspto.patent.privatePair.admin.domain.CustomerPractitionerInfo;
import gov.uspto.patent.privatePair.admin.service.common.RequestStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CreateNewCustomerNumberFacadeImplTest {

    @Mock
    UserRequestDao userRequestDao;

    @Mock
    PairUserDnDao pairUserDnDao;

    @Mock
    PairUserCnDao pairUserCnDao;

    @Mock
    PairCustomerOptDao pairCustomerOptDao;

    @Mock
    ViewRequestDao viewRequestDao;

    @Mock
    CustomerPractitionerDao customerPractitionerDao;

    @Mock
    NewCustomerNumberDao newCustomerNumberDao;

    @Mock
    UserSignatureDao userSignatureDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void saveNewCustomerNumber() {
        CreateNewCustomerNumberForm createNewCustomerNumberForm = new CreateNewCustomerNumberForm();

        createNewCustomerNumberForm.setAddressLine1("OYEN, WIGGS, GREEN & MUTALA LLP");
        createNewCustomerNumberForm.setPocFiledBy("James John Dottavio");
        createNewCustomerNumberForm.setDn("cn=Peter Test Kauslick, ou=Registered Attorneys, ou=Patent and Trademark Office, ou=Department of Commerce, o=U.S. Government, c=US");
        createNewCustomerNumberForm.setRequestStatus(RequestStatus.SAVED.getName());
        createNewCustomerNumberForm.setPairId("99999");

        CustomerPractitionerInfo customerPractitionerInfo = new CustomerPractitionerInfo();
        customerPractitionerInfo.setRegistrationNumber("19614");

        List<CustomerPractitionerInfo> customerPractitionerInfoArray = new ArrayList<>();
        customerPractitionerInfoArray.add(customerPractitionerInfo);

        createNewCustomerNumberForm.setCustomerPractitionerInfolist(customerPractitionerInfoArray);



    }
}