package projectZero;


public class OwnershipLink {
	private final int id;
	private final int person_id; 
	private final int account_id;
	private final int ownership_status_id;
	
	public OwnershipLink(int id, int pId, int aId, int oId){
		this.id = id;                 
		person_id = pId;          
		account_id = aId;         
		ownership_status_id = oId;
	}

	public int getId() {
		return id;
	}

	public int getPerson_id() {
		return person_id;
	}

	public int getAccount_id() {
		return account_id;
	}

	public int getOwnership_status_id() {
		return ownership_status_id;
	}
}
