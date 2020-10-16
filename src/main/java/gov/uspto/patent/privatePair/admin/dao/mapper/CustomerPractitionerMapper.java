package gov.uspto.patent.privatePair.admin.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import gov.uspto.patent.privatePair.admin.dto.CustomerPractitionerDto;

/**
 * 
 * Data Access Object (DAO) used by MyBatis to access the PAIR Practitioner
 * table.
 */
public interface CustomerPractitionerMapper {

    public void insertCustomerPractitionerList(@Param("customerPractitioner") List<CustomerPractitionerDto> customerPractitionerDtoList) throws Exception;
    
    public List<CustomerPractitionerDto> getAllCustomerPractitionerById(Integer newCustomerNumberRequestId) throws Exception;

    public void deleteCustomerPractitionerByRequestId(Integer newCustomerNumberRequestId);

    public void updateCustomerPractitionerListByRequestId(
            @Param("customerPractitioner") List<CustomerPractitionerDto> customerPractitionerDtoList,
            @Param("newCustomerNumberRequestId") Integer newCustomerNumberRequestId)
            throws Exception;
}
