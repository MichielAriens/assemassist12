package logic.workstation;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;

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
	
	/**
	 * The next workstation in the chain of workstations.
	 */
	private Workstation nextWorkStation;
	
	/**
	 * Set the next workstation in the chain of workstations to the given workstation,
	 * if and only if this workstation's next workstation has not been set yet.
	 * @param next	The next workstation in the chain of workstations.
	 */
	public void setWorkStation(Workstation next){
		if(this.nextWorkStation == null)
			this.nextWorkStation = next;
	}
	
	/**
	 * Checks whether the assembly line can be moved, which is when all pending tasks at each 
	 * workstation in the chain have been completed.
	 * @return	True 	if all pending tasks in the chain have been completed.
	 * 			False 	otherwise.
	 */
	public boolean canMoveAssemblyLine(){
		if(!this.done())
			return false;
		else if(nextWorkStation==null)
			return true;
		else
			return nextWorkStation.canMoveAssemblyLine();
	}
	
	/**
	 * Moves the assembly line forward, if possible, by moving all orders in the chain to the next 
	 * workstation in the chain, and adding the given order to the first workstation in the chain.
	 * @param order	The new order that has to be added to the first workstation of the chain.
	 */
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
	 * Constructs a workstation and initializes the current order and next workstation to null. 
	 * If the current order is null the workstation represents an idle workstation.
	 * If the next workstation is null, this workstation represents the last workstation in the chain of 
	 * workstations.
	 */
	public Workstation(){
		currentOrder = null;
		nextWorkStation = null;
	}
	
	/**
	 * Returns a copy of the order currently being serviced by this workstation.
	 * When the current order is null, the workstation is idle.
	 * @return A copy of the current order.
	 */
	public Order getCurrentOrder(){
		if(currentOrder == null)
			return null;
		return currentOrder.getRawCopy();
	}
	
	/**
	 * Returns whether or not this workstation has been assigned an order to work on.
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
	 * @param order	Any order. Can be completed, semi-completed, or not started.
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
	private boolean isCompatibleTask(Task task){
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
	protected abstract List<CarPartType> getCapabilities();
	
	/**
	 * Lets the chain of workstations perform a task corresponding to the given task identifier, at the
	 * workstation corresponding workstation identifier.
	 * @param taskIdentifier		A string that defines a task that needs to be performed.
	 * @param workstationIdentifier	A string that defines the workstation where the task needs to be performed.
	 * @return	True 	if the specified task has been successfully performed at the specified workstation.
	 * 			False	otherwise. 
	 */
	public boolean doTask(String taskIdentifier, String workstationIdentifier){
		if(this.toString().equals(workstationIdentifier)){
			for(Task t : tasks){
				if(t.toString().equals(taskIdentifier))
					t.perform();
			}
			return true;
		}
		if(this.nextWorkStation != null)
			return this.nextWorkStation.doTask(taskIdentifier, workstationIdentifier);
		return false;
	}
	
	/**
	 * Lets the chain of workstations build a list of task identifiers, that specify all pending task
	 * at the workstation that corresponds with the given workstation identifier.
	 * @param workstationIdentifier	A string that defines the workstation whose tasks are needed.
	 * @return	A list of strings that correspond to the pending tasks of the given workstation identifier.
	 */
	public List<String> getRequiredTaskIdentifiers(String workstationIdentifier){
		if(this.toString().equals(workstationIdentifier)){
			ArrayList<String> returnlist = new ArrayList<String>();
			for(Task t : tasks){
				if(!t.isComplete())
					returnlist.add(t.toString());
			}
			return returnlist;
		}
		if(this.nextWorkStation != null)
			return this.nextWorkStation.getRequiredTaskIdentifiers(workstationIdentifier);
		return null;
	}
	
	
	/**
	 * Fetches the task description of the task corresponding to the given task identifier, at the workstation
	 * corresponding to the given workstation identifier.
	 * @param workstationIdentifier	A string that defines the workstation that has to be checked.
	 * @param taskIdentifier		A string that defines the task whose description is needed.
	 * @return	null	If the task or workstation have been incorrectly specified, or there is no task of that
	 * 					type currently at the specified workstation.	
	 * 			The given task's description otherwise.
	 */
	public String getTaskDescription(String workstationIdentifier, String taskIdentifier){
		if(this.toString().equals(workstationIdentifier)){
			for(Task t : tasks){
				if(t.toString().equals(taskIdentifier))
					return t.getDescription();
			}
		}
		if(this.nextWorkStation != null)
			return this.nextWorkStation.getTaskDescription(workstationIdentifier, taskIdentifier);
		return null;
	}
	
	/**
	 * Lets the chain of workstations build a list of strings, that contains all task identifiers of the
	 * workstation specified by the given workstation identifier, together with their status: 'Pending' or 'Completed'.
	 * @param workstationIdentifier	A string that defines a the workstation that has to be checked.
	 * @return	null	if the workstation was incorrectly specified.
	 * 			A list of strings with all tasks identifiers with their statuses at that workstation otherwise. 
	 */
	public List<String> getTaskStatus(String workstationIdentifier){
		if(this.toString().equals(workstationIdentifier)){
			ArrayList<String> returnlist = new ArrayList<String>();
			for(Task t : tasks){
				if(t.isComplete())
					returnlist.add(t.toString() + ": Completed");
				else
					returnlist.add(t.toString() + ": Pending");
			}
			return returnlist;
		}
		if(this.nextWorkStation != null)
			return this.nextWorkStation.getTaskStatus(workstationIdentifier);
		return null;
	}
	
	/**
	 * Returns a new instance of the type of the implementing classes. 
	 * @return	A copy of this workstation.
	 */
	protected abstract Workstation getRawCopy();
	
	/**
	 * Builds a list of workstations, by adding copies of the workstations in the chain to the back of the 
	 * given list of workstations. The amount of workstations to add is also specified.
	 * @param workstations			The list of workstations, to which the chain has to be added.
	 * @param numberOfWorkstations	The amount of workstations that has to be added to the chain.
	 */
	public void buildWorkstationList(List<Workstation> workstations, int numberOfWorkstations){
		if(numberOfWorkstations > 0){
			workstations.add(this.getRawCopy());
		}
		if(numberOfWorkstations > 1 && nextWorkStation != null){
			nextWorkStation.buildWorkstationList(workstations, numberOfWorkstations-1);
		}
	}
	
	/**
	 * Builds a list of orders, by adding copies all orders that are currently in the chain to the back of 
	 * the given list. Idle workstations don't add their (null) order.
	 * @param orders	The list of orders, to which orders in the chain need to be added.
	 */
	public void buildOrderList(List<Order> orders){
		if(this.currentOrder != null)
			orders.add(this.getCurrentOrder());
		if(nextWorkStation != null)
			nextWorkStation.buildOrderList(orders);
	}
	
	/**
	 * Recalculates the estimated completion times of the orders currently in the chain.
	 * @param prePhaseDurations	A list with the standard phase durations of the orders that come after the order
	 * 							in this workstation. 
	 * @param NbOfWorkstations	The number of workstations in the chain.
	 * @param currentTime		The current time.
	 * @return 	The estimated completion time of the order in this workstation.
	 */
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
	
	/**
	 * Counts the number of workstations in the chain.
	 * @return	The number of workstations in the chain.
	 */
	public int countWorkStations(){
		if(nextWorkStation == null)
			return 1;
		return nextWorkStation.countWorkStations()+1;
	}
	
	/**
	 * Returns the latest estimated end time of all orders in the chain of workstations.
	 * @return	The latest estimated end time of the orders in the chain.
	 */
	public DateTime getTotalEstimatedEndTime(){
		if(this.currentOrder != null)
			return this.currentOrder.getEstimatedEndTime();
		if(this.nextWorkStation != null)
			return nextWorkStation.getTotalEstimatedEndTime();
		return null;
	}
	
	/**
	 * Adjust the delays of orders in the chain of workstations. 
	 * @param phaseDuration	The actual duration of the phase.
	 */
	public void adjustDelays(int phaseDuration){
		int delay = phaseDuration - getTotalMaxPhaseTime();
		addDelay(delay);
	}
	
	/**
	 * Adds the delay to all orders in the chain.
	 * @param delay	The delay that has to be added.
	 */
	private void addDelay(int delay){
		if(this.currentOrder != null)
			this.currentOrder.addDelay(delay);
		if(this.nextWorkStation != null)
			this.nextWorkStation.addDelay(delay);
	}
	
	/**
	 * Returns the biggest standard phase duration of all orders in the chain.
	 * @return	0	if there are no orders in the chain.
	 * 			The biggest standard phase duration of all orders in the chain otherwise.
	 */
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
	
	/**
	 * Returns the order in the last workstation, if all tasks on the order are completed,
	 * so that it can be given to the statistics.
	 * @return	The order in the last workstation.
	 */
	public Order getLastOrder(){
		if(nextWorkStation != null)
			return nextWorkStation.getLastOrder();
		if(this.done())
			return this.currentOrder;
		return null;
	}
	
	/**
	 * Checks if all workstations in the chain are idle.
	 * @return	True	if all workstations in the chain are idle.
	 * 			False	otherwise.
	 */
	public boolean allIdle(){
		if(this.currentOrder != null)
			return false;
		if(this.nextWorkStation != null)
			return this.nextWorkStation.allIdle();
		return true;
	}
}
