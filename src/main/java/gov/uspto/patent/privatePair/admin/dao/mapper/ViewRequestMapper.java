package gov.uspto.patent.privatePair.admin.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import gov.uspto.patent.privatePair.admin.dto.ViewRequestDto;

/**
 * 
 * View RequestMapper
 * 
 */

@Mapper
public interface ViewRequestMapper {

    /**
     * Retrieve Entity Change Requests from the PAIR DB.
     * 
     * @param requestStatusList
     *            List of Data Transfer Objects (DTOs) representing Entity
     *            Change requests.
     * @param requestDays
     *            Number of calendar days to search.
     * @param requestType
     *            Type of requests to retrieve.
     * @param privatePAIRdn
     *            Distinguished Name 'DN' from user's PKI Certificate.
     * @return List of Data Transfer Objects (DTOs) representing the retrieved
     *         view requests.
     * @throws Exception
     */
    public List<ViewRequestDto> getEntityRequestsforView(@Param("requestStatusList") List<?> requestStatusList,
            @Param("requestDays") String requestDays, @Param("requestType") String requestType,
            @Param("privatePAIRdn") String privatePAIRdn) throws Exception;

    /**
     * Retrieve list of Update Address Requests.
     * 
     * @param requestStatusList
     *            List of Data Transfer Objects (DTOs) representing Update
     *            Address Requests.
     * @param requestDays
     *            Number of calendar days to search.
     * @param requestType
     *            Type of requests to retrieve.
     * @param privatePAIRdn
     *            Distinguished Name 'DN' from user's PKI Certificate.
     * @return List of Data Transfer Objects (DTOs) representing the retrieved
     *         update application requests.
     * @throws Exception
     */
    public List<ViewRequestDto> getUpdateAddrRequestsforView(@Param("requestStatusList") List<?> requestStatusList,
            @Param("requestDays") String requestDays, @Param("requestType") String requestType,
            @Param("privatePAIRdn") String privatePAIRdn) throws Exception;

    /**
     * Retrieve list of Customer Requests from the PAIR DB.
     * 
     * @param requestStatus
     *            List Data Transfer Objects (DTOs) representing Customer
     *            Requests.
     * 
     * @param requestDays
     *            Number of days to search.
     * @param requestType
     *            Type of request to retrieve.
     * @param privatePAIRdn
     *            Distinguished Name 'DN' from user's PKI certificate.
     * @return List of Data Transfer Objects representing the view requests.
     * @throws Exception
     */
    public List<ViewRequestDto> getCustomerRequestsforView(@Param("requestStatusList") List<?> requestStatusList,
            @Param("requestDays") String requestDays, @Param("requestType") String requestType,
            @Param("privatePAIRdn") String privatePAIRdn) throws Exception;

}
