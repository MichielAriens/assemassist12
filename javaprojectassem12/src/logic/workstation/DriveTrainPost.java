package logic.workstation;

import java.util.Arrays;
import java.util.List;

import logic.car.CarPartType;

/**
 * Class used to describe a drive train post of an assembly line.
 */
public class DriveTrainPost extends Workstation {
	
	/**
	 * Initializes the compatible tasks for this drive train post.
	 */
	private static List<CarPartType> compatibleTasks;
	{
		CarPartType[] array= {CarPartType.Engine, CarPartType.Gearbox};
		compatibleTasks = (List<CarPartType>) Arrays.asList(array);
	}

	/**
	 * Get the types of tasks this workstation can perform. This methods completely 
	 * defines the capabilities of an implementing class.
	 * @return		A List of CarPartType elements defining the capabilities of the implementation. 
	 * 				({CarPartType.Engine, CarPartType.Gearbox})
	 */
	@Override
	public
	List<CarPartType> getCapabilities() {
		return DriveTrainPost.compatibleTasks;
	}
	
	@Override
	protected Workstation getRawCopy(){
		return new DriveTrainPost();
	}
	
	/**
	 * Returns a string representation of a drive train post.
	 */
	@Override
	public String toString(){
		return "Drive Train Post";
	}

}
