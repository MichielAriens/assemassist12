package logic.users;

import java.util.ArrayList;
import java.util.Calendar;

import logic.car.CarOrder;
import logic.car.CarSpecification;

public class GarageHolder extends User{
	
	private CarManufacturingCompany company;
	private ArrayList<CarOrder> pendingOrders;
	private ArrayList<CarOrder> completedOrders;
	
	public GarageHolder(CarManufacturingCompany comp, String userName){
		super(userName);
		this.company = comp;
		this.pendingOrders = new ArrayList<CarOrder>();
		this.completedOrders = new ArrayList<CarOrder>();
	}
	
	public void alertCompleted(CarOrder order){
		if(order == null || !order.done())
			return;
		if(pendingOrders.contains(order))
			pendingOrders.remove(order);
		completedOrders.add(order);
	}
	
	public ArrayList<CarOrder> getPendingOrders(){
		return this.pendingOrders;
	}
	
	public ArrayList<String> getPendingOrderStrings(){
		ArrayList<String> pendingOrderStrings = new ArrayList<String>();
		for(CarOrder order : pendingOrders){
			pendingOrderStrings.add(order.toString());
		}
		return pendingOrderStrings;
	}
	
	public ArrayList<CarOrder> getCompletedOrders(){
		return this.completedOrders;
	}
	
	public void placeOrder(CarSpecification specification){
		if(specification == null)
			return;
		CarOrder order = new CarOrder(specification, Calendar.getInstance()) ;
		company.addOrder(order);
		this.pendingOrders.add(order);
	}

}
