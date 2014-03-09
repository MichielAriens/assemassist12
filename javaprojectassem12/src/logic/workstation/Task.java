package logic.workstation;



import logic.car.CarPart;
import logic.users.Mechanic;

/**
 * 
 * @author Team 12
 */
public class Task {
	private boolean completed = false;
	private Mechanic mechanic = null;
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
	public void perform(Calendar endTime, Mechanic mechanic){
		if(endTime == null || mechanic == null){
			return;
		}
		if(this.isComplete()){
			return;
		}
		this.completed = true;
		this.mechanic = mechanic;
	}
	
	
	/**
	 * Checks whether the task is completed.
	 */
	public boolean isComplete(){
		return completed;
	}
	
}
