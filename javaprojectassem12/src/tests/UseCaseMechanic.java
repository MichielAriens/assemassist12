package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import logic.car.CarModel;
import logic.car.CarPartType;
import logic.users.CarManufacturingCompany;

import org.junit.Before;
import org.junit.Test;

import controllers.AssemAssistController;
import controllers.GarageHolderController;
import controllers.MechanicController;

public class UseCaseMechanic {

	private CarManufacturingCompany company;
	private AssemAssistController controller;
	
	@Before
	public void prelude(){
		company = new CarManufacturingCompany();
		controller = new AssemAssistController(company);
	}
	

	@Test
	public void mainSuccesTest() {
		MechanicController mCont = (MechanicController) controller.logIn("Joren");
		assertEquals("Joren", mCont.getUserName());
		//print pending and completed orders (currently empty)
		assertEquals(new ArrayList<String>(), ghCont.getPendingOrders());
		assertEquals(new ArrayList<String>(), ghCont.getCompletedOrders());
		//if the user does not want to make a new order, the use case ends here (alternate flow 1)
		//the user fills in an ordering form on the gui:
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
		//print pending and completed orders (pending no longer empty)
		assertNotEquals(new ArrayList<String>(), ghCont.getPendingOrders());
		assertEquals(1, ghCont.getPendingOrders().size());
		assertEquals(new ArrayList<String>(), ghCont.getCompletedOrders());
		//the user indicates that he doesn't want to place a new order and the use case ends
	}

}
