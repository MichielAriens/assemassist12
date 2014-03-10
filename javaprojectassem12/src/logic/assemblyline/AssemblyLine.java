package logic.assemblyline;
import java.util.List;
import java.util.ArrayList;
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
			if(!workStations[i].done()){
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
				return FIFOQueue.remove();
			}catch(NoSuchElementException e){
				return null;
			}
			
			
		}
	
		public List<CarOrder> getFutureSchedule(){
			ArrayList<CarOrder> returnList = new ArrayList<CarOrder>();
			returnList.add(getNextOrder());
			returnList.add(workStations[0].getCurrentOrder());
			returnList.add(workStations[1].getCurrentOrder());
			return returnList;
		}
	
	}
	
	
}
