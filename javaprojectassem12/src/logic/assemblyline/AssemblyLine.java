package logic.assemblyline;
import interfaces.Printable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import logic.car.Order;
import logic.car.VehicleModel;
import logic.workstation.Task;
import logic.workstation.Workstation;
import logic.workstation.WorkstationChainBuilder;
import logic.workstation.WorkstationDirector;

import org.joda.time.DateTime;
import org.joda.time.Days;

import com.sun.corba.se.spi.ior.MakeImmutable;

/**
 * Class handling an assembly line of a car manufacturing company.
 */

public class AssemblyLine implements Printable {


	private OperationalStatus status = OperationalStatus.OPERATIONAL;
	
	private int cycleTime=0;
	
	/**
	 * The first workstation in a line of workstations.
	 */
	private Workstation firstWorkStation;

	/**
	 * Integer holding the number of work stations.
	 */
	private int numberOfWorkStations;

	/**
	 * A list holding the pending orders not on the assembly line.
	 */
	private LinkedList<Order> queue;

	/**
	 * A dateTime object holding the current time of the system. 
	 */
	private DateTime cycleStartTime;

	/**
	 * A variable holding the statistics of the assembly line.
	 */
	private StatisticsAssemblyLine stats;

	/**
	 * Returns the current time of the system.
	 * @return	The current time of the system.
	 */
	public DateTime getcycleStartTime() {
		return cycleStartTime;
	}

	/**
	 * A variable containing the schedule, which is used for scheduling orders.
	 */
	private Schedule schedule;

	private Collection<VehicleModel> capabilities;

	/**
	 * Initializes the workstations, the schedule, the statistics and the queue containing the orders.
	 * Also sets the current time to January first 2014 at the beginning of the shift. 
	 */
	public AssemblyLine(Collection<VehicleModel> capabilities, WorkstationChainBuilder builder){
		this.capabilities = capabilities;
		queue= new LinkedList<Order>();
		schedule = new Schedule();
		this.firstWorkStation = builder.getResult();
		this.numberOfWorkStations = firstWorkStation.countWorkStations();
		this.cycleStartTime = new DateTime(2014, 1, 1, 6, 0);
		stats = new StatisticsAssemblyLine(this.getStringRepresentation());
	}

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
	 * Initializes the workstation using a builder.
	 */
	private void initialiseWorkstations(){
		WorkstationChainBuilder builder = new WorkstationChainBuilder();
		WorkstationDirector director = new WorkstationDirector(builder);
		director.construct();
		this.firstWorkStation = builder.getResult();
	}
	
	public int getCycleTime(){
		return this.cycleTime;
	}
	
	/**
	 * Gets the end of the current phase (= time of the begin of the next cycle).
	 * @return
	 */
	public DateTime getCycleEnd(){
		return this.getcycleStartTime().plusMinutes(cycleTime);
	}
	
	public List<Order> setStatus(OperationalStatus status){
		this.status=status;
		LinkedList<Order> returnList = new LinkedList<Order>();
		if(status != OperationalStatus.OPERATIONAL){
			returnList = schedule.makeCopyOfQueue();
			queue.clear();
		}
		return returnList;
			
	}
	
	protected void fix(DateTime realTime){
		this.cycleStartTime =realTime;
		this.setStatus(OperationalStatus.OPERATIONAL);
	}

	/**
	 * Returns true if the assembly line can be moved.
	 * @param phaseDuration	The longest time needed to complete this phase.
	 * @return	True if the assembly line can be moved
	 * 			False otherwise.
	 */
	 protected boolean moveAssemblyLine(DateTime realTime){
		if(tryMoveAssemblyLine()){
			if(status== OperationalStatus.MAINTENANCE && firstWorkStation.allIdle())
				setStatus(OperationalStatus.OPERATIONAL);
			boolean done = schedule.moveAndReschedule(cycleTime);
			cycleTime = 0;
			cycleStartTime = realTime;
			if(status==OperationalStatus.MAINTENANCE && firstWorkStation.allIdle())
				cycleTime=status.getTime();
			return done;
		}

		return false;
	}

