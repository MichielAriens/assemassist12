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
	
	public DateTime getCurrentTime() {
		return currentTime;
	}

	/**
	 * A variable containing the schedule, which is used for scheduling orders.
	 */
	private Schedule schedule;
	
	/**
	 * 
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
	 * Moves the car orders on the assembly line if every work station is ready and 
	 * sets the end time of the first order to the given end time.
	 * @param The time in minutes representing the duration of the current phase.
	 * @return True if the assembly line can be moved.
	 * @return False if the assembly line can not be moved.
	 */
	public boolean moveAssemblyLine(int shiftDuration){
		if(checkWorkStations()){
			
			this.currentTime = currentTime.plusMinutes(shiftDuration);
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
			schedule.updateEstimatedTimes(shiftDuration);
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
	

	public Workstation[] getWorkStations() {
		return workStations;
	}
	
	
	/**
	 * First
	 * @param order
	 */
	public void addCarOrder(CarOrder order){
		schedule.scheduleCarOrder(order);
	}
	
	public List<CarOrder> askFutureSchedule(){
		return schedule.getFutureSchedule();
	}
	
	/**
	 * 
	 *
	 */
	class Schedule {
		/**
		 * Hour start time of a sift in ideal circumstances.
		 */
		int shiftBeginHour = 6;
		
		/**
		 * Hour end time of a sift in ideal circumstances.
		 */
		int shiftEndHour = 22;
		
		/**
		 * Time in hours it takes to assemble a car in ideal circumstances.
		 */
		int assemblyTime = 3;
		
		int overTime = 0;
		

		private void calculateOverTime() {
			if(currentTime.getHourOfDay()<shiftBeginHour){
				overTime = 24-shiftEndHour+currentTime.getMinuteOfDay();
			}else{
				setOverTime((currentTime.getMinuteOfDay()-(shiftEndHour*60)));
			}
		}

		private void setNextDay() {
			if(currentTime.getHourOfDay()<shiftBeginHour)
				currentTime = new DateTime(2014,currentTime.getMonthOfYear(),currentTime.getDayOfMonth(),6,0);
			else{
				currentTime = currentTime.plusDays(1);
			currentTime = new DateTime(2014,currentTime.getMonthOfYear(),currentTime.getDayOfMonth(),6,0);
			}
			this.scheduleCarOrder(getNextOrder());
			
		}

		private void setOverTime(int i) {
			if(i<=0)
				overTime = 0;
			else
				overTime=i;
			
		}

		private boolean checkEndOfDay() {
			for(Workstation workstation : workStations){
				if(workstation.getCurrentOrder()!=null)
					return false;
			}
			if(currentTime.getMinuteOfDay()>=(shiftEndHour*60-overTime-assemblyTime) || currentTime.getMinuteOfDay()<shiftBeginHour*60)
				return true;
			return false;
		}

		private boolean timeForNewOrder(DateTime time) {
			if(Days.daysBetween(time, currentTime).getDays()==0){
				return time.getMinuteOfDay()<(shiftEndHour*60-overTime) && time.getMinuteOfDay()>=shiftBeginHour*60;
			}else{
				return  time.getMinuteOfDay()<(shiftEndHour*60) && time.getMinuteOfDay()>=shiftBeginHour*60;
			}
		}

		private void updateEstimatedTimes(int shiftduration) {
			updateEstimatedTimeWorkStations(shiftduration);
			LinkedList<CarOrder> copyOfQueue = makeCopyOfQueue();
			FIFOQueue.clear();
			if(!copyOfQueue.isEmpty()){
				for(CarOrder next: copyOfQueue){
					scheduleCarOrder(next);
				}
			}

		}

		private LinkedList<CarOrder> makeCopyOfQueue() {
			LinkedList<CarOrder> returnList = new LinkedList<CarOrder>();
			for(CarOrder next:FIFOQueue){
				returnList.add(next);
			}
			return returnList;
		}

		private void updateEstimatedTimeWorkStations(int shiftduration) {
			int time = shiftduration-60;
			for(Workstation next: workStations){
				if(next.getCurrentOrder()!= null)
					next.getCurrentOrder().setEstimatedEndTime(next.getCurrentOrder().getEstimatedEndTime().plusMinutes(time));
			}
			
		}

		private void scheduleCarOrder(CarOrder order) {
			DateTime estimatedEndTime = new DateTime(currentTime);
			if(workStations[0].getCurrentOrder() == null && currentTime.getMinuteOfDay()<shiftEndHour*60-overTime-assemblyTime*60 && currentTime.getHourOfDay()>=shiftBeginHour && FIFOQueue.isEmpty()){
				workStations[0].setOrder(order);
				estimatedEndTime = estimatedEndTime.plusHours(3);
				order.setEstimatedEndTime(estimatedEndTime);
				DateTime startTime = new DateTime(currentTime);
				order.setStartTime(startTime);
				return;
			}
			if(FIFOQueue.isEmpty()){
				FIFOQueue.add(order);
				estimatedEndTime = estimatedEndTime.plusHours(4);
				estimatedEndTime = canBeScheduledToday(estimatedEndTime);
				order.setEstimatedEndTime(estimatedEndTime);
				DateTime startTime = new DateTime(currentTime);
				order.setStartTime(startTime);
			}else{
				estimatedEndTime = new DateTime(FIFOQueue.getLast().getEstimatedEndTime());
				FIFOQueue.add(order);
				estimatedEndTime = estimatedEndTime.plusHours(1);
				estimatedEndTime = canBeScheduledToday(estimatedEndTime);
				order.setEstimatedEndTime(estimatedEndTime);
				DateTime startTime = new DateTime(currentTime);
				order.setStartTime(startTime);
			}
		}

		private DateTime canBeScheduledToday(DateTime estimatedEndTime) {
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
		 * Returns the next order to come on the assembly line if the assembly line is moved.
		 * If there is no next order it will return null.
		 */
		private CarOrder getNextOrder() {
			try{
				return FIFOQueue.remove();
			}catch(NoSuchElementException e){
				return null;
			}
			
			
		}
	
		private List<CarOrder> getFutureSchedule(){
			ArrayList<CarOrder> returnList = new ArrayList<CarOrder>();
			try{
				returnList.add(FIFOQueue.getFirst());
			}catch(NoSuchElementException e){
				returnList.add(null);
			}
			returnList.add(workStations[0].getCurrentOrder());
			returnList.add(workStations[1].getCurrentOrder());
			return returnList;
		}
	
	}
	
	
}
