package gov.uspto.patent.privatePair.admin.service.common;

import gov.uspto.patent.privatePair.admin.dto.PairUserDnDto;
import gov.uspto.patent.privatePair.exceptionhandlers.PairAdminDatabaseException;
import gov.uspto.patent.privatePair.exceptionhandlers.UserNotFoundException;

/**
 * 
 * Pair Administrator Helper Services
 * 
 */
public interface PairAdminHelperServices {

    /**
     * Retrieve PAIR user information, from the PAIR database, using their
     * distinguished name
     * 
     * @param dn
     *            distinguished name from user's PKI certificate
     * @return {@link PairUserDnDto}
     * @throws PairAdminDatabaseException
     * @throws UserNotFoundException
     * @throws Exception
     */
    public PairUserDnDto getPairUserDtoByDn(String dn) throws PairAdminDatabaseException, UserNotFoundException, Exception;

}
