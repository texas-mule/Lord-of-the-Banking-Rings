package projectZero;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
	private static Connection conn;
	
	public static Connection getConnection (String url, String user, String password) throws SQLException {
		if (conn == null) {
			conn = DriverManager.getConnection(url, user, password);
		}
		
		return conn;
	}
}