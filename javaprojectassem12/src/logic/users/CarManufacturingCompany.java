package logic.users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import logic.assemblyline.AssemblyLine;
import logic.car.Order;
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
		this.assemblyLine = new AssemblyLine();
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
	 * @return 	True if the assembly line can be moved.
	 * 			False if the assembly line can not be moved.
	 */
	public boolean moveAssemblyLine(int shiftDuration){
		return this.assemblyLine.tryMoveAssemblyLine(shiftDuration);
	}
	
	/**
	 * Returns the car orders that would be on the assembly line if the assembly line was progressed.
	 * @return The list of car orders.
	 */
	public List<Order> askFutureSchedule(){
		return this.assemblyLine.askFutureSchedule();
	}

	public String getStatistics() {
		return assemblyLine.getStatistics();
	}

	public String getCurrentStrategy() {
		return assemblyLine.getCurrentStrategy();
	}

	public ArrayList<String> getAvailableStrategies() {
		return assemblyLine.getAvailableStrategies();
	}
}
