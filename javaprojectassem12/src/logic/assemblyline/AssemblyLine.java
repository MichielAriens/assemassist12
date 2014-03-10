package logic.assemblyline;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.joda.time.DateTime;

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
	
	public Workstation[] getWorkStations() {
		return workStations;
	}

	/**
	 * Integer holding the number of work stations.
	 */
	private int numberOfWorkStations = 3;
	
	
	
	private LinkedList<CarOrder> FIFOQueue;
	
	/**
	 * 
	 */
	private DateTime currentTime;
	
	/**
	 * 
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
			if(schedule.timeForNewOrder())
				workStations[0].setOrder(schedule.getNextOrder());
			else
				workStations[0].setOrder(null);
			if(schedule.checkEndOfDay()){
				schedule.calculateOverTime();
				schedule.setNextDay();
			}
			schedule.updateEstimatedTimes();
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
			if(workStations[i].done()){
				return false;
			}
		}
		return true;
	}
	

	
	
	/**
	 * First
	 * @param order
	 */
	public void addCarOrder(CarOrder order){
		schedule.scheduleCarOrder(order);
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
				overTime = 12-shiftEndHour+currentTime.getMinuteOfDay();
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

		private boolean timeForNewOrder() {
			return currentTime.getMinuteOfDay()<(shiftEndHour*60-overTime-assemblyTime) && currentTime.getMinuteOfDay()>=shiftBeginHour*60;
		}

		private void updateEstimatedTimes() {
			updateEstimatedTimeWorkStations();
			for(int i = 0;i<FIFOQueue.size();i++){
				FIFOQueue.get(i).setEstimatedEndTime(FIFOQueue.get(0).getEstimatedEndTime().plusHours(4+i));
			}
		}

		private void updateEstimatedTimeWorkStations() {
			CarOrder next = workStations[0].getCurrentOrder();
			if(next!=null)
				next.setEstimatedEndTime(next.getEstimatedEndTime().plusHours(3));
			next = workStations[1].getCurrentOrder();
			if(next!=null)
				next.setEstimatedEndTime(next.getEstimatedEndTime().plusHours(2));
			next = workStations[2].getCurrentOrder();
			if(next!=null)
				next.setEstimatedEndTime(next.getEstimatedEndTime().plusHours(1));
		}

		private void scheduleCarOrder(CarOrder order) {
			FIFOQueue.add(order);
			DateTime estimatedEndTime = new DateTime(currentTime);
			estimatedEndTime = estimatedEndTime.plusHours(3+FIFOQueue.size());
			order.setEstimatedEndTime( new DateTime(currentTime));
			DateTime startTime = new DateTime(currentTime);
			order.setStartTime(startTime);
		}

		/**
		 * Returns the next order to come on the assembly line if the assembly line is moved.
		 * If there is no next order it will return null.
		 */
		private CarOrder getNextOrder() {
			try{
				return FIFOQueue.getFirst();
			}catch(NoSuchElementException e){
				return null;
			}
			
			
		}
		
		/**
		 * Returns an overview of the estimated end times for all the orders in the schedule.
		 * Returns "No orders planned." if there are no orders.
		 */
		public String getScheduleOverview(){
//			String overview = "";
//			if(FIFOQueue.size()<1 && !hasOrder()){
//				overview = "No orders planned.";
//			}
//			else{
//				for(int i = 0; i < FIFOQueue.size()-1; i++){
//					overview = overview +"Car order "+ (i+1)+": " + FIFOQueue.get(i).toString()+"\n";
//				}
//				overview = overview +"Car order "+FIFOQueue.size() + FIFOQueue.get(FIFOQueue.size()-1).toString();
//			}
//			return overview;
			
			
		}
		private boolean hasOrder(){
			for(Workstation workstation : workStations){
				if(workstation.getCurrentOrder()!=null)
					return true;
			}return false;
		}
//
//		/**
//		 * If the given order exists the estimated end time for the new order will be three hours
//		 * later than the end time of the given order. If that time is passed
//		 * 22:00 the estimated end time will be 09:00 the next day.
//		 * If there is no order on the first work station of the assembly line there are 
//		 * three options. First option is that the current time is before 06:00 so the estimated 
//		 * end time will be 09:00. Second option is that the current time is between 06:00 and 
//		 * 19:00 so the estimated end time will be three hours after the current time. Third option
//		 * is that the current time is past 19:00 so the estimated end time will be at 09:00 on the
//		 * next day.
//		 * @return
//		 */
//		public DateTime calculateEstimatedEndTimeForOrderAfterGivenOrder(CarOrder previousOrder){			
//			//If there is a current order on the first work station
//			if(previousOrder != null){
//				Calendar endTimePreviousOrder = previousOrder.getEstimatedEndTime();
//				Calendar endTimeNextOrder = Calendar.getInstance(); 
//				endTimeNextOrder = Calendar.getInstance();
//				endTimeNextOrder.setTime(endTimePreviousOrder.getTime());
//				endTimeNextOrder.add(Calendar.HOUR_OF_DAY, assemblyTime);
//
//				Calendar shiftEndTime = getGivenTimeOnGivenDay(endTimePreviousOrder, shiftEndHour, 0, 0);
//				if(endTimeNextOrder.before(shiftEndTime)){
//					return endTimeNextOrder;
//				}
//				else{
//					//Set time to endTimePreviousOrder because it could be the day has been changed
//					endTimeNextOrder.setTime(endTimePreviousOrder.getTime());
//					//Add 1 day
//					endTimeNextOrder.add(Calendar.DAY_OF_MONTH, 1);
//					//Set hour to shiftBeginHour + assemblyTime
//					endTimeNextOrder = getGivenTimeOnGivenDay(endTimeNextOrder, shiftBeginHour+assemblyTime, 0, 0);
//					return endTimeNextOrder;
//				}
//			}
//			
//			//If there is no current order on the first work station
//			else{
//				//Set endTimeNextOrder to current date and time
//				Calendar endTimeNextOrder = Calendar.getInstance();
//				
//				//Current date at shiftBeginTime
//				Calendar shiftBeginTime = getGivenTimeOnGivenDay(endTimeNextOrder, shiftBeginHour, 0, 0);
//				
//				//Current date at shiftEndTime
//				Calendar shiftEndTime = getGivenTimeOnGivenDay(endTimeNextOrder, shiftEndHour, 0, 0);
//				
//				//Check if endTimeNextOrder is after shiftBegintHour else it is at shigtBeginHour
//				if(endTimeNextOrder.after(shiftBeginTime)){
//					//Add assemblyTime hours to endTimeNextOrder
//					endTimeNextOrder.add(Calendar.HOUR_OF_DAY, assemblyTime);
//					//Check if endTimeNextOrder is before shiftEndTime else endTimeNextOrder is shiftBegintime + assemblyTime the next day
//					if(endTimeNextOrder.before(shiftEndTime)){
//						return endTimeNextOrder;
//					}
//					else{
//						//Add 1 day
//						endTimeNextOrder.add(Calendar.DAY_OF_MONTH, 1); 
//						//Set time to shiftBeginHour assemblyTime
//						endTimeNextOrder = getGivenTimeOnGivenDay(endTimeNextOrder, shiftBeginHour + assemblyTime, 0, 0); 
//						return endTimeNextOrder;
//					}
//				}
//				else{
//					endTimeNextOrder = getGivenTimeOnGivenDay(endTimeNextOrder, shiftBeginHour + assemblyTime, 0, 0);
//					return endTimeNextOrder;
//				}	
//			}	
//		}
//		
//		/**
//		 * If there is an order on the first work station of the assembly line the 
//		 * estimated end time for the new order will be three hours later. If that time is passed
//		 * 22:00 the estimated end time will be 09:00 the next day.
//		 * If there is no order on the first work station of the assembly line there are 
//		 * three options. First option is that the current time is before 06:00 so the estimated 
//		 * end time will be 09:00. Second option is that the current time is between 06:00 and 
//		 * 19:00 so the estimated end time will be three hours after the current time. Third option
//		 * is that the current time is past 19:00 so the estimated end time will be at 09:00 on the
//		 * next day.
//		 * @return
//		 */
//		public Calendar calculateEstimatedEndTimeForNewOrder(){	
//			int indexLastCarOrder = carOrders.size()-1;
//			CarOrder lastCarOrder = carOrders.get(indexLastCarOrder);
//			return calculateEstimatedEndTimeForOrderAfterGivenOrder(lastCarOrder);
//		}
//		
//		/**
//		 * Returns a calendar object on the given day with the given time in hours, minutes and seconds.
//		 */
//		public Calendar getGivenTimeOnGivenDay(Calendar givenDay, int hour, int minute, int second){
//			Calendar givenTimeOnGivenDay = Calendar.getInstance(); 
//			givenTimeOnGivenDay.setTime(givenDay.getTime());
//			givenTimeOnGivenDay.set(Calendar.HOUR_OF_DAY, hour);
//			givenTimeOnGivenDay.set(Calendar.MINUTE, minute);
//			givenTimeOnGivenDay.set(Calendar.SECOND, second);
//			return givenTimeOnGivenDay;
//		}
//		
	}
	
	
}
