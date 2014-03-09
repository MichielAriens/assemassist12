package views.initialise;

import java.util.ArrayList;

import logic.users.Mechanic;
import logic.workstation.Workstation;

public class MechanicController extends UserController{
	
	private Mechanic mechanic;
	
	public void setMechanic(Mechanic mechanic) {
		this.mechanic = mechanic;
	}

	@Override
	public String getUserName() {
		if(this.mechanic != null)
			return this.mechanic.getUserName();
		return null;
	}
	
	public ArrayList<String> getWorkStations(){
		if(this.mechanic == null)
			return null;
		ArrayList<String> workstations = new ArrayList<String>();
		Workstation[] stations = this.mechanic.getAvailableWorkstations();
		for(Workstation w : stations){
			workstations.add(w.toString());
		}
		return workstations;
	}
	
	public void setWorkStation(String name){
		if(this.mechanic == null)
			return;
		for(Workstation workstation :this.mechanic.getAvailableWorkstations()){
			if(workstation.toString().equals(name))
				this.mechanic.setActiveWorkstation(workstation);
		}
	}

}
