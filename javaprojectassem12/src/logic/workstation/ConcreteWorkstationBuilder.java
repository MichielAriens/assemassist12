package logic.workstation;

public class ConcreteWorkstationBuilder extends WorkstationBuilder{
	
	private Workstation result;
	
	private Workstation lastAdded;
	
	@Override
	public void buildCarBodyPost() {
		Workstation temp = lastAdded;
		this.lastAdded = new CarBodyPost(); 
		if(result == null)
			result = lastAdded;
		if(temp != null)
			temp.setWorkStation(lastAdded);
	}

	@Override
	public void buildDriveTrainPost() {
		Workstation temp = lastAdded;
		this.lastAdded = new CarBodyPost(); 
		if(result == null)
			result = lastAdded;
		if(temp != null)
			temp.setWorkStation(lastAdded);
	}

	@Override
	public void buildAccessoriesPost() {
		Workstation temp = lastAdded;
		this.lastAdded = new CarBodyPost(); 
		if(result == null)
			result = lastAdded;
		if(temp != null)
			temp.setWorkStation(lastAdded);
	}

	@Override
	public Workstation getResult() {
		return this.result;
	}
	
}