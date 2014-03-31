package logic.car;

import java.util.ArrayList;
import java.util.List;

import logic.workstation.Task;

public class CarOrderDetails{
	
	
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

	public List<Task> getPendingTasks() {
		return this.tasks;
	}

	public int getPhaseTime() {
		return model.phaseDuration;
	}
	
	@Override
	public String toString(){
		String str = this.model.toString() + "; (";
		for(Task task : tasks){
			str += task.getCarPart().toString() + ", ";
		}
		str = str.substring(0, str.length()-2);
		str += ")";
		return str;
	}

}
