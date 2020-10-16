package gov.uspto.patent.privatePair.PCSCommon.dao;

import gov.uspto.patent.privatePair.PCSCommon.dao.mapper.CustomerDetailsMapper;
import gov.uspto.patent.privatePair.PCSCommon.dto.CustNumsByDNRow;
import gov.uspto.patent.privatePair.PCSCommon.dto.PcsCommonDataConstant;
import gov.uspto.patent.privatePair.utils.ServiceException;
import gov.uspto.patent.privatePair.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import static gov.uspto.patent.privatePair.admin.dao.DaoUtil.daoUtil;
import static gov.uspto.patent.privatePair.admin.dao.DaoUtil.getDaoUtil;

@Component
@Configuration
@PropertySource("classpath:application.properties")
public class CustomerDetailsDAO {

    private static final Logger log = LoggerFactory.getLogger(CustomerDetailsDAO.class);
    @Autowired
    CustomerDetailsMapper customerDetailsMapper;
    @Autowired
    DataSource dataSource;

    public CustomerDetailsDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Vector getCustRowsByDN(String p_dn, String CASE_SENSITIVE_CHECK)
            throws ServiceException, SQLException {
        log.debug("****Start of getCustRowsByDN method in CustomerDetailsDAO********");
        Vector resultRowVect = new Vector();
        String sqlQueryStr = "";
        Statement stmt = null;
        ResultSet rs = null;
        Connection dbConnection = null;
        String reverseMode = "";

        try {
            dbConnection = dataSource.getConnection();
            sqlQueryStr = custNumAndPilotWithDNQuery(p_dn, CASE_SENSITIVE_CHECK);
            log.debug("Query = \n" + sqlQueryStr);

            stmt = dbConnection.createStatement();
            rs = stmt.executeQuery(sqlQueryStr);

            String custNumStr, success, dn, insertDate, updateDate, pilotTxt;
            if (null != rs) {
                while (rs.next()) {
                    success = rs.getString(1);
                    dn = rs.getString(2);
                    /**
                     * As per request from EFSWeb Team, removing 55000000 from customer number
                     * before returning the value to them.
                     */
                    custNumStr = "" + (rs.getBigDecimal(3).subtract(new BigDecimal(55000000)));
                    insertDate = rs.getString(4);
                    updateDate = rs.getString(5);
                    pilotTxt = rs.getString(6);
                    CustNumsByDNRow purmMaintCustRow =
                            new CustNumsByDNRow(custNumStr, insertDate, updateDate, pilotTxt);
                    resultRowVect.add(purmMaintCustRow);
                }
            }
            rs.close();
        } catch (SQLException sqlE) {
            log.error("ERROR: CustomerdetailsDAO.getCustRowsbyDN() :" + sqlE.getMessage());
            // sqlE.printStackTrace();
            if (sqlE.getErrorCode() == 1017 || sqlE.getErrorCode() == 03114)
                throw new ServiceException(PcsCommonDataConstant.DB_CONNECTION_ERROR);

        } catch (Exception e) {
            log.error("ERROR: CustomerdetailsDAO.getCustRowsbyDN() :" + e.getMessage());
            //e.printStackTrace();
        } finally {
            getDaoUtil().closeResultSet(rs);
            getDaoUtil().closeStatement(stmt);
            getDaoUtil().closeConnection(dbConnection);
        }
        log.debug("****End of getCustRowsByDN method in CustomerDetailsDAO********");
        return resultRowVect;
    }

    public String custNumAndPilotWithDNQuery(String dn, String caseSensitiveCheck) {
        log.info("****Start of custNumAndPilotWithDNQuery method in CustomerDetailsDAO********");
        String dnEscaped = StringUtil.replace(dn, "'", "''");
        String distinguishedName = dnEscaped;
        String custNumWithDNSql = "";
        if (caseSensitiveCheck != null && "TRUE".equalsIgnoreCase(caseSensitiveCheck.trim())) {
            custNumWithDNSql = "select originalquery.*, pilot_customer.ACTIVE_IN from ("
                    + "select 'success', "
                    + " PAIR_USER_DN.dn, "
                    + " PAIR_USER_CN.cust_num, "
                    + " nvl(to_char(PAIR_USER_CN.ins_date, 'mm/dd/yyyy'), '-'), "
                    + " nvl(to_char(PAIR_USER_CN.upd_date, 'mm/dd/yyyy'), '-') "
                    + " from PAIR_USER_DN, PAIR_USER_CN "
                    + " where upper(PAIR_USER_DN.pair_user_dn_id) = upper(PAIR_USER_CN.fk_pair_user_dn_id)  "
                    + " and  PAIR_USER_DN.dn = '"
                    + distinguishedName
                    + "' and PAIR_USER_DN.delete_flag is null "
                    + " and PAIR_USER_CN.delete_flag is null"
                    + " order by pair_user_cn.cust_num "
                    + ") originalquery left join pilot_customer on (originalquery.CUST_NUM - 55000000) = pilot_customer.CUSTOMER_NO";
        } else {
            custNumWithDNSql = "select originalquery.*, pilot_customer.ACTIVE_IN from ("
                    + "select 'success', "
                    + " PAIR_USER_DN.dn, "
                    + " PAIR_USER_CN.cust_num, "
                    + " nvl(to_char(PAIR_USER_CN.ins_date, 'mm/dd/yyyy'), '-'), "
                    + " nvl(to_char(PAIR_USER_CN.upd_date, 'mm/dd/yyyy'), '-') "
                    + " from PAIR_USER_DN, PAIR_USER_CN "
                    + " where upper(PAIR_USER_DN.pair_user_dn_id) = upper(PAIR_USER_CN.fk_pair_user_dn_id)  "
                    + " and  upper(PAIR_USER_DN.dn) = upper('"
                    + distinguishedName
                    + "') "
                    + " and PAIR_USER_DN.delete_flag is null "
                    + " and PAIR_USER_CN.delete_flag is null"
                    + " order by pair_user_cn.cust_num "
                    + ") originalquery left join pilot_customer on (originalquery.CUST_NUM - 55000000) = pilot_customer.CUSTOMER_NO";
        }
        log.debug("****End of custNumAndPilotWithDNQuery method in CustomerDetailsDAO********");
        return custNumWithDNSql;
    }

    public List<String> getRegistrationNumber(String dn)
            throws ServiceException {

        List<String> regNUM = new ArrayList<String>();
        try {
            regNUM = customerDetailsMapper.getRegistrationNumber(dn);
        } catch (Exception sqlE) {
            log.info("ERROR: CustomerdetailsDAO.getRegistrationNumber() :" + sqlE.getMessage());
            // sqlE.printStackTrace();
            //if(sqlE.getErrorCode()==1017 || sqlE.getErrorCode()==03114)
            throw new ServiceException(PcsCommonDataConstant.DB_CONNECTION_ERROR);
        }
        return regNUM;
    }

}
