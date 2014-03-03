package workstation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import car.CarPartType;


public class CarBodyPost extends Workstation {

	private static ArrayList<CarPartType> compatibleTasks;
	{
		CarPartType[] array= {CarPartType.Body, CarPartType.Colour};
		compatibleTasks = (ArrayList<CarPartType>) Arrays.asList(array);
	}

	
	
	
	@Override
	List<CarPartType> getDoableTasks() {
		return CarBodyPost.compatibleTasks;
	}
}