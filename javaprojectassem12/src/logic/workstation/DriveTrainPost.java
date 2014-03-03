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

	
	
	
	@Override
	public
	List<CarPartType> getCapabilities() {
		return DriveTrainPost.compatibleTasks;
	}

}
