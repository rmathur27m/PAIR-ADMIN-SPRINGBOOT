package gov.uspto.patent.privatePair.PCSEJBApp.dao;

import gov.uspto.patent.privatePair.PCSEJBApp.dto.CustOutGoingRow;
import gov.uspto.patent.privatePair.admin.dao.DaoUtil;
import gov.uspto.patent.privatePair.utils.PcsEJBConstant;
import gov.uspto.patent.privatePair.utils.ServiceException;
import gov.uspto.patent.privatePair.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

import static gov.uspto.patent.privatePair.admin.dao.DaoUtil.daoUtil;
import static gov.uspto.patent.privatePair.admin.dao.DaoUtil.getDaoUtil;

@Component
@Configuration
@PropertySource("classpath:application.properties")
public class OutGoingCorrDAO {

    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    /* The method "getPartialOutGoingCorrByCN" is used to retrieve only 100 outgoing correspondence
     * and related data from the PAIR_OUTGOING_CORR table and the rest is returned as range string for each 100 records.
     */
    public HashMap getDisplayOutGoingCorrByCN(String custNum, int pastDays, String searchType, String sortBy, String sortOrder, int iDispRowIndex) throws ServiceException, ClassNotFoundException, SQLException {

        HashMap resultHashMap = new HashMap();
        ArrayList displayRowList = new ArrayList();
        ArrayList rangeRowList = new ArrayList();
        String sqlQueryStr = "", sqlCountQuery = "";
        Statement stmt = null, stmtCount = null;
        ResultSet rs = null, rsCount = null;
        int recordCount = 0;
        Connection dbConnection = dataSource.getConnection();
        List cnList = StringUtil.getTokenizedList(custNum, ",");

        //Initial default sortBy and sortOrder
        if (sortBy.trim().equals(""))
            sortBy = "UPLOAD_DT";
        if (sortOrder.trim().equals(""))
            sortOrder = "DESC";

        //Formation for count query and actual query.
        String strWhereClause = outGoingCorrWhereClause(cnList, pastDays, searchType);
        sqlCountQuery = outGoingCorrCountQuery(strWhereClause);
        sqlQueryStr = outGoingCorrQuery(strWhereClause, sortBy, sortOrder);
        System.out.println(" MM 100 Outgoing Correspondence Query \n" + sqlQueryStr);
        try {

            int displayStartRow = (iDispRowIndex * 100) + 1;
            int displayEndRow = displayStartRow + 99;


            Calendar before = Calendar.getInstance();
            recordCount = jdbcTemplate.queryForObject(sqlCountQuery, Integer.class);
            Calendar after = Calendar.getInstance();
            System.out.println("  OutgoingCorr display count query time taken : " + StringUtil.diffDates(before, after));


            if (recordCount <= 0) {
                resultHashMap.put("RECORD_COUNT", new Integer(recordCount));
                return resultHashMap; // return empty List
            }
            System.out.println("OutgoingCorrespondence display count for customers " + custNum + " is: " + recordCount);

            stmt = dbConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            before = Calendar.getInstance();
            rs = stmt.executeQuery(sqlQueryStr);
            after = Calendar.getInstance();
            System.out.println("  OutgoingCorr display query time taken : " + StringUtil.diffDates(before, after));

            if (null!=rs) {
            	 CustOutGoingRow outGoingCorrByCustNum =  new CustOutGoingRow();
                //while (rs.next()) {
            	 //rs.first();
                for (int j = displayStartRow; j <= displayEndRow && !(rs.isLast()); j++) {
                    rs.absolute(j);
                    //System.out.println("Dispaly row num:-->"+rs.getRow());
                    //changed for DR # 33
                    //added by MM DR 24 5/10/2012
                    
                    outGoingCorrByCustNum = new CustOutGoingRow(rs.getString(1).trim(), rs.getString(2), rs.getString(3),
                            rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
                            rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14), rs.getTimestamp(15), rs.getTimestamp(16),
                            rs.getTimestamp(17), rs.getString(18), rs.getString(19), rs.getString(20), rs.getString(21), rs.getString(22),
                            rs.getString(23), rs.getString(24), rs.getString(25));

                    displayRowList.add(outGoingCorrByCustNum);
                }
                //rs.first();
                for (int k = 1; !(rs.isAfterLast()); k = k + 100) {
                //while(rs.next()){
                    HashMap hm = new HashMap();
                    rs.absolute(k);
                    hm.put("LOWER_LIMIT", getOutGoingDispRefText(rs, sortBy));

                    rs.absolute(k + 99);

                    if (rs.isLast() || rs.isAfterLast()) {
                        rs.last();
                        hm.put("UPPER_LIMIT", getOutGoingDispRefText(rs, sortBy));
                        rs.next();
                    } else {
                        hm.put("UPPER_LIMIT", getOutGoingDispRefText(rs, sortBy));
                    }

                    rangeRowList.add(hm);
                }
            }

            resultHashMap.put("RECORD_COUNT", new Integer(recordCount));
            resultHashMap.put("DISPLAY_LIST", displayRowList);
            resultHashMap.put("RANGE_LIST", rangeRowList);

        } catch (SQLException sqlE) {

            System.out.println("ERROR: OutGoingCorrDAO.getDisplayOutGoingCorrByCN() :" + sqlE.getMessage());
            if (sqlE.getErrorCode() == 1017 || sqlE.getErrorCode() == 03114)
                throw new ServiceException(PcsEJBConstant.DB_CONNECTION_ERROR);
        } catch (Exception e) {
            System.out.println("ERROR: OutGoingCorrDAO.getDisplayOutGoingCorrByCN() :" + e.getMessage());
        } finally {

            getDaoUtil().closeResultSet(rsCount);
            getDaoUtil().closeResultSet(rs);
            getDaoUtil().closeStatement(stmt);
            getDaoUtil().closeConnection(dbConnection);

        }

