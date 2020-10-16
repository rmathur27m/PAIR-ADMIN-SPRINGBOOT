package gov.uspto.patent.privatePair.admin.dao;

import com.google.gson.Gson;
import gov.uspto.patent.privatePair.admin.domain.CustDataInfo;
import gov.uspto.patent.privatePair.utils.PcsEJBConstant;
import gov.uspto.patent.privatePair.utils.ServiceException;
import gov.uspto.patent.privatePair.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
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
public class ViewCustomerNoDetailsDAO {
	Gson gson = new Gson();
    @Autowired
    DataSource dataSource;

	/**
	 * The method "getLastPrivatePairUpdate" is used to retrieve the
	 * date time stamp of last updation of Customer correspondence
	 * details through Private PAIR portal.
	 *
	 * @param int		Customer number
	 * @return Last update Date timestamp list.
	 * @exception ServiceException
	 * @since JKD 1.4
     * @modified on 6/19/2009 - eOffice Action
	 */
	public List getLastPrivatePairUpdate(int custNum) throws ServiceException {

		List resultList = new ArrayList();
		String sqlQueryStr = "";
		PreparedStatement p_stmt=null;
		ResultSet rs=null;
		Connection dbConnection=null;
		
		//sqlQueryStr = "select max(CHANGE_REQ_SUBMIT_DT) from PAIR_CUSTOMER_ADDRESS where CUSTOMER_NO=?";
		sqlQueryStr = "select max(DATE_RESPONDED) from PAIR_CUSTOMER_ADDRESS where CUSTOMER_NO=?";
		
		try {

			dbConnection = dataSource.getConnection();
		
			p_stmt = dbConnection.prepareStatement(sqlQueryStr);
			p_stmt.setInt(1,custNum);
			rs = p_stmt.executeQuery();
			while(rs.next()){
				resultList.add((Date)rs.getTimestamp(1));
			}

		}catch (SQLException sqlE)
		{
			 System.out.println("ERROR: CustdetailsDAO.getLastPrivatePairUpdate() :"+sqlE.getMessage());
			 if(sqlE.getErrorCode()==1017 || sqlE.getErrorCode()==03114)
				throw new ServiceException(PcsEJBConstant.DB_CONNECTION_ERROR);
		}
		 catch (Exception e) {
			System.out.println("ERROR: CustdetailsDAO.getLastPrivatePairUpdate() :"+e.getMessage());
		}
		finally {
			 getDaoUtil().closeResultSet(rs);
	         getDaoUtil().closeStatement(p_stmt);
	         getDaoUtil().closeConnection(dbConnection);
		} //finally

		return resultList;

	}// getLastPrivatePairUpdate()
	
	
	/**
	 * The method "getOptedInDetails" is used to retrieve the
	 * date time stamp and other details of opted option of Customer correspondence
	 * details through Private PAIR portal.
	 *
	
	 */
	public List getOptedInDetails(int custNum) throws ServiceException {

		List resultList = new ArrayList();
		String sqlQueryStr = "";
		PreparedStatement p_stmt=null;
		ResultSet rs=null;
		Connection dbConnection=null;
		
		
		sqlQueryStr = "select last_modified_ts from PAIR_CUSTOMER_OPT where CUSTOMER_NO=?";
		
		try {


			dbConnection = dataSource.getConnection();
		
			p_stmt = dbConnection.prepareStatement(sqlQueryStr);
			p_stmt.setInt(1,custNum);
			rs = p_stmt.executeQuery();
			while(rs.next()){
				resultList.add((Date)rs.getTimestamp(1));
			}
			if(!rs.next()) {
				resultList.add(null);
			}
		}catch (SQLException sqlE)
		{
			 System.out.println("ERROR: CustdetailsDAO.getLastPrivatePairUpdate() :"+sqlE.getMessage());
			 if(sqlE.getErrorCode()==1017 || sqlE.getErrorCode()==03114)
				throw new ServiceException(PcsEJBConstant.DB_CONNECTION_ERROR);
		}
		 catch (Exception e) {
			System.out.println("ERROR: CustdetailsDAO.getLastPrivatePairUpdate() :"+e.getMessage());
		}
		finally {
			 getDaoUtil().closeResultSet(rs);
	         getDaoUtil().closeStatement(p_stmt);
	         getDaoUtil().closeConnection(dbConnection);
		} //finally

		return resultList;

	}
	
	
	
	
	/** This method insert or update the PURM Association - DN to CN
	 * @param custNum
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	public List insertOrUpdateAssociationDNtoCN(String custNum, ArrayList userId) throws ServiceException, SQLException {
		List returnStatusList = new ArrayList();
		PreparedStatement stmt =null;
		PreparedStatement stmt1 = null;
		PreparedStatement stmt2 = null;
		//ResultSet rs = null;
		//ResultSet rs1 = null;	
		Connection dbConnection=null;
		String status="", message="";	
		String sqlCNSelectStr="";
		boolean result = false;

		
		System.out.println("userId size "+userId.size());

		try {
			dbConnection = dataSource.getConnection();
			for (Object o : userId) {
				sqlCNSelectStr = selectCNQueryString(custNum, o.toString());
				System.out.println("sqlCNSelectStr" + sqlCNSelectStr);
				try(PreparedStatement stmt00 = dbConnection.prepareStatement(sqlCNSelectStr);
						ResultSet rs = stmt00.executeQuery();){

				if (rs.next()) {
					System.out.println("updating pair_user_cn table");
					//updating pair_user_cn table
					String updateCNExistQueryString = updateCNExistQueryString(custNum, o.toString());
					System.out.println("SQL11 is " + updateCNExistQueryString);
					try(PreparedStatement stmt10 = dbConnection.prepareStatement(updateCNExistQueryString);){
						stmt10.execute();
						status = "SUCCESS";
						message = "Successfully Updated Association between Attorney DN(Distinguish Name) to CN(Customer Number)";}
				} else {
					System.out.println("inserting into pair_user_cn table");
					//inserting into pair_user_cn table
					String selectDNQuery = selectDNQueryString(custNum, o.toString());
					System.out.println("selectDNQuery" + selectDNQuery);
					try(PreparedStatement stmt11 = dbConnection.prepareStatement(selectDNQuery);
							ResultSet rs1 = stmt11.executeQuery();){
					
					if (rs1.next()) {
						String insertCNQueryString = insertCNQueryString(custNum, o.toString(), rs1.getString("USER_ID"), rs1.getString("SYSADMINID"));
						System.out.println("SQL13 is " + insertCNQueryString);
						try(PreparedStatement stmt21 = dbConnection.prepareStatement(insertCNQueryString);){
							stmt21.execute();
						status = "SUCCESS";
						message = "Successfully Associated Attorney DN(Distinguish Name) to CN(Customer Number)";
						}
					}}
				}
			}
			}}catch(SQLException sqlE) {
			System.out.println("ERROR: CustdetailsDAO.inserOrUpdateCustNumQuery() :" + sqlE.getMessage());
			//sqlE.printStackTrace();

			if (sqlE.getErrorCode() == 1017 || sqlE.getErrorCode() == 1612)
				throw new ServiceException(PcsEJBConstant.DB_CONNECTION_ERROR);
			status = "FAILURE";
			message = sqlE.getMessage();
		} finally {
			
			 //getDaoUtil().closeResultSet(rs);
			 //getDaoUtil().closeResultSet(rs1);
			 getDaoUtil().closeStatement(stmt);
			 getDaoUtil().closeStatement(stmt1);
			 getDaoUtil().closeStatement(stmt2);		
			 getDaoUtil().closeConnection(dbConnection);
		}
		returnStatusList.add(0,status);
		returnStatusList.add(1,message);
		return returnStatusList;
	}
	
	
	public List deleteCustNumQuery(String custNum, ArrayList userId)throws ServiceException
	{
		List returnStatusList = new ArrayList();
		Statement stmt =null;
		Connection dbConnection=null;
		String status="SUCCESS", message="Successfully disassociated Attorney DN(Distinguish Name) from CN(Customer Number).";
		String sqlQueryStr="";
		//boolean processedRecord=false;
		//int processRecCount=0;
		try
		{
			dbConnection = dataSource.getConnection();

			String commonName = null;
			stmt = dbConnection.createStatement();
			for(int i=0;i<userId.size();i++)
			{
				/*if(userId.get(i).toString().trim().equals(""))
				{
				    System.out.println("INFO: CustdetailsDAO.deleteCustNumQuery() - User did not select the PURM user of the deleted attorney to disassociate from CN."+i);
					continue;
				}*/

