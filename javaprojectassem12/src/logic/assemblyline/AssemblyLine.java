package logic.assemblyline;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.joda.time.DateTime;
import org.joda.time.Days;

import logic.car.Order;
import logic.workstation.AccessoriesPost;
import logic.workstation.CarBodyPost;
import logic.workstation.DriveTrainPost;
import logic.workstation.Workstation;

/**
 * Class handling an assembly line of a car manufacturing company.
 */

public class AssemblyLine {

	/**
	 * Array holding the three workstations.
	 */
	private LinkedList<Workstation> workStations = new LinkedList<Workstation>();
	
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

//	/**
//	 * Asks the current time of the system.
//	 * @return	The current time of the system.
//	 */
//	public DateTime getCurrentTime() {
//		return currentTime;
//	}

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
		this.initialiseWorkStations();
		this.currentTime = new DateTime(2014, 1, 1, 6, 0);
	}
	
	private void initialiseWorkStations(){
		workStations.add( new CarBodyPost());
		workStations.add(new DriveTrainPost());
		workStations.add(new AccessoriesPost());
		this.firstWorkStation = workStations.getFirst();
		workStations.get(0).setWorkStation(workStations.get(1));
		workStations.get(1).setWorkStation(workStations.get(2));
		numberOfWorkStations = workStations.size();
	}

	/**
	 * First checks if the phaseDuration is between 0 and 180 minutes.
	 * Moves the car orders on the assembly line if every work station is ready and 
	 * sets the end time of the first order to the given end time if there was an order 
	 * in the last workstation. Then checks if it's the end of the day. If it is the end of the day,
	 * sets the time to the next day at the beginning of the shift and updates the estimated time of
	 * the car orders. It also calculates the overtime made during that day. Else just updates the
	 * estimated time of the car orders.
	 * @param phaseDuration The time in minutes representing the duration of the current phase.
	 * @return	False if the assembly line can not be moved.
	 * 			True otherwise.
	 */
	private boolean moveAssemblyLine(int phaseDuration){
			return schedule.moveAndReschedule(phaseDuration);
	}
	
	/**
	 * Progresses time without progressing the assembly line. 
	 * Does no checks as to the state of the line or overtime. As such this method can simulate prolonged idle time on the assemblyline.
	 * Overtime will be carried to the next day if the invocation results in a moment between shifts.
	 * @param phaseDuration		The duration of idle time in minutes.
	 */
