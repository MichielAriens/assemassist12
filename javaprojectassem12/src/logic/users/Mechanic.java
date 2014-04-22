package logic.users;

import java.util.List; 

import logic.workstation.Task;
import logic.workstation.Workstation;

/**
 * Class used to describe a mechanic working on an assembly line in a car manufacturing company.
 */
public class Mechanic extends User{
	
	/**
	 * The string identifier of the workstation this mechanic is currently working on.
	 */
	private Workstation activeStation = null;
	
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
	public Workstation getActiveWorkstation(){
		return this.activeStation;
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
	 * @param task	A copy of the task that needs to be performed.
	 */
	public void doTask(Task task, int duration){
		boolean performed = company.doTask(task);
		if(performed){
			setMaxPhaseDuration(duration);
			if(company.moveAssemblyLine(maxPhaseDuration))
				resetMaxPhaseDuration();
		}
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
	 * Returns a list tasks that are currently pending at this mechanic's active workstation.
	 * @return	null	if the mechanic is not currently posted.
	 * 			The list of tasks otherwise.
	 */
	public List<Task> getAvailableTasks(){
		if(!isPosted())
			return null;
		return this.company.getRequiredTasks(this.activeStation);
	}
	
	/**
	 * Returns a list of all tasks at a given workstation.
	 * @param station	The a copy of the workstation for which the tasks are needed.
	 * @return	A list of tasks at the given workstation.
	 */
	public List<Task> getAllTasks(Workstation station){
		return this.company.getAllTasks(station);
	}
	
	/**
	 * Returns the list of workstations from the assembly line of the car manufacturing company.
	 * @return the list of workstations from the assembly line of the car manufacturing company.
	 */
	public List<Workstation> getWorkstations(){
		return this.company.getWorkStations();
	}
	
	/**
	 * Set the active workstation to the given workstation.
	 * @param identifier
	 */
	public void setActiveWorkstation(Workstation station){
		this.activeStation = station;
	}

}
