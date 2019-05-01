/**
 * 
 */
package projectZero;

/**
 * @author owd45
 *
 */
public class Person {
	private int personID; //Primary Key
	private int personTypeID; //Foreign Key
	private String userName; //Unique non-nullable
	private String password; //For the worlds most "secure" password storage
	private String name;
	
	public Person(String userName, )
	
	public int[] getAccounts() {
		
		return null;
	}

	public void deposit(double amount, int accountId) {          
		                             
	}                                
	
	public void withdraw(double amount, int accountId) {
		
	}
	public void transfer(double amount, int accountId, int targetId) {
		
	}
	public void requestNewAccount() {
		
	}
	public void requestNewJointAccount () {
		
	}          
	public void handleJointAccountRequests() {
		
	}
	
	
	
	
	//Employee only functions (admins are employees)
	public void queryAccounts() {
		
	}
	public void manageAccountRequests() {
		
	}

	 

	//Admin only functions

	public void adminDeposit() {
		
	}
	public void adminWithdraw() {
		
	}
	public void adminTransferring() {
		
	}
	public void cancelAccount() {
		
	}
}
