package gov.uspto.patent.privatePair.PCSEJBApp.dao;

import gov.uspto.patent.privatePair.PCSEJBApp.dto.AppListByCustNumRow;
import gov.uspto.patent.privatePair.PCSEJBApp.dto.CustOutGoingRow;
import gov.uspto.patent.privatePair.PCSEJBApp.dto.ElectrnDocStagingTableVO;
import gov.uspto.patent.privatePair.PCSEJBApp.dto.IfwOutgoingVO;
import gov.uspto.patent.privatePair.utils.DBUtil;
import gov.uspto.patent.privatePair.utils.PcsEJBConstant;
import gov.uspto.patent.privatePair.utils.ServiceException;
import gov.uspto.patent.privatePair.utils.StringUtil;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import static gov.uspto.patent.privatePair.admin.dao.DaoUtil.getDaoUtil;

@Component
@Configuration
@PropertySource("classpath:application.properties")
public class CustDetailsDAO {

    @Autowired
    DataSource dataSource;

    Calendar now = Calendar.getInstance();
    SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
    public static final String STATUS = "STATUS";
    public static final String RECORD_COUNT ="RECORD_COUNT";
    public static final String RESULT_LIST = "RESULT_LIST";
    public static final String DISPLAY_LIST = "DISPLAY_LIST";
    public static final String RANGE_LIST = "RANGE_LIST";
    public static final String STATUS_MESSAGE ="STATUS_MESSAGE";
    public static final String SUCCESS = "SUCCESS";
    public static final String PCS_REQUEST_FAILED =	"Request failed. Please try after some time.";
    public static final String ERROR = "ERROR";
    public static final String DB_CONNECTION_ERROR = "DB_ERROR: Unable to connect to PAIR database.";
    public static final String DB_OEMS_CONNECTION_ERROR = "DB_ERROR: Unable to connect to OEMS database.";
    public static final String DB_INVALID_CONNECTION_DETAILS = "DB_ERROR: Invalid credentials.";
    public static final String DB_ERROR_CODE = "DB_ERROR: ORACLE ERROR CODE: ";
    public static final String SUCCESS_RETRIEVE	= "Successfully retrieved";
    public static final String NO_REC_FOUND = "No record found for this search";
    public static final String LIMIT_EXCEED_MSG = "Resultant list exceeded the limit, please refine the search";
    public static final String PCS_DB_ERROR = "DB_ERROR: Unable to connect to PAIR database.";
    /**
     * The method "getDisplayAppList" is used to retrieve application data for which the status has changed
     * for the last numbers of days passed as input paramerter. The date is retrieved from the PAIR_CUSTOMERS table.
     * The data is retrieved in display and range list format in which full data is displayed for the first 100 rows and
     * the remaining data is displayed in range format.
     *
     * @param //int	Customer number
     * @param //int	days in which the application status as changed
     * @param //String	sort by string
     * @param //String	sort order
     * @param //int 	display row index, default is 1
     * @return HashMap	Display and Range link format of the resultant application data
     * @exception ServiceException
     * @since JKD 1.4
     * @modified on 6/19/2009 - eOffice Action
     */
    public HashMap getDisplayAppList(String displayBy, String custNum, String appStatus, int intStatusDays, String sortBy, String sortOrder, int iDispRowIndex) throws ServiceException {

        HashMap resultHashMap = new HashMap();
        ArrayList displayRowList = new ArrayList();
        ArrayList rangeRowList = new ArrayList();
        String sqlQueryStr = "", sqlCountQuery="";
        Statement stmt=null, stmtCount=null;
        ResultSet rs=null, rsCount=null;
        Connection dbConnection =null;
        int recordCount=0;
        String status="", statusMsg="";

        //Initial default sortBy and sortOrder
        if(sortBy.trim().equals(""))
            sortBy="APPL_ID";
        if(sortOrder.trim().equals(""))
            sortOrder="DESC";
//		Formation for count query and actual query.
        String strWhereClause = displayAppListWhereClause(displayBy, custNum, appStatus, intStatusDays);
        //sqlCountQuery = "SELECT count(1) FROM PAIR_CUSTOMERS WHERE CUSTOMER_NO = ? AND STATUS_DATE >= (sysdate - ?)";
        sqlCountQuery = displayAppListCountQuery(strWhereClause);
        //sqlQueryStr = appListByCnAndStatusDtQuery(sortBy, sortOrder);
        sqlQueryStr = displayAppListQuery(strWhereClause, sortBy, sortOrder);
        try {

            int displayStartRow=(iDispRowIndex*100)+1;
            int displayEndRow=displayStartRow+99;
            dbConnection = dataSource.getConnection();
            stmtCount = dbConnection.createStatement();
            System.out.println("INFO: CustDetailsDAO.getDisplayAppList(): Count Query "+ sqlCountQuery);
            rsCount = stmtCount.executeQuery(sqlCountQuery);
            while(rsCount.next()){
                recordCount = rsCount.getInt(1);
            }
            System.out.println("INFO: CustDetailsDAO.getDisplayAppList(): Count for CUSTOMER:"+ custNum+", DISPLAY-BY:"+displayBy+", STATUS:"+appStatus+" is: "+recordCount);

            if (recordCount<=0) {
                resultHashMap.put("RECORD_COUNT",new Integer(recordCount));

                resultHashMap.put(STATUS,SUCCESS);
                resultHashMap.put(STATUS_MESSAGE,NO_REC_FOUND);
                resultHashMap.put(RECORD_COUNT,new Integer(recordCount));
                resultHashMap.put(DISPLAY_LIST, displayRowList);
                resultHashMap.put(RANGE_LIST, rangeRowList);

                return resultHashMap; // return empty List
            }

            //System.out.println("Total Application count for Customer "+ custNum+" is: "+recordCount);

            stmt = dbConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

            Calendar before = Calendar.getInstance();
            System.out.println("INFO: CustDetailsDAO.getDisplayAppList(): "+ sqlQueryStr);
            rs = stmt.executeQuery(sqlQueryStr);
            Calendar after = Calendar.getInstance();
            System.out.println("  getDisplayAppList query time taken : " + StringUtil.diffDates(before, after));
            int count = 0;
            AppListByCustNumRow AppByCustNum=null;        
            if(null!=rs) {
                    //while (rs.next()) {
                    for(int j=displayStartRow;j<=displayEndRow && !(rs.isLast());j++)
                    {
                        rs.absolute(j);

                        AppByCustNum = new AppListByCustNumRow(rs.getString(1).trim(), rs.getString(2), rs.getString(3), rs.getString(4),
                                ((rs.getString(5).trim().length() <= 5) ? "-" : rs.getString(5)),
                                rs.getString(6), rs.getString(7), rs.getString(8), "", "", rs.getString(9));

                        displayRowList.add(AppByCustNum);

                    }
            rs.first();
                   
                     for(int k=1;!(rs.isAfterLast());k=k+100)
                    {

                        HashMap hm = new HashMap();
                         rs.absolute(k);
                        hm.put("LOWER_LIMIT", getDispRefText(rs, sortBy));

                         rs.absolute(k+99);
                        if (rs.isLast() || rs.isAfterLast()) {
                            rs.last();
                            hm.put("UPPER_LIMIT", getDispRefText(rs, sortBy));
                            rs.next();
                        } else {
                            hm.put("UPPER_LIMIT", getDispRefText(rs, sortBy));
                        }

                        rangeRowList.add(hm);
                    }
             }
            status=SUCCESS;
            statusMsg= SUCCESS_RETRIEVE;

        }catch (SQLException sqlE)
        {
            status=ERROR;
            statusMsg=PCS_DB_ERROR;
            System.out.println("ERROR: CustdetailsDAO.getDisplayAppList() :"+sqlE.getMessage());
            if(sqlE.getErrorCode()==1017 || sqlE.getErrorCode()==03114)
                throw new ServiceException(DB_CONNECTION_ERROR);
            sqlE.printStackTrace();
        }
        catch (Exception e) {
            status=ERROR;
            statusMsg=PCS_REQUEST_FAILED;
            System.out.println("ERROR: CustdetailsDAO.getDisplayAppList() :"+e.getMessage());
            e.printStackTrace();
        }
        finally {
            getDaoUtil().closeResultSet(rsCount);
            getDaoUtil().closeStatement(stmtCount);
            getDaoUtil().closeResultSet(rs);
            getDaoUtil().closeStatement(stmt);
            getDaoUtil().closeConnection(dbConnection);
        }//finally

        resultHashMap.put(STATUS,status);
        resultHashMap.put(STATUS_MESSAGE,statusMsg);
        resultHashMap.put(RECORD_COUNT,new Integer(recordCount));
        resultHashMap.put(DISPLAY_LIST, displayRowList);
        resultHashMap.put(RANGE_LIST, rangeRowList);

        return resultHashMap;

    }// getDisplayAppList()
    
    
    
    
    

