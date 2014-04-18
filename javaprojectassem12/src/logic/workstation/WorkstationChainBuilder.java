package logic.workstation;

/**
 * A class used to represent a concrete builder to construct a chain of workstations.
 */
public class WorkstationChainBuilder extends WorkstationBuilder{
	
	/**
	 * The first workstation of the chain being constructed.
	 */
	private Workstation result;
	
	/**
	 * The last workstation that has been added to the chain.
	 */
	private Workstation lastAdded;
	
	/**
	 * Add a CarBodyPost to the end of the chain.
	 */
	@Override
	public void buildCarBodyPost() {
		Workstation temp = lastAdded;
		this.lastAdded = new CarBodyPost(); 
		if(result == null)
			result = lastAdded;
		if(temp != null)
			temp.setWorkStation(lastAdded);
	}
	
	/**
	 * Add a DriveTrainPost to the end of the chain.
	 */
	@Override
	public void buildDriveTrainPost() {
		Workstation temp = lastAdded;
		this.lastAdded = new DriveTrainPost(); 
		if(result == null)
			result = lastAdded;
		if(temp != null)
			temp.setWorkStation(lastAdded);
	}
	
	/**
	 * Add an AccessoriesPost to the end of the chain.
	 */
	@Override
	public void buildAccessoriesPost() {
		Workstation temp = lastAdded;
		this.lastAdded = new AccessoriesPost(); 
		if(result == null)
			result = lastAdded;
		if(temp != null)
			temp.setWorkStation(lastAdded);
	}
	
	/**
	 * Return the result, which is the first workstation of the chain.
	 * @return	The first workstation of the chain.
	 */
	@Override
	public Workstation getResult() {
		return this.result;
	}
	
}