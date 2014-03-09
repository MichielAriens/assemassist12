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
	
}
