package logic.workstation;

import java.util.ArrayList;
import java.util.List;
import logic.car.CarPartType;
import logic.car.Order;

/**
 * Class used to describe a work station of an assembly line.
 */
public abstract class Workstation {
	
	/**
	 * The current order this workstation is working on.
	 */
	private Order currentOrder;
	
	private Workstation nextWorkStation;
	
	public void setWorkStation(Workstation next){
		this.nextWorkStation = next;
	}
	
	public boolean canMoveAssemblyLine(){
		if(!this.done())
			return false;
		else if(nextWorkStation==null)
			return true;
		else
			return nextWorkStation.canMoveAssemblyLine();
	}
	
	public void advanceWorkstations(Order order){
		if(nextWorkStation!=null)
			nextWorkStation.advanceWorkstations(currentOrder);
		this.setOrder(order);
	}
	
	
	/**
	 * A list of tasks that can be performed at this workstation.
	 */
	private List<Task> tasks = new ArrayList<Task>();
	
	/**
	 * Constructs a workstation and initializes the current order to null. 
	 * If the current order is null the workstation represents an idle workstation.
	 */
	public Workstation(){
		currentOrder = null;
	}
	
	/**
	 * Returns the order currently being serviced by this workstation.
	 * When the current order is null, the workstation is idle.
	 * @return The current order.
	 */
	public Order getCurrentOrder(){
		return currentOrder;
	}
	
	/**
	 * Returns whether or not this workstation has been assigned a car order to work on.
	 * @return 	True if the current order is null.
	 * 			False otherwise.
	 */
	public boolean idle(){
		return this.currentOrder == null;
	}
	
	/**
	 * Checks whether all tasks, of the current order which can be performed on this 
	 * workstation, have been completed.
	 * @return 	True if and only if there isn't a task left that this workspace can 
	 * 			complete for the current order.
	 * 			False otherwise.
	 */
	private boolean done(){
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
	 * Sets the given order as the current order. The task list will be cleared and updated
	 * with the compatible task(s) of the new order.
	 * @param order	Any car order. Can be completed, semi-completed, or not started.
	 */
	public void setOrder(Order order){
		if(order == null){
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
	 * Checks whether the given task can be performed on this workstation.
	 * @param task	The task that needs to be checked for compatibility.
	 * @return	True if and only if the task can be performed on this workstation,
	 * 			the task is not null and the task is not already completed.
	 * 			False otherwise.
	 */
	public boolean isCompatibleTask(Task task){
		if(getCapabilities().contains(task.getCarPart().type) && !task.isComplete() && task != null){
			return true;
		}
		return false;
	}

	/**
	 * Get the types of tasks that can be performed on this workstation. This method completely 
	 * defines the capabilities of an implementing class.
	 * @return		A List of CarPartType elements defining the capabilities of 
	 * 				the implementation.
	 */
	public abstract List<CarPartType> getCapabilities();
	
	/**
	 * Executes a given task if the task is compatible with this workstation.
	 * @param task	The task which requires completion.
	 */
	public void doTask(Task task){
		if(this.isCompatibleTask(task))
			task.perform();
	}

	/**
	 * Returns the list of tasks from the current order that can be performed on this workstation.
	 * @return The list of tasks from the current order that can be performed on this workstation.
	 */
	public List<Task> getRequiredTasks() {
		return tasks;
	}
	
	
}
