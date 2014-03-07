package logic.car;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.EnumMap;

/**
 * A class used to describe the car specifications of a car order. 
 */
public class CarSpecification {

	/**
	 * The car model of this car specification.
	 */
	private CarModel model;
	
	/**
	 * The list of parts of this car specification.
	 */
	private ArrayList<CarPart> parts;

	/**
	 * Make a new car specification with a given car model and a list of car parts.
	 * @param	model 	The car model of this car specification.
	 * @param	parts 	The list of parts of this car specification.
	 * @throws 	InvalidParameterException	
	 * 			Throws an InvalidParameterException if: 
	 * 			 - the model is null, 
	 * 		   	 - the list of parts is null, 
	 * 			 - the list of parts is incomplete, 
	 * 			 - the list of parts contains more than one part of a certain type.
	 */
	public CarSpecification(CarModel model, ArrayList<CarPart> parts) throws InvalidParameterException{
		if(!isValidSpecification(model, parts))
			throw new InvalidParameterException();
		this.model = model;
		this.parts = parts;
	}
	
	/**
	 * Check if the given model and list of parts are valid.
	 * @param model		The car model that has to be checked.
	 * @param parts		The list of parts that has to be checked.
	 * @return	Returns false if:
	 * 			- the model is null, 
	 * 		   	- the list of parts is null, 
	 * 			- the list of parts is incomplete, 
	 * 			- the list of parts contains more than one part of a certain type. 
	 * 			Returns true otherwise.
	 */
	private boolean isValidSpecification(CarModel model, ArrayList<CarPart> parts){
		if(model == null)
			return false;
		if(!containsEveryPartTypeOnce(parts)){
			return false;
		}
		return true;
	}

	/**
	 * Check if the given list of parts is valid.
	 * @param parts		The list of parts that has to be checked.
	 * @return  Returns false if:
	 * 		   	- the list of parts is null, 
	 * 			- the list of parts is incomplete, 
	 * 			- the list of parts contains more than one part of a certain type. 
	 * 			Returns true otherwise.
	 */
	private boolean containsEveryPartTypeOnce(ArrayList<CarPart> parts){
		if(parts == null || parts.size() > 7)
			return false;
		EnumMap<CarPartType, Boolean> flags = new EnumMap<CarPartType, Boolean>(CarPartType.class);
		flags.put(CarPartType.Body, false);
		flags.put(CarPartType.Airco, false);
		flags.put(CarPartType.Colour, false);
		flags.put(CarPartType.Engine, false);
		flags.put(CarPartType.Gearbox, false);
		flags.put(CarPartType.Seats, false);
		flags.put(CarPartType.Wheels, false);

		for(int i = 0; i < parts.size(); i++){
			CarPartType type = parts.get(i).type;
			if(!flags.get(type)){
				flags.put(type, true);
			}
			else{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Method to return the list of parts of this car specification.
	 * @return	Returns the list of parts.
	 */
	public List<CarPart> getParts(){
		return parts;
	}	
	
	/**
	 * Method to return a string that represents this specification.
	 * @return Returns the string that represenets this specification.
	 */
	@Override
	public String toString(){
		String str = this.model.toString() + "; (";
		for(CarPart part : parts){
			str += part.toString() + ", ";
		}
		str = str.substring(0, str.length()-2);
		str += ")";
		return str;
	}
}
