package controllers;

import interfaces.Printable;

import java.util.ArrayList;
import java.util.List;

import logic.users.Mechanic;

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
	 * Returns the list of workstations from the current assembly line with numbering if the current
	 * mechanic is not null.
	 * @return 	Null if the current mechanic is null.
	 * 			The list of workstations from the assembly line with numbering otherwise.
	 */
	public ArrayList<String> getWorkStationsFromAssemblyLine(){
		if(this.currentMechanic == null)
			return null;
		ArrayList<String> workstations = new ArrayList<String>();
		List<Printable> stations = this.currentMechanic.getWorkstationsFromAssemblyLine();
		int count = 1;
		for(Printable w : stations){
			workstations.add(w.getStringRepresentation() + ": " + count);
			count++;
		}
		return workstations;
	}
	
	/**
	 * Returns the list of assembly lines from the assembly line with numbering if the current
	 * mechanic is not null.
	 * @return 	Null if the current mechanic is null.
	 * 			The list of workstations from the assembly line with numbering otherwise.
	 */
	public ArrayList<String> getAssemblyLines() {
		if(this.currentMechanic == null)
			return null;
		ArrayList<String> assemblyLines = new ArrayList<String>();
		List<Printable> lines = this.currentMechanic.getAssemblyLines();
		int count = 1;
		for(Printable w : lines){
			assemblyLines.add(w.getStringRepresentation() + ": " + count);
			count++;
		}
		return assemblyLines;
	}
	
	/**
	 * Returns the list of tasks the current mechanic can perform at his chosen work station
	 * with numbering.
	 * @return 	Null if the mechanic is null.
	 * 			The list of tasks the mechanic can perform with numbering otherwise.
	 */
	public ArrayList<String> getTasks(){
		if(this.currentMechanic == null)
			return null;
		ArrayList<String> tasks = new ArrayList<String>();
		int count = 1;
		for(Printable task : this.currentMechanic.getAvailableTasks()){
			tasks.add(task.getStringRepresentation() + ": " + count);
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
		for(Printable task : this.currentMechanic.getAvailableTasks()){
			if(task.getStringRepresentation().equals(taskName))
				return task.getExtraInformation();
		}
		return null;
	}
	
	/**
	 * Performs the given task if the current mechanic is not null.
	 * @param taskName	The name of the task that needs to be performed.
	 * @param duration	How long the task took to perform.
	 */
	public void doTask(String taskName, int duration){
		if(this.currentMechanic == null)
			return;
		for(Printable task : this.currentMechanic.getAvailableTasks()){
			if(task.getStringRepresentation().equals(taskName))
				currentMechanic.doTask(task, duration);
		}
	}
	
	/**
	 * Sets the assembly line for the current mechanic to the given assembly line.
	 * @param assemblyLine		The new assembly line for the current mechanic.
	 */
	public void setAssemblyLine(String assemblyLine) {
		if(this.currentMechanic == null)
			return;
		for(Printable line : this.currentMechanic.getAssemblyLines()){
			if(line.getStringRepresentation().equals(assemblyLine))
				this.currentMechanic.setActiveAssemblyLine(line);
		}	
	}
	
	/**
	 * Sets the workstation for the current mechanic to the given workstation.
	 * @param workstationName	The new workstation for the current mechanic.
	 */
	public void setWorkStation(String workstationName){
		if(this.currentMechanic == null)
			return;
		for(Printable station : this.currentMechanic.getWorkstationsFromAssemblyLine()){
			if(station.getStringRepresentation().equals(workstationName))
				this.currentMechanic.setActiveWorkstation(station);
		}
	}

	/**
	 * Returns the list of tasks per workstation per assembly line, stating pending and 
	 * completed tasks or stating inactive if there are no tasks at the workstation.
	 * @return 	The list of tasks per workstation per assembly line, stating pending and completed tasks
	 *			or stating inactive if there are no tasks at the workstation.
	 */
	public List<String> getTasksPerWorkstation(){
		if(this.currentMechanic == null)
			return null;
		ArrayList<String> tasks = new ArrayList<String>();
		List<Printable> assemblyLines = this.currentMechanic.getAssemblyLines();
		for(Printable line : assemblyLines){
			tasks.add("= " + line.getStringRepresentation() + " =");
			this.currentMechanic.setActiveAssemblyLine(line);
			List<Printable> workStations = this.currentMechanic.getWorkstationsFromAssemblyLine();
			for(Printable stat : workStations){
				String temp = " " + stat.toString() + ":\n";
				if(this.currentMechanic.getAllTasks(stat).size() == 0)
					temp += "   Inactive.\n";
				else{
					for(Printable task : this.currentMechanic.getAllTasks(stat)){
						temp += "   -" + task.toString() + ": " + task.getStatus() + "\n";
					}
				}
				tasks.add(temp);
			}
		}
		return tasks;
	}
}
