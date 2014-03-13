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
		
		GarageHolderController ghCont = (GarageHolderController) controller.logIn("Michiel");
		assertEquals("Michiel", ghCont.getUserName());
		//The system prints the pending and completed orders (both are currently empty)
		assertEquals(new ArrayList<String>(), ghCont.getPendingOrders());
		assertEquals(new ArrayList<String>(), ghCont.getCompletedOrders());
		//if the user does not want to make a new order, the use case ends here (alternate flow 1)
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
		//user places the order:
		//if the user does not want to place the newly created order, the use case ends here (alternate flow 2)
		ghCont.placeOrder(form);
		//the system prints the pending and completed orders (pending orders is no longer empty)
		assertFalse(new ArrayList<String>().equals(ghCont.getPendingOrders()));
		assertEquals(1, ghCont.getPendingOrders().size());
		assertEquals(new ArrayList<String>(), ghCont.getCompletedOrders());
		//the user indicates that he doesn't want to place a new order and the use case ends
	}
}
