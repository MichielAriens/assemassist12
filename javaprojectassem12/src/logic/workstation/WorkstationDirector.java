package logic.workstation;

/**
 * A class used to represent a director used to build a structure of workstations.
 */
public abstract class WorkstationDirector {
	
	/**
	 * The builder that this director uses to build the structure of workstations.
	 */
	protected WorkstationBuilder builder;
	
	/**
	 * Construct a new WorkstationDirector with a given WorkstationBuilder.
	 * @param builder	The builder that this director uses to build the structure of workstations.
	 */
	public WorkstationDirector(WorkstationBuilder builder){
		this.builder = builder;
	}
	
	/**
	 * Build the structure of workstations.
	 */
	public abstract void construct();
}