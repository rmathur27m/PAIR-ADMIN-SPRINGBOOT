package gov.uspto.patent.privatePair.admin.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.uspto.patent.privatePair.admin.dao.mapper.UpdateApplicationAddressMapper;
import gov.uspto.patent.privatePair.admin.dto.UpdateApplicationAddressDto;
import gov.uspto.patent.privatePair.exceptionhandlers.PairAdminDatabaseException;

/**
 * Data Access Object (DAO) used to insert, delete and retrieve Update Address
 * Change requests.
 * 
 */
@Component
public class UpdateApplicationAddressDao implements UpdateApplicationAddressMapper {

    @Autowired
    private SqlSession sqlSession;

    /**
     * {@inheritDoc}
     */
    public void insertUpdateAppAddressRequestList(
            @Param("updateApplicationAddressNumbers") List<UpdateApplicationAddressDto> updateApplicationAddressDtoList)
            throws PairAdminDatabaseException {

        try {
            UpdateApplicationAddressMapper updateApplicationAddressMapper = sqlSession
                    .getMapper(UpdateApplicationAddressMapper.class);

            updateApplicationAddressMapper.insertUpdateAppAddressRequestList(updateApplicationAddressDtoList);
        } catch (Exception e) {
            throw new PairAdminDatabaseException("Failed to update Update Application Request", e);
        }
    }

    /**
     * Add an Update Application Address requests to the PAIR database.
     * 
     * @param fKeyUserRequestId
     *            User Request Id (foreign Key mapped to User Request table)
     * @param customerNumber
     *            customerNumber Customer Number associated with the request
     * @param application_id
     *            Application number
     * @param patentNumber
     *            PatentNumber
     * @param corrAddrChaneIn
     *            Indicator to change correspondence address or not
     * @param maintFeeAddrChaneIn
     *            Indicator to change correspondence address or not
     * @param attrAuthIn
     *            Power of Attorney Indicator 'Y/N'
     * @param userId
     *            Primary key
     */
    public void insertUpdateAppAddressRequestWhenListIsEmpty(@Param("fKeyUserRequestId") Long fKeyUserRequestId,
            @Param("customerNumber") Integer customerNumber, @Param("application_id") String application_id,
            @Param("patentNumber") String patentNumber, @Param("corrAddrChaneIn") String corrAddrChaneIn,
            @Param("maintFeeAddrChaneIn") String maintFeeAddrChaneIn, @Param("attrAuthIn") String attrAuthIn,
            @Param("userId") Long userId, @Param("requestErrorTx") String requestErrorTx,
            @Param("powerOfAttorneyIndicator") String powerOfAttorneyIndicator) {

        UpdateApplicationAddressMapper updateApplicationAddressMapper = sqlSession
                .getMapper(UpdateApplicationAddressMapper.class);

        updateApplicationAddressMapper.insertUpdateAppAddressRequestWhenListIsEmpty(fKeyUserRequestId, customerNumber,
                application_id, patentNumber, corrAddrChaneIn, maintFeeAddrChaneIn, attrAuthIn, userId, requestErrorTx,
                powerOfAttorneyIndicator);
    }

    /**
     * {@inheritDoc}
     */
    public void deleteUpdateAppAddressByRequestId(long userRequestId) {

        UpdateApplicationAddressMapper updateApplicationAddressMapper = sqlSession
                .getMapper(UpdateApplicationAddressMapper.class);

        updateApplicationAddressMapper.deleteUpdateAppAddressByRequestId(userRequestId);
    }

    /**
     * {@inheritDoc}
     */
    public List<UpdateApplicationAddressDto> getUpdateAppAddressRequestById(long userRequestId) {

        List<UpdateApplicationAddressDto> updateApplicationAddressDtoList = new ArrayList<UpdateApplicationAddressDto>();

        UpdateApplicationAddressMapper updateApplicationAddressMapper = sqlSession
                .getMapper(UpdateApplicationAddressMapper.class);

        updateApplicationAddressDtoList = updateApplicationAddressMapper.getUpdateAppAddressRequestById(userRequestId);

        return updateApplicationAddressDtoList;
    }

    /**
     * {@inheritDoc}
     */
    public Integer deleteUpdateApplicationAddressByUserRequestId(@Param("lastModifiedTimeStamp") Date lastModifiedTimeStamp,
            @Param("deleteIndicator") String deleteIndicator, @Param("requestStatusCount") String requestStatusCount,
            @Param("userRequestId") long userRequestId) {

        UpdateApplicationAddressMapper updateApplicationAddressMapper = sqlSession
                .getMapper(UpdateApplicationAddressMapper.class);

        return updateApplicationAddressMapper.deleteUpdateApplicationAddressByUserRequestId(lastModifiedTimeStamp,
                deleteIndicator, requestStatusCount, userRequestId);
    }

    /**
     * {@inheritDoc}
     */
    public Integer updateUpdateApplicationAddressRequest(
            @Param("updateApplicationAddressNumber") UpdateApplicationAddressDto updateApplicationAddressDto) {

        UpdateApplicationAddressMapper updateApplicationAddressMapper = sqlSession
                .getMapper(UpdateApplicationAddressMapper.class);

        return updateApplicationAddressMapper.updateUpdateApplicationAddressRequest(updateApplicationAddressDto);
    }
}
