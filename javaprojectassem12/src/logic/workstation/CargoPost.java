package logic.workstation;

import java.util.Arrays;
import java.util.List;

import logic.order.VehiclePartType;

/**
 * Class used to describe a cargo post of an assembly line.
 */
public class CargoPost extends Workstation {
	
	/**
	 * Initializes the compatible tasks for this cargo post.
	 */
	private List<VehiclePartType> compatibleTasks;
	{
		VehiclePartType[] array= {VehiclePartType.ToolStorage, VehiclePartType.CargoProtection};
		compatibleTasks = (List<VehiclePartType>) Arrays.asList(array);
	}

	/**
	 * Get the types of tasks this workstation can perform. This methods completely 
	 * defines the capabilities of an implementing class.
	 * @return		A List of VehiclePartType elements defining the capabilities of the implementation. 
	 * 				({VehiclePartType.ToolStorage, VehiclePartType.CargoProtection})
	 */
	@Override
	public List<VehiclePartType> getCapabilities() {
		return this.compatibleTasks;
	}
	
	/**
	 * Returns a string representation of a cargo post.
	 */
	@Override
	public String toString(){
		return "Cargo Post";
	}

	/**
	 * Returns a string representation of a cargo post.
	 */
	@Override
	public String getStringRepresentation() {
		return this.toString();
	}
}
