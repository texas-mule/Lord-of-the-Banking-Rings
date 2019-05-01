package projectZero;

public class OwnershipStatus {
	private final int ownershipStatusid;
	private final String ownershipStatusName;
	
	public OwnershipStatus(int id, String name) {
		ownershipStatusid = id;
		ownershipStatusName = name;
	}

	public int getOwnershipStatusid() {
		return ownershipStatusid;
	}

	public String getOwnershipStatusName() {
		return ownershipStatusName;
	}
	
}
