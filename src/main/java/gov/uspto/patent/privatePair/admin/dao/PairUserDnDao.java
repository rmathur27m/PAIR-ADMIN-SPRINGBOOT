package gov.uspto.patent.privatePair.admin.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.stereotype.Component;

import gov.uspto.patent.privatePair.admin.dao.mapper.PairUserDnMapper;
import gov.uspto.patent.privatePair.admin.domain.Constants;
import gov.uspto.patent.privatePair.admin.dto.PairUserDnDto;
import gov.uspto.patent.privatePair.exceptionhandlers.PairAdminDatabaseException;
import gov.uspto.patent.privatePair.exceptionhandlers.UserNotFoundException;

/**
 * Data Access Object (DAO) used to retrieve, insert, delete PAIR users using
 * the Distinguished Name (DN).
 * 
 */
@Component
public class PairUserDnDao implements PairUserDnMapper {

    @Autowired
    private SqlSession sqlSession;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPairUserDnByDn(String dn) throws Exception {
        try {
            PairUserDnMapper pairUserDnMapper = sqlSession.getMapper(PairUserDnMapper.class);
            return pairUserDnMapper.getPairUserDnByDn(dn);
        } catch (Exception e) {
            throw new PairAdminDatabaseException("Retreiving pair userId by dn failed: " + e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PairUserDnDto getPairUserDtoByDn(String dn) throws Exception {
        PairUserDnDto pairUserDnDto = null;
        try {
            PairUserDnMapper pairUserDnMapper = sqlSession.getMapper(PairUserDnMapper.class);
            pairUserDnDto = pairUserDnMapper.getPairUserDtoByDn(dn);
            if (pairUserDnDto == null) {
                throw new UserNotFoundException("User not found for dn: " + dn);
            }
        } catch (Exception e) {
            if (e instanceof UserNotFoundException) {
                throw new UserNotFoundException("User not found for dn: " + dn);
            } else if (e.getCause().getCause() instanceof CannotGetJdbcConnectionException) {
                throw new PairAdminDatabaseException(Constants.DB_CONNECTION_MESSAGE.toString(), e.getCause());
            }
            throw new Exception("Failed to retreive the pair user.", e);
        }
        return pairUserDnDto;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public List<PairUserDnDto> hasPairUserDn(@Param("regNumberList") List regNumberList) throws Exception {

        List<PairUserDnDto> pairUserDnRegNumList = new ArrayList<PairUserDnDto>();

        PairUserDnMapper pairUserDnMapper = sqlSession.getMapper(PairUserDnMapper.class);

        pairUserDnRegNumList = pairUserDnMapper.hasPairUserDn(regNumberList);

        return pairUserDnRegNumList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertPairUserDn(PairUserDnDto pairUserDnDto) throws Exception {
        try {
            PairUserDnMapper pairUserDnMapper = sqlSession.getMapper(PairUserDnMapper.class);
            pairUserDnMapper.insertPairUserDn(pairUserDnDto);
        } catch (Exception e) {
            throw new PairAdminDatabaseException("Insertion of pair user failed: " + pairUserDnDto.getDn());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void detelePairUserDn(String dn) throws Exception {
        try {
            PairUserDnMapper pairUserDnMapper = sqlSession.getMapper(PairUserDnMapper.class);
            pairUserDnMapper.detelePairUserDn(dn);
        } catch (Exception e) {
            throw new PairAdminDatabaseException("Deletion of pair user failed: " + dn);
        }
    }
}
