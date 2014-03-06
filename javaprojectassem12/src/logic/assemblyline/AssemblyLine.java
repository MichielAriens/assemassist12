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
	
	
	public AssemblyLine(){
		schedule = new Schedule();
		workStations[0] = new CarBodyPost();
		workStations[1] = new DriveTrainPost();
		workStations[2] = new AccessoriesPost();
	}
	
	/**
	 * Moves the car orders on the assembly line if every work station is ready.
	 * @return True if the assembly line can be moved.
	 * @return False if the assembly line can not be moved.
	 */
	public boolean moveAssemblyLine(){
		for(int i = 0; i < numberOfWorkStations; i++){
			if(workStations[i].done()){
				return false;
			}
		}
		
		for(int i = numberOfWorkStations - 1; i > 0; i--){
			workStations[i].setOrder(workStations[i-1].getCurrentOrder());
			
		}
		workStations[0].setOrder(schedule.getNextOrder());
		
		return true;
	}
	
	public void addCarOrder(CarOrder order){
		carOrders.add(order);
	}

	public class Schedule {
		// TODO: Misschien variabelen maken voor de begintijd en eindtijd van een werk dag 
		
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
		 */
		public CarOrder getNextOrder() {
			return carOrders.get(numberOfWorkStations);
		}
		
		public Calendar calculateEstimatedEndTime(CarOrder carOrder){
			if(carOrders.size() > 1){ 
				Calendar endTimePreviousOrder = carOrders.get(carOrders.indexOf(carOrder) - 1).getEstimatedEndTime(); 

				Calendar endTimeNextOrder = Calendar.getInstance(); 
				endTimeNextOrder = Calendar.getInstance();
				endTimeNextOrder.setTime(endTimePreviousOrder.getTime());
				endTimeNextOrder.add(Calendar.HOUR_OF_DAY, 3); // 3 uur bijvoegen

				Calendar checkDate = Calendar.getInstance(); 
				checkDate.setTime(endTimePreviousOrder.getTime());
				checkDate.set(Calendar.HOUR_OF_DAY, 22);
				checkDate.set(Calendar.MINUTE, 0);
				if(endTimeNextOrder.before(checkDate)){
					return endTimeNextOrder;
				}
				else{
					endTimeNextOrder.setTime(endTimePreviousOrder.getTime());
					endTimeNextOrder.add(Calendar.DAY_OF_WEEK, 1); // add 1 day
					
					endTimeNextOrder.set(Calendar.HOUR_OF_DAY, 6);
					endTimeNextOrder.set(Calendar.MINUTE, 0);
					endTimeNextOrder.set(Calendar.SECOND, 0);
					
					endTimeNextOrder.add(Calendar.HOUR_OF_DAY, 3); // 3 uur bijvoegen
					return endTimeNextOrder;
				}
			}
			
			//Als de gegeven carorder de enige carorder is moet naar het huidige uur gekeken worden
			else{
				//Current date
				Calendar endTimeNextOrder = Calendar.getInstance();
				endTimeNextOrder.getTime();
				
				Calendar checkTime0600 = Calendar.getInstance(); 
				checkTime0600.setTime(endTimeNextOrder.getTime());
				checkTime0600.set(Calendar.HOUR_OF_DAY, 6);
				checkTime0600.set(Calendar.MINUTE, 0);
				checkTime0600.set(Calendar.SECOND, 0);
				
				Calendar checkTime2200 = Calendar.getInstance(); 
				checkTime2200.setTime(endTimeNextOrder.getTime());
				checkTime2200.set(Calendar.HOUR_OF_DAY, 22);
				checkTime2200.set(Calendar.MINUTE, 0);
				checkTime2200.set(Calendar.SECOND, 0);
				
				endTimeNextOrder.add(Calendar.HOUR_OF_DAY, 3); // 3 uur bijvoegen
				
				// nakijken of het tussen 6 en 8 is
				if(endTimeNextOrder.after(checkTime0600)){
					if(endTimeNextOrder.before(checkTime2200)){
						return endTimeNextOrder;
					}
					else{
						endTimeNextOrder.setTime(checkTime0600.getTime());
						endTimeNextOrder.add(Calendar.DAY_OF_WEEK, 1); // add 1 day
						return endTimeNextOrder;
					}
				}
				else{
					endTimeNextOrder.setTime(checkTime0600.getTime());
					return endTimeNextOrder;
				}	
			}	
		}
	}
}