//	public void progressTime(int phaseDuration){
//		currentTime = currentTime.plusMinutes(phaseDuration);
//		if(currentTime.getHourOfDay()>=22 || currentTime.getHourOfDay()<6){
//			schedule.setNextDay();
//			
//		}
//		schedule.updateEstimatedTimes(phaseDuration);
//	}

	/**
	 * Checks if all the work stations are done.
	 * @return 	True if all work stations are done.
	 * 			False if one or more work stations are not done.
	 */
	private boolean checkWorkStations() {
		return firstWorkStation.canMoveAssemblyLine();
	}
	
	public boolean tryMoveAssemblyLine(int phaseDuration){
		if(checkWorkStations())
			return moveAssemblyLine(phaseDuration);
		else
			return false;
	}

	/**
	 * Asks a list of workstations available.
	 * @return	The list of workstations.
	 */
	public List<Workstation> getWorkStations() {
		return new LinkedList<Workstation>(workStations);
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

		SchedulingStrategy strategy = new FifoStrategy(); 
		
		
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
			currentTime = currentTime.plusMinutes(phaseDuration);
			Order firstOrder = workStations.get(numberOfWorkStations-1).getCurrentOrder();
			if(firstOrder!=null)
				firstOrder.setEndTime(currentTime);
			
			firstWorkStation.advanceWorkstations(null);
			if(checkEndOfDay()){
				calculateOverTime();
				setNextDay();
			}

			reschedule();
			return true;
		}
		
		private boolean checkPhaseDuration(int phaseDuration){
			
			for(Workstation next : workStations){
				if(next.getCurrentOrder()!=null){
					Order order =next.getCurrentOrder();
					int difference = getTimeDifference(order.getEstimatedEndTime(),currentTime);
					int diff=
				}
			}
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
			rescheduleWorkstations();
			rescheduleQueue();
		}
		
		private void addOrderToFirstWorkstation(){
			Order order = queue.getFirst();
			LinkedList<Order> list = new LinkedList<Order>();
			int assemblyTime = 0;
			list.add(order);
			assemblyTime+=calculateMaxPhase(list);
			list.add(workStations.get(1).getCurrentOrder());
			assemblyTime+=calculateMaxPhase(list);
			list.add(workStations.get(2).getCurrentOrder());
			assemblyTime+=calculateMaxPhase(list);
			DateTime estimatedEndTime = new DateTime(currentTime);
			estimatedEndTime = estimatedEndTime.plusMinutes(assemblyTime);
			if(estimatedEndTime.getMinuteOfDay()<shiftEndHour*60-overTime || estimatedEndTime.getHourOfDay()>=shiftBeginHour){
				firstWorkStation.setOrder(queue.pop());
			}
			
		}
		
		private void rescheduleQueue(){
			if(!queue.isEmpty()){
				LinkedList<Order> list = new LinkedList<Order>();
				DateTime estimatedEndTime = new DateTime(currentTime);
				int index =0;

				for(Order next : queue){
					if(index==0){
						buildEstimateFirstInQueue();
						adjustPrevious(index);
					}else{
						list.addAll(addPrevious(index));
						estimatedEndTime = list.getFirst().getEstimatedEndTime();
						int assemblyTime = getAssemblyTimeReversed(next,list);
						estimatedEndTime = estimatedEndTime.plusMinutes(assemblyTime);
						estimatedEndTime = getEstimatedTime(estimatedEndTime, next);
						next.setEstimatedEndTime(estimatedEndTime);
						adjustPrevious(index);
						list.clear();
					}
					index++;
				}
			}
		}
		
		private int getAssemblyTimeReversed(Order order, LinkedList<Order> list){
			int returnval = 0;
			returnval+=positiveDifference(order, biggestPhaseTime(list));
			list.removeLast();
			returnval+=positiveDifference(order, biggestPhaseTime(list));
			returnval+=order.getPhaseTime();
			return returnval;
		}
		
		private int positiveDifference(Order first,Order second){
			int returnval = first.getPhaseTime()-second.getPhaseTime();
			if(returnval>0)
				return returnval;
			else
				return 0;
		}
		
		private void buildEstimateFirstInQueue(){
			LinkedList<Order> list = new LinkedList<Order>();
			DateTime estimatedEndTime = new DateTime(currentTime);
			if(estimatedEndTime.getHourOfDay()<shiftBeginHour || estimatedEndTime.getMinuteOfDay()>=shiftEndHour*60-overTime){
				estimatedEndTime = getEstimatedTime(estimatedEndTime, queue.getFirst());
				queue.getFirst().setEstimatedEndTime(estimatedEndTime);
				return;
			}
			list.add(queue.getFirst());
			list.add(firstWorkStation.getCurrentOrder());
			list.add(workStations.get(1).getCurrentOrder());
			estimatedEndTime = estimatedEndTime.plusMinutes(calculateMaxPhase(list));
			list.removeLast();
			estimatedEndTime = estimatedEndTime.plusMinutes(calculateMaxPhase(list));
			list.removeLast();
			estimatedEndTime = estimatedEndTime.plusMinutes(calculateMaxPhase(list));
			list.removeLast();
			estimatedEndTime = getEstimatedTime(estimatedEndTime, queue.getFirst());
			queue.getFirst().setEstimatedEndTime(estimatedEndTime);
			
		}
	
		private LinkedList<Order> addPrevious(int index){
			LinkedList<Order> list = new LinkedList<Order>();
			int n = workStations.size();
			int nbInStations = n-1-index;
			int count = 0;
			for(int i = index-1; i >= 0; i--){
				if(count >= n-1)
					break;
				list.add(queue.get(i));
				count++;
			}
			for(int i = 0; i < nbInStations; i++){
				list.add(workStations.get(i).getCurrentOrder());
			}
			return list;
		}
		
		
		private Order biggestPhaseTime(List<Order> orders){
			if(orders == null)
				return null;
			int maximum = 0;
			Order bestOrder = null;
			for(Order o: orders){
				if(o != null){
					if(o.getPhaseTime() > maximum){
						maximum = o.getPhaseTime();
						bestOrder = o;
					}
				}
			}
			return bestOrder;
		}
		
		private void adjustPrevious(int index){
			Order order = queue.get(index);
			LinkedList<Order> list = addPrevious(index);

			if(timeForNewOrder(order.getEstimatedEndTime())){
				int big = 0;
				for(int i=0;i<list.size();i++){
					LinkedList<Order> sublist = (LinkedList<Order>) list.subList(0, i+1);
					if(biggestPhaseTime(sublist) == null)
						continue;
					big=biggestPhaseTime(sublist).getPhaseTime();
					if(order.getPhaseTime()>big){
						for(Order next : sublist){
							if(next!=null && order.getEstimatedEndTime().getDayOfYear()==next.getEstimatedEndTime().getDayOfYear() && next.getEstimatedEndTime().getHourOfDay()>shiftBeginHour)
								next.setEstimatedEndTime(next.getEstimatedEndTime().plusMinutes(order.getPhaseTime()-big));
						}


					}
				}
			}
		}
		

		private void rescheduleWorkstations(){
			LinkedList<Order> list = new LinkedList<Order>();
			DateTime estimatedEndTime = new DateTime(currentTime);
			for(Workstation w : workStations){
				list.add(w.getCurrentOrder());
			}
			estimatedEndTime = estimatedEndTime.plusMinutes(calculateMaxPhase(list));
			for(int i = workStations.size()-1; i >= 0; i--){
				if(workStations.get(i).getCurrentOrder()!=null)
					workStations.get(i).getCurrentOrder().setEstimatedEndTime(estimatedEndTime);
				list.removeLast();
				estimatedEndTime = estimatedEndTime.plusMinutes(calculateMaxPhase(list));
			}
		}
		
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
			for(Workstation workstation : workStations){
				if(workstation.getCurrentOrder()!=null)
					return false;
			}
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
		 * First updates the estimated end time of the workstations, then reschedules the pending car orders
		 * in a queue iteratively.
		 * @param phaseDuration The duration of the current phase of the assembly line.
		 */
