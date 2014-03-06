package logic.users;

import java.util.ArrayList;
import java.util.HashMap;

import logic.assemblyline.AssemblyLine;
import logic.car.CarOrder;

public class CarManufacturingCompany {
	
	private HashMap<String,GarageHolder> garageHolders;
	private HashMap<String,Mechanic> mechanics;
	private HashMap<String,Manager> managers;

	ArrayList<String> validNamesGarage = new ArrayList<String>();
	ArrayList<String> validNamesManager = new ArrayList<String>();
	ArrayList<String> validNamesMechanic = new ArrayList<String>();
	
	private AssemblyLine assemblyLine;
	
	public CarManufacturingCompany(){
		this.assemblyLine = new AssemblyLine();
		this.garageHolders = new HashMap<String,GarageHolder>();
		this.managers = new HashMap<String,Manager>();
		this.mechanics= new HashMap<String,Mechanic>();
		this.initialise();
	}

	private void initialise() {
		validNamesGarage.add("Jex");
		validNamesManager.add("Wander");
		validNamesMechanic.add("Joren");
		validNamesGarage.add("Michiel");
	}

	public boolean logIn(String userName){
		if(garageHolders.containsKey(userName)){
			return true;
		}
		return true;
	}
	public void addOrder(CarOrder order) {
		this.assemblyLine.addCarOrder(order);
	}
	
	
}
