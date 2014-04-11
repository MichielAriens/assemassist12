package logic.workstation;

public abstract class WorkstationBuilder {
	
	public abstract void buildCarBodyPost();
	
	public abstract void buildDriveTrainPost();
	
	public abstract void buildAccessoriesPost();
	
	public abstract Workstation getResult();
}