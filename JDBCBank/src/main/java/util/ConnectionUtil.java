package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtil {
	private static Connection con;
	
	private ConnectionUtil() {
		
	}
	
	public static Connection getConnection() throws SQLException, ClassNotFoundException, IOException {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		
		if (con == null) {
			Properties prop = new Properties();
			InputStream in = new FileInputStream("C:\\Users\\Joe\\Documents\\Project0\\JDBCBank\\JDBCBank.properties");
			prop.load(in);
			String url = prop.getProperty("jdbc.url");
			String username = prop.getProperty("jdbc.username");
			String pass = prop.getProperty("jdbc.pass");
			con = DriverManager.getConnection(url, username, pass);
			//con=DriverManager.getConnection("jdbc:oracle:thin:@myrds.ctyify3t5omm.us-east-2.rds.amazonaws.com:1521:ORCL", "Joseph_Spencer", "y:e8sQthq3ft5J2");
		}
		
		return con;
	}
}
