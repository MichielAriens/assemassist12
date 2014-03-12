package controllers;

import java.util.ArrayList;

import logic.users.Mechanic;
import logic.workstation.Task;
import logic.workstation.Workstation;

public class MechanicController extends UserController{
	
	private Mechanic mechanic;
	
	public void setMechanic(Mechanic mechanic) {
		this.mechanic = mechanic;
	}

	@Override
	public String getUserName() {
		if(this.mechanic != null)
			return this.mechanic.getUserName();
		return null;
	}
	
	public ArrayList<String> getWorkStations(){
		if(this.mechanic == null)
			return null;
		ArrayList<String> workstations = new ArrayList<String>();
		Workstation[] stations = this.mechanic.getAvailableWorkstations();
		int count = 1;
		for(Workstation w : stations){
			workstations.add(w.toString() + ": " + count);
			count++;
		}
		return workstations;
	}
	
	public ArrayList<String> getTasks(){
		if(this.mechanic == null)
			return null;
		ArrayList<String> tasks = new ArrayList<String>();
		int count = 1;
		for(Task task : this.mechanic.getAvailableTasks()){
			if(!task.isComplete()){
				tasks.add(task.toString() + ": " + count);
				count++;
			}
		}
		return tasks;
	}
	
	public String getTaskInformation(String taskName){
		if(this.mechanic == null)
			return null;
		for(Task task : this.mechanic.getActiveWorkstation().getRequiredTasks()){
			if(task.toString().equals(taskName))
				return task.getDescription();
		}
		return null;
	}
	
	public void doTask(String taskName){
		if(this.mechanic == null)
			return;
		for(Task task : this.mechanic.getActiveWorkstation().getRequiredTasks()){
			if(task.toString().equals(taskName)){
				this.mechanic.doTask(task);
				return;
			}
		}
	}
	
	public void setWorkStation(String name){
		if(this.mechanic == null)
			return;
		for(Workstation workstation :this.mechanic.getAvailableWorkstations()){
			if(workstation.toString().equals(name))
				this.mechanic.setActiveWorkstation(workstation);
		}
	}

}
