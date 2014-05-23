package logic.workstation;

import java.util.Arrays;
import java.util.List;

import logic.order.VehiclePartType;

/**
 * Class used to describe a car body post of an assembly line.
 */
public class CarBodyPost extends Workstation {

	/**
	 * Initializes the compatible tasks for this car body post.
	 */
	private List<VehiclePartType> compatibleTasks;
	{
		VehiclePartType[] array= {VehiclePartType.Body, VehiclePartType.Colour};
		compatibleTasks = (List<VehiclePartType>) Arrays.asList(array);
	}

	/**
	 * Get the types of tasks this workstation can perform. This methods completely 
	 * defines the capabilities of an implementing class.
	 * @return		A List of VehiclePartType elements defining the capabilities of the implementation. 
	 * 				({VehiclePartType.Body, VehiclePartType.Colour})
	 */
	@Override
	public List<VehiclePartType> getCapabilities(){
		return this.compatibleTasks;
	}
	
	/**
	 * Returns a string representation of a car body post.
	 */
	@Override
	public String toString(){
		return "Car Body Post";
	}

	/**
	 * Returns a string representation of a car body post.
	 */
	@Override
	public String getStringRepresentation() {
		return this.toString();
	}
}
