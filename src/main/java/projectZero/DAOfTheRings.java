package projectZero;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DAOfTheRings {

	private static final int APPROVED = 1;
	private static final int DENIED = 2;
	private static final int PENDING_APPROVAL = 3;
	private static final int PENDING_JOIN = 4;
	private static final int CANCELED = 5;

	static DAOfTheRings dao = new DAOfTheRings();

	public static int createUser(Connection con, int typeId, String username, String password, String name) {
		int userId;
		try {
			String sql = "INSERT INTO person (person_type_id, person_username, person_password, person_name) \n"
					+ "VALUES (?,?,?,?) RETURNING person_id";
			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, typeId);
			ps.setString(2, username);
			ps.setString(3, password);
			ps.setString(4, name);

			ResultSet rs = ps.executeQuery();

			rs.next();

			userId = rs.getInt(1);

		} catch (Exception e) {
			e.printStackTrace();
			userId = -1;
		}
		return userId;
	}

	public static Person validateLogin(Connection con, String username, String password) {
		Person person;
		try {
			String sql = "SELECT * FROM person WHERE person_username = ? AND person_password = ? ";
			PreparedStatement ps = con.prepareStatement(sql);

			ps.setString(1, username);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();
			rs.next();

			person = new Person(rs.getInt(1), rs.getInt("person_type_id"), username, password,
					rs.getString("person_name"));

		} catch (Exception e) {
			e.printStackTrace();
			person = null;
		}
		return person;
	}

	public static int getCountOfOpenAccountsByPersonId(Connection con, int id) {
		int count;
		try {
			String sql = "SELECT count(*) FROM account \n"
					+ "JOIN ownership_link on account.account_id = ownership_link.account_id \n"
					+ "WHERE ownership_link.person_id = ? \n" + "AND ownership_link.ownership_status_id = 1";
			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();

			rs.next();
			count = rs.getInt(1);
		} catch (

		Exception e) {
			e.printStackTrace();
			count = -1;
		}
		return count;
	}

	public static int getCountOfAccountsByPersonId(Connection con, int id) {
		int count;
		try {
			String sql = "SELECT count(*) FROM account \n"
					+ "JOIN ownership_link on account.account_id = ownership_link.account_id \n"
					+ "WHERE ownership_link.person_id = ? ";
			PreparedStatement ps = con.prepareStatement(sql);

			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();

			rs.next();
			count = rs.getInt(1);
		} catch (

		Exception e) {
			e.printStackTrace();
			count = -1;
		}
		return count;
	}

	public static Account[] getOpenAccountsById(Connection con, int id) {
		Account[] accounts;
		try {
			String sql = "SELECT * FROM account \n"
					+ "JOIN ownership_link on account.account_id = ownership_link.account_id \n"
					+ "WHERE ownership_link.person_id = ? \n" + "AND ownership_link.ownership_status_id = 1";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			int count = getCountOfOpenAccountsByPersonId(con, id);
			accounts = new Account[count];
			for (int i = 0; i < count; i++) {
				rs.next();
				accounts[i] = new Account(rs.getInt(1), rs.getDouble(2));
			}
		} catch (

		Exception e) {
			e.printStackTrace();
			accounts = null;
		}
		return accounts;
	}

	public static int getCountOfOpenAccounts(Connection con) {
		int count;
		try {
			String sql = "SELECT count(*) FROM account \n"
					+ "JOIN ownership_link on account.account_id = ownership_link.account_id \n"
					+ "WHERE ownership_link.ownership_status_id = " + APPROVED + " ";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			rs.next();
			count = rs.getInt(1);
		} catch (

		Exception e) {
			e.printStackTrace();
			count = -1;
		}
		return count;
	}

	public static int getCountOfNonCancelledAccounts(Connection con) {
		int count;
		try {
			String sql = "SELECT count(*) FROM account \n"
					+ "JOIN ownership_link on account.account_id = ownership_link.account_id \n"
					+ "WHERE ownership_link.ownership_status_id <> " + CANCELED;
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			rs.next();
			count = rs.getInt(1);
		} catch (

		Exception e) {
			e.printStackTrace();
			count = -1;
		}
		return count;
	}

	public static int getCountOfAccounts(Connection con) {
		int count;
		try {
			String sql = "SELECT count(*) FROM account";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			rs.next();
			count = rs.getInt(1);
		} catch (

		Exception e) {
			e.printStackTrace();
			count = -1;
		}
		return count;
	}

	public static Account[] getAllOpenAccounts(Connection con) {
		Account[] accounts;
		try {
			String sql = "SELECT * FROM account \n"
					+ "JOIN ownership_link on account.account_id = ownership_link.account_id \n"
					+ "WHERE ownership_link.ownership_status_id = 1";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			int count = getCountOfOpenAccounts(con);
			accounts = new Account[count];
			for (int i = 0; i < count; i++) {
				rs.next();
				accounts[i] = new Account(rs.getInt(1), rs.getDouble(2));
			}
		} catch (

		Exception e) {
			e.printStackTrace();
			accounts = null;
		}
		return accounts;
	}

	public static Account[] getAllNonCancelledAccounts(Connection con) {
		Account[] accounts;
		try {
			String sql = "SELECT * FROM account \n"
					+ "JOIN ownership_link on account.account_id = ownership_link.account_id \n"
					+ "WHERE ownership_link.ownership_status_id <> " + CANCELED;
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			int count = getCountOfNonCancelledAccounts(con);
			accounts = new Account[count];
			for (int i = 0; i < count; i++) {
				rs.next();
				accounts[i] = new Account(rs.getInt(1), rs.getDouble(2));
			}
		} catch (

		Exception e) {
			e.printStackTrace();
			accounts = null;
		}
		return accounts;
	}

	public static Account[] getAllAccounts(Connection con) {
		Account[] accounts;
		try {
			String sql = "SELECT * FROM account \n" + "JOIN ownership_link on account.account_id";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			int count = getCountOfAccounts(con);
			accounts = new Account[count];
			for (int i = 0; i < count; i++) {
				rs.next();
				accounts[i] = new Account(rs.getInt(1), rs.getDouble(2));
			}
		} catch (

		Exception e) {
			e.printStackTrace();
			accounts = null;
		}
		return accounts;
	}

	public static boolean cancelAccount(Connection con, int id) {
		boolean isSuccessful = true;
		try {
			String sql = "UPDATE ownership_link Set ownership_status_id = " + CANCELED + " WHERE account_id = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.executeUpdate();
		} catch (

		Exception e) {
			e.printStackTrace();
			isSuccessful = false;
		}
		return isSuccessful;
	}

	public static double getAccountBalance(Connection con, int id) {
		double bal;
		try {
			String sql = "Select account_balance FROM account WHERE account_id = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			rs.next();
			bal = rs.getDouble(1);
		} catch (Exception e) {
			e.printStackTrace();
			bal = -1;
		}
		return bal;
	}

	public static boolean setAccountBalance(Connection con, int id, double bal) {
		boolean isSucsessful = true;
		try {
			String sql = "UPDATE account * Set account_balance = ? WHERE account_id = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setDouble(1, bal);
			ps.setInt(2, id);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			isSucsessful = false;
		}
		return isSucsessful;
	}

	public static int getCountOfOwnershipLinks(Connection con) {
		int count;
		try {
			String sql = "SELECT count(*) FROM ownership_link";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			rs.next();
			count = rs.getInt(1);
		} catch (

		Exception e) {
			e.printStackTrace();
			count = -1;
		}
		return count;
	}
	
