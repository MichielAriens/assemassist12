package controllers;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import logic.car.CarPart;
import logic.car.CarPartType;
import logic.car.TaskOrderDetailsMaker;
import logic.users.CustomsManager;

public class CustomsManagerController extends UserController{
	
	/**
	 * The current customs shop manager.
	 */
	private CustomsManager currentCustomsManager;
	
	/**
	 * A maker for task order details.
	 */
	private TaskOrderDetailsMaker maker = new TaskOrderDetailsMaker();
	
	/**
	 * Sets the current customs shop manager to the given customs shop manager.
	 * @param customsMan	The new mechanic.
	 */
	public void setCustomsManager(CustomsManager customsMan) {
		this.currentCustomsManager = customsMan;
	}
	
	/**
	 * Returns the user name of the current customs shop manager.
	 * @return 	Null if the current customs shop manager is null.
	 * 			The user name of the current customs shop manager otherwise.
	 */
	@Override
	public String getUserName() {
		return this.currentCustomsManager.getUserName();
	}
	
	/**
	 * Sets the part of the task order details maker if the given part is not null.
	 * @param part The part which needs to be set.
	 */
	public void choosePart(CarPart part){
		if(part == null)
			return;
		maker.choosePart(part);
	}
	
	/**
	 * Returns a numbered list of available types of tasks. 
	 * @return a numbered list of available types of tasks.
	 */
	public ArrayList<String> getAvailableTypes(){
		ArrayList<String> types = new ArrayList<String>();
		int count = 1;
		for(CarPartType type: maker.getAvailableTypesOfTasks()){
			types.add(type.toString() + ": " +  count);
			count++;
		}
		return types;
	}
	
	/**
	 * Returns a numbered list of available options for the given type of the chosen part.
	 * @param TaskTypeString	The car part type for which the available options need to be returned.
	 * @return a numbered list of available options for the given type of the chosen part.
	 */
	public ArrayList<String> getAvailableOptions(String taskTypeString){
		CarPartType taskType = CarPartType.getTypefromString(taskTypeString);
		ArrayList<String> options = new ArrayList<String>();
		int count = 1;
		for(CarPart part: maker.getAvailableParts(taskType)){
			options.add(part.toString() + ": " + count);
			count++;
		}
		return options;
	}
	
	/**
	 * Sets the deadline for the task order details maker if the given deadline is valid.
	 * @param deadlineStr	The deadline as a string.
	 * @return 	True if the given deadline string is valid and the deadline is set.
	 * 			False if the given deadline string is not valid.
	 */
	public boolean chooseDeadLine(String deadlineStr){
		try{
			DateTimeFormatter formatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm");
			DateTime dt = formatter.parseDateTime(deadlineStr);
			maker.chooseDeadline(dt);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	/**
	 * Places the order if the current customs shop manger is not null.
	 * @return 	The placed order as a string if the current customs manger is not null.
	 * 			Null otherwise.
	 */
	public String placeOrder(){
		if(currentCustomsManager == null)
			return null;
		return currentCustomsManager.placeOrder(maker.getDetails());
	}
	
}
