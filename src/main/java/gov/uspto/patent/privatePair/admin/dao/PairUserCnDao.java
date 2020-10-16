package gov.uspto.patent.privatePair.admin.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.uspto.patent.privatePair.admin.dao.mapper.PairUserCnMapper;
import gov.uspto.patent.privatePair.admin.dto.PairUserCnDto;
import gov.uspto.patent.privatePair.exceptionhandlers.PairAdminDatabaseException;

/**
 * 
 * Data Access Object (DAO) used in Create New Customer Number operation.
 * 
 */
@Component
public class PairUserCnDao implements PairUserCnMapper {

    @Autowired
    private SqlSession sqlSession;

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertPairUserCn(PairUserCnDto pairUserCnDto) throws Exception {
        try {
            PairUserCnMapper pairUserCnMapper = sqlSession.getMapper(PairUserCnMapper.class);
            pairUserCnMapper.insertPairUserCn(pairUserCnDto);
        } catch (Exception e) {
            throw new PairAdminDatabaseException("Insertion of pair user failed: " + pairUserCnDto.getCustNum());
        }
    }
}
