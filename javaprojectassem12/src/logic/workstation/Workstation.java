package logic.workstation;

import java.util.ArrayList;
import java.util.List;
import java.sql.Time;
import logic.assemblyline.AssemblyLine;
import logic.car.CarOrder;
import logic.car.CarPartType;
import logic.users.Mechanic;




public abstract class Workstation {
	
	private CarOrder currentOrder;
	private AssemblyLine assemblyLine;
	private List<Task> tasks = new ArrayList<Task>();
	public Workstation(){
		currentOrder = null;
	}
	
	/**
	 * Returns the order currently being serviced by this workstation.
	 * @return
	 */
	public CarOrder getCurrentOrder(){
		return currentOrder;
	}
	
	/**
	 * Checks whether all tasks on this workspace have been completed.
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
	
	/**
	 * Sets an order as the active order. The correct tasks are added.
	 * @param order
	 */
	public void setOrder(CarOrder order){
		if(null == order){
			throw new RuntimeException();
		}
		this.currentOrder = order;
		this.tasks = new ArrayList<Task>();
		for(Task task : this.currentOrder.getTasks()){
			if(this.isCompatibleTask(task)){
				this.tasks.add(task);
			}
		}
		
	}
	
	/**
	 * Whecks whether a task can be performed by this workstation
	 * @param task
	 * @return
	 */
	private boolean isCompatibleTask(Task task){
		if(getCapabilities().contains(task.getCarPart().type)){
			return true;
		}
		return false;
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


	public List<Task> getRequiredTasks() {
		return tasks;
	}

}
