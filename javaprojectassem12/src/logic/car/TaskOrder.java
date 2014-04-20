package logic.car;

import java.util.List;

import logic.workstation.Task;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * A class holding the information about task orders.
 */
public class TaskOrder extends Order{
	
	/**
	 * A variable holding the details for this task. 
	 */
	private TaskOrderDetails details;
	
	/**
	 * A variable holding the phase duration of this task order.
	 */
	private final int PHASE_TIME = 60;
	
	public TaskOrder(TaskOrderDetails details){
		this.details = details;
	}

	@Override
	public DateTime getDeadLine() {
		return this.details.getDeadLine();
	}

	@Override
	public List<Task> getTasks() {
		return details.getPendingTasks();
	}
	
	/**
	 * Checks if this task order is completed by checking if all its tasks are completed.
	 * @return 	True if this task order is completed.
	 * 			False if this task order is not completed.
	 */
	@Override
	public boolean done() {
		for(Task t : details.getPendingTasks()){
			if(!t.isComplete())
				return false;
		}
		return true;
	}

	@Override
	public int getPhaseTime() {
		return this.PHASE_TIME;
	}
	
	@Override
	public boolean equals(Object obj){
		return super.equals(obj);
	}
	
	@Override
	public Order getRawCopy(){
		return new TaskOrder(details.getRawCopy());
	}
	
	public String toString(){
		DateTimeFormatter fmt = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm");
		return "Estimated completion: " + fmt.print(super.getEstimatedEndTime());
	}
}
