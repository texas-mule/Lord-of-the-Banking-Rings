package projectZero;

public class Person {
	private final int id; //Primary Key
	private final int personTypeID; //Foreign Key
	private final String userName; //Unique non-nullable
	private final String password; //For the worlds most "secure" password storage
	private final String name;
	
	public Person(int id, int typeId, String userName, String password, String name) {
		this.id = id;
		personTypeID = typeId;
		this.userName = userName;
		this.password = password;
		this.name = name;
	}

	public int getId() {
		return id;
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
}
