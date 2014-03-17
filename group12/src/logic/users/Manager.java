package logic.users;

import java.util.List;

import logic.car.CarOrder;
import logic.workstation.Workstation;

/**
 * Class used to describe a manager working for a car manufacturing company.
 */
public class Manager extends User{
	
	/**
	 * The manufacturing company for which the garage holder works.
	 */
	private CarManufacturingCompany company;
	
	/**
	 * Constructs a new manager initializing its company and user name with the given 
	 * parameters.
	 * @param company	The car manufacturing company the new manager works for.
	 * @param userName	The user name for this new manager.
	 */
	public Manager(CarManufacturingCompany company, String userName){
		super(userName);
		this.company = company;
	}
	
	/**
	 * Returns the workstations of the car manufacturing company.
	 * @return The workstations of the car manufacturing company.
	 */
	public Workstation[] getWorkstations(){
		return this.company.getWorkStations();
	}
	
	/**
	 * Moves the assembly line.
	 * @param phaseDuration	The duration spent on the current phase.
	 * @return 	True if the assembly line has been moved.
	 * 			False if the assembly line can not be moved.
	 */
	public boolean moveAssemblyLine(int phaseDuration){
		return this.company.moveAssemblyLine(phaseDuration);
	}
	
	/**
	 * Returns the list of car orders in the schedule as it would be if the assembly line has been moved once.
	 * @return the list of car orders in the schedule as it would be if the assembly line has been moved once.
	 */
	public List<CarOrder> askFutureSchedule(){
		return this.company.askFutureSchedule();
	}
}
