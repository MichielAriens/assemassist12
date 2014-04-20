package logic.car;

import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

public class TaskOrderDetailsMaker {
	
	private CarPart chosenPart;
	private DateTime chosenDeadline = null;
	
	/**
	 * List of available types of tasks.
	 */
	private ArrayList<CarPartType> availableTypes;
	
	public TaskOrderDetailsMaker(){
		this.availableTypes = new ArrayList<CarPartType>();
		this.availableTypes.add(CarPartType.Colour);
		this.availableTypes.add(CarPartType.Seats);
	}
	
	public List<CarPartType> getAvailableTypes(){
		return availableTypes;
	}
	
	public List<CarPart> getAvailableParts(CarPartType type){
		ArrayList<CarPart> parts = new ArrayList<CarPart>();
		for(CarPart part : CarPart.values()){
			if(part.type == type)
				parts.add(part);
		}
		return parts;
	}
	
	private boolean isValidPart(CarPart part){
		for(CarPartType type: availableTypes){
			if(type == part.type)
				return true;
		}
		return false;
	}
	
	public void choosePart(CarPart part){
		if(isValidPart(part))
			this.chosenPart = part;
	}
	
	public void chooseDeadline(DateTime deadline){
		this.chosenDeadline = deadline;
	}
	
	public TaskOrderDetails getDetails(){
		if(chosenPart != null && chosenDeadline != null)
			return new TaskOrderDetails(chosenPart, chosenDeadline);
		return null;
	}
}
