package tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import logic.users.CarManufacturingCompany;

import org.junit.Test;

import controllers.AssemAssistController;
import controllers.ManagerController;

/**
 * A test case to test the use case of the manager. The other actors don't interact with the
 * system in this test case, but this is done in the UseCaseCombinedTest case.
 */
public class UseCaseManagerTest {
	
	/**
	 * The main test.
	 */
	@Test
	public void mainSuccesTest() {
		AssemAssistController controller = new AssemAssistController(new CarManufacturingCompany());
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
