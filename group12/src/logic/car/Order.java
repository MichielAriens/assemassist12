package logic.car;

import java.util.List;

import logic.workstation.Task;

import org.joda.time.DateTime;

/**
 * A class which represents different orders placed by users, only car orders and task orders available at the moment.
 */
public abstract class Order implements Comparable<Order>{
	
	/**
	 * The time this order was created.
	 */
	private DateTime startTime = null;
	
	/**
	 * The time this order was completed.
	 */
	private DateTime endTime;
	
	/**
	 * An estimated time for when this order will be finished.
	 * If the order is finished this will be the same as the end time.
	 */
	private DateTime estimatedEndTime;
	
	/**
	 * The amount of delay there is on this order in minutes.
	 */
	private int delay = 0;
	
	/**
	 * Returns the time this order was created.
	 * @return The time this order was created.
	 */
	public DateTime getStartTime() {
		return startTime;
	}

	/**
	 * Returns the time this order was completed.
	 * @return	The time this order was completed.
	 */
	public DateTime getEndTime() {
		return endTime;
	}

	/**
	 * Returns the time this order was completed.
	 * If the order is finished this will be the same as the end time.
	 * @return The time this order was completed.
	 */
	public DateTime getEstimatedEndTime() {
		return estimatedEndTime;
	}
	
	/**
	 * Returns the delay there is on this order.
	 * @return	An integer holding the total delay on this order.
	 */
	public int getDelay() {
		return delay;
	}
	
	/**
	 * Adds a certain amount of delay to this order.
	 * @param amount	The amount of delay that needs to be added to this order.
	 */
	public void addDelay(int amount){
		delay+= amount;
	}
	
	/**
	 * Sets the estimated end time to the given time.
	 * @param time	Time to set the estimated end time to.
	 */
	public void setEstimatedEndTime(DateTime time){
		this.estimatedEndTime = time;
	}
	
	/**
	 * Sets the end time and estimated end time to the given time.
	 * @param time	Time to set the end time to.
	 */
	public void setEndTime(DateTime time){
		this.endTime = time;
		this.estimatedEndTime = endTime;
	}

	/**
	 * Sets the start time to the given time.
	 * @param startTime	Time to set the start time to.
	 */
	public void setStartTime(DateTime startTime) {
		if(this.startTime == null)
			this.startTime = startTime;
	}
	
	/**
	 * Returns the default time it takes to install parts on this order.
	 * @return The default time it takes to install parts on this order.
	 */
	public abstract int getPhaseTime();
	
	/**
	 * Returns the time this order needs to be finished.
	 * @return	The time which is the absolute end time of this order.
	 */
	public abstract DateTime getDeadLine();
	
	/**
	 * Returns a list of tasks that are required to complete this order.
	 * @return	A list containing different tasks which need to be done for this order.
	 */
	public abstract List<Task> getTasks();
	
	/**
	 * Returns a copy of this order
	 * @return	A copy of this order.
	 */
	public abstract Order getRawCopy();
	
	/**
	 * Checks if this order is completed by checking if all its tasks are completed.
	 * @return 	True if this order is completed.
	 * 			False if this order is not completed.
	 */
	public abstract boolean done();
	
	/**
	 * Checks if this object is the same as a given object.
	 * @param	obj	The object against which we want to check equality.
	 * @return	True if this object is the same as a given object
	 * 			False if the given object is null or the classes aren't the same or some fields do not have the same values.
	 */
	@Override
	public boolean equals(Object obj){
		if(obj == null) return false;
		if(this.getClass() != obj.getClass()) return false;
		return equals((Order) obj);
	}
	
	/**
	 * Checks if this order's tasks are the same as a given order's tasks.
	 * @param other	The other order against which we want to check equality.
	 * @return	True if this order's tasks are the same as the given order's tasks.
	 * 			False otherwise.
	 */
	private boolean equals(Order other){
		return this.getTasks().equals(other.getTasks());
	}
	
	/**
	 * Checks if this order's estimated completion time is bigger than a given order's estimated completion time.
	 * @param	o The order against which we want to check the estimated completion time.
	 * @return	-1 if the given order has a bigger estimated completion time than this order's estimated completion time.
	 * 			0 if both are equal
	 * 			+1 if this order's estimated completion time is bigger than the given order's estimated completion time.
	 */
	@Override
	public int compareTo(Order o) {
		if(o == null)
			return -1;
		return this.estimatedEndTime.compareTo(o.estimatedEndTime);
	}
}
