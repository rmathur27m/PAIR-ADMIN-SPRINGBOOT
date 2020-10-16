package gov.uspto.patent.privatePair.admin.dao.mapper;

import gov.uspto.patent.privatePair.admin.dto.NewCustomerNumberDto;

/**
 * 
 * Data Access Object (DAO) used by MyBatis to access the PAIR New Customer Number Request
 * table.
 */
public interface NewCustomerNumberMapper {

    public void insertNewCustomerNumberRequest(NewCustomerNumberDto newCustomerNumberDto);

    public NewCustomerNumberDto getNewCustomerNumberRequestById(long userRequestId);

    public void deleteNewCustomerNumberRequestById(long userRequestId);

    public void updateNewCustomerNumberRequestById(NewCustomerNumberDto newCustomerNumberDto);
}
