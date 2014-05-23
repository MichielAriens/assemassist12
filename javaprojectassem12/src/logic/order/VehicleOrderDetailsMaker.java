package logic.order;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for building the vehicle order's details.
 */
public class VehicleOrderDetailsMaker {
	
	/**
	 * A variable holding the model of the vehicle order.
	 */
	private VehicleModel model;
	
	/**
	 * A list of vehicle parts which the user wants on his vehicle.
	 */
	private ArrayList<VehiclePart> chosenParts;
	
	/**
	 * A list of different type of parts the user wants on his vehicle.
	 */
	private ArrayList<VehiclePartType> chosenTypes;
	
	/**
	 * Initializes this vehicle order details maker with a given model.
	 * @param model	The vehicle model of the VehicleOrderDetails that has to be made by this maker.
	 */
	public VehicleOrderDetailsMaker(VehicleModel model){
		this.model = model;
		this.chosenParts = new ArrayList<VehiclePart>();
		this.chosenTypes = new ArrayList<VehiclePartType>();
	}
	
	/**
	 * Adds a given part to the list of vehicle parts and adds its type to the list of vehicle part types if possible.
	 * @param part The part that needs to be added.
	 */
	public void addPart(VehiclePart part){
		if(isCompatiblePart(part)){
			chosenParts.add(part);
			chosenTypes.add(part.type);
		}
	}
	
	/**
	 * Checks if the given part is compatible with the parts in this vehicle order detail.
	 * @param part	The part that needs to be checked.
	 * @return	True if the part is allowed in the model and is present in the list of chosen types and in the list of present vehicle parts.
	 * 			False if it's not present and some requirements are not met.
	 */
	private boolean isCompatiblePart(VehiclePart part){
		if(!model.validPart(part))
			return false;
		if(chosenTypes.contains(part.type))
			return false;
		if(part.type != VehiclePartType.Airco && part.type != VehiclePartType.Engine && part.type != VehiclePartType.Spoiler && part.type != VehiclePartType.Wheels)
			return true;
		if(part.type == VehiclePartType.Airco){
			if(chosenParts.contains(VehiclePart.ENGINE_8) && part == VehiclePart.AIRCO_AUTO)
				return false;
		}
		if(part.type == VehiclePartType.Engine){
			if(chosenParts.contains(VehiclePart.BODY_SPORT) && part == VehiclePart.ENGINE_4)
				return false;
		}
		if(part.type == VehiclePartType.Spoiler){
			if(chosenParts.contains(VehiclePart.BODY_SPORT) && part == VehiclePart.SPOILER_NONE)
				return false;
		}
		if(part.type == VehiclePartType.Wheels){
			if(chosenParts.contains(VehiclePart.BODY_PLATFORM) && part != VehiclePart.WHEELS_HEAVY_DUTY)
				return false;
		}
		return true;
	}
	
	/**
	 * Given a certain vehicle part type, returns a list of all possible parts which are of the given type and are based on the model of this object. 
	 * @param type	The type for which we want the parts.
	 * @return	A list containing all the parts who have the given type and which are in the possible parts list of this object's model.
	 */
	public List<VehiclePart> getAvailableParts(VehiclePartType type){
		ArrayList<VehiclePart> result = new ArrayList<VehiclePart>();
		for(VehiclePart part: model.possibleParts){
			if(part.type == type && isCompatiblePart(part))
				result.add(part);
		}
		return result;
	}
	
	/**
	 * Makes a vehicle order detail with this objects model and parts and returns it if possible
	 * @return	A new vehicle order detail with this object's model and parts, null if there are too many or too less parts in this object's parts list.
	 */
	public VehicleOrderDetails getDetails(){
		if(chosenParts.size() == 11){
			return new VehicleOrderDetails(model, chosenParts);
		}
		return null;
	}
}