	/**
	 * Returns a string containing the representation of the statistics.
	 * @return	A string containing the representation of the statistics.
	 */
	public StatisticsAssemblyLine getStatistics(){
		return stats;
	}

	/**
	 * Completes the task corresponding to the given task.
	 * @param task	A copy of the task that needs to be completed.
	 * @return	True if the task is completed successfully.
	 * 			False the task could not be completed.
	 */
	public boolean doTask(Printable task, int timeTaken){
		boolean work = firstWorkStation.doTask(task, timeTaken);
		if(firstWorkStation.canMoveAssemblyLine()){
			cycleTime = firstWorkStation.getMaxElapsedTime();
		}
		return work;
	}

	/**
	 * Returns a list of pending tasks at a given workstation.
	 * @param station	The a copy of the workstation for which the pending tasks are needed.
	 * @return	A list of tasks that are pending at the given workstation.
	 */
	public List<Printable> getRequiredTasks(Printable station){
		return this.firstWorkStation.getRequiredTasks(station);
	}

	/**
	 * Returns a list of all tasks at a given workstation.
	 * @param station	The a copy of the workstation for which the tasks are needed.
	 * @return	A list of tasks at the given workstation.
	 */
	public List<Printable> getAllTasks(Printable station){
		return this.firstWorkStation.getAllTasks(station);
	}

	/**
	 * Returns true if the assembly line can be moved, false otherwise.
	 * @param phaseDuration The largest time needed for this phase to complete.
	 * @return	True if the assembly line and workstations can be moved.
	 * 			false otherwise.
	 */
	public boolean tryMoveAssemblyLine(){
		return firstWorkStation.canMoveAssemblyLine();
			
	}

