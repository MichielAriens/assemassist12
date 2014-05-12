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
	
	/**
	 * The current list of orders that are viable to be used by the batch specification 
	 * scheduling strategy.
	 */
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
	 * Returns a string representation of the current statistics.
	 * @return	a string representation of the current statistics.
	 */
	public String getStatistics() {
		return this.currentManager.getStatistics();		
	}
	
	/**
	 * Returns a list of strings of the current strategy followed by the available scheduling 
	 * strategies.
	 * @return a list of strings of the current strategy followed by the available scheduling 
	 * strategies.
	 */
	public ArrayList<String> getStrategies() {
		ArrayList<String> strategies = new ArrayList<String>();
		for(SchedulingStrategy s : currentManager.getStrategies()){
			strategies.add(s.toString());
		}
		return strategies;
	}

	/**
	 * Returns a list of orders that are viable to be used by the batch specification scheduling strategy.
	 * @return	a list of orders that are viable to be used by the batch specification scheduling strategy.
	 */
	public ArrayList<String> getBatchList() {
		if(currentManager==null)
			return null;
		currentBatchList = currentManager.getBatchList();
		ArrayList<String> carOptions = new ArrayList<String>();
		for(int i = 0; i < currentBatchList.size(); i++){
			String carOption = "   " + (i+1) + ": Option " + (i+1) + ":\n";
			carOption += getCarOptions(currentBatchList.get(i)) + "\n";
			carOptions.add(carOption);
		}
		return carOptions;
	}
	
	/**
	 * Returns the options that belong to the given car order.
	 * @param order	The order for which the options need to be returned.
	 * @return the options that belong to the given car order.
	 */
	private String getCarOptions(Order order) {
		String options = "";
		List<Task> tasks = order.getTasks();
		for(Task t : tasks){
			options += "      - " + t.getCarPart().toString() + "\n";
		}
		return options;
	}

	/**
	 * Tries to change the current strategy to the FIFO strategy. Returns true if successful,
	 * false otherwise.
	 * @return 	True if the strategy has been changed successful.
	 * 			False otherwise.
	 */
	public boolean changeStrategyToFIFO(){
		if(!currentManager.getStrategies().get(0).toString().equals("FIFO")){
			currentManager.changeStrategy(null);
			return true;
		}
		return false;
	}
	
	/**
	 * Tries to change the current strategy to the batch processing strategy. Returns true if successful,
	 * false otherwise.
	 * @param index	The index of the order for batch processing. 
	 */
	public void changeStrategyToBatchProcessing(int index){
		if(index+1>currentBatchList.size() || currentBatchList==null)
			return;
		currentManager.changeStrategy(currentBatchList.get(index));
		currentBatchList.clear();
	}

	public ArrayList<String> getAssemblyLines() {
		ArrayList<String> assemblyLines = new ArrayList<String>();
		for(String s : currentManager.getAssemblyLines()){
			assemblyLines.add(s.toString());
		}		
		return assemblyLines;
	}
	
	public ArrayList<String> getAssemblyLinesStatuses() {
		ArrayList<String> statuses = new ArrayList<String>();
		for(String s : currentManager.getAssemblyLinesStatuses()){
			statuses.add(s.toString());
		}		
		return statuses;
	}
}
