package logic.users;

import java.util.List;

import logic.car.CarOrder;
import logic.workstation.Workstation;

public class Manager extends User{
	private CarManufacturingCompany company;
	public Manager(CarManufacturingCompany comp, String UserName){
		super(UserName);
		this.company = comp;
	}
	
	public Workstation[] getWorkstations(){
		return this.company.getWorkStations();
	}
	
	public boolean moveAssemblyLine(int shiftDuration){
		return this.company.moveAssemblyLine(shiftDuration);
	}
	
	public List<CarOrder> askFutureSchedule(){
		return this.company.askFutureSchedule();
	}
}
