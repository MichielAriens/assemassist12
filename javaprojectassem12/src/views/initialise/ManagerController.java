package views.initialise;

import java.util.ArrayList;
import java.util.List;

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
			for(Task task : stat.getRequiredTasks()){
				String status = "Pending";
				if(task.isComplete())
					status = "Completed";
				temp += "   -" + task.toString() + ": " + status + "\n";
			}
			tasks.add(temp);
		}
		return tasks;
	}

}
