package views.initialise;

import java.util.ArrayList;

import logic.users.Mechanic;

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
		//vraag mechanic class voor lijst van workstations, mechanic vraagt volledige lijst aan carManufacturingCompany
		//zo kunnen we in de toekomst ook eenvoudig uitbreiden naar mechanics die niet aan alle workstations mogen werken
		
		//vorm deze lijst om naar strings en return
		return null;
	}
	
	public void setWorkStation(String name){
		//vraag mechanic om de lijst van workstations, kies workstation dat overeen komt met de naam en roep
		//laat de mechanic zijn current
	}

}
