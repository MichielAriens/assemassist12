package logic.assemblyline;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.joda.time.DateTime;
import org.joda.time.Days;

import logic.car.Order;
import logic.workstation.WorkstationChainBuilder;
import logic.workstation.Workstation;
import logic.workstation.WorkstationDirector;

/**
 * Class handling an assembly line of a car manufacturing company.
 */

public class AssemblyLine {

	
	/**
	 * The first workstation in a line of workstations.
	 */
	private Workstation firstWorkStation;

	/**
	 * Integer holding the number of work stations.
	 */
	private int numberOfWorkStations;

	/**
	 * A list holding the pending orders not on the assemblyline.
	 */
	private LinkedList<Order> queue;

	/**
	 * A dateTime object holding the current time of the system. 
	 */
	private DateTime currentTime;

	/**
	 * A variable holding the statistics of the assembly line.
	 */
	private Statistics stats;
	
	/**
	 * Asks the current time of the system.
	 * @return	The current time of the system.
	 */
	public DateTime getCurrentTime() {
		return currentTime;
	}

	/**
	 * A variable containing the schedule, which is used for scheduling orders.
	 */
	private Schedule schedule;

	/**
	 * Initializes the workstations, the schedule, the statistics and the queue containing the orders.
	 * Also sets the current time to January first 2014 at the beginning of the shift. 
	 */
	public AssemblyLine(){
		queue= new LinkedList<Order>();
		schedule = new Schedule();
		initialiseWorkstations();
		this.numberOfWorkStations = firstWorkStation.countWorkStations();
		this.currentTime = new DateTime(2014, 1, 1, 6, 0);
		stats = new Statistics();
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
	
	/**
	 * Returns true if the assembly line can be moved.
	 * @param phaseDuration	The longest time needed to complete this phase.
	 * @return	True if the assembly line can be moved
	 * 			False otherwise.
	 */
	private boolean moveAssemblyLine(int phaseDuration){
			return schedule.moveAndReschedule(phaseDuration);
	}
	
	/**
	 * Returns a string containing the representation of the statistics.
	 * @return	A string of the statistics.
	 */
	public String getStatistics(){
		return stats.toString();
	}
	/**
	 * completes a task in a certain workstation using the two given identifiers.
	 * @param taskIdentifier	The identifier for the task that needs to be completed.
	 * @param workstationIdentifier	The identifier for the workstation that needs to complete a task.
	 * @return	True if the task is completed succesfully
	 * 			False an identifier is wrong.
	 */
	public boolean doTask(String taskIdentifier, String workstationIdentifier){
		return firstWorkStation.doTask(taskIdentifier, workstationIdentifier);
	}
	
	/**
	 * Returns a list of required task identifiers in the given workstation.
	 * @param workstationIdentifier	The identifier for the workstation for which we need the required tasks.
	 * @return	A list of required task identifiers in the form of strings.
	 */
	public List<String> getRequiredTaskIdentifiers(String workstationIdentifier){
		return this.firstWorkStation.getRequiredTaskIdentifiers(workstationIdentifier);
	}
	
	/**
	 * Returns a string which holds the description for a given task in a given workstation.
	 * @param taskIdentifier	The task for which we need the description.
	 * @param workstationIdentifier	The workstation in which we find the right task.
	 * @return	A string of the description of a given task.
	 */
	public String getTaskDescription(String taskIdentifier, String workstationIdentifier){
		return this.firstWorkStation.getTaskDescription(workstationIdentifier, taskIdentifier);
	}
	
	/**
	 * Returns a list of strings containing the tasks in the given workstation with its status.
	 * @param workstationIdentifier	The identifier for the workstation from which we want the task status.
	 * @return	A list of strings containing the tasks status in one workstation.
	 */
	public List<String> getTaskStatus(String workstationIdentifier){
		return this.firstWorkStation.getTaskStatus(workstationIdentifier);
	}
	
	/**
	 * If the assembly line can be moved then returns true. false otherwise
	 * @param phaseDuration The largest time needed for this phase to complete.
	 * @return	True if the assembly line and workstations can be moved.
	 * 			false otherwise.
	 */
	public boolean tryMoveAssemblyLine(int phaseDuration){
		if(firstWorkStation.canMoveAssemblyLine())
			return moveAssemblyLine(phaseDuration);
		else
			return false;
	}
	
	/**
	 * Returns a list of strategies used by the system.
	 * @return	A list containing all the strategies.
	 */
	public List<SchedulingStrategy> getStrategies(){
		return schedule.getStrategies();
	}
	
	/**
	 * Returns a list of orders which can be used in the batch processing strategy.
	 * @return	A list of orders which qualify for batch processing.
	 */
	public List<Order> getBachList(){
		return schedule.getBatchList();
	}

	/**
	 * Asks a list of workstations available.
	 * @return	The list containing copies of the workstations.
	 */
	public List<Workstation> getWorkStations() {
		LinkedList<Workstation> workStations = new LinkedList<Workstation>();
		firstWorkStation.buildWorkstationList(workStations,numberOfWorkStations);
		return workStations;
	}
	
	/**
	 * Calls the schedule and asks to schedule the given car order.
	 * @param order The car order to be scheduled.
	 */
	public void addOrder(Order order){
		if(order != null)
			schedule.scheduleOrder(order);
	}

	/**
	 * Changes the strategy if possible. Doesn't change anything if the manager wants to change to the currently used strategy. 
	 * @param order null if the manager wants to change to fifo strategy.
	 * 				the example order used in batch processing otherwise.
	 */
	public void changeStrategy(Order order){
		schedule.changeStrategy(order);
	}
	
	/**
	 * Returns a list of 3 car orders which would be in the workstations 
	 * if the assembly is moved by the manager.
	 * @return	A list of car orders which would be in the workstations 
	 * 			if the assembly line should move.
	 */
	public List<Order> askFutureSchedule(){
		return schedule.getFutureSchedule();
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
		 * A constructor which initializes the used strategies and sets the current strategy to fifo strategy.
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
			if(currentTime.getHourOfDay()<shiftBeginHour){
				overTime = (24-shiftEndHour) * 60 + currentTime.getMinuteOfDay();
			}else{
				setOverTime((currentTime.getMinuteOfDay()-(shiftEndHour*60)));
			}
		}

		/**
		 * First checks if the given phase duration is allowed or not.
		 * Updates the current time and sets the end time of the order in the last workstation if there is one.
		 * Also sets the next day and overtime if necessary and updates the statistics. 
		 * Then checks if the strategy needs to change back to fifo.
		 * Reschedules all orders both in the queue and workstations.
		 * @param phaseDuration	The amount which represents the longest time worked on a certain task.
		 * @return	True if the workstation has been moved and rescheduled.	
		 * 			False if the given phase duration is too big.
		 */
		private boolean moveAndReschedule(int phaseDuration) {
			if(!checkPhaseDuration(phaseDuration))
				return false;
			firstWorkStation.adjustDelays(phaseDuration);
			currentTime = currentTime.plusMinutes(phaseDuration);
			Order firstOrder = firstWorkStation.getLastOrder();
			if(firstOrder!=null){
				firstOrder.setEndTime(currentTime);
				stats.finishedCarOrder(firstOrder);
			}
			firstWorkStation.advanceWorkstations(null);
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
		 * If the current strategy is using batch specification, then checks if the system can change to fifo strategy,
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
		 * If the given phase duration does not cause the day to change to the next day after 6 am, then returns true,
		 * otherwise returns false.
		 * @param phaseDuration	The phase duration that needs to be checked.
		 * @return	True if the given phase duration is allowed.
		 * 			False if it is too big.
		 */
		private boolean checkPhaseDuration(int phaseDuration){
			int difference = 0;
			if(firstWorkStation.getTotalEstimatedEndTime() != null)
				difference = getTimeDifference(firstWorkStation.getTotalEstimatedEndTime(), currentTime);
			int diff= shiftBeginHour*60 - difference - 1;
			int max = 24*60 - currentTime.getMinuteOfDay()+diff;
			if(max>= phaseDuration)
				return true;
			return false;
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
		 * If the first workstation does not have an order in it and the queue is not empty, then tries to add
		 * an order to the first workstation. After that reschedules the workstations orders and the queue.
		 */
		private void reschedule(){
			if(firstWorkStation.getCurrentOrder()==null && !queue.isEmpty()){
				addOrderToFirstWorkstation();
			}
			DateTime workstationEET = firstWorkStation.reschedule(getPhaseDurations(), numberOfWorkStations, currentTime);
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
		 * Given the start time, calculates the estimated time of the orders in the queue.
		 * Every order calculates its own estimated end time using a number of next orders that is equal to the amount of workstations minus one.
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
				startTime = startTime.plusMinutes(maxPhase);
				if(startTime.getHourOfDay()<shiftBeginHour || startTime.getMinuteOfDay()>=shiftEndHour*60-overTime)
					startTime = getEstimatedTime(startTime, queue.get(i));
				queue.get(i).setEstimatedEndTime(startTime);
			}
		}
		
		/**
		 * Adds an order to the first workstation if it can still be scheduled today. It calculates 
		 * the estimated time to see if the order can be scheduled today, but does not set it, even if
		 * the order can be scheduled today.
		 */
		private void addOrderToFirstWorkstation(){
			Order order = queue.getFirst();
			LinkedList<Order> list = new LinkedList<Order>();
			int assemblyTime = 0;
			list.add(order);
			assemblyTime += calculateMaxPhase(list);
			for(int i = 1; i < getWorkStations().size(); i++){
				list.add(getWorkStations().get(i).getCurrentOrder());
				assemblyTime += calculateMaxPhase(list);
			}
			DateTime estimatedEndTime = new DateTime(currentTime);
			estimatedEndTime = estimatedEndTime.plusMinutes(assemblyTime);
			if(estimatedEndTime.getMinuteOfDay()<shiftEndHour*60-overTime || estimatedEndTime.getHourOfDay()>=shiftBeginHour){
				firstWorkStation.setOrder(queue.pop());
			}
			
		}

		
		/**
		 * Calculates the maximum phase duration from a given list of orders.
		 * @param orders	The list of orders on which we need to calculate the phase duration.
		 * @return	An integer containing the maximum phase duration.
		 */
		private int calculateMaxPhase(List<Order> orders){
			int max = 0;
			if(!orders.isEmpty()){
				for(Order next : orders){
					if(next!=null){
						if(next.getPhaseTime()>max)
							max=next.getPhaseTime();
					}
				}
			}
			return max;
		}
		
		/**
		 * Sets the current time to the next day at the beginning of the shift, if the time is later than the begin hour of the shift.
		 * Else just sets the hour at the beginning of the shift.
		 */
		private void setNextDay() {
			if(currentTime.getHourOfDay()<shiftBeginHour)
				currentTime = new DateTime(currentTime.getYear(),currentTime.getMonthOfYear(),currentTime.getDayOfMonth(),shiftBeginHour,0);
			else{
				currentTime = currentTime.plusDays(1);
				currentTime = new DateTime(currentTime.getYear(),currentTime.getMonthOfYear(),currentTime.getDayOfMonth(),shiftBeginHour,0);
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
		 * Checks if it is the end of the working day, if it is returns true, else returns false.
		 * @return 	True if all workstations are done and it is later than the end hour of the shift 
		 * 		  	minus the overtime or earlier than the begin hour of the shift.
		 * 			False otherwise.
		 */
		private boolean checkEndOfDay() {
			boolean doneWorking = firstWorkStation.allIdle();
			if(!doneWorking)
				return false;
			int assemblyTime = 60*numberOfWorkStations;
			if(!queue.isEmpty()){
				assemblyTime = queue.getFirst().getPhaseTime()*numberOfWorkStations;
			}
			if(currentTime.getMinuteOfDay()>=(shiftEndHour * 60-overTime - assemblyTime) || currentTime.getMinuteOfDay()<shiftBeginHour*60)
				return true;
			return false;
		}

		/**
		 * If the days between the given time and the current time of the system differ, then it checks 
		 * if there is time for a new order without considering the overtime. Else checks if there is time
		 * for a new order considering the overtime. 
		 * @param time	The time to check against the current time of the system.
		 * @return True if there is time for a new order on this day, else returns false.
		 */
		private boolean timeForNewOrder(DateTime time) {
			if(Days.daysBetween(time, currentTime).getDays()==0){
				return time.getMinuteOfDay()<(shiftEndHour*60-overTime) && time.getMinuteOfDay()>=shiftBeginHour*60;
			}else{
				return  time.getMinuteOfDay()<(shiftEndHour*60) && time.getMinuteOfDay()>=shiftBeginHour*60;
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
		 * Sets the start time of the given order to the current time, then uses the current strategy to add the order
		 * and reschedules the whole queue and workstations.
		 * @param order	The order that needs to be scheduled.
		 */
		private void scheduleOrder(Order order){
			DateTime startTime = new DateTime(currentTime);
			order.setStartTime(startTime);
			currentStrategy.addOrder(order, queue);
			reschedule();
		}
		
		/**
		 * If the given order is null then changes the strategy to fifo. else it changes to batch processing and
		 * uses the given order as an example order, used to rebuild the queue. Afterwards it reschedules the queue and workstations.
		 * @param order The possible example order used by batch processing, null if fifostrategy needs to be used.
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
					return new DateTime(estimatedEndTime.getYear(),estimatedEndTime.getMonthOfYear(),estimatedEndTime.getDayOfMonth(),shiftBeginHour,0).plusMinutes(order.getPhaseTime()*numberOfWorkStations);

				}else{
					estimatedEndTime =  new DateTime(estimatedEndTime.getYear(),estimatedEndTime.getMonthOfYear(),estimatedEndTime.getDayOfMonth(),shiftBeginHour,0).plusMinutes(order.getPhaseTime()*numberOfWorkStations);
					return estimatedEndTime.plusDays(1);
				}
			}else{
				return estimatedEndTime;
			}
		}

		/**
		 * Returns a list of 3 car orders which shows the car orders in the workstations if the
		 * assembly line would be moved. 
		 * @return	A list of car orders which would be in the workstations after the assembly line is moved.
		 */
		private List<Order> getFutureSchedule(){
			ArrayList<Order> returnList = new ArrayList<Order>();

			try{
				if(queue.getFirst().getEstimatedEndTime().getDayOfMonth()==currentTime.getDayOfMonth() && queue.getFirst().getEstimatedEndTime().getHourOfDay()<shiftEndHour)
					returnList.add(queue.getFirst());
				else returnList.add(null);
			}catch(NoSuchElementException e){
				returnList.add(null);
			}
			firstWorkStation.buildFutureOrderList(returnList);
			return returnList;
		}
		
		/**
		 * Returns a list of strategies used by the system. The first element is the current strategy used.
		 * It contains the currentstrategy 2 times.
		 * @return A list containing the different strategies.
		 */
		private List<SchedulingStrategy> getStrategies(){
			LinkedList<SchedulingStrategy> returnList = new LinkedList<SchedulingStrategy>();
			returnList.add(currentStrategy.getRawCopy());
			for(SchedulingStrategy next : stratList){
				returnList.add(next.getRawCopy());
			}
			return returnList;
		}
		
		/**
		 * Checks if there are orders in the workstations and the queue for which batch processing can be applied.
		 * It then returns a list of those.
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

	}
}

