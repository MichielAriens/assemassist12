package logic.workstation;

import java.util.Arrays;
import java.util.List;

import logic.car.VehiclePartType;

/**
 * Class used to describe a drive train post of an assembly line. //TODO fix docu
 */
public class CargoPost extends Workstation {
	
	/**
	 * Initializes the compatible tasks for this drive train post.
	 */
	private List<VehiclePartType> compatibleTasks;
	{
		VehiclePartType[] array= {VehiclePartType.ToolStorage, VehiclePartType.CargoProtection};
		compatibleTasks = (List<VehiclePartType>) Arrays.asList(array);
	}

	/**
	 * Get the types of tasks this workstation can perform. This methods completely 
	 * defines the capabilities of an implementing class.
	 * @return		A List of CarPartType elements defining the capabilities of the implementation. 
	 * 				({CarPartType.Engine, CarPartType.Gearbox})
	 */
	@Override
	public List<VehiclePartType> getCapabilities() {
		return this.compatibleTasks;
	}
	
	/**
	 * Get a new instance of DriveTrainPost.
	 */
	@Override
	protected Workstation getRawCopy(){
		return new CargoPost();
	}
	
	/**
	 * Returns a string representation of a cargo post.
	 */
	@Override
	public String toString(){
		return "Cargo Post";
	}

	@Override
	public String getStringRepresentation() {
		return this.toString();
	}

	@Override
	public String getExtraInformation() {
		// TODO Auto-generated method stub
		return null;
	}

}
