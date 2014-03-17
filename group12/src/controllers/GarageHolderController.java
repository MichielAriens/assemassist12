package controllers;

import java.util.ArrayList;

import logic.car.*;
import logic.users.GarageHolder;

/**
 * Class used to form a link between the user interface and the GarageHolder class.
 */
public class GarageHolderController extends UserController{
	
	/**
	 * The current garage holder.
	 */
	private GarageHolder currentGarageHolder;
	
	/**
	 * Sets the current garage holder to the given garage holder.
	 * @param garageHolder	The new garage holder.
	 */
	public void setGarageHolder(GarageHolder garageHolder){
		this.currentGarageHolder = garageHolder;
	}

	/**
	 * Returns the user name of the current garage holder.
	 * @return 	Null if the current garage holder is null.
	 * 			The user name of the current garage holder otherwise.
	 */
	@Override
	public String getUserName() {
		if(this.currentGarageHolder != null)
			return this.currentGarageHolder.getUserName();
		return null;
	}
	
	/**
	 * Returns the list of pending orders for the current garage holder.
	 * @return Null if the current garage holder is null.
	 * @return The list of pending orders for the current garage holder.
	 */
	public ArrayList<String> getPendingOrders(){
		if(this.currentGarageHolder == null)
			return null;
		ArrayList<String> pendingOrderStrings = new ArrayList<String>();
		for(CarOrder order : this.currentGarageHolder.getPendingOrders()){
			pendingOrderStrings.add(order.toString());
		}
		return pendingOrderStrings;
	}

	/**
	 * Returns the list of completed orders for the current garage holder.
	 * @return Null if the current garage holder is null.
	 * @return The list of completed orders for the current garage holder.
	 */
	public ArrayList<String> getCompletedOrders() {
		if(this.currentGarageHolder == null)
			return null;
		ArrayList<String> completedOrderStrings = new ArrayList<String>();
		for(CarOrder order : this.currentGarageHolder.getCompletedOrders()){
			completedOrderStrings.add(order.toString());
		}
		return completedOrderStrings;
	}
	
	/**
	 * Returns the list of available car models with numbering.
	 * @return The list of available car models with numbering.
	 */
	public ArrayList<String> getModels(){
		ArrayList<String> models = new ArrayList<String>();
		int count = 1;
		for(CarModel mod : CarModel.values()){
			models.add(mod.toString() + ": " +  count);
			count++;
		}
		return models;
	}
	
	/**
	 * Returns the list of options for the given car part type and model with numbering.
	 * @param type	The car part type for which the options need to be returned.
	 * @param model	The car model for which the options need to be returned.
	 * @return Null if the given type or model is null.
	 * @return The list of options for the given car part type and model with numbering.
	 */
	public ArrayList<String> getOptions(CarPartType type, CarModel model){
		if(type == null || model == null)
			return null;
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
	
	/**
	 * Places a car order with the given specifications for the current garage holder if the
	 * given specifications and the current garage holder are not null. 
	 * @param spec	The specifications for the car order to be placed.
	 */
	public void placeOrder(ArrayList<String> spec){
		if(spec == null || currentGarageHolder == null)
			return;
		CarModel model = CarModel.getModelFromString(spec.get(0));
		ArrayList<CarPart> parts = new ArrayList<CarPart>();
		for(int i = 1; i < spec.size(); i++){
			parts.add(CarPart.getPartfromString(spec.get(i)));
		}
		CarSpecification specification = new CarSpecification(model, parts);
		this.currentGarageHolder.placeOrder(specification);
	}
}
