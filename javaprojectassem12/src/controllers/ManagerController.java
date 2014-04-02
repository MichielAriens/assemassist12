package controllers;

import java.util.ArrayList;
import java.util.List;

import logic.assemblyline.SchedulingStrategy;
import logic.car.Order;
import logic.users.Manager;
import logic.workstation.Task;
import logic.workstation.Workstation;

/**
 * Class used to form a link between the user interface and the Manager class.
 */
public class ManagerController extends UserController{

	/**
	 * The current manager.
	 */
	private Manager currentManager;
	
	private List<Order> currentBatchList;
	
	/**
	 * Sets the current manager to the given manager.
	 * @param manager	The new manager.
	 */
	public void setManager(Manager manager) {
		this.currentManager = manager;
	}

	/**
	 * Returns the user name of the current manager.
	 * @return 	Null if the current manager is null.
	 * 			The user name of the current manager otherwise.
	 */
	@Override
	public String getUserName() {
		if(this.currentManager != null)
			return currentManager.getUserName();
		return null;
	}
	
	/**
	 * Returns the list of tasks per workstation, stating pending and completed tasks
	 * or stating inactive if there are no tasks at the workstation.
	 * @return 	The list of tasks per workstation, stating pending and completed tasks
	 *			or stating inactive if there are no tasks at the workstation.
	 */
	public List<String> getTasksPerWorkstation(){
		if(this.currentManager == null)
			return null;
		List<Workstation> workStations = this.currentManager.getWorkstations();
		List<String> tasks = new ArrayList<String>();
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
	
	/**
	 * Returns the list of unfinished tasks over all workstations.
	 * @return the list of unfinished tasks over all workstations.
	 */
	public List<String> getUnfinishedTasks(){
		if(this.currentManager == null)
			return null;
		List<Workstation> workStations = this.currentManager.getWorkstations();
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
	
	/**
	 * Returns the future status of the assembly line.
	 * @return Null if the current manager is null.
	 * @return The future status of the assembly line otherwise.
	 */
//	public List<String> getFutureStatus(){
//		if(this.currentManager == null)
//			return null;
//		List<Workstation> workStations = this.currentManager.getWorkstations();
//		ArrayList<String> tasks = new ArrayList<String>();
//		for(int i = 0; i < workStations.size(); i++){
//			String temp = workStations.get(i).toString() + ":\n";
//			if(currentManager.askFutureSchedule().get(i) == null)
//				temp += "Inactive.\n";
//			else{
//				for(Task task : currentManager.askFutureSchedule().get(i).getTasks()){
//					if(workStations.get(i).isCompatibleTask(task))
//						temp += "   -" + task.toString() + ": Pending\n";
//				}
//			}
//			tasks.add(temp);
//		}
//		return tasks;
//	}
	
	/**
	 * Moves the assembly line with the given shift duration. 
	 * @param phaseDuration	The time spent on the current phase in minutes.
	 * @return False if the current manager is null.
	 * @return False if the assembly line can not be moved.
	 * @return True if the assembly line has been moved successfully.
	 */
//	public boolean moveAssemblyLine(int phaseDuration){
//		if(this.currentManager == null)
//			return false;
//		return this.currentManager.moveAssemblyLine(phaseDuration);
//	}

	public String getStatistics() {
		return this.currentManager.getStatistics();		
	}
	
	public ArrayList<String> getStrategies() {
		ArrayList<String> strategies = new ArrayList<String>();
		for(SchedulingStrategy s : currentManager.getStrategies()){
			strategies.add(s.toString());
		}
		return strategies;
	}

	public ArrayList<String> getBatchList() {
		currentBatchList = currentManager.getBatchList();
		if(currentBatchList.isEmpty()){
			return null;
		}
		else{
			ArrayList<String> carOptions = new ArrayList<String>();
			for(int i = 0; i < currentBatchList.size(); i++){
				String carOption = "Option " + (i+1) + ":\n";
				//TODO: caroptions erbij zetten
				
				carOptions.add(carOption);
			}
			return carOptions;
		}
	}
	
	//TODO
	public boolean changeStrategyToFIFO(){
		if(!currentManager.getStrategies().get(0).toString().equals("FIFO")){
			currentManager.changeStrategy(null);
			return true;
		}
		return false;
	}
	
	//TODO
	public boolean changeStrategyToBatchProcessing(int index){
		if(!currentManager.getStrategies().get(0).equals("Batch Processing")){
			currentManager.changeStrategy(currentBatchList.get(index));
			currentBatchList.clear();
			return true;
		}
		return false;
	}
}
