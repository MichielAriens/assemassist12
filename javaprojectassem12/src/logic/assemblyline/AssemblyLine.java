package logic.assemblyline;
import interfaces.Printable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import logic.order.Order;
import logic.order.VehicleModel;
import logic.workstation.Task;
import logic.workstation.Workstation;
import logic.workstation.WorkstationChainBuilder;

import org.joda.time.DateTime;
import org.joda.time.Days;

/**
 * Class handling an assembly line of a car manufacturing company.
 */

public class AssemblyLine implements Printable<AssemblyLine> {


	/**
	 * The operational status of this assembly line.
	 */
	private OperationalStatus status = OperationalStatus.OPERATIONAL;
	
	/**
	 * The time it took for this assembly line to complete the longest during task.
	 */
	private int cycleTime=0;
	
	/**
	 * A boolean holding whether this assembly line can go to the next day or not.
	 */
	private boolean newday = false;
	
	/**
	 * The first workstation in a line of workstations.
	 */
	private Workstation firstWorkStation;

	/**
	 * Integer holding the number of work stations.
	 */
	private int numberOfWorkStations;

	/**
	 * A list holding the pending orders waiting to be processed on this assembly line.
	 */
	private LinkedList<Order> queue;

	/**
	 * A dateTime object holding the phase start time of this assembly line. 
	 */
	private DateTime cycleStartTime;

	/**
	 * A variable holding the statistics of this assembly line.
	 */
	private StatisticsAssemblyLine stats;

	/**
	 * Returns the phase start time of this assembly line.
	 * @return	The phase start time of this assembly line.
	 */
	public DateTime getcycleStartTime() {
		return cycleStartTime;
	}

	/**
	 * A variable containing this assembly line's schedule, which is used for scheduling orders.
	 */
	private Schedule schedule;

	/**
	 * A variable holding the different models that can be processed by this assembly line
	 */
	private Collection<VehicleModel> capabilities;
	
	/**
	 * The name of this assembly line.
	 */
	private String name;

	/**
	 *Initializes this assembly line with the given possible models, the workstation builder, the cycle start time, and the name of this assembly line.
	 *Also makes the queue for orders and the statistics.
	 */
	public AssemblyLine(Collection<VehicleModel> capabilities, WorkstationChainBuilder builder, DateTime startTime, String name){
		this.name = name;
		this.capabilities = capabilities;
		queue= new LinkedList<Order>();
		schedule = new Schedule();
		this.firstWorkStation = builder.getResult();
		this.numberOfWorkStations = firstWorkStation.countWorkStations();
		this.cycleStartTime = startTime;
		stats = new StatisticsAssemblyLine(this.getStringRepresentation());
	}

	/**
	 * Checks if this assembly line can process the given order's model.
	 * @param order	The order which needs to be checked.
	 * @return	True if this model can be processed by this assembly line.
	 * 			False otherwise
	 */
	public boolean accepts(Order order){
		VehicleModel model = order.getModel();
		if(model == null){
			return true;
		}
		if(this.capabilities.contains(model)){
			return true;
		}return false;
	}
	
	/**
	 * Checks if this assembly line is ready for the day.
	 * @return	True if this assembly line can go to the next day.
	 */
	protected boolean readyForNextDay(){
		return newday;
	}
	
	/**
	 * If this assembly line is ready for the next day and the cycle start time is at the beginning of the shift, then sets the ready for next day to false.
	 */
	public void setNewDay(){
		if(newday == true && cycleStartTime.getHourOfDay()==schedule.shiftBeginHour){
			newday = false;
		}
	}
	
	/**
	 * Returns the longest time taken by this assembly line to complete its cycle.
	 * @return	The longest time taken by a task in this phase.
	 */
	public int getCycleTime(){
		return this.cycleTime;
	}
	
	/**
	 * Gets the end of the current phase (= time of the begin of the next cycle).
	 * @return The end time of the cycle in this assembly line.
	 */
	public DateTime getCycleEnd(){
		return this.getcycleStartTime().plusMinutes(cycleTime);
	}
	
