package logic.workstation;

import logic.car.CarPart;

/**
 * Class used to describe a task that needs to be performed in order to complete a car order.
 */
public class Task {
	
	/**
	 * Flag keeping track of the completion of the task. 
	 */
	private boolean completed = false;
	
	/**
	 * The car part where upon the task needs to be performed.
	 */
	private final CarPart carPart;
	
	/**
	 * Creates a task corresponding with the given car part.
	 * @param part	The car part to perform the task on.
	 */
	public Task(CarPart part){
		this.carPart = part;
	}
	
	/**
	 * Returns the car part corresponding to this task.
	 * @return the car part corresponding to this task.
	 */
	public CarPart getCarPart(){
		return carPart;
	}
	
	/**
	 * Performs this task. Sets completed to true.
	 */
	public void perform(){
		this.completed = true;
	}	
	
	/**
	 * Checks whether the task is completed.
	 * @return completed
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
	public String getDescription(){
		return "Task description:\n   -Type of part needed: " + this.getCarPart().type.toString() + ",\n   -Car Part: " + this.getCarPart().toString() + "\n";
	}
	
}
