package logic.assemblyline;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.joda.time.DateTime;
import org.joda.time.Days;

import logic.car.Order;
import logic.workstation.ConcreteWorkstationBuilder;
import logic.workstation.Workstation;
import logic.workstation.WorkstationDirector;

/**
 * Class handling an assembly line of a car manufacturing company.
 */

public class AssemblyLine {

	
	
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
	 * Initializes the workstations, the schedule and the queue containing the orders.
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
	
	private void initialiseWorkstations(){
		ConcreteWorkstationBuilder builder = new ConcreteWorkstationBuilder();
		WorkstationDirector director = new WorkstationDirector(builder);
		director.construct();
		this.firstWorkStation = builder.getResult();
	}
	
	
	private boolean moveAssemblyLine(int phaseDuration){
			return schedule.moveAndReschedule(phaseDuration);
	}
	
	public String getStatistics(){
		return stats.toString();
	}
	
	public boolean doTask(String taskIdentifier, String workstationIdentifier){
		return firstWorkStation.doTask(taskIdentifier, workstationIdentifier);
	}
	
	public List<String> getRequiredTaskIdentifiers(String workstationIdentifier){
		return this.firstWorkStation.getRequiredTaskIdentifiers(workstationIdentifier);
	}
	
	public String getTaskDescription(String taskIdentifier, String workstationIdentifier){
		return this.firstWorkStation.getTaskDescription(workstationIdentifier, taskIdentifier);
	}
	
	public List<String> getTaskStatus(String workstationIdentifier){
		return this.firstWorkStation.getTaskStatus(workstationIdentifier);
	}
	
	public boolean tryMoveAssemblyLine(int phaseDuration){
		if(firstWorkStation.canMoveAssemblyLine())
			return moveAssemblyLine(phaseDuration);
		else
			return false;
	}
	
	public List<SchedulingStrategy> getStrategies(){
		return schedule.getStrategies();
	}
	
	public List<Order> getBachList(){
		return schedule.getBatchList();
	}

	/**
	 * Asks a list of workstations available.
	 * @return	The list of workstations.
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
	 *A class which keeps the begin and end hour of the shift. Also holds the expected assembly time
	 *for a car order and the overtime made during the previous day. It calculates estimated end times
	 *and start time of car orders. It can also schedule new car orders into a queue containing 
	 *pending car orders.
	 */
	class Schedule {

		SchedulingStrategy currentStrategy; 
		
		LinkedList<SchedulingStrategy> stratList = new LinkedList<SchedulingStrategy>();
		
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
		
//		private void adjustDelays(int phaseDuration){
//			LinkedList<Order> list = new LinkedList<Order>();
//			for(Workstation next : workStations){
//				list.add(next.getCurrentOrder());
//			}
//			int amount = phaseDuration - calculateMaxPhase(list);
//			for(Workstation next : workStations){
//				if(next.getCurrentOrder()!=null)
//					next.getCurrentOrder().addDelay(amount);
//			}
//			
//		}
		
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
		
		private int getTimeDifference(DateTime estimate, DateTime current){
			if(estimate.getMinuteOfDay()>current.getMinuteOfDay()){
				return estimate.getMinuteOfDay()-current.getMinuteOfDay();
			}else{
				return estimate.getMinuteOfDay()+ 24*60 -current.getMinuteOfDay();
			}
		}
		
