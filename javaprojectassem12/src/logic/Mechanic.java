package logic;

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
