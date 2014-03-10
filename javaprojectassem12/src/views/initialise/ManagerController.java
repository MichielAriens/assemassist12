package views.initialise;

import java.util.ArrayList;
import java.util.List;

import logic.car.CarOrder;
import logic.users.Manager;
import logic.workstation.Task;
import logic.workstation.Workstation;

public class ManagerController extends UserController{

	private Manager manager;
	
	public void setManager(Manager manager) {
		this.manager = manager;
	}

	@Override
	public String getUserName() {
		if(this.manager != null)
			return manager.getUserName();
		return null;
	}
	
	public List<String> getTasksPerWorkstation(){
		if(this.manager == null)
			return null;
		Workstation[] workStations = this.manager.getWorkstations();
		ArrayList<String> tasks = new ArrayList<String>();
		for(Workstation stat : workStations){
			String temp = stat.toString() + ":\n";
			if(stat.getRequiredTasks().size() == 0)
				temp += "Inactive.\n";
			else{
				for(Task task : stat.getRequiredTasks()){
					String status = "Pending";
					if(task.isComplete())
						status = "Completed";
					temp += "   -" + task.toString() + ": " + status + "\n";
				}
			}
			tasks.add(temp);
		}
		return tasks;
	}
	
	public List<String> getUnfinishedTasks(){
		if(this.manager == null)
			return null;
		Workstation[] workStations = this.manager.getWorkstations();
		ArrayList<String> tasks = new ArrayList<String>();
		for(Workstation stat : workStations){
			if(stat.getRequiredTasks().size()>0){
				String temp = stat.toString() + ":\n";
				for(Task task : stat.getRequiredTasks()){
					if(!task.isComplete()){
						temp += "   -Unfinished task: " + task.toString() + "\n";
					}
				}
				tasks.add(temp);
			}
		}
		return tasks;
	}
	
	public List<String> getFutureStatus(){
		if(this.manager == null)
			return null;
		Workstation[] workStations = this.manager.getWorkstations();
		ArrayList<String> tasks = new ArrayList<String>();
		for(int i = 0; i < workStations.length; i++){
			String temp = workStations[i].toString() + ":\n";
			if(manager.askFutureSchedule().get(i) == null)
				temp += "Inactive.\n";
			else{
				for(Task task : manager.askFutureSchedule().get(i).getTasks()){
					if(workStations[i].isCompatibleTask(task))
						temp += "   -" + task.toString() + ": Pending\n";
				}
			}
			tasks.add(temp);
		}
		return tasks;
	}
	
	public boolean moveAssemblyLine(int shiftDuration){
		if(this.manager == null)
			return false;
		return this.manager.moveAssemblyLine(shiftDuration);
	}

}
