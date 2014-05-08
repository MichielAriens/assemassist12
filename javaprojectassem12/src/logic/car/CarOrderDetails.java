package logic.car;

import java.util.ArrayList;
import java.util.List;

import logic.workstation.Task;

/**
 * A class holding more information about car orders.
 */
public class CarOrderDetails{
	
	/**
	 * A variable holding the type of car model this is.
	 */
	private VehicleModel model;
	
	/**
	 * A variable holding a list of tasks that need to be done for this car order's detail.
	 */
	private ArrayList<Task> tasks;
	
	/**
	 * Initializes the model and tasks of this car order's detail.
	 * @param model	The model of this car order's detail.
	 * @param parts	The parts required for specific tasks that need to be completed for this car order's detail.
	 */
	public CarOrderDetails(VehicleModel model, List<VehiclePart> parts){
		this.model = model;
		buildTasks(parts);		
	}
	
	/**
	 * Builds the tasks of this car order's detail with the given car parts.
	 * @param parts	The list of car parts that need to be on this car.
	 */
	private void buildTasks(List<VehiclePart> parts){
		this.tasks = new ArrayList<Task>();
		for(VehiclePart p: parts){
			this.tasks.add(new Task(p));
		}
	}

	/**
	 * Returns a list of tasks that still need to be completed for this car order's detail.
	 * @return	A list of remaining tasks, an empty list if there are no more remaining tasks.
	 */
	public List<Task> getPendingTasks() {
		return this.tasks;
	}

	/**
	 * Returns the phase duration of this object's model.
	 * @return	An integer containing the phase duration of this detail's model.
	 */
	public int getPhaseTime() {
		return model.phaseDuration;
	}
	
	/**
	 * Makes a new car order detail and sets the tasks and model values to this object's values.
	 * @return	A copy of this object with the parts and model copied into it.
	 */
	public CarOrderDetails getRawCopy(){
		ArrayList<VehiclePart> parts = new ArrayList<VehiclePart>();
		for(Task t : tasks){
			parts.add(t.getCarPart());
		}
		return new CarOrderDetails(model, parts);
	}
	
	/**
	 * Builds a string which is a representation of this detail's model and tasks.
	 * @return	A string representation of this object containing the model and the tasks required.
	 */
	@Override
	public String toString(){
		String str = this.model.toString() + "; (";
		for(Task task : tasks){
			str += task.getCarPart().toString() + ", ";
		}
		str = str.substring(0, str.length()-2);
		str += ")";
		return str;
	}

}
