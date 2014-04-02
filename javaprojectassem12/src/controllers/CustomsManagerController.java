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
	
	private TaskOrderDetailsMaker maker = new TaskOrderDetailsMaker();
	
	/**
	 * Sets the current customs shop manager to the given customs shop manager.
	 * @param mechan	The new mechanic.
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
	
	public void choosePart(CarPart part){
		if(part == null)
			return;
		maker.choosePart(part);
	}
	
	public ArrayList<String> getAvailableTypes(){
		ArrayList<String> types = new ArrayList<String>();
		int count = 1;
		for(CarPartType type: maker.getAvailableTypes()){
			types.add(type.toString() + ": " +  count);
			count++;
		}
		return types;
	}
	
	public ArrayList<String> getAvailableOptions(CarPartType type){
		ArrayList<String> options = new ArrayList<String>();
		int count = 1;
		for(CarPart part: maker.getAvailableParts(type)){
			options.add(part.toString() + ": " + count);
			cou
		}
	}
	
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
	
}
