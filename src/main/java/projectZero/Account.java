package projectZero;

public class Account {
	private final int accountId;
	private double balance =0.0;
	 
	public Account(int id, double bal) {
		accountId = id;
		balance = bal;
	}
	
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}

	public int getAccountId() {
		return accountId;
	}
}