	/**
	 * Returns a list of the current strategies followed by the available scheduling strategies.
	 * @return a list of the current strategies followed by the available scheduling strategies.
	 */
	public List<Printable> getStrategies(){
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
	 * @return	The list containing copies of the workstations.
	 */
	public List<Printable> getWorkStations() {
		LinkedList<Printable> workStations = new LinkedList<>();
		firstWorkStation.buildWorkstationList(workStations,numberOfWorkStations);
		return workStations;
	}

	/**
	 * Calls the schedule and asks to schedule the given car order.
	 * The order will only be scheduled if it is not null and this assemblyline can actually schedule this order.
	 * @param order The car order to be scheduled.
	 * @return		Whether the order was scheduled.
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
	 * @param 		order		The order to schedule.
	 * @return		A estimated assembly time or null if the order cannot be scheduled on this line.
	 * @stateless
	 */
	public DateTime getEstimate(Order order) {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public String getStringRepresentation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getExtraInformation() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public DateTime getEstimate(Order order, DateTime realTime){
		return schedule.getEstimate(order, realTime);
		
	}

	@Override
	public String getStatus() {
		// TODO Auto-generated method stub
		return null;
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
		 * First checks if the given phase duration is allowed or not.
		 * Updates the current time and sets the end time of the order in the last workstation if there is one.
		 * Also sets the next day and overtime if necessary and updates the statistics. 
		 * Then checks if the strategy needs to change back to FIFO.
		 * Reschedules all orders both in the queue and workstations.
		 * @param phaseDuration	The amount which represents the longest time worked on a certain task.
		 * @return	True if the workstation has been moved and rescheduled.	
		 * 			False if the given phase duration is too big.
		 */
		private boolean moveAndReschedule(int phaseDuration) {

			firstWorkStation.adjustDelays(phaseDuration);
			cycleStartTime = cycleStartTime.plusMinutes(phaseDuration);
			Integer delayLastOrder= firstWorkStation.getDelayLastOrder();
			if(delayLastOrder != null)
				stats.finishedCarOrder(delayLastOrder, cycleStartTime);
			firstWorkStation.advanceOrders(null, cycleStartTime);
			if(checkEndOfDay()){
				calculateOverTime();
				setNextDay();
				stats.setNextDay();
			}

			checkStrategy();

			reschedule();
			return true;
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
			DateTime workstationEET = firstWorkStation.reschedule(getPhaseDurations(), numberOfWorkStations, cycleStartTime);
			rescheduleQueue(workstationEET);
		}


		/**
		 * Builds a list of phase durations from the orders in the queue. The length of the returned list is either
		 * the amount of workstations or the number of orders in the queue, if there are less orders than workstations.
		 * @return A list of integers containing the phase durations.
		 */
		private List<Integer> getPhaseDurations(){
			ArrayList<Integer> prePhaseDurations = new ArrayList<Integer>();
			int j = 0;
			for(int i = 0; i < queue.size(); i++){
				if(j >= numberOfWorkStations-1)
					break;
				j++;
				prePhaseDurations.add(0, queue.get(i).getPhaseTime());
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
			for(int i = 0; i < queue.size(); i++){
				int count = 0;
				int maxPhase = queue.get(i).getPhaseTime();
				for(int j = i+1; j < queue.size(); j++){
					if(count >= numberOfWorkStations-1)
						break;
					count++;
					if(queue.get(j).getPhaseTime() > maxPhase)
						maxPhase = queue.get(j).getPhaseTime();
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
			int assemblyTime = 50*numberOfWorkStations;
			if(!queue.isEmpty()){
				assemblyTime = getEstimatedAssemblyTime(queue.getFirst());
			}
			if(cycleStartTime.getMinuteOfDay()>=(shiftEndHour * 60-overTime - assemblyTime) || cycleStartTime.getMinuteOfDay()<shiftBeginHour*60)
				return true;
			return false;
		}
		
		//TODO docu
		private int getEstimatedAssemblyTime(Order order){
			ArrayList<Integer> phases = new ArrayList<>();
			firstWorkStation.buildEstimPhaseList(phases, queue.getFirst());
			int assemblyTime = 0;
			for(Integer i : phases){
				assemblyTime += i;
			}
			return assemblyTime;
		}

		/**
		 * If the days between the given time and the current time of the system differ, then it checks 
		 * if there is time for a new order without considering the overtime. Else checks if there is time
		 * for a new order considering the overtime. 
		 * @param time	The time to check against the current time of the system.
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
		 * Sets the start time of the given order to the current time, then uses the current 
		 * strategy to add the order and reschedule the whole queue and workstations.
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
		private List<Printable> getStrategies(){
			LinkedList<Printable> returnList = new LinkedList<>();
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
		
		//TODO DOCU
		private DateTime getEstimate(Order order, DateTime realTime){
			DateTime returnTime = null;
			LinkedList<Order> copyqueue = makeCopyOfQueue();
			order.setStartTime(realTime);
			if(firstWorkStation.getCurrentOrder()==null && queue.isEmpty()){
				returnTime = firstOrderEstimate(order);

			}else{
				if(queue.getFirst().equals(order) && currentStrategy.example.equals(order) && getEstimatedAssemblyTime(queue.getFirst()) > getEstimatedAssemblyTime(order) && !checkDeadline(queue.getFirst(), order)){
					returnTime = firstOrderEstimate(order);

				}else{
					scheduleOrder(order);
					returnTime = order.getEstimatedEndTime();
					queue = copyqueue;
					reschedule();
				}
			}
			return returnTime;

		}
		
		//TODO DOCU
		private DateTime firstOrderEstimate(Order order){
			int assemblyTime = 0;
			assemblyTime += order.getPhaseTime();
			int maxPhaseWorkstations = order.getPhaseTime();
			for(int i = 1; i < getWorkStations().size(); i++){
				maxPhaseWorkstations = Math.max(maxPhaseWorkstations, firstWorkStation.getPhaseDuration(i));
				assemblyTime += maxPhaseWorkstations;
			}
			DateTime estimatedEndTime = new DateTime(cycleStartTime);
			return estimatedEndTime.plusMinutes(assemblyTime);
		}
		
		//TODO docu
		private boolean checkDeadline(Order order, Order next) {
			if(order.getDeadLine()==null)
				return false;
			if(next.getDeadLine()==null)
				return true;
			return order.getDeadLine().isBefore(next.getDeadLine());
		}
	}

	public OperationalStatus getOperationalStatus() {
		return this.status;
	}
}