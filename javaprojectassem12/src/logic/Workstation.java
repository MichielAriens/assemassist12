package logic;

import java.util.List;

public abstract class Workstation {
	
	/**
	 * Get the types of tasks this workstation can perform. 
	 * @return
	 */
	abstract List<CarPartType> getDoableTasks();

}
