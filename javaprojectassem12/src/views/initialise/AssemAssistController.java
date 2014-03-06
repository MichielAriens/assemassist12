package views.initialise;

import logic.users.CarManufacturingCompany;
import logic.users.User;

public class AssemAssistController {
	private GUI gui;
	private CarManufacturingCompany company;
	public AssemAssistController(CarManufacturingCompany company) {
		this.company = company;
	}

	public void setGUI(GUI gui){
		if(gui!=null && gui.getController() == this ){
			this.gui = gui;
		}
		
	}

	public User logIn(String userName) {
		return company.logIn(userName);
	}
}
