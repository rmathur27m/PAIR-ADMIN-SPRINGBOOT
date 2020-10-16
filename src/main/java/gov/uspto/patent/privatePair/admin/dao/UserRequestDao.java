package gov.uspto.patent.privatePair.admin.dao;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.uspto.patent.privatePair.admin.dao.mapper.UserRequestMapper;
import gov.uspto.patent.privatePair.admin.dto.UserRequestDto;
import gov.uspto.patent.privatePair.exceptionhandlers.PairAdminDatabaseException;

/**
 * 
 * Data Access Object (DAO) used by MyBatis to access the PAIR User Request
 * table.
 */
@Component
public class UserRequestDao implements UserRequestMapper {

	private static final Logger  log = LoggerFactory.getLogger(UserRequestDao.class);
    @Autowired
    private SqlSession sqlSession;

    /**
     * {@inheritDoc}
     * @return
     */
    public long insertUserRequest(UserRequestDto userRequestDto) throws Exception {
        try {
        	log.info("inside UserRequestDao");
            UserRequestMapper userRequestMapper = sqlSession.getMapper(UserRequestMapper.class);
            userRequestMapper.insertUserRequest(userRequestDto);
        } catch (Exception e) {
        	log.error(e.getMessage());
            throw new PairAdminDatabaseException("Failed to insert the user Request: " + e);
        }
        log.error(userRequestDto.getUserCommentsText());
        return  (userRequestDto.getUserRequestId());
    }

    /**
     * {@inheritDoc}
     */
    public UserRequestDto getUserRequestById(long userRequestId) throws Exception {
        UserRequestDto userRequestDto = null;
        try {
            UserRequestMapper userRequestMapper = sqlSession.getMapper(UserRequestMapper.class);
            userRequestDto = userRequestMapper.getUserRequestById(userRequestId);
        } catch (Exception e) {
            throw new PairAdminDatabaseException("Failed to get user request for: " + userRequestId, e.getCause());
        }
        return userRequestDto;
    }

    /**
     * {@inheritDoc}
     */
    public void deleteUserRequestByUserRequestId(long userRequestId) throws Exception {
        try {
            UserRequestMapper userRequestMapper = sqlSession.getMapper(UserRequestMapper.class);
            userRequestMapper.deleteUserRequestByUserRequestId(userRequestId);
        } catch (Exception e) {
            throw new PairAdminDatabaseException("Failed to delete user request.", e.getCause());
        }
    }

    /**
     * {@inheritDoc}
     */
    public List<UserRequestDto> getAllUserRequests() throws Exception {
        List<UserRequestDto> userRequestDtoList = new ArrayList<UserRequestDto>();
        try {
            UserRequestMapper userRequestMapper = sqlSession.getMapper(UserRequestMapper.class);
            userRequestDtoList = userRequestMapper.getAllUserRequests();
        } catch (Exception e) {
            throw new PairAdminDatabaseException("Failed to get all user requests.", e.getCause());
        }
        return userRequestDtoList;
    }

    /**
     * {@inheritDoc}
     */
    public void updateUserRequest(UserRequestDto userRequestDto) throws Exception {
        try {
            UserRequestMapper userRequestMapper = sqlSession.getMapper(UserRequestMapper.class);
            userRequestMapper.updateUserRequest(userRequestDto);
        } catch (Exception e) {
            throw new PairAdminDatabaseException("Failed to update user request for : " + userRequestDto.getTypeOfRequest(),
                    e.getCause());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void updateRequestStatusByUserRequestId(UserRequestDto userRequestDto) throws Exception {
        try {
            UserRequestMapper userRequestMapper = sqlSession.getMapper(UserRequestMapper.class);
            userRequestMapper.updateRequestStatusByUserRequestId(userRequestDto);
        } catch (Exception e) {
            throw new PairAdminDatabaseException(
                    "Failed to update the request status for : " + userRequestDto.getTypeOfRequest(), e.getCause());
        }
    }

    /**
     * {@inheritDoc}
     */
    public Integer updateDeleteIndicatorByUserRequestId(UserRequestDto userRequestDto) throws Exception {
    	Integer val =null;
        try {
            UserRequestMapper userRequestMapper = sqlSession.getMapper(UserRequestMapper.class);
           val = userRequestMapper.updateDeleteIndicatorByUserRequestId(userRequestDto);
        } catch (Exception e) {
            throw new PairAdminDatabaseException("Failed to update the delete Indicator for: "
                    + userRequestDto.getTypeOfRequest(), e.getCause());
        }
        
        return val;
    }
}
