package logic.workstation;

import java.util.List;




import logic.car.CarOrder;
import logic.car.CarPartType;
import logic.users.Mechanic;




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
