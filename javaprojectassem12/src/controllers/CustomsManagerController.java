package controllers;

import logic.users.CustomsManager;

public class CustomsManagerController extends UserController{
	
	/**
	 * The current customs shop manager.
	 */
	private CustomsManager currentCustomsManager;
	
	/**
	 * Sets the current customs shop manager to the given customs shop manager.
	 * @param mechan	The new mechanic.
	 */
	public void setCustomsManager(CustomsManager customsMan) {
		this.currentCustomsManager = customsMan;
	}
	
	/**
	 * Returns the user name of the current customs shop manager.
	 * @return 	Null if the current customs shop manager is null.
	 * 			The user name of the current customs shop manager otherwise.
	 */
	@Override
	public String getUserName() {
		return this.currentCustomsManager.getUserName();
	}

}
