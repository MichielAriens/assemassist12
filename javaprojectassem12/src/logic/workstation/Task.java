package logic.workstation;

import interfaces.Printable;
import logic.car.VehiclePart;

/**
 * Class used to describe a task that needs to be performed in order to complete a car order.
 */
public class Task implements Printable{
	
	/**
	 * Flag keeping track of the completion of the task. 
	 */
	private boolean completed = false;
	
	//TODO DOCU
	private int elapsedTime = -1;
	
	private final int estimatedPhaseDuration;
	
	/**
	 * The car part where upon the task needs to be performed.
	 */
	private final VehiclePart carPart;
	
	/**
	 * Creates a task corresponding with the given car part.
	 * @param part	The car part to perform the task on. //TODO docu
	 */
	public Task(VehiclePart part, int estimDur){
		this.carPart = part;
		this.estimatedPhaseDuration = estimDur;
	}
	
	/**
	 * Returns a copy of this task.
	 * @return	a copy of this task.
	 */
	public Task getRawCopy(){
		Task copy = new Task(this.carPart, 0);
		if(this.isComplete())
			copy.perform(0);
		return copy;
	}
	
	/**
	 * Returns the car part corresponding to this task.
	 * @return the car part corresponding to this task.
	 */
	public VehiclePart getCarPart(){
		return carPart;
	}
	
	/**
	 * Sets completed to true. //TODO docu
	 */
	public void perform(int elapsedTime){
		if(this.completed)
			return;
		this.completed = true;
		this.elapsedTime = elapsedTime;
	}	
	
	//TODO docu
	public int getElapsedTime(){
		return this.elapsedTime;
	}
	
	//TODO DOCU
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
		return "Install " + this.getCarPart().type.toString() + "= " + this.getCarPart().toString();
	}
	
	/**
	 * Returns the string representation of the description of this task.
	 * @return The string representation of the description of this task.
	 */
	private String getDescription(){
		return "Task description:\n   -Type of part needed: " + this.getCarPart().type.toString() + ",\n   -Car Part: " + this.getCarPart().toString() + "\n";
	}
	
	/**
	 * Checks if this Task is equal to a given object.
	 * @return	False	if the given object is null, or if the given object is not an instance of the Task class,
	 * 					Or if the given object does not have the same CarPart as this Task.
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
	 * @return	True	if the CarPart of this Task is the same as the CarPart of the other Task.
	 * 			False	otherwise.
	 */
	private boolean equals(Task other){
		return this.carPart == other.carPart;
	}

	@Override
	public String getStringRepresentation() {
		return this.toString();
	}
	
	//TODO: fix this up
	
	@Override
	public String getExtraInformation() {
		return this.getDescription();
	}

	@Override
	public String getStatus() {
		if(this.isComplete())
			return "Completed";
		return "Pending";
	}
}
