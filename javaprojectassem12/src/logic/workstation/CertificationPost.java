package logic.workstation;

import java.util.Arrays;
import java.util.List;

import logic.car.VehiclePartType;

/**
 * Class used to describe a car body post of an assembly line. //TODO fix docu
 */
public class CertificationPost extends Workstation {

	/**
	 * Initializes the compatible tasks for this car body post.
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
	 * 				({CarPartType.Body, CarPartType.Colour})
	 */
	@Override
	public List<VehiclePartType> getCapabilities(){
		return this.compatibleTasks;
	}
	
	/**
	 * Get a new instance of CarBodyPost.
	 */
	@Override
	protected Workstation getRawCopy(){
		return new CertificationPost();
	}
	
	/**
	 * Returns a string representation of a car body post.
	 */
	@Override
	public String toString(){
		return "Certification Post";
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
