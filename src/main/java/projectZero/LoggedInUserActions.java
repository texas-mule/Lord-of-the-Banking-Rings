package projectZero;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class LoggedInUserActions {
	Connection con;
	Scanner sc;
	Person user;

	public LoggedInUserActions(Connection con, Scanner sc, Person user) {
		this.con = con;
		this.sc = sc;
		this.user = user;
		boolean exit = false;
		int personType = user.getPersonTypeID(); // 1 normal 2 employee 3 admin (admins are also employees)
		while (!exit) {
			System.out.println("As a regular user you may press:");
			System.out.println("0 to exit");
			System.out.println("1 to get a list of your Accounts");
			System.out.println("2 to make a deposit to one of your accounts");
			System.out.println("3 to make a withdraw from one of your accounts");
			System.out.println("4 to make a transfer from one of your accounts to another account");
			System.out.println("5 to request a new account");
			System.out.println("6 to request a account of yours be joined with another user");
			System.out.println("7 to handle join requests from other users");
			if (personType == 2 || personType == 3) {
				System.out.println("");
				System.out.println("As an employee you may also press:");
				System.out.println("8 to query Accounts");
				System.out.println("9 to query People");
				System.out.println("10 to manage account requests");
				if (personType == 3) {
					System.out.println("");
					System.out.println("As an admin you may also press:");
					System.out.println("11 to make a deposit to any account");
					System.out.println("12 to make a withdraw from any account");
					System.out.println("13 to make a transfer from any one to another account");
					System.out.println("14 to cancel an account");
				}
			}
			int userInput = StubbornScanner.scanInt(sc);
			switch (userInput) {
			case 0:
				try {
					con.close();
				} catch (SQLException e) {
					System.out.println("close connection failed");
					e.printStackTrace();
				} finally {
					System.exit(0);
				}
			case 1:
				printAccountsByPersonId();
				break;
			case 2:
				deposit();
				break;
			case 3:
				withdraw();
				break;
			case 4:
				transfer();
				break;
			case 5:
				requestNewAccount();
				break;
			case 6:
				requestNewJointAccount();
				break;
			case 7:
				handleJointAccountRequests();
				break;
			case 8:
				if (personType == 2 || personType == 3) {
					getAllAccountInfo();
					break;
				}
			case 9:
				if (personType == 2 || personType == 3) {
					queryPersons();
					break;
				}
			case 10:
				if (personType == 2 || personType == 3) {
					manageAccountRequests();
					break;
				}
			case 11:
				if (personType == 3) {
					adminDeposit();
					break;
				}
			case 12:
				if (personType == 3) {
					adminWithdraw();
					break;
				}
			case 13:
				if (personType == 3) {
					adminTransfer();
					break;
				}
			case 14:
				if (personType == 3) {
					cancelAccount();
					break;
				}
			default:
				System.out.println("Invalid option, Goodbye");
				exit = true;
			}

		}
	}

	private void printAccountsByPersonId() {
		Account[] accounts = DAOfTheRings.getOpenAccountsById(con, user.getId());
		for (int i = 0; i < accounts.length; i++) {
			System.out.println("account: " + accounts[i].getAccountId() + " with " + accounts[i].getBalance() + "$");
		}
	}

	private void deposit() {
		Account[] accounts = DAOfTheRings.getOpenAccountsById(con, user.getId());
		int[] accountIds = getAndPrintAccountIds(accounts);

		System.out.println("Which account would you like to deposit to?");
		int accountId = StubbornScanner.scanValidId(sc, accountIds);
		double bal = DAOfTheRings.getAccountBalance(con, accountId);

		System.out.println("How much would you like to deposit?");
		bal += StubbornScanner.scanDouble(sc);
		DAOfTheRings.setAccountBalance(con, accountId, bal);
	}

	private void withdraw() {
		Account[] accounts = DAOfTheRings.getOpenAccountsById(con, user.getId());
		int[] accountIds = getAndPrintAccountIds(accounts);

		System.out.println("Which account would you like to withdraw from?");
		int accountId = StubbornScanner.scanValidId(sc, accountIds);
		double bal = DAOfTheRings.getAccountBalance(con, accountId);

		System.out.println("How much would you like to withdraw?");
		bal -= StubbornScanner.scanDouble(sc);
		if(bal < 0) {
			System.out.println("Requested withdraw would overdraw account");
		}else {
			DAOfTheRings.setAccountBalance(con, accountId, bal);
		}
	}

	private void transfer() {
		Account[] accounts = DAOfTheRings.getOpenAccountsById(con, user.getId());
		int[] accountIds = getAndPrintAccountIds(accounts);

		System.out.println("Which account would you like to transfer from?");
		int accountId = StubbornScanner.scanValidId(sc, accountIds);
		double bal = DAOfTheRings.getAccountBalance(con, accountId);

		System.out.println("How much would you like to transfer?");
		double amount = StubbornScanner.scanDouble(sc);
		bal -= amount;
		if (bal < 0) {
			System.out.println("Requested transfer would overdraw account");
		} else {
			DAOfTheRings.setAccountBalance(con, accountId, bal);

			System.out.println("Which account would you like to transfer to?");
			Account[] accounts2 = DAOfTheRings.getAllOpenAccounts(con);
			int[] accountIds2 = getAndPrintAccountIds(accounts2);
			int accountId2 = StubbornScanner.scanValidId(sc, accountIds2);
			bal = DAOfTheRings.getAccountBalance(con, accountId2);
			bal += amount;
			DAOfTheRings.setAccountBalance(con, accountId2, bal);
		}
	}

	private void requestNewAccount() {
		DAOfTheRings.createAccount(con, user.getId());
	}

	private void requestNewJointAccount() {
		System.out.println("Who would you like to offer to join your account? Give there id");
		Person[] persons = DAOfTheRings.getAllPersons(con);
		for (Person i : persons) {
			System.out.println(i.getId());
		}
		int perId = StubbornScanner.scanInt(sc);

		System.out.println("What account would you like to have joined? Give the id");
		printAccountsByPersonId();
		int accId = StubbornScanner.scanInt(sc);

		DAOfTheRings.createJoinRequestAccount(con, perId, accId);
	}

	private void handleJointAccountRequests() {
		System.out.println("What join request would you like to handle?");
		OwnershipLink[] ownershipLinks = DAOfTheRings.getJoinRequestsByPersonId(con, user.getId());
		int[] accountIds = new int[ownershipLinks.length];
		for (int i = 0; i<ownershipLinks.length; i++) {
			System.out.println(ownershipLinks[i].getAccount_id());
			accountIds[i] = ownershipLinks[i].getAccount_id();
		}
		int accId = StubbornScanner.scanValidId(sc, accountIds);

		System.out.println("Do you want to aprrove this link? enter 'y' for yes");
		String userInput = sc.next();
		userInput = userInput.substring(0, 1);
		boolean isApproved = userInput.equalsIgnoreCase("y");
		if (isApproved) {
			DAOfTheRings.confirmJoin(con, accId, user.getId());
		} else {
			DAOfTheRings.denyJoin(con, accId, user.getId());
		}
	}

	// Employee only functions (admins are employees)
	private void getAllAccountInfo() {
		String[] accountInfo = DAOfTheRings.getAccountInfo(con);
		for (String i : accountInfo) {
			System.out.println(i);
		}
	}

	private void queryPersons() {
		Person[] peopleInfo = DAOfTheRings.getAllPersons(con);
		for (int i = 0; i<peopleInfo.length; i++ ) {
			System.out.println("personId: " + peopleInfo[i].getId() + "\t username: " + peopleInfo[i].getUserName()
				+ "\t full name: " + peopleInfo[i].getName() + "\t person typeId: " + peopleInfo[i].getPersonTypeID());
		}
	}

	private void manageAccountRequests() {
		System.out.println("What account request would you like to handle?");
		OwnershipLink[] ownershipLinks = DAOfTheRings.getAccountRequests(con);
		for (OwnershipLink i : ownershipLinks) {
			System.out.println(i.getAccount_id());
		}
		int accId = StubbornScanner.scanInt(sc);

		System.out.println("Do you want to aprrove this account? enter 'y' for yes");
		String userInput = sc.next();
		userInput = userInput.substring(0, 1);
		boolean isApproved = userInput.equalsIgnoreCase("y");
		if (isApproved) {
			DAOfTheRings.confirmAccount(con, accId, user.getId());
		} else {
			DAOfTheRings.denyAccount(con, accId, user.getId());
		}
	}

	// Admin only functions

	private void adminDeposit() {
		Account[] accounts = DAOfTheRings.getAllOpenAccounts(con);
		int[] accountIds = new int[accounts.length];
		for (int i = 0; i < accounts.length; i++) {
			System.out.println(accounts[i].getAccountId());
			accountIds[i] = accounts[i].getAccountId();
		}
		System.out.println("Which account would you like to deposit to?");
		int accountId = StubbornScanner.scanValidId(sc, accountIds);
		double bal = DAOfTheRings.getAccountBalance(con, accountId);
		System.out.println("How much would you like to deposit?");
		bal += StubbornScanner.scanDouble(sc);
		DAOfTheRings.setAccountBalance(con, accountId, bal);
	}

	private void adminWithdraw() {
		Account[] accounts = DAOfTheRings.getAllOpenAccounts(con);
		int[] accountIds = getAndPrintAccountIds(accounts);

		System.out.println("Which account would you like to withdraw from?");
		int accountId = StubbornScanner.scanValidId(sc, accountIds);
		double bal = DAOfTheRings.getAccountBalance(con, accountId);

		System.out.println("How much would you like to withdraw?");
		bal -= StubbornScanner.scanDouble(sc);
		if (bal < 0) {
			System.out.println("Requested withdraw would overdraw account");
		} else {
			DAOfTheRings.setAccountBalance(con, accountId, bal);
		}
	}

	private void adminTransfer() {
		Account[] accounts = DAOfTheRings.getAllOpenAccounts(con);
		int[] accountIds = getAndPrintAccountIds(accounts);

		System.out.println("Which account would you like to transfer from?");
		int accountId = StubbornScanner.scanValidId(sc, accountIds);
		double bal = DAOfTheRings.getAccountBalance(con, accountId);

		System.out.println("How much would you like to transfer?");
		double amount = StubbornScanner.scanDouble(sc);
		bal -= amount;
		if (bal < 0) {
			System.out.println("Requested transfer would overdraw account");
		} else {
			DAOfTheRings.setAccountBalance(con, accountId, bal);

			System.out.println("Which account would you like to transfer to?");
			getAndPrintAccountIds(accounts);
			int accountId2 = StubbornScanner.scanValidId(sc, accountIds);
			bal = DAOfTheRings.getAccountBalance(con, accountId2);
			bal += amount;
			DAOfTheRings.setAccountBalance(con, accountId2, bal);
		}
	}

	private void cancelAccount() {
		Account[] accounts = DAOfTheRings.getAllNonCancelledAccounts(con);
		int[] accountIds = getAndPrintAccountIds(accounts);

		System.out.println("Which account would you like to cancel");
		StubbornScanner.scanValidId(sc, accountIds);
	}

	// helper methods
	private int[] getAndPrintAccountIds(Account[] accounts) {
		int[] accountIds = new int[accounts.length];
		for (int i = 0; i < accounts.length; i++) {
			System.out.println(accounts[i].getAccountId());
			accountIds[i] = accounts[i].getAccountId();
		}
		return accountIds;
	}
}
