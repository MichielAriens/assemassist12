package logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccessoriesPost extends Workstation {
	
	private static ArrayList<CarPartType> compatibleTasks;
	{
		CarPartType[] array= {CarPartType.Seats, CarPartType.Airco, CarPartType.Wheels};
		compatibleTasks = (ArrayList<CarPartType>) Arrays.asList(array);
	}

	

	@Override
	List<CarPartType> getDoableTasks() {
		return AccessoriesPost.compatibleTasks;
	}

}
