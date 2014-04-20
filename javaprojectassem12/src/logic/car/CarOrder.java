package logic.car;

import java.util.List;

import logic.workstation.Task;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * A class representing a car order and all its requirements. 
 */
public class CarOrder extends Order{
	
	/**
	 * A variable holding the car order details. 
	 */
	private CarOrderDetails details;
	
	/**
	 * Initializes the car order details of this class to the given details.
	 * @param details	The car order details which need to be set.
	 */
	public CarOrder(CarOrderDetails details){
		this.details = details;
	}

	/**
	 * Returns a time object which represents the deadline of this car order.
	 * @return	The deadline of this car order.
	 */
	@Override
	public DateTime getDeadLine() {
		return null;
	}

	/**
	 * Returns a list of tasks which are required to complete this car order's details and thus the car itself.
	 * @return	A list of task needed to complete the details of this car order.
	 */
	@Override
	public List<Task> getTasks() {
		return this.details.getPendingTasks();
	}

	/**
	 * returns a boolean holding whether this car order is done or not.
	 * @return	True if all tasks from the details of this car order are done or this object is null.
	 * 			False otherwise
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
	 * Returns the phase duration of this car order's detail.
	 * @return	An integer holding the phase duration of this car order's details.
	 */
	@Override
	public int getPhaseTime() {
		return this.details.getPhaseTime();
	}
	
	/**
	 * Checks if this object is the same as the given object.
	 * @param obj	The object against which we need to check.
	 * @return	True if the given object is the same as this object.
	 * 			False otherwise
	 */
	@Override
	public boolean equals(Object obj){
		return super.equals(obj);
	}
	
	/**
	 * Returns a string representation of this car order.
	 * @return	The estimated end time followed by the car specification.
	 */
	@Override
	public String toString(){
		String str;
		DateTimeFormatter fmt = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm");
		str = fmt.print(super.getEstimatedEndTime());
		return str;
	}
	
	/**
	 * Returns information about this car order. 
	 * The information contains this car order's details and the start, end and est end times.
	 * @return	A string containing the details, the start time, end time and estimated end time.
	 */
	public String getInformation(){
		String str = "   Specifications:   " + details.toString() + "\n";
		DateTimeFormatter fmt = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm");
		str +=       "   Start Time:       " + fmt.print(super.getStartTime()) + "\n";
		if(done())
			str +=   "   End Time:         " + fmt.print(super.getEndTime()) + "\n";
		else
			str +=   "   Est. End Time:    " + fmt.print(super.getEstimatedEndTime()) + "\n";
		return str;
	}
	
	/**
	 * Makes a new car order and sets its detail's field to a copy of this car order's details.
	 * @return	A new car order with a copy of its details in it.
	 */
	@Override
	public Order getRawCopy(){
		return new CarOrder(details.getRawCopy());
	}

}
