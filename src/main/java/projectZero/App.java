/**
 * 
 */
package projectZero;

import java.util.Scanner;

/**
 * @author owd45
 *
 */
public class App {
	//String logLocation;
	Scanner sc = new Scanner(System.in);
	
	public App(){
		//this.logLocation ="";
		boolean hasAccount;
		System.out.println("Do you have an account? enter 'y' for yes ");
		String userInput = sc.next();
		userInput = userInput.substring(0, 1);
		hasAccount = userInput.equalsIgnoreCase("y");
		if(hasAccount) {
			Authentication.login();
		}else {
			Authentication.register();
		}
	}
	public App(String location){
		//this.logLocation = location;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {                                   
		App app = new App();                                                
	}

	/*
	 * public String getLogLocation() { return logLocation; }
	 */

}