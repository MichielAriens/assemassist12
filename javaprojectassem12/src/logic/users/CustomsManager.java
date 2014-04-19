package logic.users;

import logic.car.TaskOrder;
import logic.car.TaskOrderDetails;

/**
 * Class used to describe a customs shop manager who can place orders for single tasks.
 */
public class CustomsManager extends User{
	
	/**
	 * The manufacturing company for which the customs shop manager works.
	 */
	private CarManufacturingCompany company;
	
	/**
	 * Constructs a new customs shop manager initializing its company and user name with the given 
	 * parameters and creating an empty list of committed orders.
	 * @param company	The car manufacturing company this new customs shop manager works for.
	 * @param userName	The user name for this new customs shop manager.
	 */
	public CustomsManager(CarManufacturingCompany company, String userName) {
		super(userName);
		this.company = company;
	}
	
	/**
	 * Commits a new order with the given task order details if the given task order details are not null.
	 * @param details The task order details for the new order.
	 */
	public String placeOrder(TaskOrderDetails details){
		if(details == null)
			return null;
		TaskOrder order = new TaskOrder(details);
		company.addOrder(order);
		return order.toString();
	}

}
