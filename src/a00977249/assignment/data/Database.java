/**
 * Project: COMP3613_A00977249Assignment02
 * File: Database.java
 * Date: Oct 14, 2016
 * Time: 12:49:39 PM
 */
package a00977249.assignment.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Siamak Pourian
 *
 * Makes a connection to the BCIT server
 *
 * Database Class
 */
public class Database {
	
	private static Connection con = null;
	
	/**
	 * Configures the remote database
	 * 
	 * @param props the DB properties file
	 * @throws SQLException
	 */
	public static void config(Properties props) throws SQLException {
		try {
			Class.forName(props.getProperty("Driver"));
//			con = DriverManager.getConnection(props.getProperty("URL"), props);	
		} catch (ClassNotFoundException e) {
		    throw new SQLException(e);
		}
	}
	
	/**
	 * Connects to the remote database
	 * 
	 * @param props the DB properties file
	 * @throws SQLException
	 */
	public static void connect(Properties props) throws SQLException {
		con = DriverManager.getConnection(props.getProperty("URL"), props);
	}
	
	/**
	 * Returns the connection to the remote db
	 * 
	 * @return the connection to db
	 * @throws SQLException 
	 */
	public static Connection getConnection() throws SQLException {
		return con;
	}
	
	/**
	 * Closes the connection to the server
	 * 
	 * @throws SQLException
	 */
	public static void closeConnection() throws SQLException {
		if (con != null && !con.isClosed()) {
			con.close();
		}
	}
}
