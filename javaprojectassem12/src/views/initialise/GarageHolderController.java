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
		for(CarModel mod : CarModel.values()){
			models.add(mod.toString());
		}
		return models;
	}
	
	public ArrayList<String> getOptions(CarPartType type, CarModel model){
		ArrayList<String> options = new ArrayList<String>();
		for(CarPart opt : CarPart.values()){
			if(opt.type == type && model.validPart(opt))
				options.add(opt.toString());
		}
		return options;
	}
	
	public void placeOrder(CarModel model, ArrayList<String> parts){
		System.out.println("Order placed: ");
		System.out.println(model);
		for(String s : parts){
			System.out.println(s);
		}
	}
	
	
}
