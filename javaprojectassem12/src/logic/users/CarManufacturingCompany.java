package logic.users;

import java.util.ArrayList;
import java.util.HashMap;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import logic.assemblyline.AssemblyLine;
import logic.car.CarOrder;

public class CarManufacturingCompany {
	private HashMap<String,User> users;
	ArrayList<String> validNamesGarage = new ArrayList<String>();
	ArrayList<String> validNamesManager = new ArrayList<String>();
	ArrayList<String> validNamesMechanic = new ArrayList<String>();
	
	private AssemblyLine assemblyLine;
	
	public CarManufacturingCompany(){
		this.assemblyLine = new AssemblyLine();
		this.users = new HashMap<String,User>();
		this.initialise();
		
	}

	private void initialise() {
		validNamesGarage.add("Jex");
		validNamesManager.add("Wander");
		validNamesMechanic.add("Joren");
		validNamesGarage.add("Michiel");
	}

	public User logIn(String userName){
		if(users.containsKey(userName)){
			return users.get(userName);
		}
		if(validNamesManager.contains(userName)){
			Manager user = new Manager(this, userName);
			users.put(userName, user);
			return user;
		}
		if(validNamesGarage.contains(userName)){
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
	public void addOrder(CarOrder order) {
		this.assemblyLine.addCarOrder(order);
	}
	
	
}
