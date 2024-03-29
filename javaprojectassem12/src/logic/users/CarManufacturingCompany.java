package logic.users;

import interfaces.Printable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import logic.assemblyline.AssemblyLine;
import logic.assemblyline.AssemblyLineScheduler;
import logic.assemblyline.OperationalStatus;
import logic.assemblyline.SchedulingStrategy;
import logic.order.Order;
import logic.workstation.Task;
import logic.workstation.Workstation;

/**
 * Class used to describe a car manufacturing company.
 */
public class CarManufacturingCompany {
	
	/**
	 * HashMap holding all the users of this system together with their user names.
	 */
	private HashMap<String,User> users;
	
	/**
	 * List of all valid names for garage holders.
	 */
	private ArrayList<String> validNamesGarageHolders = new ArrayList<String>();
	
	/**
	 * List of all valid names for managers.
	 */
	private ArrayList<String> validNamesManager = new ArrayList<String>();
	
	/**
	 * List of all valid names for car mechanic.
	 */
	private ArrayList<String> validNamesMechanic = new ArrayList<String>();
	
	/**
	 * List of all valid names for customs shop managers.
	 */
	private ArrayList<String> validNamesCustomsManager = new ArrayList<String>();
	
	/**
	 * Variable holding the assembly line of this car manufacturing company.
	 */
	private AssemblyLineScheduler assemblyLineScheduler;
	
	/**
	 * Makes a new car manufacturing company.
	 */
	public CarManufacturingCompany(){
		this.assemblyLineScheduler = new AssemblyLineScheduler();
		this.users = new HashMap<String,User>();
		this.initialise();
	}

	/**
	 * Initializes the users of this car manufacturing company.
	 */
	private void initialise() {
		validNamesGarageHolders.add("Jeroen");
		validNamesGarageHolders.add("gar");
		validNamesManager.add("Wander");
		validNamesManager.add("man");
		validNamesMechanic.add("Joren");
		validNamesMechanic.add("mech");
		validNamesCustomsManager.add("Michiel");
		validNamesCustomsManager.add("cust");
	}

	/**
	 * Checks if the given user name is valid and returns the corresponding user.
	 * If the given user name is valid and the user does not exist yet, the user is
	 * added to the HashMap of users. If the given user name is not valid, 
	 * this method returns null.
	 * @param userName		The user name used to login.
	 * @return	Returns the user corresponding to the given user name otherwise if:
	 * 			- the HashMap of users contains the given user name as a key,
	 * 			- the list of valid manager names contains the given user name,
	 * 			- the list of valid garage holders names contains the given user name or
	 * 			- the list of valid mechanic names contains the given user name.
	 * 			- the list of valid customs shop manager names contains the given user name.
	 * 			Returns null otherwise.
	 */
	public User logIn(String userName){
		if(users.containsKey(userName)){
			return users.get(userName);
		}
		if(validNamesManager.contains(userName)){
			Manager user = new Manager(this, userName);
			users.put(userName, user);
			return user;
		}
		if(validNamesGarageHolders.contains(userName)){
			GarageHolder user = new GarageHolder(this, userName);
			users.put(userName, user);
			return user;
		}
		if(validNamesMechanic.contains(userName)){
			Mechanic user = new Mechanic(this, userName);
			users.put(userName, user);
			return user;
		}
		if(validNamesCustomsManager.contains(userName)){
			CustomsManager user = new CustomsManager(this, userName);
			users.put(userName, user);
			return user;
		}
		return null;
	}
	
	/**
	 * Adds the given order to the assembly line if the given order is not null.
	 * @param order	The order which needs to be added to the assembly line.
	 */
	public void addOrder(Order order) {
		if(order != null)
			this.assemblyLineScheduler.addOrder(order);
	}
	
	/**
	 * Method to return the list of representations of workstations of the given assembly line 
	 * of this car manufacturing company.
	 * @param assemblyLine The assembly line for which we want the workstations.
	 * @return	The representations of the workstations of the assembly line.
	 */
	public List<Printable<Workstation>> getWorkStationsFromAssemblyLine(Printable<AssemblyLine> assemblyLine){
		
		return assemblyLineScheduler.getWorkStationsFromAssemblyLine(assemblyLine);
	}
	
	/**
	 * Returns the list of representations of all workstations from all assembly lines of the car manufacturing company.
	 * @return the list of representations of all workstations from all assembly lines of the car manufacturing company.
	 */
	public List<Printable<Workstation>> getWorkStations() {
		return assemblyLineScheduler.getAllWorkstations();
	}
	
	/**
	 * Returns the current time of the system.
	 * @return	The current time of the system.
	 */
	public DateTime getCurrentTime(){
		return this.assemblyLineScheduler.getCurrentTime();
	}
	
	
	/**
	 * Completes the task corresponding to the given task representation in the given axssembly line.
	 * @param task	A copy of the task that needs to be completed.
	 * @param assemblyLine The assembly line on which we want to perform a task.
	 * @param duration The duration needed to complete the given task.
	 * @return	True if the task is completed successfully
	 * 			False the task could not be completed.
	 */
	public boolean doTask(Printable<Task> task, Printable<AssemblyLine> assemblyLine, int duration){
		if(checkPhaseDuration(duration, assemblyLine))
			return assemblyLineScheduler.doTask(task, assemblyLine, duration);
		return false;
	}
	
