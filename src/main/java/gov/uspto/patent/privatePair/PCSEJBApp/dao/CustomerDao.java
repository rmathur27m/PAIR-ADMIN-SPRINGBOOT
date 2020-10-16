package gov.uspto.patent.privatePair.PCSEJBApp.dao;

import gov.uspto.patent.privatePair.admin.dao.DaoUtil;
import gov.uspto.patent.privatePair.PCSEJBApp.dto.CustomerVo;
import gov.uspto.patent.privatePair.admin.dto.SearchCriteriaVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import gov.uspto.patent.privatePair.PCSEJBApp.dao.mapper.CustomerMapper;
import gov.uspto.patent.privatePair.utils.PcsEJBConstant;
import gov.uspto.patent.privatePair.utils.ServiceException;

import javax.sql.DataSource;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static gov.uspto.patent.privatePair.admin.dao.DaoUtil.daoUtil;
import static gov.uspto.patent.privatePair.admin.dao.DaoUtil.getDaoUtil;

@Component
@Qualifier("primaryDataSource") 
public class CustomerDao {
	
	private static final Logger  logger = LoggerFactory.getLogger(CustomerDao.class);
	 @Autowired
	 SqlSession sqlSession;

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;
	/*
	 * @Autowired CustomerMapper customerMapper;
	 */

    private static final String LOCK_CONTROL_NO = "1";


    String validateCustomerSQL =
            "SELECT count(*) FROM ipt1intppty WHERE CUST_NO = ?";

    String selectCustomerSQL =
            "SELECT count(*) FROM PILOT_CUSTOMER WHERE CUSTOMER_NO = ?";

    String insertCustSQL =
            "INSERT INTO PILOT_CUSTOMER (CUSTOMER_NO,REQUESTOR_FULL_NM,REQUESTOR_REGISTRATION_NO,REQUESTOR_TELEPHONE_NO,"
                    +
                    "REQUESTOR_EXT_NO,REQUESTOR_EMAIL_TX,CONTACT_FULL_NM,CONTACT_REGISTRATION_NO,CONTACT_TELEPHONE_NO,CONTACT_EXT_NO,"
                    +
                    "CONTACT_EMAIL_TX,ACTIVE_IN,ADD_TS,REMOVE_TS,CREATE_TS,CREATE_USER_ID,LAST_MOD_TS,LAST_MOD_USER_ID,LOCK_CONTROL_NO)"
                    + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    String updateCustSQL =
            "UPDATE PILOT_CUSTOMER SET REQUESTOR_FULL_NM = ?, REQUESTOR_REGISTRATION_NO = ?, REQUESTOR_TELEPHONE_NO = ?,"
                    +
                    "REQUESTOR_EXT_NO = ?, REQUESTOR_EMAIL_TX = ?, CONTACT_FULL_NM = ?, CONTACT_REGISTRATION_NO = ?, CONTACT_TELEPHONE_NO = ?,"
                    +
                    "CONTACT_EXT_NO = ?, CONTACT_EMAIL_TX = ?, ACTIVE_IN = ?, ADD_TS = ?, REMOVE_TS = ?,"
                    +
                    "CREATE_TS = ?, CREATE_USER_ID = ?, LAST_MOD_TS = ?, LAST_MOD_USER_ID = ?, LOCK_CONTROL_NO = ?"
                    + " WHERE CUSTOMER_NO = ?";

