package houseLogger;

import java.sql.*;

import org.apache.log4j.Logger;

public class databaseManager {
	
	private static Logger logger;
	private static String url = "jdbc:mysql://localhost:3306/houses";
    private static String user = "testuser";
    private static String password = "123456";
	
	public databaseManager(Logger globalLogger)
	{
		logger = globalLogger;
	}
	
	public void run()
	{
		Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        
        try {
            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
            rs = st.executeQuery("SELECT VERSION()");

            if (rs.next()) {
                System.out.println(rs.getString(1));
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
