package logic.car;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import logic.workstation.Task;

public class CarOrder {

	private Calendar startTime;
	private Calendar endTime;
	private Calendar estimatedEndTime;
	private CarSpecification carSpecification;
	private List<Task> tasks = new ArrayList<Task>();

	public CarOrder(CarSpecification carSpecification, Calendar startTime){
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

	public Calendar getStartTime() {
		return startTime;
	}

	public Calendar getEndTime() {
		return endTime;
	}

	public Calendar getEstimatedEndTime() {
		return estimatedEndTime;
	}

	public CarSpecification getCarSpecification() {
		return carSpecification;
	}

	public void setEstimatedEndTime(Calendar time){
		this.estimatedEndTime = time;
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
}

