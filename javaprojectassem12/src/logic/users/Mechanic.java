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
	 * @return False if the active workstation is null.
	 * @return True otherwise.
	 */
	public boolean isPosted(){
		return this.getActiveWorkstation() != null;
	}
	
	/**
	 * Performs a task if the task is compatible with the active workstation of the mechanic.
	 * @param task	The task that needs to be performed.
	 * @return True if the task is compatible with the active workstation.
	 * @return False otherwise.
	 */
	public boolean doTask(Task task){
		if (getActiveWorkstation().isCompatibleTask(task)){
			task.perform();
			return true;
		}
		return false;
	}

	public void work(float actualWork) {
		
	}
	
	/**
	 * Returns the list of tasks that need to be performed at the active workstation or null
	 * if the mechanic has no active workstation.
	 * @return	The list of tasks that need to be performed at the active workstation 
	 * 			if the active workstation is not null.
	 * @return	Null otherwise.
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
	public Workstation[] getAvailableWorkstations(){
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
