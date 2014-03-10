package logic.car;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import logic.workstation.Task;

/**
 * Class used to describe a car order.
 */
public class CarOrder {

	/**
	 * The time this car order was created.
	 */
	private DateTime startTime;
	
	/**
	 * The time this car order was completed.
	 */
	private DateTime endTime;
	
	/**
	 * An estimated time for when this car order will be finished.
	 * If the car order is finished this will be the same as the end time.
	 */
	private DateTime estimatedEndTime;
	
	/** 
	 * The specifications for this car order.
	 */
	private CarSpecification carSpecification;
	
	/**
	 * List of tasks that need to be completed to complete this car order.
	 */
	private List<Task> tasks = new ArrayList<Task>();

	/**
	 * Creates a car order and initializes the car specification to the given car
	 * specification. Builds the tasks that need to be completed for this car order to
	 * be completed.
	 * @param carSpecification	The car specification defining this car order.
	 */
	public CarOrder(CarSpecification carSpecification){
		this.carSpecification = carSpecification;
		buildTasks();
	}

	/**
	 * Builds a list of tasks that need to be completed for this car order to be completed.
	 */
	private void buildTasks() {
		for(CarPart part : this.getCarSpecification().getParts()){
			this.tasks.add(new Task(part));
		}
	}

	/**
	 * Returns the list of tasks that need to be completed to complete this car order.
	 * @return	The list of tasks.
	 */
	public List<Task> getTasks(){
		return this.tasks;
	}

	/**
	 * Returns the time this car order was created.
	 * @return The time this car order was created.
	 */
	public DateTime getStartTime() {
		return startTime;
	}

	/**
	 * 
	 * @return
	 */
	public DateTime getEndTime() {
		return endTime;
	}

	/**
	 * Returns the time this car order was completed.
	 * If the car order is finished this will be the same as the end time.
	 * @return The time this car order was completed.
	 */
	public DateTime getEstimatedEndTime() {
		return estimatedEndTime;
	}

	/**
	 * An estimated time for when this car order will be finished.
	 * @return An estimated time for when this car order will be finished.
	 */
	public CarSpecification getCarSpecification() {
		return carSpecification;
	}

	/**
	 * 
	 * @param time	Time to set
	 */
	public void setEstimatedEndTime(DateTime time){
		this.estimatedEndTime = time;
	}
	
	/**
	 * 
	 * @param time
	 */
	public void setEndTime(DateTime time){
		this.endTime = time;
		this.estimatedEndTime = endTime;
	}

	/**
	 * 
	 * @param startTime
	 */
	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}

	/**
	 * Checks if this car order is completed by checking if all its tasks are completed.
	 * @return 
	 */
	public boolean done(){
		boolean retVal = true;
		for(Task task : this.tasks){
			if(!task.isComplete()){
				retVal = false;
				break;
			}
		}
		return retVal;
	}
	
	/**
	 * 
	 */
	@Override
	public String toString(){
		String str;
		if(this.done())
			str= endTime.toString();
		else
			str =estimatedEndTime.toString();
		str += " : " + carSpecification.toString();
		return str;
	}
}