	public HashMap getDisplayAppListForCustSearch(String displayBy, String custNum,String appStatus, int intStatusDays,String sortBy,String sortOrder,int iDispRowIndex) throws ServiceException {

		HashMap resultHashMap = new HashMap();
		ArrayList displayRowList = new ArrayList();
		ArrayList rangeRowList = new ArrayList();
		String sqlQueryStr = "", sqlCountQuery="";
		Statement stmt=null, stmtCount=null;
		ResultSet rs=null, rsCount=null;
		Connection dbConnection =null;
		int recordCount=0;
		String status="", statusMsg="";

		//Initial default sortBy and sortOrder 
		if(sortBy.trim().equals(""))
			sortBy="APPL_ID";
		if(sortOrder.trim().equals(""))
			sortOrder="DESC";
//		Formation for count query and actual query.
		String strWhereClause = displayAppListWhereClause(displayBy, custNum, appStatus, intStatusDays);
		//sqlCountQuery = "SELECT count(1) FROM PAIR_CUSTOMERS WHERE CUSTOMER_NO = ? AND STATUS_DATE >= (sysdate - ?)";
		sqlCountQuery = displayAppListCountQuery(strWhereClause);
		//sqlQueryStr = appListByCnAndStatusDtQuery(sortBy, sortOrder);
		sqlQueryStr = displayAppListQuery(strWhereClause, sortBy, sortOrder);
		try {

			int displayStartRow=(iDispRowIndex*100)+1;
			int displayEndRow=displayStartRow+99;
			dbConnection = dataSource.getConnection();
			stmtCount = dbConnection.createStatement();
			System.out.println("INFO: CustDetailsDAO.getDisplayAppList(): Count Query "+ sqlCountQuery);
			rsCount = stmtCount.executeQuery(sqlCountQuery);
			while(rsCount.next()){
				recordCount = rsCount.getInt(1);
			}
			System.out.println("INFO: CustDetailsDAO.getDisplayAppList(): Count for CUSTOMER:"+ custNum+", DISPLAY-BY:"+displayBy+", STATUS:"+appStatus+" is: "+recordCount);

			if (recordCount<=0) {
				resultHashMap.put("RECORD_COUNT",new Integer(recordCount));

				resultHashMap.put(PcsEJBConstant.STATUS,PcsEJBConstant.SUCCESS);
				resultHashMap.put(PcsEJBConstant.STATUS_MESSAGE,PcsEJBConstant.NO_REC_FOUND);
				resultHashMap.put(PcsEJBConstant.RECORD_COUNT,new Integer(recordCount));
				resultHashMap.put(PcsEJBConstant.DISPLAY_LIST, displayRowList);
				resultHashMap.put(PcsEJBConstant.RANGE_LIST, rangeRowList);

				return resultHashMap; // return empty List
			}

			//System.out.println("Total Application count for Customer "+ custNum+" is: "+recordCount);

			stmt = dbConnection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);

			Calendar before = Calendar.getInstance();
			System.out.println("INFO: CustDetailsDAO.getDisplayAppList(): "+ sqlQueryStr);
			rs = stmt.executeQuery(sqlQueryStr);
			Calendar after = Calendar.getInstance();
			System.out.println("  getDisplayAppList query time taken : " + StringUtil.diffDates(before, after));


			AppListByCustNumRow AppByCustNum=null;
			for(int j=displayStartRow;j<=displayEndRow && !(rs.isLast());j++)
			{
				rs.absolute(j);

				AppByCustNum = new AppListByCustNumRow(rs.getString(1).trim(),rs.getString(2),rs.getString(3),rs.getString(4),
				((rs.getString(5).trim().length() <= 5)? "-" : rs.getString(5)),
				rs.getString(6),rs.getString(7),rs.getString(8),"","", rs.getString(9));

				displayRowList.add(AppByCustNum);

			}

			for(int k=1;!(rs.isAfterLast());k=k+100){

				HashMap hm = new HashMap();
				rs.absolute(k);
				hm.put("LOWER_LIMIT",getDispRefText(rs,sortBy));

				rs.absolute(k+99);
				if(rs.isLast()|| rs.isAfterLast()){
					rs.last();
					hm.put("UPPER_LIMIT",getDispRefText(rs,sortBy));
					rs.next();
				}else
				{
					hm.put("UPPER_LIMIT",getDispRefText(rs,sortBy));
				}

				rangeRowList.add(hm);
			}

			status=PcsEJBConstant.SUCCESS;
			statusMsg=PcsEJBConstant.SUCCESS_RETRIEVE;

		}catch (SQLException sqlE)
		{
			status=PcsEJBConstant.ERROR;
			statusMsg=PcsEJBConstant.PCS_DB_ERROR;
			 System.out.println("ERROR: CustdetailsDAO.getDisplayAppList() :"+sqlE.getMessage());
			 if(sqlE.getErrorCode()==1017 || sqlE.getErrorCode()==03114)
				throw new ServiceException(PcsEJBConstant.DB_CONNECTION_ERROR);
		}
		 catch (Exception e) {
			status=PcsEJBConstant.ERROR;
			statusMsg=PcsEJBConstant.PCS_REQUEST_FAILED;
		 	System.out.println("ERROR: CustdetailsDAO.getDisplayAppList() :"+e.getMessage());
		}
			finally {
				 getDaoUtil().closeResultSet(rsCount);
		            getDaoUtil().closeStatement(stmtCount);
		            getDaoUtil().closeResultSet(rs);
		            getDaoUtil().closeStatement(stmt);
		            getDaoUtil().closeConnection(dbConnection);
			}//finally

			resultHashMap.put(PcsEJBConstant.STATUS,status);
			resultHashMap.put(PcsEJBConstant.STATUS_MESSAGE,statusMsg);
			resultHashMap.put(PcsEJBConstant.RECORD_COUNT,new Integer(recordCount));
			resultHashMap.put(PcsEJBConstant.DISPLAY_LIST, displayRowList);
			resultHashMap.put(PcsEJBConstant.RANGE_LIST, rangeRowList);

