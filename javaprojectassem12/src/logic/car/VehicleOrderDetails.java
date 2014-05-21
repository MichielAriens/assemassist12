package logic.car;

import java.util.ArrayList;
import java.util.List;

import logic.workstation.Task;

/**
 * A class holding more information about vehicle orders.
 */
public class VehicleOrderDetails{
	
	/**
	 * A variable holding the type of vehicle model this is.
	 */
	private VehicleModel model;
	
	/**
	 * A variable holding a list of tasks that need to be done for this vehicle order's detail.
	 */
	private ArrayList<Task> tasks;
	
	/**
	 * Initializes the model and tasks of this vehicle order's detail.
	 * @param model	The model of this vehicle order's detail.
	 * @param parts	The parts required for specific tasks that need to be completed for this vehicle order's detail.
	 */
	public VehicleOrderDetails(VehicleModel model, List<VehiclePart> parts){
		this.model = model;
		buildTasks(parts);		
	}
	
	/**
	 * Builds the tasks of this vehicle order's detail with the given vehicle parts.
	 * @param parts	The list of vehicle parts that need to be on this vehicle.
	 */
	private void buildTasks(List<VehiclePart> parts){
		this.tasks = new ArrayList<Task>();
		for(VehiclePart p: parts){
			this.tasks.add(new Task(p, model.getPartPhaseDuration(p)));
		}
	}

	/**
	 * Returns a list of tasks that still need to be completed for this vehicle order's detail.
	 * @return	A list of remaining tasks, an empty list if there are no more remaining tasks.
	 */
	public List<Task> getPendingTasks() {
		return this.tasks;
	}

	/**
	 * Returns the phase duration of this object's model.
	 * @return	An integer containing the phase duration of this detail's model.
	 */
	public int getPhaseTime() { //TODO verwijderen maybe
		return model.standardPhaseDur;
	}
	
	/**
	 * Makes a new vehicle order detail and sets the tasks and model values to this object's values.
	 * @return	A copy of this object with the parts and model copied into it.
	 */
	public VehicleOrderDetails getRawCopy(){
		ArrayList<VehiclePart> parts = new ArrayList<VehiclePart>();
		for(Task t : tasks){
			parts.add(t.getVehiclePart());
		}
		return new VehicleOrderDetails(model, parts);
	}
	
	/**
	 * Builds a string which is a representation of this detail's model and tasks.
	 * @return	A string representation of this object containing the model and the tasks required.
	 */
	@Override
	public String toString(){
		String str = this.model.toString() + "; (";
		for(Task task : tasks){
			str += task.getVehiclePart().toString() + ", ";
		}
		str = str.substring(0, str.length()-2);
		str += ")";
		return str;
	}
	
	public VehicleModel getModel(){
		return this.model;
	}

}
