package logic.users;

import java.util.List; 
import logic.workstation.Task;
import logic.workstation.Workstation;

/**
 * Class used to describe a mechanic working on an assembly line in a car manufacturing company.
 */
public class Mechanic extends User{
	
	/**
	 * The workstation this mechanic is currently working on.
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
	 * @param task	The task that needs to be performed.
	 * @return 	True if the task is compatible with the active workstation.
	 * 			False otherwise.
	 */
	public boolean doTask(Task task, int duration){
		if (getActiveWorkstation().isCompatibleTask(task)){
			task.perform();
			setMaxPhaseDuration(duration);
			if(company.moveAssemblyLine(maxPhaseDuration))
				resetMaxPhaseDuration();
			return true;
		}
		return false;
	}

	/**
	 * Sets the maximum phase duration to the given duration if the given duration
	 * is greater than the maximum phase duration.
	 * @param duration
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
	 * Returns the list of tasks that need to be performed at the active workstation or null
	 * if the mechanic has no active workstation.
	 * @return	The list of tasks that need to be performed at the active workstation 
	 * 			if the active workstation is not null.
	 * 			Null otherwise.
	 */
	public List<Task> getAvailableTasks(){
		if(!isPosted())
			return null;
		return this.activeStation.getRequiredTasks();
	}
	
	/**
	 * Returns the list of workstations from the assembly line of the car manufacturing company.
	 * @return the list of workstations from the assembly line of the car manufacturing company.
	 */
	public List<Workstation> getWorkstations(){
		return this.company.getWorkStations();
	}
	
	/**
	 * Sets the active workstation to the given workstation.
	 * @param station	The new active workstation.
	 */
	public void setActiveWorkstation(Workstation station){
		this.activeStation = station;
	}

}
