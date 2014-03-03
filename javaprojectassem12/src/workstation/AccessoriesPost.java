package workstation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import car.CarPartType;


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



	@Override
	List<CarPartType> getCapabilities() {
		// TODO Auto-generated method stub
		return null;
	}

}
