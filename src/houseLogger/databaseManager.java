package houseLogger;

import java.sql.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
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
	
	public LinkedList<String> getHouseIDs(String table) throws SQLException
	{
		LinkedList<String> result = new LinkedList<String>();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		
		con = DriverManager.getConnection(url, user, password);
			
		st = con.createStatement();
		rs = st.executeQuery("SELECT `houseid` from `"+table+"`");
		
		while(rs.next()) 
		{
			result.add(rs.getString("houseid"));
		}
		
		if (rs != null) {
			rs.close();
		}
		if (st != null) {
			st.close();
		}

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
		
		return result;

	}
	
	public void updateQueries(Vector<yad2ShortEntry> data, String table) {

		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			con = DriverManager.getConnection(url, user, password);
			
			
			Iterator dataIterator = data.iterator();
			while (dataIterator.hasNext()) {
				yad2ShortEntry item = (yad2ShortEntry) dataIterator.next();
				
				st = con.createStatement();
				rs = st.executeQuery("SELECT * from `"+table+"` where `houseid`='"+item.getHouseID()+"' order by `id` desc");
				
				boolean showHistory = true;
				
				if (rs.next()) 
				{
					//System.out.println("Entry with houseID 29d252be6ffbb1830b2cc4001383a45997d was found");
					//an item with the following id already exists check if update happend
					if(item.equalsToSQLRow(rs))
					{
						logger.debug("Nothing to do for houseID: " + item.getHouseID());
						showHistory = false;
					}
					else
					{
						
						//logger.info("Adding newer entry for houseID: " + item.getHouseID());
						logger.info(item.toString() + " changed price from " + rs.getString("price") + " at date: " + rs.getString("date"));
						insertToDatabase(item, table);
					}
					
				}
				else
				{
					logger.info("Adding new house: " + item.toString());
					insertToDatabase(item, table);
				}
				
				//Write the house history to screen
				while(rs.next() && showHistory)
				{
					logger.info("History: " + item.toString() + " changed price from " + rs.getString("price") + " at date: " + rs.getString("date"));
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
	
	private static void insertToDatabase(yad2ShortEntry data, String table)
	{
		Connection con = null;
		Statement st = null;
		boolean rs;
		
		try {
			con = DriverManager.getConnection(url, user, password);
			
			st = con.createStatement();
			rs = st.execute(data.prepareInsertQuery(table));
			
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

	public void updateDeletedEntries(LinkedList<String> databaseHousesID, String table) {
		
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		
		try {
			con = DriverManager.getConnection(url, user, password);
			
			ListIterator<String> listIterator = databaseHousesID.listIterator();
			while (listIterator.hasNext()) {
				//System.out.println(listIterator.next());
				
				String houseid = listIterator.next();
				
				st = con.createStatement();
				rs = st.executeQuery("SELECT * from `"+table+"` where `houseid` = '"+houseid+"'");
				
				if(rs.next())
				{
					System.out.println(String.format("House with ID: %s was deleted, type: %s, located at: %s, sold for %s on %s", rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(8)));
				}
				
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				
				st = con.createStatement();
				st.execute("UPDATE `"+table+"` set `deleted`='true' where `houseid`='"+houseid+"'");
			}

			
			

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
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		return;
		
	}

}
