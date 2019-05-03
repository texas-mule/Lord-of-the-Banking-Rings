package projectZero;

import java.sql.Connection;
import java.util.Scanner;

public class Authentication {
	
	public static Person authenticate(Connection con, Scanner sc) {
		Person user;
		boolean hasAccount;
		System.out.println("Do you have an account? enter 'y' for yes");
		String userInput = sc.nextLine();
		if(userInput.length()>0) {
		userInput = userInput.substring(0, 1);
		}
		hasAccount = userInput.equalsIgnoreCase("y");
		if (hasAccount) {
			System.out.println("What is your username?");
			String username = sc.nextLine();
			System.out.println("What is your password?");
			String password = sc.nextLine();
			user = login(con, username, password);
			if(user == null)
				System.out.println("Login failed");
		} else {
			 do{ 
				System.out.println("What is your desired username?");
				String username = sc.nextLine();
				System.out.println("What password do you want?");
				String password = sc.nextLine();
				System.out.println("What is your name?");
				String name = sc.nextLine();
				System.out.println("Are you a: basic user 1, employee 2, admin 3");
				int type = StubbornScanner.scanInt(sc);
				user = register(con, type, username, password, name);
			}while (user == null); // if no user returned 
		}
		return user;		
	}
	
	public static Person register(Connection con, int typeId, String username, String password, String name ) {		
		Person newPerson;
		int personId = DAOfTheRings.createUser(con, typeId, username, password, name);
		newPerson = new Person(personId, typeId, username, password, name);
		return newPerson;		
	}        
	
	
	public static Person login(Connection con, String username, String password) {		
		Person person;
		person = DAOfTheRings.validateLogin(con, username, password);
		return person;
	}
}
