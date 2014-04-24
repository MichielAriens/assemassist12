package logic.workstation;

/**
 * A class used to represent an abstract builder to construct a structure of workstations.
 */
public abstract class WorkstationBuilder {
	
	/**
	 * Add a CarBodyPost to the structure.
	 */
	public abstract void buildCarBodyPost();
	
	/**
	 * Add a DriveTrainPost to the structure.
	 */
	public abstract void buildDriveTrainPost();
	
	/**
	 * Add an AccerssoriesPost to the structure.
	 */
	public abstract void buildAccessoriesPost();
	
	/**
	 * Get the resulting structure.
	 * @return	The resulting structure.
	 */
	public abstract Workstation getResult();
}