        return resultHashMap;

    }// getPartialAppListByCN()

    private String outGoingCorrWhereClause(List cnList, int days, String searchType) {
        Calendar now = Calendar.getInstance();

        //DR#28 - changed > to >= for upload date condition
        StringBuffer whereClauseBuf = new StringBuffer(" WHERE ifw.FK_PC_ID=pc.FK_PC_ID AND ifw.UPLOAD_DT >= TO_DATE('").append(formatter.format(now.getTime())).append("','MM-DD-YYYY') - ");

        whereClauseBuf.append(days + 1);

        //whereClauseBuf.append(" AND ifw.MAIL_ROOM_DT <=  TO_DATE('").append(formatter.format(now.getTime())).append("','MM-DD-YYYY::HH24:MI:SS')");
        whereClauseBuf.append(" AND ifw.MAIL_ROOM_DT <=  TO_DATE('").append(formatter.format(now.getTime())).append("','MM-DD-YYYY')");

        /*if(status!=null &&(status.equalsIgnoreCase("ABANDONED") || status.equalsIgnoreCase("ISSUED") ||
                  status.equalsIgnoreCase("NEW") || status.equalsIgnoreCase("PENDING")))
          {
              whereClauseBuf.append(" AND pc.STATUS_GRP_CD='");
              whereClauseBuf.append(status).append("'");
          }
          */
        if (searchType != null && searchType.equalsIgnoreCase("UNVIEWED")) {
            whereClauseBuf.append(" AND ifw.DOC_VIEW_DT is null");
        } //else all the records.

        whereClauseBuf.append(" AND ifw.CUSTOMER_NO in (");

        Iterator itCns = cnList.iterator();
        while (itCns.hasNext()) {
            whereClauseBuf.append("'" + itCns.next().toString() + "',");
        }

        //		Remove the last "," and return the string;
        return whereClauseBuf.substring(0, whereClauseBuf.length() - 1) + ")";
    }

    private String outGoingCorrCountQuery(String strWhereClause) {
        StringBuffer outGoingCorrCountQueryBuf = new StringBuffer("SELECT count(1) from PAIR_IFW_OUTGOING ifw, PAIR_CUSTOMERS pc ");
        outGoingCorrCountQueryBuf.append(strWhereClause);

        return outGoingCorrCountQueryBuf.toString();
    }

    /*
     * outGoingCorrQuery is used to retrieve outgoingcorrespondence(OfficeActions) and related
     * data from the palm database.  The data is queried from PAIR_OUTGOING_CORR and PAIR_CUSTOMERS table.
     *
     */
    private String outGoingCorrQuery(String strWhereClause, String sortBy, String sortOrder) {
        Calendar now = Calendar.getInstance();

        //Replaced the sysdate with formated date.
        StringBuffer outGoingCorrListByCnSqlBuf = new StringBuffer("SELECT ifw.APPL_ID, ");
        outGoingCorrListByCnSqlBuf.append(" DECODE((ifw.PATENT_ISSUE_DT-TO_DATE('").append(formatter.format(now.getTime())).append("','MM-DD-YYYY'))-ABS(ifw.PATENT_ISSUE_DT-TO_DATE('").append(formatter.format(now.getTime())).append("','MM-DD-YYYY')),0,'-',NVL(TRIM(ifw.PATENT_NO),'-'))PATENT_NO, ");
        outGoingCorrListByCnSqlBuf.append(" DECODE(TO_CHAR(ifw.MAIL_ROOM_DT,'YYYYMMDD'),'00010101','-',NULL,'-', TO_CHAR(ifw.MAIL_ROOM_DT,'YYYYMMDD')), ");
        outGoingCorrListByCnSqlBuf.append(" NVL(ifw.FILE_WRAPPER_PACKAGE_ID,'-'), ");
        outGoingCorrListByCnSqlBuf.append(" NVL(TRIM(ifw.DOC_DESCRIPTION_TX),'-'), ");
        outGoingCorrListByCnSqlBuf.append(" NVL(ifw.START_PAGE_NO,0), ");
        outGoingCorrListByCnSqlBuf.append(" NVL(ifw.PAGE_COUNT,0),NVL(OFFSET,0), ");
        outGoingCorrListByCnSqlBuf.append(" NVL(TRIM(ifw.ATTY_DKT_NO),'-'), ");
        outGoingCorrListByCnSqlBuf.append(" DECODE(TO_CHAR(ifw.UPLOAD_DT,'YYYYMMDD'),'00010101','-',NULL,'-', TO_CHAR(ifw.UPLOAD_DT,'YYYYMMDD')), ");
        outGoingCorrListByCnSqlBuf.append(" DECODE(TO_CHAR(ifw.DOC_VIEW_DT,'YYYYMMDD'),'00010101','-',NULL,'-', TO_CHAR(ifw.DOC_VIEW_DT,'YYYYMMDD')), ");
        outGoingCorrListByCnSqlBuf.append(" ifw.CUSTOMER_NO, ifw.DOC_CATEGORY, ifw.DOCUMENT_CD, ifw.LAST_MODIFIED_TS, pc.FILE_DT, ");
        outGoingCorrListByCnSqlBuf.append(" pc.PUB_ACTL_PUB_DT, pc.PUB_NO_KIND_CD, pc.PUB_NO_SEQ_NO, pc.PUB_NO_YR, pc.STATUS_GRP_CD,ifw.DOC_ID, ");
        //Added by dthakkar for DR # 33 - Add Docketed Check Box on OCN Tab
        outGoingCorrListByCnSqlBuf.append(" ifw.DOCKETED, ");
        outGoingCorrListByCnSqlBuf.append(" ifw.DA_ID, ");
        outGoingCorrListByCnSqlBuf.append(" NVL(ifw.DOC_VIEWER_NM,'-') "); //added by MM DR 24 5/10/2012   *** To track this column search for docViewBy   ****
        outGoingCorrListByCnSqlBuf.append(" FROM PAIR_IFW_OUTGOING ifw, PAIR_CUSTOMERS pc ");
        outGoingCorrListByCnSqlBuf.append(strWhereClause);
        outGoingCorrListByCnSqlBuf.append(" order by ");
        outGoingCorrListByCnSqlBuf.append(outGoingCorrOrderString(sortBy, sortOrder));
        outGoingCorrListByCnSqlBuf.append(" ,ifw.FILE_WRAPPER_PACKAGE_ID DESC,ifw.START_PAGE_NO ASC ");
        return outGoingCorrListByCnSqlBuf.toString();

    }

    private String getOutGoingDispRefText(ResultSet rs, String sortBy)
            throws SQLException {
        StringUtil strUtil = new StringUtil();

        //System.out.println("Displayed data Row num: "+rs.getRow());
        switch (sortBy) {
            case "APPL_ID":
                return strUtil.getFormattedApplicationNo(rs.getString(1));
            case "PATENT_NO":
                return strUtil.formatInputPatentNumber(rs.getString(2)) + "(" + strUtil.getFormattedApplicationNo(rs.getString(1)) + ")";
            case "ATTY_DKT_NO":
                return rs.getString(9) + "(" + strUtil.getFormattedApplicationNo(rs.getString(1)) + ")";
            case "CUSTOMER_NO":
                return (rs.getString(12) + "(" + strUtil.getFormattedApplicationNo(rs.getString(1)) + ")");
            case "MAIL_ROOM_DT":
                return strUtil.dateDisplayFormat(rs.getString(3)) + "(" + strUtil.getFormattedApplicationNo(rs.getString(1)) + ")";
            case "DOC_VIEW_DT":
                return strUtil.dateDisplayFormat(rs.getString(11)) + "(" + strUtil.getFormattedApplicationNo(rs.getString(1)) + ")";
            case "DOCUMENT_CD":
                return (((rs.getString(14) == null) ? "" : rs.getString(14)) + "(" + strUtil.getFormattedApplicationNo(rs.getString(1)) + ")");
            case "UPLOAD_DT":
                return strUtil.dateDisplayFormat(rs.getString(10)) + "(" + strUtil.getFormattedApplicationNo(rs.getString(1)) + ")";
            default:
                //return strUtil.getFormattedApplicationNo(rs.getString(1));
                return strUtil.dateDisplayFormat(rs.getString(10)) + "(" + strUtil.getFormattedApplicationNo(rs.getString(1)) + ")";
        }

    }

    private String outGoingCorrOrderString(String sortBy, String sortOrder) {
        String orderByString;


        switch (sortBy) {
            case "APPL_ID":
                if (sortOrder.equals("ASC"))
                    orderByString = "1 ASC";
                else
                    orderByString = "1 DESC";
                break;
            case "PATENT_NO":
                if (sortOrder.equals("ASC"))
                    orderByString = "2 ASC, 1 DESC";
                else
                    orderByString = "2 DESC, 1 DESC";
                break;
            case "MAIL_ROOM_DT":
                if (sortOrder.equals("ASC"))
                    orderByString = "3 ASC, 1 DESC";
                else
                    orderByString = "3 DESC, 1 DESC";
                break;
            case "ATTY_DKT_NO":
                if (sortOrder.equals("ASC"))
                    orderByString = "9 ASC, 1 DESC";
                else
                    orderByString = "9 DESC, 1 DESC";
                break;
            case "DOC_VIEW_DT":
                if (sortOrder.equals("ASC"))
                    orderByString = "11 ASC, 1 DESC";
                else
                    orderByString = "11 DESC, 1 DESC";
                break;
            case "CUSTOMER_NO":
                if (sortOrder.equals("ASC"))
                    orderByString = "12 ASC, 1 DESC";
                else
                    orderByString = "12 DESC, 1 DESC";
                break;
            case "DOCUMENT_CD":
                if (sortOrder.equals("ASC"))
                    orderByString = "14 ASC, 1 DESC";
                else
                    orderByString = "14 DESC, 1 DESC";
                break;
            case "UPLOAD_DT":
                if (sortOrder.equals("ASC"))
                    orderByString = "10 ASC, 1 DESC";
                else
                    orderByString = "10 DESC, 1 DESC";
                break;
            default:
                orderByString = "10 DESC, 1 DESC";
                break;
        }

        return orderByString;
    }

    /* The method "getDownloadOutGoingList" is used to retrieve all the  outgoing correspondence
     * related data from the PAIR_OUTGOING_CORR table.
     */
    public HashMap getDownloadOutGoingList(String downloadBy, String custNum, String searchType, int pastDays, String multipleAppId, String sortBy, String sortOrder) throws ServiceException, ClassNotFoundException, SQLException {

        HashMap resultHashMap = new HashMap();
        ArrayList resultList = new ArrayList();
        String status = "", statusMsg = "";
        String sqlQueryStr = "", sqlCountQuery = "";
        Statement stmt = null, stmtCount = null;
        ResultSet rs = null, rsCount = null;
        int recordCount = 0;
        //Connection dbConnection = null;
//        Class.forName(environment.getProperty("spring.datasource.driver-class-name"));
        Connection dbConnection = dataSource.getConnection();
        //List cnList = StringUtil.getTokenizedList(custNum, ",");

        //Initial default sortBy and sortOrder
        if (sortBy.trim().equals(""))
            sortBy = "UPLOAD_DT";
        if (sortOrder.trim().equals(""))
            sortOrder = "DESC";

        //Formation for count query and actual query.
        String strWhereClause = outGoingCorrDownloadWhereClause(downloadBy, custNum, searchType, pastDays, multipleAppId);
        System.out.println("strWhereClause SQL is -> " + strWhereClause);
        sqlCountQuery = outGoingCorrCountQuery(strWhereClause);
        //Query for Outgoing correspondence display and download is the same.
        sqlQueryStr = outGoingCorrDownloadQuery(strWhereClause, sortBy, sortOrder);
        System.out.println("sqlCountQuery SQL is -> " + sqlCountQuery);
        System.out.println("MM All SQM is -> " + sqlQueryStr);
        try {
            //dbConnection = daoUtil.getDBConnection(PcsEJBConstant.PCS_JDBC_LOOKUP);
            stmtCount = dbConnection.createStatement();
            rsCount = stmtCount.executeQuery(sqlCountQuery);
            while (rsCount.next()) {
                recordCount = rsCount.getInt(1);
            }
            System.out.println("Outgoing correspondence download count for customers " + custNum + " is: " + recordCount);

            if (recordCount <= 0) {
                resultHashMap.put(PcsEJBConstant.STATUS, PcsEJBConstant.SUCCESS);
                resultHashMap.put(PcsEJBConstant.STATUS_MESSAGE, PcsEJBConstant.NO_REC_FOUND);
                resultHashMap.put(PcsEJBConstant.RECORD_COUNT, new Integer(recordCount));
                resultHashMap.put(PcsEJBConstant.RESULT_LIST, resultList);

                return resultHashMap; // return empty vector
            }


            stmt = dbConnection.createStatement();

            Calendar before = Calendar.getInstance();
            rs = stmt.executeQuery(sqlQueryStr);
            Calendar after = Calendar.getInstance();
            System.out.println("OutgoingCorr download query time taken : " + StringUtil.diffDates(before, after));

            CustOutGoingRow outGoingCorrByCustNum = null;
            while (rs.next()) {
                //changed for DR # 33
                //added by MM DR 24 5/10/2012
                outGoingCorrByCustNum = new CustOutGoingRow(rs.getString(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
                        rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14), rs.getTimestamp(15), rs.getTimestamp(16),
                        rs.getTimestamp(17), rs.getString(18), rs.getString(19), rs.getString(20), rs.getString(21), rs.getString(22),
                        rs.getString(23), rs.getString(24), rs.getString(25));
                resultList.add(outGoingCorrByCustNum);
            }
            status = PcsEJBConstant.SUCCESS;
            statusMsg = PcsEJBConstant.SUCCESS_RETRIEVE;
        } catch (SQLException sqlE) {
            status = PcsEJBConstant.ERROR;
            statusMsg = PcsEJBConstant.PCS_DB_ERROR;

            System.out.println("ERROR: OutGoingCorrDAO.getDownloadOutGoingList() :" + sqlE.getMessage());
            if (sqlE.getErrorCode() == 1017 || sqlE.getErrorCode() == 03114)
                throw new ServiceException(PcsEJBConstant.DB_CONNECTION_ERROR);
        } catch (Exception e) {
            status = PcsEJBConstant.ERROR;
            statusMsg = PcsEJBConstant.PCS_REQUEST_FAILED;
            System.out.println("ERROR: OutGoingCorrDAO.getDownloadOutGoingList() :" + e.getMessage());
        } finally {

            getDaoUtil().closeResultSet(rsCount);
            getDaoUtil().closeResultSet(rs);
            getDaoUtil().closeStatement(stmtCount);
            getDaoUtil().closeStatement(stmt);
            getDaoUtil().closeConnection(dbConnection);
        }

        resultHashMap.put(PcsEJBConstant.STATUS, status);
        resultHashMap.put(PcsEJBConstant.STATUS_MESSAGE, statusMsg);
        resultHashMap.put(PcsEJBConstant.RECORD_COUNT, new Integer(recordCount));
        resultHashMap.put(PcsEJBConstant.RESULT_LIST, resultList);

        return resultHashMap;

    }// getDownloadOutGoingList()

    private String outGoingCorrDownloadWhereClause(String downloadBy, String custNum, String searchType, int statusDays, String multipleAppId) {
        Calendar now = Calendar.getInstance();

        //StringBuffer whereClauseBuf = new StringBuffer(" WHERE UPLOAD_DT > SYSDATE - ");
        //DR#28 - changed > to >= for upload date condition
        StringBuffer whereClauseBuf = new StringBuffer(" WHERE ifw.FK_PC_ID=pc.FK_PC_ID AND ifw.UPLOAD_DT >= TO_DATE('").append(formatter.format(now.getTime())).append("','MM-DD-YYYY') - ");
        whereClauseBuf.append(statusDays + 1);
        //whereClauseBuf.append(" AND MAIL_ROOM_DT <=  TO_DATE((TO_CHAR(SYSDATE,'MM-DD-YYYY')||'::00:00'),'MM-DD-YYYY::HH24:MI:SS') ");
        whereClauseBuf.append(" AND ifw.MAIL_ROOM_DT <=  TO_DATE('").append(formatter.format(now.getTime())).append("','MM-DD-YYYY')");
        if (null!=downloadBy?downloadBy.equalsIgnoreCase("CN_STATUS_DT"):false) {

            //Multiple Customers, past status days and search type might be all or unviewed.
            List cnList = StringUtil.getTokenizedList(custNum, ",");
            Iterator itCns = cnList.iterator();
            StringBuffer tempCnBuff = new StringBuffer(" AND ifw.CUSTOMER_NO in (");
            while (itCns.hasNext()) {
                tempCnBuff.append("'" + itCns.next().toString() + "',");
            }

            //Remove the last "," and return the string;
            whereClauseBuf.append(tempCnBuff.substring(0, tempCnBuff.length() - 1)).append(")");

        } else {

//			Multiple Application Id, Multiple Customer Numbers and search type might be All or Unviewed.
            List cnList = StringUtil.getTokenizedList(custNum, ",");
            Iterator itCns = cnList.iterator();
            StringBuffer tempCnBuff = new StringBuffer(" AND ifw.CUSTOMER_NO in (");
            while (itCns.hasNext()) {
                tempCnBuff.append("'" + itCns.next().toString() + "',");
            }

            //Remove the last "," and return the string;
            whereClauseBuf.append(tempCnBuff.substring(0, tempCnBuff.length() - 1)).append(")");

            List appList = StringUtil.getTokenizedList(multipleAppId, ",");
            Iterator itApps = appList.iterator();
            StringBuffer tempAppBuff = new StringBuffer(" AND ifw.APPL_ID in (");
            while (itApps.hasNext()) {
                tempAppBuff.append("'" + itApps.next().toString() + "',");
            }

            //Remove the last "," and return the string;
            whereClauseBuf.append(tempAppBuff.substring(0, tempAppBuff.length() - 1)).append(")");
        }

        //Status common to 2 conditions.
        /*if(status!=null &&(status.equalsIgnoreCase("ABANDONED") || status.equalsIgnoreCase("ISSUED") ||
                  status.equalsIgnoreCase("NEW") || status.equalsIgnoreCase("PENDING")))
          {
              whereClauseBuf.append(" AND pc.STATUS_GRP_CD='");
              whereClauseBuf.append(status).append("'");
          }*/
        if (searchType != null && searchType.equalsIgnoreCase("UNVIEWED")) {
            whereClauseBuf.append(" AND ifw.DOC_VIEW_DT is null");
        } //else all the records.

        return whereClauseBuf.toString();
    }

    /*
     * outGoingCorrDownloadQuery is used to retrieve outgoingcorrespondence(OfficeActions) and related
     * data from the PAIR database to OutGoingCorrespondence dowload xml.
     *  The data is queried from PAIR_OUTGOING_CORR and PAIR_CUSTOMERS table.
     * This method is sameas the outGoingCorrQuery() except the OrderString.
     *
     */
    private String outGoingCorrDownloadQuery(String strWhereClause, String sortBy, String sortOrder) {
        Calendar now = Calendar.getInstance();

        //Replaced the sysdate with formated date.
        StringBuffer outGoingCorrListByCnSqlBuf = new StringBuffer("SELECT ifw.APPL_ID, ");
        outGoingCorrListByCnSqlBuf.append(" DECODE((ifw.PATENT_ISSUE_DT-TO_DATE('").append(formatter.format(now.getTime())).append("','MM-DD-YYYY'))-ABS(ifw.PATENT_ISSUE_DT-TO_DATE('").append(formatter.format(now.getTime())).append("','MM-DD-YYYY')),0,'',DECODE(trim(ifw.PATENT_NO),'-','',trim(ifw.PATENT_NO)))PATENT_NO, ");
        outGoingCorrListByCnSqlBuf.append(" DECODE(TO_CHAR(ifw.MAIL_ROOM_DT,'YYYYMMDD'),'00010101','',NULL,'', TO_CHAR(ifw.MAIL_ROOM_DT,'YYYYMMDD')), ");
        outGoingCorrListByCnSqlBuf.append(" NVL(ifw.FILE_WRAPPER_PACKAGE_ID,'-'), ");
        outGoingCorrListByCnSqlBuf.append(" TRIM(ifw.DOC_DESCRIPTION_TX), ");
        outGoingCorrListByCnSqlBuf.append(" NVL(ifw.START_PAGE_NO,0), ");
        outGoingCorrListByCnSqlBuf.append(" NVL(ifw.PAGE_COUNT,0),NVL(OFFSET,0), ");
        outGoingCorrListByCnSqlBuf.append(" TRIM(ifw.ATTY_DKT_NO), ");
        outGoingCorrListByCnSqlBuf.append(" DECODE(TO_CHAR(ifw.UPLOAD_DT,'YYYYMMDD'),'00010101','',NULL,'', TO_CHAR(ifw.UPLOAD_DT,'YYYYMMDD')), ");
        outGoingCorrListByCnSqlBuf.append(" DECODE(TO_CHAR(ifw.DOC_VIEW_DT,'YYYYMMDD'),'00010101','',NULL,'', TO_CHAR(ifw.DOC_VIEW_DT,'YYYYMMDD')), ");
        outGoingCorrListByCnSqlBuf.append(" ifw.CUSTOMER_NO, ifw.DOC_CATEGORY, ifw.DOCUMENT_CD, ifw.LAST_MODIFIED_TS, pc.FILE_DT, ");
        outGoingCorrListByCnSqlBuf.append(" pc.PUB_ACTL_PUB_DT, pc.PUB_NO_KIND_CD, pc.PUB_NO_SEQ_NO, pc.PUB_NO_YR, pc.STATUS_GRP_CD,ifw.DOC_ID, ifw.DOCKETED, ifw.DA_ID, ");
        outGoingCorrListByCnSqlBuf.append(" NVL(ifw.DOC_VIEWER_NM,'-') "); //added by MM DR 24 5/10/2012    *** To track this column search for docViewBy   ****
        outGoingCorrListByCnSqlBuf.append(" FROM PAIR_IFW_OUTGOING ifw, PAIR_CUSTOMERS pc ");
        outGoingCorrListByCnSqlBuf.append(strWhereClause);
        outGoingCorrListByCnSqlBuf.append(" order by ");
        outGoingCorrListByCnSqlBuf.append(outGoingCorrDowloadOrderString(sortBy, sortOrder));
        outGoingCorrListByCnSqlBuf.append(" ,ifw.FILE_WRAPPER_PACKAGE_ID DESC,ifw.START_PAGE_NO ASC ");

        return outGoingCorrListByCnSqlBuf.toString();
    }

    private String outGoingCorrDowloadOrderString(String sortBy, String sortOrder) {
        String orderByString;


        switch (sortBy) {
            case "APPL_ID":
                if (sortOrder.equals("ASC"))
                    orderByString = "1 ASC";
                else
                    orderByString = "1 DESC";
                break;
            case "PATENT_NO":
                if (sortOrder.equals("ASC"))
                    orderByString = "2 ASC, 1 DESC";
                else
                    orderByString = "2 DESC, 1 DESC";
                break;
            case "MAIL_ROOM_DT":
                /*As MAIL_ROOM_DT is part of the documentlist(child) tag, the default sorting of the Application(parent)
                 * tag is always by Application Number in descending order. */
                if (sortOrder.equals("ASC"))
                    orderByString = "1 DESC, 3 ASC";
                else
                    orderByString = "1 DESC, 3 DESC";
                break;
            case "ATTY_DKT_NO":
                if (sortOrder.equals("ASC"))
                    orderByString = "9 ASC, 1 DESC";
                else
                    orderByString = "9 DESC, 1 DESC";
                break;
            case "DOC_VIEW_DT":
                /*As DOC_VIEW_DT is part of the documentlist(child) tag, the default sorting of the Application(parent)
                 * tag is always by Application Number in descending order. */
                if (sortOrder.equals("ASC"))
                    orderByString = "1 DESC, 11 ASC";
                else
                    orderByString = "1 DESC, 11 DESC";
                break;
            case "CUSTOMER_NO":
                if (sortOrder.equals("ASC"))
                    orderByString = "12 ASC, 1 DESC";
                else
                    orderByString = "12 DESC, 1 DESC";
                break;
            case "DOCUMENT_CD":
                /*As DOCUMENT_CD is part of the documentlist(child) tag, the default sorting of the Application(parent)
                 * tag is always by Application Number in descending order. */
                if (sortOrder.equals("ASC"))
                    orderByString = "1 DESC, 14 ASC";
                else
                    orderByString = "1 DESC, 14 DESC";
                break;
            case "UPLOAD_DT":
                /*As UPLOAD_DT is part of the documentlist(child) tag, the default sorting of the Application(parent)
                 * tag is always by Application Number in descending order. */
                if (sortOrder.equals("ASC"))
                    orderByString = "1 DESC, 10 ASC";
                else
                    orderByString = "1 DESC, 10 DESC";
                break;
            default:
                orderByString = "1 DESC, 10 DESC";
                break;
        }

        return orderByString;
    }

    public void setDataSource() {
        jdbcTemplate = new JdbcTemplate(dataSource);
       /* namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("SECURE_ORDER_VALIDATION");*/

    }

    /**
     * @param docketed
     * @param daId
     * @throws ServiceException
     * @author dthakkar DR # 33 - Add docketed check box on OCN Tab
     */
    public String updateOCNDocketedAction(String docketed, String daId) throws ServiceException {
        System.out.println("environmental variable--->" + docketed + "---daId " + daId);
        StringBuilder docketedQuery = new StringBuilder("UPDATE PAIR_IFW_OUTGOING IFW ")
            .append("SET IFW.DOCKETED = ? ")
            .append("WHERE IFW.DA_ID IN ( ")
            .append(daId)
            .append(")");

        StringBuilder docketedQueryEoa =
            new StringBuilder("UPDATE STG_PAIR_IFW_OUTGOING_EOA IFW ")
                .append("SET IFW.DOCKETED = ? ")
                .append("WHERE IFW.DA_ID IN ( ")
                .append(daId)
                .append(")");
        Connection dbConnection = null;
        try {
            dbConnection = dataSource.getConnection();
            if (DaoUtil.validateSqlInParams(daId)) {
                jdbcTemplate.update(docketedQuery.toString(), docketed);
                jdbcTemplate.update(docketedQueryEoa.toString(), docketed);

            } else {
                throw new ServiceException(PcsEJBConstant.PCS_REQUEST_FAILED);
            }
        } catch (Exception e) {
            throw new ServiceException(PcsEJBConstant.PCS_REQUEST_FAILED + " " + e.getMessage());
        } finally {
            getDaoUtil().closeConnection(dbConnection);
        }
        return "success";

    }
}
