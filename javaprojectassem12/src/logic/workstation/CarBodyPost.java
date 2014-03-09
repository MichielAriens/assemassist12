package logic.workstation;

import java.util.Arrays;
import java.util.List;

import logic.car.CarPartType;


public class CarBodyPost extends Workstation {

	private static List<CarPartType> compatibleTasks;
	{
		CarPartType[] array= {CarPartType.Body, CarPartType.Colour};
		compatibleTasks = (List<CarPartType>) Arrays.asList(array);
	}

	/**
	 * Get the types of tasks this workstation can perform. This methods completely defines the capabilities of an implementing class.
	 * @return		A List of CarPartType elements defining the capabilities of the implementation. 
	 * 				({CarPartType.Body, CarPartType.Colour})
	 */
	@Override
	public
	List<CarPartType> getCapabilities(){
		return CarBodyPost.compatibleTasks;
	}
	
	@Override
	public String toString(){
		return "Car Body Post";
	}

}
