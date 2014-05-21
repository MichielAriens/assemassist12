package logic.workstation;

import java.util.Arrays;
import java.util.List;

import logic.car.VehiclePartType;

/**
 * Class used to describe a certification post of an assembly line.
 */
public class CertificationPost extends Workstation {

	/**
	 * Initializes the compatible tasks for this certification post.
	 */
	private List<VehiclePartType> compatibleTasks;
	{
		VehiclePartType[] array= {VehiclePartType.Certification};
		compatibleTasks = (List<VehiclePartType>) Arrays.asList(array);
	}

	/**
	 * Get the types of tasks this workstation can perform. This methods completely 
	 * defines the capabilities of an implementing class.
	 * @return		A List of VehiclePartType elements defining the capabilities of the implementation. 
	 * 				({VehiclePartType.Certification})
	 */
	@Override
	public List<VehiclePartType> getCapabilities(){
		return this.compatibleTasks;
	}
	
	/**
	 * Returns a string representation of a certification post.
	 */
	@Override
	public String toString(){
		return "Certification Post";
	}
	
	/**
	 * Returns a string representation of a certification post.
	 */
	@Override
	public String getStringRepresentation() {
		return this.toString();
	}
}