	/**
	 * Returns a string representation of the current statistics.
	 * @return	A string representation of the current statistics.
	 */
	public String getStatistics() {
		return assemblyLineScheduler.getStatistics();
	}
	
	/**
	 * Returns a list of representations of the the current strategies followed by the available scheduling strategies in the given assemblyLine.
	 * @param activeAssemblyLine The assembly line for which we want the strategies.
	 * @return a list of the current strategies followed by the available scheduling strategies.
	 */
	public List<Printable<SchedulingStrategy>> getStrategies(Printable<AssemblyLine> activeAssemblyLine) {
		return assemblyLineScheduler.getStrategies(activeAssemblyLine);
	}
	
	/**
	 * Returns a map mapping each assembly line to it's strategies list.
	 * @return a map mapping each assembly line to it's strategies list.
	 */
	public Map<Printable<AssemblyLine>, List<Printable<SchedulingStrategy>>> getAssemblyLinesStrategies() {
		return assemblyLineScheduler.getAssemblyLinesStrategies();
	}
	
	/**
	 * Returns a list of orders that are viable to be used by the batch specification scheduling strategy for the given assembly line.
	 * @param assemblyLine	The assembly line for which the batch list needs to be returned.
	 * @return a list of orders that are viable to be used by the batch specification scheduling strategy for the given assembly line.
	 */
	public List<Order> getBatchList(Printable<AssemblyLine> assemblyline) {
		return assemblyLineScheduler.getBatchList(assemblyline);
	}
	
	/**
	 * Returns a list of orders that are viable to be used by the batch specification scheduling strategy of all assembly lines combined.
	 * @return a list of orders that are viable to be used by the batch specification scheduling strategy of all assembly lines combined.
	 */
	public List<Order> getBatchList() {
		return assemblyLineScheduler.getBatchList();
	}
	
	/**
	 * Changes the strategy of the given assembly line according to the given order.
	 * @param order	The order that has to be used as a template for the strategy.
	 * @param assemblyLine	The assembly line of which the strategy needs to be changed.
	 */
	public void changeStrategy(Order order, Printable<AssemblyLine> assemblyLine) {
		assemblyLineScheduler.changeStrategy(order, assemblyLine);
	}
	
	/**
	 * Changes the strategy of the given assembly line according to the given order.
	 * @param order	The order that has to be used as a template for the strategy.
	 * @param assemblyLine	The assembly line of which the strategy needs to be changed.
	 */
	public void changeStrategyAllLines(Order order) {
		assemblyLineScheduler.changeStrategyAllLines(order);
	}
	
	/**
	 * Returns a list of pending tasks at a given workstation in a given assembly line.
	 * @param station	The workstation for which the pending tasks are needed.
	 * @param assemblyLine 		The assembly line of the workstation for which the pending tasks are needed.
	 * @return	A list of representations of tasks that are pending at the given workstation.
	 */
	public List<Printable<Task>> getRequiredTasks(Printable<Workstation> station, Printable<AssemblyLine> assemblyLine){
		return this.assemblyLineScheduler.getRequiredTasks(station, assemblyLine);
	}
	
	/**
	 * Returns a list of all tasks at a given workstation on a given assembly line.
	 * @param station	The a copy of the workstation for which the tasks are needed.
	 * @param assemblyLine The assembly line on which we want the tasks.
	 * @return	A list of tasks at the given workstation and given assembly line.
	 */
	public List<Printable<Task>> getAllTasksAt(Printable<Workstation> station, Printable<AssemblyLine> assemblyLine){
		return this.assemblyLineScheduler.getAllTasksAt(station, assemblyLine);
	}
	
	/**
	 * Asks the assembly line to check the given duration, if the duration is allowed it returns true, otherwise false.
	 * @param duration	The duration that needs to be checked.
	 * @param assemblyLine The assembly line on which we need to check the phase duration.
	 * @return 	True if the duration is allowed.
	 * 			False otherwise
	 */
	private boolean checkPhaseDuration(int duration, Printable<AssemblyLine> assemblyLine){
		return this.assemblyLineScheduler.checkPhaseDuration(duration, assemblyLine);
	}

	/**
	 * Returns the assembly lines of the system in order.
	 * @return the assembly lines of the system in order.
	 */
	public List<Printable<AssemblyLine>> getAssemblyLines() {
		return assemblyLineScheduler.getAssemblyLines();
	}

	/**
	 * Returns a map mapping each assembly line to it's operational status.
	 * @return a map mapping each assembly line to it's operational status.
	 */
	public Map<Printable<AssemblyLine>,Printable<OperationalStatus>> getAssemblyLinesStatuses() {
		return assemblyLineScheduler.getAssemblyLinesStatuses();
	}

	/**
	 * Changes the operational status of the given assembly line to the given 
	 * operational status.
	 * @param activeAssemblyLine	The assembly line which status has to be changed.
	 * @param newStatus	The new status for the given assembly line.
	 */
	public void changeAssemblyLineStatus(Printable<AssemblyLine> activeAssemblyLine, OperationalStatus newStatus) {
		assemblyLineScheduler.changeAssemblyLineStatus(activeAssemblyLine, newStatus);
	}
	
}