				//Record processed
				//processedRecord=true;
				//processRecCount++;
				sqlQueryStr=createqueryString(custNum,userId.get(i).toString());
				System.out.println("INFO: CustdetailsDAO.deleteCustNumQuery() UPDATE QUERY for "+i+" :"+sqlQueryStr);
				stmt.addBatch(sqlQueryStr);

			}
			int resultCounts[]=stmt.executeBatch();

			for(int j=0;j<resultCounts.length;j++)
			{
				//If the update of any one record has not occured then status is returned as FAILURE.
				if(resultCounts[j] < 1)
				{
					status="FAILURE";
					message="Some of the disassociated Attoreny DN(Distinguish Name) was not found.";
				}
				System.out.println("INFO: CustdetailsDAO.deleteCustNumQuery() UPDATE QUERY RESULT COUNT:"+resultCounts[j]);
			}

			/*//If atlease one record is processed change the return message.
			if(processedRecord)
				message="Successfully disassociated CN from DN.";

			//If any one record fails during updation, the status is returned as FAILED.
			if(processRecCount!=resultCounts.length)
			{
				status="FAILURE";
				message="Some of the disassociated Attoreny DN(Distinguish Name) was not found.";
			}
			*/

			System.out.println("INFO: CustdetailsDAO.deleteCustNumQuery() STATUS: "+ status);
			System.out.println("INFO: CustdetailsDAO.deleteCustNumQuery() MESSAGE: "+ message);

		}catch (SQLException sqlE)
		{
			System.out.println("ERROR: CustdetailsDAO.deleteCustNumQuery() :"+sqlE.getMessage());
			 //sqlE.printStackTrace();

			if(sqlE.getErrorCode()==1017 || sqlE.getErrorCode()==03114)
			   throw new ServiceException(PcsEJBConstant.DB_CONNECTION_ERROR);

			status="FAILURE";
			message=sqlE.getMessage();
		}
		catch (Exception e) {
			System.out.println("ERROR: CustdetailsDAO.deleteCustNumQuery() :"+e.getMessage());
			//e.printStackTrace();
			status="FAILURE";
			message=e.getMessage();
		} finally {
			 getDaoUtil().closeStatement(stmt);
			 getDaoUtil().closeConnection(dbConnection);
		}
		returnStatusList.add(0,status);
		returnStatusList.add(1,message);
		return returnStatusList;
	}
	
