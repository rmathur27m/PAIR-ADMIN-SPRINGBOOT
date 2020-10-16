package gov.uspto.patent.privatePair.admin.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import gov.uspto.patent.privatePair.admin.dto.PairUserDnDto;

/**
 * 
 * Pair User DnMapper
 * 
 */
public interface PairUserDnMapper {

    /**
     * Retrieve the user id from the PAIR_USER_DN table using the Distinguished
     * Name from the user's PKI Certificate.
     * 
     * @param dn
     *            The distinguished name from the user's PKI Certificate.
     * @return userId
     */
    public String getPairUserDnByDn(String dn) throws Exception;

    /**
     * Retrieve a Data Transfer Object (DTO) representing a PAIR user using the
     * Distinguished Name from the user's PKI Certificate.
     * 
     * @param dn
     *            The distinguished name from the user's PKI Certificate.
     * @return A DTO representing the PAIR user
     * @throws Exception
     */
    public PairUserDnDto getPairUserDtoByDn(String dn) throws Exception;

    /**
     * Retrieve a list of Data Transfer Objects (DTOs) representing a list of
     * PAIR users using a list of registration numbers
     * 
     * @param regNumberList
     *            List of registration numbers
     * @return List of DTOs where each DTO represents a PAIR user.
     * @throws Exception
     */
    public List<PairUserDnDto> hasPairUserDn(@Param("regNumberList") List<String> regNumberList) throws Exception;

    /**
     * Insert a Data Transfer Object (DTO) representing a PAIR User using the
     * distinguished name (DN) from user's the PKI Certificate.
     * 
     * @param pairUserDnDto
     *            DTO representing the pair user to insert
     * @throws Exception
     */
    public void insertPairUserDn(PairUserDnDto pairUserDnDto) throws Exception;

    /**
     * Delete a PAIR user from the database using the 'distinguished name' from
     * the user's PKI Certificate.
     * 
     * @param dn
     *            The distinguished name of the user to delete.
     * @throws Exception
     */
    public void detelePairUserDn(String dn) throws Exception;
}
