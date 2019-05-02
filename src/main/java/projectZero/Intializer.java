package projectZero;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Intializer {
	Scanner sc = new Scanner(System.in);

	public Intializer() {
		Connection con = connect();
		Person user = Authentication.authenticate(con, sc);
		LoggedInUserActions userActions = new LoggedInUserActions(con, sc, user);
	}
	
	public static Connection connect() {
		
		String url = "jdbc:postgresql://192.168.99.100:32768/postgres";
		String user = "postgres";
		String password	= "bob";
		Connection con;
		try {
			con = ConnectionUtil.getConnection(url, user, password);	
		} catch (SQLException e) {
			e.printStackTrace();
			con = null; 
		}
		return con;
	
	}
}
