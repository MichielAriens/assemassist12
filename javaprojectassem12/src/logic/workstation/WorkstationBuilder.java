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
	 * Add a CargoPost to the stucture.
	 */
	public abstract void buildCargoPost();
	
	/**
	 * Add a CertificationPost to the structure.
	 */
	public abstract void buildCertificationPost();
	
	/**
	 * Get the resulting structure.
	 * @return	The resulting structure.
	 */
	public abstract Workstation getResult();
}