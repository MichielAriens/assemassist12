package views.initialise;

import logic.users.CarManufacturingCompany;

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
}
