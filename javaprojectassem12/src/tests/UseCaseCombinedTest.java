package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import logic.car.CarModel;
import logic.car.CarPartType;
import logic.users.CarManufacturingCompany;

import org.junit.Test;

import controllers.AssemAssistController;
import controllers.GarageHolderController;
import controllers.ManagerController;
import controllers.MechanicController;

/**
 * A test case to test the use case in which all users work together, the idea is to provide
 * a realistic simulation of the system use.
 */
public class UseCaseCombinedTest {
	
	/**
	 * The main test.
	 */
	@Test
	public void mainSuccesTest() {
		AssemAssistController controller = new AssemAssistController(new CarManufacturingCompany());
		
		//The garage holder adds a car order:
		//The garage holder logs in
		GarageHolderController ghCont = (GarageHolderController) controller.logIn("Michiel");
		assertEquals("Michiel", ghCont.getUserName());
		//the system prints the pending and completed orders (both are currently empty)
		assertEquals(new ArrayList<String>(), ghCont.getPendingOrders());
		assertEquals(new ArrayList<String>(), ghCont.getCompletedOrders());
		//if the user does not want to make a new order, the use case ends here (garage holder alternate flow 1)
		//the user fills in an ordering form on the user interface:
		ArrayList<String> models = new ArrayList<String>();
		models.add("Model1: 1");
		assertEquals(models, ghCont.getModels());
		ArrayList<String> bodyParts = new ArrayList<String>();
		bodyParts.add("Sedan: 1");
		bodyParts.add("Break: 2");
		assertEquals(bodyParts, ghCont.getOptions(CarPartType.Body, CarModel.MODEL1));
		ArrayList<String> form = new ArrayList<String>();
		form.add("Model1");
		form.add("Sedan");
		form.add("Red");
		form.add("Standard 2l 4 cilinders");
		form.add("6 speed manual");
		form.add("leather black");
		form.add("manual");
		form.add("comfort");
		//the user places the order:
		//if the user does not want to place the newly created order, the use case ends here (garage holder alternate flow 2)
		ghCont.placeOrder(form);
		//the system prints the pending and completed orders (pending orders is no longer empty)
		assertFalse(new ArrayList<String>().equals(ghCont.getPendingOrders()));
		assertEquals(1, ghCont.getPendingOrders().size());
		assertEquals(new ArrayList<String>(), ghCont.getCompletedOrders());
		//the user indicates that he doesn't want to place a new order and the garage holder main use case ends
		
		//The manager tries to forward the assembly line but it's not possible (manager alternate flow)
		//the manager logs in
		ManagerController maCont = (ManagerController) controller.logIn("Wander");
		assertEquals("Wander", maCont.getUserName());
		//the system shows the current and future status of the assembly line
		ArrayList<String> tasks = new ArrayList<String>();
		tasks.add("Car Body Post:\n   -Install Body= Sedan: Pending\n   -Install Colour= Red: Pending\n");
		tasks.add("Drive Train Post:\nInactive.\n");
		tasks.add("Accessories Post:\nInactive.\n");
		assertEquals(tasks, maCont.getTasksPerWorkstation());
		tasks.clear();
		tasks.add("Car Body Post:\nInactive.\n");
		tasks.add("Drive Train Post:\n   -Install Engine= Standard 2l 4 cilinders: Pending\n   -Install Gearbox= 6 speed manual: Pending\n");
		tasks.add("Accessories Post:\nInactive.\n");
		assertEquals(tasks, maCont.getFutureStatus());
		//the user confirms that he wants to move the assembly line forward.
		maCont.moveAssemblyLine(55); //the user enters the time in minutes spent at the current phase.
		//There are still pending tasks, so the assembly line can't be moved forward and the system prints the unfinished tasks.
		ArrayList<String> unfinished = new ArrayList<String>();
		unfinished.add("Car Body Post:\n   -Unfinished task: Install Body= Sedan\n   -Unfinished task: Install Colour= Red\n");
		assertEquals(unfinished, maCont.getUnfinishedTasks());
		//the user then indicates he wants to leave the overview and the manager use case alternate flow ends here
		
		//mechanic performs tasks on the car order.
		//mechanic logs in
		MechanicController mCont = (MechanicController) controller.logIn("Joren");
		assertEquals("Joren", mCont.getUserName());
		//the system prints out a list of workstations that the mechanic can choose from
		ArrayList<String> workstations = new ArrayList<String>();
		workstations.add("Car Body Post: 1");
		workstations.add("Drive Train Post: 2");
		workstations.add("Accessories Post: 3");
		assertEquals(workstations, mCont.getWorkStations());
		//the mechanic chooses the first work station
		mCont.setWorkStation("Car Body Post");
		//The system prints out a list of tasks that the mechanic can perform in this workstation
		tasks = new ArrayList<String>();
		tasks.add("Install Body= Sedan: 1");
		tasks.add("Install Colour= Red: 2");
		assertEquals(tasks,mCont.getTasks());
		//The mechanic chooses to perform the first task
		//The system shows information for this task and waits until the user indicates that he is done.
		String info = "Task description:\n   -Type of part needed: Body,\n   -Car Part: Sedan\n";
		assertEquals(info, mCont.getTaskInformation("Install Body= Sedan"));
		mCont.doTask("Install Body= Sedan");
		//The system asks if the user wants to perform another task, and the user answers with yes
		tasks.clear();
		tasks.add("Install Colour= Red: 1"); //now there will be only one available task
		assertEquals(tasks,mCont.getTasks());
		//The mechanic chooses to perform the first task
		//The system shows information for this task and waits until the user indicates that he is done.
		info = "Task description:\n   -Type of part needed: Colour,\n   -Car Part: Red\n";
		assertEquals(info, mCont.getTaskInformation("Install Colour= Red"));
		mCont.doTask("Install Colour= Red");
		//The system asks if the user wants to perform another task, and the user answers with no (mechanic alternate flow), the use case ends here
		
		//the manager successfully moves the assembly line forward
		//the manager logs in
		maCont = (ManagerController) controller.logIn("Wander");
		assertEquals("Wander", maCont.getUserName());
		//the system shows the current and future status of the assembly line
		tasks = new ArrayList<String>();
		tasks.add("Car Body Post:\n   -Install Body= Sedan: Completed\n   -Install Colour= Red: Completed\n");
		tasks.add("Drive Train Post:\nInactive.\n");
		tasks.add("Accessories Post:\nInactive.\n");
		assertEquals(tasks, maCont.getTasksPerWorkstation());
		tasks.clear();
		tasks.add("Car Body Post:\nInactive.\n");
		tasks.add("Drive Train Post:\n   -Install Engine= Standard 2l 4 cilinders: Pending\n   -Install Gearbox= 6 speed manual: Pending\n");
		tasks.add("Accessories Post:\nInactive.\n");
		assertEquals(tasks, maCont.getFutureStatus());
		//the user confirms that he wants to move the assembly line forward.
		maCont.moveAssemblyLine(55); //the user enters the time in minutes spent at the current phase.
		//The current status now equals the previous future status
		assertEquals(tasks, maCont.getTasksPerWorkstation());
		//the user then indicates he wants to leave the overview and the use case ends here
		
	}

}
