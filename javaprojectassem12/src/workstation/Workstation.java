package workstation;

import java.util.List;

import car.CarPartType;


public abstract class Workstation {
	
	/**
	 * Get the types of tasks this workstation can perform. 
	 * @return
	 */
	abstract List<CarPartType> getDoableTasks();

}
