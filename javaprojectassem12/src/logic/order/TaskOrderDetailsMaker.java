package logic.order;

import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

/**
 * A class for building the task order's details.
 */
public class TaskOrderDetailsMaker {
	
	/**
	 * The chosen part for the task order's details.
	 */
	private VehiclePart chosenPart;
	
	/**
	 * The chosen dead line for the task order's details.
	 */
	private DateTime chosenDeadline = null;
	
	/**
	 * List of available types of tasks.
	 */
	private ArrayList<VehiclePartType> availableTypesOfTasks;
	
	/**
	 * Creates a new task order detail by initializing the available types of tasks.
	 */
	public TaskOrderDetailsMaker(){
		this.availableTypesOfTasks = new ArrayList<VehiclePartType>();
		this.availableTypesOfTasks.add(VehiclePartType.Colour);
		this.availableTypesOfTasks.add(VehiclePartType.Seats);
	}
	
	/**
	 * Returns the list of available types of tasks.
	 * @return the list of available types of tasks.
	 */
	public List<VehiclePartType> getAvailableTypesOfTasks(){
		return availableTypesOfTasks;
	}
	
	/**
	 * Returns the available parts for the given car part type.
	 * @param type The car part type for which the available parts need to be returned.
	 * @return the available parts for the given car part type.
	 */
	public List<VehiclePart> getAvailableParts(VehiclePartType type){
		ArrayList<VehiclePart> parts = new ArrayList<VehiclePart>();
		for(VehiclePart part : VehiclePart.values()){
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
	private boolean isValidPart(VehiclePart part){
		for(VehiclePartType type: availableTypesOfTasks){
			if(type == part.type)
				return true;
		}
		return false;
	}
	
	/**
	 * Checks if the given part is valid and if so sets the chosen part to the given part.
	 * @param part	The part that the chosenPart needs to be set to.
	 */
	public void choosePart(VehiclePart part){
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
