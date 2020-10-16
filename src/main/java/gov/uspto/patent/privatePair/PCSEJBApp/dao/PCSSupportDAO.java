package gov.uspto.patent.privatePair.PCSEJBApp.dao;

import gov.uspto.patent.privatePair.PCSEJBApp.dto.EofficeNotificationStatusCheckVO;
import gov.uspto.patent.privatePair.PCSEJBApp.dto.OCNArchiveSearchResultVO;
import gov.uspto.patent.privatePair.utils.PcsEJBConstant;
import gov.uspto.patent.privatePair.utils.ServiceException;
import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.internal.OracleCallableStatement;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static gov.uspto.patent.privatePair.admin.dao.DaoUtil.getDaoUtil;


@Component
@Configuration
@PropertySource("classpath:application.properties")
public class PCSSupportDAO {

    static final String ALLAVAILABLE = "allAvailable";
    static final String APPENDERFORAPPNUMPOSTUNION = " ifwa.APPL_ID IN  ";
    static final String APPENDERFORAPPNUMPRE = " P.APPL_ID IN ";
    static final String APPENDERFORAPPNUMPREUNION = " ifw.APPL_ID IN  ";
    static final String APPENDERFORCUSTNUMPOSTUNION = " ifwa.CUSTOMER_NO IN  ";
    static final String APPENDERFORCUSTNUMPREUNION = " ifw.CUSTOMER_NO IN  ";
    static final String APPENDERFORCUSTOMERNO = " P.CUSTOMER_NO IN ";
    static final String APPENDERFORUPLOADDATEGREATERTHANPOSTUNION = " ifwa.UPLOAD_DT <= SYSDATE -  ";

