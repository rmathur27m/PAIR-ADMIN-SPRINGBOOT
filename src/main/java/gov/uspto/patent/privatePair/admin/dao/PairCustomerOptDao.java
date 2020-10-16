package gov.uspto.patent.privatePair.admin.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.uspto.patent.privatePair.admin.dao.mapper.PairCustomerOptMapper;
import gov.uspto.patent.privatePair.admin.dto.PairCustomerOptDto;
import gov.uspto.patent.privatePair.exceptionhandlers.PairAdminDatabaseException;

/**
 * Data Access Object (DAO) used in Create New Customer Number operation.
 * 
 */
@Component
public class PairCustomerOptDao implements PairCustomerOptMapper {

    @Autowired
    private SqlSession sqlSession;

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertPairCustomerOpt(PairCustomerOptDto pairCustomerOptDto) throws Exception {
        try {
            PairCustomerOptMapper pairCustomerOptMapper = sqlSession.getMapper(PairCustomerOptMapper.class);
            pairCustomerOptMapper.insertPairCustomerOpt(pairCustomerOptDto);
        } catch (Exception e) {
            throw new PairAdminDatabaseException("Insertion of pair Customer Opt failed: "
                    + pairCustomerOptDto.getCustomerNumber());
        }
    }
}
