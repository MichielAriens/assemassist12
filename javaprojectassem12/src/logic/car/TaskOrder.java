package logic.car;

import java.util.List;

import logic.workstation.Task;

import org.joda.time.DateTime;

public class TaskOrder extends Order{
	
	private TaskOrderDetails details;
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
}
