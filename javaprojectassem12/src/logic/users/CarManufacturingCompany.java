package logic.users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import logic.assemblyline.AssemblyLine;
import logic.car.CarOrder;
import logic.workstation.Workstation;

/**
 * Class used to describe car manufacturing companies.
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
		validNamesGarageHolders.add("Jex");
		validNamesManager.add("Wander");
		validNamesMechanic.add("Joren");
		validNamesGarageHolders.add("Michiel");
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
		return null;
	}
	
	/**
	 * Adds the given order to the assembly line if the given order is not null.
	 * @param order		the order which needs to be added to the assembly line
	 */
	public void addOrder(CarOrder order) {
		if(order != null)
			this.assemblyLine.addCarOrder(order);
	}
	
	/**
	 * Method to return the list of workstations of the assembly line 
	 * of this car manufacturing company.
	 * @return	the workstations of the assembly line of this car manufacturing company
	 */
	public Workstation[] getWorkStations(){
		return this.assemblyLine.getWorkStations();
	}
}
