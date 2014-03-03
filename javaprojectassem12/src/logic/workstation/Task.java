package logic.workstation;

import java.sql.Time;

import logic.car.CarPart;
import logic.users.Mechanic;

/**
 * 
 * @author Team 12
 */
public class Task {
	private Time endTime = null;
	private Mechanic mechanic = null;
	private final CarPart part;
	
	public Task(CarPart part){
		this.part = part;
	}
	
	
	public CarPart getCarPart(){
		return part;
	}
	
	/**
	 * Perform this task.
	 * @param endTime
	 * @param mechanic
	 */
	public void perform(Time endTime, Mechanic mechanic){
		if(endTime == null || mechanic == null){
			return;
		}
		if(this.isComplete()){
			return;
		}
		this.endTime = endTime;
		this.mechanic = mechanic;
	}
	
	
	/**
	 * Checks whether the task is completed.
	 * @return
	 */
	public boolean isComplete(){
		return endTime != null;
	}
	
}
