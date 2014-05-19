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
	 * @return		A List of CarPartType elements defining the capabilities of the implementation. 
	 * 				({CarPartType.Certification})
	 */
	@Override
	public List<VehiclePartType> getCapabilities(){
		return this.compatibleTasks;
	}
	
	/**
	 * Get a new instance of CertificationPost.
	 */
	@Override
	protected Workstation getRawCopy(){
		return new CertificationPost();
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
