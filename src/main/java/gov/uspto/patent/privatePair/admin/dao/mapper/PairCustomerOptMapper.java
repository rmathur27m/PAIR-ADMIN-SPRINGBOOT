package gov.uspto.patent.privatePair.admin.dao.mapper;

import gov.uspto.patent.privatePair.admin.dto.PairCustomerOptDto;

/**
 * 
 * Data Access Object (DAO) Mapper
 * 
 */
public interface PairCustomerOptMapper {

    /**
     * Insert a PAIR Customer notification election option data transfer object
     * (DTO), into the PAIR database, using the 'distinguished name' from the
     * user's PKI certificate.
     * 
     * @param PairCustomerOptDto
     *            DTO representing a PAIR Customer's notification election
     *            option i.e. mail or email.
     * @throws Exception
     */
    public void insertPairCustomerOpt(PairCustomerOptDto pairCustomerOptDto) throws Exception;
}
