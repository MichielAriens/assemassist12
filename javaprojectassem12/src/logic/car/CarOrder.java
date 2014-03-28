package logic.car;

import java.util.List;

import logic.workstation.Task;

import org.joda.time.DateTime;

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


}