	/**
	 * Change the status of this assembly line to the given status and returns the queue to be processed on another assembly line.
	 * @param status	The new status of this assembly line.
	 * @return	An empty list if the given status equals operational or the queue of this assembly line is empty.
	 * 			A copy of the queue of orders in this assembly line if the given status is not operational. 
	 */
	protected List<Order> changeStatus(OperationalStatus status){
		this.status=status;
		LinkedList<Order> returnList = new LinkedList<Order>();
		if(status != OperationalStatus.OPERATIONAL){
			returnList = schedule.makeCopyOfQueue();
			queue.clear();
		}
		if((status == OperationalStatus.PREMAINTENANCE && firstWorkStation.allIdle()) || status == OperationalStatus.MAINTENANCE){
			this.status = OperationalStatus.MAINTENANCE;
			cycleTime = this.status.getTime();
		}
		return returnList;
			
	}
	
	/**
	 * Repairs this assembly line if the status was broken. Sets this assembly line's cycle start time to the given realTime.
	 * @param realTime	The current time of the system
	 */
	protected void fix(DateTime realTime){
		if(OperationalStatus.BROKEN==status){
			cycleTime= calculateBrokenTime(realTime);
			this.cycleStartTime =realTime;
			this.changeStatus(OperationalStatus.OPERATIONAL);
			schedule.reschedule();
		}
	}
	
	/**
	 * If the manager decides that maintenance should not continue, then the assembly line scheduler will call this method.
	 * This method should only be called to abort maintenance. The cycle start time is set to the given time of the system.
	 * And the cycle time is set to 0.
	 * @param realTime	The current time of the system.
	 */
	protected void abortMaintenance(DateTime realTime){
		if(status== OperationalStatus.MAINTENANCE){
			changeStatus(OperationalStatus.OPERATIONAL);
			cycleStartTime = realTime;
			cycleTime=0;
		}
	}
	
	/**
	 * Calculates the new cycle time of this assembly line, based on the time this assembly line was broken. 
	 * @param realTime	The current time of the system
	 * @return	0 if the time broken was longer than the previous cycle time.
	 * 			otherwise the minutes remaining to complete the cycle.
	 */
	private int calculateBrokenTime(DateTime realTime){
		if(Days.daysBetween(cycleStartTime, realTime).getDays() == 0 || (Days.daysBetween(cycleStartTime, realTime).getDays() == 0 && cycleStartTime.getMinuteOfDay()+cycleTime <= realTime.getMinuteOfDay())){
			return 0;
		}
		return cycleTime - (realTime.getMinuteOfDay() - cycleStartTime.getMinuteOfDay());
	}

	/**
	 * Tries to move the assembly line if all workstations are done. Checks the status of this assembly line 
	 * and sets the cycletime, newday and cyclestarttime accordingly. 
	 * 
	 * Note: If you use this class stand-allone (without an assemblyline scheduler) you can call: assemblyLine.moveAssemblyLine(assemblyLine.getCycleEnd());
	 * to effect to move without external time effects.
	 * @param realTime	The current time of the system and new cycle start time if this assembly line can be moved.
	 * @return	True if the assembly line can be moved
	 * 			False otherwise.
	 */
	 public boolean moveAssemblyLine(DateTime realTime){
		if(tryMoveAssemblyLine()){
			if(status== OperationalStatus.MAINTENANCE){
				changeStatus(OperationalStatus.OPERATIONAL);
				cycleTime=0;
			}
			schedule.moveAndReschedule(cycleTime);
			if(newday == false && cycleTime==0)
				cycleStartTime = realTime;
			cycleTime = 0;
			
			if(status==OperationalStatus.PREMAINTENANCE && firstWorkStation.allIdle())
				changeStatus(OperationalStatus.MAINTENANCE);
			return true;
		}
		else{
			return false;
		}
	}
	 
	/**
	 * Sets the cycle start time of this assembly line.
	 * @param time The new cycle start time this assembly line needs to have.
	 */
	protected void setCycleStartTime(DateTime time){
		this.cycleStartTime = time;
		this.cycleTime = 0;
	}

