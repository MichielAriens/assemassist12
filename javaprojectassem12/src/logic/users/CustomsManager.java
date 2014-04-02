package logic.users;

import logic.car.TaskOrder;
import logic.car.TaskOrderDetails;

public class CustomsManager extends User{

	private CarManufacturingCompany company;
	
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
