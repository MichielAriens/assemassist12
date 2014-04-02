package logic.car;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

import logic.workstation.Task;

public class TaskOrderDetails{
	
	private DateTime deadLine;
	private ArrayList<Task> tasks;
	
	public TaskOrderDetails(CarPart part, DateTime deadLine){
		this.deadLine = deadLine;
		buildTasks(part);
	}
	
	private void buildTasks(CarPart part){
		tasks = new ArrayList<Task>();
		tasks.add(new Task(part));
	}

	public List<Task> getPendingTasks() {
		return tasks;
	}

	public DateTime getDeadLine() {
		return this.deadLine;
	}
	
	public TaskOrderDetails getRawCopy(){
		return new TaskOrderDetails(tasks.get(0).getCarPart(), null);
	}

}
