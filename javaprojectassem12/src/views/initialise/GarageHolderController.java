package views.initialise;

import java.util.ArrayList;

import logic.car.*;
import logic.users.GarageHolder;

public class GarageHolderController extends UserController{
	
	private GarageHolder garageHolder;
	
	public void setGarageHolder(GarageHolder gh){
		this.garageHolder = gh;
	}

	@Override
	public String getUserName() {
		if(this.garageHolder != null)
			return this.garageHolder.getUserName();
		return null;
	}
	
	public ArrayList<String> getPendingOrders(){
		ArrayList<String> pendingOrderStrings = new ArrayList<String>();
		for(CarOrder order : this.garageHolder.getPendingOrders()){
			pendingOrderStrings.add(order.toString());
		}
		return pendingOrderStrings;
	}

	public ArrayList<String> getCompletedOrders() {
		ArrayList<String> completedOrderStrings = new ArrayList<String>();
		for(CarOrder order : this.garageHolder.getPendingOrders()){
			completedOrderStrings.add(order.toString());
		}
		return completedOrderStrings;
	}
	
	public ArrayList<String> getModels(){
		ArrayList<String> models = new ArrayList<String>();
		int count = 1;
		for(CarModel mod : CarModel.values()){
			models.add(mod.toString() + ": " +  count);
			count++;
		}
		return models;
	}
	
	public ArrayList<String> getOptions(CarPartType type, CarModel model){
		ArrayList<String> options = new ArrayList<String>();
		int count = 1;
		for(CarPart opt : CarPart.values()){
			if(opt.type == type && model.validPart(opt)){
				options.add(opt.toString() + ": " + count);
				count++;
			}
		}
		return options;
	}
	
	public void placeOrder(ArrayList<String> spec){
		System.out.println("Order placed: ");
		for(String s : spec){
			System.out.println(s);
		}
	}
	
	
}
