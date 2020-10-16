package gov.uspto.patent.privatePair.admin.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.uspto.patent.privatePair.admin.dao.mapper.ViewRequestMapper;
import gov.uspto.patent.privatePair.admin.dto.ViewRequestDto;
import gov.uspto.patent.privatePair.exceptionhandlers.PairAdminDatabaseException;

/**
 * 
 * Data Access Object (DAO) used by MyBatis to access the PAIR User Request
 * table.
 */

@Component
public class ViewRequestDao {
	
	

    @Autowired
    private SqlSession sqlSession;

    /**
     * {@inheritDoc}
     */
    public List<ViewRequestDto> getEntityRequestsforView(@Param("requestStatusList") List requestStatusList,
            @Param("requestDays") String requestDays, @Param("requestType") String requestType,
            @Param("privatePAIRdn") String privatePAIRdn) throws Exception {

        List<ViewRequestDto> viewEntityList = null;

        try {

            viewEntityList = new ArrayList<ViewRequestDto>();

            ViewRequestMapper viewRequestMapper = sqlSession.getMapper(ViewRequestMapper.class);

            viewEntityList = viewRequestMapper.getEntityRequestsforView(requestStatusList, requestDays, requestType,
                    privatePAIRdn);
        } catch (Exception e) {
            throw new PairAdminDatabaseException("Failed to Retrieve Entity change request List for View.", e);
        }

        return viewEntityList;
    }

    /**
     * {@inheritDoc}
     */
    public List<ViewRequestDto> getUpdateAddrRequestsforView(@Param("requestStatusList") List requestStatusList,
            @Param("requestDays") String requestDays, @Param("requestType") String requestType,
            @Param("privatePAIRdn") String privatePAIRdn) throws Exception {

        List<ViewRequestDto> viewUpdateAddrList = null;

        try {
            viewUpdateAddrList = new ArrayList<ViewRequestDto>();

            ViewRequestMapper viewRequestMapper = sqlSession.getMapper(ViewRequestMapper.class);

            viewUpdateAddrList = viewRequestMapper.getUpdateAddrRequestsforView(requestStatusList, requestDays, requestType,
                    privatePAIRdn);
        } catch (Exception e) {
            throw new PairAdminDatabaseException("Failed to Retrieve Update Address change request List for View.", e);
        }
        return viewUpdateAddrList;
    }

    /**
     * {@inheritDoc}
     */
    public List<ViewRequestDto> getCustomerRequestsforView(@Param("requestStatusList") List requestStatusList,
            @Param("requestDays") String requestDays, @Param("requestType") String requestType,
            @Param("privatePAIRdn") String privatePAIRdn) throws Exception {

        List<ViewRequestDto> viewCustomerList = null;

        try {
            viewCustomerList = new ArrayList<ViewRequestDto>();

            ViewRequestMapper viewRequestMapper = sqlSession.getMapper(ViewRequestMapper.class);

            viewCustomerList = viewRequestMapper.getCustomerRequestsforView(requestStatusList, requestDays, requestType,
                    privatePAIRdn);
        } catch (Exception e) {
            throw new PairAdminDatabaseException("Failed to Retrieve New Customer Request List for View.", e);
        }

        return viewCustomerList;
    }
}