	/**
	 * Returns a class containing the representation of the statistics of this assembly line.
	 * @return	A class containing the representation of the statistics.
	 */
	public StatisticsAssemblyLine getStatistics(){
		return stats;
	}

	/**
	 * Completes the task corresponding to the given task.
	 * @param task	A representation of the task that needs to be completed.
	 * @param timeTaken	The time it took for the given task to be finished.
	 * @return	True if the task is completed successfully.
	 * 			False the task could not be completed.
	 */
	public boolean doTask(Printable<Task> task, int timeTaken){
		if(status == OperationalStatus.BROKEN)
			return false;
		boolean work = firstWorkStation.doTask(task, timeTaken);
		if(firstWorkStation.canMoveAssemblyLine()){
			cycleTime = firstWorkStation.getMaxElapsedTime();
		}
		return work;
	}

	/**
	 * Returns a list of pending tasks at a given workstation.
	 * @param station	The representation of the workstation for which the pending tasks are needed.
	 * @return	A list of tasks that are pending at the given workstation.
	 */
	public List<Printable<Task>> getRequiredTasks(Printable<Workstation> station){
		return this.firstWorkStation.getRequiredTasks(station);
	}

	/**
	 * Returns a list of all tasks at a given workstation.
	 * @param station	The representation of the workstation for which the tasks are needed.
	 * @return	A list of tasks at the given workstation.
	 */
	public List<Printable<Task>> getAllTasks(Printable<Workstation> station){
		return this.firstWorkStation.getAllTasks(station);
	}

	/**
	 * Returns true if the assembly line can be moved, false otherwise.
	 * @return	True if the assembly line and workstations can be moved.
	 * 			false otherwise.
	 */
	public boolean tryMoveAssemblyLine(){
		return firstWorkStation.canMoveAssemblyLine();
			
	}
	
	/**
	 * Checks if all the workstations are empty.
	 * @return	True if all workstations are empty.
	 * 			False otherwise.
	 */
	public boolean empty(){
		return firstWorkStation.allIdle();
	}
	
	/**
	 * Checks if this assembly line is ready to move.
	 * @return	True if this assembly line is done with the orders in the workstations
	 * 			False otherwise.
	 */
	public boolean ready(){
		return this.tryMoveAssemblyLine();
	}

	/**
	 * Returns a list of the current strategies followed by the available scheduling strategies.
	 * @return a list of the current strategies followed by the available scheduling strategies.
	 */
	public List<Printable<SchedulingStrategy>> getStrategies(){
		return schedule.getStrategies();
	}

	/**
	 * Returns a list of orders which qualify for batch processing strategy.
	 * @return	A list of orders which qualify for batch processing strategy.
	 */
	public List<Order> getBachList(){
		return schedule.getBatchList();
	}

	/**
	 * Asks a list of available workstations.
	 * @return	The list containing representations of the workstations.
	 */
	public List<Printable<Workstation>> getWorkStations() {
		LinkedList<Printable<Workstation>> workStations = new LinkedList<>();
		firstWorkStation.buildWorkstationList(workStations,numberOfWorkStations);
		return workStations;
	}

	/**
	 * Calls the schedule and asks to schedule the given car order.
	 * The order will only be scheduled if it is not null and this assembly line can actually schedule this order.
	 * @param order The car order to be scheduled.
	 * @return		True if the order could be scheduled.
	 * 				False otherwise.
	 */
	public boolean addOrder(Order order){
		if(order != null && this.accepts(order)){
			schedule.scheduleOrder(order);
			return true;
		}
		return false;
	}

	/**
	 * Returns an estimate for an order if scheduled on this line without mutation of either the order or this line.
	 * @param order	The order to schedule.
	 * @return		An estimated assembly time or null if the order cannot be scheduled on this line.
	 */
	public DateTime getEstimate(Order order,DateTime realTime) {		
		return schedule.getEstimate(order, realTime).plusMillis(getBias());
	}

