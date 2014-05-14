package logic.workstation;

public class WorkstationDirectorA extends WorkstationDirector {

	public WorkstationDirectorA(WorkstationBuilder builder) {
		super(builder);
	}

	/**
	 * Construct the topology for assemblylines 1 & 2
	 */
	@Override
	public void construct() {
		builder.buildCarBodyPost();
		builder.buildDriveTrainPost();
		builder.buildAccessoriesPost();
	}

}