    String insertCustHistorySQL =
            "INSERT INTO PILOT_CUSTOMER_HISTORY (FK_PILOT_CUSTOMER_ID,CUSTOMER_NO,REQUESTOR_FULL_NM,REQUESTOR_REGISTRATION_NO,REQUESTOR_TELEPHONE_NO,"
                    +
                    "REQUESTOR_EXT_NO,REQUESTOR_EMAIL_TX,CONTACT_FULL_NM,CONTACT_REGISTRATION_NO,CONTACT_TELEPHONE_NO,CONTACT_EXT_NO,CONTACT_EMAIL_TX,ACTIVE_IN,"
                    + "ADD_TS,REMOVE_TS,CREATE_TS,CREATE_USER_ID,LAST_MOD_TS,LAST_MOD_USER_ID)" +
                    " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    String selectSQL =
            "SELECT CUSTOMER_NO,REQUESTOR_FULL_NM,REQUESTOR_REGISTRATION_NO, REQUESTOR_TELEPHONE_NO,"
                    +
                    " REQUESTOR_EXT_NO, REQUESTOR_EMAIL_TX,CONTACT_FULL_NM,CONTACT_REGISTRATION_NO, CONTACT_TELEPHONE_NO,"
                    + " CONTACT_EXT_NO, CONTACT_EMAIL_TX,ACTIVE_IN," +
                    " ADD_TS,REMOVE_TS,CREATE_TS,CREATE_USER_ID,LAST_MOD_TS,LAST_MOD_USER_ID" +
                    " FROM PILOT_CUSTOMER" + " WHERE CUSTOMER_NO = NVL(?,CUSTOMER_NO) " +
                    " AND GREATEST(NVL(ADD_TS, CREATE_TS),NVL(REMOVE_TS,CREATE_TS),CREATE_TS) >= to_date(?,'DD-MON-YY')"
                    +
                    " AND GREATEST(NVL(ADD_TS, CREATE_TS),NVL(REMOVE_TS,CREATE_TS),CREATE_TS) <= to_date(?,'DD-MON-YY') ORDER BY CUSTOMER_NO";

    String selectCustHistorySQL =
            "SELECT FK_PILOT_CUSTOMER_ID,CUSTOMER_NO,REQUESTOR_FULL_NM,REQUESTOR_REGISTRATION_NO,REQUESTOR_TELEPHONE_NO,"
                    +
                    " REQUESTOR_EXT_NO, REQUESTOR_EMAIL_TX,CONTACT_FULL_NM,CONTACT_REGISTRATION_NO, CONTACT_TELEPHONE_NO,CONTACT_EXT_NO,"
                    +
                    " CONTACT_EMAIL_TX,ACTIVE_IN,ADD_TS,REMOVE_TS,CREATE_TS,CREATE_USER_ID,LAST_MOD_TS,LAST_MOD_USER_ID"
                    + " FROM PILOT_CUSTOMER_HISTORY" + " WHERE CUSTOMER_NO = ? " +
                    " ORDER BY CREATE_TS DESC";

    String selectCustInfoSQL =
            "SELECT PILOT_CUSTOMER_ID,CUSTOMER_NO,REQUESTOR_FULL_NM,REQUESTOR_REGISTRATION_NO,REQUESTOR_TELEPHONE_NO,"
                    +
                    " REQUESTOR_EXT_NO,REQUESTOR_EMAIL_TX,CONTACT_FULL_NM,CONTACT_REGISTRATION_NO,CONTACT_TELEPHONE_NO,CONTACT_EXT_NO,"
                    +
                    " CONTACT_EMAIL_TX,ACTIVE_IN,ADD_TS,REMOVE_TS,CREATE_TS,CREATE_USER_ID,LAST_MOD_TS,LAST_MOD_USER_ID"
                    + " FROM PILOT_CUSTOMER" + " WHERE CUSTOMER_NO = ?";

    /**
     * Verifies if the customer numbers exists in PAIR_CUSTOMERS table. Sends the
     * error back to the user, to check the customer number.
     *
     * @return
     * @throws ServiceException
     * @throws //SQLException
     */
    public int customerValidation(String customerNo) throws Exception

     {
         int count = 0;

         try {
        	 CustomerMapper customerMapper = sqlSession
                     .getMapper(CustomerMapper.class);

             count=customerMapper.customerValidation(customerNo);
         }
         catch(SQLException sqlE){
             throw new
                     ServiceException(PcsEJBConstant.DB_CONNECTION_ERROR + sqlE.getMessage());
         }
         catch (Exception e) {
        	 logger.error("Exception caught in customerValidation DOA class"+ e.getMessage());
             throw e;
         }
         return count;
     }



