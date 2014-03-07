package views.initialise;

import logic.users.Manager;

public class ManagerController extends UserController{

	private Manager manager;
	
	public void setManager(Manager manager) {
		this.manager = manager;
	}

	@Override
	public String getUserName() {
		if(this.manager != null)
			return manager.getUserName();
		return null;
	}

}
