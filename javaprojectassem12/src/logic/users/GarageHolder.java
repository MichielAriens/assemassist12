package logic.users;

import java.util.ArrayList;
import java.util.Collections;

import logic.car.CarOrder;
import logic.car.CarOrderDetails;

/**
 * Class used to describe a garage holder who can order cars from a car manufacturing company.
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
	 * Constructs a new garage holder initializing its company and user name with the given 
	 * parameters and creating an empty list of committed orders.
	 * @param company	The car manufacturing company this new garage holder works for.
	 * @param userName	The user name for this new garage holder.
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
	 * Returns a list of all committed orders that are completed sorted on end time.
	 * @return a list of all committed orders that are completed sorted on end time.
	 */
	public ArrayList<CarOrder> getCompletedOrders(){
		ArrayList<CarOrder> completedOrders = new ArrayList<CarOrder>();
		for(CarOrder o : this.committedOrders){
			if(o.done())
				completedOrders.add(o);
		}
		Collections.sort(completedOrders);
		return completedOrders;
	}
	
	/**
	 * Commits a new order with the given car order details if the given car order details are not null.
	 * @param details The car order details for the new order.
	 */
	public void placeOrder(CarOrderDetails details){
		if(details == null)
			return;
		CarOrder order = new CarOrder(details);
		company.addOrder(order);
		this.committedOrders.add(order);
	}
}
