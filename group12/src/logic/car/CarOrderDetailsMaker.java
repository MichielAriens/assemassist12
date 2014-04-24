package logic.car;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for building the car order's details.
 */
public class CarOrderDetailsMaker {
	
	/**
	 * A variable holding the model of the car order.
	 */
	private CarModel model;
	
	/**
	 * A list of car parts which the user wants on his car.
	 */
	private ArrayList<CarPart> chosenParts;
	
	/**
	 * A list of different type of parts the user wants on his car.
	 */
	private ArrayList<CarPartType> chosenTypes;
	
	/**
	 * Initializes this car order details maker with a given model.
	 * @param model	The car model of the CarOrderDetails that has to be made by this maker.
	 */
	public CarOrderDetailsMaker(CarModel model){
		this.model = model;
		this.chosenParts = new ArrayList<CarPart>();
		this.chosenTypes = new ArrayList<CarPartType>();
	}
	
	/**
	 * Adds a given part to the list of car parts and adds its type to the list of car part types if possible.
	 * @param part The part that needs to be added.
	 */
	public void addPart(CarPart part){
		if(isCompatiblePart(part)){
			chosenParts.add(part);
			chosenTypes.add(part.type);
		}
	}
	
	/**
	 * Checks if the given part is compatible with the parts in this car order detail.
	 * @param part	The part that needs to be checked.
	 * @return	True if the part is allowed in the model and is present in the list of chosen types and in the list of present car parts.
	 * 			False if it's not present and some requirements are not met.
	 */
	private boolean isCompatiblePart(CarPart part){
		if(!model.validPart(part))
			return false;
		if(chosenTypes.contains(part.type))
			return false;
		if(part.type != CarPartType.Airco && part.type != CarPartType.Engine && part.type != CarPartType.Spoiler)
			return true;
		if(part.type == CarPartType.Airco){
			if(chosenParts.contains(CarPart.ENGINE_8) && part == CarPart.AIRCO_AUTO)
				return false;
		}
		if(part.type == CarPartType.Engine){
			if(chosenParts.contains(CarPart.BODY_SPORT) && part == CarPart.ENGINE_4)
				return false;
		}
		if(part.type == CarPartType.Spoiler){
			if(chosenParts.contains(CarPart.BODY_SPORT) && part == CarPart.SPOILER_NONE)
				return false;
		}
		return true;
	}
	
	/**
	 * Given a certain car part type, returns a list of all possible parts which are of the given type and are based on the model of this object. 
	 * @param type	The type for which we want the parts.
	 * @return	A list containing all the parts who have the given type and which are in the possible parts list of this object's model.
	 */
	public List<CarPart> getAvailableParts(CarPartType type){
		ArrayList<CarPart> result = new ArrayList<CarPart>();
		for(CarPart part: model.possibleParts){
			if(part.type == type && isCompatiblePart(part))
				result.add(part);
		}
		return result;
	}
	
	/**
	 * Makes a car order detail with this objects model and parts and returns it if possible
	 * @return	A new car order detail with this object's model and parts, null if there are too many or too less parts in this object's parts list.
	 */
	public CarOrderDetails getDetails(){
		if(chosenParts.size() == 8){
			return new CarOrderDetails(model, chosenParts);
		}
		return null;
	}
}
