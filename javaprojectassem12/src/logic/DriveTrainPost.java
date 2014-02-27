package logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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
