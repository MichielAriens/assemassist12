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
	public void progressTime(int phaseDuration){
		currentTime = currentTime.plusMinutes(phaseDuration);
		if(currentTime.getHourOfDay()>=22 || currentTime.getHourOfDay()<6){
			schedule.setNextDay();
			
		}
		schedule.updateEstimatedTimes(phaseDuration);
	}

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
		 * Time in hours it takes to assemble a car in normal circumstances.
		 */
		int assemblyTime = 3;

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

			currentTime = currentTime.plusMinutes(phaseDuration);
			Order firstOrder = workStations.get(numberOfWorkStations-1).getCurrentOrder();
			if(firstOrder!=null)
				firstOrder.setEndTime(currentTime);
			
			firstWorkStation.advanceWorkstations(null);
			if(checkEndOfDay()){
				calculateOverTime();
				setNextDay();
			}
			schedule.updateEstimatedTimes(phaseDuration);
			return true;
		}
		
		private void reschedule(){
			rescheduleWorkstations();
		}

		private void rescheduleWorkstations(){
			LinkedList<Order> list = new LinkedList<Order>();
			workStations.getLast().getCurrentOrder().setEstimatedEndTime(calculateMaxPhase());
		}
		
		private int calculateMaxPhase(List<Order> orders){
			
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
			if(currentTime.getMinuteOfDay()>=(shiftEndHour * 60-overTime - assemblyTime * 60) || currentTime.getMinuteOfDay()<shiftBeginHour*60)
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
		private void updateEstimatedTimes(int phaseDuration) {
			updateEstimatedTimeWorkStations(phaseDuration);
			LinkedList<Order> copyOfQueue = makeCopyOfQueue();
			queue.clear();
			if(!copyOfQueue.isEmpty()){
				for(Order next: copyOfQueue){
					scheduleOrder(next);
				}
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
		 * Updates the estimated time of the orders in the workstations, workstations without an order
		 * do not get updated. It considers the phaseDuration when updating the estimated end times.
		 * @param phaseDuration The time it took for the current phase to be completed.
		 */
		private void updateEstimatedTimeWorkStations(int phaseDuration) {
			firstWorkStation.updateEstimatedEndTimeCurrentOrder(phaseDuration);
		}

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
		private void scheduleOrder(Order order) {
			DateTime estimatedEndTime = new DateTime(currentTime);
			if(firstWorkStation.getCurrentOrder() == null && currentTime.getMinuteOfDay()<shiftEndHour*60-overTime-assemblyTime*60 && currentTime.getHourOfDay()>=shiftBeginHour && queue.isEmpty()){
				firstWorkStation.setOrder(order);
				estimatedEndTime = estimatedEndTime.plusHours(assemblyTime);
			}else if(queue.isEmpty()){
				queue.add(order);
				estimatedEndTime = estimatedEndTime.plusHours(assemblyTime+1);
				estimatedEndTime = getEstimatedTime(estimatedEndTime);
			}else{
				estimatedEndTime = new DateTime(queue.getLast().getEstimatedEndTime());
				queue.add(order);
				estimatedEndTime = estimatedEndTime.plusHours(1);
				estimatedEndTime = getEstimatedTime(estimatedEndTime);
			}
			order.setEstimatedEndTime(estimatedEndTime);
			DateTime startTime = new DateTime(currentTime);
			order.setStartTime(startTime);
		}

		/**
		 * Returns the right estimated end time from the given possibly wrong estimated end time.
		 * @param estimatedEndTime The estimated end time that needs to be scheduled.
		 * @return	The renewed estimated end time if it was scheduled wrong before. 
		 */
		private DateTime getEstimatedTime(DateTime estimatedEndTime) {
			if(!timeForNewOrder(estimatedEndTime)){
				if(estimatedEndTime.getHourOfDay()<=shiftBeginHour){
					return new DateTime(estimatedEndTime.getYear(),estimatedEndTime.getMonthOfYear(),estimatedEndTime.getDayOfMonth(),shiftBeginHour+assemblyTime,0);

				}else{
					estimatedEndTime =  new DateTime(estimatedEndTime.getYear(),estimatedEndTime.getMonthOfYear(),estimatedEndTime.getDayOfMonth(),shiftBeginHour+assemblyTime,0);
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
