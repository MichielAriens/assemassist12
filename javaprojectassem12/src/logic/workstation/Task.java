package logic.workstation;

import interfaces.Printable;
import logic.car.VehiclePart;

/**
 * Class used to describe a task that needs to be performed in order to complete a vehicle order.
 */
public class Task implements Printable<Task>{
	
	/**
	 * Flag keeping track of the completion of the task. 
	 */
	private boolean completed = false;
	
	/**
	 * The time a mechanic worked on this task to complete it.
	 */
	private int elapsedTime = -1;
	
	/**
	 * The estimated time a mechanic should work on this task to complete it.
	 */
	private final int estimatedPhaseDuration;
	
	/**
	 * The vehicle part where upon the task needs to be performed.
	 */
	private final VehiclePart vehiclePart;
	
	/**
	 * Creates a task corresponding with the given vehicle part, and estimated duration.
	 * @param part	The vehicle part to perform the task on.
	 * @param estimDur The estimated duration for this task to be completed.
	 */
	public Task(VehiclePart part, int estimDur){
		if(part.autocompl){
			this.completed = true;
		}
		this.vehiclePart = part;
		this.estimatedPhaseDuration = estimDur;
	}
	
	/**
	 * Returns the vehicle part corresponding to this task.
	 * @return the vehicle part corresponding to this task.
	 */
	public VehiclePart getVehiclePart(){
		return vehiclePart;
	}
	
	/**
	 * Sets completed to true, and saves the time it took to complete it.
	 * @param elapsedTime	The time it took to complete this task.
	 */
	public void perform(int elapsedTime){
		if(this.completed)
			return;
		this.completed = true;
		this.elapsedTime = elapsedTime;
	}	
	
	/**
	 * Returns the time a mechanic worked on this task to complete it.
	 * @return the time a mechanic worked on this task to complete it.
	 */
	public int getElapsedTime(){
		return this.elapsedTime;
	}

	/**
	 * Returns the estimated time a mechanic should work on this task to complete it.
	 * @return the estimated time a mechanic should work on this task to complete it.
	 */
	public int getEstimatedPhaseDuration(){
		return this.estimatedPhaseDuration;
	}
	
	/**
	 * Checks whether the task is completed.
	 * @return 	True if the task is completed.
	 * 			False otherwise.
	 */
	public boolean isComplete(){
		return completed;
	}
	
	/**
	 * Returns the string representation of this task.
	 * @return The string representation of this task.
	 */
	@Override
	public String toString(){
		return "Install " + this.getVehiclePart().type.toString() + "= " + this.getVehiclePart().toString();
	}
	
	/**
	 * Returns the string representation of the description of this task.
	 * @return The string representation of the description of this task.
	 */
	private String getDescription(){
		return "Task description:\n   -Type of part needed: " + this.getVehiclePart().type.toString() + ",\n   -Car Part: " + this.getVehiclePart().toString() + "\n";
	}
	
	/**
	 * Checks if this Task is equal to a given object.
	 * @return	False	if the given object is null, or if the given object is not an instance of the Task class,
	 * 					Or if the given object does not have the same vehiclePart as this Task.
	 * 			True 	otherwise.
	 */
	@Override
	public boolean equals(Object obj){
		if(obj == null) return false;
		if(this.getClass() != obj.getClass()) return false;
		return equals((Task) obj);
	}
	
	/**
	 * Checks if this Task is equal to a given Task.
	 * @param other	The other Task that needs to be checked.
	 * @return	True	if the CarPart of this Task is the same as the vehiclePart of the other Task.
	 * 			False	otherwise.
	 */
	private boolean equals(Task other){
		return this.vehiclePart == other.vehiclePart;
	}
	
	/**
	 * Returns the string representation of this task.
	 * @return The string representation of this task.
	 */
	@Override
	public String getStringRepresentation() {
		return this.toString();
	}
	
	/**
	 * Returns the string representation of the description of this task.
	 * @return The string representation of the description of this task.
	 */
	@Override
	public String getExtraInformation() {
		return this.getDescription();
	}
	
	/**
	 * Returns a string representation of the current status of this task.
	 * @return 	"Completed" if the task is completed.
	 * 			"Pending"	otherwise.
	 */
	@Override
	public String getStatus() {
		if(this.isComplete())
			return "Completed";
		return "Pending";
	}
}
