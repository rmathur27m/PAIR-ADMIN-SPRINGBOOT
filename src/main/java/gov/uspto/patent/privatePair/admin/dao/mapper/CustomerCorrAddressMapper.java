/**
 * 
 */
package gov.uspto.patent.privatePair.admin.dao.mapper;

import gov.uspto.patent.privatePair.admin.dto.CustomerCorrAddressDto;

/**
 * @author skosuri1
 * 
 */
public interface CustomerCorrAddressMapper {

    /**
     * Insert a Data Transfer Object (DTO) representing a PAIR Customer
     * Correspondence Address details.
     * 
     * @param CustomerCorrAddressDto
     *            DTO representing the PAIR user to insert.
     * @throws Exception
     */
    public void insertCorrAddress(CustomerCorrAddressDto customerCorrAddressDto) throws Exception;

    /**
     * Retrieve a Data Transfer Object (DTO) representing a Customer
     * Correspondence Address using the User Request Id.
     * 
     * @param userRequestId
     *            User request id associated with the customer corr address
     *            request.
     * @return A DTO representing the Customer Correspondence Address.
     * @throws Exception
     */
    public CustomerCorrAddressDto getCustomerCorrAddressById(long userRequestId) throws Exception;

    /**
     * Delete Customer Correspondence Details using the User Request Id.
     * 
     * @param userRequestId
     *            User request id associated with the customer corr address
     *            request.
     * @throws Exception
     */
    public void deleteCustomerCorrAddressById(long userRequestId) throws Exception;

}
