package gov.uspto.patent.privatePair.admin.dao.mapper;

import gov.uspto.patent.privatePair.admin.dto.PairUserCnDto;

/**
 * 
 * Pair User CnMapper
 * 
 */
public interface PairUserCnMapper {

    /**
     * Insert a Data Transfer Object (DTO) representing a PAIR user using the
     * 'Distinguished Name' from the user's PKI certificate.
     * 
     * @param PairUserCnDto
     *            DTO representing the PAIR user to update.
     * @throws Exception
     */
    public void insertPairUserCn(PairUserCnDto pairUserCnDto) throws Exception;
}
