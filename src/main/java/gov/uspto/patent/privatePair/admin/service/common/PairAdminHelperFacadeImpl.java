package gov.uspto.patent.privatePair.admin.service.common;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.uspto.patent.privatePair.admin.dao.PairUserDnDao;
import gov.uspto.patent.privatePair.admin.dto.PairUserDnDto;
import gov.uspto.patent.privatePair.exceptionhandlers.UserNotFoundException;

/**
 * Pair Administer Helper FacadeImpl.
 * 
 */
@Component
public class PairAdminHelperFacadeImpl implements PairAdminHelperServices {

    @Autowired
    PairUserDnDao pairUserDnDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public PairUserDnDto getPairUserDtoByDn(String dn) throws Exception, UserNotFoundException {

        PairUserDnDto pairUserDnDto = pairUserDnDao.getPairUserDtoByDn(dn);

        return pairUserDnDto;
    }

}
