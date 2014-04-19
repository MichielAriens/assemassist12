package controllers;

import java.util.ArrayList;
import java.util.List;

import logic.assemblyline.SchedulingStrategy;
import logic.car.Order;
import logic.users.Manager;
import logic.workstation.Task;

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
		if(currentBatchList == null){
			return null;
		}
		else{
			ArrayList<String> carOptions = new ArrayList<String>();
			for(int i = 0; i < currentBatchList.size(); i++){
				String carOption = "   " + (i+1) + ": Option " + (i+1) + ":\n";
				carOption += getCarOptions(currentBatchList.get(i)) + "\n";
				carOptions.add(carOption);
			}
			return carOptions;
		}
	}
	
	private String getCarOptions(Order order) {
		String options = "";
		List<Task> tasks = order.getTasks();
		for(Task t : tasks){
			options += "      - " + t.getCarPart().toString() + "\n";
		}
		return options;
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
	public void changeStrategyToBatchProcessing(int index){
			currentManager.changeStrategy(currentBatchList.get(index));
			currentBatchList.clear();
	}
}
