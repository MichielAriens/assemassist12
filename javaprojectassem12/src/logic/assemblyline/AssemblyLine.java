package logic.assemblyline;
import java.util.ArrayList;
import java.util.Calendar;

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
	private int numberOfWorkStations = 3;
	
	/**
	 * List containing the pending car orders.
	 */
	private ArrayList<CarOrder> carOrders = new ArrayList<CarOrder>();
	
	/**
	 * 
	 */
	private Schedule schedule;
	
	/**
	 * 
	 */
	public AssemblyLine(){
		schedule = new Schedule();
		workStations[0] = new CarBodyPost();
		workStations[1] = new DriveTrainPost();
		workStations[2] = new AccessoriesPost();
	}
	
	/**
	 * Moves the car orders on the assembly line if every work station is ready and 
	 * sets the end time of the first order to the given end time.
	 * @return True if the assembly line can be moved.
	 * @return False if the assembly line can not be moved.
	 */
	public boolean moveAssemblyLine(Calendar endTime){
		if(checkWorkStations()){
			CarOrder firstOrder = workStations[numberOfWorkStations-1].getCurrentOrder();
			firstOrder.setEndTime(endTime);
			for(int i = numberOfWorkStations - 1; i > 0; i--){
				workStations[i].setOrder(workStations[i-1].getCurrentOrder());
			}

			workStations[0].setOrder(schedule.getNextOrder());

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
	 * Moves the car orders on the assembly line if every work station is ready and 
	 * sets the end time of the first order to the given end time.
	 * @return True if the assembly line can be moved.
	 * @return False if the assembly line can not be moved.
	 */
//	public boolean moveAssemblyLine(Calendar endTime){
//		checkWorkStations();
//		workStations[numberOfWorkStations-1].getCurrentOrder().setEndTime(endTime);
//		for(int i = numberOfWorkStations - 1; i > 0; i--){
//			workStations[i].setOrder(workStations[i-1].getCurrentOrder());
//		}
//		
//		workStations[0].setOrder(schedule.getNextOrder());
//		
//		//Needs to return true at the end because the assembly line can be moved.
//		return true;
//	}
	
	/**
	 * 
	 * @param order
	 * @return
	 */
	public Calendar calculateEstimatedEndTimeForNewOrder(){
		return schedule.calculateEstimatedEndTimeForNewOrder();
	}
	
	/**
	 * First
	 * @param order
	 */
	public void addCarOrder(CarOrder order){
		schedule.calculateEstimatedEndTimeForNewOrder();
		order.setEstimatedEndTime(schedule.calculateEstimatedEndTimeForNewOrder());
		carOrders.add(order);
	}

	/**
	 * 
	 *
	 */
	public class Schedule {
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
		
		/**
		 * 
		 * @param order
		 * @return
		 */
		public Calendar getEstimatedCompletionTime(CarOrder order){
			return order.getEstimatedEndTime();
		}

		/**
		 * Returns the next order to come on the assembly line if the assembly line is moved.
		 * If there is no next order it will return null.
		 */
		public CarOrder getNextOrder() {
			int numberOfOrdersOnTheLine = 0;
			for(int i = 0; i < workStations.length; i++){
				if(workStations[i].getCurrentOrder() != null){
					numberOfOrdersOnTheLine++;
				}
			}
			
			if(carOrders.size() > numberOfOrdersOnTheLine){
				return carOrders.get(numberOfOrdersOnTheLine);
			}
			else{
				return null;
			}
		}
		
		/**
		 * If the given order exists the estimated end time for the new order will be three hours
		 * later than the end time of the given order. If that time is passed
		 * 22:00 the estimated end time will be 09:00 the next day.
		 * If there is no order on the first work station of the assembly line there are 
		 * three options. First option is that the current time is before 06:00 so the estimated 
		 * end time will be 09:00. Second option is that the current time is between 06:00 and 
		 * 19:00 so the estimated end time will be three hours after the current time. Third option
		 * is that the current time is past 19:00 so the estimated end time will be at 09:00 on the
		 * next day.
		 * @return
		 */
		public Calendar calculateEstimatedEndTimeForOrderAfterGivenOrder(CarOrder previousOrder){			
			//If there is a current order on the first work station
			if(previousOrder != null){
				Calendar endTimePreviousOrder = previousOrder.getEstimatedEndTime();
				Calendar endTimeNextOrder = Calendar.getInstance(); 
				endTimeNextOrder = Calendar.getInstance();
				endTimeNextOrder.setTime(endTimePreviousOrder.getTime());
				endTimeNextOrder.add(Calendar.HOUR_OF_DAY, assemblyTime);

				Calendar shiftEndTime = getGivenTimeOnGivenDay(endTimePreviousOrder, shiftEndHour, 0, 0);
				if(endTimeNextOrder.before(shiftEndTime)){
					return endTimeNextOrder;
				}
				else{
					//Set time to endTimePreviousOrder because it could be the day has been changed
					endTimeNextOrder.setTime(endTimePreviousOrder.getTime());
					//Add 1 day
					endTimeNextOrder.add(Calendar.DAY_OF_MONTH, 1);
					//Set hour to shiftBeginHour + assemblyTime
					endTimeNextOrder = getGivenTimeOnGivenDay(endTimeNextOrder, shiftBeginHour+assemblyTime, 0, 0);
					return endTimeNextOrder;
				}
			}
			
			//If there is no current order on the first work station
			else{
				//Set endTimeNextOrder to current date and time
				Calendar endTimeNextOrder = Calendar.getInstance();
				
				//Current date at shiftBeginTime
				Calendar shiftBeginTime = getGivenTimeOnGivenDay(endTimeNextOrder, shiftBeginHour, 0, 0);
				
				//Current date at shiftEndTime
				Calendar shiftEndTime = getGivenTimeOnGivenDay(endTimeNextOrder, shiftEndHour, 0, 0);
				
				//Check if endTimeNextOrder is after shiftBegintHour else it is at shigtBeginHour
				if(endTimeNextOrder.after(shiftBeginTime)){
					//Add assemblyTime hours to endTimeNextOrder
					endTimeNextOrder.add(Calendar.HOUR_OF_DAY, assemblyTime);
					//Check if endTimeNextOrder is before shiftEndTime else endTimeNextOrder is shiftBegintime + assemblyTime the next day
					if(endTimeNextOrder.before(shiftEndTime)){
						return endTimeNextOrder;
					}
					else{
						//Add 1 day
						endTimeNextOrder.add(Calendar.DAY_OF_MONTH, 1); 
						//Set time to shiftBeginHour assemblyTime
						endTimeNextOrder = getGivenTimeOnGivenDay(endTimeNextOrder, shiftBeginHour + assemblyTime, 0, 0); 
						return endTimeNextOrder;
					}
				}
				else{
					endTimeNextOrder = getGivenTimeOnGivenDay(endTimeNextOrder, shiftBeginHour + assemblyTime, 0, 0);
					return endTimeNextOrder;
				}	
			}	
		}
		
		/**
		 * If there is an order on the first work station of the assembly line the 
		 * estimated end time for the new order will be three hours later. If that time is passed
		 * 22:00 the estimated end time will be 09:00 the next day.
		 * If there is no order on the first work station of the assembly line there are 
		 * three options. First option is that the current time is before 06:00 so the estimated 
		 * end time will be 09:00. Second option is that the current time is between 06:00 and 
		 * 19:00 so the estimated end time will be three hours after the current time. Third option
		 * is that the current time is past 19:00 so the estimated end time will be at 09:00 on the
		 * next day.
		 * @return
		 */
		public Calendar calculateEstimatedEndTimeForNewOrder(){	
			int indexLastCarOrder = carOrders.size()-1;
			CarOrder lastCarOrder = carOrders.get(indexLastCarOrder);
			return calculateEstimatedEndTimeForOrderAfterGivenOrder(lastCarOrder);
		}
		
		/**
		 * Returns a calendar object on the given day with the given time in hours, minutes and seconds.
		 */
		public Calendar getGivenTimeOnGivenDay(Calendar givenDay, int hour, int minute, int second){
			Calendar givenTimeOnGivenDay = Calendar.getInstance(); 
			givenTimeOnGivenDay.setTime(givenDay.getTime());
			givenTimeOnGivenDay.set(Calendar.HOUR_OF_DAY, hour);
			givenTimeOnGivenDay.set(Calendar.MINUTE, minute);
			givenTimeOnGivenDay.set(Calendar.SECOND, second);
			return givenTimeOnGivenDay;
		}
		
		/**
		 * Returns an overview of the estimated end times for all the orders in the schedule.
		 * Returns "No orders planned." if there are no orders.
		 */
		public String getScheduleOverview(){
			String overview = "";
			if(carOrders.size()<1){
				overview = "No orders planned.";
			}
			else{
				for(int i = 0; i < carOrders.size()-1; i++){
					overview = overview +"Car order "+ (i+1)+": " + carOrders.get(i).toString()+"\n";
				}
				overview = overview +"Car order "+ carOrders.size() + carOrders.get(carOrders.size()-1).toString();
			}
			return overview;
		}
	}
}
