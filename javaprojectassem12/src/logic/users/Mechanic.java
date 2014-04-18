package logic.users;

import java.util.List; 

import logic.workstation.Workstation;

/**
 * Class used to describe a mechanic working on an assembly line in a car manufacturing company.
 */
public class Mechanic extends User{
	
	/**
	 * The string identifier of the workstation this mechanic is currently working on.
	 */
	private String activeStationIdentifier = null;
	
	/**
	 * The manufacturing company for which the mechanic works.
	 */
	private CarManufacturingCompany company;
	
	/**
	 * The maximum time it took to perform a task and so the maximum duration of
	 * a phase in the assembly line. Supposed that every task can be performed parallel.
	 */
	private int maxPhaseDuration = 0;
	
	/**
	 * Constructs a new mechanic initializing its company and user name with the given 
	 * parameters.
	 * @param company	The car manufacturing company the new mechanic works for.
	 * @param userName	The user name for this new mechanic.
	 */
	public Mechanic(CarManufacturingCompany company, String userName){
		super(userName);
		this.company = company;
	}
	
	/**
	 * Returns the workstation the mechanic is currently working on.
	 * @return The workstation the mechanic is currently working on.
	 */
	public String getActiveWorkstation(){
		return this.activeStationIdentifier;
	}
	
	/**
	 * Returns whether the mechanic is working at any workstation.
	 * @return 	False if the active workstation is null.
	 * 			True otherwise.
	 */
	public boolean isPosted(){
		return this.getActiveWorkstation() != null;
	}
	
	/**
	 * Performs a task if the task is compatible with the active workstation of the mechanic
	 * and tries to move the assembly line.
	 * @param task	The string identifier of the task that needs to be performed.
	 * @return 	True 	if the specified task has been successfully performed at the active workstation.
	 * 			False 	otherwise.
	 */
	public boolean doTask(String taskIdentifier, int duration){
		boolean performed = company.doTask(taskIdentifier, this.activeStationIdentifier);
		if(performed){
			setMaxPhaseDuration(duration);
			if(company.moveAssemblyLine(maxPhaseDuration))
				resetMaxPhaseDuration();
		}
		return performed;
	}

	/**
	 * Sets the maximum phase duration to the given duration if the given duration
	 * is greater than the maximum phase duration.
	 * @param duration	The duration that has to be checked and saved if it's greater than the current
	 * 					maximum.
	 */
	private void setMaxPhaseDuration(int duration) {
		if(duration > maxPhaseDuration){
			maxPhaseDuration = duration;
		}
	}
	
	/**
	 * Resets the maximum phase duration to zero.
	 */
	private void resetMaxPhaseDuration() {
		maxPhaseDuration = 0;
	}
	
	/**
	 * Returns a list of string identifiers of the tasks that are currently pending at this mechanic's 
	 * active workstation.
	 * @return	null	if the mechanic is not currently posted, or his current workstation is incorrectly
	 * 					specified by the string identifier.
	 * 			The list of string identifiers otherwise.
	 */
	public List<String> getAvailableTaskIdentifiers(){
		if(!isPosted())
			return null;
		return this.company.getRequiredTaskIdentifiers(this.activeStationIdentifier);
	}
	
	/**
	 * Returns a list of strings, that contains all task identifiers of the workstation specified by 
	 * the given workstation identifier, together with their status: 'Pending' or 'Completed'.
	 * @param workstationIdentifier	A string that defines a the workstation that has to be checked.
	 * @return	null	if the workstation was incorrectly specified.
	 * 			A list of strings with all tasks identifiers with their statuses at that workstation otherwise. 
	 */
	public List<String> getTaskStatus(String workstationIdentifier){
		return this.company.getTaskStatus(workstationIdentifier);
	}
	
	/**
	 * Returns the task description of the task corresponding to the given task identifier.
	 * @param taskIdentifier	A string that defines the task whose description is needed.
	 * @return	null	If the task has been incorrectly specified, or there is no task of that
	 * 					type currently at the active workstation.	
	 * 			The given task's description otherwise.
	 */
	public String getTaskDescription(String taskIdentifier){
		return company.getTaskDescription(taskIdentifier, this.activeStationIdentifier);
	}
	
	/**
	 * Returns the list of workstations from the assembly line of the car manufacturing company.
	 * @return the list of workstations from the assembly line of the car manufacturing company.
	 */
	public List<Workstation> getWorkstations(){
		return this.company.getWorkStations();
	}
	
	/**
	 * Set the identifier of the active workstation to the given identifier.
	 * @param identifier
	 */
	public void setActiveWorkstation(String identifier){
		this.activeStationIdentifier = identifier;
	}

}
