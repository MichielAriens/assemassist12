package logic.assemblyline;


import java.util.ArrayList;
import java.util.List;

import logic.car.CarOrder;
import logic.car.CarPartType;
import logic.workstation.AccessoriesPost;
import logic.workstation.CarBodyPost;
import logic.workstation.DriveTrainPost;
import logic.workstation.Workstation;

/**
 * Class handeling an assembly line of a car manufacturing company.
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
		/**
		 * 
		 */
		private String standardBeginTime;
		
		/**
		 * 
		 */
		private String standardEndTime;
		
		/**
		 * 
		 * @param order
		 * @return
		 */
		public String getEstimatedCompletionTime(CarOrder order){
			return order.getEstimatedEndTime();
		}

		/**
		 * Returns the next order to come on the assembly line if the assembly line is moved.
		 */
		public CarOrder getNextOrder() {
			return carOrders.get(numberOfWorkStations);
		}
		
		public String calculateEstimatedEndTime(CarOrder carOrder){
			if(carOrders.get(carOrders.indexOf(carOrder) - 1).getEstimatedEndTime() != null);
			return "8008";
		}
	}
}