//		private void updateEstimatedTimes(int phaseDuration) {
//			updateEstimatedTimeWorkStations(phaseDuration);
//			LinkedList<Order> copyOfQueue = makeCopyOfQueue();
//			queue.clear();
//			if(!copyOfQueue.isEmpty()){
//				for(Order next: copyOfQueue){
//					scheduleOrder(next);
//				}
//			}
//
//		}

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
		 * Updates the estimated time of the orders in the workstations, workstations without an order
		 * do not get updated. It considers the phaseDuration when updating the estimated end times.
		 * @param phaseDuration The time it took for the current phase to be completed.
		 */
//		private void updateEstimatedTimeWorkStations(int phaseDuration) {
//			firstWorkStation.updateEstimatedEndTimeCurrentOrder(phaseDuration);
//		}

		/**
		 * Schedules the given car order into the assembly line.
		 * If the first workstation is empty and there is time for a new car order, sets the order of the 
		 * first workstation to the given order. The estimated time is set to the curren time plus assembly time.
		 * Else if the queue of pending orders if empty, adds it and sets the estimated time to the currentime plus 
		 * the assembly time plus 1 hour. 
		 * Else adds the order at the end of the pending queue and looks at the previous estimated end time and adds
		 * 1 hour to it. 
		 * After the order is added to the workstation or to the queue of pending orders, and the estimated time
		 * is set, sets the start time of the order to the current time.
		 * @param order the new car order that needs to be scheduled.
		 */
//		private void scheduleOrder(Order order) {
//			DateTime estimatedEndTime = new DateTime(currentTime);
//			if(firstWorkStation.getCurrentOrder() == null && currentTime.getMinuteOfDay()<shiftEndHour*60-overTime-assemblyTime*60 && currentTime.getHourOfDay()>=shiftBeginHour && queue.isEmpty()){
//				firstWorkStation.setOrder(order);
//				estimatedEndTime = estimatedEndTime.plusHours(assemblyTime);
//			}else if(queue.isEmpty()){
//				queue.add(order);
//				estimatedEndTime = estimatedEndTime.plusHours(assemblyTime+1);
//				estimatedEndTime = getEstimatedTime(estimatedEndTime);
//			}else{
//				estimatedEndTime = new DateTime(queue.getLast().getEstimatedEndTime());
//				queue.add(order);
//				estimatedEndTime = estimatedEndTime.plusHours(1);
//				estimatedEndTime = getEstimatedTime(estimatedEndTime);
//			}
//			order.setEstimatedEndTime(estimatedEndTime);
//			DateTime startTime = new DateTime(currentTime);
//			order.setStartTime(startTime);
//		}

		private void scheduleOrder(Order order){
			DateTime startTime = new DateTime(currentTime);
			order.setStartTime(startTime);
			strategy.addOrder(order, queue);
			reschedule();
		}
		
		private void changeStrategy(Order order){
			if(order==null)
				strategy = new FifoStrategy();
			else
				strategy = new BatchSpecificationStrategy(order);
			LinkedList<Order> copy = makeCopyOfQueue();
			queue.clear();
			strategy.refactorQueue(queue,copy);
		}
		
		/**
		 * Returns the right estimated end time from the given possibly wrong estimated end time.
		 * @param estimatedEndTime The estimated end time that needs to be scheduled.
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
			returnList.add(firstWorkStation.getCurrentOrder());
			returnList.add(workStations.get(1).getCurrentOrder());
			return returnList;
		}

	}
}
