package logic.car;

import java.util.ArrayList;
import java.util.List;

import logic.workstation.Task;

public class CarOrderDetails extends OrderDetails{
	
	
	private CarModel model;
	private ArrayList<Task> tasks;
	
	public CarOrderDetails(CarModel model, List<CarPart> parts){
		this.model = model;
		buildTasks(parts);		
	}
	
	private void buildTasks(List<CarPart> parts){
		this.tasks = new ArrayList<Task>();
		for(CarPart p: parts){
			this.tasks.add(new Task(p));
		}
	}

	@Override
	public List<Task> getPendingTasks() {
		return this.tasks;
	}

	public int getPhaseTime() {
		return model.phaseDuration;
	}

}