     /* Verifies if the customer record already exists in PILOT_CUSTOMER table. Sends
     * the error back to the user, to update the customer record.
     *
     * @return
     * @throws ServiceException
     * @throws SQLException
     */

    public int checkCustomer(String customerNo) throws Exception {

        int count = 0;

        try {
        	CustomerMapper customerMapper = sqlSession
                    .getMapper(CustomerMapper.class);
            count=customerMapper.checkCustomer(customerNo);
        }
        catch(SQLException sqlE){
            throw new
                    ServiceException(PcsEJBConstant.DB_CONNECTION_ERROR + sqlE.getMessage());
        }
        catch (Exception e) {
        	logger.error("Exception caught in customerValidation DOA class"+ e.getMessage());
            throw e;
        }
        return count;

    }

    /**
     * Inserts the customer record in PILOT_CUSTOMER table in the database. Also
     * inserts the customer record in PILOT_CUSTOMER_HISTORY table in the
     * database when the Customer is opted in and opted out.
     *
     * @return
     * @throws ServiceException
     * @throws SQLException
     */
    public int saveCustomer(CustomerVo customerVO) throws ServiceException, SQLException {
        java.util.Date date = new java.util.Date();
        CustomerVo customerInfo = new CustomerVo();
        int count=0;
        int reCount=0;
        try {
        	CustomerMapper customerMapper = sqlSession
                    .getMapper(CustomerMapper.class);
            customerVO.setPilotAddedDate(new java.sql.Timestamp(date.getTime()));
            customerVO.setPilotRemovedDate(new java.sql.Timestamp(date.getTime()));
            customerVO.setDBCreatedDate(new java.sql.Timestamp(date.getTime()));
            customerVO.setLastModifyDate(new java.sql.Timestamp(date.getTime()));
            customerVO.setLockControlNO(1);
            count=customerMapper.saveCustomer(customerVO);
            // save the information in PILOT_CUSTOMER_HISTORY table.
            reCount= this.saveCustomerHistory(customerVO);
           /* // retrieve the saved record from PILOT_CUSTOMER table to use the
            // PILOT_CUSTOMER_ID column value.
            customerInfo = getCustomerInfo(customerVO.getCustomerNumber());

            //setting PilotCustomerId before inserting into history table
            customerVO.setPilotCustomerId(customerInfo.getPilotCustomerId());
            // save the information in PILOT_CUSTOMER_HISTORY table.
            count=customerMapper.insertCustomerHistory(customerVO);*/
        } catch (Exception e) {
            throw new ServiceException(PcsEJBConstant.PCS_REQUEST_FAILED + e.getMessage());
        }
        return reCount;
    }

    public int saveCustomerHistory(CustomerVo customerVO) throws ServiceException, SQLException {
        java.util.Date date = new java.util.Date();
        CustomerMapper customerMapper = sqlSession
                .getMapper(CustomerMapper.class);
        CustomerVo customerInfo = new CustomerVo();
        customerInfo = getCustomerInfo(customerVO.getCustomerNumber());
        //setting PilotCustomerId before inserting into history table
        customerVO.setPilotCustomerId(customerInfo.getPilotCustomerId());
        // save the information in PILOT_CUSTOMER_HISTORY table.
        customerVO.setPilotAddedDate(new java.sql.Timestamp(date.getTime()));
        customerVO.setPilotRemovedDate(new java.sql.Timestamp(date.getTime()));
        customerVO.setDBCreatedDate(new java.sql.Timestamp(date.getTime()));
        customerVO.setTransactionDate(new java.sql.Timestamp(date.getTime()));
        customerVO.setLastModifyDate(new java.sql.Timestamp(date.getTime()));
        return customerMapper.insertCustomerHistory(customerVO);
    }

