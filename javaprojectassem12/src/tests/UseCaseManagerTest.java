package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import logic.users.CarManufacturingCompany;

import org.joda.time.DateTime;
import org.junit.Test;

import controllers.AssemAssistController;
import controllers.ManagerController;

/**
 * A test case to test the use case of the manager. The other actors don't interact with the
 * system in this test case, but this is done in the UseCaseCombinedTest case.
 */
public class UseCaseManagerTest {
	
	/**
	 * 5.5 Use Case: Check Production Statistics
	 */
	@Test
	public void checkStatisticsTest() {
		AssemAssistController controller = new AssemAssistController(new CarManufacturingCompany());
		//Precondition: The manager is logged in
		ManagerController maCont = (ManagerController) controller.logIn("Wander");
		assertEquals("Wander", maCont.getUserName());
		//1. The user wants to check statistics about the production
		//2. The system shows a set of available statistics
		String stats = "";
		stats += "Statistics of Assembly Line 1:\n";
		stats += "Average number of cars produced: 0\n";
		stats += "Mean number of cars produced: 0\n";
		stats += "Exact numbers two last days:\n";
		stats += "   No records.\n";
		stats += "Average delay: 0 minutes\n";
		stats += "Mean delay: 0 minutes\n";
		stats += "Two last delays:\n";
		stats += "   No records.\n";
		stats += "\n";
		stats += "Statistics of Assembly Line 2:\n";
		stats += "Average number of cars produced: 0\n";
		stats += "Mean number of cars produced: 0\n";
		stats += "Exact numbers two last days:\n";
		stats += "   No records.\n";
		stats += "Average delay: 0 minutes\n";
		stats += "Mean delay: 0 minutes\n";
		stats += "Two last delays:\n";
		stats += "   No records.\n";
		stats += "\n";
		stats += "Statistics of Assembly Line 3:\n";
		stats += "Average number of cars produced: 0\n";
		stats += "Mean number of cars produced: 0\n";
		stats += "Exact numbers two last days:\n";
		stats += "   No records.\n";
		stats += "Average delay: 0 minutes\n";
		stats += "Mean delay: 0 minutes\n";
		stats += "Two last delays:\n";
		stats += "   No records.\n";
		stats += "\n";
		stats += "Statistics of Generality:\n";
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
	 * 5.6 Use-Case: Adapt Scheduling Algorithm.
	 */
	@Test
	public void adaptSchedulingTest(){
		CarManufacturingCompany cmc = new CarManufacturingCompany();
		AssemAssistController controller = new AssemAssistController(cmc);
		//Precondition: The manager is logged in
		ManagerController maCont = (ManagerController) controller.logIn("Wander");
		assertEquals("Wander", maCont.getUserName());
		//1. The user wants to select an alternative scheduling algorithm.
		maCont.setAssemblyLine("Assembly Line 1");
		//2. The system shows the available algorithms, as well as the currently selected algorithm.
		ArrayList<String> strats = new ArrayList<String>();
		strats.add("FIFO");
		strats.add("FIFO");
		strats.add("Specification Batch");
		//2(a) alternate flow: The user indicates that he wants to cancel the order and the use case ends here.
		assertEquals(strats,maCont.getStrategiesActiveLine());
		//3. The user selects the new scheduling algorithm to be used.
		assertFalse(maCont.changeToFIFOActiveLine()); //the system is already using FIFO
		//The system applies the new scheduling algorithm and updates its state accordingly.
		
		//3(a) alternate flow: The user indicates he wants to user the Specification Batch algorithm.
		ArrayList<String> batchList = new ArrayList<String>();
		//4. The system shows a list of the sets of vehicle options for which more than 3 orders are pending in the production queue.
		assertEquals(batchList,maCont.getBatchListActiveLine()); //there are currently no orders in the queue.
		//5. The user selects one of these sets for batch processing. (here empty)
		maCont.changeStrategyToBatchProcessingActiveLine(0);
		
		//This use case is tested extensively in UseCaseCombinedTest.java
	}
	
	/**
	 * 5.7 Use-Case: change the operational status of an assembly line.
	 */
	@Test
	public void changeStatusTest(){
		CarManufacturingCompany cmc = new CarManufacturingCompany();
		AssemAssistController controller = new AssemAssistController(cmc);
		//Precondition: The manager is logged in
		ManagerController maCont = (ManagerController) controller.logIn("Wander");
		assertEquals("Wander", maCont.getUserName());
		//1. The user wants to change the operational status of an assembly line.
		ArrayList<String> lines = new ArrayList<>();
		lines.add("Assembly Line 1: 1");
		lines.add("Assembly Line 2: 2");
		lines.add("Assembly Line 3: 3");
		assertEquals(lines,maCont.getAssemblyLines()); 
		maCont.setAssemblyLine("Assembly Line 1"); //the manager chooses which assembly line to change
		//2. The system shows the available statuses (operational, maintenance, broken), as wel as the current operational status.
		//2.a The user can cancel here, the use case then ends here
		assertEquals("OPERATIONAL" ,maCont.getCurrentAssemblyLineStatus());
		//3. The user selects the new operational status to be used.
		maCont.changeAssemblyLineStatus("BROKEN");
		//4.The system applies the new marks the new operational status of the assembly line.
		assertEquals("BROKEN" ,maCont.getCurrentAssemblyLineStatus());
		maCont.changeAssemblyLineStatus("OPERATIONAL");
		assertEquals("OPERATIONAL" ,maCont.getCurrentAssemblyLineStatus());
		maCont.changeAssemblyLineStatus("FOUT"); //changing the operational status to a non-existing status doesn't work
		assertEquals("OPERATIONAL" ,maCont.getCurrentAssemblyLineStatus());
		maCont.changeAssemblyLineStatus("MAINTENANCE");
		//Maintanace happens and the time is set forwards by four hours.
		assertEquals("OPERATIONAL" ,maCont.getCurrentAssemblyLineStatus());
		//quickly test the new time is correct.
		assertTrue(AssemblyLineTest.eqiDateTime(cmc.getCurrentTime(),new DateTime(2014,1,1,10,0)));
	}
}