	/**
	 * Changes the strategy if possible. Doesn't change anything if the manager wants to change to the currently used strategy. 
	 * @param order Null if the manager wants to change to FIFO strategy.
	 * 				The example order used in batch processing otherwise.
	 */
	public void changeStrategy(Order order){
		schedule.changeStrategy(order);
	}

	/**
	 * Returns true if the given phase duration does not cause the day to change to 
	 * the next day after 6 am, false otherwise.
	 * @param phaseDuration	The phase duration that needs to be checked.
	 * @return	True if the given phase duration is allowed.
	 * 			False otherwise.
	 */
	public boolean checkPhaseDuration(int phaseDuration){
		int difference = 0;
		if(firstWorkStation.getTotalEstimatedEndTime() != null)
			difference = schedule.getTimeDifference(firstWorkStation.getTotalEstimatedEndTime(), cycleStartTime);
		int diff= schedule.shiftBeginHour*60 - difference - 1;
		int max = 24*60 - cycleStartTime.getMinuteOfDay()+diff;
		if(max>= phaseDuration)
			return true;
		return false;
	}
	
	/**
	 * Returns the name of this assembly line.
	 * @return The name of this assembly line.
	 */
	@Override
	public String toString(){
		return this.name;
	}

	/**
	 * Returns the name in the form of a string of this assembly line.
	 * @return The name of this assembly line.
	 */
	@Override
	public String getStringRepresentation() {
		return this.toString();
	}

	/**
	 * Returns additional information about this assembly line. Additional information consists of the possible vehicle models that can be processed in this assembly line.
	 * @return Extra information in the form of a string. 
	 */
	@Override
	public String getExtraInformation() {
		String extraInfo = "";
		extraInfo += "Possible models: \n";
		for(VehicleModel model : capabilities){
			extraInfo += "   - " + model.toString() +"\n";
		}
		return extraInfo;
	}

	/**
	 * Returns the status of this assembly line.
	 * @return The status of this assembly line in the form of a string.
	 */
	@Override
	public String getStatus() {
		return status.toString();
	}
	

	public int getQueueLength(){
		return this.queue.size();
	}
	
	/**
	 * Returns the status of this assembly line.
	 * @return The operational status of this assembly line.
	 */
	public OperationalStatus getOperationalStatus() {
		return this.status;
	}
	
	/**
	 * A small bias to induce a absolute ordering between assemblylines.
	 * @return		a small bias.
	 */
	private int getBias(){
		return this.capabilities.size() + this.numberOfWorkStations;
	}

	/**
	 *A class made to reschedule the assembly line and workstations. Changes the different times from orders appropriately.
	 */
	class Schedule {

		/**
		 * The currently used strategy.
		 */
		SchedulingStrategy currentStrategy; 

		/**
		 * A list containing the different strategies used by the system.
		 */
		LinkedList<SchedulingStrategy> stratList = new LinkedList<SchedulingStrategy>();


		/**
		 * A constructor which initializes the used strategies and sets the current strategy to FIFO strategy.
		 */
		private Schedule(){
			stratList.add(new FifoStrategy());
			stratList.add(new BatchSpecificationStrategy());
			currentStrategy = stratList.getFirst();
		}

		/**
		 * The begin time of the shift, represented in hours.
		 */
		int shiftBeginHour = 6;

		/**
		 * The end time of the shift, represented in hours.
		 */
		int shiftEndHour = 22;

		/**
		 * A variable holding the overtime made by the mechanics, represented in minutes.
		 */
		int overTime = 0;

		/**
		 * Calculates the overtime made at the end of the day. It is possible that the day ends early,
		 * in that case the overtime is set to 0.
		 */
		private void calculateOverTime() {
			if(cycleStartTime.getHourOfDay()<shiftBeginHour){
				overTime = (24-shiftEndHour) * 60 + cycleStartTime.getMinuteOfDay();
			}else{
				setOverTime((cycleStartTime.getMinuteOfDay()-(shiftEndHour*60)));
			}
		}

