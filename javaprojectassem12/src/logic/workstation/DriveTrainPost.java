package logic.workstation;

import java.util.Arrays;
import java.util.List;

import logic.order.VehiclePartType;

/**
 * Class used to describe a drive train post of an assembly line.
 */
public class DriveTrainPost extends Workstation {
	
	/**
	 * Initializes the compatible tasks for this drive train post.
	 */
	private List<VehiclePartType> compatibleTasks;
	{
		VehiclePartType[] array= {VehiclePartType.Engine, VehiclePartType.Gearbox};
		compatibleTasks = (List<VehiclePartType>) Arrays.asList(array);
	}

	/**
	 * Get the types of tasks this workstation can perform. This methods completely 
	 * defines the capabilities of an implementing class.
	 * @return		A List of VehiclePartType elements defining the capabilities of the implementation. 
	 * 				({VehiclePartType.Engine, VehiclePartType.Gearbox})
	 */
	@Override
	public List<VehiclePartType> getCapabilities() {
		return this.compatibleTasks;
	}
	
	/**
	 * Returns a string representation of a drive train post.
	 */
	@Override
	public String toString(){
		return "Drive Train Post";
	}
	
	/**
	 * Returns a string representation of a drive train post.
	 */
	@Override
	public String getStringRepresentation() {
		return this.toString();
	}
}
