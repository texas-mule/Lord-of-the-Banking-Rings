/**
 * 
 */
package projectZero;

/**
 * @author owd45
 *
 */
public class Accounnt {
	private final int accountId;//sequence
	private boolean approvalState;
	private double balance =0.0;
	
	public boolean isApprovalState() {
		return approvalState;
	}
	public void setApprovalState(boolean approvalState) {
		this.approvalState = approvalState;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
}
