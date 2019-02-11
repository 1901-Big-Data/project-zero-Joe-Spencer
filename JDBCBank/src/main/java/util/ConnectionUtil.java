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
	
	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		Class.forName("oracle.jdbc.driver.OracleDriver");
		if (con == null) {
			con=DriverManager.getConnection("jdbc:oracle:thin:@myrds.ctyify3t5omm.us-east-2.rds.amazonaws.com:1521:ORCL", "Joseph_Spencer", "y:e8sQthq3ft5J2");
		}
		
		
		return con;
	}
}
