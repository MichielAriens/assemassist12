package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import logic.car.CarModel;
import logic.car.CarPartType;
import logic.users.CarManufacturingCompany;

import org.junit.Test;

import controllers.*;

/**
 * A test case to test the use case of the garage holder. The other actors don't interact with the
 * system in this test case, but this is done in the UseCaseCombinedTest case.
 */
public class UseCaseGarageHolderTest {

	/**
	 * The main test.
	 */
	@Test
	public void mainSuccesTest() {
		AssemAssistController controller = new AssemAssistController(new CarManufacturingCompany());
		
		GarageHolderController ghCont = (GarageHolderController) controller.logIn("Jeroen");
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
//		//user places the order:
//		//if the user does not want to place the newly created order, the use case ends here (alternate flow 2)
		ghCont.placeOrder();
//		//the system prints the pending and completed orders (pending orders is no longer empty)
		System.out.println(ghCont.getPendingOrders());
		assertFalse(new ArrayList<String>().equals(ghCont.getPendingOrders()));
		assertEquals(1, ghCont.getPendingOrders().size());
		assertEquals(new ArrayList<String>(), ghCont.getCompletedOrders());
		//the user indicates that he doesn't want to place a new order and the use case ends
	}
}
