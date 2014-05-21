package logic.car;

import java.util.List;

import logic.workstation.Task;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


/**
 * A class representing a vehicle order and all its requirements. 
 */
public class VehicleOrder extends Order{
	
	/**
	 * A variable holding the vehicle order details. 
	 */
	private VehicleOrderDetails details;
	
	/**
	 * Initializes the vehicle order details of this class to the given details.
	 * @param details	The vehicle order details which need to be set.
	 */
	public VehicleOrder(VehicleOrderDetails details){
		this.details = details;
	}

	/**
	 * Returns a time object which represents the deadline of this vehicle order.
	 * @return	The deadline of this vehicle order, which is null.
	 */
	@Override
	public DateTime getDeadLine() {
		return null;
	}

	/**
	 * Returns a list of tasks which are required to complete this vehicle order's details and thus the vehicle itself.
	 * @return	A list of task needed to complete the details of this vehicle order.
	 */
	@Override
	public List<Task> getTasks() {
		return this.details.getPendingTasks();
	}

	/**
	 * returns a boolean holding whether this vehicle order is done or not.
	 * @return	True if all tasks from the details of this vehicle order are done or this object is null.
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
	 * Returns a string representation of this vehicle order.
	 * @return	The estimated end time in the form of a string.
	 */
	@Override
	public String toString(){
		String str;
		DateTimeFormatter fmt = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm");
		str = fmt.print(super.getEstimatedEndTime());
		return str;
	}
	
	/**
	 * Makes a new vehicle order and sets its detail's field to a copy of this vehicle order's details.
	 * @return	A new vehicle order with a copy of its details in it.
	 */
	@Override
	public Order getRawCopy(){
		return new VehicleOrder(details.getRawCopy());
	}
	
	/**
	 * Returns information about this vehicle order. 
	 * The information contains this vehicle order's details and the start, end and estimated end times.
	 * @return	A string containing the details, the start time, end time and estimated end time.
	 */
	@Override
	public String getExtraInformation() {
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
	 * Returns the model of this vehicle order.
	 * @return the model of this vehicle order.
	 */
	@Override
	public VehicleModel getModel() {
		return this.details.getModel();
	}
}
