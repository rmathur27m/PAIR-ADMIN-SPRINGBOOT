package gov.uspto.patent.privatePair.utils;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * This Class contains utility methods for database operations.
 * @modified on 6/19/2009 - eOffice Action
 */
public class DBUtil {

	private static final Logger  logger = LoggerFactory.getLogger(DBUtil.class);
	/**
	 * Closes the specified connection
	 * @param conn - the connection to close
	 */
	public static void close(Connection conn)
	{
		try
		{
			if (conn != null && !conn.isClosed())
			{
				conn.close();
			}
		}
		catch (SQLException se)
		{
			logger.error("ERROR: Error while closing the DB Connection :"+se.getLocalizedMessage());
		}
	}

	/**
	 * Closes the specified ResultSet
	 * @param rs - the ResultSet to close
	 */
	public static void close(ResultSet rs)
	{
		try
		{
			if (rs != null)
			{
				rs.close();
			}
		}
		catch (SQLException se)
		{
			logger.error("ERROR: Error while closing the SQL ResultSet :"+se.getLocalizedMessage());
		}
	}

	/**
	 * Closes the specified PreparedStatement
	 * @param stmnt -  the PreparedStatement to close
	 */
	public static void close(PreparedStatement stmnt)
	{
		try
		{
			if (stmnt != null)
			{
				stmnt.close();
			}
		}
		catch (SQLException se)
		{
			logger.error("Error while closing the SQL PreparedStatement :"+se.getLocalizedMessage());
		}
	}

	/**
	 * Closes the specified Statement
	 * @param stmnt -  the Statement to close
	 */
	public static void close(Statement stmnt)
	{
		try
		{
			if (stmnt != null)
			{
				stmnt.close();
			}
		}
		catch (SQLException se)
		{
			logger.error("Error while closing the SQL Statement :"+se.getLocalizedMessage());
		}
	}


}
