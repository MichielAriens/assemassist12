package logic.assemblyline;

public enum OperationalStatus {
	OPERATIONAL("Operational",0), 
	MAINTENANCE("Maintenance",240), 
	BROKEN("Broken",0);
	
	/**
	 * The full name of this operational status.
	 */
	public final String fullName;
	
	public final int time;
	
	OperationalStatus(String name, int time){
		fullName = name;
		this.time = time;
	}
	
	
	public int getTime(){
		return this.time;
	}
	
	/**
	 * Returns a string representation of this operational status.
	 */
	@Override
	public String toString(){
		return this.fullName;
	}
}
