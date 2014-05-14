package logic.users;

import interfaces.Printable;

import java.util.List;

import logic.assemblyline.SchedulingStrategy;
import logic.car.Order;

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
	 * Returns a string representation of the current statistics.
	 * @return	a string representation of the current statistics.
	 */
	public String getStatistics() {
		return company.getStatistics();
	}
	
	/**
	 * Returns a list of the current strategies followed by the available scheduling strategies.
	 * @return a list of the current strategies followed by the available scheduling strategies.
	 */
	public List<Printable> getStrategies() {
		return company.getStrategies();
	}
	
	/**
	 * Returns a list of orders that are viable to be used by the batch specification scheduling strategy.
	 * @return	a list of orders that are viable to be used by the batch specification scheduling strategy.
	 */
	public List<Order> getBatchList() {
		return company.getBatchList();
	}
	
	/**
	 * Changes the strategy according to the given order.
	 * @param order	The order that has to be used as a template for the strategy.
	 */
	public void changeStrategy(Order order) {
		company.changeStrategy(order);
	}

	public List<String> getAssemblyLines() {
		return company.getAssemblyLines();
	}
	
	public List<String> getAssemblyLinesStatuses() {
		return company.getAssemblyLinesStatuses();
	}
}
