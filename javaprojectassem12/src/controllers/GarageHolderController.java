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
	
	private CarOrderDetailsMaker maker;
	
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
			pendingOrderStrings.add("Pending, est. completion at: " + order.toString());
		}
		return pendingOrderStrings;
	}
	
	public String getPendingInfo(int index){
		if(this.currentGarageHolder == null)
			return null;
		return this.currentGarageHolder.getPendingOrders().get(index).getInformation();
	}
	
	public String getCompletedInfo(int index){
		if(this.currentGarageHolder == null)
			return null;
		return this.currentGarageHolder.getCompletedOrders().get(index).getInformation();
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
			completedOrderStrings.add("Completed on: " + order.toString());
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
	public ArrayList<String> getOptions(CarPartType type){
		if(type == null)
			return null;
		ArrayList<String> result = new ArrayList<String>();
		int count = 1;
		for(CarPart opt : maker.getAvailableParts(type)){
			result.add(opt.toString() + ": " + count);
			count++;
		}
		return result;
	}
	
	public void chooseModel(CarModel model){
		if(model == null)
			return;
		this.maker = new CarOrderDetailsMaker(model);
	}
	
	public void addPart(String partString){
		if(partString == null)
			return;
		this.maker.addPart(CarPart.getPartfromString(partString));
	}
	
	/**
	 * Places a car order with the given specifications for the current garage holder if the
	 * given specifications and the current garage holder are not null. 
	 * @param spec	The specifications for the car order to be placed.
	 */
	public void placeOrder(){
		if(currentGarageHolder == null)
			return;
		this.currentGarageHolder.placeOrder(maker.getDetails());
	}
}
