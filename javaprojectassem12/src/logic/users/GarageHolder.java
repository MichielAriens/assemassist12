package logic.users;

import java.util.ArrayList;
import java.util.Calendar;

import logic.car.CarOrder;
import logic.car.CarSpecification;

public class GarageHolder extends User{
	
	private CarManufacturingCompany company;
	private ArrayList<CarOrder> committedOrders;
	
	public GarageHolder(CarManufacturingCompany comp, String userName){
		super(userName);
		this.company = comp;
		this.committedOrders = new ArrayList<CarOrder>();
	}
	
	public ArrayList<CarOrder> getPendingOrders(){
		ArrayList<CarOrder> pendingOrders = new ArrayList<CarOrder>();
		for(CarOrder o : this.committedOrders){
			if(!o.done())
				pendingOrders.add(o);
		}
		return pendingOrders;
	}
	
	public ArrayList<CarOrder> getCompletedOrders(){
		ArrayList<CarOrder> completedOrders = new ArrayList<CarOrder>();
		for(CarOrder o : this.committedOrders){
			if(o.done())
				completedOrders.add(o);
		}
		return completedOrders;
	}
	
	public void placeOrder(CarSpecification specification){
		if(specification == null)
			return;
		CarOrder order = new CarOrder(specification) ;
		company.addOrder(order);
		this.committedOrders.add(order);
	}

}