		/**
		 * Updates the current time and sets the end time of the order in the last workstation if there is one.
		 * Also sets the next day and overtime if necessary and updates the statistics. 
		 * Then checks if the strategy needs to change back to FIFO.
		 * Reschedules all orders both in the queue and workstations.
		 * @param phaseDuration	The amount which represents the longest time worked on a certain task.
		 */
		private void moveAndReschedule(int phaseDuration) {

			firstWorkStation.adjustDelays(phaseDuration);
			cycleStartTime = cycleStartTime.plusMinutes(phaseDuration);
			Integer delayLastOrder= firstWorkStation.getDelayLastOrder();
			if(delayLastOrder != null){
				stats.finishedCarOrder(delayLastOrder, cycleStartTime);
			}
			firstWorkStation.advanceOrders(null, cycleStartTime);
			if(checkEndOfDay()){
				calculateOverTime();
				setNextDay();
				newday = true;
			}

			checkStrategy();
			reschedule();
		}
		
		/**
		 * Checks if the system can change to FIFO strategy if the current strategy is batch specification and
		 * if there are no more orders which qualify for batch processing. 
		 */
		private void checkStrategy(){
			LinkedList<Order> listo = new LinkedList<Order>();
			listo = makeCopyOfQueue();
			firstWorkStation.buildOrderList(listo);
			if(currentStrategy instanceof BatchSpecificationStrategy){
				for(Order nexto : listo){
					if(nexto.equals(currentStrategy.example))
						return;
				}
			}
			currentStrategy = stratList.getFirst();
		}



		/**
		 * Returns the difference in minutes between two given times.
		 * @param estimate	The time for which we need the difference.
		 * @param current	The current time which is a reference point.
		 * @return	An integer containing the difference in minutes between the two given times.
		 */
		private int getTimeDifference(DateTime estimate, DateTime current){
			if(estimate.getMinuteOfDay()>current.getMinuteOfDay()){
				return estimate.getMinuteOfDay()-current.getMinuteOfDay();
			}else{
				return estimate.getMinuteOfDay()+ 24*60 -current.getMinuteOfDay();
			}
		}

		/**
		 * Tries to add an order to the first workstation if the first workstation does not 
		 * have an order and the queue is not empty. Afterwards reschedules the workstations 
		 * and the queue.
		 */
		private void reschedule(){
			if(firstWorkStation.getCurrentOrder()==null && !queue.isEmpty()){
				addOrderToFirstWorkstation();
			}
			DateTime workstationEET = firstWorkStation.reschedule(getPhaseDurations(), numberOfWorkStations, cycleStartTime, false, true);
			rescheduleQueue(workstationEET);
		}


		/**
		 * Builds a list of a list of phase durations from the orders in the queue. The length of the returned list is either
		 * the amount of workstations or the number of orders in the queue, if there are less orders than workstations.
		 * @return A list of a list of integers containing the phase durations. 
		 */
		private List<List<Integer>> getPhaseDurations(){
			ArrayList<List<Integer>> prePhaseDurations = new ArrayList<>();
			int j = 0;
			for(int i = 0; i < queue.size(); i++){
				if(j >= numberOfWorkStations-1)
					break;
				j++;
				ArrayList<Integer> phases = new ArrayList<>();
				firstWorkStation.buildEstimPhaseList(phases, queue.get(i));
				prePhaseDurations.add(phases);
			}
			return prePhaseDurations;
		}

