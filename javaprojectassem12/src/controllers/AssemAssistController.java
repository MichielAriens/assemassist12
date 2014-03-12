package controllers;

import views.UI;
import logic.users.CarManufacturingCompany;
import logic.users.GarageHolder;
import logic.users.Manager;
import logic.users.Mechanic;
import logic.users.User;

public class AssemAssistController {
	private UI ui;
	private CarManufacturingCompany company;
	private GarageHolderController ghController;
	private ManagerController maController;
	private MechanicController meController;
	public AssemAssistController(CarManufacturingCompany company) {
		this.company = company;
		this.ghController = new GarageHolderController();
		this.maController = new ManagerController();
		this.meController = new MechanicController();
	}

	public void setGUI(UI ui){
		if(ui!=null && ui.getController() == this ){
			this.ui = ui;
		}
		
	}

	public UserController logIn(String userName) {
		User user = company.logIn(userName);
		if(user instanceof GarageHolder){
			this.ghController.setGarageHolder((GarageHolder)user);
			return this.ghController;
		}
		else if(user instanceof Manager){
			this.maController.setManager((Manager)user);
			return this.maController;
		}
		else if(user instanceof Mechanic){
			this.meController.setMechanic((Mechanic)user);
			return this.meController;
		}
		return null;
	}
}
