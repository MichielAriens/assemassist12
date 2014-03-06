package logic.users;

import java.util.List;





import logic.assemblyline.Shift;
import logic.workstation.Workstation;

public class Mechanic extends User{
	private Workstation activeStation = null;
	
	private List<Shift> shifts;
	
	public Mechanic(String userName){
		super(userName);
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

}