		/**
		 * Given the start time, calculates the estimated end times of the orders in the 
		 * pending queue. Every order calculates its own estimated end time using a number 
		 * of next orders that is, at most, equal to the amount of workstations minus one.
		 * @param startTime	The time used for scheduling the queue.
		 */
		private void rescheduleQueue(DateTime startTime){
			ArrayList<Integer> phases = new ArrayList<>();
			for(int i = 0; i < queue.size(); i++){
				phases.clear();
				int count = numberOfWorkStations-1;
				firstWorkStation.buildEstimPhaseList(phases, queue.get(i));
				int maxPhase = phases.get(count);
				for(int j = i+1; j < queue.size(); j++){
					if(count <= 0)
						break;
					count--;
					phases.clear();
					firstWorkStation.buildEstimPhaseList(phases, queue.get(j));
					if(phases.get(count) > maxPhase)
						maxPhase = phases.get(count);
				}
				startTime = startTime.plusMinutes(maxPhase+status.getTime());
				if(startTime.getHourOfDay()<shiftBeginHour || startTime.getMinuteOfDay()>=shiftEndHour*60-overTime)
					startTime = getEstimatedTime(startTime, queue.get(i));
				queue.get(i).setEstimatedEndTime(startTime);
			}
		}

		/**
		 * Adds an order to the first workstation if it can still be scheduled today. 
		 * It calculates the estimated end time to see if the order can be scheduled today 
		 * but the end time is not set even if the order can be scheduled today.
		 */
		private void addOrderToFirstWorkstation(){
			Order order = queue.getFirst();
			DateTime estimatedEndTime;
			estimatedEndTime = firstOrderEstimate(order);
			if(estimatedEndTime.getMinuteOfDay()<shiftEndHour*60-overTime && estimatedEndTime.getHourOfDay()>=shiftBeginHour && status == OperationalStatus.OPERATIONAL){
				firstWorkStation.setOrder(queue.pop());
			}
		}

		/**
		 * If the hour of the current time is after the hour of the beginning of the shift, 
		 * the day of the current time is set to the next day, otherwise the day of the current 
		 * time remains the same. Afterwards, sets the current hour to the beginning of the shift. 
		 */
		private void setNextDay() {
			if(cycleStartTime.getHourOfDay()<shiftBeginHour)
				cycleStartTime = new DateTime(cycleStartTime.getYear(),cycleStartTime.getMonthOfYear(),cycleStartTime.getDayOfMonth(),shiftBeginHour,0);
			else{
				cycleStartTime = cycleStartTime.plusDays(1);
				cycleStartTime = new DateTime(cycleStartTime.getYear(),cycleStartTime.getMonthOfYear(),cycleStartTime.getDayOfMonth(),shiftBeginHour,0);
			}

			stats.setNextDay();
		}

		/**
		 * Sets the overTime to the given overtime if it is higher than 0. Else sets the overtime to 0. 
		 * @param overtime	The overtime made during this day.
		 */
		private void setOverTime(int overtime) {
			if(overtime<=0)
				overTime = 0;
			else
				overTime=overtime;
		}

		/**
		 * Returns true if it is the end of the working day, false otherwise.
		 * @return 	True if all workstations are done and it is later than the end hour of the shift 
		 * 		  	minus the overtime or earlier than the begin hour of the shift.
		 * 			False otherwise.
		 */
		private boolean checkEndOfDay() {
			boolean doneWorking = firstWorkStation.allIdle();
			if(!doneWorking)
				return false;
			int assemblyTime = 150;
			if(!queue.isEmpty()){
				assemblyTime = getEstimatedAssemblyTime(queue.getFirst());
			}
			if(cycleStartTime.getMinuteOfDay()>=(shiftEndHour * 60-overTime - assemblyTime) || cycleStartTime.getMinuteOfDay()<shiftBeginHour*60){
				return true;
			}
			return false;
		}
		
		/**
		 * Calculates the total assembly time for a given order in the workstations.
		 * @param order	The order for which we want the total assembly time.
		 * @return	The assembly time of the given order.
		 * 			0 if the given order is null.
		 */
		private int getEstimatedAssemblyTime(Order order){
			if(order == null)
				return 0;
			ArrayList<Integer> phases = new ArrayList<>();
			firstWorkStation.buildEstimPhaseList(phases, order);
			int assemblyTime = 0;
			for(Integer i : phases){
				assemblyTime += i;
			}
			return assemblyTime;
		}

