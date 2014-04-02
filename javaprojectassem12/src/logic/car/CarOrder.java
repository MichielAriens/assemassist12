package logic.car;

import java.util.List;

import logic.workstation.Task;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class CarOrder extends Order{
	
	private CarOrderDetails details;
	
	public CarOrder(CarOrderDetails details){
		this.details = details;
	}

	@Override
	public DateTime getDeadLine() {
		return null;
	}

	@Override
	public List<Task> getTasks() {
		return this.details.getPendingTasks();
	}

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
		return this.details.getPhaseTime();
	}
	
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
	
	@Override
	public Order getRawCopy(){
		return new CarOrder(details.getRawCopy());
	}

}
