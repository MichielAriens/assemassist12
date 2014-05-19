package controllers;

import interfaces.Printable;

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
	 * A maker for car order details.
	 */
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
		for(Printable<Order> order : this.currentGarageHolder.getPendingOrders()){
			pendingOrderStrings.add("Pending, est. completion at: " + order.getStringRepresentation());
		}
		return pendingOrderStrings;
	}
	
	//TODO: make better use of the interface here
	
	/**
	 * Returns the information for the pending order with the given index.
	 * @param index	The index for the order in the list of pending orders.
	 * @return	Null if the current garage holder is null or the given index is not valid. 
	 * 			The information for the pending order with the given index otherwise.
	 */
	public String getPendingInfo(int index){
		if(this.currentGarageHolder == null)
			return null;
		ArrayList<Printable<Order>> pendingOrders = this.currentGarageHolder.getPendingOrders();
		if(index < pendingOrders.size() && index >= 0)
			return pendingOrders.get(index).getExtraInformation();
		else
			return null;
	}
	
	/**
	 * Returns the information for the completed order with the given index.
	 * @param index	The index for the order in the list of completed orders.
	 * @return 	Null if the current garage holder is null or the given index is not valid. 
	 * 			The information for the completed order with the given index otherwise.
	 */
	public String getCompletedInfo(int index){
		if(this.currentGarageHolder == null)
			return null;
		ArrayList<Printable<Order>> completedOrders = this.currentGarageHolder.getCompletedOrders();
		if(index < completedOrders.size() && index >= 0)
			return completedOrders.get(index).getExtraInformation();
		else
			return null;
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
		for(Printable<Order> order : this.currentGarageHolder.getCompletedOrders()){
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
		for(VehicleModel mod : VehicleModel.values()){
			models.add(mod.toString() + ": " +  count);
			count++;
		}
		return models;
	}
	
	/**
	 * Returns the list of options for the given car part type and model defined in the maker with numbering.
	 * @param type	The car part type for which the options need to be returned.
	 * @return Null if the given type or model is null.
	 * @return The list of options for the given car part type and model with numbering.
	 */
	public ArrayList<String> getOptions(VehiclePartType type){
		if(type == null)
			return null;
		ArrayList<String> result = new ArrayList<String>();
		int count = 1;
		for(VehiclePart opt : maker.getAvailableParts(type)){
			result.add(opt.toString() + ": " + count);
			count++;
		}
		return result;
	}
	
	/**
	 * Creates a new car order's details maker with the given model if the given model is not
	 * null.
	 * @param model	The model for the new car order's details maker.
	 */
	public void chooseModel(VehicleModel model){
		if(model == null)
			return;
		this.maker = new CarOrderDetailsMaker(model);
	}
	
	/**
	 * Adds the car part from the given string to the car order's details maker if the given
	 * string is not null.
	 * @param partString	The string representation of the part to be added.
	 */
	public void addPart(String partString){
		if(partString == null)
			return;
		this.maker.addPart(VehiclePart.getPartfromString(partString));
	}
	
	/**
	 * Places a car order with the given specifications for the current garage holder if the
	 * given specifications and the current garage holder are not null. 
	 */
	public void placeOrder(){
		if(currentGarageHolder == null)
			return;
		this.currentGarageHolder.placeOrder(maker.getDetails());
	}
}
