package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import logic.car.CarModel;
import logic.car.CarPartType;
import logic.users.CarManufacturingCompany;
import logic.users.Mechanic;
import logic.workstation.Task;
import logic.workstation.Workstation;

import org.junit.Before;
import org.junit.Test;

import controllers.AssemAssistController;
import controllers.GarageHolderController;

/**
 * A test case to test the use case of the garage holder. The other actors don't interact with the
 * system in this test case, but this is done in the UseCaseCombinedTest case.
 */
public class UseCaseGarageHolderTest {
	CarManufacturingCompany cmc;
	AssemAssistController controller;
	GarageHolderController ghCont;
	
	/**
	 * We start by setting up an environment with assets commonly used by the test suite in the prequel.
	 * This contains a CarManufacturingCompany, an AssemAssistController and a GarageHolderController.
	 */
	@Before
	public void prequel() {
		cmc = new CarManufacturingCompany();
		controller = new AssemAssistController(cmc);
		ghCont = (GarageHolderController) controller.logIn("Jeroen");
	}
	
	/**
	 * The main test.
	 */
	@Test
	public void mainTest(){
		testOrderNewCar();
		populate();
		testCheckOrderDetails();
	}

	/**
	 * Test making and placing an order.
	 */
	private void testOrderNewCar() {
		//Precondition: The garage holder is successfully logged into the system.
		assertEquals("Jeroen", ghCont.getUserName());
		
		//The system prints the pending and completed orders (both are currently empty)
		assertEquals(new ArrayList<String>(), ghCont.getPendingOrders());
		assertEquals(new ArrayList<String>(), ghCont.getCompletedOrders());
		//if the user does not want to make a new order, the use case ends here (alternate flow 1)
		//the user fills in an ordering form on the user interface:
		ArrayList<String> models = new ArrayList<String>();
		models.add("Model A: 1");
		models.add("Model B: 2");
		models.add("Model C: 3");
		CarModel  model = CarModel.getModelFromString("Model A");
		ghCont.chooseModel(model);
		assertEquals(models, ghCont.getModels());
		ArrayList<String> bodyParts = new ArrayList<String>();
		bodyParts.add("Sedan: 1");
		bodyParts.add("Break: 2");
		
		assertEquals(bodyParts, ghCont.getOptions(CarPartType.Body));
		
		ghCont.addPart("Model A");
		ghCont.addPart("Sedan");
		ghCont.addPart("Red");
		ghCont.addPart("Standard 2l v4");
		ghCont.addPart("6 speed manual");
		ghCont.addPart("Leather black");
		ghCont.addPart("Manual");
		ghCont.addPart("Comfort");
		ghCont.addPart("No Spoiler");
		//user places the order:
		//if the user does not want to place the newly created order, the use case ends here (alternate flow 2)
		ghCont.placeOrder();
		//the system prints the pending and completed orders (pending orders is no longer empty)
		assertFalse(new ArrayList<String>().equals(ghCont.getPendingOrders()));
		assertEquals(1, ghCont.getPendingOrders().size());
		assertEquals(new ArrayList<String>(), ghCont.getCompletedOrders());
		//the user indicates that he doesn't want to place a new order and the use case ends
	}
	
	/**
	 * Test the checking of the order details.
	 */
	private void testCheckOrderDetails() {
		//1. The system presents an overview of the orders placed by the user...
		List<String> pendingList = ghCont.getPendingOrders();
		List<String> completeList = ghCont.getCompletedOrders();
		assertTrue(pendingList.size() == 1);
		assertTrue(completeList.size() == 1);
		
		//2. The user indicates the order he wants to check the details for. (we'll select the first option)
		//3. The system presents the order.
		assertTrue(ghCont.getCompletedInfo(0).contains("Specifications:   Model A; (Sedan, Red, Standard 2l v4, 6 speed manual, Leather black, Manual, Comfort, No Spoiler)"));
		assertTrue(ghCont.getCompletedInfo(0).contains("Start Time:       01-01-2014 06:00"));
		assertTrue(ghCont.getCompletedInfo(0).contains("End Time:         01-01-2014 09:00"));
	}

	/**
	 * Completes one order and adds another for testing purposes.
	 */
	private void populate() {
		completeOneOrder();
		addOrder();
	}
	
	/**
	 * Add one order for Model A
	 */
	private void addOrder(){
		CarModel  model = CarModel.getModelFromString("Model A");
		ghCont.chooseModel(model);
		ghCont.addPart("Model A");
		ghCont.addPart("Sedan");
		ghCont.addPart("Red");
		ghCont.addPart("Standard 2l v4");
		ghCont.addPart("6 speed manual");
		ghCont.addPart("Leather black");
		ghCont.addPart("Manual");
		ghCont.addPart("Comfort");
		ghCont.addPart("No Spoiler");
		ghCont.placeOrder();
	}
	
	/**
	 * Complete an order.
	 */
	private void completeOneOrder(){
		Mechanic mech = new Mechanic(cmc, "AutoMech2014");
		//Do all the orders
		for(Workstation	ws : cmc.getWorkStations()){
			mech.setActiveWorkstation(ws);
			for(Task task : mech.getAvailableTasks()){
				mech.doTask(task, 60);
			}
		}
	}
}