		/**
		 * If the days between the given time and the cycle start time of this assembly line differ, then it checks 
		 * if there is time for a new order without considering the overtime. Else checks if there is time
		 * for a new order considering the overtime. 
		 * @param time	The time to check against thcycle start time of this assembly line.
		 * @return True if there is time for a new order on this day, false otherwise.
		 */
		private boolean timeForNewOrder(DateTime time) {
			if(Days.daysBetween(time, cycleStartTime).getDays()==0){
				return time.getMinuteOfDay()<(shiftEndHour*60-overTime) && time.getMinuteOfDay()>=shiftBeginHour*60;
			}else{
				return time.getMinuteOfDay()<(shiftEndHour*60) && time.getMinuteOfDay()>=shiftBeginHour*60;
			}
		}

		/**
		 * Makes a copy of the queue containing the pending orders.
		 * @return A list which is a copy of the queue containing the pending car orders.
		 * 		   An empty list if there are no pending orders.
		 */
		private LinkedList<Order> makeCopyOfQueue() {
			LinkedList<Order> returnList = new LinkedList<Order>();
			for(Order next:queue){
				returnList.add(next);
			}
			return returnList;
		}

		/**
		 * Uses the current strategy to add the order and reschedule the whole queue and workstations.
		 * @param order	The order that needs to be scheduled.
		 */
		private void scheduleOrder(Order order){
			currentStrategy.addOrder(order, queue);
			reschedule();
		}

		/**
		 * Changes the strategy to FIFO if the given order is null. Otherwise, changes to 
		 * batch processing and uses the given order as an example order to reschedule the
		 * queue. Afterwards it reschedules the queue and workstations.
		 * @param order The possible example order used by batch processing or null if 
		 * 				the strategy needs to be changed to the FIFO strategy.
		 */
		private void changeStrategy(Order order){
			if(order==null){
				currentStrategy = stratList.getFirst();
			}else{
				currentStrategy = stratList.getLast();
				currentStrategy.setExample(order);
			}
			LinkedList<Order> copy = makeCopyOfQueue();
			queue.clear();
			currentStrategy.refactorQueue(queue,copy);
			reschedule();
		}

		/**
		 * Returns the right estimated end time from the given possibly wrong estimated end time.
		 * @param estimatedEndTime The estimated end time that needs to be scheduled.
		 * @param order The order for which the estimated end time possibly needs to change.
		 * @return	The renewed estimated end time if it was scheduled wrong before. 
		 */
		private DateTime getEstimatedTime(DateTime estimatedEndTime, Order order) {
			if(!timeForNewOrder(estimatedEndTime)){
				if(estimatedEndTime.getHourOfDay()<=shiftBeginHour){
					return new DateTime(estimatedEndTime.getYear(),estimatedEndTime.getMonthOfYear(),estimatedEndTime.getDayOfMonth(),shiftBeginHour,0).plusMinutes(getEstimatedAssemblyTime(order));

				}else{
					estimatedEndTime =  new DateTime(estimatedEndTime.getYear(),estimatedEndTime.getMonthOfYear(),estimatedEndTime.getDayOfMonth(),shiftBeginHour,0).plusMinutes(getEstimatedAssemblyTime(order));
					return estimatedEndTime.plusDays(1);
				}
			}else{
				return estimatedEndTime;
			}
		}

		/**
		 * Returns a list of the current strategies followed by the available scheduling strategies.
		 * @return a list of the current strategies followed by the available scheduling strategies.
		 */
		private List<Printable<SchedulingStrategy>> getStrategies(){
			LinkedList<Printable<SchedulingStrategy>> returnList = new LinkedList<>();
			returnList.add(currentStrategy.getRawCopy());
			for(SchedulingStrategy next : stratList){
				returnList.add(next.getRawCopy());
			}
			return returnList;
		}

