package logic.workstation;



import logic.car.CarPart;


/**
 * 
 * @author Team 12
 */
public class Task {
	private boolean completed = false;
	
	private final CarPart part;
	
	/**
	 * Creates a task corresponding with the installation of the prt provided
	 * @param part	The CarPart to install
	 */
	public Task(CarPart part){
		this.part = part;
	}
	
	/**
	 * The CarPart corresponding to this task
	 */
	public CarPart getCarPart(){
		return part;
	}
	
	/**
	 * Perform this task.
	 * @param endTime2
	 * @param mechanic
	 */
	public void perform(){
		
		if(this.isComplete()){
			return;
		}
		this.completed = true;
		
	}
	
	
	/**
	 * Checks whether the task is completed.
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
		return "Install " + this.part.type.toString() + "= " + this.part.toString();
	}
	
	/**
	 * Returns the string representation of the description of this task.
	 * @return The string representation of the description of this task.
	 */
	public String getDescription(){
		return "Task description:\n   -Type of part needed: " + this.part.type.toString() + ",\n   -Car Part: " + this.part.toString() + "\n";
	}
	
}
