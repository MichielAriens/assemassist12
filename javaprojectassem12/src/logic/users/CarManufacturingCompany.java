package logic.users;

import interfaces.Printable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import logic.assemblyline.AssemblyLineScheduler;
import logic.car.Order;

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
	 * Method to return the list of workstations of the assembly line 
	 * of this car manufacturing company.
	 * @param assemblyLine
	 * @return	The workstations of the assembly line.
	 */
	public List<Printable> getWorkStationsFromAssemblyLine(Printable assemblyLine){
		
		return assemblyLineScheduler.getWorkStationsFromAssemblyLine(assemblyLine);
	}
	
	/**
	 * Returns the list of all workstations from all assembly lines of the car manufacturing company.
	 * @return the list of all workstations from all assembly lines of the car manufacturing company.
	 */
	public List<Printable> getWorkStations() {
		return assemblyLineScheduler.getAllWorkstations();
	}
	
	/**
	 * Moves the assembly line forward if every work station is ready and 
	 * sets the end time of the first order to the given end time.
	 * @return 	True if the assembly line has been moved.
	 * 			False if the assembly line can not be moved.
	 */
	public boolean moveAssemblyLine(int shiftDuration){
		return this.assemblyLineScheduler.tryMoveAssemblyLine(shiftDuration);
	}
	
	/**
	 * Returns the current time.
	 * @return	The current time.
	 */
	public DateTime getCurrentTime(){
		return this.assemblyLineScheduler.getCurrentTime();
	}
	
	
	/**
	 * Completes the task corresponding to the given task.
	 * @param task	A copy of the task that needs to be completed.
	 * @return	True if the task is completed successfully
	 * 			False the task could not be completed.
	 */
	public boolean doTask(Printable task, Printable assemblyLine, int duration){
		return assemblyLineScheduler.doTask(task, assemblyLine, duration);
	}
	
	/**
	 * Returns a string representation of the current statistics.
	 * @return	A string representation of the current statistics.
	 */
	public String getStatistics() {
		return assemblyLineScheduler.getStatistics();
	}
	
	/**
	 * Returns a list of the current strategies followed by the available scheduling strategies.
	 * @return a list of the current strategies followed by the available scheduling strategies.
	 */
	public List<Printable> getStrategies() {
		return assemblyLineScheduler.getStrategies();
	}
	
	/**
	 * Returns a list of orders that are viable to be used by the batch specification scheduling strategy.
	 * @return	a list of orders that are viable to be used by the batch specification scheduling strategy.
	 */
	public List<Order> getBatchList() {
		return assemblyLineScheduler.getBachList();
	}
	
	/**
	 * Changes the strategy according to the given order.
	 * @param order	The order that has to be used as a template for the strategy.
	 */
	public void changeStrategy(Order order) {
		assemblyLineScheduler.changeStrategy(order);
	}
	
	/**
	 * Changes the strategy according to the given order.
	 * @param order	The order that has to be used as a template for the strategy.
	 */
	public void changeStrategy(Order order, Printable assemblyLine) {
		assemblyLineScheduler.changeStrategy(order, assemblyLine);
	}

	
	/**
	 * Returns a list of pending tasks at a given workstation.
	 * @param station	The workstation for which the pending tasks are needed.
	 * @param line 		The assembly line of the workstation for which the pending tasks are needed.
	 * @return	A list of tasks that are pending at the given workstation.
	 */
	public List<Printable> getRequiredTasks(Printable station, Printable assemblyLine){
		return this.assemblyLineScheduler.getRequiredTasks(station, assemblyLine);
	}
	
	/**
	 * Returns a list of all tasks at a given workstation.
	 * @param station	The a copy of the workstation for which the tasks are needed.
	 * @return	A list of tasks at the given workstation.
	 */
	public List<Printable> getAllTasksAt(Printable station, Printable assemblyLine){
		return this.assemblyLineScheduler.getAllTasksAt(station, assemblyLine);
	}
	
	/**
	 * Asks the assembly line to check the given duration, if the duration is allowed it returns true, otherwise false.
	 * @param duration	The duration that needs to be checked.
	 * @return 	True if the duration is allowed.
	 * 			False otherwise
	 */
	public boolean checkPhaseDuration(int duration, Printable assemblyLine){
		return this.assemblyLineScheduler.checkPhaseDuration(duration, assemblyLine);
	}

	/**
	 * Returns the assemblylines of the system in order.
	 * @return
	 */
	public List<Printable> getAssemblyLines() {
		return assemblyLineScheduler.getAssemblyLines();
	}

	/**
	 * returns a map mapping each assembly line to it's status.
	 * @return
	 */
	public Map<Printable,Printable> getAssemblyLinesStatuses() {
		return assemblyLineScheduler.getAssemblyLinesStatuses();
	}
}
