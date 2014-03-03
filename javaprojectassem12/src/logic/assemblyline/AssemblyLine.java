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
			workStations[i].setCurrentCarOrder(workStations[i-1].getCurrentCarOrder());
			
		}
		workStations[0].setCurrentCarOrder(eerst volgende car order terug geven)
		
		return true;
	}

	public class Schedule {
		public void getEstimatedCompletionTime(CarOrder order){
			order.getEstimatedEndTime();
		}
	}
}
