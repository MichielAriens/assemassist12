package logic.users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;

import logic.assemblyline.AssemblyLine;
import logic.assemblyline.SchedulingStrategy;
import logic.car.Order;
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
	private AssemblyLine assemblyLine;
	
	/**
	 * Makes a new car manufacturing company.
	 */
	public CarManufacturingCompany(){
		//TODO assembly lines maken hé mannen
		//this.assemblyLine = new AssemblyLine();
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
			this.assemblyLine.addOrder(order);
	}
	
	/**
	 * Method to return the list of workstations of the assembly line 
	 * of this car manufacturing company.
	 * @return	The workstations of the assembly line.
	 */
	public List<Workstation> getWorkStations(){
		return this.assemblyLine.getWorkStations();
	}
	
	/**
	 * Moves the assembly line forward if every work station is ready and 
	 * sets the end time of the first order to the given end time.
	 * @return 	True if the assembly line has been moved.
	 * 			False if the assembly line can not be moved.
	 */
	public boolean moveAssemblyLine(int shiftDuration){
		return this.assemblyLine.tryMoveAssemblyLine(shiftDuration);
	}
	
	/**
	 * Returns the current time.
	 * @return	The current time.
	 */
	public DateTime getCurrentTime(){
		return this.assemblyLine.getCurrentTime();
	}
	
	/**
	 * Returns a string representation of the current statistics.
	 * @return	A string representation of the current statistics.
	 */
	public String getStatistics() {
		return assemblyLine.getStatistics();
	}
	
	/**
	 * Returns a list of the current strategies followed by the available scheduling strategies.
	 * @return a list of the current strategies followed by the available scheduling strategies.
	 */
	public List<SchedulingStrategy> getStrategies() {
		return assemblyLine.getStrategies();
	}
	
	/**
	 * Returns a list of orders that are viable to be used by the batch specification scheduling strategy.
	 * @return	a list of orders that are viable to be used by the batch specification scheduling strategy.
	 */
	public List<Order> getBatchList() {
		return assemblyLine.getBachList();
	}
	
	/**
	 * Changes the strategy according to the given order.
	 * @param order	The order that has to be used as a template for the strategy.
	 */
	public void changeStrategy(Order order) {
		assemblyLine.changeStrategy(order);
	}
	
	/**
	 * Completes the task corresponding to the given task.
	 * @param task	A copy of the task that needs to be completed.
	 * @return	True if the task is completed successfully
	 * 			False the task could not be completed.
	 */
	public boolean doTask(Task task){
		return assemblyLine.doTask(task);
	}
	
	/**
	 * Returns a list of pending tasks at a given workstation.
	 * @param station	The a copy of the workstation for which the pending tasks are needed.
	 * @return	A list of tasks that are pending at the given workstation.
	 */
	public List<Task> getRequiredTasks(Workstation station){
		return this.assemblyLine.getRequiredTasks(station);
	}
	
	/**
	 * Returns a list of all tasks at a given workstation.
	 * @param station	The a copy of the workstation for which the tasks are needed.
	 * @return	A list of tasks at the given workstation.
	 */
	public List<Task> getAllTasks(Workstation station){
		return this.assemblyLine.getAllTasks(station);
	}
	
	/**
	 * Asks the assembly line to check the given duration, if the duration is allowed it returns true, otherwise false.
	 * @param duration	The duration that needs to be checked.
	 * @return 	True if the duration is allowed.
	 * 			False otherwise
	 */
	public boolean checkPhaseDuration(int duration){
		return this.assemblyLine.checkPhaseDuration(duration);
	}

	public List<String> getAssemblyLines() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getAssemblyLinesStatuses() {
		// TODO Auto-generated method stub
		return null;
	}
}
