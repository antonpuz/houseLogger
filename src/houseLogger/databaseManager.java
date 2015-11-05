package houseLogger;

import java.sql.*;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

public class databaseManager {

	private static Logger logger;
	private static String url = "jdbc:mysql://localhost:3306/houses?characterEncoding=utf-8";
	private static String user = "testuser";
	private static String password = "123456";

	public databaseManager(Logger globalLogger) {
		logger = globalLogger;
	}

	public void updateQueries(Vector<yad2ShortEntry> data) {

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			con = DriverManager.getConnection(url, user, password);
			
			
			Iterator dataIterator = data.iterator();
			while (dataIterator.hasNext()) {
				yad2ShortEntry item = (yad2ShortEntry) dataIterator.next();
				
				st = con.createStatement();
				rs = st.executeQuery("SELECT * from `shortinfo` where `houseid`='"+item.getHouseID()+"' order by `id` desc");

				if (rs.next()) 
				{
					//System.out.println("Entry with houseID 29d252be6ffbb1830b2cc4001383a45997d was found");
					//an item with the following id already exists check if update happend
					if(item.equalsToSQLRow(rs))
					{
						logger.info("Nothing to do for houseID: " + item.getHouseID());
					}
					else
					{
						logger.info("Adding newer entry for houseID: " + item.getHouseID());
						insertToDatabase(item);
					}
					
				}
				else
				{
					logger.info("Adding a new entry for houseID: " + item.getHouseID());
					insertToDatabase(item);
				}
				
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
			}

		} catch (SQLException ex) {
			logger.error(ex.getMessage());

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				logger.error(ex.getMessage());
			}
		}
		
		

	}
	
	private static void insertToDatabase(yad2ShortEntry data)
	{
		Connection con = null;
		Statement st = null;
		boolean rs;
		
		try {
			con = DriverManager.getConnection(url, user, password);
			
			st = con.createStatement();
			rs = st.execute(data.prepareInsertQuery("shortinfo"));
			
			if (rs) 
			{
				System.out.println("Item was added to database");
			}

			
		} catch (SQLException ex) {
			logger.error(ex.getMessage());

		} finally {
			try {
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				logger.error(ex.getMessage());
			}
		}
	}
	
	public void run() {
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;

		try {
			con = DriverManager.getConnection(url, user, password);
			st = con.createStatement();
			rs = st.executeQuery("SELECT VERSION()");

			if (rs.next()) {
				logger.error(String.format("Database is up and running version %s", rs.getString(1)));
			}

		} catch (SQLException ex) {
			logger.error(ex.getMessage());

		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				logger.error(ex.getMessage());
			}
		}
	}

}
