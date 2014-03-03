package workstation;

import java.util.List;


import users.Mechanic;


import car.CarOrder;
import car.CarPartType;


public abstract class Workstation {
	
	private CarOrder currentOrder;
	
	public Workstation(){
		currentOrder = null;
	}
	
	
	/**
	 * Get the types of tasks this workstation can perform. 
	 * @return
	 */
	abstract List<CarPartType> getCapabilities();
	
	
	
	
	public void doTask(Task task, Mechanic mechanic){
		
	}


	List<CarPartType> getDoableTasks() {
		
		return null;
	}

}