		/**
		 * Checks if there are orders in the workstations and the queue for which batch 
		 * processing can be applied. It then returns a list of those.
		 * @return A list of orders for which batch processing can be used.
		 */
		private List<Order> getBatchList(){
			LinkedList<Order> returnList = new LinkedList<Order>();
			LinkedList<Order> allOrdersList = new LinkedList<Order>();
			firstWorkStation.buildOrderList(allOrdersList);
			for(Order next: queue){
				allOrdersList.add(next.getRawCopy());
			}
			LinkedList<Order> allOrdersList2 = new LinkedList<Order>(allOrdersList);
			for(Order order: allOrdersList){
				int count = 0;
				for(Order next : allOrdersList2){
					if(order.equals(next))
						count++;
				}
				if(count>=3 && !returnList.contains(order))
					returnList.add(order);
				allOrdersList2.removeFirst();
			}
			return returnList;
		}
		
		/**
		 * Returns the estimated end time of a given order, sets the start time of the given order because it will be scheduled afterwards.
		 * @param order The order for which we want to calculate the estimated end time.
		 * @param realTime	The current time of the system.
		 * @return An estimated date time object which is the estimated end time of the given order.
		 */
		private DateTime getEstimate(Order order, DateTime realTime){
			DateTime returnTime = null;
			LinkedList<Order> copyqueue = makeCopyOfQueue();
			
			if(firstWorkStation.getCurrentOrder()==null && queue.isEmpty()){
				returnTime = firstOrderEstimate(order);
				returnTime = getEstimatedTime(returnTime, order);
				return returnTime;
			}else{
				if(!queue.isEmpty()){
					if(queue.getFirst().equals(order) && order.equals(currentStrategy.example) && getEstimatedAssemblyTime(queue.getFirst()) > getEstimatedAssemblyTime(order) && !checkDeadline(queue.getFirst(), order) && firstWorkStation.getCurrentOrder()==null){
						returnTime = firstOrderEstimate(order);
						return returnTime;
					}
					
				}
					scheduleOrder(order);
					returnTime = order.getEstimatedEndTime();
					queue = copyqueue;
					reschedule();
					return returnTime;
			}
			

		}
		
		/**
		 * Calculates the estimated end time for a given order possibly scheduled in the first workstation.
		 * @param order	The order for which we want an estimated end time.
		 * @return	The estimated end time for the given order.
		 */
		private DateTime firstOrderEstimate2(Order order){
			int assemblyTime = 0;
			ArrayList<Integer> phases = new ArrayList<>();
			firstWorkStation.buildEstimPhaseList(phases, order);
			assemblyTime += phases.get(numberOfWorkStations-1);
			int maxPhaseWorkstations = phases.get(numberOfWorkStations-1);
			for(int i = 1; i < numberOfWorkStations; i++){
				maxPhaseWorkstations = Math.max(maxPhaseWorkstations, firstWorkStation.getPhaseDuration(i));
				assemblyTime += maxPhaseWorkstations;
			}
			DateTime estimatedEndTime = new DateTime(cycleStartTime);
			return estimatedEndTime.plusMinutes(assemblyTime);
		}
		
		private DateTime firstOrderEstimate(Order order){
			ArrayList<List<Integer>> phaseList = new ArrayList<>();
			ArrayList<Integer> orderPhases = new ArrayList<>();
			firstWorkStation.buildEstimPhaseList(orderPhases, order);
			phaseList.add(orderPhases);
			DateTime scheduleTime = firstWorkStation.reschedule(phaseList, numberOfWorkStations, cycleStartTime, true, false);
			if(firstWorkStation.idle()){
				return scheduleTime;
			}
			return scheduleTime.plusMinutes(orderPhases.get(orderPhases.size()-1));
		}
		
		/**
		 * Returns true if the deadline of the given order is earlier than the deadline of the given next order.
		 * @param order The order for which we want to check.
		 * @param next	The order against which we want to check.
		 * @return	True if the first orders deadline is before the next orders deadline.
		 * 			False if first order deadline is null or later than deadline of the next order.
		 */
		private boolean checkDeadline(Order order, Order next) {
			if(order.getDeadLine()==null)
				return false;
			if(next.getDeadLine()==null)
				return true;
			return order.getDeadLine().isBefore(next.getDeadLine());
		}
	}

}