package logic.workstation;

import java.util.ArrayList;
import java.util.List;
import logic.car.CarOrder;
import logic.car.CarPartType;




public abstract class Workstation {
	/**
	 * The current order this workstation is working on.
	 */
	private CarOrder currentOrder;
	/**
	 * A list of tasks this workstation can fulfil.
	 */
	private List<Task> tasks = new ArrayList<Task>();
	/**
	 * Constructs a workstation and initializes the currentorder to null. This represents an idle Workstation
	 * 
	 */
	public Workstation(){
		currentOrder = null;
	}
	
	/**
	 * Returns the order currently being serviced by this workstation.
	 * @return the currentorder, null if there is none (the workstation is idle)
	 */
	public CarOrder getCurrentOrder(){
		return currentOrder;
	}
	
	/**
	 * Returns whether or not this workstation has been assigned an order to work on.
	 * @return	currentorder == null
	 */
	public boolean idle(){
		return this.currentOrder == null;
	}
	
	/**
	 * Checks whether all tasks that the workspace can do on the current order have been completed.
	 * @return 	True: 	if and only if there exists no task that this workspace can complete for the current order.
	 * 			False:	otherwise
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
	 * Sets an order as the active order. 
	 * @param 	order: Any car order. Can be completed, semi-completed, or not started.
	 */
	public void setOrder(CarOrder order){
		if(null == order){
			this.currentOrder=null;
			this.tasks.clear();
		}else{
			this.currentOrder = order;
			this.tasks = new ArrayList<Task>();
			for(Task task : this.currentOrder.getTasks()){
				if(this.isCompatibleTask(task)){
					this.tasks.add(task);
				}
			}
		}
	}
	
	/**
	 * Checks whether a task can be performed by this workstation
	 * @param task		A task
	 * @return			True:	If and only if 	the task can be performed by this workstation, as described by getCapablilities()
	 * 											and the task is not already completed.
	 * 					False:	Otherwise	
	 */
	private boolean isCompatibleTask(Task task){
		if(getCapabilities().contains(task.getCarPart().type) && !task.isComplete()){
			return true;
		}
		return false;
	}
	
	
	/**
	 * Get the types of tasks this workstation can perform. This methods completely defines the capabilities of an implementing class.
	 * @return		A List of CarPartType elements defining the capabilities of the implementation.
	 */
	public abstract List<CarPartType> getCapabilities();
	
	/**
	 * Execute a given task. 
	 * 		Does nothing if any of the parameters are null or if this worktation is not compatible with the given task.
	 * 		Otherwise the task will be performed.
	 * @param task		The task which requires completetion. Can be a compatible or non compatible
	 * @param endTime	<-- will change
	 * @param mechanic	The mechanic doing the task.
	 */
	public void doTask(Task task){
		if(null == task){
			return;
		}
		if(this.isCompatibleTask(task))
			task.perform();
	}

	/**
	 * Returns a list of tasks from the current order that can be completed by this workstation.
	 * @return
	 */
	public List<Task> getRequiredTasks() {
		return tasks;
	}

}
