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
	 * 4.5 Use Case: Check Production Statistics
	 */
	@Test
	public void mainSuccesTest() {
		AssemAssistController controller = new AssemAssistController(new CarManufacturingCompany());
		//Precondition: The manager is logged in
		ManagerController maCont = (ManagerController) controller.logIn("Wander");
		assertEquals("Wander", maCont.getUserName());
		//1. The user wants to check statistics about the production
		//2. The system shows a set of available statistics
		String stats = "";
		stats += "Average number of cars produced: 0\n";
		stats += "Mean number of cars produced: 0\n";
		stats += "Exact numbers two last days:\n";
		stats += "   No records.\n";
		
		stats += "Average delay: 0 minutes\n";
		stats += "Mean delay: 0 minutes\n";
		stats += "Two last delays:\n";
		stats += "   No records.\n";
		assertEquals(stats, maCont.getStatistics());
		//3. The user then indicates he wants to leave the overview and the use case ends here
	}
	
	/**
	 * The test for use-case 4.6: Adapt Scheduling Algorithm is covered in UseCaseCombinedTest.java
	 */
}
