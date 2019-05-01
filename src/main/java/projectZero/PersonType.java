package projectZero;

public class PersonType {
	private final int PersonTypeID;
	private String PersonTypeName;
	
	public PersonType(int id, String name) {
		PersonTypeID = id;
		PersonTypeName = name;
	}

	public int getPersonTypeID() {
		return PersonTypeID;
	}

	public String getPersonTypeName() {
		return PersonTypeName;
	}
}
