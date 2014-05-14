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
	 * Creates a new task order by initializing the task order details with the given details.
	 * @param details	The details for this task order.
	 */
	public TaskOrder(TaskOrderDetails details){
		this.details = details;
	}

	/**
	 * Returns the time this task order needs to be finished.
	 * @return	The time which is the absolute end time of this task order.
	 */
	@Override
	public DateTime getDeadLine() {
		return this.details.getDeadLine();
	}

	/**
	 * Returns a list of tasks that are required to complete this task order.
	 * @return	A list containing different tasks which need to be done for this task order.
	 */
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
	
	/**
	 * Checks if this task order's tasks are the same as a given task order's tasks.
	 * @param other	The other task order against which we want to check equality.
	 * @return	True if this task order's tasks are the same as the given task order's tasks.
	 * 			False otherwise.
	 */
	@Override
	public boolean equals(Object obj){
		return super.equals(obj);
	}
	
	/**
	 * Returns a copy of this task order
	 * @return	A copy of this task order.
	 */
	@Override
	public Order getRawCopy(){
		return new TaskOrder(details.getRawCopy());
	}
	
	/**
	 * Returns a string representation for the estimated completion time for this task order.
	 * @return a string representation for the estimated completion time for this task order.
	 */
	public String toString(){
		DateTimeFormatter fmt = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm");
		return "Estimated completion: " + fmt.print(super.getEstimatedEndTime());
	}

	@Override
	public VehicleModel getModel(){
		return null;
	}
	
	/**
	 * Returns a string representation for the estimated completion time for this task order.
	 * @return a string representation for the estimated completion time for this task order.
	 */
	@Override
	public String getStringRepresentation() {
		return this.toString();
	}
	
	//TODO: docushit
	@Override
	public String getExtraInformation() {

		return null;
	}

	@Override
	public String getStatus() {
		if(this.done())
			return "Completed";
		return "Pending";
	}
}
