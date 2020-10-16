package gov.uspto.patent.privatePair.admin.dao.mapper;

import gov.uspto.patent.privatePair.admin.dto.EntityChangeRequestDto;

/**
 * 
 * Data Access Object (DAO) used by MyBatis to access the PAIR Entity Change Request
 * table.
 */
public interface EntityChangeRequestMapper {

    /**
     * Add entity change request to PAIR database.
     * 
     * @param entityChangeRequestDto
     *            Entity change request data transfer object.
     * @throws Exception
     */
    public void insertEntityChangeRequest(EntityChangeRequestDto entityChangeRequestDto) throws Exception;

    /**
     * Retrieve entity change request, from PAIR database, by entity change
     * request id.
     * 
     * @param userRequestId
     *            User request id associated with the entity change request.
     * @return {@link EntityChangeRequestDto} Entity change request data
     *         transfer object (DTO).
     */
    public EntityChangeRequestDto getEntityChangeRequestById(long userRequestId) throws Exception;

    /**
     * Delete the entity change request, in the PAIR database, by entity change
     * request id.
     * 
     * @param userRequestId
     *            User request id associated with the entity change request.
     * @throws Exception
     */
    public void deleteEntityChangeRequestById(long userRequestId) throws Exception;

    /**
     * Update the entity change request, in the PAIR database, by entity change
     * request id.
     * 
     * @param updatedEntityChangeRequestDto
     *            The updated entity change request data transfer object (DTO)
     * @throws Exception
     */
    public void updateEntityChangeRequestById(EntityChangeRequestDto updatedEntityChangeRequestDto) throws Exception;

}
