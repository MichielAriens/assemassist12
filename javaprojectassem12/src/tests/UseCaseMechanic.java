package tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import logic.users.CarManufacturingCompany;

import org.junit.Before;
import org.junit.Test;

import controllers.AssemAssistController;
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
		ArrayList<String> workstations = new ArrayList<String>();
		workstations.add("Car Body Post: 1");
		workstations.add("Drive Train Post: 2");
		workstations.add("Accessories Post: 3");
		assertEquals(workstations, mCont.getWorkStations());
		mCont.setWorkStation("Car Body Post");
		ArrayList<String> tasks = new ArrayList<String>();
		assertEquals(tasks,mCont.getTasks());
	}

}