    /**
     * Reads the customer information from PILOT_CUSTOMER table in the database.
     *
     * @return
     * @throws ServiceException
     * @throws SQLException
     */
    public CustomerVo getCustomerInfo(String customerNo) throws ServiceException, SQLException {

        CustomerVo customerInformation = new CustomerVo();
        try {
        	CustomerMapper customerMapper = sqlSession
                    .getMapper(CustomerMapper.class);
                customerInformation= customerMapper.getCustomerInfo(customerNo);

        } catch (Exception e) {
            throw e;
        }
        return customerInformation;
    }

    /**
     * Updates the customer record in PILOT_CUSTOMER table in the database. Also
     * inserts the customer record in PILOT_CUSTOMER_HISTORY table in the
     * database when the Customer is opted in and opted out.
     *
     * @return
     * @throws ServiceException
     * @throws SQLException
     */
    public int updateCustomer(CustomerVo customerVO) throws ServiceException, SQLException {
        CustomerVo customerInfo = new CustomerVo();
        String indicator = null;
        int count=0;
        java.util.Date date = new java.util.Date();
        CustomerMapper customerMapper = sqlSession
                .getMapper(CustomerMapper.class);
        try {
        	
            customerInfo = getCustomerInfo(customerVO.getCustomerNumber());
            if ("optIn".equalsIgnoreCase(customerVO.getIndicator())) {
                indicator = "Y";
            } else {
                indicator = "N";
            }
            customerVO.setIndicator(indicator);
            if (!indicator.equals(customerInfo.getIndicator())) {
                if ("Y".equalsIgnoreCase(indicator)) {
                    customerVO.setPilotAddedDate(new java.sql.Timestamp(date.getTime()));
                    customerVO.setPilotRemovedDate(null);
                   /* updateCust.setTimestamp(12, getCurrentTimeStamp());
                    updateCust.setTimestamp(13, pilotRemovedDate);*/
                }
                if ("N".equalsIgnoreCase(indicator)) {
                    customerVO.setPilotAddedDate(null);
                    customerVO.setPilotRemovedDate(new java.sql.Timestamp(date.getTime()));
                }
            } else {
                customerVO.setPilotAddedDate( customerInfo.getPilotAddedDate());
                customerVO.setPilotRemovedDate(customerInfo.getPilotRemovedDate());
            }
            customerVO.setDBCreatedDate(customerInfo.getDBCreatedDate());
            customerVO.setLastModifyDate(new java.sql.Timestamp(date.getTime()));
            //updating Customer
            customerMapper.updateCustomer(customerVO);

            // retrieve the saved record from PILOT_CUSTOMER table to use the
            // PILOT_CUSTOMER_ID column value.
            //customerInfo = getCustomerInfo(customerVO.getCustomerNumber());

            //setting PilotCustomerId before inserting into history table
            customerVO.setPilotCustomerId(customerInfo.getPilotCustomerId());
            // save the information in PILOT_CUSTOMER_HISTORY table.
            count=this.updateCustomerHistory(customerVO, customerInfo.getIndicator(), indicator);
           
        } catch (Exception e) {
            throw new ServiceException(PcsEJBConstant.PCS_REQUEST_FAILED + e.getMessage());
        }
        return count;
    }

