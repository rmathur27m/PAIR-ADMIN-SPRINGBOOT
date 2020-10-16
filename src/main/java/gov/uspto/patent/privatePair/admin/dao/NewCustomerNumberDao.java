package gov.uspto.patent.privatePair.admin.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.uspto.patent.privatePair.admin.dao.mapper.NewCustomerNumberMapper;
import gov.uspto.patent.privatePair.admin.dto.NewCustomerNumberDto;

/**
 * Data Access Object (DAO) representing a create new customer number request.
 * 
 */

@Component
public class NewCustomerNumberDao {

    @Autowired
    private SqlSession sqlSession;

    /**
     * Insert a create new customer number request into the database.
     * 
     * @param newCustomerNumberDto
     *            Data Transfer Object (DTO) representing a create new customer
     *            number request.
     */
    public void insertNewCustomerNumberRequest(NewCustomerNumberDto newCustomerNumberDto) {

        NewCustomerNumberMapper newCustomerNumberMapper = sqlSession.getMapper(NewCustomerNumberMapper.class);

        newCustomerNumberMapper.insertNewCustomerNumberRequest(newCustomerNumberDto);
    }

    /**
     * Delete a create new customer number request using the create new customer
     * number id
     * 
     * @param userRequestId
     *            Id of the create new customer number request to be deleted.
     */
    public void deleteNewCustomerNumberRequestById(long userRequestId) {

        NewCustomerNumberMapper newCustomerNumberMapper = sqlSession.getMapper(NewCustomerNumberMapper.class);

        newCustomerNumberMapper.deleteNewCustomerNumberRequestById(userRequestId);
    }

    /**
     * Retrieve a create new customer number request using the create new
     * customer number request id.
     * 
     * @param userRequestId
     *            Id of the create new customer number request to be deleted.
     * 
     * @return
     */
    public NewCustomerNumberDto getNewCustomerNumberRequestById(long userRequestId) {

        NewCustomerNumberMapper newCustomerNumberMapper = sqlSession.getMapper(NewCustomerNumberMapper.class);

        NewCustomerNumberDto newCustomerNumberDto = newCustomerNumberMapper.getNewCustomerNumberRequestById(userRequestId);

        return newCustomerNumberDto;
    }

    /**
     * Update a create new customer number request using the create new customer
     * number request id.
     * 
     * @param newCustomerNumberDto
     *            Data Transfer Object (DTO) representing a create new customer
     *            number object containing the updated information.
     */
    public void updateNewCustomerNumberRequestById(NewCustomerNumberDto newCustomerNumberDto) {

        NewCustomerNumberMapper newCustomerNumberMapper = sqlSession.getMapper(NewCustomerNumberMapper.class);

        newCustomerNumberMapper.updateNewCustomerNumberRequestById(newCustomerNumberDto);

    }
}
