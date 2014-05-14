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
	
	private void build(Workstation ws){
		Workstation temp = lastAdded;
		this.lastAdded = ws;
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


	/**
	 * Add a CarBodyPost to the end of the chain.
	 */
	@Override
	public void buildCarBodyPost() {
		build(new CarBodyPost());
	}
	
	
	/**
	 * Add a DriveTrainPost to the end of the chain.
	 */
	@Override
	public void buildDriveTrainPost() {
		build(new DriveTrainPost());
	}
	
	/**
	 * Add an AccessoriesPost to the end of the chain.
	 */
	@Override
	public void buildAccessoriesPost() {
		build(new AccessoriesPost());
	}
	
	/**
	 * Add a CargoPost to the end of the chain.
	 */
	@Override
	public void buildCargoPost() {
		build(new CargoPost());
		
	}
	
	/**
	 * Add a CertificationPost to the end of the chain.
	 */
	@Override
	public void buildCertificationPost() {
		build(new CertificationPost());
		
	}
	
}