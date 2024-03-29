package logic.users;

import interfaces.Printable;

import java.util.List;
import java.util.Map;

import logic.assemblyline.AssemblyLine;
import logic.assemblyline.OperationalStatus;
import logic.assemblyline.SchedulingStrategy;
import logic.order.Order;

/**
 * Class used to describe a manager working for a car manufacturing company.
 */
public class Manager extends User{
	
	/**
	 * The manufacturing company for which the garage holder works.
	 */
	private CarManufacturingCompany company;
	
	/**
	 * The printable instance of the assembly line the manager wants to perform actions on.
	 */
	private Printable<AssemblyLine> activeAssemblyLine = null;
	
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
	 * Returns a list of the current strategies followed by the available scheduling strategies for the active assembly line.
	 * @return a list of the current strategies followed by the available scheduling strategies for the active assembly line.
	 */
	public List<Printable<SchedulingStrategy>> getStrategiesActiveLine() {
		return company.getStrategies(activeAssemblyLine);
	}
	
	/**
	 * Returns a map mapping each assembly line to it's strategies list.
	 * @return a map mapping each assembly line to it's strategies list.
	 */
	public Map<Printable<AssemblyLine>, List<Printable<SchedulingStrategy>>> getAssemblyLinesStrategies() {
		return company.getAssemblyLinesStrategies();
	}
	
	/**
	 * Returns a list of orders that are viable to be used by the batch specification scheduling strategy for the given assembly line.
	 * @return a list of orders that are viable to be used by the batch specification scheduling strategy for the given assembly line.
	 */
	public List<Order> getBatchListActiveLine() {
		return company.getBatchList(activeAssemblyLine);
	}
	
	/**
	 * Returns a list of orders that are viable to be used by the batch specification scheduling strategy of all assembly lines combined.
	 * @return a list of orders that are viable to be used by the batch specification scheduling strategy of all assembly lines combined.
	 */
	public List<Order> getBatchListAllLines() {
		return company.getBatchList();
	}
	
	/**
	 * Changes the strategy of the active assembly line according to the given order.
	 * @param order	The order that has to be used as a template for the strategy.
	 */
	public void changeStrategyActiveAssemblyLine(Order order) {
		company.changeStrategy(order, activeAssemblyLine);
	}
	
	/**
	 * Changes the strategy of the active assembly line according to the given order.
	 * @param order	The order that has to be used as a template for the strategy.
	 */
	public void changeStrategyAllLines(Order order) {
		company.changeStrategyAllLines(order);
	}

	/**
	 * Returns a list of all the assembly lines in the company.
	 * @return	A list of all the assembly lines in this company.
	 */
	public List<Printable<AssemblyLine>> getAssemblyLines() {
		return company.getAssemblyLines();
	}
	
	/**
	 * Returns a map of representations of assembly lines as keys and the representations of the statuses as values.
	 * @return A map of representations of assembly lines as keys and the representations of the statuses as values.
	 */
	public Map<Printable<AssemblyLine>, Printable<OperationalStatus>> getAssemblyLinesStatuses() {
		return company.getAssemblyLinesStatuses();
	}
	
	/**
	 * Set the active assembly line to the given assembly line.
	 * @param line	The new active assembly line.
	 */
	public void setActiveAssemblyLine(Printable<AssemblyLine> line){
		this.activeAssemblyLine = line;
	}

	/**
	 * Returns the assembly line representation on which this manager is currently operating.
	 * @return The assembly line representation on which this manager is currently operating.
	 */
	public Printable<AssemblyLine> getActiveAssemblyLine() {
		return activeAssemblyLine;
	}

	/**
	 * Changes the status of this manager's active assembly line to the given status.
	 * @param newStatus	The new status of this manager's active assembly line.
	 */
	public void changeAssemblyLineStatus(OperationalStatus newStatus) {
		company.changeAssemblyLineStatus(activeAssemblyLine, newStatus);
	}
}
