package logic.workstation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;

import logic.car.CarPartType;
import logic.car.Order;

/**
 * Class used to describe a work station of an assembly line.
 * TODO: Maybe change the workstation to pull the tasks form it's order instead of storing locally.
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
		if(canMoveAssemblyLine()){
			if(nextWorkStation!=null)
				nextWorkStation.advanceWorkstations(currentOrder);
			this.setOrder(order);
		}
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
		if(currentOrder == null)
			return null;
		return currentOrder.getRawCopy();
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
	
	protected abstract Workstation getRawCopy();
	
	public void buildWorkstationList(List<Workstation> workstations, int numberOfWorkstations){
		if(numberOfWorkstations > 0){
			workstations.add(this.getRawCopy());
		}
		if(numberOfWorkstations > 1 && nextWorkStation != null){
			nextWorkStation.buildWorkstationList(workstations, numberOfWorkstations-1);
		}
	}
	
	public void buildOrderList(List<Order> orders){
		if(this.currentOrder != null)
			orders.add(this.getCurrentOrder());
		if(nextWorkStation != null)
			nextWorkStation.buildOrderList(orders);
	}
	
	public void buildFutureOrderList(List<Order> orders){
		if(nextWorkStation != null)
			orders.add(this.getCurrentOrder());
			nextWorkStation.buildFutureOrderList(orders);
	}
	
	public DateTime reschedule(List<Integer> prePhaseDurations, int NbOfWorkstations, DateTime currentTime){
		DateTime nextStationEET = currentTime;
		if(nextWorkStation != null){
			if(currentOrder == null)
				prePhaseDurations.add(0);
			else
				prePhaseDurations.add(currentOrder.getPhaseTime());
			nextStationEET = nextWorkStation.reschedule(prePhaseDurations, NbOfWorkstations, currentTime);
		}	
		int maxPre = 0;
		if(currentOrder != null)
			maxPre = currentOrder.getPhaseTime();
		int j = 0;
		for(int i = prePhaseDurations.size()-1; i >=0; i--){
			if(j >= NbOfWorkstations-1)
				break;
			j++;
			if(prePhaseDurations.get(i)>maxPre)
				maxPre = prePhaseDurations.get(i);
		}
		if(prePhaseDurations.size()>0)
			prePhaseDurations.remove(prePhaseDurations.size()-1);
		if(currentOrder != null){
			currentOrder.setEndTime(nextStationEET.plusMinutes(maxPre));
			return currentOrder.getEstimatedEndTime();
		}
		return nextStationEET.plusMinutes(maxPre);
	}
	
	public int countWorkStations(){
		if(nextWorkStation == null)
			return 1;
		return nextWorkStation.countWorkStations()+1;
	}
	
	public DateTime getTotalEstimatedEndTime(){
		if(this.currentOrder != null)
			return this.currentOrder.getEstimatedEndTime();
		if(this.nextWorkStation != null)
			return nextWorkStation.getTotalEstimatedEndTime();
		return null;
	}
	
	public void adjustDelays(int phaseDuration){
		int delay = phaseDuration - getTotalMaxPhaseTime();
		addDelay(delay);
	}
	
	private void addDelay(int delay){
		if(this.currentOrder != null)
			this.currentOrder.addDelay(delay);
		if(this.nextWorkStation != null)
			this.nextWorkStation.addDelay(delay);
	}
	
	private int getTotalMaxPhaseTime(){
		if(this.nextWorkStation == null){
			if(this.currentOrder == null)
				return 0;
			return this.currentOrder.getPhaseTime();
		}
		if(this.currentOrder == null)
			return this.nextWorkStation.getTotalMaxPhaseTime();
		return Math.max(this.currentOrder.getPhaseTime(), this.nextWorkStation.getTotalMaxPhaseTime());
	}
	
	public Order getLastOrder(){
		if(nextWorkStation != null)
			return nextWorkStation.getLastOrder();
		return this.currentOrder;
	}
	
	public boolean allIdle(){
		if(this.currentOrder != null)
			return false;
		if(this.nextWorkStation != null)
			return this.nextWorkStation.allIdle();
		return true;
	}
}
