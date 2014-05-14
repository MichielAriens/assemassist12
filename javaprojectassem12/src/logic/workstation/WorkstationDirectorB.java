package logic.workstation;

public class WorkstationDirectorB extends WorkstationDirector {

	public WorkstationDirectorB(WorkstationBuilder builder) {
		super(builder);
	}

	/**
	 * Construct the topology for assemblyline 3
	 */
	@Override
	public void construct() {
		builder.buildCarBodyPost();
		builder.buildCargoPost();
		builder.buildDriveTrainPost();
		builder.buildAccessoriesPost();
		builder.buildCertificationPost();
	}

}
