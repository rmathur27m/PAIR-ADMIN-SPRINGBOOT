package gov.uspto.patent.privatePair.admin.dao;



import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


public class DaoUtil {
	private static final Logger  log = LoggerFactory.getLogger(DaoUtil.class);
	InitialContext ic;
	public static DaoUtil daoUtil=null;
	
	public static DaoUtil getDaoUtil()
	{
		if(daoUtil!=null)
			return daoUtil;
		else
			return daoUtil = new DaoUtil();
	}
	
	private DaoUtil() {
		try {
				ic = new InitialContext();
			} catch (NamingException ne) {
		}
	}


	/**
	 * daId should only contain comma separated numbers.
	 * DR#33 - Docketed functionality check
	 * @param daId
	 * @return
	 */
	public static boolean validateSqlInParams(String daId) {
		Pattern p = Pattern.compile("[0-9,\\s]+");
		Matcher m = p.matcher(daId);
		return m.find();
	}
	
	
	/*public Connection getDBConnection(String jdbcLookup) throws Exception, Exception {
		Connection dbConnection;
		try{
			DataSource dataSource = (DataSource) ic.lookup(jdbcLookup);
			dbConnection = dataSource.getConnection();
			if(isStaleConnection(dbConnection))
			{
				closeConnection(dbConnection);
				dbConnection = dataSource.getConnection();
			}
			
		}catch(NamingException ne)
		{
			throw new Exception(ne.getMessage());
		}
		return dbConnection;
	}*/
	
	/*private boolean isStaleConnection(Connection dbConnection) throws SQLException
	{
		boolean staleFlag=false;
		try{
			Statement stmt = dbConnection.createStatement();
			stmt.executeQuery("select 1 from dual");
		}catch(Exception sCE)
		{
			staleFlag=true;
		}
		finally {
			dbConnection.close();

		}
		
		return staleFlag;
		
	}
	*/
	public void closeStatement(Statement stmt) {
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception se) {
			log.error("ERROR: Error while closing the SQL Statement :"+se.getLocalizedMessage());
			//do nothing
		}
	}
	
	public void closeStatement(PreparedStatement pStmt) {
		try {
			if (pStmt != null)
				pStmt.close();
		} catch (Exception se) {
			//do nothing
			log.error("ERROR: Error while closing the SQL PreparedStatement :"+se.getLocalizedMessage());
		}
	}
	public void closeResultSet(ResultSet result) {
		try {
			if (result != null)
				result.close();
		} catch (Exception se) {
			//do nothing
			log.error("ERROR: Error while closing the SQL ResultSet :"+se.getLocalizedMessage());
		}
	}
	public void closeConnection(Connection dbConnection) {
		try {
			if (dbConnection != null && !dbConnection.isClosed())
				dbConnection.close();
		} catch (Exception se) {
			//do nothing
			log.error("ERROR: Error while closing the DB Connection :"+se.getLocalizedMessage());
		}
	}
}
