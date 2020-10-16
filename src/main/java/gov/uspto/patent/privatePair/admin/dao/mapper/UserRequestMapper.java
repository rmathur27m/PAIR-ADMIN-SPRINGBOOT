package gov.uspto.patent.privatePair.admin.dao.mapper;

import java.util.List;

import gov.uspto.patent.privatePair.admin.dto.UserRequestDto;

/**
 * 
 * User RequestMapper
 * 
 */

public interface UserRequestMapper {
    /**
     * Inserts User Request Data Transfer Object (DTO) into the user request
     * table.
     * 
     * @param userRequestDto
     *            The user request object to add.
     * @return Auto generated id.
     * 
     * @throws Exception
     */
    public long insertUserRequest(UserRequestDto userRequestDto) throws Exception;

    /**
     * Retrieve the user request using the request id.
     * 
     * @param userRequestId
     *            user request id
     * 
     * @return User Request Data Transfer (DTO) object
     * 
     * @throws Exception
     */
    public UserRequestDto getUserRequestById(long userRequestId) throws Exception;

    /**
     * Retrieve all the rows in the user request table.
     * 
     * @return List of user request Data Transfer Objects (DTOs) representing
     *         all the rows in the user request table.
     * 
     * @throws Exception
     */
    public List<UserRequestDto> getAllUserRequests() throws Exception;

    /**
     * Update the given userRequest object in the userRequest table.
     * 
     * @param userRequest
     *            Data Transfer Object (DTO) representing the user request to
     *            update.
     * 
     * @throws Exception
     */
    public void updateUserRequest(UserRequestDto userRequest) throws Exception;

    /**
     * Deletes the userRequest for the given userRequestId.
     * 
     * @param userRequestId
     *            Data Transfer Object (DTO) representing the user request to
     *            delete.
     * 
     * @throws Exception
     */
    public void deleteUserRequestByUserRequestId(long userRequestId) throws Exception;

    /**
     * Updates the request status for the given user request using the user
     * request id.
     * 
     * @param userRequestDto
     *            Data Transfer Object (DTO) representing the user request whose
     *            status is being updated.
     * 
     * @throws Exception
     */
    public void updateRequestStatusByUserRequestId(UserRequestDto userRequestDto) throws Exception;

    /**
     * Updates the delete indicator for the given userRequestId.
     * 
     * @param userRequestDto
     *            Data Transfer Object (DTO) representing the user request whose
     *            delete indicator is being updated.
     * 
     * @throws Exception
     */
    public Integer updateDeleteIndicatorByUserRequestId(UserRequestDto userRequestDto) throws Exception;
}
