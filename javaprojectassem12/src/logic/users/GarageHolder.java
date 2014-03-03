package logic.users;

import java.util.ArrayList;

import logic.car.CarOrder;
import logic.car.CarSpecification;

public class GarageHolder {
	
	private CarManufacturingCompany company;
	private ArrayList<CarOrder> pendingOrders;
	private ArrayList<CarOrder> completedOrders;
	
	public GarageHolder(CarManufacturingCompany comp){
		this.company = comp;
		this.pendingOrders = new ArrayList<CarOrder>();
		this.completedOrders = new ArrayList<CarOrder>();
	}
	
	public void alertCompleted(CarOrder order){
		if(order == null || !order.isDone())
			return;
		if(pendingOrders.contains(order))
			pendingOrders.remove(order);
		completedOrders.add(order);
	}
	
	public ArrayList<CarOrder> getPendingOrders(){
		return this.pendingOrders;
	}
	
	public ArrayList<CarOrder> getCompletedOrders(){
		return this.completedOrders;
	}
	
	public void placeOrder(CarSpecification specification){
		if(specification == null)
			return;
		CarOrder order = new CarOrder(this,specification);
		company.addOrder(order);
		this.pendingOrders.add(order);
	}

}
