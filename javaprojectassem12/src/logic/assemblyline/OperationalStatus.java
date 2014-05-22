package logic.assemblyline;

import java.util.ArrayList;
import java.util.List;

import interfaces.Printable;

/**
 * An enum to represent the operational status of an assembly line.
 */
public enum OperationalStatus implements Printable<OperationalStatus>{
	OPERATIONAL("Operational",0), 
	MAINTENANCE("Maintenance",240), 
	BROKEN("Broken",0),
	PREMAINTENANCE("Premaintenance",0);
	
	/**
	 * The full name of this operational status.
	 */
	public final String fullName;
	
	/**
	 * The standard time the assembly line is offline according to this status.
	 */
	public final int time;
	
	/**
	 * Makes an operational status with given name and time.
	 * @param name	The name for this operational status.
	 * @param time	The standard offline time for this operational status.
	 */
	OperationalStatus(String name, int time){
		this.fullName = name;
		this.time = time;
	}
	
	/**
	 * Returns the standard time the assembly line is offline according to this status.
	 * @return the standard time the assembly line is offline according to this status.
	 */
	public int getTime(){
		return this.time;
	}
	
	public static List<OperationalStatus> getSelectableStatusByManager(){
		ArrayList<OperationalStatus> selectables = new ArrayList<>();
		selectables.add(OPERATIONAL);
		selectables.add(BROKEN);
		selectables.add(MAINTENANCE);
		return selectables;
	}
	
	/**
	 * Returns a string representation of this operational status.
	 */
	@Override
	public String getStringRepresentation() {
		return this.fullName;
	}

	@Override
	public String getExtraInformation() {
		return "Standard time offline: " + getTime() + " minutes.";
	}

	@Override
	public String getStatus() {
		return this.fullName;
	}
}