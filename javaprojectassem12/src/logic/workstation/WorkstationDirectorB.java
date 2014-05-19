package logic.workstation;

/**
 * A class used to represent a director used to build a structure of workstations, used to create
 * the topology for AssemblyLines 3.
 */
public class WorkstationDirectorB extends WorkstationDirector {
	

	/**
	 * Construct a new WorkstationDirectorB with a given WorkstationBuilder.
	 * @param builder	The builder that this director uses to build the structure of workstations.
	 */
	public WorkstationDirectorB(WorkstationBuilder builder) {
		super(builder);
	}

	/**
	 * Constructs the topology for AssemblyLine 3.
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
