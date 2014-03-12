package controllers;

import java.util.ArrayList;

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
	 * @return The user name of the current mechanic.
	 * @return Null if the current mechanic is null.
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
	 * @return The list of workstations from the assembly line with numbering.
	 * @return Null if the current mechanic is null.
	 */
	public ArrayList<String> getWorkStations(){
		if(this.currentMechanic == null)
			return null;
		ArrayList<String> workstations = new ArrayList<String>();
		Workstation[] stations = this.currentMechanic.getAvailableWorkstations();
		int count = 1;
		for(Workstation w : stations){
			workstations.add(w.toString() + ": " + count);
			count++;
		}
		return workstations;
	}
	
	/**
	 * Returns the list of tasks the current mechanic can perform with numbering.
	 * @return Null if the mechanic is null.
	 * @return The list of tasks the mechanic can perform with numbering.
	 */
	public ArrayList<String> getTasks(){
		if(this.currentMechanic == null)
			return null;
		ArrayList<String> tasks = new ArrayList<String>();
		int count = 1;
		for(Task task : this.currentMechanic.getAvailableTasks()){
			if(!task.isComplete()){
				tasks.add(task.toString() + ": " + count);
				count++;
			}
		}
		return tasks;
	}
	
	/**
	 * Returns the task information for the given task.
	 * @param taskName	The task for which the information needs to be returned.
	 * @return Null if the mechanic is null.
	 * @return Null if the given task name does not correspond with any of the tasks 
	 * 				of the workstation the current mechanic is currently working on. 
	 * @return The task information otherwise.
	 */
	public String getTaskInformation(String taskName){
		if(this.currentMechanic == null)
			return null;
		for(Task task : this.currentMechanic.getActiveWorkstation().getRequiredTasks()){
			if(task.toString().equals(taskName))
				return task.getDescription();
		}
		return null;
	}
	
	/**
	 * Performs the given task if the current mechanic is not null.
	 * @param taskName	The name of the task that needs to be performed.
	 */
	public void doTask(String taskName){
		if(this.currentMechanic == null)
			return;
		for(Task task : this.currentMechanic.getActiveWorkstation().getRequiredTasks()){
			if(task.toString().equals(taskName)){
				this.currentMechanic.doTask(task);
				return;
			}
		}
	}
	
	/**
	 * Sets the workstation for the current mechanic to the given workstation.
	 * @param workstationName	The new workstation for the current mechanic.
	 */
	public void setWorkStation(String workstationName){
		if(this.currentMechanic == null)
			return;
		for(Workstation workstation :this.currentMechanic.getAvailableWorkstations()){
			if(workstation.toString().equals(workstationName))
				this.currentMechanic.setActiveWorkstation(workstation);
		}
	}

}
