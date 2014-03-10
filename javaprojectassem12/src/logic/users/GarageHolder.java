package logic.users;

import java.util.ArrayList;
import logic.car.CarOrder;
import logic.car.CarSpecification;

/**
 * Class used to describe a garage holder working for a car manufacturing company.
 */
public class GarageHolder extends User{
	
	/**
	 * The manufacturing company for which the garage holder works.
	 */
	private CarManufacturingCompany company;
	
	/**
	 * List of all the orders committed by this garage holder.
	 */
	private ArrayList<CarOrder> committedOrders;
	
	/**
	 * Constructs a garage holder initializing the company and user name with the given 
	 * parameters and creating an empty list of committed orders.
	 * @param company	The car manufacturing company this garage holder works for.
	 * @param userName	The user name for this garage holder.
	 */
	public GarageHolder(CarManufacturingCompany company, String userName){
		super(userName);
		this.company = company;
		this.committedOrders = new ArrayList<CarOrder>();
	}
	
	/**
	 * Returns a list of all committed orders that are not yet completed.
	 * @return a list of all committed orders that are not yet completed.
	 */
	public ArrayList<CarOrder> getPendingOrders(){
		ArrayList<CarOrder> pendingOrders = new ArrayList<CarOrder>();
		for(CarOrder o : this.committedOrders){
			if(!o.done())
				pendingOrders.add(o);
		}
		return pendingOrders;
	}
	
	/**
	 * Returns a list of all committed orders that are completed.
	 * @return a list of all committed orders that are completed.
	 */
	public ArrayList<CarOrder> getCompletedOrders(){
		ArrayList<CarOrder> completedOrders = new ArrayList<CarOrder>();
		for(CarOrder o : this.committedOrders){
			if(o.done())
				completedOrders.add(o);
		}
		return completedOrders;
	}
	
	/**
	 * Commits a new order with the given car specification if the given car specifications
	 * are not null.
	 * @param specification	The car specification for the new order.
	 */
	public void placeOrder(CarSpecification specification){
		if(specification == null)
			return;
		CarOrder order = new CarOrder(specification);
		company.addOrder(order);
		this.committedOrders.add(order);
	}

}
