package logic.users;

import interfaces.Printable;

import java.util.List; 

/**
 * Class used to describe a mechanic working on an assembly line in a car manufacturing company.
 */
public class Mechanic extends User{
	
	/**
	 * The printable instance of the workstation this mechanic is currently working on.
	 */
	private Printable activeStation = null;
	
	/**
	 * The printable instance of the assembly line this mechanic is currently working on.
	 */
	private Printable activeAssemblyLine = null;
	
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
	public Printable getActiveWorkstation(){
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
	 * @param duration	How long it took to perform the task.
	 */
	//TODO: checpPhaseDuration kan evengoed in company gebeuren?
	public void doTask(Printable task, int duration){
		if(company.checkPhaseDuration(duration,activeAssemblyLine)){
			boolean performed = company.doTask(task, activeAssemblyLine, duration);
			if(performed){
				if(company.moveAssemblyLine(duration));
			}
		}
	}
	
	/**
	 * Returns a list tasks that are currently pending at this mechanic's active workstation.
	 * @return	null	if the mechanic is not currently posted.
	 * 			The list of tasks otherwise.
	 */
	public List<Printable> getAvailableTasks(){
		if(!isPosted() || activeAssemblyLine==null)
			return null;
		return this.company.getRequiredTasks(this.activeStation, this.activeAssemblyLine);
	}
	
	/**
	 * Returns a list of all tasks at a given workstation.
	 * @param station	The a copy of the workstation for which the tasks are needed.
	 * @return	A list of tasks at the given workstation.
	 */
	public List<Printable> getAllTasks(Printable station){
		if(activeAssemblyLine==null || station == null)
			return null;
		return this.company.getAllTasksAt(station, activeAssemblyLine);
	}
	
	/**
	 * Returns the list of workstations from the active assembly line of the car manufacturing company.
	 * @return the list of workstations from the active assembly line of the car manufacturing company.
	 */
	public List<Printable> getWorkstationsFromAssemblyLine(){
		if(activeAssemblyLine==null)
			return null;
		return this.company.getWorkStationsFromAssemblyLine(activeAssemblyLine);
	}
	
	/**
	 * Returns the list of all workstations from all assembly lines of the car manufacturing company.
	 * @return the list of all workstations from all assembly lines of the car manufacturing company.
	 */
	public List<Printable> getWorkstations() {
		if(company == null)
			return null;
		return this.company.getWorkStations();
	}
	
	/**
	 * Returns the printable list of assembly lines of the car manufacturing company.
	 * @return the printable list of assembly lines of the car manufacturing company.
	 */
	public List<Printable> getAssemblyLines() {
		if(company == null)
			return null;
		return this.company.getAssemblyLines();
	}

	/**
	 * Set the active workstation to the given workstation.
	 * @param workstation	The new active workstation.
	 */
	public void setActiveWorkstation(Printable station){
		this.activeStation = station;
	}
	
	/**
	 * Set the active assembly line to the given assembly line.
	 * @param line	The new active assembly line.
	 */
	public void setActiveAssemblyLine(Printable line){
		this.activeAssemblyLine = line;
	}
}
