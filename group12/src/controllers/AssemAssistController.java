package controllers;

import logic.users.CarManufacturingCompany;
import logic.users.CustomsManager;
import logic.users.GarageHolder;
import logic.users.Manager;
import logic.users.Mechanic;
import logic.users.User;

/**
 * The controller that offers a UI the methods to let a user log in to the AssemAssist system.
 */
public class AssemAssistController {
	/**
	 * The car manufacturing company.
	 */
	private CarManufacturingCompany company;
	
	/**
	 * The controller for garage holders.
	 */
	private GarageHolderController ghController;
	
	/**
	 * The controller for managers.
	 */
	private ManagerController maController;
	
	/**
	 * The controller for mechanics.
	 */
	private MechanicController meController;
	
	/**
	 * The controller for customs shop managers.
	 */
	private CustomsManagerController cuController;
	
	/**
	 * Creates a new AssemAssistController with the given car manufacturing company.
	 * @param company	A car manufacturing company. 
	 */
	public AssemAssistController(CarManufacturingCompany company) {
		this.company = company;
		this.ghController = new GarageHolderController();
		this.maController = new ManagerController();
		this.meController = new MechanicController();
		this.cuController = new CustomsManagerController();
	}

	/**
	 * Checks if the given user is part of the system and returns the controller corresponding
	 * to the user.
	 * @param userName	The user name of the user who wants to log in.
	 * @return 	Null if the user name is not in the system.
	 * 			The controller corresponding to the user otherwise.
	 */
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
		else if(user instanceof CustomsManager){
			this.cuController.setCustomsManager((CustomsManager)user);
			return this.cuController;
		}
		return null;
	}
}
