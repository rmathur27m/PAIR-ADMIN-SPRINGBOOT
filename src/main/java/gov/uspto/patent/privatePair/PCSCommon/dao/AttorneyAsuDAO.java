package gov.uspto.patent.privatePair.PCSCommon.dao;

import gov.uspto.patent.privatePair.PCSCommon.dto.*;
import gov.uspto.patent.privatePair.utils.ServiceException;
import gov.uspto.patent.privatePair.utils.StringUtil;
import org.hibernate.engine.jdbc.connections.spi.JdbcConnectionAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static gov.uspto.patent.privatePair.admin.dao.DaoUtil.daoUtil;
import static gov.uspto.patent.privatePair.admin.dao.DaoUtil.getDaoUtil;

@Component
@Configuration
@PropertySource("classpath:application.properties")
public class AttorneyAsuDAO {

    private static final Logger log = LoggerFactory.getLogger(AttorneyAsuDAO.class);

    @Autowired
    DataSource dataSource;

    public HashMap getAttorneyAsuByDN(String p_dn, String CASE_SENSITIVE_CHECK, String sortBy, String sortOrder)
            throws ServiceException, SQLException {
        //STATUS=SUCCESS/FAILURE/EMPTY
        //SUCCESS = Successful, Mappings and user Available
        //FAILURE = SystemError/RuntimeError/BusinessError(No Matching record found for the requested DN)
        //EMPTY	= User Available, No maapings
        log.info("****Start of getAttorneyAsuByDN method in AttorneyAsuDAO********");
        HashMap resultHashMap = new HashMap();
        List userMappingList = new ArrayList();
        String dn_escaped = StringUtil.replace(p_dn, "'", "''");
        //p_dn = dn_escaped;
        UserDTO userDto = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try (Connection dbConnection = dataSource.getConnection()) {
//			Class.forName(environment.getProperty("spring.datasource.driver-class-name"));
            //dbConnection=daoUtil.getDBConnection(PcsCommonDataConstant.PCS_JDBC_LOOKUP);
            //Query to get the userId and CommonName for requested DN
            String sqlUserQuery = userByDNQry(CASE_SENSITIVE_CHECK);
            log.info("sqlUserQuery at line 257 in AttorneyAsuDAO is -> \n" + sqlUserQuery);
            pstmt = dbConnection.prepareStatement(sqlUserQuery);
            pstmt.setString(1, p_dn);
            log.info("AttorneyAsuDAO.getAttorneyAsuByDN(): " + sqlUserQuery + " VALUES:" + p_dn);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                userDto = new UserDTO();
                userDto.setUserId(rs.getString(1));
                userDto.setUserCommonName(rs.getString(2));
                //TODO :  test the below line of code
                userDto.setUserType(PcsUtil.getCNAndGroup(p_dn)[1]);
                resultHashMap.put("REQUESTED_USER", userDto);
            } else {
                resultHashMap.put(PcsCommonConstant.STATUS, PcsCommonConstant.FAILURE);
                resultHashMap.put(PcsCommonConstant.STATUS_MESSAGE, PcsCommonMessage.NO_USER_FOR_DN);
                return resultHashMap;
            }

            //Retrieve UserIdMappings
            List mappingsList = getUserMappingsByUserId(userDto.getUserId(), sortBy, sortOrder);
            if (mappingsList == null || mappingsList.isEmpty()) {
                resultHashMap.put(PcsCommonConstant.STATUS, PcsCommonConstant.EMPTY);
                resultHashMap.put(PcsCommonConstant.STATUS_MESSAGE, PcsCommonMessage.NO_MAPPINGS_FOR_USER);
            } else {
                resultHashMap.put(PcsCommonConstant.STATUS, PcsCommonConstant.SUCCESS);
                resultHashMap.put(PcsCommonConstant.STATUS_MESSAGE, PcsCommonConstant.SUCCESS);
                resultHashMap.put("MAPPING_LIST", mappingsList);
            }

        } catch (SQLException sqlE) {
            log.error("AttorneyAsuDAO.getAttorneyAsuByDN() :" + sqlE.getMessage());
            // sqlE.printStackTrace();
            if (sqlE.getErrorCode() == 1017 || sqlE.getErrorCode() == 03114)
                throw new ServiceException(PcsCommonDataConstant.DB_CONNECTION_ERROR);

        } catch (Exception e) {

            log.error("AttorneyAsuDAO.getAttorneyAsuByDN() :" + e.getMessage());
            throw new ServiceException(PcsCommonDataConstant.SYSTEM_ERROR + " " + e.getMessage());

        } finally {
            daoUtil.closeResultSet(rs);
            daoUtil.closeStatement(pstmt);

        }
        log.info("****END of getAttorneyAsuByDN method in AttorneyAsuDAO********");
        return resultHashMap;
    }

    /*
     * userByDNQry is used to retrieve user details.
     * The data is queried from PAIR_USER_DN table.
     *
     */
    private String userByDNQry(String CASE_SENSITIVE_CHECK) {

        StringBuilder userByDnQueryBuf = new StringBuilder("SELECT PAIR_USER_DN_ID,COMMON_NM FROM PAIR_USER_DN ");
        userByDnQueryBuf.append("WHERE DELETE_FLAG is null AND ");

        if (CASE_SENSITIVE_CHECK.trim().equalsIgnoreCase(PcsCommonConstant.TRUE))
            userByDnQueryBuf.append("DN = ?");
        else
            userByDnQueryBuf.append("upper(DN) = upper(?)");

        return userByDnQueryBuf.toString();
    }

    public List<UserDTO> getUserMappingsByUserId(String userId, String sortBy, String sortOrder)
            throws ServiceException, SQLException {
        //STATUS=SUCCESS/FAILURE/EMPTY
        //SUCCESS = Successful, Mappings and user Available
        //FAILURE = SystemError/RuntimeError/BusinessError(No Matching record found for the requested DN)
        //EMPTY	= User Available, No maapings
        log.debug("********Start of getUserMappingsByUserId method is AttorneyAsuDAO**********");
        List<UserDTO> userMappingList = new ArrayList<UserDTO>();

        UserDTO userDto = null;
        PreparedStatement pstmtMappings = null;
        ResultSet rsMappings = null;
        Connection dbConnection =null;

        try  {
            dbConnection = dataSource.getConnection();
            String sqlMappingQueryStr = userMappingsQuery(userId, sortBy, sortOrder);
            log.debug("sqlMappingQueryStr at 176 is -> \n" + sqlMappingQueryStr);
            pstmtMappings = dbConnection.prepareStatement(sqlMappingQueryStr);
            pstmtMappings.setString(1, userId);
            log.debug("userId at 179 is -> \n" + userId);
            log.debug("AttorneyAsuDAO.getUserMappingsByUserId(): " + sqlMappingQueryStr + " VALUES:" + userId);
            rsMappings = pstmtMappings.executeQuery();
            if (null != rsMappings) {
                if (!rsMappings.next()) {
                    //If no mapping records present
                    log.debug("********if(!rsMappings.next()) at line 185**********");
                    return userMappingList;
                } else {
                    //Include the first mapping recored
                    log.debug("********else of (!rsMappings.next()) at line 190**********");
                    userDto = new UserDTO();
                    userDto.setUserId(rsMappings.getString(1));
                    userDto.setUserCommonName(rsMappings.getString(2));
                    userDto.setUserDn(rsMappings.getString(3));
                    userMappingList.add(userDto);
                }

                while (rsMappings.next()) {
                    //Include the remaining mapping records
                    userDto = new UserDTO();
                    userDto.setUserId(rsMappings.getString(1));
                    userDto.setUserCommonName(rsMappings.getString(2));
                    userDto.setUserDn(rsMappings.getString(3));
                    userMappingList.add(userDto);
                }//while(rsMappings.next())
            }

        } catch (SQLException sqlE) {
            log.error("AttorneyAsuDAO.getUserMappingsByUserId() :" + sqlE.getMessage());
            // sqlE.printStackTrace();
            if (sqlE.getErrorCode() == 1017 || sqlE.getErrorCode() == 03114)
                throw new ServiceException("DB_ERROR: Unable to connect to PAIR database.");
        } catch (Exception e) {
            log.error("AttorneyAsuDAO.getUserMappingsByUserId() :" + e.getMessage());
            throw new ServiceException("System Error: Please contact the system admin." + " " + e.getMessage());
        } finally {
            /*daoUtil.closeResultSet(rsMappings);
            daoUtil.closeStatement(pstmtMappings);*/
            getDaoUtil().closeResultSet(rsMappings);
            getDaoUtil().closeStatement(pstmtMappings);
            getDaoUtil().closeConnection(dbConnection);
        }
        log.debug("********END of getUserMappingsByUserId method is AttorneyAsuDAO**********");
        return userMappingList;
    }

    /*
     * The method "getAttorneyAsuByDN" retrieves attorney-authroized support mapping list
     * from PAIR_ATTORNEY_ASU table in PALM DB
     */

    /*
     * userMappingsQuery is used to retrieve attorney-authroized support mapping list.
     * The data is queried from PAIR_ATTORNEY_ASU table.
     *
     */
    private String userMappingsQuery(String requestUserId, String sortBy, String sortOrder) {

        StringBuffer userMappingsQueryBuf = new StringBuffer("SELECT d.pair_user_dn_id, d.COMMON_NM, d.dn FROM ");
        userMappingsQueryBuf.append("PAIR_USER_DN d, PAIR_ATTORNEY_ASU a ");
        userMappingsQueryBuf.append("WHERE d.DELETE_FLAG is null AND a.DELETE_IN is null AND ");
        //If the userId prefix is A then attorney mappings needs to be returned else ASU mappings needs to be returned
        if (requestUserId.substring(0, 1).equalsIgnoreCase("A"))
            userMappingsQueryBuf.append("upper(d.pair_user_dn_id) = upper(a.fk_attorney_pair_user_dn_id) and upper(a.fk_asu_pair_user_dn_id)=upper(?) ");
        else
            userMappingsQueryBuf.append("upper(d.pair_user_dn_id) = upper(a.fk_asu_pair_user_dn_id) and upper(a.fk_attorney_pair_user_dn_id)=upper(?) ");

        userMappingsQueryBuf.append(" ORDER BY ").append(userMappingsOrderString(sortBy, sortOrder));
        return userMappingsQueryBuf.toString();

    }

    private String userMappingsOrderString(String sortBy, String sortOrder) {
        String orderByString;

        if (sortBy.equals("UserId")) {
            if (sortOrder.equals("ASC"))
                orderByString = "1 ASC";
            else
                orderByString = "1 DESC";
        } else if (sortBy.equals("CommonName")) {
            if (sortOrder.equals("ASC"))
                orderByString = "2 ASC";
            else
                orderByString = "2 DESC";
        } else if (sortBy.equals("DN")) {
            if (sortOrder.equals("ASC"))
                orderByString = "3 ASC";
            else
                orderByString = "3 DESC";
        } else
            orderByString = "1 ASC";

        return orderByString;
    }


}
