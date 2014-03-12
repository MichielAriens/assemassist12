package logic.assemblyline;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.joda.time.DateTime;
import org.joda.time.Days;

import logic.car.CarOrder;
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
	private Workstation[] workStations = new Workstation[3];

	/**
	 * Integer holding the number of work stations.
	 */
	private int numberOfWorkStations = workStations.length;

	/**
	 * A list holding the pending orders not on the assemblyline.
	 */
	private LinkedList<CarOrder> FIFOQueue;

	/**
	 * A dateTime object holding the current time of the system. 
	 */
	private DateTime currentTime;

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
		FIFOQueue= new LinkedList<CarOrder>();
		schedule = new Schedule();
		workStations[0] = new CarBodyPost();
		workStations[1] = new DriveTrainPost();
		workStations[2] = new AccessoriesPost();
		this.currentTime = new DateTime(2014, 1, 1, 6, 0);
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
	 * @return True if the assembly line can be moved.
	 * @return False if the assembly line can not be moved.
	 */
	public boolean moveAssemblyLine(int phaseDuration){
		if(phaseDuration < 0 || phaseDuration > 180)
				return false;
		
		if(checkWorkStations()){

			this.currentTime = currentTime.plusMinutes(phaseDuration);
			CarOrder firstOrder = workStations[numberOfWorkStations-1].getCurrentOrder();
			if(firstOrder!=null)
				firstOrder.setEndTime(currentTime);
			for(int i = numberOfWorkStations - 1; i > 0; i--){
				workStations[i].setOrder(workStations[i-1].getCurrentOrder());
			}
			workStations[0].setOrder(null);
			if(schedule.checkEndOfDay()){
				schedule.calculateOverTime();
				schedule.setNextDay();
			}
			schedule.updateEstimatedTimes(phaseDuration);
			//Needs to return true at the end because the assembly line can be moved.
			return true;
		}
		return false;
	}

	/**
	 * Checks if all the work stations are done.
	 * @return true if all work stations are done.
	 * @return false if one or more work stations are not done.
	 */
	private boolean checkWorkStations() {
		for(int i = 0; i < numberOfWorkStations; i++){
			if(!workStations[i].done()){
				return false;
			}
		}
		return true;
	}

	/**
	 * Asks a list of workstations available.
	 * @return	The list of workstations.
	 */
	public Workstation[] getWorkStations() {
		return workStations;
	}


	/**
	 * Calls the schedule and asks to schedule the given car order.
	 * @param order The car order to be scheduled.
	 */
	public void addCarOrder(CarOrder order){
		schedule.scheduleCarOrder(order);
	}

	/**
	 * Returns a list of 3 car orders which would be in the workstations 
	 * if the assembly is moved by the manager.
	 * @return A list of car orders which would be in the workstations 
	 * if the assembly line should move.
	 */
	public List<CarOrder> askFutureSchedule(){
		return schedule.getFutureSchedule();
	}

	/**
	 *A class which keeps the begin and end hour of the shift. Also holds the expected assembly time
	 *for a car order and the overtime made during the previous day. It calculates estimated end times
	 *and start time of car orders. It can also schedule new car orders into a queue containing 
	 *pending car orders.
	 */
	class Schedule {

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
				overTime = 24-shiftEndHour+currentTime.getMinuteOfDay();
			}else{
				setOverTime((currentTime.getMinuteOfDay()-(shiftEndHour*60)));
			}
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
		 * @return True if all workstations are done and it is later than the end hour of the shift 
		 * 		   minus the overtime or earlier than the begin hour of the shift.
		 * 			False otherwise.
		 */
		private boolean checkEndOfDay() {
			for(Workstation workstation : workStations){
				if(workstation.getCurrentOrder()!=null)
					return false;
			}
			if(currentTime.getMinuteOfDay()>=(shiftEndHour*60-overTime-assemblyTime) || currentTime.getMinuteOfDay()<shiftBeginHour*60)
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
			LinkedList<CarOrder> copyOfQueue = makeCopyOfQueue();
			FIFOQueue.clear();
			if(!copyOfQueue.isEmpty()){
				for(CarOrder next: copyOfQueue){
					scheduleCarOrder(next);
				}
			}

		}

		/**
		 * Makes a copy of the queue containing the pending orders.
		 * @return A list which is a copy of the queue containing the pending car orders.
		 * 		   An empty list if there are no pending orders.
		 */
		private LinkedList<CarOrder> makeCopyOfQueue() {
			LinkedList<CarOrder> returnList = new LinkedList<CarOrder>();
			for(CarOrder next:FIFOQueue){
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
			int time = phaseDuration-60;
			for(Workstation next: workStations){
				if(next.getCurrentOrder()!= null)
					next.getCurrentOrder().setEstimatedEndTime(next.getCurrentOrder().getEstimatedEndTime().plusMinutes(time));
			}
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
		private void scheduleCarOrder(CarOrder order) {
			DateTime estimatedEndTime = new DateTime(currentTime);
			if(workStations[0].getCurrentOrder() == null && currentTime.getMinuteOfDay()<shiftEndHour*60-overTime-assemblyTime*60 && currentTime.getHourOfDay()>=shiftBeginHour && FIFOQueue.isEmpty()){
				workStations[0].setOrder(order);
				estimatedEndTime = estimatedEndTime.plusHours(assemblyTime);
			}else if(FIFOQueue.isEmpty()){
				FIFOQueue.add(order);
				estimatedEndTime = estimatedEndTime.plusHours(assemblyTime+1);
				estimatedEndTime = getEstimatedTime(estimatedEndTime);
			}else{
				estimatedEndTime = new DateTime(FIFOQueue.getLast().getEstimatedEndTime());
				FIFOQueue.add(order);
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
		private List<CarOrder> getFutureSchedule(){
			ArrayList<CarOrder> returnList = new ArrayList<CarOrder>();

			try{
				if(FIFOQueue.getFirst().getEstimatedEndTime().getDayOfMonth()==currentTime.getDayOfMonth() && FIFOQueue.getFirst().getEstimatedEndTime().getHourOfDay()<shiftEndHour)
					returnList.add(FIFOQueue.getFirst());
				else returnList.add(null);
			}catch(NoSuchElementException e){
				returnList.add(null);
			}
			returnList.add(workStations[0].getCurrentOrder());
			returnList.add(workStations[1].getCurrentOrder());
			return returnList;
		}

	}


}
