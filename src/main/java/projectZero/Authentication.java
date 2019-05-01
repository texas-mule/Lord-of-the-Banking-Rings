package projectZero;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Authentication {
	
	public static Person authenticate(Connection con, Scanner sc) {
		Person user;
		boolean hasAccount;
		System.out.println("Do you have an account? enter 'y' for yes ");
		String userInput = sc.next();
		userInput = userInput.substring(0, 1);
		hasAccount = userInput.equalsIgnoreCase("y");
		if (hasAccount) {
			System.out.println("What is your username?");
			String username = sc.next();
			System.out.println("What is your password?");
			String password = sc.next();
			user = login(con, username, password);
		} else {
			 do{ 
				System.out.println("What is your desired username?");
				String username = sc.next();
				System.out.println("What password do you want?");
				String password = sc.next();
				System.out.println("What is your name?");
				String name = sc.next();
				user = register(con, 0, username, password, name);
			}while (user == null); // if no user returned 
		}
		return user;		
	}
	
	public static Person register(Connection con, int typeId, String username, String password, String name ) {		
		Person newPerson;
		
		try {
			String sql = "INSERT INTO person (person_type_id, person_username, person_password, person_name) /n"
					   + "VALUES (?,?,?,?)/n"
					   + "RETURNING id;";
			PreparedStatement ps = con.prepareStatement(sql);
			
			ps.setInt(1, typeId);
			ps.setString(2, username);
			ps.setString(3, password);
			ps.setString(4, name);
			
			ResultSet rs = ps.executeQuery();
			
			rs.next();
			
			newPerson = new Person(rs.getInt(1), typeId, username, password, name);
			
		} catch (Exception e) {
			e.printStackTrace();
			newPerson = null;
		}
		return newPerson;
		
	}        
	
	
	public static Person login(Connection con, String username, String password) {		
		Person person;
		
		try {
			String sql = "SELECT * /n"
					+    "FROM users /n"
					+    "WHERE u_username = ? /n"
					+    "AND   u_password = ? ;";	
			PreparedStatement ps = con.prepareStatement(sql);
			
			ps.setString(1, username);
			ps.setString(2, password);
			
			ResultSet rs = ps.executeQuery();
			
			rs.next();
			
			person = new Person(rs.getInt(1), rs.getInt("person_type_id"), username, password, rs.getString("person_name"));
			
		} catch (Exception e) {
			e.printStackTrace();
			person = null;
		}
		return person;
	}
}
