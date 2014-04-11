package logic.workstation;

public class WorkstationDirector {

	private WorkstationBuilder builder;
	
	public WorkstationDirector(WorkstationBuilder builder){
		this.builder = builder;
	}
	
	public void construct(){
		builder.buildCarBodyPost();
		builder.buildDriveTrainPost();
		builder.buildAccessoriesPost();
	}
}