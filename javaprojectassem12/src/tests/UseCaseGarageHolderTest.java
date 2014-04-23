package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import logic.car.CarModel;
import logic.car.CarOrder;
import logic.car.CarOrderDetailsMaker;
import logic.car.CarPart;
import logic.car.CarPartType;
import logic.car.Order;
import logic.users.CarManufacturingCompany;
import logic.users.Mechanic;
import logic.workstation.Task;

import org.junit.Before;
import org.junit.Test;

import controllers.*;

/**
 * A test case to test the use case of the garage holder. The other actors don't interact with the
 * system in this test case, but this is done in the UseCaseCombinedTest case.
 */
public class UseCaseGarageHolderTest {
	CarManufacturingCompany cmc;
	AssemAssistController controller;
	GarageHolderController ghCont;
	List<Mechanic> mechs;
	List<Order> orders;	
	
	
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
	public void testOrderNewCar() {
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
		System.out.println(ghCont.getPendingOrders());
		assertFalse(new ArrayList<String>().equals(ghCont.getPendingOrders()));
		assertEquals(1, ghCont.getPendingOrders().size());
		assertEquals(new ArrayList<String>(), ghCont.getCompletedOrders());
		//the user indicates that he doesn't want to place a new order and the use case ends
	}
	

	private void testCheckOrderDetails() {
		//Start by populating the system with some orders.
		doSomeWork(10, 5);
		//The system presents an overview.
		ghCont.getCompletedOrders();
	}
	
	
	/**
	 * Set up an environment where: 
	 * 		n orders have been submitted
	 * 		n/2 orders have been done
	 * 		each order has duration 60.
	 */
	public void doSomeWork(int total, int toDo){
		//initialize the required actors
		for(int i = 0; i < 3; i++){
			mechs.add(new Mechanic(cmc, "SuperMech2014"));
			mechs.get(i).setActiveWorkstation(cmc.getWorkStations().get(i));
		}
		
		//Build n orders
		Order curr;
		for(int i = 0; i < total; i++){
			curr = buildStandardOrderC();
			orders.add(curr);
			cmc.addOrder(curr);
		}
		
		//Do all the orders
		while(!orders.get(toDo-1).done()){
			for(Mechanic mech : mechs){
				for(Task task : mech.getAvailableTasks()){
					mech.doTask(task, 60);
				}
			}
		}
	}
	
	/**
	 * Build a standard order: duration 60
	 * @return
	 */
	private CarOrder buildStandardOrderC(){
		CarPart[] partsArray = {
				CarPart.BODY_SPORT, 
				CarPart.COLOUR_BLACK,
				CarPart.ENGINE_8,
				CarPart.GEARBOX_6MANUAL,
				CarPart.SEATS_LEATHER_WHITE,
				CarPart.AIRCO_NONE,
				CarPart.WHEELS_SPORTS,
				CarPart.SPOILER_LOW
			};
		
		CarOrderDetailsMaker maker = new CarOrderDetailsMaker(CarModel.MODELC);
		for(CarPart part : partsArray){
			maker.addPart(part);
		}
		return new CarOrder(maker.getDetails());
	}
}
