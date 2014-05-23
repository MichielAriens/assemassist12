package tests;

import static org.junit.Assert.*;
import logic.users.CarManufacturingCompany;
import org.junit.Test;

import controllers.AssemAssistController;
import controllers.CustomsManagerController;

/**
 * A class for testing the use case of the custom shop manager.
 */
public class CustomUseCaseTest {

	/**
	 * A test case that follows the use case (5.8) of the custom shop manager. The custom shop manager receives a list of things he can order and then orders something and sets the deadline.
	 */
	@Test
	public void mainSuccessScenario() {
		AssemAssistController controller = new AssemAssistController(new CarManufacturingCompany());
		
		//Precondition: the user logs in
		CustomsManagerController custCont = (CustomsManagerController) controller.logIn("cust");
		
		//1. The user wants to order a single task
		//2. The system shows the list of available tasks.
		//	2.a. The user chooses the type of task from:
			assertTrue(custCont.getAvailableTypes().contains("Colour: 1"));
			assertTrue(custCont.getAvailableTypes().contains("Seats: 2"));
		//	2.b. The user chooses a specific task. (Here we choose from type 1)
			assertTrue(custCont.getAvailableOptions("Colour").toString().equals("[Red: 1, Blue: 2, Black: 3, White: 4, Green: 5, Yellow: 6]"));
			custCont.choosePart("Red");
		//3. The user has selected the task he wants to order (Colour: RED)
		//4. The system asks for a deadline
		//5. The user enters the required details
			custCont.chooseDeadLine("10-12-2050 10:30");
		//6. System stores the order and updates the schedule.
			String orderInfo = custCont.placeOrder();
		//7. The system presents an estimated completion date
			assertTrue(orderInfo.contains("01-01-2014 07:00"));
			
			
		
	}

}
