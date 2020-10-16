package gov.uspto.patent.privatePair.admin.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.uspto.patent.privatePair.admin.dao.mapper.UserSignatureMapper;
import gov.uspto.patent.privatePair.admin.dto.UserSignatureDto;
import gov.uspto.patent.privatePair.exceptionhandlers.PairAdminDatabaseException;

/**
 * 
 * Data Access Object (DAO) used by MyBatis to access the PAIR User Signature
 * table.
 * 
 */
@Component
public class UserSignatureDao implements UserSignatureMapper {

    @Autowired
    private SqlSession sqlSession;

    /**
     * {@inheritDoc}
     * 
     * @throws Exception
     */
    public void insertUserSignatureList(@Param("userSignatures") List<UserSignatureDto> userSignatureDtoList) throws Exception {
        try {
            UserSignatureMapper userSignatureMapper = sqlSession.getMapper(UserSignatureMapper.class);
            userSignatureMapper.insertUserSignatureList(userSignatureDtoList);
        } catch (Exception e) {
            throw new PairAdminDatabaseException("Failed to insert name/signature.", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<UserSignatureDto> getAllUserSignatureById(Long userRequestId) throws Exception {
        List<UserSignatureDto> userSignatureList = new ArrayList<UserSignatureDto>();
        try {
            UserSignatureMapper userSignatureMapper = sqlSession.getMapper(UserSignatureMapper.class);
            userSignatureList = userSignatureMapper.getAllUserSignatureById(userRequestId);
        } catch (Exception e) {
            throw new PairAdminDatabaseException("Failed to get all user signatures by Id for: " + userRequestId, e);
        }
        return userSignatureList;
    }

    /**
     * {@inheritDoc}
     */
    public void deleteUserSignatureByRequestId(Long userRequestId) throws Exception {
        try {
            UserSignatureMapper userSignatureMapper = sqlSession.getMapper(UserSignatureMapper.class);
            userSignatureMapper.deleteUserSignatureByRequestId(userRequestId);
        } catch (Exception e) {
            throw new PairAdminDatabaseException("Failed to delete user signatures for: " + userRequestId + e, e);
        }

    }

}
