package logic.users;

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
}
