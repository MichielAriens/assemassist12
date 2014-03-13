package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

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
		//the manager logs in
		ManagerController maCont = (ManagerController) controller.logIn("Wander");
		assertEquals("Wander", maCont.getUserName());
		//the system shows the current and future status of the assembly line
		ArrayList<String> tasks = new ArrayList<String>();
		tasks.add("Car Body Post:\nInactive.\n");
		tasks.add("Drive Train Post:\nInactive.\n");
		tasks.add("Accessories Post:\nInactive.\n");
		assertEquals(tasks, maCont.getTasksPerWorkstation());
		assertEquals(tasks, maCont.getFutureStatus());
		//the user confirms that he wants to move the assembly line forward.
		maCont.moveAssemblyLine(55); //the user enters the time in minutes spent at the current phase.
		//since there were no car orders, the status hasn't changed.
		assertEquals(tasks, maCont.getTasksPerWorkstation());
		assertEquals(tasks, maCont.getFutureStatus());
		//the user then indicates he wants to leave the overview and the use case ends here
	}

}
