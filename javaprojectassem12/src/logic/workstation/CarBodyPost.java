package logic.workstation;

import java.util.Arrays;
import java.util.List;

import logic.car.CarPartType;

/**
 * Class used to describe a car body post of an assembly line.
 */
public class CarBodyPost extends Workstation {

	/**
	 * Initializes the compatible tasks for this car body post.
	 */
	private List<CarPartType> compatibleTasks;
	{
		CarPartType[] array= {CarPartType.Body, CarPartType.Colour};
		compatibleTasks = (List<CarPartType>) Arrays.asList(array);
	}

	/**
	 * Get the types of tasks this workstation can perform. This methods completely 
	 * defines the capabilities of an implementing class.
	 * @return		A List of CarPartType elements defining the capabilities of the implementation. 
	 * 				({CarPartType.Body, CarPartType.Colour})
	 */
	@Override
	public List<CarPartType> getCapabilities(){
		return this.compatibleTasks;
	}
	
	/**
	 * Get a new instance of CarBodyPost.
	 */
	@Override
	protected Workstation getRawCopy(){
		return new CarBodyPost();
	}
	
	/**
	 * Returns a string representation of a car body post.
	 */
	@Override
	public String toString(){
		return "Car Body Post";
	}

}
