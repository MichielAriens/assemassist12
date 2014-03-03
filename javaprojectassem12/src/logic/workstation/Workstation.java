package logic.workstation;

import java.util.List;





import logic.assemblyline.AssemblyLine;
import logic.car.CarOrder;
import logic.car.CarPartType;
import logic.users.Mechanic;




public abstract class Workstation {
	
	private CarOrder currentOrder;
	private AssemblyLine assemblyLine;
	private List<Task> tasks;
	
	public Workstation(){
		currentOrder = null;
	}
	
	public CarOrder getCurrentOrder(){
		return currentOrder;
	}
	
	public boolean done(){
		return false;
	}
	
	public void setOrder(CarOrder order){
		
	}
	
	
	/**
	 * Get the types of tasks this workstation can perform. 
	 * @return
	 */
	abstract List<CarPartType> getCapabilities();
	
	
	public void doTask(Task task, Time endTime, Mechanic mechanic){
		if(null == task || null == mechanic || null == endTime){
			return;
		}
		task.perform(endTime, mechanic);
	}


	public List<CarPartType> getRequiredTasks() {
		
		for this.currentOrder.getTasks();
	}

}
