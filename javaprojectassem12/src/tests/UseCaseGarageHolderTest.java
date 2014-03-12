package tests;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

import logic.users.CarManufacturingCompany;

import org.junit.Before;
import org.junit.Test;

import views.UI;
import controllers.AssemAssistController;

public class UseCaseGarageHolderTest {
	
	
	private BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));
	private CarManufacturingCompany company;
	AssemAssistController controller;
	
	
	@Before
	public void prelude(){
		company = new CarManufacturingCompany();
		controller = new AssemAssistController(company);
		new UI(controller);
	}

	@Test
	public void test() {
		
	}

}
