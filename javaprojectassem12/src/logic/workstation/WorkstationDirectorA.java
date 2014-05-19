package logic.workstation;

/**
 * A class used to represent a director used to build a structure of workstations, used to create
 * the topology for AssemblyLines 1 & 2.
 */
public class WorkstationDirectorA extends WorkstationDirector {
	
	/**
	 * Construct a new WorkstationDirectorA with a given WorkstationBuilder.
	 * @param builder	The builder that this director uses to build the structure of workstations.
	 */
	public WorkstationDirectorA(WorkstationBuilder builder) {
		super(builder);
	}

	/**
	 * Constructs the topology for AssemblyLines 1 & 2.
	 */
	@Override
	public void construct() {
		builder.buildCarBodyPost();
		builder.buildDriveTrainPost();
		builder.buildAccessoriesPost();
	}

}