		return resultHashMap;

	}// getDisplayAppList() 
    
    

    private String getDispRefText(ResultSet rs,String sortBy)
            throws SQLException
    {
        StringUtil strUtil = new StringUtil();

        //System.out.println("Displayed data Row num: "+rs.getRow());
        //if(sortBy.equals("APPL_ID"))
        if("APPL_ID".equals(sortBy))
            return strUtil.getFormattedApplicationNo(rs.getString(1));
            //else if(sortBy.equals("CUSTOMER_NO"))
        else if("CUSTOMER_NO".equals(sortBy))
            return rs.getString(2)+"("+strUtil.getFormattedApplicationNo(rs.getString(1))+")";
            //else if(sortBy.equals("PATENT_NO"))
        else if("PATENT_NO".equals(sortBy))
            return strUtil.formatInputPatentNumber(rs.getString(3))+"("+strUtil.getFormattedApplicationNo(rs.getString(1))+")";
            //else if(sortBy.equals("ATTY_DKT_NO"))
        else if("ATTY_DKT_NO".equals(sortBy))
            return rs.getString(4)+"("+strUtil.getFormattedApplicationNo(rs.getString(1))+")";
            //else if(sortBy.equals("PUB_NO_YR"))
        else if("PUB_NO_YR".equals(sortBy))
            return (((rs.getString(5).trim().length() <= 5)? "-" : rs.getString(5)) + "("+strUtil.getFormattedApplicationNo(rs.getString(1))+")");
            //else if(sortBy.equals("STATUS_DATE"))
        else if("STATUS_DATE".equals(sortBy))
            return strUtil.dateDisplayFormat(rs.getString(6))+"("+strUtil.getFormattedApplicationNo(rs.getString(1))+")";
            //else if(sortBy.equals("FILE_DT")){
        else if("FILE_DT".equals(sortBy)){
            return strUtil.dateDisplayFormat(rs.getString(7))+"("+strUtil.getFormattedApplicationNo(rs.getString(1))+")";
        }
        else
            return strUtil.getFormattedApplicationNo(rs.getString(1));

    }//private String getDispRefText(rs,sortBy)

    /**
     * @modified on 6/19/2009 - eOffice Action
     */
    private String displayAppListOrderByClause(String sortBy, String sortOrder)
    {
        String orderByString;

        //if(sortBy.equals("APPL_ID")){
        if("APPL_ID".equals(sortBy)){
            //if(sortOrder.equals("ASC"))
            if("ASC".equals(sortOrder))
                orderByString = "1 ASC";
            else
                orderByString = "1 DESC";
            //}else if(sortBy.equals("CUSTOMER_NO")){
        }else if("CUSTOMER_NO".equals(sortBy)){
            //if(sortOrder.equals("ASC"))
            if("ASC".equals(sortOrder))
                orderByString = "2 ASC, 1 DESC";
            else
                orderByString = "2 DESC, 1 DESC";
            //}else if(sortBy.equals("PATENT_NO")){
        }else if("PATENT_NO".equals(sortBy)){
            //if(sortOrder.equals("ASC"))
            if("ASC".equals(sortOrder))
                orderByString = "3 ASC, 1 DESC";
            else
                orderByString = "3 DESC, 1 DESC";
        }
        //else if(sortBy.equals("ATTY_DKT_NO")){
        else if("ATTY_DKT_NO".equals(sortBy)){
            //if(sortOrder.equals("ASC"))
            if("ASC".equals(sortOrder))
                orderByString = "4 ASC, 1 DESC";
            else
                orderByString = "4 DESC, 1 DESC";
        }
        //else if(sortBy.equals("PUB_NO_YR")){
        else if("PUB_NO_YR".equals(sortBy)){
            //if(sortOrder.equals("ASC"))
            if("ASC".equals(sortOrder))
                orderByString = "5 ASC, 1 DESC";
            else
                orderByString = "5 DESC, 1 DESC";
        }
        //6 & 7 is status_code and status_description
        //else if(sortBy.equals("STATUS_DATE")){
        else if("STATUS_DATE".equals(sortBy)){
            //if(sortOrder.equals("ASC"))
            if("ASC".equals(sortOrder))
                orderByString = "STATUS_DATE ASC, 1 DESC";
            else
                orderByString = "STATUS_DATE DESC, 1 DESC";
        }
        //else if(sortBy.equals("FILE_DT")){
        else if("FILE_DT".equals(sortBy)){
            //if(sortOrder.equals("ASC"))
            if("ASC".equals(sortOrder))
                orderByString = "FILE_DT ASC, 1 DESC";
            else
                orderByString = "FILE_DT DESC, 1 DESC";
        }
        else
            orderByString = "1, DESC";

        return orderByString;
    }

    /**
     * @modified on 6/19/2009 - eOffice Action
     */
    private String displayAppListWhereClause(String displayBy, String custNum, String status, int days)
    {
        StringBuffer whereClauseBuf = new StringBuffer(" WHERE ");
        if(null!=displayBy && displayBy.equalsIgnoreCase("CN"))
        {
            //Only one CustomerNumber, Status might be All or Abandoned, Issued, New, Pending
            whereClauseBuf.append(" CUSTOMER_NO = ");
            whereClauseBuf.append(custNum);

        }else if(null!=displayBy &&  displayBy.equalsIgnoreCase("CN_STATUS_DT"))
        {

            //Multiple Customers, past status days and status might be All or Abandoned, Issued, New, Pending
            List cnList = StringUtil.getTokenizedList(custNum, "#");
            Iterator itCns = cnList.iterator();
            StringBuffer tempCnBuff = new StringBuffer(" CUSTOMER_NO in (");
            while(itCns.hasNext())
            {
                tempCnBuff.append(""+itCns.next().toString()+",");
            }

            //Remove the last "," and return the string;
            whereClauseBuf.append(tempCnBuff.substring(0,tempCnBuff.length()-1)).append(")");
            //Append the status date where clause
            whereClauseBuf.append(" AND STATUS_DATE >= (TO_DATE('").append(formatter.format(now.getTime())).append("','MM-DD-YYYY') -");
            whereClauseBuf.append(days).append(")");

        }

        //Status common to above 2 conditions.
		/* Earlier hardcoded status
		if(status!=null &&(status.equalsIgnoreCase("ABANDONED") || status.equalsIgnoreCase("ISSUED") ||
				status.equalsIgnoreCase("NEW") || status.equalsIgnoreCase("PENDING")))
		{
			whereClauseBuf.append(" AND STATUS_GRP_CD='");
			whereClauseBuf.append(status).append("'");
		} */

        status = status.toUpperCase().trim();
        if(status!=null && !status.trim().equals("") && !status.equalsIgnoreCase("ALL"))
        {
            if(status.equalsIgnoreCase("OTHER"))
            {
                whereClauseBuf.append(" AND (STATUS_GRP_CD is null OR STATUS_GRP_CD =' '  OR");
                whereClauseBuf.append(" TRIM(STATUS_GRP_CD) NOT IN (SELECT upper(PROPERTY_KEY_TX) FROM PAIR_PROPERTY WHERE");
                whereClauseBuf.append("  PROPERTY_MODE_TX='PRIVATE' AND PROPERTY_NM='ApplicationStatusType' AND upper(PROPERTY_KEY_TX)!='ALL' AND upper(PROPERTY_KEY_TX)!='OTHER'))");
            }else
            {
                //The selected status to be
                whereClauseBuf.append(" AND STATUS_GRP_CD='");
                whereClauseBuf.append(status).append("'");
            }
        }//if(status!=null && !status.trim().equals("") && !status.equalsIgnoreCase("ALL"))

        return whereClauseBuf.toString();

    }

    /**
     * @modified on 6/19/2009 - eOffice Action
     */
    private String displayAppListCountQuery(String strWhereClause)
    {
        StringBuffer displayAppListCountQueryBuf = new StringBuffer("SELECT count(1) from PAIR_CUSTOMERS ");
        displayAppListCountQueryBuf.append(strWhereClause);

        return displayAppListCountQueryBuf.toString();
    }

    private String displayAppListQuery(String strWhereClause, String sortBy, String sortOrder) {

        StringBuffer displayAppListSQLBuf = new StringBuffer("SELECT  APPL_ID, CUSTOMER_NO, ");
        //displayAppListSQLBuf.append(" DECODE((PATENT_ISSUE_DT-SYSDATE)-ABS(PATENT_ISSUE_DT-SYSDATE),0,'-',NVL(LTRIM(RTRIM(PATENT_NO)),'-'))PATENT_NO,  ");
        displayAppListSQLBuf.append(" DECODE((PATENT_ISSUE_DT-TO_DATE('").append(formatter.format(now.getTime()))
                .append("','MM-DD-YYYY'))-ABS(PATENT_ISSUE_DT-TO_DATE('").append(formatter.format(now.getTime()))
                .append("','MM-DD-YYYY')),0,'-',NVL(TRIM(PATENT_NO),'-'))PATENT_NO, ");
        displayAppListSQLBuf.append(" NVL(TRIM(ATTY_DKT_NO),'-'), ");
        displayAppListSQLBuf.append(" 'US ' ||   NVL(TO_CHAR(PUB_NO_YR),'0') || '-' ||   NVL(TRIM(PUB_NO_SEQ_NO),' ') || ' ' ||   NVL(TRIM(PUB_NO_KIND_CD),' '),   ");
        displayAppListSQLBuf.append(" DECODE(TO_CHAR(STATUS_DATE,'YYYYMMDD'),NULL,'-','00010101','-',TO_CHAR(STATUS_DATE,'YYYYMMDD')), ");
        displayAppListSQLBuf.append(" DECODE(TO_CHAR(FILE_DT,'YYYYMMDD'),NULL,'-','00010101','-',TO_CHAR(FILE_DT,'YYYYMMDD')), ");
        displayAppListSQLBuf.append(" NVL(EFW_STATUS_IN,'-'), STATUS_GRP_CD ");
        displayAppListSQLBuf.append(" FROM PAIR_CUSTOMERS ");
        displayAppListSQLBuf.append(strWhereClause);
        displayAppListSQLBuf.append(" order by ");
        displayAppListSQLBuf.append(displayAppListOrderByClause(sortBy,sortOrder));
        return displayAppListSQLBuf.toString();
    }
    
    /*
	 * The method "getOptedInCustRowsByDN" retrieves customer numbers associated with 
	 * Distingushed Name - DN that have opted into the e-Office Action notification.
	 */
	public Vector getOptedInCustRowsByDN(String distinguishedName) throws ServiceException {

		Vector resultRowVect = new Vector();
		String sqlQueryStr = "";
		Connection dbConnection = null;
		ResultSet rs = null;
		Statement stmt = null;
		
		try {
			sqlQueryStr = optedInCustNumWithDNQuery(distinguishedName);
			
			// Fetch the database connection
			dbConnection = dataSource.getConnection();
			
			// Create the SQL statement and execute 
			stmt = dbConnection.createStatement();
			rs = stmt.executeQuery(sqlQueryStr);
			
			// If there is no data; return an empty vector
			if (rs == null || !rs.next()) {
				return resultRowVect; // return empty vector
			} else {
				
				// Add the First Record to the Vector
				resultRowVect.add(rs.getBigDecimal(1));
				
				while (rs.next()) {
					resultRowVect.add(rs.getBigDecimal(1));
				}	
			}
		} catch (SQLException sqlE) {
			if (sqlE.getErrorCode() == 1017)
				throw new ServiceException(PcsEJBConstant.DB_INVALID_CONNECTION_DETAILS);
			else if (sqlE.getErrorCode() == 03114)
				throw new ServiceException(PcsEJBConstant.DB_CONNECTION_ERROR);
			else {
				throw new ServiceException(PcsEJBConstant.DB_ERROR_CODE + sqlE.getErrorCode());
			}
		} catch (Exception e) {
			throw new ServiceException(e.getMessage());
		} finally {		
	            getDaoUtil().closeResultSet(rs);
	            getDaoUtil().closeStatement(stmt);
	            getDaoUtil().closeConnection(dbConnection);
		}
		return resultRowVect;
	}
	
	/**
	 * The query "optedInCustNumWithDNQuery" is used to retrieve customer numbers that are
	 * associated with the distinguished name and have opted into the e-Office Action notification. 
	 * 
	 * @param String 	DN string
	 * @param String	case sensitive chek or not.
	 * @return String	Customer number select query for the DN passed in.
	 * @exception
	 * @since JDK 1.4
	 *
     */
	public String optedInCustNumWithDNQuery(String distinguishedName) {
		
		String distinguishedName_escaped = StringUtil.replace(distinguishedName, "'", "''");

		/*String optedInCustNumWithDNSql = "select PAIR_CUSTOMER_OPT.CUSTOMER_NO "
					+ " from PAIR_USER_DN, PAIR_USER_CN, PAIR_CUSTOMER_OPT"
					+ " WHERE PAIR_USER_DN.user_id = PAIR_USER_CN.user_id" 
					+ " AND (PAIR_USER_CN.CUST_NUM - 55000000)= PAIR_CUSTOMER_OPT.CUSTOMER_NO"
					+ " AND upper(PAIR_USER_DN.dn) = upper('" + distinguishedName_escaped + "')"
 					+ " AND PAIR_USER_DN.delete_flag is null"
 					+ " AND PAIR_USER_CN.delete_flag is null" 
 					+ " AND PAIR_CUSTOMER_OPT.OPTIN_IND = 'EMAIL'"
 					+ " order by pair_user_cn.cust_num";*/
		
    String optedInCustNumWithDNSql = "select CUSTOMER_NO  from (select distinct PAIR_CUSTOMER_OPT.CUSTOMER_NO as CUSTOMER_NO "
    + " from PAIR_USER_DN, PAIR_USER_CN, PAIR_CUSTOMER_OPT"
    + " WHERE PAIR_USER_DN.user_id = PAIR_USER_CN.user_id" 
    + " AND (PAIR_USER_CN.CUST_NUM - 55000000)= PAIR_CUSTOMER_OPT.CUSTOMER_NO"
    + " AND upper(PAIR_USER_DN.dn) = upper('" + distinguishedName_escaped + "')"
    + " AND PAIR_USER_DN.delete_flag is null"
    + " AND PAIR_USER_CN.delete_flag is null" 
    + " AND PAIR_CUSTOMER_OPT.OPTIN_IND = 'EMAIL')"
    + " order by CUSTOMER_NO ";
		
	return optedInCustNumWithDNSql;
	}

    /* The method "getDownloadAppList" is used to retrieve application numbers
     * and related data from the PAIR_CUSTOMERS table.
     * @modified on 6/19/2009 - eOffice Action
     */
    public HashMap getDownloadAppList(String downloadBy, String custNum,String appStatus, int statusDays, String multipleAppId, String sortBy, String sortOrder)
            throws ServiceException {




        HashMap resultHashMap = new HashMap();
        String status="", statusMsg="";
        int recordCount=0;
        List resultRowList = new ArrayList();
        Statement stmt=null;
        ResultSet rs=null;
        String sqlQueryStr = "", sqlCountQuery="";
        Connection dbConnection=null;

        //Initial default sortBy and sortOrder
        if(sortBy.trim().equals(""))
            sortBy="APPL_ID";
        if(sortOrder.trim().equals(""))
            sortOrder="DESC";

        //Formation for count query and actual query.
        String strWhereClause = downloadAppListWhereClause(downloadBy, custNum, appStatus, statusDays, multipleAppId);
        //sqlCountQuery = downloadAppListCountQuery(strWhereClause);
        //	sqlQueryStr = appListByCustNumQuery(CustNum,status,sortBy,sortOrder);
        System.out.println("INFO: CustDetailsDAO.getDownloadAppList(): "+ sqlQueryStr);
        sqlQueryStr = downloadAppListQuery(strWhereClause, sortBy, sortOrder);



        try {
            dbConnection = dataSource.getConnection();
            stmt = dbConnection.createStatement();
            Calendar before = Calendar.getInstance();
            System.out.println("INFO: CustDetailsDAO.getDownloadAppList(): "+ sqlQueryStr);
            rs = stmt.executeQuery(sqlQueryStr);
            Calendar after = Calendar.getInstance();
            System.out.println(" getDownloadAppList query time taken : " + StringUtil.diffDates(before, after));

            if (!rs.next()) {
                resultHashMap.put(PcsEJBConstant.STATUS,PcsEJBConstant.SUCCESS);
                resultHashMap.put(PcsEJBConstant.STATUS_MESSAGE,PcsEJBConstant.NO_REC_FOUND);
                resultHashMap.put(PcsEJBConstant.RECORD_COUNT,new Integer(recordCount));
                resultHashMap.put(PcsEJBConstant.RESULT_LIST, resultRowList);

                return resultHashMap; // return empty vector
            }

            String appNumber = "" + rs.getString(1);
            String patentNo = rs.getString(2);
            String attorneyDocNo = rs.getString(3);
            String earliestPubNo = ((rs.getString(4).trim().length() <= 5)? " " : rs.getString(4));
            String statusNo = rs.getString(5);
            String statusDesc = rs.getString(6);
            String statusDate = rs.getString(7);
            String filingDate = rs.getString(8);

            //Added in PrivatePAIR 7.1
            String customerNumber = rs.getString(9);
            String examinerName = rs.getString(10);
            String gauCd = rs.getString(11);
            String artClassNo = rs.getString(12);
            String artSubClassNo = rs.getString(13);
            String continuityType = rs.getString(14);
            Date actualPubDt = rs.getDate(15);
            Date pcaActionDt = rs.getDate(16);
            String pcaActionDesc = rs.getString(17);
            String efwStatusIn = rs.getString(18);
            if(efwStatusIn!=null && efwStatusIn.trim().equalsIgnoreCase("Y"))
                efwStatusIn="true";
            else
                efwStatusIn="false";
            Date patentIssueDt = rs.getDate(19);
            Timestamp lastModifiedDt = rs.getTimestamp(20);
            String lastModifiedUserId = rs.getString(21);


            AppListByCustNumRow dowloadApp =
                    new AppListByCustNumRow(
                            appNumber,
                            patentNo,
                            attorneyDocNo,
                            earliestPubNo,
                            statusDate,
                            filingDate,
                            efwStatusIn,
                            statusNo,
                            statusDesc, customerNumber, examinerName, gauCd, artClassNo, artSubClassNo, continuityType,
                            actualPubDt, pcaActionDt, pcaActionDesc, patentIssueDt, lastModifiedDt, lastModifiedUserId);
            resultRowList.add(dowloadApp);
            while (rs.next()) {
                appNumber = "" + rs.getString(1);
                patentNo = rs.getString(2);
                attorneyDocNo = rs.getString(3);
                earliestPubNo = ((rs.getString(4).trim().length() <= 5)? " " : rs.getString(4));
                statusNo = rs.getString(5);
                statusDesc = rs.getString(6);
                statusDate = rs.getString(7);
                filingDate = rs.getString(8);
                customerNumber = rs.getString(9);
                examinerName = rs.getString(10);
                gauCd = rs.getString(11);
                artClassNo = rs.getString(12);
                artSubClassNo = rs.getString(13);
                continuityType = rs.getString(14);
                actualPubDt = rs.getDate(15);
                pcaActionDt = rs.getDate(16);
                pcaActionDesc = rs.getString(17);
                efwStatusIn = rs.getString(18);
                if(efwStatusIn!=null && efwStatusIn.trim().equalsIgnoreCase("Y"))
                    efwStatusIn="true";
                else
                    efwStatusIn="false";
                patentIssueDt = rs.getDate(19);
                lastModifiedDt = rs.getTimestamp(20);
                lastModifiedUserId = rs.getString(21);

                dowloadApp =
                        new AppListByCustNumRow(
                                appNumber,
                                patentNo,
                                attorneyDocNo,
                                earliestPubNo,
                                statusDate,
                                filingDate,
                                efwStatusIn,
                                statusNo,
                                statusDesc, customerNumber, examinerName, gauCd, artClassNo, artSubClassNo, continuityType,
                                actualPubDt, pcaActionDt, pcaActionDesc,  patentIssueDt, lastModifiedDt, lastModifiedUserId);

                resultRowList.add(dowloadApp);
            }
            status=PcsEJBConstant.SUCCESS;
            statusMsg=PcsEJBConstant.SUCCESS_RETRIEVE;
        }catch (SQLException sqlE)
        {
            status=PcsEJBConstant.ERROR;
            statusMsg=PcsEJBConstant.PCS_DB_ERROR;
            System.out.println("ERROR: CustdetailsDAO.getAppListByCN() :"+sqlE.getMessage());
            //sqlE.printStackTrace();

            if(sqlE.getErrorCode()==1017 || sqlE.getErrorCode()==03114)
                throw new ServiceException(PcsEJBConstant.DB_CONNECTION_ERROR);
        }
        catch (Exception e) {
            status=PcsEJBConstant.ERROR;
            statusMsg=PcsEJBConstant.PCS_REQUEST_FAILED;
            System.out.println("ERROR: CustdetailsDAO.getAppListByCN() :"+e.getMessage());
            //e.printStackTrace();
        }
        finally {
            getDaoUtil().closeResultSet(rs);
            getDaoUtil().closeStatement(stmt);
            getDaoUtil().closeConnection(dbConnection);
        }
        resultHashMap.put(PcsEJBConstant.STATUS,status);
        resultHashMap.put(PcsEJBConstant.STATUS_MESSAGE,statusMsg);
        resultHashMap.put(PcsEJBConstant.RECORD_COUNT,new Integer(resultRowList.size()));
        resultHashMap.put(PcsEJBConstant.RESULT_LIST, resultRowList);

        return resultHashMap;
    }

    private String downloadAppListWhereClause(String downloadBy, String custNum, String status, int days, String multipleAppId)
    {
        StringBuffer whereClauseBuf = new StringBuffer(" WHERE pc.status_no = pasc.status_no (+) ");
        if(downloadBy.equalsIgnoreCase("CN"))
        {
            //Only one CustomerNumber, Status might be All or Abandoned, Issued, New, Pending
            whereClauseBuf.append(" AND pc.CUSTOMER_NO = ");
            whereClauseBuf.append(custNum);

        }else if(downloadBy.equalsIgnoreCase("CN_STATUS_DT"))
        {

            //Multiple Customers, past status days and status might be All or Abandoned, Issued, New, Pending
            List cnList = StringUtil.getTokenizedList(custNum, ",");
            Iterator itCns = cnList.iterator();
            StringBuffer tempCnBuff = new StringBuffer(" AND pc.CUSTOMER_NO in (");
            while(itCns.hasNext())
            {
                tempCnBuff.append("'"+itCns.next().toString()+"',");
            }

            //Remove the last "," and return the string;
            whereClauseBuf.append(tempCnBuff.substring(0,tempCnBuff.length()-1)).append(")");
            //Append the status date where clause
            whereClauseBuf.append(" AND pc.STATUS_DATE >= (TO_DATE('").append(formatter.format(now.getTime())).append("','MM-DD-YYYY') -");
            whereClauseBuf.append(days).append(")");

        }else
        {

//		Multiple Application Id, Multiple Customer Numbers and status might be All or Abandoned, Issued, New, Pending
            List cnList = StringUtil.getTokenizedList(custNum, ",");
            Iterator itCns = cnList.iterator();
            StringBuffer tempCnBuff = new StringBuffer(" AND pc.CUSTOMER_NO in (");
            while(itCns.hasNext())
            {
                tempCnBuff.append("'"+itCns.next().toString()+"',");
            }

            //Remove the last "," and return the string;
            whereClauseBuf.append(tempCnBuff.substring(0,tempCnBuff.length()-1)).append(")");

            List appList = StringUtil.getTokenizedList(multipleAppId, ",");
            Iterator itApps = appList.iterator();
            StringBuffer tempAppBuff = new StringBuffer(" AND pc.APPL_ID in (");
            while(itApps.hasNext())
            {
                tempAppBuff.append("'"+itApps.next().toString()+"',");
            }

            //Remove the last "," and return the string;
            whereClauseBuf.append(tempAppBuff.substring(0,tempAppBuff.length()-1)).append(")");
        }

        //Status common to all 3 conditions.
	/* Earlier hardcoded status
	if(status!=null &&(status.equalsIgnoreCase("ABANDONED") || status.equalsIgnoreCase("ISSUED") ||
			status.equalsIgnoreCase("NEW") || status.equalsIgnoreCase("PENDING")))
	{
		whereClauseBuf.append(" AND pc.STATUS_GRP_CD='");
		whereClauseBuf.append(status).append("'");
	}*/
        if(status!=null && !status.trim().equals("") && !status.equalsIgnoreCase("ALL"))
        {
            if(status.equalsIgnoreCase("OTHER"))
            {
                whereClauseBuf.append(" AND (STATUS_GRP_CD is null OR STATUS_GRP_CD =' '  OR");
                whereClauseBuf.append(" TRIM(STATUS_GRP_CD) NOT IN (SELECT upper(PROPERTY_KEY_TX) FROM PAIR_PROPERTY WHERE");
                whereClauseBuf.append(" PROPERTY_MODE_TX='PRIVATE' AND PROPERTY_NM='ApplicationStatusType' AND upper(PROPERTY_KEY_TX)!='ALL' AND upper(PROPERTY_KEY_TX)!='OTHER'))");
            }else
            {
                //The selected status to be
                whereClauseBuf.append(" AND STATUS_GRP_CD='");
                whereClauseBuf.append(status).append("'");
            }
        }//if(status!=null && !status.trim().equals("") && !status.equalsIgnoreCase("ALL"))

        return whereClauseBuf.toString();

    }

    private String downloadAppListQuery(String strWhereClause, String sortBy, String sortOrder) {

        StringBuffer downloadAppListSQLBuf = new StringBuffer("SELECT pc.APPL_ID, ");
        //downloadAppListSQLBuf.append(" DECODE((pc.PATENT_ISSUE_DT-SYSDATE)-ABS(pc.PATENT_ISSUE_DT-SYSDATE),0,'',pc.PATENT_NO)PATENT_NO,");
        downloadAppListSQLBuf.append(" DECODE((pc.PATENT_ISSUE_DT-TO_DATE('").append(formatter.format(now.getTime()))
                .append("','MM-DD-YYYY'))-ABS(pc.PATENT_ISSUE_DT-TO_DATE('").append(formatter.format(now.getTime()))
                .append("','MM-DD-YYYY')),0,'',pc.PATENT_NO)PATENT_NO,");
        downloadAppListSQLBuf.append(" pc.ATTY_DKT_NO,");
        downloadAppListSQLBuf.append(" 'US ' ||   NVL(TO_CHAR(pc.PUB_NO_YR),'0') || '-' || NVL(TRIM(pc.PUB_NO_SEQ_NO),' ') || ' ' ||   NVL(TRIM(pc.PUB_NO_KIND_CD),' '),");
        downloadAppListSQLBuf.append(" pc.STATUS_NO, pasc.DESCRIPTION_TX,");
        downloadAppListSQLBuf.append(" DECODE(TO_CHAR(pc.STATUS_DATE,'MM-DD-YYYY'),NULL,'','01-01-0001','',TO_CHAR(pc.STATUS_DATE,'MM-DD-YYYY')),");
        downloadAppListSQLBuf.append(" DECODE(TO_CHAR(pc.FILE_DT,'MM-DD-YYYY'),NULL,'','01-01-0001','',TO_CHAR(pc.FILE_DT,'MM-DD-YYYY')),");
        downloadAppListSQLBuf.append(" pc.CUSTOMER_NO, pc.EXAMINER_NM, pc.GAU_CD, pc.ARTCLS_NO, pc.ARTSUBCLS_NO, pc.CONTINUITY_TYPE, ");
        downloadAppListSQLBuf.append(" pc.PUB_ACTL_PUB_DT, pc.LAST_PCA_ACTION_DT, pc.LAST_PCA_ACTION_DESC, pc.EFW_STATUS_IN,");
        downloadAppListSQLBuf.append(" pc.PATENT_ISSUE_DT, pc.LAST_MODIFIED_TS, pc.LAST_MODIFIED_USR_ID ");
        downloadAppListSQLBuf.append(" FROM PAIR_CUSTOMERS pc, PAIR_APP_STATUS_CODE pasc ");
        downloadAppListSQLBuf.append(strWhereClause);
        downloadAppListSQLBuf.append(" order by ");
        downloadAppListSQLBuf.append(downloadAppListOrderByClause(sortBy,sortOrder));

        return downloadAppListSQLBuf.toString();
    }

    private String downloadAppListOrderByClause(String sortBy, String sortOrder)
    {
        String orderByString;
        if(sortBy.equals("APPL_ID")){
            if(sortOrder.equals("ASC"))
                orderByString = "1 ASC";
            else
                orderByString = "1 DESC";
        }else if(sortBy.equals("PATENT_NO")){
            if(sortOrder.equals("ASC"))
                orderByString = "2 ASC, 1 DESC";
            else
                orderByString = "2 DESC, 1 DESC";
        }
        else if(sortBy.equals("ATTY_DKT_NO")){
            if(sortOrder.equals("ASC"))
                orderByString = "3 ASC, 1 DESC";
            else
                orderByString = "3 DESC, 1 DESC";
        }
        else if(sortBy.equals("PUB_NO_YR")){
            if(sortOrder.equals("ASC"))
                orderByString = "4 ASC, 1 DESC";
            else
                orderByString = "4 DESC, 1 DESC";
        }
        //6 & 7 is status_code and status_description
        else if(sortBy.equals("STATUS_DATE")){
            if(sortOrder.equals("ASC"))
                orderByString = "pc.STATUS_DATE ASC, 1 DESC";
            else
                orderByString = "pc.STATUS_DATE DESC, 1 DESC";
        }
        else if(sortBy.equals("FILE_DT")){
            if(sortOrder.equals("ASC"))
                orderByString = "pc.FILE_DT ASC, 1 DESC";
            else
                orderByString = "pc.FILE_DT DESC, 1 DESC";
        }else if(sortBy.equals("CUSTOMER_NO")){
            orderByString = "pc.CUSTOMER_NO, 1 DESC";
        }
        else
            orderByString = "1, DESC";

        return orderByString;
    }

    /*
     * The method updateAttorneyDocketNumber is used to update attorney docket number in PAIR tables,
     * which inturn call two methods, one to update PAIR_CUSTOMER records and the other to update
     * PAIR_IFW_OUTGOING records.
     * @modified on 6/19/2009 - eOffice Action
     */

    public Map updateAttorneyDocketNumber(String oldAttDktNum, String newAttDktNum, List appList)
            throws ServiceException {
        Map map = new HashMap();
      /*  List appListMain= new ArrayList();
        String[] applicationNumberArray=appList.split(",");
        for(int index=0; index<applicationNumberArray.length;index++){
            appListMain.add(applicationNumberArray[index]);
        }*/
        String customerUpdateStatus = updateCustomerAttorneyDocketNumber(oldAttDktNum,newAttDktNum,appList);
        String outgoingUpdateStatus = updateOutgoingAttorneyDocketNumber(oldAttDktNum,newAttDktNum,appList);
        map.put("CUSTOMER_UPDATE",customerUpdateStatus);
        map.put("OUTGOING_UPDATE",outgoingUpdateStatus);
        return map;//"CUSTOMER_UPDATE:"+customerUpdateStatus +"    OUTGOING_UPDATE:"+ outgoingUpdateStatus;

    }

    /*
     * The method "updateCustomerAttorneyDocketNumber" is used to update the attorney dockety column of PAIR_CUSTOMERS
     * table with the new value to the list application records passed.
     * @modified on 6/19/2009 - eOffice Action
     */

    private String updateCustomerAttorneyDocketNumber(String oldAttDktNum, String newAttDktNum, List appList)
            throws ServiceException {

        StringBuffer sqlCustAttorDktUpdateQueryStr =
                new StringBuffer("update PAIR_CUSTOMERS set ATTY_DKT_NO=? where ATTY_DKT_NO=? and APPL_ID in (");
        PreparedStatement p_stmt=null;
        String updateFlag="SUCCESS";
        int updateCount=0;
        Connection dbConnection=null;

        try {

            //Constructing the in clause string

            Iterator itApps = appList.iterator();
            while(itApps.hasNext())
            {
                sqlCustAttorDktUpdateQueryStr.append("'"+itApps.next().toString()+"',");
            }
            sqlCustAttorDktUpdateQueryStr = new StringBuffer(sqlCustAttorDktUpdateQueryStr.substring(0,sqlCustAttorDktUpdateQueryStr.length()-1));
            sqlCustAttorDktUpdateQueryStr.append(")");

            dbConnection = dataSource.getConnection();
            p_stmt = dbConnection.prepareStatement(sqlCustAttorDktUpdateQueryStr.toString());
            p_stmt.setString(1,newAttDktNum);
            p_stmt.setString(2,oldAttDktNum);


            Date beforeDt = new Date();
            updateCount = p_stmt.executeUpdate();
            Date afterDt = new Date();

            long executionTime = afterDt.getTime() - beforeDt.getTime();
            System.out.println("PAIR_CUSTOMERS AttorneyUpdate execution time:"+(executionTime/1000)+":"+executionTime%1000+" seconds");

            if(updateCount <= 0)
                updateFlag = "NO RECORDS FOUND TO UPDATE";

            System.out.println("INFO: CustdetailsDAO.updateCustomerAttorneyDocketNumber() - PrepareStatement:"+sqlCustAttorDktUpdateQueryStr.toString());
            System.out.println("INFO: CustdetailsDAO.updateCustomerAttorneyDocketNumber() - OldAttDktNum:"+oldAttDktNum+", NewAttDktNum:"+newAttDktNum);
            System.out.println("INFO: CustdetailsDAO.updateCustomerAttorneyDocketNumber() - Number of records updated:"+updateCount);

        }catch (SQLException sqlE)
        {
            updateFlag = "ERROR UPDATING: "+sqlE.getLocalizedMessage();
            System.out.println("ERROR: CustdetailsDAO.updateCustomerAttorneyDocketNumber() :"+sqlE.getMessage());
            if(sqlE.getErrorCode()==1017 || sqlE.getErrorCode()==03114)
            {
                updateFlag = PcsEJBConstant.DB_CONNECTION_ERROR;
                throw new ServiceException(PcsEJBConstant.DB_CONNECTION_ERROR);
            }
        }
        catch (Exception e) {
            updateFlag = "ERROR UPDATING: "+e.getLocalizedMessage();
            System.out.println("ERROR: CustdetailsDAO.updateCustomerAttorneyDocketNumber() :"+e.getMessage());
        }
        finally {
            getDaoUtil().closeStatement(p_stmt);
            getDaoUtil().closeConnection(dbConnection);
        } //finally

        return updateFlag;

    }// updateAttorneyDocketNumber()

    /*
     * The method "updateOutgoingAttorneyDocketNumber" is used to update the attorney dockety column of PAIR_IFW_OUTGOING
     * table with the new value to the list application records passed.
     * @modified on 6/19/2009 - eOffice Action
     */

    private String updateOutgoingAttorneyDocketNumber(String oldAttDktNum, String newAttDktNum, List appList)
            throws ServiceException {

        StringBuffer sqlOutgoingAttorDktUpdateQueryStr =
                new StringBuffer("update PAIR_IFW_OUTGOING set ATTY_DKT_NO=?, LAST_MODIFIED_TS=sysdate where ATTY_DKT_NO=? and APPL_ID in (");
        PreparedStatement p_stmt=null;
        String updateFlag="SUCCESS";
        int updateCount=0;
        Connection dbConnection=null;

        try {

            //Constructing the in clause string

            Iterator itApps = appList.iterator();
            while(itApps.hasNext())
            {
                sqlOutgoingAttorDktUpdateQueryStr.append("'"+itApps.next().toString()+"',");
            }
            sqlOutgoingAttorDktUpdateQueryStr = new StringBuffer(sqlOutgoingAttorDktUpdateQueryStr.substring(0,sqlOutgoingAttorDktUpdateQueryStr.length()-1));
            sqlOutgoingAttorDktUpdateQueryStr.append(")");

            dbConnection = dataSource.getConnection();
            p_stmt = dbConnection.prepareStatement(sqlOutgoingAttorDktUpdateQueryStr.toString());
            p_stmt.setString(1,newAttDktNum);
            p_stmt.setString(2,oldAttDktNum);


            Date beforeDt = new Date();
            updateCount = p_stmt.executeUpdate();
            Date afterDt = new Date();

            long executionTime = afterDt.getTime() - beforeDt.getTime();
            System.out.println("PAIR_IFW_OUTGOING AttorneyUpdate execution time:"+(executionTime/1000)+":"+executionTime%1000+" seconds");

            if(updateCount <= 0)
                updateFlag = "NO RECORDS FOUND TO UPDATE";

            System.out.println("INFO: CustdetailsDAO.updateOutgoingAttorneyDocketNumber() - PrepareStatement:"+sqlOutgoingAttorDktUpdateQueryStr.toString());
            System.out.println("INFO: CustdetailsDAO.updateOutgoingAttorneyDocketNumber() - OldAttDktNum:"+oldAttDktNum+", NewAttDktNum:"+newAttDktNum);
            System.out.println("INFO: CustdetailsDAO.updateOutgoingAttorneyDocketNumber() - Number of records updated:"+updateCount);

        }catch (SQLException sqlE)
        {
            updateFlag = "ERROR UPDATING: "+sqlE.getLocalizedMessage();
            System.out.println("ERROR: CustdetailsDAO.updateOutgoingAttorneyDocketNumber() :"+sqlE.getMessage());
            if(sqlE.getErrorCode()==1017 || sqlE.getErrorCode()==03114)
            {
                updateFlag = PcsEJBConstant.DB_CONNECTION_ERROR;
                throw new ServiceException(PcsEJBConstant.DB_CONNECTION_ERROR);
            }
        }
        catch (Exception e) {
            updateFlag = "ERROR UPDATING: "+e.getLocalizedMessage();
            System.out.println("ERROR: CustdetailsDAO.updateOutgoingAttorneyDocketNumber() :"+e.getMessage());
        }
        finally {
            getDaoUtil().closeStatement(p_stmt);
            getDaoUtil().closeConnection(dbConnection);
        } //finally

        return updateFlag;

    }// updateOutgoingAttorneyDocketNumber()
    
    
    public String outGoingCorrByAppId(String appId,String user) {
    	List<IfwOutgoingVO> displayRowList = new ArrayList();
    	Statement stmt=null;
		ResultSet rs =null;
		Connection dbConnection=null;
      Calendar now = Calendar.getInstance();
        
        //Replaced the sysdate with formated date.
        StringBuffer outGoingCorrListByAppIdSqlBuf = new StringBuffer("SELECT ifw.APPL_ID, ");
        outGoingCorrListByAppIdSqlBuf.append(" DECODE((ifw.PATENT_ISSUE_DT-TO_DATE('").append(formatter.format(now.getTime())).append("','MM-DD-YYYY'))-ABS(ifw.PATENT_ISSUE_DT-TO_DATE('").append(formatter.format(now.getTime())).append("','MM-DD-YYYY')),0,'-',NVL(TRIM(ifw.PATENT_NO),'-'))PATENT_NO, ");
        outGoingCorrListByAppIdSqlBuf.append(" DECODE(TO_CHAR(ifw.MAIL_ROOM_DT,'MM-dd-yyyy'),'01-01-0001','-',NULL,'-', TO_CHAR(ifw.MAIL_ROOM_DT,'MM-dd-yyyy')), ");
        outGoingCorrListByAppIdSqlBuf.append(" NVL(ifw.FILE_WRAPPER_PACKAGE_ID,'-'), ");
        outGoingCorrListByAppIdSqlBuf.append(" NVL(TRIM(ifw.DOC_DESCRIPTION_TX),'-'), ");
        outGoingCorrListByAppIdSqlBuf.append(" NVL(ifw.START_PAGE_NO,0), ");
        outGoingCorrListByAppIdSqlBuf.append(" NVL(ifw.PAGE_COUNT,0),NVL(OFFSET,0), ");
        outGoingCorrListByAppIdSqlBuf.append(" NVL(TRIM(ifw.ATTY_DKT_NO),'-'), ");
        outGoingCorrListByAppIdSqlBuf.append(" DECODE(TO_CHAR(ifw.UPLOAD_DT,'YYYYMMDD'),'00010101','-',NULL,'-', TO_CHAR(ifw.UPLOAD_DT,'YYYYMMDD')), ");
        outGoingCorrListByAppIdSqlBuf.append(" DECODE(TO_CHAR(ifw.DOC_VIEW_DT,'YYYYMMDD'),'00010101','-',NULL,'-', TO_CHAR(ifw.DOC_VIEW_DT,'YYYYMMDD')), ");
        outGoingCorrListByAppIdSqlBuf.append(" ifw.CUSTOMER_NO, ifw.DOC_CATEGORY, ifw.DOCUMENT_CD, ifw.LAST_MODIFIED_TS, pc.FILE_DT, ");
        outGoingCorrListByAppIdSqlBuf.append(" pc.PUB_ACTL_PUB_DT, pc.PUB_NO_KIND_CD, pc.PUB_NO_SEQ_NO, pc.PUB_NO_YR, pc.STATUS_GRP_CD,ifw.DOC_ID, ");
          //Added by dthakkar for DR # 33 - Add Docketed Check Box on OCN Tab
        outGoingCorrListByAppIdSqlBuf.append(" ifw.DOCKETED, ");
        outGoingCorrListByAppIdSqlBuf.append(" ifw.DA_ID, ");
        outGoingCorrListByAppIdSqlBuf.append(" NVL(ifw.DOC_VIEWER_NM,'-') "); //added by MM DR 24 5/10/2012   *** To track this column search for docViewBy   ****
        outGoingCorrListByAppIdSqlBuf.append(" FROM PAIR_IFW_OUTGOING ifw, PAIR_CUSTOMERS pc ");
        outGoingCorrListByAppIdSqlBuf.append(" WHERE ifw.FK_PC_ID=pc.FK_PC_ID AND ifw.APPL_ID='" +appId+"'");
       
		try {
			dbConnection = dataSource.getConnection();
			stmt = dbConnection.createStatement();
			System.out.println(outGoingCorrListByAppIdSqlBuf.toString());
			 rs =  stmt.executeQuery(outGoingCorrListByAppIdSqlBuf.toString());
			while (rs.next())
			{
				System.out.println(rs.getString(25));
				System.out.println(rs.getString(25).equalsIgnoreCase("-"));
				
				if(rs.getString(25).equalsIgnoreCase("-")) {
				IfwOutgoingVO outGoingCorrByCustNum = new IfwOutgoingVO(rs.getString(1).trim(), rs.getString(2), rs.getString(3),
                            rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
                            rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14), rs.getTimestamp(15).toString(), rs.getTimestamp(16).toString(),
                            rs.getTimestamp(17).getTime(), rs.getString(18), rs.getString(19), rs.getString(20), rs.getString(21), rs.getString(22),
                            rs.getString(23), rs.getString(24), user);

                    displayRowList.add(outGoingCorrByCustNum);
				return updateOutGoingCorrViewStatus(displayRowList);
				}
			}
			
	    }
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally {
			getDaoUtil().closeResultSet(rs);
			 getDaoUtil().closeStatement(stmt);
	            getDaoUtil().closeConnection(dbConnection);
			
		} //finally
		return null;
    }

   
    
    
    
    
    
    
    
    
    /* The method "updateOutGoingCorrViewStatus" is used to update the Doc View Date and DOC_VIEWER_NM(User Name)columns 
	 * in the PAIR_IFW_OUTGOING table
	 * with the current date when the customer views that particular record in Private PAIR portal.
     * @modified on 6/19/2009 - eOffice Action
     * MM DR24 May2012 Added Common User Name (DOC_VIEWER_NM) who viewed record first in addition to Doc_view_dt.   *** To track this column search for docViewBy   ****
	 */
	public String updateOutGoingCorrViewStatus(List ifwOutgoingList) throws ServiceException {
		System.out.println("ifwOutgoingList"+ifwOutgoingList);
		String sqlUpdateQueryStr = "";
		String sqlUpdateQueryStrEOA = "";
		PreparedStatement p_stmt=null;
		PreparedStatement p_stmt_eoa =null;
		Connection dbConnection=null;
		String updateFlag="NO RECORDS FOUND TO UPDATE";
		int updateCount=0;
		int updateCountEoa=0;
		
		sqlUpdateQueryStr = "update PAIR_IFW_OUTGOING set DOC_VIEW_DT=sysdate, LAST_MODIFIED_TS=sysdate, DOC_VIEWER_NM=? where CUSTOMER_NO=? and "
			+ "DOC_VIEW_DT is null and RTRIM(APPL_ID) = ? AND RTRIM(FILE_WRAPPER_PACKAGE_ID) = ? AND MAIL_ROOM_DT = ?";

		sqlUpdateQueryStrEOA = "update STG_PAIR_IFW_OUTGOING_EOA set DOC_VIEW_DT=sysdate, LAST_MODIFIED_TS=sysdate, DOC_VIEWER_NM=? where CUSTOMER_NO=? and "
			+ "DOC_VIEW_DT is null and RTRIM(APPL_ID) = ? AND RTRIM(FILE_WRAPPER_PACKAGE_ID) = ? AND MAIL_ROOM_DT = ?";
		
		System.out.println("INFO: CustdetailsDAO.updateOutGoingCorrViewStatus() - Update Query: "+sqlUpdateQueryStr);
		System.out.println("INFO: CustdetailsDAO.updateOutGoingCorrViewStatus() - Update Query EOA: "+sqlUpdateQueryStrEOA);
		try {
			
			dbConnection = dataSource.getConnection();
			p_stmt = dbConnection.prepareStatement(sqlUpdateQueryStr);
			p_stmt_eoa = dbConnection.prepareStatement(sqlUpdateQueryStrEOA);
			
			SimpleDateFormat df = new SimpleDateFormat("MM-dd-yyyy");
			int iCustNum=0;
			Iterator itIfwOutgoing = ifwOutgoingList.iterator();
			while(itIfwOutgoing.hasNext())
			{
				
				IfwOutgoingVO ifwOutgoing = (IfwOutgoingVO)itIfwOutgoing.next();
				//System.out.println("CustNum: "+ifwOutgoing.getCustNumber());
				//System.out.println("DocId: " +ifwOutgoing.getDocumentId());
				//System.out.println("App Num: "+ ifwOutgoing.getApplicationNumber());
				//System.out.println("DocViewBy: " +ifwOutgoing.getDocViewBy());
				if(ifwOutgoing.getCustNum()!=null && !ifwOutgoing.getCustNum().trim().equals(""))
					iCustNum = Integer.parseInt(ifwOutgoing.getCustNum());

				p_stmt.setString(1, ifwOutgoing.getDocViewBy().trim().length()> 50 ? ifwOutgoing.getDocViewBy().trim().substring(0,49) : ifwOutgoing.getDocViewBy().trim());
				p_stmt.setInt(2,iCustNum);
				p_stmt.setString(3, ifwOutgoing.getAppNumber());
				p_stmt.setString(4, ifwOutgoing.getDocID());
				System.out.println("getMailRoomDate"+ifwOutgoing.getMailRoomDate());
				java.util.Date mailRoomDt = df.parse(ifwOutgoing.getMailRoomDate());
				java.sql.Date sqlMailRoomDt = new java.sql.Date(mailRoomDt.getTime());
				p_stmt.setDate(5,sqlMailRoomDt);
				
				p_stmt_eoa.setString(1, ifwOutgoing.getDocViewBy().trim().length()> 50 ? ifwOutgoing.getDocViewBy().trim().substring(0,49) : ifwOutgoing.getDocViewBy().trim());
				p_stmt_eoa.setInt(2,iCustNum);
				p_stmt_eoa.setString(3, ifwOutgoing.getAppNumber());
				p_stmt_eoa.setString(4, ifwOutgoing.getDocID());
				p_stmt_eoa.setDate(5,sqlMailRoomDt);
				
				updateCount = p_stmt.executeUpdate();

				updateCountEoa = p_stmt_eoa.executeUpdate();
				
				if(updateCount >= 1 || updateCountEoa >= 1)
					updateFlag = "SUCCESS";
				fetchAndUpdateElectrnDocStagingTable(ifwOutgoing);
				System.out.println("INFO: CustdetailsDAO.updateOutGoingCorrViewStatus() - CN:"+iCustNum+", ApplId:"+ifwOutgoing.getAppNumber()+", DocId:"+ifwOutgoing.getDocID()+", mailRoomDt:"+sqlMailRoomDt.toString());
				System.out.println("INFO: CustdetailsDAO.updateOutGoingCorrViewStatus() - Number of records updated in PAIR_IFW_OUTGOING:"+updateCount);
				System.out.println("INFO: CustdetailsDAO.updateOutGoingCorrViewStatus() - Number of records updated in STG_PAIR_IFW_OUTGOING_EOA: "+updateCountEoa);
			}
			}
			catch (SQLException sqlE)
			{
				updateFlag = "ERROR UPDATING SQL: "+sqlE.getLocalizedMessage();
				System.out.println("SQL ERROR: CustdetailsDAO.updateOutGoingCorrViewStatus() :"+sqlE.getMessage());
				if(sqlE.getErrorCode()==1017 || sqlE.getErrorCode()==03114)
				{
					updateFlag = PcsEJBConstant.DB_CONNECTION_ERROR;
					throw new ServiceException(PcsEJBConstant.DB_CONNECTION_ERROR);
				}
			}
			catch (Exception e) 
			{
				updateFlag = "ERROR UPDATING: "+e.getLocalizedMessage();
				System.out.println("Other ERROR: CustdetailsDAO.updateOutGoingCorrViewStatus() :"+e.getMessage());
			}
			finally 
			{
				 getDaoUtil().closeStatement(p_stmt);
				 getDaoUtil().closeStatement(p_stmt_eoa);
		            getDaoUtil().closeConnection(dbConnection);
				
			} //finally

			return updateFlag;
	}// updateOutGoingCorrViewStatus()
	
	/**
	 * This methos is for fetching  the unique  document form ifw table for updating  Staging table .
	 * @param ifwOutgoing
	 * @modified on 6/19/2009 - eOffice Action
	 */

	public void fetchAndUpdateElectrnDocStagingTable(IfwOutgoingVO ifwOutgoing)
		{
		Statement stmt=null;
		ResultSet rst =null;
		Connection dbConnection=null;
		String queryString ="select appl_id,to_char(UPLOAD_DT, 'mm/dd/yyyy') UPLOAD_DT,fk_pc_id,to_char(mail_room_dt, 'mm/dd/yyyy')mail_room_dt," +
							"page_count,offset,document_cd,doc_id,"+
					"FILE_WRAPPER_PACKAGE_ID  from pair_ifw_outgoing ifw where "+
					" RTRIM(APPL_ID) = '"+ifwOutgoing.getAppNumber()+"' "+
					"AND RTRIM(FILE_WRAPPER_PACKAGE_ID) ='"+ifwOutgoing.getDocID()+"'"+
					" AND customer_no ="+ifwOutgoing.getCustNum()+""+
					" AND MAIL_ROOM_DT = to_date('"+ifwOutgoing.getMailRoomDate()+"','MM/dd/yyyy')";
					 System.out.println(" CustDetailsDAO.fetchAndUpdateElectrnDocStagingTable() queryString: "+queryString);

				try {
					dbConnection = dataSource.getConnection();
					stmt = dbConnection.createStatement();
					 rst =  stmt.executeQuery(queryString);
					while (rst.next())
					{
					ElectrnDocStagingTableVO elctrnVO = new ElectrnDocStagingTableVO();
					elctrnVO.setFkPcId(rst.getString("fk_pc_id"));
					elctrnVO.setDocumentCode(rst.getString("document_cd").toString().trim());
					elctrnVO.setMailRmDte(rst.getString("mail_room_dt"));
					elctrnVO.setUploadDt(rst.getString("UPLOAD_DT"));
					elctrnVO.setPackageId(rst.getString("FILE_WRAPPER_PACKAGE_ID").toString().trim());
					elctrnVO.setDocumentId(rst.getString("doc_id"));
					elctrnVO.setOffset(rst.getString("offset"));
					elctrnVO.setPageCount(rst.getString("page_count"));
					String updateflag=	UpdateReviewDtInElctrnDocStgngTable(elctrnVO);
					}
			    }
				catch(Exception e)
				{
					e.printStackTrace();
				}
				finally {
					getDaoUtil().closeResultSet(rst);
					 getDaoUtil().closeStatement(stmt);
			            getDaoUtil().closeConnection(dbConnection);
					
				} //finally
			}
	
	/**
	 *
	 * @param elctrVO
	 * @return
	 * @throws ServiceException
    * @modified on 6/19/2009 - eOffice Action
	 */
