package gov.uspto.patent.privatePair.admin.dao.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import gov.uspto.patent.privatePair.admin.dto.UpdateApplicationAddressDto;
import gov.uspto.patent.privatePair.exceptionhandlers.PairAdminDatabaseException;

/**
 * 
 * Update Application AddressMapper
 * 
 */
public interface UpdateApplicationAddressMapper {

    /**
     * Add a list of data transfer objects (DTOs), representing a list of Update
     * Application Address requests to the PAIR database.
     * 
     * @param UpdateApplicationAddressDto
     *            A list of (DTOs) representing a list of Update Application
     *            Address requests
     * 
     */
    public void insertUpdateAppAddressRequestList(
            @Param("updateApplicationAddressNumbers") List<UpdateApplicationAddressDto> updateApplicationAddressDtoList)
            throws PairAdminDatabaseException;

    /**
     * Retrieve a list of data transfer objects (DTOs) that represent a list of
     * Update Application Address requests using the request id.
     * 
     * @param userRequestId
     *            User request id associated with Update Application Address
     *            request.
     * @return List of Update Application Address Request (DTOs)
     */
    public List<UpdateApplicationAddressDto> getUpdateAppAddressRequestById(long userRequestId);

    public void insertUpdateAppAddressRequestWhenListIsEmpty(@Param("fKeyUserRequestId") Long fKeyUserRequestId,
            @Param("customerNumber") Integer customerNumber, @Param("application_id") String application_id,
            @Param("patentNumber") String patentNumber, @Param("corrAddrChaneIn") String corrAddrChaneIn,
            @Param("maintFeeAddrChaneIn") String maintFeeAddrChaneIn, @Param("attrAuthIn") String attrAuthIn,
            @Param("userId") Long userId, @Param("requestErrorTx") String requestErrorTx,
            @Param("powerOfAttorneyIndicator") String powerOfAttorneyIndicator);

    /**
     * Delete an Update Application Address request by user request id.
     * 
     * @param userRequestId
     *            User request id associated with the Update Application Address
     *            request.
     */
    public void deleteUpdateAppAddressByRequestId(long userRequestId);

    /**
     * Update an Update Application Address Request
     * 
     * @param UpdateApplicationAddressDto
     *            Data Transfer Object representing the Update Application
     *            Address request to update.
     * 
     * @return Integer count of rows affected
     * 
     */
    public Integer updateUpdateApplicationAddressRequest(
            @Param("updateApplicationAddressNumber") UpdateApplicationAddressDto updateApplicationAddressDto);

    /**
     * Perform a "soft" delete of the Update Application Address. Specifically,
     * set the delete indicator
     * 
     * @param UpdateApplicationAddressDto
     *            Data Transfer Object (DTO) representing the row in the
     *            database to set the delete indicator for.
     * 
     * 
     * @return Integer count of rows affected
     */
    public Integer deleteUpdateApplicationAddressByUserRequestId(@Param("lastModifiedTimeStamp") Date lastModifiedTimeStamp,
            @Param("deleteIndicator") String deleteIndicator, @Param("requestStatusCount") String requestStatusCount,
            @Param("userRequestId") long userRequestId);
}