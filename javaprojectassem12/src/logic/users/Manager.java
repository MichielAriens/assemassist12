package logic.users;

import java.util.ArrayList;
import java.util.List;

import logic.assemblyline.SchedulingStrategy;
import logic.car.Order;
import logic.workstation.Workstation;

/**
 * Class used to describe a manager working for a car manufacturing company.
 */
public class Manager extends User{
	
	/**
	 * The manufacturing company for which the garage holder works.
	 */
	private CarManufacturingCompany company;
	
	/**
	 * Constructs a new manager initializing its company and user name with the given 
	 * parameters.
	 * @param company	The car manufacturing company the new manager works for.
	 * @param userName	The user name for this new manager.
	 */
	public Manager(CarManufacturingCompany company, String userName){
		super(userName);
		this.company = company;
	}
	
	/**
	 * Returns the workstations of the car manufacturing company.
	 * @return The workstations of the car manufacturing company.
	 */
	public List<Workstation> getWorkstations(){
		return this.company.getWorkStations();
	}

	public String getStatistics() {
		return company.getStatistics();
	}
	
	public ArrayList<SchedulingStrategy> getStrategies() {
		return company.getStrategies();
	}

	public ArrayList<Order> getBatchList() {
		return company.getBatchList();
	}

	public void changeStrategy(Order order) {
		company.changeStrategy(order);
	}
}
