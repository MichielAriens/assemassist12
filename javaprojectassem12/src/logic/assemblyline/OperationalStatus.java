package logic.assemblyline;

import interfaces.Printable;

public enum OperationalStatus implements Printable {
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


	@Override
	public String getStringRepresentation() {
		return this.toString();
	}


	@Override
	public String getExtraInformation() {
		return this.toString();
	}


	@Override
	public String getStatus() {
		return this.toString();
	}
}
