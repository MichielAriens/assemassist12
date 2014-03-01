package workstation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import car.CarPartType;



public class DriveTrainPost extends Workstation {
	
	private static ArrayList<CarPartType> compatibleTasks;
	{
		CarPartType[] array= {CarPartType.Engine, CarPartType.Gearbox};
		compatibleTasks = (ArrayList<CarPartType>) Arrays.asList(array);
	}

	
	
	
	@Override
	List<CarPartType> getDoableTasks() {
		return DriveTrainPost.compatibleTasks;
	}

}
