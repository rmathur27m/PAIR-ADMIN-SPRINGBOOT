package gov.uspto.patent.privatePair.admin.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.uspto.patent.privatePair.admin.dao.mapper.EntityChangeRequestMapper;
import gov.uspto.patent.privatePair.admin.dto.EntityChangeRequestDto;
import gov.uspto.patent.privatePair.exceptionhandlers.PairAdminDatabaseException;

/**
 * DAO object representing Entity Change Request.
 * 
 */

@Component
public class EntityChangeRequestDao implements EntityChangeRequestMapper {

    @Autowired
    private SqlSession sqlSession;

    /**
     * {@inheritDoc}
     */
    public void insertEntityChangeRequest(EntityChangeRequestDto entityChangeRequestDto) throws Exception {
        try {
            EntityChangeRequestMapper entityChangeRequestMapper = sqlSession.getMapper(EntityChangeRequestMapper.class);
            entityChangeRequestMapper.insertEntityChangeRequest(entityChangeRequestDto);
        } catch (Exception e) {
            throw new PairAdminDatabaseException("Failed to insert the entity change request.", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void deleteEntityChangeRequestById(long userRequestId) throws Exception {
        try {
            EntityChangeRequestMapper entityChangeRequestMapper = sqlSession.getMapper(EntityChangeRequestMapper.class);
            entityChangeRequestMapper.deleteEntityChangeRequestById(userRequestId);
        } catch (Exception e) {
            throw new PairAdminDatabaseException("Failed to delete the entity change request for: " + userRequestId, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public EntityChangeRequestDto getEntityChangeRequestById(long userRequestId) throws Exception {
        EntityChangeRequestDto entityChangeRequestDto = null;
        try {
            EntityChangeRequestMapper entityChangeRequestMapper = sqlSession.getMapper(EntityChangeRequestMapper.class);
            entityChangeRequestDto = entityChangeRequestMapper.getEntityChangeRequestById(userRequestId);
        } catch (Exception e) {
            throw new PairAdminDatabaseException("Failed to get the entity change request for: " + userRequestId, e);
        }
        return entityChangeRequestDto;
    }

    /**
     * {@inheritDoc}
     */
    public void updateEntityChangeRequestById(EntityChangeRequestDto updatedEntityChangeRequestDto) throws Exception {
        try {
            EntityChangeRequestMapper entityChangeRequestMapper = sqlSession.getMapper(EntityChangeRequestMapper.class);
            entityChangeRequestMapper.updateEntityChangeRequestById(updatedEntityChangeRequestDto);
        } catch (Exception e) {
            throw new PairAdminDatabaseException("Failed to update the entity change request for: "
                    + updatedEntityChangeRequestDto.getUserRequestId(), e);
        }

    }
}
