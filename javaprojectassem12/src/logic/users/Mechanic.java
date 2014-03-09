package logic.users;

import java.util.List;





import logic.assemblyline.Shift;
import logic.workstation.Workstation;

public class Mechanic extends User{
	
	private Workstation activeStation = null;
	private CarManufacturingCompany company;
	private List<Shift> shifts;
	
	public Mechanic(CarManufacturingCompany comp, String userName){
		super(userName);
		this.company = comp;
	}
	
	public Workstation getActiveWorkstation(){
		return this.activeStation;
	}
	
	public boolean isPosted(){
		return this.getActiveWorkstation() != null;
	}

	public void work(float actualWork) {
		//Boekhouding
		
		
	}
	
	public Workstation[] getAvailableWorkstations(){
		return this.company.getWorkStations();
	}
	
	public void setActiveWorkstation(Workstation station){
		this.activeStation = station;
	}

}
