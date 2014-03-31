package logic.car;

import java.util.List;
import logic.workstation.Task;
import org.joda.time.DateTime;


public abstract class Order {
	
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
		if(startTime == null)
			this.startTime = startTime;
	}
	
	/**
	 * Returns the default time it takes to install parts on this order.
	 * @return The default time it takes to install parts on this order.
	 */
	public abstract int getPhaseTime();
	
	public abstract DateTime getDeadLine();
	
	public abstract List<Task> getTasks();
	
	/**
	 * Checks if this order is completed by checking if all its tasks are completed.
	 * @return 	True if this order is completed.
	 * 			False if this order is not completed.
	 */
	public abstract boolean done();
	
	@Override
	public boolean equals(Object obj){
		if(obj == null) return false;
		if(this.getClass() != obj.getClass()) return false;
		return equals((Order) obj);
	}
	
	private boolean equals(Order other){
		return this.getTasks().equals(other.getTasks());
	}
}
