package tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import logic.users.CarManufacturingCompany;

import org.junit.Test;

import controllers.AssemAssistController;
import controllers.MechanicController;

/**
 * A test case to test the use case of the mechanic. The other actors don't interact with the
 * system in this test case, but this is done in the UseCaseCombinedTest case.
 */
public class UseCaseMechanicTest {

	/**
	 * Use Case 5.3: Test performing assembly tasks.
	 */
	@Test
	public void performTaskTest() {
		AssemAssistController controller = new AssemAssistController(new CarManufacturingCompany());

		//The mechanic logs in.
		MechanicController mCont = (MechanicController) controller.logIn("Joren");
		assertEquals("Joren", mCont.getUserName());
		//the system prints out a list of assembly lines that the mechanic can choose from
		ArrayList<String> assemblyLines = new ArrayList<String>();
		assemblyLines.add("Assembly Line 1: 1");
		assemblyLines.add("Assembly Line 2: 2");
		assemblyLines.add("Assembly Line 3: 3");
		assertEquals(assemblyLines, mCont.getAssemblyLines());
		mCont.setAssemblyLine("Assembly Line 1");
		//the system prints out a list of workstations that the mechanic can choose from
		ArrayList<String> workstations = new ArrayList<String>();
		workstations.add("Car Body Post: 1");
		workstations.add("Drive Train Post: 2");
		workstations.add("Accessories Post: 3");
		assertEquals(workstations, mCont.getWorkStationsFromAssemblyLine());
		//the mechanic chooses the first work station
		mCont.setWorkStation("Car Body Post");
		//The system prints out a list of tasks that the mechanic can perform in this workstation
		//This list is empty because the work station is currently inactive.
		ArrayList<String> tasks = new ArrayList<String>();
		assertEquals(tasks,mCont.getTasks());
		//The system asks whether the user wants to perform another task, but the user answers no (alternate flow)
		//The use case ends here.
	}

	/**
	 * Use case 5.4: Check Assembly Line Status.
	 */
	@Test
	public void checkAssemblyLineTest(){
		AssemAssistController controller = new AssemAssistController(new CarManufacturingCompany());

		//The mechanic logs in.
		MechanicController mCont = (MechanicController) controller.logIn("Joren");
		assertEquals("Joren", mCont.getUserName());
		//the system prints out a list of assembly lines that the mechanic can choose from
		ArrayList<String> assemblyLines = new ArrayList<String>();
		assemblyLines.add("Assembly Line 1: 1");
		assemblyLines.add("Assembly Line 2: 2");
		assemblyLines.add("Assembly Line 3: 3");
		assertEquals(assemblyLines, mCont.getAssemblyLines());
		//1. The user wants to check the current status of the assembly line.
		mCont.setAssemblyLine("Assembly Line 1");
		//2. The system presents an overview of the current assembly line status, including pending and finished
		//	 tasks at each work post.
		ArrayList<String> tasks = new ArrayList<String>();
		tasks.add("= Assembly Line 1 [OPERATIONAL] =");
		tasks.add(" Car Body Post:\n   Inactive.\n");
		tasks.add(" Drive Train Post:\n   Inactive.\n");
		tasks.add(" Accessories Post:\n   Inactive.\n");
		tasks.add("= Assembly Line 2 [OPERATIONAL] =");
		tasks.add(" Car Body Post:\n   Inactive.\n");
		tasks.add(" Drive Train Post:\n   Inactive.\n");
		tasks.add(" Accessories Post:\n   Inactive.\n");
		tasks.add("= Assembly Line 3 [OPERATIONAL] =");
		tasks.add(" Car Body Post:\n   Inactive.\n");
		tasks.add(" Cargo Post:\n   Inactive.\n");
		tasks.add(" Drive Train Post:\n   Inactive.\n");
		tasks.add(" Accessories Post:\n   Inactive.\n");
		tasks.add(" Certification Post:\n   Inactive.\n");
		assertEquals(tasks, mCont.getTasksPerWorkstation());
	}
}