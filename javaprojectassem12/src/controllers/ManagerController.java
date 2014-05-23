package controllers;

import interfaces.Printable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import logic.assemblyline.AssemblyLine;
import logic.assemblyline.OperationalStatus;
import logic.assemblyline.SchedulingStrategy;
import logic.order.Order;
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
	 * strategies for the active assembly line of the currentManager.
	 * @return a list of strings of the current strategy followed by the available scheduling 
	 * strategies for the active assembly line of the currentManager.
	 */
	public ArrayList<String> getStrategiesActiveLine() {
		if(currentManager == null)
			return null;
		ArrayList<String> strategies = new ArrayList<String>();
		for(Printable<SchedulingStrategy> s : currentManager.getStrategiesActiveLine()){
			strategies.add(s.getStringRepresentation());
		}
		return strategies;
	}
	

	/**
	 * Returns a list containing the list of the assembly lines combined with their strategy
	 * and a list of all possible strategies.
	 * @return a list containing the list of the assembly lines combined with their strategy
	 * and a list of all possible strategies.
	 */
	public ArrayList<ArrayList<String>> getStrategiesAllLines() {
		if(currentManager == null)
			return null;
		
		ArrayList<ArrayList<String>> strategies = new ArrayList<ArrayList<String>>();
		ArrayList<String> currentStrategies = new ArrayList<String>();
		ArrayList<String> possibleStrategies = new ArrayList<String>();
		
		Map<Printable<AssemblyLine>, List<Printable<SchedulingStrategy>>> allStrategies = currentManager.getAssemblyLinesStrategies();
		for(Entry<Printable<AssemblyLine>, List<Printable<SchedulingStrategy>>> entry : allStrategies.entrySet()) {
			List<Printable<SchedulingStrategy>> strategiesList = entry.getValue();
			currentStrategies.add(entry.getKey().getStringRepresentation() + ": " + strategiesList.get(0));
			possibleStrategies = new ArrayList<String>();
			for(int i = 1; i < strategiesList.size(); i++){
				possibleStrategies.add(strategiesList.get(i).getStringRepresentation());
			}
		}		
		Collections.sort(currentStrategies);
		strategies.add(currentStrategies);
		strategies.add(possibleStrategies);
		return strategies;
	}

	/**
	 * Returns a list of orders that are viable to be used by the batch specification scheduling strategy.
	 * @return	a list of orders that are viable to be used by the batch specification scheduling strategy.
	 */
	public ArrayList<String> getBatchListActiveLine() {
		if(currentManager == null)
			return null;
		currentBatchList = currentManager.getBatchListActiveLine();
		ArrayList<String> options = new ArrayList<String>();
		for(int i = 0; i < currentBatchList.size(); i++){
			String option = "   " + (i+1) + ": Option " + (i+1) + ":\n";
			option += getOptions(currentBatchList.get(i)) + "\n";
			options.add(option);
		}
		return options;
	}
	
	/**
	 * Returns a list of orders that are viable to be used by the batch specification scheduling strategy of all assembly lines combined.
	 * @return a list of orders that are viable to be used by the batch specification scheduling strategy of all assembly lines combined.
	 */
	public ArrayList<String> getBatchListAllLines() {
		if(currentManager == null)
			return null;
		currentBatchList = currentManager.getBatchListAllLines();
		ArrayList<String> options = new ArrayList<String>();
		for(int i = 0; i < currentBatchList.size(); i++){
			String option = "   " + (i+1) + ": Option " + (i+1) + ":\n";
			option += getOptions(currentBatchList.get(i)) + "\n";
			options.add(option);
		}
		return options;
	}
	
	/**
	 * Returns the options that belong to the given order.
	 * @param order	The order for which the options need to be returned.
	 * @return the options that belong to the given order.
	 */
	private String getOptions(Order order) {
		String options = "";
		List<Task> tasks = order.getTasks();
		for(Task t : tasks){
			options += "      - " + t.getVehiclePart().toString() + "\n";
		}
		return options;
	}

	/**
	 * Tries to change the current strategy to the FIFO strategy. Returns true if successful,
	 * false otherwise.
	 * @return 	True if the strategy has been changed successful.
	 * 			False otherwise.
	 */
	public boolean changeToFIFOActiveLine(){
		if(!currentManager.getStrategiesActiveLine().get(0).toString().equals("FIFO")){
			currentManager.changeStrategyActiveAssemblyLine(null);
			return true;
		}
		return false;
	}
	
	/**
	 * Changes the current strategy of all assembly lines to the FIFO strategy.
	 */
	public void changeToFIFOAllLines(){
		currentManager.changeStrategyAllLines(null);
	}
	
	/**
	 * Tries to change the current strategy of the active assembly line to the batch 
	 * processing strategy.
	 * @param index	The index of the order for batch processing. 
	 */
	public void changeStrategyToBatchProcessingActiveLine(int index){
		if(index+1>currentBatchList.size() || currentBatchList==null)
			return;
		currentManager.changeStrategyActiveAssemblyLine(currentBatchList.get(index));
		currentBatchList.clear();
	}
	
	/**
	 * Tries to change the current strategy of all assembly lines to the batch 
	 * processing strategy.
	 * @param index	The index of the order for batch processing. 
	 */
	public void changeStrategyToBatchProcessingAllLines(int index) {
		if(index+1>currentBatchList.size() || currentBatchList==null)
			return;
		currentManager.changeStrategyAllLines(currentBatchList.get(index));
		currentBatchList.clear();
	}

	/**
	 * Returns the list of assembly lines from the assembly line with numbering if the current
	 * mechanic is not null.
	 * @return 	Null if the current mechanic is null.
	 * 			The list of workstations from the assembly line with numbering otherwise.
	 */
	public ArrayList<String> getAssemblyLines() {
		if(this.currentManager == null)
			return null;
		ArrayList<String> assemblyLines = new ArrayList<String>();
		List<Printable<AssemblyLine>> lines = this.currentManager.getAssemblyLines();
		int count = 1;
		for(Printable<AssemblyLine> w : lines){
			assemblyLines.add(w.getStringRepresentation() + ": " + count);
			count++;
		}
		return assemblyLines;
	}
	
	/**
	 * Returns a string representation of the status of the current AssemblyLine.
	 * @return a string representation of the status of the current AssemblyLine.
	 */
	public String getCurrentAssemblyLineStatus() {
		if(this.currentManager == null || this.currentManager.getActiveAssemblyLine() == null)
			return null;
		Map<Printable<AssemblyLine>, Printable<OperationalStatus>> assemblyLineStatuses = currentManager.getAssemblyLinesStatuses();
		Printable<OperationalStatus> currentAssemblyLine = assemblyLineStatuses.get(currentManager.getActiveAssemblyLine());
		return currentAssemblyLine + "";
	}

	/**
	 * Sets the assembly line for the current mechanic to the given assembly line.
	 * @param assemblyLine		The new assembly line for the current mechanic.
	 */
	public void setAssemblyLine(String assemblyLine) {
		if(this.currentManager == null)
			return;
		for(Printable<AssemblyLine> line : this.currentManager.getAssemblyLines()){
			if(line.getStringRepresentation().equals(assemblyLine))
				this.currentManager.setActiveAssemblyLine(line);
		}	
	}
	
	/**
	 * Changes the status of the current AssemblyLine to the given status.
	 * @param newStatus	The new status for the given AssemblyLine.
	 */
	public void changeAssemblyLineStatus(String newStatus) {
		OperationalStatus[] availableStatuses = OperationalStatus.values();
		for(OperationalStatus s : availableStatuses){
			if(s.toString().equals(newStatus)){
				currentManager.changeAssemblyLineStatus(s);
			}
		}
	}
}
