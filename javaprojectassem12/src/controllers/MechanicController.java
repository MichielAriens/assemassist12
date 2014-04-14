package controllers;

import java.util.ArrayList;
import java.util.List;

import logic.users.Mechanic;
import logic.workstation.Task;
import logic.workstation.Workstation;

/**
 * Class used to form a link between the user interface and the Mechanic class.
 */
public class MechanicController extends UserController{
	
	/**
	 * The current mechanic.
	 */
	private Mechanic currentMechanic;
	
	/**
	 * Sets the current mechanic to the given mechanic.
	 * @param mechanic	The new mechanic.
	 */
	public void setMechanic(Mechanic mechanic) {
		this.currentMechanic = mechanic;
	}

	/**
	 * Returns the user name of the current mechanic.
	 * @return 	Null if the current mechanic is null.
	 * 			The user name of the current mechanic otherwise.
	 */
	@Override
	public String getUserName() {
		if(this.currentMechanic != null)
			return this.currentMechanic.getUserName();
		return null;
	}
	
	/**
	 * Returns the list of workstations from the assembly line with numbering if the current
	 * mechanic is not null.
	 * @return 	Null if the current mechanic is null.
	 * 			The list of workstations from the assembly line with numbering otherwise.
	 */
	public ArrayList<String> getWorkStations(){
		if(this.currentMechanic == null)
			return null;
		ArrayList<String> workstations = new ArrayList<String>();
		List<Workstation> stations = this.currentMechanic.getWorkstations();
		int count = 1;
		for(Workstation w : stations){
			workstations.add(w.toString() + ": " + count);
			count++;
		}
		return workstations;
	}
	
	/**
	 * Returns the list of tasks the current mechanic can perform with numbering.
	 * @return 	Null if the mechanic is null.
	 * 			The list of tasks the mechanic can perform with numbering otherwise.
	 */
	public ArrayList<String> getTasks(){
		if(this.currentMechanic == null)
			return null;
		ArrayList<String> tasks = new ArrayList<String>();
		int count = 1;
		for(String taskStr : this.currentMechanic.getAvailableTaskIdentifiers()){
			tasks.add(taskStr + ": " + count);
			count++;
		}
		return tasks;
	}
	
	/**
	 * Returns the task information for the given task.
	 * @param taskName	The task for which the information needs to be returned.
	 * @return 	Returns null if:
	 * 			- the mechanic is null
	 * 			- the given task name does not correspond with any of the tasks of the workstation
	 * 			Returns the task information otherwise.
	 */
	public String getTaskInformation(String taskName){
		if(this.currentMechanic == null)
			return null;
		return this.currentMechanic.getTaskDescription(taskName);
	}
	
	/**
	 * Performs the given task if the current mechanic is not null.
	 * @param taskName	The name of the task that needs to be performed.
	 * @param duration	How long the task took to perform.
	 */
	public void doTask(String taskName, int duration){
		if(this.currentMechanic == null)
			return;
		this.currentMechanic.doTask(taskName, duration);
	}
	
	/**
	 * Sets the workstation for the current mechanic to the given workstation.
	 * @param workstationName	The new workstation for the current mechanic.
	 */
	public void setWorkStation(String workstationName){
		if(this.currentMechanic == null)
			return;
		this.currentMechanic.setActiveWorkstation(workstationName);
	}

	/**
	 * Returns the list of tasks per workstation, stating pending and completed tasks
	 * or stating inactive if there are no tasks at the workstation.
	 * @return 	The list of tasks per workstation, stating pending and completed tasks
	 *			or stating inactive if there are no tasks at the workstation.
	 */
	public List<String> getTasksPerWorkstation(){
		if(this.currentMechanic == null)
			return null;
		List<Workstation> workStations = this.currentMechanic.getWorkstations();
		ArrayList<String> tasks = new ArrayList<String>();
		for(Workstation stat : workStations){
			String temp = stat.toString() + ":\n";
			if(this.currentMechanic.getTaskStatus(stat.toString()).size() == 0)
				temp += "Inactive.\n";
			else{
				for(String taskStr : this.currentMechanic.getTaskStatus(stat.toString())){
					temp += "   -" + taskStr + "\n";
				}
			}
			tasks.add(temp);
		}
		return tasks;
	}

}