    /*
     * static final String eNotificationSQL =
     * "SELECT P.APPL_ID,P.CUSTOMER_NO,C.CREATE_TS,E.MAILROOM_DT,E.PAGE_CNT_QT,E.DN_IFW_DOC_ID,E.DN_IFW_PKG_ID,E.PRCSG_STAT_CT,E.PRCSG_TS,E.PRCSG_REASON_CD,"
     * +
     * "E.PRCSG_RETURN_CD,E.FK_EOCL_IMAGE_UPLOAD_DT,E.FK_EOCL_ACTN_STAT_CT,E.FK_EOCL_APC_FK_PC_ID,E.REVIEW_DT,E.LAST_MOD_USR_ID,E.LAST_MOD_TS "
     * +
     * "FROM PAIR_IFW_OUTGOING P, ELCTRN_DOC_SCAN_LOG E, ELCTRN_OUTGOING_CORR_LOG C "
     * + "WHERE P.DOC_ID = E.DN_IFW_DOC_ID " + "AND P.FK_PC_ID = C.FK_APC_FK_PC_ID "
     * + "AND C.ACTN_STAT_CT = 'RDYFORVIEW'";
     */
    static final String APPENDERFORUPLOADDATEGREATERTHANPREUNION = " ifw.UPLOAD_DT <= SYSDATE -  ";
    static final String APPENDERFORUPLOADDATEPOSTUNION = " ifwa.UPLOAD_DT >= SYSDATE -  ";
    static final String APPENDERFORUPLOADDATEPREUNION = " ifw.UPLOAD_DT >= SYSDATE -  ";
    static final String APPLICATIONID = "ApplicatinID";
    static final String APPLNUM = "applnum";
    static final String CUSTNUM = "customernum";
    static final String CUSTOMERNUMBER = "CustomerNumber";
    static final String ENDDATE = "enddate";
    static final String FROM = "from";
    // static final String IMGUPLOADDT=" C. IMAGE_UPLOAD_DT ";
    static final String IMGUPLOADDT = " E. FK_EOCL_IMAGE_UPLOAD_DT";
    static final String MAILRMDATE = "mailroomdate";
    static final String MAILROOMDATE = " E.MAILROOM_DT ";
    static final String NUMOFDAYS = "numOfDays";
    static final String ORDERBY = " ORDER BY 10 DESC ";
    static final String PROCESSSTAT = " E.PRCSG_STAT_CT ";
    static final String PROCESSSTATUS = "processstatus";
    static final String SEARCHQUERYPOSTUNION = "SELECT CUSTOMER_NO, APPL_ID, PATENT_NO, MAIL_ROOM_DT,  DOC_CATEGORY, DOC_ID, DOC_DESCRIPTION_TX, "
            + " LAST_MODIFIED_TS, ATTY_DKT_NO, UPLOAD_DT, POSTCARD_NOTIFICATION_IN, DOC_VIEW_DT, PATENT_ISSUE_DT,DOCUMENT_CD, DOC_VIEWER_NM "
            + " FROM PAIR_IFW_OUTGOING_ARCH ifwa WHERE ";
    // Constants
    static final String SEARCHQUERYPREUNION = "SELECT CUSTOMER_NO, APPL_ID, PATENT_NO, MAIL_ROOM_DT,  DOC_CATEGORY, DOC_ID, DOC_DESCRIPTION_TX, "
            + " LAST_MODIFIED_TS, ATTY_DKT_NO, UPLOAD_DT, POSTCARD_NOTIFICATION_IN, DOC_VIEW_DT, PATENT_ISSUE_DT,DOCUMENT_CD, DOC_VIEWER_NM "
            + " FROM PAIR_IFW_OUTGOING ifw WHERE ";
    static final String STARTDATE = "startdate";
    static final String TO = "to";
    static final String UNION = " UNION ";
    static final String UPLOADDATE = "imageuploaddate";
    static final String UPLOADDT = " P. UPLOAD_DT ";
    static final String eNotificationSQL = "SELECT P.APPL_ID,P.CUSTOMER_NO,E.MAILROOM_DT,E.PAGE_CNT_QT,E.DN_IFW_DOC_ID,E.DN_IFW_PKG_ID,E.PRCSG_STAT_CT,E.PRCSG_TS,E.PRCSG_REASON_CD,"
            + "E.PRCSG_RETURN_CD,E.FK_EOCL_IMAGE_UPLOAD_DT,E.FK_EOCL_ACTN_STAT_CT,E.FK_EOCL_APC_FK_PC_ID,E.REVIEW_DT,E.LAST_MOD_USR_ID,E.LAST_MOD_TS "
            + "FROM PAIR_IFW_OUTGOING P, ELCTRN_DOC_SCAN_LOG E " + "WHERE P.DOC_ID = E.DN_IFW_DOC_ID ";
    @Autowired
    DataSource dataSource;

