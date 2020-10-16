package gov.uspto.patent.privatePair.PCSEJBApp.dao;

import gov.uspto.patent.privatePair.PCSEJBApp.dto.EOfficeActionRow;
import gov.uspto.patent.privatePair.utils.PcsEJBConstant;
import gov.uspto.patent.privatePair.utils.ServiceException;
import gov.uspto.patent.privatePair.utils.StringUtil;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

import static gov.uspto.patent.privatePair.admin.dao.DaoUtil.getDaoUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class EOfficeActionDAO<T> {

    private final String APPL_ID_COLUMN = "APPL_ID";
    private final String ATTY_DKT_NO_COLUMN = "ATTY_DKT_NO";
    private final String CUSTOMER_NO_COLUMN = "CUSTOMER_NO";
    private final String DOCUMENT_CD_COLUMN = "DOCUMENT_CD";
    private final String DOC_VIEW_DT_COLUMN = "DOC_VIEW_DT";
    private final String E_NOTIFICATION_DATE_COLUMN = "E_NOTIFICATION_DT";
    private final String MAILROOM_DT_COLUMN = "MAILROOM_DT";
    private final String PATENT_NO_COLUMN = "PATENT_NO";
    @Autowired
    DataSource dataSource;
    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * Queries database for eOffice Action notifications by application id
     *
     * @param appId
     * @param //startDate
     * @param //endDate
     * @param sortBy
     * @param sortOrder
     * @param dispRowIndex
     * @return HashMap of eOffice Action Notification details
     * @throws ServiceException
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public HashMap getDisplayEOfficeActionByAppId(String appId, String sortBy, String sortOrder, int dispRowIndex,
                                                  String allOptedInCustNumbers) throws ServiceException, ClassNotFoundException, SQLException {

        String whereClause = eOfficeActionByAppIdWhereClause(appId, allOptedInCustNumbers);
        return getDisplayEOfficeAction(whereClause, sortBy, sortOrder, dispRowIndex);
    }
 
    private String eOfficeActionByAppIdWhereClause(String appId, String allOptedInCustNumbers) {

        StringBuffer whereClauseBuf = new StringBuffer(" WHERE ifw.DOC_ID = e.DN_IFW_DOC_ID AND ifw.FK_PC_ID=pc.FK_PC_ID");

        whereClauseBuf.append(" AND ((e.PRCSG_RETURN_CD = '1') OR (e.PRCSG_RETURN_CD = '0'AND e.prcsg_reason_cd = '3')) AND e.PRCSG_STAT_CT IN ('PROCESSED','IGNORED')");
        whereClauseBuf.append(" AND pc.APPL_ID = '").append(appId).append("'");
        whereClauseBuf.append(" AND pc.APPL_ID NOT LIKE 'PCT%'");

        List cnList = StringUtil.getTokenizedList(allOptedInCustNumbers, ",");
        Iterator itCns = cnList.iterator();
        StringBuffer tempCnBuff = new StringBuffer(" AND pc.CUSTOMER_NO IN (");
        while (itCns.hasNext()) {
            tempCnBuff.append("'" + itCns.next().toString() + "',");
        }

        //Remove the last "," and return the string;
        whereClauseBuf.append(tempCnBuff.substring(0, tempCnBuff.length() - 1)).append(")");

        return whereClauseBuf.toString();
    }

    @SuppressWarnings("unchecked")
    private HashMap getDisplayEOfficeAction(String whereClause, String sortBy, String sortOrder, int dispRowIndex) throws ServiceException, SQLException, ClassNotFoundException {
        List<EOfficeActionRow> listEOfficeActionRows = new ArrayList<EOfficeActionRow>();
        HashMap resultHashMap = new HashMap();
        ArrayList displayRowList = new ArrayList();
        ArrayList rangeRowList = new ArrayList();
        String sqlQueryStr = "", sqlCountQuery = "";
        Statement stmt = null, stmtCount = null;
        ResultSet rs = null, rsCount = null;
        int recordCount = 0;
        Connection conn = dataSource.getConnection();


        //Initial default sortBy and sortOrder
        if (sortBy.trim().equals(""))
            sortBy = E_NOTIFICATION_DATE_COLUMN;
        if (sortOrder.trim().equals(""))
            sortOrder = "DESC";

        sqlCountQuery = eOfficeActionCountQuery(whereClause);
        sqlQueryStr = eOfficeActionQuery(whereClause, sortBy, sortOrder);
        System.out.println("Query for recordCount--> "+sqlCountQuery.toString());

        try {
            int displayStartRow = (dispRowIndex * 100) + 1;
            int displayEndRow = displayStartRow + 99;

          
            try(Statement stmtCount1 = conn.createStatement();              
            		ResultSet rsCount1 = stmtCount1.executeQuery(sqlCountQuery);){
                          
            while (rsCount1.next()) {
                recordCount = rsCount1.getInt(1);
            }

            if (recordCount <= 0) {
               resultHashMap.put(PcsEJBConstant.RECORD_COUNT, new Integer(recordCount));
               return resultHashMap; // return empty List
            }
                System.out.println("Query for --> "+sqlQueryStr.toString());
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = stmt.executeQuery(sqlQueryStr);
                     
            EOfficeActionRow eOfficeAction = null;
            for (int j = displayStartRow; j <= displayEndRow && !(rs.isLast()); j++) {
               rs.absolute(j);
               eOfficeAction = new EOfficeActionRow(rs.getString(1), rs.getString(2), rs.getString(3),
                     rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
                     rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14), rs.getTimestamp(15), rs.getTimestamp(16),
                     rs.getTimestamp(17), rs.getString(18), rs.getString(19), rs.getString(20), rs.getString(21), rs.getString(22),
                     rs.getString(23), rs.getString(24), rs.getString(25));

               displayRowList.add(eOfficeAction);
            }

            for (int k = 1; !(rs.isAfterLast()); k = k + 100) {
               HashMap hm = new HashMap();
               rs.absolute(k);
               hm.put("LOWER_LIMIT", getEOfficeActionDispRefText(rs, sortBy));

               rs.absolute(k + 99);

               if (rs.isLast() || rs.isAfterLast()) {
                  rs.last();
                  hm.put("UPPER_LIMIT", getEOfficeActionDispRefText(rs, sortBy));
                  rs.next();
               } else {
                  hm.put("UPPER_LIMIT", getEOfficeActionDispRefText(rs, sortBy));
               }
               rangeRowList.add(hm);
            }

           resultHashMap.put(PcsEJBConstant.RECORD_COUNT, new Integer(recordCount));
           resultHashMap.put(PcsEJBConstant.DISPLAY_LIST, displayRowList);
           resultHashMap.put(PcsEJBConstant.RANGE_LIST, rangeRowList);
            }

        } catch (SQLException sqlE) {
            if (sqlE.getErrorCode() == 1017 || sqlE.getErrorCode() == 03114) {
                throw new ServiceException(PcsEJBConstant.DB_CONNECTION_ERROR + ": " + sqlE.getMessage());
            } else {
                throw new ServiceException(PcsEJBConstant.PCS_REQUEST_FAILED + ": " + sqlE.getMessage());
            }
        } finally {
           
            
            getDaoUtil().closeResultSet(rsCount);
            getDaoUtil().closeResultSet(rs);
            getDaoUtil().closeStatement(stmt);
            getDaoUtil().closeConnection(conn);
        }

        return resultHashMap;

    }

    private String eOfficeActionCountQuery(String strWhereClause) {
        StringBuffer eOfficeActionCountQueryBuf = new StringBuffer("SELECT count(1) FROM STG_PAIR_IFW_OUTGOING_EOA ifw, PAIR_CUSTOMERS pc, ELCTRN_DOC_SCAN_LOG_EOA e ");
        eOfficeActionCountQueryBuf.append(strWhereClause);
        return eOfficeActionCountQueryBuf.toString();
    }

    private String eOfficeActionQuery(String strWhereClause, String sortBy, String sortOrder) {
        Calendar now = Calendar.getInstance();

        StringBuffer outGoingCorrListByCnSqlBuf = new StringBuffer("SELECT ifw.APPL_ID, ");
        outGoingCorrListByCnSqlBuf.append(" DECODE((ifw.PATENT_ISSUE_DT-TO_DATE('").append(formatter.format(now.getTime())).append("','MM-DD-YYYY'))-ABS(ifw.PATENT_ISSUE_DT-TO_DATE('").append(formatter.format(now.getTime())).append("','MM-DD-YYYY')),0,'-',NVL(TRIM(ifw.PATENT_NO),'-'))PATENT_NO, ");
        outGoingCorrListByCnSqlBuf.append(" DECODE(TO_CHAR(e.MAILROOM_DT,'YYYYMMDD'),'00010101','-',NULL,'-', TO_CHAR(e.MAILROOM_DT,'YYYYMMDD')), ");
        outGoingCorrListByCnSqlBuf.append(" NVL(ifw.FILE_WRAPPER_PACKAGE_ID,'-'), ");
        outGoingCorrListByCnSqlBuf.append(" NVL(TRIM(ifw.DOC_DESCRIPTION_TX),'-'), ");
        outGoingCorrListByCnSqlBuf.append(" NVL(ifw.START_PAGE_NO,0), ");
        outGoingCorrListByCnSqlBuf.append(" NVL(ifw.PAGE_COUNT,0),NVL(OFFSET,0), ");
        outGoingCorrListByCnSqlBuf.append(" NVL(TRIM(ifw.ATTY_DKT_NO),'-'), ");
        outGoingCorrListByCnSqlBuf.append(" DECODE(TO_CHAR(e.PRCSG_TS,'YYYYMMDD'),'00010101','-',NULL,'-', TO_CHAR(e.PRCSG_TS,'YYYYMMDD')) E_NOTIFICATION_DATE, ");
        outGoingCorrListByCnSqlBuf.append(" DECODE(TO_CHAR(ifw.DOC_VIEW_DT,'YYYYMMDD'),'00010101','-',NULL,'-', TO_CHAR(ifw.DOC_VIEW_DT,'YYYYMMDD')), ");
        outGoingCorrListByCnSqlBuf.append(" ifw.CUSTOMER_NO, ifw.DOC_CATEGORY, ifw.DOCUMENT_CD, ifw.LAST_MODIFIED_TS, pc.FILE_DT, ");
        outGoingCorrListByCnSqlBuf.append(" pc.PUB_ACTL_PUB_DT, pc.PUB_NO_KIND_CD, pc.PUB_NO_SEQ_NO, pc.PUB_NO_YR, pc.STATUS_GRP_CD,ifw.DOC_ID, ");
        outGoingCorrListByCnSqlBuf.append(" ifw.DOCKETED, ");
        outGoingCorrListByCnSqlBuf.append(" ifw.DA_ID, ");
        outGoingCorrListByCnSqlBuf.append(" NVL(ifw.DOC_VIEWER_NM,'-') ");
        outGoingCorrListByCnSqlBuf.append(" FROM STG_PAIR_IFW_OUTGOING_EOA ifw, PAIR_CUSTOMERS pc, ELCTRN_DOC_SCAN_LOG_EOA e ");
        outGoingCorrListByCnSqlBuf.append(strWhereClause);
        outGoingCorrListByCnSqlBuf.append(" order by ");
        outGoingCorrListByCnSqlBuf.append(eOfficeActionOrderString(sortBy, sortOrder));
        outGoingCorrListByCnSqlBuf.append(" ,ifw.FILE_WRAPPER_PACKAGE_ID DESC,ifw.START_PAGE_NO ASC ");

        return outGoingCorrListByCnSqlBuf.toString();

    }

    private String getEOfficeActionDispRefText(ResultSet rs, String sortBy)
            throws SQLException {
        StringUtil strUtil = new StringUtil();

        switch (sortBy) {
            case APPL_ID_COLUMN:
                return strUtil.getFormattedApplicationNo(rs.getString(1));
            case PATENT_NO_COLUMN:
                return strUtil.formatInputPatentNumber(rs.getString(2)) + "(" + strUtil.getFormattedApplicationNo(rs.getString(1)) + ")";
            case ATTY_DKT_NO_COLUMN:
                return rs.getString(9) + "(" + strUtil.getFormattedApplicationNo(rs.getString(1)) + ")";
            case CUSTOMER_NO_COLUMN:
                return (rs.getString(12) + "(" + strUtil.getFormattedApplicationNo(rs.getString(1)) + ")");
            case MAILROOM_DT_COLUMN:
                return strUtil.dateDisplayFormat(rs.getString(3)) + "(" + strUtil.getFormattedApplicationNo(rs.getString(1)) + ")";
            case DOC_VIEW_DT_COLUMN:
                return strUtil.dateDisplayFormat(rs.getString(11)) + "(" + strUtil.getFormattedApplicationNo(rs.getString(1)) + ")";
            case DOCUMENT_CD_COLUMN:
                return (((rs.getString(14) == null) ? "" : rs.getString(14)) + "(" + strUtil.getFormattedApplicationNo(rs.getString(1)) + ")");
            case E_NOTIFICATION_DATE_COLUMN:
                return strUtil.dateDisplayFormat(rs.getString(10)) + "(" + strUtil.getFormattedApplicationNo(rs.getString(1)) + ")";
            default:
                return strUtil.dateDisplayFormat(rs.getString(10)) + "(" + strUtil.getFormattedApplicationNo(rs.getString(1)) + ")";
        }
    }

    private String eOfficeActionOrderString(String sortBy, String sortOrder) {
        String orderByString;

        switch (sortBy) {
            case APPL_ID_COLUMN:
                if (sortOrder.equals("ASC"))
                    orderByString = "1 ASC";
                else
                    orderByString = "1 DESC";
                break;
            case PATENT_NO_COLUMN:
                if (sortOrder.equals("ASC"))
                    orderByString = "2 ASC, 1 DESC";
                else
                    orderByString = "2 DESC, 1 DESC";
                break;
            case MAILROOM_DT_COLUMN:
                if (sortOrder.equals("ASC"))
                    orderByString = "3 ASC, 1 DESC";
                else
                    orderByString = "3 DESC, 1 DESC";
                break;
            case ATTY_DKT_NO_COLUMN:
                if (sortOrder.equals("ASC"))
                    orderByString = "9 ASC, 1 DESC";
                else
                    orderByString = "9 DESC, 1 DESC";
                break;
            case DOC_VIEW_DT_COLUMN:
                if (sortOrder.equals("ASC"))
                    orderByString = "11 ASC, 1 DESC";
                else
                    orderByString = "11 DESC, 1 DESC";
                break;
            case CUSTOMER_NO_COLUMN:
                if (sortOrder.equals("ASC"))
                    orderByString = "12 ASC, 1 DESC";
                else
                    orderByString = "12 DESC, 1 DESC";
                break;
            case DOCUMENT_CD_COLUMN:
                if (sortOrder.equals("ASC"))
                    orderByString = "14 ASC, 1 DESC";
                else
                    orderByString = "14 DESC, 1 DESC";
                break;
            case E_NOTIFICATION_DATE_COLUMN:
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

    /**
     * Queries database for eOffice Action notifications by customer number list
     *
     * @param //custNum
     * @param //startDate
     * @param //endDate
     * @param sortBy
     * @param sortOrder
     * @param dispRowIndex
     * @return HashMap of eOffice Action Notification details
     * @throws ServiceException
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public HashMap getDisplayEOfficeActionByCn(String custNum, String startDate, String endDate, String sortBy, String sortOrder, int dispRowIndex) throws ServiceException, ClassNotFoundException, SQLException {

        String whereClause = eOfficeActionByCNWhereClause(custNum, startDate, endDate);
        return getDisplayEOfficeAction(whereClause, sortBy, sortOrder, dispRowIndex);
    }
  

    
 private String eOfficeActionByCNWhereClause(String custNum, String startDate, String endDate) {
        
        StringBuffer whereClauseBuf = new StringBuffer(" WHERE ifw.DOC_ID = e.DN_IFW_DOC_ID AND ifw.FK_PC_ID=pc.FK_PC_ID");
        whereClauseBuf.append(" AND ((e.PRCSG_RETURN_CD = '1') OR (e.PRCSG_RETURN_CD = '0' AND e.prcsg_reason_cd = '3'))");
        whereClauseBuf.append(" AND e.PRCSG_STAT_CT IN ('PROCESSED','IGNORED')"); 
        whereClauseBuf.append(" AND pc.APPL_ID NOT LIKE 'PCT%'");
        whereClauseBuf.append(" AND to_char(e.PRCSG_TS,'YYYYMMDD') >= ");
        whereClauseBuf.append(startDate);
        whereClauseBuf.append(" AND to_char(e.PRCSG_TS,'YYYYMMDD') <= ");  
        whereClauseBuf.append(endDate);
        
        List cnList = StringUtil.getTokenizedList(custNum, ",");
        Iterator itCns = cnList.iterator();
        StringBuffer tempCnBuff = new StringBuffer(" AND ifw.CUSTOMER_NO in (");
        while (itCns.hasNext()) {
            tempCnBuff.append("'" + itCns.next().toString() + "',");
        }

        //Remove the last "," and return the string;
        whereClauseBuf.append(tempCnBuff.substring(0, tempCnBuff.length() - 1)).append(")");
        
        return whereClauseBuf.toString();
    }

}