//	 added by jK
	public String createqueryString(String custNum ,String userId){
		String sqlQueryStr="update PAIR_USER_CN "
			+ "set delete_flag = 'Y' ,"
			+ "upd_date = sysdate "
			//+ "SYSADMINID = '"+user+"' "
			+"where PAIR_USER_CN.fk_pair_user_dn_id ='" + userId +"'"
			+" and PAIR_USER_CN.cust_num = " + (55000000 + Integer.parseInt(custNum));
		return sqlQueryStr;
	}
	
	
	
	/**
	 * This method generates a sql query based on parameters and method name
	 * @param custNum
	 * @param userId
	 * @return sql query
	 */
	public String selectCNQueryString(String custNum ,String userId){
		String sqlQueryStr="SELECT * " +
				"FROM PAIR_USER_CN " +
				"WHERE PAIR_USER_CN.FK_PAIR_USER_DN_ID = '" + userId +"'" +
						" AND PAIR_USER_CN.CUST_NUM = "+ (55000000 + Integer.parseInt(custNum));			
		return sqlQueryStr;
	}
	
	/**
	 * This method generates a sql query based on parameters and method name
	 * @param custNum
	 * @param userId
	 * @return sql query
	 */
	public String updateCNExistQueryString(String custNum ,String userId){
		String sqlQueryStr="update PAIR_USER_CN "
			+ "set delete_flag = null ,"
			+ "upd_date = sysdate "
			//+ "SYSADMINID = '"+user+"' "
			+"where PAIR_USER_CN.fk_pair_user_dn_id ='" + userId +"'"
			+" and PAIR_USER_CN.cust_num = " + (55000000 + Integer.parseInt(custNum));
		return sqlQueryStr;
	}	
	
	/**
	 * This method generates a sql query based on parameters and method name
	 * @param custNum
	 * @param userId
	 * @return sql query
	 */
	public String selectDNQueryString(String custNum ,String userId){
		String sqlQueryStr="select USER_ID, SYSADMINID " +
				"FROM PAIR_USER_DN " +
				"WHERE PAIR_USER_DN.PAIR_USER_DN_ID = '" + userId +"'";			
		return sqlQueryStr;
	}
	
	/**
	 * This method generates a sql query based on parameters and method name
	 * @param custNum
	 * @param userId
	 * @param uid
	 * @param sysAdminId
	 * @return sql query
	 */
	public String insertCNQueryString(String custNum ,String userId, String uid, String sysAdminId){
		String sqlQueryStr="Insert into PAIR_USER_CN (USER_ID,CUST_NUM,SYSADMINID,INS_DATE,UPD_DATE,DELETE_FLAG,FK_PAIR_USER_DN_ID) " +
				"values ('" + uid +"',"+				 
		        +(55000000 + Integer.parseInt(custNum))		        
			    + ",'" + sysAdminId +"',sysdate,null,null," +
				"'"+userId+"')";
		return sqlQueryStr;
	}
	
	
	/**
	 * The method "insertCorrDissassociate" inserts customer number details data and disassociate attorney details
	 * into the PAIR_CUSTOMER_ADDRESS in PALM DB
	 * If the passed status paramerter is SUCCESS it insert with Printed and responded values else these fields will
	 * be balnk which will be picked as un processed record in PURM application.
	 * In this method REG_NO_TO_INSERT will not be inserted
	 * @param custDataInfo customer data to be inserted
	 * @param status the previous activity status
	 * @return String the insert status. It can either be SUCCESS or FAILURE
	 * @throws ServiceException
	 * @modified on 6/19/2009 - eOffice Action
	 */
	public String insertCorrDissassociate(CustDataInfo custDataInfo, String status)
	throws ServiceException
	{

		PreparedStatement p_stmt=null;
		String insertString="SUCCESS";
		int insertCount=0;
		boolean insertSuccess=false;
		boolean updateSuccess =false;
		Connection dbConnection=null;
		Calendar currentDate = Calendar.getInstance();
		SimpleDateFormat sdf1  = new SimpleDateFormat("MM-dd-yyyy::HH:mm:ss");
		String currentDateSet = sdf1.format(currentDate.getTime());

		StringBuffer queryStrBuff = new StringBuffer("INSERT INTO PAIR_CUSTOMER_ADDRESS (NAME_LINE_ONE_O,NAME_LINE_TWO_O,STREET_LINE_ONE_O,");
		queryStrBuff.append(" STREET_LINE_TWO_O, CITY_NM_O,POSTAL_CD_O,STATE_NM_O,COUNTRY_NM_O,TELEPHONE_NO_O,SECONDARY_TELEPHONE_NO_O,");
		queryStrBuff.append(" TERTIARY_TELEPHONE_NO_O, FAX_NO_O,SECONDARY_FAX_NO_O,EMAIL_ADDRESS_O,SECONDARY_EMAIL_ADDR_OLD_TX, TERTIARY_EMAIL_ADDR_OLD_TX,");
		queryStrBuff.append(" CURR_DLVRY_MODE_OPT_IN_O, CURR_DLVRY_MODE_OPT_DT_O,");
		queryStrBuff.append(" NAME_LINE_ONE_N,NAME_LINE_TWO_N,STREET_LINE_ONE_N,STREET_LINE_TWO_N,CITY_NM_N,POSTAL_CD_N,STATE_NM_N,COUNTRY_NM_N,");
		queryStrBuff.append(" TELEPHONE_NO_N,SECONDARY_TELEPHONE_NO_N,TERTIARY_TELEPHONE_NO_N, FAX_NO_N,SECONDARY_FAX_NO_N,EMAIL_ADDRESS_N,");
		queryStrBuff.append(" SECONDARY_EMAIL_ADDR_NEW_TX,TERTIARY_EMAIL_ADDR_NEW_TX,CURR_DLVRY_MODE_OPT_IN_N, CURR_DLVRY_MODE_OPT_DT_N,");
		queryStrBuff.append(" REG_NO_TO_INSERT,REG_NO_TO_DELETE,CUSTOMER_NO,DISTINGUISHED_NM, ");
		queryStrBuff.append(" CHANGE_REQ_REGN_NO, CHANGE_REQ_USER_NM,");
		queryStrBuff.append(" DATE_PRINTED,DATE_RESPONDED,LAST_MODIFIED_TS, ");
		queryStrBuff.append(" USER_ID_PRINTED, USER_ID_RESPONDED, CHANGE_ADDRESS_IN,POINT_OF_CONTACT_NM,POINT_OF_CONTACT_PHONE,");
		queryStrBuff.append(" POINT_OF_CONTACT_EMAIL,CHANGE_REQUEST_TX,CHANGE_REQ_SUBMIT_DT,POSTCARD_NOTIFY_IND_O,POSTCARD_NOTIFY_IND_N) ");
		queryStrBuff.append(" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?,'YYYY-MM-DD::HH24:MI:SS'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,TO_DATE(?,'MM-DD-YYYY::HH24:MI:SS'),?,?,?,?,?,?,");
		queryStrBuff.append("TO_DATE(?,'MM-DD-YYYY::HH24:MI:SS'),TO_DATE(?,'MM-DD-YYYY::HH24:MI:SS'),sysdate,?,?,?,?,?,?,?,TO_DATE(?,'MM-DD-YYYY::HH24:MI:SS'),?,?)");

		try
		{
			dbConnection = dataSource.getConnection();
			p_stmt = dbConnection.prepareStatement(queryStrBuff.toString());
			
			//Start 	DR31 Mar2012 Limit field to appropriate datatype length(50)
			String name1o = custDataInfo.getName_line_one_o().trim().length()> 50 ? custDataInfo.getName_line_one_o().trim().substring(0,49) : custDataInfo.getName_line_one_o().trim();
			String name2o = custDataInfo.getName_line_two_o().trim().length()> 50 ? custDataInfo.getName_line_two_o().trim().substring(0,49) : custDataInfo.getName_line_two_o().trim();
			
			p_stmt.setString(1, name1o);
			p_stmt.setString(2, name2o);
			System.out.println("ATTENTION!M CustdetailsDAO.getLastPrivatePairUpdate() : name1o = "+name1o + " name2o = " + name2o);
			//END DR31
			p_stmt.setString(3,custDataInfo.getStreet_line_one_o());
			p_stmt.setString(4,custDataInfo.getStreet_line_two_o());
			p_stmt.setString(5,custDataInfo.getCity_nm_o());
			p_stmt.setString(6,custDataInfo.getPostal_cd_o());
			p_stmt.setString(7,custDataInfo.getState_nm_o());
			p_stmt.setString(8,custDataInfo.getCountry_nm_o());
			p_stmt.setString(9,custDataInfo.getTelephone_no_1_o());
			p_stmt.setString(10,custDataInfo.getTelephone_no_2_o());
			p_stmt.setString(11,custDataInfo.getTelephone_no_3_o());
			p_stmt.setString(12,custDataInfo.getFax_no_1_o());
			p_stmt.setString(13,custDataInfo.getFax_no_2_o());
			p_stmt.setString(14,custDataInfo.getEmail_address_1_o());
			p_stmt.setString(15,custDataInfo.getEmail_address_2_o());
			p_stmt.setString(16,custDataInfo.getEmail_address_3_o());
			p_stmt.setString(17,custDataInfo.getDelivery_mode_opt_ind_o());
			p_stmt.setString(18,custDataInfo.getDelivery_mode_opt_date_o());
			
			//Start 	DR31 Mar2012 Limit field to appropriate datatype length(50)
			String name1n = custDataInfo.getName_line_one_n().trim().length()> 50 ? custDataInfo.getName_line_one_n().trim().substring(0,49) : custDataInfo.getName_line_one_n().trim();
			String name2n = custDataInfo.getName_line_two_n().trim().length()> 50 ? custDataInfo.getName_line_two_n().trim().substring(0,49) : custDataInfo.getName_line_two_n().trim();
			
			p_stmt.setString(19,name1n);
			p_stmt.setString(20,name2n);
			//END	DR31
			p_stmt.setString(21,custDataInfo.getStreet_line_one_n());
			p_stmt.setString(22,custDataInfo.getStreet_line_two_n());
			p_stmt.setString(23,custDataInfo.getCity_nm_n());
			p_stmt.setString(24,custDataInfo.getPostal_cd_n());
			p_stmt.setString(25,custDataInfo.getState_nm_n());
			p_stmt.setString(26,custDataInfo.getCountry_nm_n());
			p_stmt.setString(27,custDataInfo.getTelephone_no_1_n());
			p_stmt.setString(28,custDataInfo.getTelephone_no_2_n());
			p_stmt.setString(29,custDataInfo.getTelephone_no_3_n());
			p_stmt.setString(30,custDataInfo.getFax_no_1_n());
			p_stmt.setString(31,custDataInfo.getFax_no_2_n());
			p_stmt.setString(32,custDataInfo.getEmail_address_1_n());
			p_stmt.setString(33,custDataInfo.getEmail_address_2_n());
			p_stmt.setString(34,custDataInfo.getEmail_address_3_n());
			p_stmt.setString(35,custDataInfo.getDelivery_mode_opt_ind_n());
			p_stmt.setString(36,currentDateSet);
			p_stmt.setString(37,custDataInfo.getReg_no_to_insert());
			p_stmt.setString(38,custDataInfo.getReg_no_to_delete());
			p_stmt.setString(39,custDataInfo.getCustomer_no());
			p_stmt.setString(40,custDataInfo.getDistinguished_nm());
			p_stmt.setString(41,custDataInfo.getChange_req_regn_no());
			p_stmt.setString(42,custDataInfo.getChange_req_user_nm());

			if(status.equalsIgnoreCase("SUCCESS"))
			{
				p_stmt.setString(43,currentDateSet);
				p_stmt.setString(44,currentDateSet);
				p_stmt.setString(45,custDataInfo.getUser_id_printed());
				p_stmt.setString(46,custDataInfo.getUser_id_responded());
			}
			else
			{
				//If Failure
				p_stmt.setString(43,null);
				p_stmt.setString(44,null);
				p_stmt.setString(45,null);
				p_stmt.setString(46,null);
			}
			p_stmt.setString(47,custDataInfo.getChange_address_in());
			p_stmt.setString(48,custDataInfo.getPoint_of_contact_nm());
			p_stmt.setString(49,custDataInfo.getPoint_of_contact_phone());
			p_stmt.setString(50,custDataInfo.getPoint_of_contact_email());

			//restrict the change_request_text message length to 250 as the column lenght is only 250
			p_stmt.setString(51,(custDataInfo.getChangeRequestStatus_text().trim().length()>256) ? custDataInfo.getChangeRequestStatus_text().trim().substring(0,255) : custDataInfo.getChangeRequestStatus_text().trim());
			p_stmt.setString(52,currentDateSet);
			p_stmt.setString(53,custDataInfo.getPost_card_opt_ind_o());
			p_stmt.setString(54,custDataInfo.getPost_card_opt_ind_n());

			//Insert the record
			insertCount = p_stmt.executeUpdate();
			System.out.println("insertCount"+insertCount);
			if(insertCount <= 0){
				insertString = "FAILURE";
				System.out.println("ERROR: CustdetailsDAO.insertCorrDissassociate() : Insert PAIR_CUSTOMER_ADDRESS returned with zero count : CN:"+custDataInfo.getCustomer_no()+", Change Req reg no:"+custDataInfo.getChange_req_regn_no());
			}
			else
			{
				System.out.println("INFO: CustdetailsDAO.insertCorrDissassociate(): Successful insertion of PAIR_CUSTOMER ADDRESS : CN:"+custDataInfo.getCustomer_no()+", Change Req reg no:"+custDataInfo.getChange_req_regn_no());
			}
			//Update PAIR_CUSTOMER_OPT record irrespective of PALM SERVICE status
			updateSuccess = updatePairCustomerOpt(custDataInfo);
			// If update not successful, try inserting into PAIR_CUSTOMER_OPT.
			if(!updateSuccess)
			{
				insertSuccess = insertPairCustomerOpt(custDataInfo);

			}
			//If both update and insert fail, mark it as failure
			if(!updateSuccess&&!insertSuccess)
			{
				insertString = "FAILURE";
			}

		}
		catch (SQLException sqlE)
		{
			insertString = "FAILURE";
			 System.out.println("ERROR: CustdetailsDAO.insertCorrDissassociate() :"+sqlE.getMessage());
			 if(sqlE.getErrorCode()==1017 || sqlE.getErrorCode()==03114)
			 {
				throw new ServiceException(PcsEJBConstant.DB_CONNECTION_ERROR);
			 }
		}
		catch (Exception e)
		{
		 	insertString = "FAILURE";
			System.out.println("ERROR: CustdetailsDAO.insertCorrDissassociate() :"+e.getMessage());
		}
		finally
		{
			 getDaoUtil().closeStatement(p_stmt);
			 getDaoUtil().closeConnection(dbConnection);
		}

		return ResponseEntity.ok(gson.toJson(insertString)).getBody();
	}

	/*
	 * The method "insertAssociateDisAssociate" inserts attorney association details data and Attorney disassociate attorney details
	 *  into the PAIR_CUSTOMER_ADDRESS in PALM DB
	 * If the passed status paramerter is SUCCESS it insert with Printed and responded values else these fields will
	 * be balnk which will be picked as un processed record in PURM application.
	 * If this method is called to insert values for REG_NO_TO_INSERT status will always be FAILURE, But the relavent Message will
	 * be updated in Change Request text
	 * @modified on 6/19/2009 - eOffice Action
	 */
	public String insertAssociateDisAssociate(CustDataInfo custDataInfo, String status)
	throws ServiceException
	{

		PreparedStatement p_stmt=null;
		String insertString="SUCCESS";
		int insertCount=0;
		Connection dbConnection=null;

		String sqlInsertQueryStr = "INSERT INTO PAIR_CUSTOMER_ADDRESS (REG_NO_TO_INSERT,REG_NO_TO_DELETE,CUSTOMER_NO,DISTINGUISHED_NM, "
			+ " CHANGE_REQ_REGN_NO, CHANGE_REQ_USER_NM, CHANGE_REQ_SUBMIT_DT,DATE_PRINTED,DATE_RESPONDED,LAST_MODIFIED_TS, "
			+ " USER_ID_PRINTED, USER_ID_RESPONDED, POINT_OF_CONTACT_NM,POINT_OF_CONTACT_PHONE,"
			+ " POINT_OF_CONTACT_EMAIL,CHANGE_REQUEST_TX) "
			+ " VALUES (?,?,?,?,?,?,TO_DATE(?,'MM-DD-YYYY::HH24:MI:SS'),TO_DATE(?,'MM-DD-YYYY::HH24:MI:SS'),TO_DATE(?,'MM-DD-YYYY::HH24:MI:SS'),sysdate,?,?,?,?,?,?)";

		try {


/*			if(custDataInfo.getChangeRequestStatus_text().trim().length()>256)
				custDataInfo.setChangeRequestStatus_text(custDataInfo.getChangeRequestStatus_text().trim().substring(0,255));
*/
			Calendar currentDate = Calendar.getInstance();
			if(custDataInfo.getReg_no_to_delete() != null)
				currentDate.add(Calendar.SECOND,-1);
			else
				currentDate.add(Calendar.SECOND,1);
			SimpleDateFormat sdf1  = new SimpleDateFormat("MM-dd-yyyy::HH:mm:ss");
			String currentDateSet = sdf1.format(currentDate.getTime());

			dbConnection = dataSource.getConnection();
			p_stmt = dbConnection.prepareStatement(sqlInsertQueryStr);
			p_stmt.setString(1,custDataInfo.getReg_no_to_insert());
			p_stmt.setString(2,custDataInfo.getReg_no_to_delete());
			p_stmt.setString(3,custDataInfo.getCustomer_no());
			p_stmt.setString(4,custDataInfo.getDistinguished_nm());
			p_stmt.setString(5,custDataInfo.getChange_req_regn_no());
			p_stmt.setString(6,custDataInfo.getChange_req_user_nm());
			p_stmt.setString(7,currentDateSet);
			System.out.println("status"+status);
			if(status.equalsIgnoreCase("SUCCESS"))
			{
				System.out.println("inside success of insertAssociateDisAssociate");
				
				p_stmt.setString(8,currentDateSet);
				p_stmt.setString(9,currentDateSet);
				p_stmt.setString(10,custDataInfo.getUser_id_printed());
				p_stmt.setString(11,custDataInfo.getUser_id_responded());
			}else{
				//If Failure
				p_stmt.setString(8,null);
				p_stmt.setString(9,null);
				p_stmt.setString(10,null);
				p_stmt.setString(11,null);
			}
			p_stmt.setString(12,custDataInfo.getPoint_of_contact_nm());
			p_stmt.setString(13,custDataInfo.getPoint_of_contact_phone());
			p_stmt.setString(14,custDataInfo.getPoint_of_contact_email());
//			restrict the change_request_text message length to 250 as the column lenght is only 250
			p_stmt.setString(15,(custDataInfo.getChangeRequestStatus_text().trim().length()>256) ? custDataInfo.getChangeRequestStatus_text().trim().substring(0,255) : custDataInfo.getChangeRequestStatus_text().trim());

			insertCount = p_stmt.executeUpdate();
			if(insertCount <= 0)
				insertString = "FAILURE";
			System.out.println("INFO: CustdetailsDAO.insertAssociateDisAssociate() - CN:"+custDataInfo.getCustomer_no()+", Change Req reg no:"+custDataInfo.getChange_req_regn_no());

		}catch (SQLException sqlE)
		{
			insertString = "FAILURE";
			 System.out.println("ERROR: CustdetailsDAO.insertAssociateDisAssociate() :"+sqlE.getMessage());
			 if(sqlE.getErrorCode()==1017 || sqlE.getErrorCode()==03114)
				throw new ServiceException(PcsEJBConstant.DB_CONNECTION_ERROR);
		}
		 catch (Exception e) {
		 	insertString = "FAILURE";
			System.out.println("ERROR: CustdetailsDAO.insertAssociateDisAssociate() :"+e.getMessage());
		}
		finally {
			getDaoUtil().closeStatement(p_stmt);
			 getDaoUtil().closeConnection(dbConnection);
		} //finally
		return ResponseEntity.ok(gson.toJson(insertString)).getBody();
	}


	/**
	 * Updates a record into PAIR_CUSTOMER_OPT table if there is a difference in customer optin indicator and the new value is Y.
	 * This update happens irrespective of PALM updateCustomer service status and insertion of CustomerAddress table
	 * as this doesnot have any followup process through PURM to manully update the record.
	 * @param custDataInfo
	 * @return
	 * @throws ServiceException
	 * @modified on 6/19/2009 - eOffice Action
	 */
	private boolean updatePairCustomerOpt(CustDataInfo custDataInfo) throws ServiceException
	{

		 // Check if the old and new optInvalue are different
		 // If different, update the record
		PreparedStatement p_stmt=null;
		Connection dbConnection=null;
		boolean success=false;
		int updateCount=0;
		int iCustNum=0;
		StringBuffer queryStrBuff = new StringBuffer("UPDATE PAIR_CUSTOMER_OPT SET OPTIN_IND=?,");
		queryStrBuff.append(" POSTCARD_MAIL_OPTIN_TX=?, LAST_MODIFIED_TS=sysdate,");
		queryStrBuff.append(" DISTINGUISHED_NM=?,OPT_DT=sysdate, POSTCARD_MAIL_OPT_DT=sysdate WHERE CUSTOMER_NO=?");
		System.out.println("OLD Delivery Mode: "+custDataInfo.getDelivery_mode_opt_ind_o().trim());
		System.out.println("NEW Delivery Mode: "+custDataInfo.getDelivery_mode_opt_ind_n().trim());
		System.out.println("OLD POSTCARD INDICATOR: "+custDataInfo.getPost_card_opt_ind_o().trim());
		System.out.println("NEW POSTCARD INDICATOR: "+custDataInfo.getPost_card_opt_ind_n().trim());
		try
		{
			if(!custDataInfo.getDelivery_mode_opt_ind_o().trim().equalsIgnoreCase(custDataInfo.getDelivery_mode_opt_ind_n().trim())
				|| !custDataInfo.getPost_card_opt_ind_o().trim().equalsIgnoreCase(custDataInfo.getPost_card_opt_ind_n().trim())	)
			{

				String customerNo=StringUtil.noNulls(custDataInfo.getCustomer_no());
				//Validate Customer Number
				if(customerNo.equals(""))
				{
					throw new ServiceException("The Customer number is null or empty");
				}
				iCustNum= Integer.parseInt(customerNo);
				dbConnection = dataSource.getConnection();
				p_stmt = dbConnection.prepareStatement(queryStrBuff.toString());
				p_stmt.setString(1,custDataInfo.getDelivery_mode_opt_ind_n() );
				p_stmt.setString(2,custDataInfo.getPost_card_opt_ind_n());
				p_stmt.setString(3,custDataInfo.getDistinguished_nm());
				p_stmt.setInt(4,iCustNum);
				System.out.println(" OPTIN_IND: "+custDataInfo.getDelivery_mode_opt_ind_n());
				System.out.println(" POSTCARD_MAIL_OPTIN_TX: "+custDataInfo.getPost_card_opt_ind_n());
				System.out.println(" DISTINGUISHED_NM: "+custDataInfo.getDistinguished_nm());
				System.out.println(" CUSTOMER_NO: "+iCustNum);

				updateCount = p_stmt.executeUpdate();
				System.out.println("Number of Rows Updated: "+updateCount);
				if(updateCount > 0)
				{
					success=true;
				}

			}else {
				//do nothing
				success=true;
			}
		}
		catch (SQLException sqlE)
		{
			 System.out.println("ERROR: CustdetailsDAO.updatePairCustomerOpt() :"+sqlE.getMessage());
			 if(sqlE.getErrorCode()==1017 || sqlE.getErrorCode()==03114)
			 {
				throw new ServiceException(PcsEJBConstant.DB_CONNECTION_ERROR);
			 }
		}
		catch (Exception e)
		{
			System.out.println("ERROR: CustdetailsDAO.updatePairCustomerOpt() :"+e.getMessage());
		}
		finally
		{
			 getDaoUtil().closeStatement(p_stmt);
			 getDaoUtil().closeConnection(dbConnection);
		}

		return success;
	}
	
	
	/**
	 * Inserts a record into PAIR_CUSTOMER_OPT table
	 * @param custDataInfo
	 * @return
	 * @throws ServiceException
	 * @modified on 6/19/2009 - eOffice Action
	 */
	private boolean insertPairCustomerOpt(CustDataInfo custDataInfo) throws ServiceException
	{
		boolean success=false;
		PreparedStatement p_stmt=null;
		int updateCount=0;
		int iCustNum=0;
		Connection dbConnection=null;

		StringBuffer queryStrBuff = new StringBuffer("INSERT INTO PAIR_CUSTOMER_OPT(CUSTOMER_NO,OPTIN_IND,POSTCARD_MAIL_OPTIN_TX,");
		queryStrBuff.append("LAST_MODIFIED_TS,DISTINGUISHED_NM,OPT_DT,POSTCARD_MAIL_OPT_DT) ");
		queryStrBuff.append("VALUES (?,?,?,sysdate,?,sysdate,sysdate)");

		try
		{
			if(!custDataInfo.getDelivery_mode_opt_ind_o().trim().equalsIgnoreCase(custDataInfo.getDelivery_mode_opt_ind_n().trim())
					|| !custDataInfo.getPost_card_opt_ind_o().trim().equalsIgnoreCase(custDataInfo.getPost_card_opt_ind_n().trim())	)
			{
				String customerNo=StringUtil.noNulls(custDataInfo.getCustomer_no());
				//Validate Customer Number
				if(customerNo.equals(""))
				{
					throw new ServiceException("The Customer number is null or empty");
				}
				iCustNum= Integer.parseInt(customerNo);
				dbConnection = dataSource.getConnection();
				p_stmt = dbConnection.prepareStatement(queryStrBuff.toString());
				p_stmt.setInt(1,iCustNum);
				p_stmt.setString(2,custDataInfo.getDelivery_mode_opt_ind_n() );
				p_stmt.setString(3,custDataInfo.getPost_card_opt_ind_n());
				p_stmt.setString(4,custDataInfo.getDistinguished_nm());
				updateCount = p_stmt.executeUpdate();
				if(updateCount>0)
				{
					success=true;
				}
			}
			else {
				//do nothing 
				success=true;
			}
		}
		catch (SQLException sqlE)
		{
			 System.out.println("ERROR: CustdetailsDAO.updatePairCustomerOpt() :"+sqlE.getMessage());
			 if(sqlE.getErrorCode()==1017 || sqlE.getErrorCode()==03114)
			 {
				throw new ServiceException(PcsEJBConstant.DB_CONNECTION_ERROR);
			 }
		}
		catch (Exception e)
		{
			System.out.println("ERROR: CustdetailsDAO.updatePairCustomerOpt() :"+e.getMessage());
		}
		finally
		{
			 getDaoUtil().closeStatement(p_stmt);
			 getDaoUtil().closeConnection(dbConnection);
		}

		return success;
	}
	
	
	/*
	 * The method "insertCorrDissassociate" inserts two customer number details data record, One with Correspondence
	 * update details and other with Dissassociation details, this methond is called when Corrpondence and dissassociation
	 * has different PALM update status.
	 * In this method REG_NO_TO_INSERT will not be inserted
	 */
	public String insertCorrDissassociate(CustDataInfo custDataInfo, String corrStatus, String disAssociateStatus, String corrMessage)
	throws ServiceException
	{

		//Insert DisassociateAttorney details
		CustDataInfo custDissAssociateInfo = new CustDataInfo();
		custDissAssociateInfo.setCustomer_no(custDataInfo.getCustomer_no());
		custDissAssociateInfo.setDistinguished_nm(custDataInfo.getDistinguished_nm());
		custDissAssociateInfo.setChange_req_regn_no(custDataInfo.getChange_req_regn_no());
		custDissAssociateInfo.setChange_req_user_nm(custDataInfo.getChange_req_user_nm());
		custDissAssociateInfo.setChange_req_submit_dt(custDataInfo.getChange_req_submit_dt());
		custDissAssociateInfo.setDate_printed(custDataInfo.getDate_printed());
		custDissAssociateInfo.setDate_responded(custDataInfo.getDate_responded());
		custDissAssociateInfo.setLast_modified_ts(custDataInfo.getLast_modified_ts());

		custDissAssociateInfo.setUser_id_printed(custDataInfo.getUser_id_printed());
		custDissAssociateInfo.setUser_id_responded(custDataInfo.getUser_id_responded());
		custDissAssociateInfo.setPoint_of_contact_nm(custDataInfo.getPoint_of_contact_nm());
		custDissAssociateInfo.setPoint_of_contact_phone(custDataInfo.getPoint_of_contact_phone());
		custDissAssociateInfo.setPoint_of_contact_email(custDataInfo.getPoint_of_contact_email());
		custDissAssociateInfo.setReg_no_to_delete(custDataInfo.getReg_no_to_delete());
		custDissAssociateInfo.setChangeRequestStatus_text(custDataInfo.getChangeRequestStatus_text());

		String insertDisassociateStatus = insertAssociateDisAssociate(custDissAssociateInfo,disAssociateStatus);

		//Insert correspondence details
		custDataInfo.setReg_no_to_delete("");
		custDataInfo.setReg_no_to_insert("");
		custDataInfo.setChangeRequestStatus_text(corrMessage);
		String insertCorrStatus = insertCorrDissassociate(custDataInfo,corrStatus);

		return insertCorrStatus + "::" + insertDisassociateStatus;
	}
	
	
}
