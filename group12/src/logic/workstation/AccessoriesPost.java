package logic.workstation;

import java.util.Arrays;
import java.util.List;

import logic.car.CarPartType;

/**
 * Class used to describe an accessories post of an assembly line.
 */
public class AccessoriesPost extends Workstation {

	/**
	 * Initializes the compatible tasks for this accessories post.
	 */
	private static List<CarPartType> compatibleTasks;
	{
		CarPartType[] array= {CarPartType.Seats, CarPartType.Airco, CarPartType.Wheels};
		compatibleTasks = (List<CarPartType>) Arrays.asList(array);
	}

	/**
	 * Get the types of tasks this workstation can perform. This methods completely 
	 * defines the capabilities of an implementing class.
	 * @return		A List of CarPartType elements defining the capabilities of the implementation. 
	 * 				({CarPartType.Seats, CarPartType.Airco, CarPartType.Wheels})
	 */
	@Override
	public List<CarPartType> getCapabilities() {
		return AccessoriesPost.compatibleTasks;
	}
	
	/**
	 * Returns a string representation of an accessories post.
	 */
	@Override
	public String toString(){
		return "Accessories Post";
	}

}
