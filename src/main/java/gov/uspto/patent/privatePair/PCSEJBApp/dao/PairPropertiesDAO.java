package gov.uspto.patent.privatePair.PCSEJBApp.dao;

import gov.uspto.patent.privatePair.PCSEJBApp.dao.mapper.PairPropertiesMapper;
import gov.uspto.patent.privatePair.PCSEJBApp.dto.PairPropertyVO;
import gov.uspto.patent.privatePair.utils.PcsEJBConstant;
import gov.uspto.patent.privatePair.utils.ServiceException;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Component
public class PairPropertiesDAO {

    @Autowired
    DataSource dataSource;
    @Autowired
    SqlSession sqlSession;

    /* The method "getPairProperties" is used to retrieve records from PAIR_PROPERTY table.
     */
    public HashMap getPairPropertiesData(String mode, String propertyName, String sortBy, String sortOrder) throws ServiceException {
        PairPropertiesMapper pairPropertiesMapper = sqlSession.getMapper(PairPropertiesMapper.class);
        HashMap resultHashMap = new HashMap();
        List<PairPropertyVO> propertyList = new ArrayList<PairPropertyVO>();
        PairPropertyVO pairProperty = null;
        String sqlGroupQuery = null;

        String reverseMode = "";

        if (mode != null && mode.equalsIgnoreCase("PRIVATE"))
            reverseMode = "PUBLIC";
        else
            reverseMode = "PRIVATE";

        try {

            if (propertyName != null && !propertyName.trim().equals("") && !propertyName.equalsIgnoreCase("ALL")) {
                sqlGroupQuery = pairPropertiesGroupQry(reverseMode, propertyName);
            }

            System.out.println("INFO: PairPropertiesDAO.getPairProperties(): " + sqlGroupQuery + " VALUES:" + reverseMode);
            String tempPropertyName = pairPropertiesOrderString(sortBy, sortOrder);
            //rsGroup=groupPstmt.executeQuery();
            propertyList = pairPropertiesMapper.getPairPropertiesData(reverseMode, sqlGroupQuery, propertyName, tempPropertyName);

            //setting map!
            if (sqlGroupQuery == null || sqlGroupQuery.equals(""))
                resultHashMap.put("BLANK", propertyList);
            else
                resultHashMap.put(sqlGroupQuery, propertyList);

        } catch (SQLException sqlE) {

            System.out.println("ERROR: PairPropertiesDAO.getPairProperties() :" + sqlE.getMessage());
            if (sqlE.getErrorCode() == 1017 || sqlE.getErrorCode() == 03114)
                throw new ServiceException(PcsEJBConstant.DB_CONNECTION_ERROR);
        } catch (Exception e) {
            System.out.println("ERROR: PairPropertiesDAO.getPairProperties() :" + e.getMessage());
        }
        return resultHashMap;

    }


    /* reverseMode=PUBLIC/PRIVATE String propertyName!=ALL */
    private String pairPropertiesGroupQry(String reverseMode, String propertyName) {
        String result = null;
        PairPropertiesMapper pairPropertiesMapper = sqlSession.getMapper(PairPropertiesMapper.class);
        try {
            result = pairPropertiesMapper.pairPropertiesGroupQry(reverseMode, propertyName);
        } catch (Exception ex) {
            System.out.println("Excpetion caugth into pairPropertiesGroupQry" + ex.getMessage());
            ex.printStackTrace();
        }

        return result;
    }

    private String pairPropertiesOrderString(String sortBy, String sortOrder) {
        String orderByString;

        if (sortBy.equals("PROPERTY_NM")) {
            if (sortOrder.equals("ASC"))
                orderByString = "2 ASC";
            else
                orderByString = "2 DESC";
        } else if (sortBy.equals("PROPERTY_KEY_TX")) {
            if (sortOrder.equals("ASC"))
                orderByString = "3 ASC";
            else
                orderByString = "3 DESC";
        } else
            orderByString = "2 ASC";

        return orderByString;
    }
}
