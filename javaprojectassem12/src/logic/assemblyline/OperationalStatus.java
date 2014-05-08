package logic.assemblyline;

public enum OperationalStatus {
	OPERATIANAL("Operational"), 
	MAINTENANCE("Maintenance"), 
	BROKEN("Broken");
	
	/**
	 * The full name of this operational status.
	 */
	public final String fullName;
	
	OperationalStatus(String name){
		fullName = name;
	}
	
	/**
	 * Returns a string representation of this operational status.
	 */
	@Override
	public String toString(){
		return this.fullName;
	}
}