//	public static String[] getAccountInfoById(Connection con, int id) {
//		String[] resultTable;
//		try {
//			int count = getCountOfOwnershipLinksByPersonId(con, id);
//			String sql = "SELECT * from get_account_info_by_id('?')";
//			PreparedStatement ps = con.prepareStatement(sql);
//			ps.setInt(1, id);
//			ResultSet rs = ps.executeQuery();
//			resultTable = new String[count];
//			for (int i = 0; 1 < resultTable.length; i++) {
//				rs.next();
//				resultTable[i] = new String("personId: " + rs.getString(1) + "\t accountId: " + rs.getString(2) + "\t username: " + rs.getString(3)
//						+ "\t full name: " + rs.getString(4) + "\t person type" + rs.getString(5) + "\t balance: "
//						+ rs.getString(6) + "\t account status: " + rs.getString(7));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			resultTable = null;
//		}
//		return resultTable;
//
//	}

	public static String[] getAccountInfo(Connection con) {
		String[] resultTable;
		try {
			int count = getCountOfOwnershipLinks(con);
			String sql = "SELECT * from account_info";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			resultTable = new String[count];
			for (int i = 0; i < count; i++) {
				rs.next();
				resultTable[i] = new String("personId: " + rs.getString(1) + "\t accountId: " + rs.getString(2) + "\t username: " + rs.getString(3)
				+ "\t full name: " + rs.getString(4) + "\t person type: " + rs.getString(5) + "\t balance: "
				+ rs.getString(6) + "\t account status: " + rs.getString(7));
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultTable = null;
		}
		return resultTable;

	}

	public static boolean createAccount(Connection con, int id) {
		boolean isSuccesful = true;
		try {
			String sql = "SELECT (create_account(?, " + PENDING_APPROVAL + "))";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ps.executeUpdate();
		} catch (Exception e) {
			//e.printStackTrace(); //It prints an error but runs fine
			isSuccesful = false;
		}
		return isSuccesful;
	}

	public static boolean createJoinRequestAccount(Connection con, int perId, int accId) {
		boolean isSuccesful = true;
		try {
			String sql = "INSERT INTO ownership_link (person_id, account_id, ownership_status_id) VALUES(?, ?, " + PENDING_JOIN + ")";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, perId);
			ps.setInt(2, accId);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			isSuccesful = false;
		}
		return isSuccesful;
	}

	public static int countJoinRequestsByPersonId(Connection con, int perId) {
		int count;
		try {
			String sql = "SELECT count(ownership_link_id) FROM ownership_link WHERE ownership_status_id = "
					+ PENDING_JOIN + " AND person_id = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, perId);
			ResultSet rs = ps.executeQuery();
			rs.next();
			count = rs.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
			count = -1;
		}
		return count;
	}

	public static OwnershipLink[] getJoinRequestsByPersonId(Connection con, int perId) {
		OwnershipLink[] ownershipLinks;
		try {
			String sql = "SELECT * FROM ownership_link WHERE ownership_status_id = " + PENDING_JOIN + " AND person_id = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, perId);
			ResultSet rs = ps.executeQuery();
			int count = countJoinRequestsByPersonId(con, perId);
			ownershipLinks = new OwnershipLink[count];
			for (int i = 0; i < count; i++) {
				rs.next();
				ownershipLinks[i] = new OwnershipLink(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4));
			}
		} catch (Exception e) {
			e.printStackTrace();
			ownershipLinks = null;
		}
		return ownershipLinks;
	}

	public static boolean confirmJoin(Connection con, int accId, int perId) {
		boolean isSuccesful = true;
		try {
			String sql = "UPDATE ownership_link SET ownership_status_id = " + PENDING_APPROVAL
					+ " WHERE account_id = ? AND person_id = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, accId);
			ps.setInt(2, perId);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			isSuccesful = false;
		}
		return isSuccesful;
	}

	public static boolean denyJoin(Connection con, int accId, int perId) {
		boolean isSuccesful = true;
		try {
			// String sql = "DELETE FROM ownership_link WHERE account_id = ? AND person_id =
			// ?";
			String sql = "UPDATE ownership_link SET ownership_status_id = " + DENIED + " WHERE account_id = ? \n"
					+ "AND person_id = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, accId);
			ps.setInt(2, perId);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			isSuccesful = false;
		}
		return isSuccesful;
	}

	public static int countAccountRequests(Connection con) {
		int count;
		try {
			String sql = "SELECT count(ownership_link_id) FROM ownership_link WHERE ownership_status_id = "
					+ PENDING_APPROVAL;
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			rs.next();
			count = rs.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
			count = -1;
		}
		return count;
	}

	public static OwnershipLink[] getAccountRequests(Connection con) {
		OwnershipLink[] ownershipLinks;
		try {
			String sql = "SELECT * FROM ownership_link WHERE ownership_status_id = " + PENDING_APPROVAL;
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			int count = countAccountRequests(con);
			ownershipLinks = new OwnershipLink[count];
			for (int i = 0; i < count; i++) {
				rs.next();
				ownershipLinks[i] = new OwnershipLink(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4));
			}
		} catch (Exception e) {
			e.printStackTrace();
			ownershipLinks = null;
		}
		return ownershipLinks;
	}

	public static boolean confirmAccount(Connection con, int accId, int perId) {
		boolean isSuccesful = true;
		try {
			String sql = "UPDATE ownership_link SET ownership_status_id = " + APPROVED + " WHERE account_id = ? \n"
					+ "AND person_id = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, accId);
			ps.setInt(2, perId);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			isSuccesful = false;
		}
		return isSuccesful;
	}

	public static boolean denyAccount(Connection con, int accId, int perId) {
		boolean isSuccesful = true;
		try {
			// String sql = "DELETE FROM ownership_link WHERE account_id = ? AND person_id =
			// ?";
			String sql = "UPDATE ownership_link SET ownership_status_id = " + DENIED + " WHERE account_id = ? \n"
					+ "AND person_id = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, accId);
			ps.setInt(2, perId);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			isSuccesful = false;
		}
		return isSuccesful;
	}

	public static int getPersonCount(Connection con) {
		int count;
		try {
			String sql = "SELECT count(*) FROM person";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			rs.next();
			count = rs.getInt(1);
		} catch (

		Exception e) {
			e.printStackTrace();
			count = -1;
		}
		return count;
	}

	public static Person[] getAllPersons(Connection con) {
		Person[] persons;
		try {
			String sql = "SELECT * FROM person";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			int count = getPersonCount(con);
			persons = new Person[count];
			for (int i = 0; i < count; i++) {
				rs.next();
				persons[i] = new Person(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5));
			}
		} catch (

		Exception e) {
			e.printStackTrace();
			persons = null;
		}
		return persons;
	}

}
