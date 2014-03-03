package logic.workstation;

import logic.car.CarPart;
import logic.users.Mechanic;

/**
 * 
 * @author Team 12
 */
public class CopyOfTask {
	private int duration = 60;
	private int progress = 0;
	private Mechanic mechanic;
	
	public final CarPart part;
	
	
	public CopyOfTask(CarPart part){
		this.part = part;
	}
	

	/**
	 * Delay this task as specified. This means that extra work must be done to complete the task. 
	 * @param delay		The time delay in minutes.
	 */
	public void delay(float delay){
		this.duration += delay;
	}
	
	/**
	 * Perform this task. for the specified amount of minutes.
	 * @param minutes
	 * @param mechanic
	 */
	public void work(int minutes, Mechanic mechanic){
		float actualWork = this.work(minutes);
		mechanic.work(actualWork);
	}
	
	
	/**
	 * The mechanic works the specified amount of minutes on the task. If more work is specified than is necessary the mechanic stops early.
	 * Returns the actual amount of minutes that was worked.
	 * @param minutes		Amount of time worked in minutes
	 * @return				The actual amount worked. Is capped by the duration of the task.
	 */
	private int work(int minutes){
		if(this.isComplete()){
			return 0;
		}else if(progress + minutes > duration){
			progress = duration;
			return duration - progress;
		}else{
			progress += minutes;
			return minutes;
		}
	}
	
	/**
	 * Checks whether the task is completed.
	 * @return
	 */
	public boolean isComplete(){
		return progress >= duration;
	}
	
}
