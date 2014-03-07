package views.initialise;

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

}