    //<editor-fold desc="Description">
    // insert  the information in PILOT_CUSTOMER_HISTORY table after updating the customer
    public int updateCustomerHistory(CustomerVo customerVO, String custInfoIndicator, String indicator) throws ServiceException, SQLException {
        java.util.Date date = new java.util.Date();
        CustomerMapper customerMapper = sqlSession
                .getMapper(CustomerMapper.class);
        if (!indicator.equals(custInfoIndicator)) {
                if ("Y".equalsIgnoreCase(indicator)) {
                    customerVO.setPilotAddedDate(new java.sql.Timestamp(date.getTime()));
                    customerVO.setPilotRemovedDate(null);
                }
                if ("N".equalsIgnoreCase(indicator)) {
                    customerVO.setPilotAddedDate(null);
                    customerVO.setPilotRemovedDate(new java.sql.Timestamp(date.getTime()));
                }
                customerVO.setDBCreatedDate(new java.sql.Timestamp(date.getTime()));
                customerVO.setLastModifyDate(new java.sql.Timestamp(date.getTime()));
            try {
                return customerMapper.insertCustomerHistory(customerVO);
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }
    return 0;
    }
    
    /**
     * Reads the customer record in PILOT_CUSTOMER table in the database.
     * 
     * @return
     * @throws Exception 
     */
    public List<CustomerVo> getCustomersList(SearchCriteriaVo searchCriteria) throws Exception {
        String whereClause=null;
        System.out.println(searchCriteria);
        Connection conn1 = null;
        Connection nc1 = null;
        PreparedStatement selectCustomers = null;
        ResultSet rs = null;
        List searchResults = new ArrayList();
        Connection dbConnection = dataSource.getConnection();
        try {
            if (searchCriteria.getCustomerNo() != null) {
                int exists = this.checkCustomer(searchCriteria.getCustomerNo());
                if (exists <= 0) {
                    searchCriteria.setErrorMessage("Customer Number " + searchCriteria.getCustomerNo()
                            + " does not exist in the pilot, please verify and re-enter");
                    return null;
                }
            }

            /*conn1 = daoUtil.getDBConnection(PcsEJBConstant.PCS_JDBC_LOOKUP);
            nc1 = (Connection) WSJdbcUtil.getNativeConnection((WSJdbcConnection) conn1);*/

            if (searchCriteria.getStartDate() == null) {
                Statement stmt = null;
                ResultSet rs1 = null;

                String earliestDateSQL = "select MIN(CREATE_TS) from PILOT_CUSTOMER";
                java.sql.Date earliestDate = null; // MIN(CREATE_TS)
                try {
                    stmt = dbConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
                    rs1 = stmt.executeQuery(earliestDateSQL);
                    if (rs1.next()) {
                        earliestDate = rs1.getDate(1);
                    }

                    searchCriteria.setStartDate(earliestDate);
                } catch (SQLException e) {
                    throw e;
                } finally {
                    getDaoUtil().closeResultSet(rs1);
                    getDaoUtil().closeStatement(stmt);
                }
            }

            if (searchCriteria.getEndDate() == null) {
                searchCriteria.setEndDate(Calendar.getInstance().getTime());
            }
            StringBuilder sqlBuilder = new StringBuilder(selectSQL).append(" ").append(searchCriteria.getSortOrder());
            selectCustomers = dbConnection.prepareStatement(sqlBuilder.toString());
            selectCustomers.setString(1, searchCriteria.getCustomerNo());
            selectCustomers.setString(2, getTimeStamp(searchCriteria.getStartDate()));
            selectCustomers.setString(3, getTimeStamp(getCurrentDateplus24hours(searchCriteria.getEndDate())));

            // execute select SQL statement
            rs = selectCustomers.executeQuery();
            if(null!=rs) {
                    while (rs.next()) {
                        CustomerVo dto = new CustomerVo();

                        dto.setCreatedDate(rs.getDate("CREATE_TS"));
                        dto.setCustomerNumber(rs.getInt("CUSTOMER_NO") + "");
                        if ("Y".equalsIgnoreCase(rs.getString("ACTIVE_IN"))) {
                            dto.setIndicator("Opt-in");
                            dto.setTransactionDate(rs.getDate("ADD_TS"));
                        } else if ("N".equalsIgnoreCase(rs.getString("ACTIVE_IN"))) {
                            dto.setIndicator("Opt-out");
                            dto.setTransactionDate(rs.getDate("REMOVE_TS"));
                        }

                    	dto.setRequestorName(rs.getString("REQUESTOR_FULL_NM"));
                        dto.setRequestorEmail(rs.getString("REQUESTOR_EMAIL_TX"));
                        dto.setRequestorPhoneNumber(rs.getString("REQUESTOR_TELEPHONE_NO"));
                        dto.setPocName(rs.getString("CONTACT_FULL_NM"));
                        dto.setPocEmail(rs.getString("CONTACT_EMAIL_TX"));
                        dto.setPocPhoneNumber(rs.getString("CONTACT_TELEPHONE_NO"));

                        searchResults.add(dto);
                    }
                }
        } catch (SQLException e) {
            throw e;
        }
        finally {
            getDaoUtil().closeResultSet(rs);
            getDaoUtil().closeConnection(dbConnection);
            getDaoUtil().closeStatement(selectCustomers);
        }
        return searchResults;
    }
    
    public SearchCriteriaVo earliestDateSQL(SearchCriteriaVo searchCriteria) {
    	
    	java.sql.Date earliestDate = null;
    	CustomerMapper customerMapper = sqlSession.getMapper(CustomerMapper.class);
    	try {
    	earliestDate= customerMapper.earliestDateSQL();    	
    	searchCriteria.setStartDate(earliestDate);
    	}catch(Exception ex){
    		logger.error("Inside earliestDateSQL--->  "+ex.getMessage());
    	}
    	
    	return searchCriteria;
    }
    
    /**
     * Reads the customer history records from PILOT_CUSTOMER_HISTORY table in
     * the database.
     * 
     * @return
     * @throws ServiceException
     * @throws SQLException
     */
    public List<CustomerVo> getCustomerHistoryList(String customerNo) throws ServiceException, SQLException {

    	CustomerMapper customerMapper = sqlSession.getMapper(CustomerMapper.class);
        List customerHistoryResults = new ArrayList();
        List<CustomerVo> tempList= new ArrayList<CustomerVo>();
        try {


            // execute select SQL statement
        	tempList = customerMapper.getCustomerHistoryList(customerNo);

        	for(CustomerVo customerVo:tempList) {
        		CustomerVo dto = new CustomerVo();

                dto.setCustomerNumber(customerVo.getCustomerNumber());

                if ("Y".equalsIgnoreCase(customerVo.getIndicator())){
                    dto.setIndicator("Opt-in");
                    dto.setTransactionDate(customerVo.getPilotAddedDate());
                } else if ("N".equalsIgnoreCase(customerVo.getIndicator())) {
                    dto.setIndicator("Opt-out");
                    dto.setTransactionDate(customerVo.getPilotRemovedDate());
                }

                dto.setRequestorName(customerVo.getRequestorName());
                dto.setRequestorEmail(customerVo.getRequestorEmail());
                dto.setRequestorPhoneNumber(customerVo.getRequestorPhoneNumber());
                dto.setPocName(customerVo.getPocName());
                dto.setPocEmail(customerVo.getPocEmail());
                dto.setPocPhoneNumber(customerVo.getPocPhoneNumber());

                customerHistoryResults.add(dto);
        	}

        } catch (SQLException e) {
            throw e;
        }
        return customerHistoryResults;

    }
    
	/*
	 * public Date calGreatestDate(Date dateOne, Date dateTwo, Date dateThree) {
	 * 
	 * if(dateOne.before(dateTwo)) {
	 * 
	 * } return Date; }
	 * 
	 * public Date NVLDate(Date dateOne, Date dateTwo) {
	 * 
	 * if(null==dateOne || null==dateTwo) { return null; }else if(null!=dateOne){
	 * return dateOne; }else { return dateTwo; }
	 
    
    }*/
    
    private static Date getCurrentDateplus24hours(Date date) {
        Calendar cal = Calendar.getInstance();
        if (date != null) {
            cal.setTime(date);
        }
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
    }

    private static String getTimeStamp(java.util.Date date) {
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
            return sdf.format(date);
        } else {
            return null;
        }
    }

}