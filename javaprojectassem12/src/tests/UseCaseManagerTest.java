package tests;

import static org.junit.Assert.*;
import logic.users.CarManufacturingCompany;

import org.junit.Before;
import org.junit.Test;

import controllers.AssemAssistController;
import controllers.ManagerController;

public class UseCaseManagerTest {
	
	private CarManufacturingCompany company;
	private AssemAssistController controller;
	
	@Before
	public void prelude(){
		company = new CarManufacturingCompany();
		controller = new AssemAssistController(company);
	}

	
	@Test
	public void mainSuccesTest() {
		ManagerController maCont = (ManagerController) controller.logIn("Wander");
	}

}