    /**
     * Queries db for system announcement. Requires native jdbc connection object
     * for compatibility with oracle type ARRAY
     *
     * @return
     * @throws ServiceException
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public String[] getSystemAnnouncement(String msg) throws ServiceException, SQLException, ClassNotFoundException {
        Connection conn = null;
        Connection nc = null;
        Statement st = null;
        String[] message = {};
        ResultSet rs = null;
        Connection dbConnection=null;
//		Class.forName(environment.getProperty("spring.datasource.driver-class-name"));
       
        Object array = new Object();
        
        try {
        	  dbConnection = dataSource.getConnection();
        	st = dbConnection.createStatement();
            if (msg != null && msg.equalsIgnoreCase("active")) {
				/*array = jdbcTemplate.queryForObject("SELECT property_value_varray FROM pair_property "
						+ "WHERE property_nm='ImportantMessage' AND property_mode_tx='PRIVATE' AND property_key_tx='active'",
						Object.class);*/
                rs = st.executeQuery("SELECT property_value_varray FROM pair_property" +
                        " WHERE property_nm='ImportantMessage' AND property_mode_tx='PRIVATE' AND property_key_tx='active'");

            } else if (msg != null && msg.equalsIgnoreCase("default")) {
				/*array = jdbcTemplate.queryForObject("SELECT property_value_varray FROM pair_property "
						+ "WHERE property_nm='ImportantMessage' AND property_mode_tx='PRIVATE' AND property_key_tx='Default'",
						Object.class);*/
                rs = st.executeQuery("SELECT property_value_varray FROM pair_property" +
                        " WHERE property_nm='DefaultMessage' AND property_mode_tx='PRIVATE' AND property_key_tx='Default'");
            }
            if (null != rs) {
                rs.next();
                Array a =  rs.getArray(1);
                message = (String[]) a.getArray(); // convert oracle ARRAY into java Array
                System.out.println(message);
            }
        } catch (SQLException sqlE) {
            throw new ServiceException(PcsEJBConstant.DB_CONNECTION_ERROR);
        } catch (Exception e) {
            throw new ServiceException(PcsEJBConstant.PCS_REQUEST_FAILED + " " + e.getMessage());
        } finally {        	
            getDaoUtil().closeResultSet(rs);
            getDaoUtil().closeStatement(st);
            getDaoUtil().closeConnection(dbConnection);
        }

        return message;
    }

    /**
     * Query db to get eNofitification information
     *
     * @param searchParams
     * @throws ServiceException
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public List<EofficeNotificationStatusCheckVO> pcsSupportSearchEnotifications(Map searchParams)
            throws ServiceException, ClassNotFoundException, SQLException {
        String custNum = null;
        String appNum = null;
        String startDate = null;
        String endDate = null;
        String mailRoomDate = null;
        String uploadDate = null;
        String status = null;
        boolean isSearchDateExist = true;
        boolean isMailDateExist = true;
        boolean isImageDateExist = true;
        boolean isappNumExist = true;
//		Class.forName(environment.getProperty("spring.datasource.driver-class-name"));
        Connection dbConnection = dataSource.getConnection();
        StringBuilder sqlToExecute = new StringBuilder();
        if (null != searchParams) {
            custNum = null != searchParams.get(CUSTNUM) ? (String) searchParams.get(CUSTNUM).toString().trim() : null;
            appNum = null != searchParams.get(APPLNUM) ? (String) searchParams.get(APPLNUM).toString().trim() : null;
            startDate = null != searchParams.get(STARTDATE) ? (String) searchParams.get(STARTDATE).toString().trim()
                    : null;
            endDate = null != searchParams.get(ENDDATE) ? (String) searchParams.get(ENDDATE).toString().trim() : null;
            mailRoomDate = null != searchParams.get(MAILRMDATE) ? (String) searchParams.get(MAILRMDATE).toString().trim()
                    : null;
            uploadDate = null != searchParams.get(UPLOADDATE) ? (String) searchParams.get(UPLOADDATE).toString().trim()
                    : null;
            status = null != searchParams.get(PROCESSSTATUS)
                    ? (String) searchParams.get(PROCESSSTATUS).toString().trim()
                    : null;
        }

        sqlToExecute.append(eNotificationSQL);
        if (custNum != null && !custNum.equalsIgnoreCase("") && !custNum.equalsIgnoreCase("''")) {
            sqlToExecute.append(" AND ");
            sqlToExecute.append(APPENDERFORCUSTOMERNO + "(" + custNum + ")");
        }
        if (appNum != null && !appNum.equalsIgnoreCase("") && !appNum.equalsIgnoreCase("''")) {
            sqlToExecute.append(" AND ");
            sqlToExecute.append(APPENDERFORAPPNUMPRE + "(" + appNum + ")");
        } else {
            isappNumExist = false;
        }

        if (startDate != null && !startDate.equalsIgnoreCase("") && (endDate == null || endDate.equalsIgnoreCase(""))) {

            sqlToExecute.append(" AND ");
            sqlToExecute.append("to_char(E.PRCSG_TS,'MM/DD/YYYY') ='" + startDate + "'");
        }

        if (endDate != null && !endDate.equalsIgnoreCase("")) {
            sqlToExecute.append(" AND ");
            // sqlToExecute.append("to_char(E.PRCSG_TS,'MM/DD/YYYY') >= '"+startDate+"'");
            sqlToExecute.append("E.PRCSG_TS >= to_date('" + startDate + "', 'MM/DD/YYYY') ");
            sqlToExecute.append(" AND ");
            // sqlToExecute.append("to_char(E.PRCSG_TS,'MM/DD/YYYY') <= '"+endDate+"'");
            sqlToExecute.append("E.PRCSG_TS <= to_date('" + endDate + "', 'MM/DD/YYYY') + 1");
        }

        if (mailRoomDate != null && !mailRoomDate.equalsIgnoreCase("")) {
            sqlToExecute.append(" AND ");
            sqlToExecute.append("to_char(" + MAILROOMDATE + ",'MM/DD/YYYY') ='" + mailRoomDate + "'");
        } else {
            isMailDateExist = false;
        }

        if (uploadDate != null && !uploadDate.equalsIgnoreCase("")) {
            sqlToExecute.append(" AND ");
            sqlToExecute.append("to_char(" + UPLOADDT + ",'MM/DD/YYYY') ='" + uploadDate + "'");
            sqlToExecute.append(" AND ");
            sqlToExecute.append("to_char(" + IMGUPLOADDT + ",'MM/DD/YYYY') ='" + uploadDate + "'");
        } else {
            isImageDateExist = false;
        }

        if ((startDate == null || startDate.equalsIgnoreCase(""))
                && ((endDate == null || endDate.equalsIgnoreCase("")))) {
            isSearchDateExist = false;
        }

        if ((custNum != null && !custNum.equalsIgnoreCase("''")) && !isappNumExist && !isSearchDateExist
                && !isMailDateExist && !isImageDateExist) {
            sqlToExecute.append(" AND ");
            sqlToExecute.append("E.PRCSG_TS >= TRUNC(SYSDATE) - 90 ");

        }
        if (status != null && !status.equalsIgnoreCase("") && !status.equalsIgnoreCase("ALL")) {
            sqlToExecute.append(" AND ");
            sqlToExecute.append(PROCESSSTAT + "='" + status + "'");
        }
        sqlToExecute.append(" ORDER BY E.PRCSG_TS DESC,P.CUSTOMER_NO,P.APPL_ID");

        // Connection dbConnection = null;
        Statement stmt = null;
        ResultSet rsCount = null;
        List<EofficeNotificationStatusCheckVO> searchResults = new ArrayList<EofficeNotificationStatusCheckVO>();
        System.out.println("e-Office Action Search query:****" + sqlToExecute.toString());

        try {
            // dbConnection=daoUtil.getDBConnection(PcsEJBConstant.PCS_JDBC_LOOKUP);
            stmt = dbConnection.createStatement();
            rsCount = stmt.executeQuery(sqlToExecute.toString());
            if (null != rsCount) {
                while (rsCount.next()) {
                    EofficeNotificationStatusCheckVO dto = new EofficeNotificationStatusCheckVO();

                    dto.setApplID(rsCount.getString(1));
                    dto.setCustomerNumber(rsCount.getInt(2));
                    // domain.setCreateTs(rsCount.getTimestamp(3));
                    dto.setMailRoomDt(rsCount.getDate(3));
                    dto.setPageCnt(rsCount.getString(4));
                    dto.setDocId(rsCount.getString(5));
                    dto.setPackId(rsCount.getString(6));
                    dto.setProcesStatusCode(rsCount.getString(7));
                    dto.setProcessTs(rsCount.getDate(8));
                    dto.setPrcsReasonCd(rsCount.getString(9));
                    dto.setPrcsRetunCd(rsCount.getString(10));
                    dto.setImageUploadDt(rsCount.getDate(11));
                    dto.setActionStatusCd(rsCount.getString(12));
                    dto.setFkPcId(rsCount.getString(13));
                    dto.setReviewDt(rsCount.getDate(14));
                    dto.setLastModify(rsCount.getString(15));
                    dto.setLastMoifyTs(rsCount.getTimestamp(16));

                    searchResults.add(dto);
                }
            }

        } catch (SQLException sqlE) {
            throw new ServiceException(PcsEJBConstant.NO_REC_FOUND);
        } catch (Exception e) {
            throw new ServiceException(PcsEJBConstant.PCS_REQUEST_FAILED + " " + e.getMessage());
        } finally {            
            getDaoUtil().closeResultSet(rsCount); 
            getDaoUtil().closeStatement(stmt);
            getDaoUtil().closeConnection(dbConnection);
        }
        return searchResults;
    }

    /**
     * @param searchParams
     * @throws ServiceException
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public List<OCNArchiveSearchResultVO> pcsSupportSearchOCNRows(Map searchParams)
            throws ServiceException, ClassNotFoundException, SQLException {
        String numOfdays = null;
        String custNum = null;
        String appNum = null;
        String searchedByCriteria = null;
        String from = null;
        String to = null;
        StringBuilder buildQuery = new StringBuilder();
        custNum = (String) searchParams.get(CUSTOMERNUMBER);
        appNum = (String) searchParams.get(APPLICATIONID);
        boolean custNumSwitch = custNumSwitch(custNum, appNum);
        searchedByCriteria = appCustNumCheck(custNum, appNum);

        if (searchParams.containsKey(NUMOFDAYS)) {
            numOfdays = (String) searchParams.get(NUMOFDAYS);
            buildQueryForLastDays(numOfdays, searchedByCriteria, buildQuery, custNumSwitch);
        } else if (searchParams.containsKey(ALLAVAILABLE)) {
            buildQueryForAllAvailable(searchedByCriteria, buildQuery, custNumSwitch);
        } else if (searchParams.containsKey(FROM) && searchParams.containsKey(TO)) {
            from = (String) searchParams.get(FROM);
            to = (String) searchParams.get(TO);
            buildQueryForDaysRange(searchedByCriteria, from, to, buildQuery, custNumSwitch);
        }

        return pcsSupportSearchOCNRows(buildQuery.toString());
    }

    /**
     * @param custNum
     * @param appNum
     * @return
     */
    private boolean custNumSwitch(String custNum, String appNum) {
        if (custNum != null && custNum.trim().length() != 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param custNum
     * @param appNum
     * @return
     */
    private String appCustNumCheck(String custNum, String appNum) {
        String appNumOrCustNum = "";
        if (custNum != null && custNum.trim().length() != 0) {
            appNumOrCustNum = custNum;
        } else {
            appNumOrCustNum = appNum;
        }
        return appNumOrCustNum;
    }

    /**
     * Overloaded
     *
     * @param numOfdays
     * @param custNum
     * @param appNum
     */
    private void buildQueryForLastDays(String numOfdays, String appNumOrCustNum, StringBuilder buildQuery,
                                       boolean custNumSwitch) {
        buildQuery.append(SEARCHQUERYPREUNION);
        buildQuery.append(APPENDERFORUPLOADDATEPREUNION);
        buildQuery.append(numOfdays);

        if (custNumSwitch) {
            buildQuery.append(" AND " + APPENDERFORCUSTNUMPREUNION);
            buildQuery.append(" ( " + appNumOrCustNum + " ) ");
        } else {
            buildQuery.append(" AND " + APPENDERFORAPPNUMPREUNION);
            buildQuery.append(" ( " + appNumOrCustNum + " ) ");
        }

        buildQuery.append(UNION);

        buildQuery.append(SEARCHQUERYPOSTUNION);
        buildQuery.append(APPENDERFORUPLOADDATEPOSTUNION);
        buildQuery.append(numOfdays);

        if (custNumSwitch) {
            buildQuery.append(" AND " + APPENDERFORCUSTNUMPOSTUNION);
            buildQuery.append(" ( " + appNumOrCustNum + " ) ");
        } else {
            buildQuery.append(" AND " + APPENDERFORAPPNUMPOSTUNION);
            buildQuery.append(" ( " + appNumOrCustNum + " ) ");
        }
        buildQuery.append(ORDERBY);
    }

    private void buildQueryForAllAvailable(String appNumOrCustNum, StringBuilder buildQuery, boolean custNumSwitch) {
        buildQuery.append(SEARCHQUERYPREUNION);
        // buildQuery.append(APPENDERFORUPLOADDATEPREUNION);

        if (custNumSwitch) {
            buildQuery.append(APPENDERFORCUSTNUMPREUNION);
            buildQuery.append(" ( " + appNumOrCustNum + " ) ");
        } else {
            buildQuery.append(APPENDERFORAPPNUMPREUNION);
            buildQuery.append(" ( " + appNumOrCustNum + " ) ");
        }

        buildQuery.append(UNION);

        buildQuery.append(SEARCHQUERYPOSTUNION);

        if (custNumSwitch) {
            buildQuery.append(APPENDERFORCUSTNUMPOSTUNION);
            buildQuery.append(" ( " + appNumOrCustNum + " ) ");
        } else {
            buildQuery.append(APPENDERFORAPPNUMPOSTUNION);
            buildQuery.append(" ( " + appNumOrCustNum + " ) ");
        }
        buildQuery.append(ORDERBY);
    }

    /**
     * Overloaded
     *
     * @param custNum
     * @param appNum
     */
    private void buildQueryForDaysRange(String appNumOrCustNum, String from, String to, StringBuilder buildQuery,
                                        boolean custNumSwitch) {
        buildQuery.append(SEARCHQUERYPREUNION);
        if (custNumSwitch) {
            buildQuery.append(APPENDERFORCUSTNUMPREUNION);
            buildQuery.append(" ( " + appNumOrCustNum + " ) ");
        } else {
            buildQuery.append(APPENDERFORAPPNUMPREUNION);
            buildQuery.append(" ( " + appNumOrCustNum + " ) ");
        }
        if (from != null && from.length() > 0) {
            buildQuery.append(" AND " + APPENDERFORUPLOADDATEGREATERTHANPREUNION + from);
        }
        if (to != null && to.length() > 0) {
            buildQuery.append(" AND " + APPENDERFORUPLOADDATEPREUNION + to);
        }

        buildQuery.append(UNION);

        buildQuery.append(SEARCHQUERYPOSTUNION);
        if (custNumSwitch) {
            buildQuery.append(APPENDERFORCUSTNUMPOSTUNION);
            buildQuery.append(" ( " + appNumOrCustNum + " ) ");
        } else {
            buildQuery.append(APPENDERFORAPPNUMPOSTUNION);
            buildQuery.append(" ( " + appNumOrCustNum + " ) ");
        }

        if (from != null && from.length() > 0) {
            buildQuery.append(" AND " + APPENDERFORUPLOADDATEGREATERTHANPOSTUNION + from);
        }
        if (to != null && to.length() > 0) {
            buildQuery.append(" AND " + APPENDERFORUPLOADDATEPOSTUNION + to);
        }

        buildQuery.append(ORDERBY);
    }

    /**
     * @param numOfDays
     * @throws ServiceException
     * @throws ClassNotFoundException
     * @throws SQLException
     * @author dthakkar DR#9 - New GUI for OCN Archive Rows Search
     */
    public List<OCNArchiveSearchResultVO> pcsSupportSearchOCNRows(String queryToRun)
            throws ServiceException, ClassNotFoundException, SQLException {

        Connection dbConnection = null;
        Statement stmt = null;
        ResultSet rsCount = null;
        List<OCNArchiveSearchResultVO> searchResults = new ArrayList<OCNArchiveSearchResultVO>();
//		Class.forName(environment.getProperty("spring.datasource.driver-class-name"));
       
        System.out.println("main Query--->  " + queryToRun.toString());
        try {
        	 dbConnection = dataSource.getConnection();
            // dbConnection=daoUtil.getDBConnection(PcsEJBConstant.PCS_JDBC_LOOKUP);
            stmt = dbConnection.createStatement();
            rsCount = stmt.executeQuery(queryToRun);

            while (rsCount.next()) {
                OCNArchiveSearchResultVO dto = new OCNArchiveSearchResultVO();
                dto.setCustNum(rsCount.getInt(1));
                dto.setAppId(rsCount.getString(2));
                dto.setPatNum(rsCount.getString(3));
                dto.setMailRoomDt(rsCount.getDate(4));
                dto.setDocId(rsCount.getString(6));
                dto.setDocDesc(rsCount.getString(7));
                dto.setAttyDktNo(rsCount.getString(9));
                dto.setUploadDt(rsCount.getDate(10));
                dto.setPostCardNotfctnIn(rsCount.getString(11));
                dto.setDocViewDt(rsCount.getDate(12));
                dto.setPatIssDt(rsCount.getDate(13));
                dto.setDocCd(rsCount.getString(14));
                dto.setDocViewBy(rsCount.getString(15));
                searchResults.add(dto);
            }

        } catch (SQLException sqlE) {
            throw new ServiceException(PcsEJBConstant.NO_REC_FOUND);
        } catch (Exception e) {
            throw new ServiceException(PcsEJBConstant.PCS_REQUEST_FAILED + " " + e.getMessage());
        } finally {
        	getDaoUtil().closeResultSet(rsCount); 
            getDaoUtil().closeStatement(stmt);
            getDaoUtil().closeConnection(dbConnection);
        }
        return searchResults;
    }

    /**
     * Updates message to database. Requires native jdbc connection object for
     * compatibility with oracle type ARRAY
     *
     * @param message String[] required for saving to oracle UDT varchar_varray
     * @return
     * @throws ServiceException
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    
    public int setSystemAnnouncement(String message, String msg) throws ServiceException, SQLException, ClassNotFoundException {
    	Connection conn = null;
        Connection nc = null;
        Statement st = null;
        int count=0;
        ResultSet rs = null;
        
//		Class.forName(environment.getProperty("spring.datasource.driver-class-name"));
        Connection dbConnection = dataSource.getConnection();
        Object array = new Object();
       
        try{
        	 if (msg != null && msg.equalsIgnoreCase("active")) {
            st = dbConnection.createStatement();
            String sql = "update PAIR_PROPERTY SET PROPERTY_VALUE_VARRAY = VARCHAR_VARRAY('"+message+"')" +
                    " WHERE property_nm='ImportantMessage' and property_mode_tx='PRIVATE' and property_key_tx='active'";
            
             count =  st.executeUpdate(sql);
           
        	 }else if (msg != null && msg.equalsIgnoreCase("default")) {
        		 st = dbConnection.createStatement();
                 String sql = "update PAIR_PROPERTY SET PROPERTY_VALUE_VARRAY = VARCHAR_VARRAY('"+message+"')" +
                		 " WHERE property_nm='DefaultMessage' and property_mode_tx='PRIVATE' and property_key_tx='Default'";
              
                  count =  st.executeUpdate(sql);
               
        	 }
         }catch(SQLException se){
            //Handle errors for JDBC
            se.printStackTrace();
         }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
         }finally{           
            getDaoUtil().closeResultSet(rs);
            getDaoUtil().closeStatement(st);
            getDaoUtil().closeConnection(dbConnection);
         }//end try
        return count;
    }
        
        
        
       

  /*  public int setSystemAnnouncement(String[] message, String msg) throws ServiceException, SQLException, ClassNotFoundException {
        PreparedStatement preparedStatement = null;
        Connection dbConnection;
        int rows = 0;
        Statement st = null;
        ResultSet rs = null;
        String k="hello";
//		  Class.forName(environment.getProperty("spring.datasource.driver-class-name"));
        dbConnection = dataSource.getConnection();
       
        //oracle.jdbc.driver.OracleConnection oc = (oracle.jdbc.driver.OracleConnection) dbConnection.getMetaData().getConnection(); 
        st = dbConnection.createStatement();	 

        try {
            if (msg != null && msg.equalsIgnoreCase("active")) {
                //Connection con = preparedStatement.getConnection();
            	 rs = st.executeQuery("update PAIR_PROPERTY SET PROPERTY_VALUE_VARRAY = VARCHAR_VARRAY(5767575)" +
                        " WHERE property_nm='ImportantMessage' and property_mode_tx='PRIVATE' and property_key_tx='active'");
               
           
               
              
               
            } else if (msg != null && msg.equalsIgnoreCase("default")) {
                preparedStatement = dbConnection.prepareStatement("update PAIR_PROPERTY SET PROPERTY_VALUE_VARRAY = (?)" +
                        " WHERE property_nm='DefaultMessage' and property_mode_tx='PRIVATE' and property_key_tx='Default'");
                //Array a = dbConnection.createArrayOf("PAIRUSRP.VARCHAR_VARRAY",message);
                ArrayDescriptor arrayDescriptor = ArrayDescriptor.createDescriptor("VARCHAR_VARRAY", preparedStatement.getConnection());
                //String[] Parameter = { "user1", "Administrator" };
                java.sql.Array sqlArray = new oracle.sql.ARRAY(arrayDescriptor, dbConnection, message);
                preparedStatement.setArray(1, sqlArray);
            }
            rows = null != rs ? rs.getRow() : 0;
        } catch (SQLException sqlE) {
            throw new ServiceException(PcsEJBConstant.DB_CONNECTION_ERROR);
        } catch (Exception e) {
            throw new ServiceException(PcsEJBConstant.PCS_REQUEST_FAILED + " " + e.getMessage());
        } finally {
            dbConnection.close();
			/*
			  	daoUtil.closeStatement(preparedStatement);
				daoUtil.closeConnection(dbConnection);

			
        }
        return rows;
    }*/

    public int validateUser(String userName) throws ServiceException, ClassNotFoundException, SQLException {
        Connection dbConnection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;
        String userQuery = null;
        userName = extractUserId(userName);
//		Class.forName(environment.getProperty("spring.datasource.driver-class-name"));
       
        userQuery = "select count(*) from PAIR_SUPPORT_USER where PAIR_USER = ?";
        if (userName != null && userName.trim().length() > 0) {
            try {
            	 dbConnection = dataSource.getConnection();
                // dbConnection = daoUtil.getDBConnection(PcsEJBConstant.PCS_JDBC_LOOKUP);
                pstmt = dbConnection.prepareStatement(userQuery);
                pstmt.setString(1, userName);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    count = rs.getInt(1);
                }
            } catch (SQLException sqlE) {
                throw new ServiceException(PcsEJBConstant.DB_CONNECTION_ERROR);
            } catch (Exception e) {
                throw new ServiceException(PcsEJBConstant.PCS_REQUEST_FAILED + " " + e.getMessage());
            } finally {                              
            	getDaoUtil().closeResultSet(rs);
                getDaoUtil().closeStatement(pstmt);
                getDaoUtil().closeConnection(dbConnection);
                userQuery = null;
            }
        }
        return count;
    }

    // Added by sramagiri to implement eNotification page functionality

    /**
     * @param userName
     * @return
     */
    private String extractUserId(String userName) {
        if (userName != null && userName.length() > 0) {
            String tmpString = userName.replace('\\', '*');
            String[] userId = tmpString.split("\\*");
            userName = userId.length > 1 ? userId[1] : userId[0];
        }
        return userName;
    }
}
