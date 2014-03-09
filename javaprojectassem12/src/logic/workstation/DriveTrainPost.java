package logic.workstation;

import java.util.Arrays;
import java.util.List;

import logic.car.CarPartType;



public class DriveTrainPost extends Workstation {
	
	private static List<CarPartType> compatibleTasks;
	{
		CarPartType[] array= {CarPartType.Engine, CarPartType.Gearbox};
		compatibleTasks = (List<CarPartType>) Arrays.asList(array);
	}

	
	
	/**
	 * Get the types of tasks this workstation can perform. This methods completely defines the capabilities of an implementing class.
	 * @return		A List of CarPartType elements defining the capabilities of the implementation. 
	 * 				({CarPartType.Engine, CarPartType.Gearbox})
	 */
	@Override
	public
	List<CarPartType> getCapabilities() {
		return DriveTrainPost.compatibleTasks;
	}
	
	@Override
	public String toString(){
		return "Drive Train Post";
	}

}
