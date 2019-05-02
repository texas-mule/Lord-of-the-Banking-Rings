package projectZero;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DAOfTheRings {

	static DAOfTheRings dao = new DAOfTheRings();

	public static int createUser(Connection con, int typeId, String username, String password, String name) {
		int userId;
		try {
			String sql = "INSERT INTO person (person_type_id, person_username, person_password, person_name) \n"
					+ "VALUES (?,?,?,?) \n" + "RETURNING person_id;";
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
			String sql = "SELECT * FROM person \n" + "WHERE person_username = ? \n" + "AND   person_password = ? ;";
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
					+ "WHERE ownership_link.person_id = ? \n" + "AND ownership_link.ownership_status_id = 1;";
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
					+ "WHERE ownership_link.person_id = ? ;";
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
					+ "WHERE ownership_link.person_id = ? \n" + "AND ownership_link.ownership_status_id = 1;";
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
					+ "WHERE ownership_link.ownership_status_id = 1;";
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
			String sql = "SELECT count(*) FROM account \n"
					+ "JOIN ownership_link on account.account_id = ownership_link.account_id;";
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
					+ "WHERE ownership_link.ownership_status_id = 1;";
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
					+ "JOIN ownership_link on account.account_id = ownership_link.account_id;";
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

	public static Account[] getAllAccounts(Connection con) {
		Account[] accounts;
		try {
			String sql = "SELECT * FROM account \n" + "JOIN ownership_link on account.account_id;";
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

	public static boolean cancelAccount(Connection con, int id) {
		boolean isSuccessful = true;
		try {
			String sql = "UPDATE ownership_link *\n" + "Set ownership_status_id = 5 \n" + "WHERE account_id = ?;";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
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
			String sql = "Select account_balance \n" + "FROM account \n" + "WHERE account_id = ? \n;";
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
			String sql = "UPDATE account * \n" + "Set account_balance = ? \n" + "WHERE account_id = ?;";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setDouble(1, bal);
			ps.setInt(2, id);
			ps.executeQuery();
		} catch (Exception e) {
			e.printStackTrace();
			isSucsessful = false;
		}
		return isSucsessful;
	}

	public static String[] getAccountInfoById(Connection con, int id) {
		String[] resultTable;
		try {
			int count = getCountOfAccountsByPersonId(con, id);
			String sql = "SELECT * from get_account_info_by_id('3');";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			resultTable = new String[count];
			for (int i=0; 1<resultTable.length; i++) {
				rs.next();
				resultTable[i] = new String("personId: " + rs.getString(1) + "\t username: " + rs.getString(2) + "\t full name: "
						+ rs.getString(3) + "\t person type" + rs.getString(4) + "\t balance: " + rs.getDouble(5)
						+ "\t account status: " + rs.getString(6));
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultTable = null;
		}
		return resultTable;

	}
	
	public static String[] getAccountInfo(Connection con) {
		String[] resultTable;
		try {
			int count = getCountOfAccounts(con);
			String sql = "SELECT * from get_account_info;";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			resultTable = new String[count];
			for (String i : resultTable) {
				rs.next();
				i = new String("personId: " + rs.getString(1) + "\t username: " + rs.getString(2) + "\t full name: "
						+ rs.getString(3) + "\t person type" + rs.getString(4) + "\t balance: " + rs.getDouble(5)
						+ "\t account status: " + rs.getString(6));
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultTable = null;
		}
		return resultTable;

	}
}
