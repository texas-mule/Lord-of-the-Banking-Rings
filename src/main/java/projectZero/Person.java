package projectZero;

public class Person {
	private int personID; //Primary Key
	private int personTypeID; //Foreign Key
	private String userName; //Unique non-nullable
	private String password; //For the worlds most "secure" password storage
	private String name;
	
	public Person(int id, int typeId, String userName, String password, String name) {
		personID = id;
		personTypeID = typeId;
		this.userName = userName;
		this.password = password;
		this.name = name;
	}
	
	public int getPersonID() {
		return personID;
	}

	public int getPersonTypeID() {
		return personTypeID;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public String getName() {
		return name;
	}

	
	
	
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
