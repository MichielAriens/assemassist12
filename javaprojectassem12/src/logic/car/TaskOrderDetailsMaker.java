package logic.car;

import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

public class TaskOrderDetailsMaker {
	
	/**
	 * The chosen part for the task order's details.
	 */
	private CarPart chosenPart;
	
	/**
	 * The chosen dead line for the task order's details.
	 */
	private DateTime chosenDeadline = null;
	
	/**
	 * List of available types of tasks.
	 */
	private ArrayList<CarPartType> availableTypesOfTasks;
	
	/**
	 * Creates a new task order detail by initializing the available types of tasks.
	 */
	public TaskOrderDetailsMaker(){
		this.availableTypesOfTasks = new ArrayList<CarPartType>();
		this.availableTypesOfTasks.add(CarPartType.Colour);
		this.availableTypesOfTasks.add(CarPartType.Seats);
	}
	
	/**
	 * Returns the list of available types of tasks.
	 * @return the list of available types of tasks.
	 */
	public List<CarPartType> getAvailableTypesOfTasks(){
		return availableTypesOfTasks;
	}
	
	/**
	 * Returns the available parts for the given car part type.
	 * @param type The car part type for which the available parts need to be returned.
	 * @return the available parts for the given car part type.
	 */
	public List<CarPart> getAvailableParts(CarPartType type){
		ArrayList<CarPart> parts = new ArrayList<CarPart>();
		for(CarPart part : CarPart.values()){
			if(part.type == type)
				parts.add(part);
		}
		return parts;
	}
	
	/**
	 * Checks if the given part is a valid part for which a task order can be made.
	 * @param part	The part for which needs to be checked if a task order can be made.
	 * @return 	True if the given part is a valid part.
	 * 			False if the given part is not a valid part.
	 */
	private boolean isValidPart(CarPart part){
		for(CarPartType type: availableTypesOfTasks){
			if(type == part.type)
				return true;
		}
		return false;
	}
	
	/**
	 * Checks if the given part is valid and if so sets the chosen part to the given part.
	 * @param part	The part that the chosenPart needs to be set to.
	 */
	public void choosePart(CarPart part){
		if(isValidPart(part))
			this.chosenPart = part;
	}
	
	/**
	 * Sets the chosen deadline to the given date.
	 * @param deadline	The date to which the chosen deadline needs to be set to.
	 */
	public void chooseDeadline(DateTime deadline){
		this.chosenDeadline = deadline;
	}
	
	/**
	 * Returns a new task order details if the chosen part and chosen dead line are both not null.
	 * @return a new task order details if the chosen part and chosen dead line are both not null.
	 */
	public TaskOrderDetails getDetails(){
		if(chosenPart != null && chosenDeadline != null)
			return new TaskOrderDetails(chosenPart, chosenDeadline);
		return null;
	}
}