		private void reschedule(){
			if(firstWorkStation.getCurrentOrder()==null && !queue.isEmpty()){
				addOrderToFirstWorkstation();
			}
			DateTime workstationEET = firstWorkStation.reschedule(getPhaseDurations(), numberOfWorkStations, currentTime);
			rescheduleQueue(workstationEET);
		}
		
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
		
//		private void rescheduleQueue(){
//			if(!queue.isEmpty()){
//				LinkedList<Order> list = new LinkedList<Order>();
//				DateTime estimatedEndTime = new DateTime(currentTime);
//				int index =0;
//
//				for(Order next : queue){
//					if(index==0){
//						buildEstimateFirstInQueue();
//						adjustPrevious(index);
//					}else{
//						list.addAll(addPrevious(index));
//						estimatedEndTime = list.getFirst().getEstimatedEndTime();
//						int assemblyTime = getAssemblyTimeReversed(next,list);
//						estimatedEndTime = estimatedEndTime.plusMinutes(assemblyTime);
//						estimatedEndTime = getEstimatedTime(estimatedEndTime, next);
//						next.setEstimatedEndTime(estimatedEndTime);
//						adjustPrevious(index);
//						list.clear();
//					}
//					index++;
//				}
//			}
//		}
		
//		private int getAssemblyTimeReversed(Order order, LinkedList<Order> list){
//			int returnval = 0;
//			returnval+=positiveDifference(order, biggestPhaseTime(list));
//			list.removeLast();
//			returnval+=positiveDifference(order, biggestPhaseTime(list));
//			returnval+=order.getPhaseTime();
//			return returnval;
//		}
		
//		private int positiveDifference(Order first,Order second){
//			int returnval = first.getPhaseTime()-second.getPhaseTime();
//			if(returnval>0)
//				return returnval;
//			else
//				return 0;
//		}
		
//		private void buildEstimateFirstInQueue(){
//			LinkedList<Order> list = new LinkedList<Order>();
//			DateTime estimatedEndTime = new DateTime(currentTime);
//			if(estimatedEndTime.getHourOfDay()<shiftBeginHour || estimatedEndTime.getMinuteOfDay()>=shiftEndHour*60-overTime){
//				estimatedEndTime = getEstimatedTime(estimatedEndTime, queue.getFirst());
//				queue.getFirst().setEstimatedEndTime(estimatedEndTime);
//				return;
//			}
//			for(Workstation ws : getWorkStations()){
//				list.add(ws.getCurrentOrder());				
//			}
//			estimatedEndTime = estimatedEndTime.plusMinutes(calculateMaxPhase(list));
//			list.removeLast();
//			list.add(0,queue.getFirst());
//			while(!list.isEmpty()){
//				estimatedEndTime = estimatedEndTime.plusMinutes(calculateMaxPhase(list));
//				list.removeLast();
//			}
//			estimatedEndTime = getEstimatedTime(estimatedEndTime, queue.getFirst());
//			queue.getFirst().setEstimatedEndTime(estimatedEndTime);
//		}
	
//		private LinkedList<Order> addPrevious(int index){
//			LinkedList<Order> list = new LinkedList<Order>();
//			int n = workStations.size();
//			int nbInStations = n-1-index;
//			int count = 0;
//			for(int i = index-1; i >= 0; i--){
//				if(count >= n-1)
//					break;
//				list.add(queue.get(i));
//				count++;
//			}
//			for(int i = 0; i < nbInStations; i++){
//				list.add(workStations.get(i).getCurrentOrder());
//			}
//			return list;
//		}
		
		
//		private Order biggestPhaseTime(List<Order> orders){
//			if(orders == null)
//				return null;
//			int maximum = 0;
//			Order bestOrder = null;
//			for(Order o: orders){
//				if(o != null){
//					if(o.getPhaseTime() > maximum){
//						maximum = o.getPhaseTime();
//						bestOrder = o;
//					}
//				}
//			}
//			return bestOrder;
//		}
		
//		private void adjustPrevious(int index){
//			Order order = queue.get(index);
//			LinkedList<Order> list = addPrevious(index);
//
//			if(timeForNewOrder(order.getEstimatedEndTime())){
//				int big = 0;
//				for(int i=0;i<list.size();i++){
//					LinkedList<Order> sublist = new LinkedList<Order>(list.subList(0, i+1));
//					if(biggestPhaseTime(sublist) == null)
//						continue;
//					big=biggestPhaseTime(sublist).getPhaseTime();
//					if(order.getPhaseTime()>big){
//						for(Order next : sublist){
//							if(next!=null && order.getEstimatedEndTime().getDayOfYear()==next.getEstimatedEndTime().getDayOfYear() && next.getEstimatedEndTime().getHourOfDay()>shiftBeginHour)
//								next.setEstimatedEndTime(next.getEstimatedEndTime().plusMinutes(order.getPhaseTime()-big));
//						}
//
//
//					}
//				}
//			}
//		}
		

//		private void rescheduleWorkstations(){
//			LinkedList<Order> list = new LinkedList<Order>();
//			DateTime estimatedEndTime = new DateTime(currentTime);
//			for(Workstation w : workStations){
//				list.add(w.getCurrentOrder());
//			}
//			estimatedEndTime = estimatedEndTime.plusMinutes(calculateMaxPhase(list));
//			for(int i = workStations.size()-1; i >= 0; i--){
//				if(workStations.get(i).getCurrentOrder()!=null)
//					workStations.get(i).getCurrentOrder().setEstimatedEndTime(estimatedEndTime);
//				list.removeLast();
//				estimatedEndTime = estimatedEndTime.plusMinutes(calculateMaxPhase(list));
//			}
//		}
		
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
		
		private void scheduleOrder(Order order){
			DateTime startTime = new DateTime(currentTime);
			order.setStartTime(startTime);
			currentStrategy.addOrder(order, queue);
			reschedule();
		}
		
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
		 * It then returns a list 
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
