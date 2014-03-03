package logic.workstation;

import java.util.Arrays;
import java.util.List;

import logic.car.CarPartType;


public class AccessoriesPost extends Workstation {
	
	private static List<CarPartType> compatibleTasks;
	{
		CarPartType[] array= {CarPartType.Seats, CarPartType.Airco, CarPartType.Wheels};
		compatibleTasks = (List<CarPartType>) Arrays.asList(array);
	}


	@Override
	public
	List<CarPartType> getCapabilities() {
		return AccessoriesPost.compatibleTasks;
	}

}
