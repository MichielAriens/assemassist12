package users;

import java.util.List;



import workstation.Shift;
import workstation.Workstation;

public class Mechanic {
	private Workstation activeStation = null;
	
	private List<Shift> shifts;
	
	public Mechanic(){
		
	}
	
	public Workstation getActiveWorkstation(){
		return this.activeStation;
	}
	
	public boolean isWorking(){
		return activeStation != null;
	}

	public void work(float actualWork) {
		// TODO Auto-generated method stub
		
	}

}
