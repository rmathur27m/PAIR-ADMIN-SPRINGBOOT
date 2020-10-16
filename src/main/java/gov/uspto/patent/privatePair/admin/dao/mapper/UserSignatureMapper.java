package gov.uspto.patent.privatePair.admin.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import gov.uspto.patent.privatePair.admin.dto.UserSignatureDto;

public interface UserSignatureMapper {

	/**
	 * Insert Name/Signature in the UserSignature table in the PURM/PAIR DB.
	 * 
	 * @param userSignatureDtoList
	 *            List of Name/Signature pair combination.
	 * @throws Exception
	 */
	public void insertUserSignatureList(
			@Param("userSignatures") List<UserSignatureDto> userSignatureDtoList)
			throws Exception;

	/**
	 * Retrieve all user signatures from the USER_SIGNATURE table for the given
	 * pairId/userRequestId.
	 * 
	 * @param userRequestId
	 *            The pairId of the request.
	 * @return {@link List}
	 * @throws Exception
	 */
	public List<UserSignatureDto> getAllUserSignatureById(Long userRequestId)
			throws Exception;

	/**
	 * Deletes all the user signatures for the given userRequestId.
	 * 
	 * @param userRequestId
	 *            The pairId.
	 * @throws Exception
	 */
	public void deleteUserSignatureByRequestId(Long userRequestId)
			throws Exception;
}