public String UpdateReviewDtInElctrnDocStgngTable(ElectrnDocStagingTableVO elctrVO) throws ServiceException {

	String sqlUpdateQueryStr = "";
	String sqlUpdateQueryStrEOA = "";
	PreparedStatement p_stmt=null;
	PreparedStatement p_stmtEOA=null;
	Connection dbConnection=null;
	String updateFlag="NO RECORDS FOUND TO UPDATE";
	int updateCount=0;
	int updateCountEOA=0;
	
	sqlUpdateQueryStr = "update ELCTRN_DOC_SCAN_LOG set REVIEW_DT=sysdate,last_mod_ts=sysdate where FK_EOCL_APC_FK_PC_ID=? and "
		    + " REVIEW_DT is null and FK_EOCL_IMAGE_UPLOAD_DT = to_date('"+elctrVO.getUploadDt()+"','MM/dd/yyyy') AND DN_IFW_PKG_ID=? " 
			+ " AND mailroom_dt = to_date('"+elctrVO.getMailRmDte()+"','MM/dd/yyyy')  AND OFFSET_NO="
		    + Integer.parseInt(elctrVO.getOffset())+" AND PAGE_CNT_QT="+Integer.parseInt(elctrVO.getPageCount());

	sqlUpdateQueryStrEOA = "update ELCTRN_DOC_SCAN_LOG_EOA set REVIEW_DT=sysdate,last_mod_ts=sysdate where FK_EOCL_APC_FK_PC_ID=? and "
			+ " REVIEW_DT is null and FK_EOCL_IMAGE_UPLOAD_DT = to_date('"+elctrVO.getUploadDt()+"','MM/dd/yyyy') AND DN_IFW_PKG_ID=? " 
			+ " AND mailroom_dt = to_date('"+elctrVO.getMailRmDte()+"','MM/dd/yyyy')  AND OFFSET_NO="
			+ Integer.parseInt(elctrVO.getOffset())+" AND PAGE_CNT_QT="+Integer.parseInt(elctrVO.getPageCount());

	System.out.println("INFO: CustdetailsDAO.UpdateReviewDtInElctrnDocStgngTable() - Update Query: "+sqlUpdateQueryStr);
	System.out.println("INFO: CustdetailsDAO.UpdateReviewDtInElctrnDocStgngTable() - Update QueryEOA: "+sqlUpdateQueryStrEOA);
	try {
		dbConnection = dataSource.getConnection();
		p_stmt = dbConnection.prepareStatement(sqlUpdateQueryStr);
		p_stmtEOA = dbConnection.prepareStatement(sqlUpdateQueryStrEOA);
		
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");

			p_stmt.setInt(1,Integer.parseInt(elctrVO.getFkPcId()));
			p_stmt.setString(2, elctrVO.getPackageId().trim());
						
			p_stmtEOA.setInt(1,Integer.parseInt(elctrVO.getFkPcId()));
			p_stmtEOA.setString(2, elctrVO.getPackageId().trim());

			updateCount = p_stmt.executeUpdate();
			updateCountEOA = p_stmtEOA.executeUpdate();

			if(updateCount >= 1 ||updateCountEOA >= 1  )
				updateFlag = "SUCCESS";

			System.out.println("INFO: CustdetailsDAO.UpdateReviewDtInElctrnDocStgngTable() -  FK_PC_ID: "
			                 + elctrVO.getFkPcId()+", PackageId:"+elctrVO.getPackageId()+", mailRoomDt: " 
					         + elctrVO.getMailRmDte().toString()+"PAGECOUNT "+elctrVO.getPageCount());
			System.out.println("INFO: CustdetailsDAO.UpdateReviewDtInElctrnDocStgngTable() -  UPLOAD DATE: "
			                 + elctrVO.getUploadDt()+", Document CODE:"+elctrVO.getDocumentCode()+", doc_id: "
					         + elctrVO.getDocumentId()+" OFFSET: "+elctrVO.getOffset());
			System.out.println("INFO: CustdetailsDAO.UpdateReviewDtInElctrnDocStgngTable() "
					         + "- Number of records updated in ELCTRN_DOC_SCAN_LOG: "+updateCount);
			System.out.println("INFO: CustdetailsDAO.UpdateReviewDtInElctrnDocStgngTable() "
					         + "- Number of records updated in ELCTRN_DOC_SCAN_LOG_EOA: "+updateCountEOA);

	}
	catch (SQLException sqlE)
	{
		updateFlag = "ERROR UPDATING: "+sqlE.getLocalizedMessage();
		 System.out.println("ERROR: CustdetailsDAO.UpdateReviewDtInElctrnDocStgngTable() :"+sqlE.getMessage());
		 if(sqlE.getErrorCode()==1017 || sqlE.getErrorCode()==03114)
		 {
		 	updateFlag = PcsEJBConstant.DB_CONNECTION_ERROR;
			throw new ServiceException(PcsEJBConstant.DB_CONNECTION_ERROR);
		 }
	}
	 catch (Exception e) {
	 	updateFlag = "ERROR UPDATING: "+e.getLocalizedMessage();
		System.out.println("ERROR: CustdetailsDAO.UpdateReviewDtInElctrnDocStgngTable() :"+e.getMessage());
	}
	finally {
		getDaoUtil().closeStatement(p_stmtEOA);
		 getDaoUtil().closeStatement(p_stmt);
         getDaoUtil().closeConnection(dbConnection);
	} //finally

	return updateFlag;

}// updateOutGoingCorrViewStatus()
}
