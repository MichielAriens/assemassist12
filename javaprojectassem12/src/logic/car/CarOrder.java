package logic.car;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import logic.workstation.Task;

public class CarOrder {

	private DateTime startTime;
	private DateTime endTime;
	private DateTime estimatedEndTime;
	private CarSpecification carSpecification;
	private List<Task> tasks = new ArrayList<Task>();

	public CarOrder(CarSpecification carSpecification, DateTime startTime){
		this.carSpecification = carSpecification;
		this.startTime = startTime;
		buildTasks();
	}

	/**
	 * Build a list of tasks to be done. For use on the production line.
	 */
	private void buildTasks() {
		for(CarPart part : this.getCarSpecification().getParts()){
			this.tasks.add(new Task(part));
		}
	}

	public List<Task> getTasks(){
		return this.tasks;
	}

	public DateTime getStartTime() {
		return startTime;
	}

	public DateTime getEndTime() {
		return endTime;
	}

	public DateTime getEstimatedEndTime() {
		return estimatedEndTime;
	}

	public CarSpecification getCarSpecification() {
		return carSpecification;
	}

	public void setEstimatedEndTime(DateTime time){
		this.estimatedEndTime = time;
	}
	
	public void setEndTime(DateTime time){
		this.endTime = time;
	}

	/**
	 * 
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

