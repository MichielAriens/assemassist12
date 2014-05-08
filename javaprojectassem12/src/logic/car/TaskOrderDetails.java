package logic.car;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import logic.workstation.Task;

/**
 * A class holding more information about task orders.
 */
public class TaskOrderDetails{
	
	/**
	 * The absolute end time for this task order's details.
	 */
	private DateTime deadLine;
	
	/**
	 * A list of tasks that need to be performed for the part involving this task order's details.
	 */
	private ArrayList<Task> tasks;
	
	/**
	 * Constructs a new task order's details by initializing the part and deadline of this 
	 * task order's detail with the given parameters.
	 * @param part	The part of this task order detail.
	 * @param deadLine	The absolute end time for this task order.
	 */
	public TaskOrderDetails(VehiclePart part, DateTime deadLine){
		this.deadLine = deadLine;
		buildTasks(part);
	}
	
	/**
	 * Builds the list of tasks that need to be performed for the part involving this task
	 * order's details.
	 * @param part The part for which the tasks need to be build.
	 */
	private void buildTasks(VehiclePart part){
		tasks = new ArrayList<Task>();
		tasks.add(new Task(part));
	}

	/**
	 * Returns the pending tasks for this task order's details.
	 * @return the pending tasks for this task order's details.
	 */
	public List<Task> getPendingTasks() {
		return tasks;
	}

	/**
	 * Returns the dead line for this task order's details.
	 * @return the dead line for this task order's details.
	 */
	public DateTime getDeadLine() {
		return this.deadLine;
	}
	
	/**
	 * Returns a raw copy for this task order's details.
	 * @return a raw copy for this task order's details.
	 */
	public TaskOrderDetails getRawCopy(){
		return new TaskOrderDetails(tasks.get(0).getCarPart(), null);
	}
}
