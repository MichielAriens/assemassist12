package logic.car;

import java.util.List;
import logic.workstation.Task;
import org.joda.time.DateTime;


public abstract class Order {
	
	public abstract List<Task> getTasks();
	
	public abstract DateTime getStartTime();
	
	public abstract DateTime getEndTime();
	
	public abstract DateTime getEstimatedEndTime();
	
	public abstract void setStartTime(DateTime d);
	
	public abstract void setEndTime(DateTime d);
	
	public abstract void setEstimatedEndTime(DateTime d);
	
	public abstract boolean done();


}
