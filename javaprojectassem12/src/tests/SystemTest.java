package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import logic.car.CarModel;
import logic.car.CarOrder;
import logic.car.CarPart;
import logic.car.CarSpecification;
import logic.users.CarManufacturingCompany;
import logic.users.GarageHolder;
import logic.users.Manager;
import logic.users.Mechanic;
import logic.workstation.Task;

import org.junit.Before;
import org.junit.Test;

public class SystemTest {
	
	CarManufacturingCompany company;
	
	@Before
	public void prequel(){
		company = new CarManufacturingCompany();
	}
	
	/**
	 * Test the garageHolder class
	 */
	@Test
	public void garageHolderTest() {
		//login with garageholder account
		GarageHolder g = (GarageHolder) company.logIn("Michiel");
		//check initial values
		assertEquals("Michiel", g.getUserName());
		assertEquals(0,g.getPendingOrders().size());
		assertEquals(0,g.getCompletedOrders().size());
		//try to add a car order with null as carspecification
		g.placeOrder(null);
		//make carspecification to place an order
		ArrayList<CarPart> carparts = new ArrayList<CarPart>();
		carparts.add(CarPart.BODY_BREAK);
		carparts.add(CarPart.COLOUR_BLACK);
		carparts.add(CarPart.SEATS_LEATHER_BLACK);
		carparts.add(CarPart.AIRCO_AUTO);
		carparts.add(CarPart.WHEELS_COMFORT);
		carparts.add(CarPart.ENGINE_4);
		carparts.add(CarPart.GEARBOX_5AUTO);
		CarSpecification spec = new CarSpecification(CarModel.MODEL1, carparts);
		g.placeOrder(spec);
		//check for new pending order
		assertEquals(1,g.getPendingOrders().size());
		assertEquals(0,g.getCompletedOrders().size());
		//do all tasks for that order
		for(Task t : g.getPendingOrders().get(0).getTasks())
			t.perform();
		//check if the order is now counted as completed
		assertEquals(0,g.getPendingOrders().size());
		assertEquals(1,g.getCompletedOrders().size());
	}
	
	private void makeAndPlaceOrder(){
		GarageHolder g = (GarageHolder) company.logIn("Michiel");
		ArrayList<CarPart> carparts = new ArrayList<CarPart>();
		carparts.add(CarPart.BODY_BREAK);
		carparts.add(CarPart.COLOUR_BLACK);
		carparts.add(CarPart.SEATS_LEATHER_BLACK);
		carparts.add(CarPart.AIRCO_AUTO);
		carparts.add(CarPart.WHEELS_COMFORT);
		carparts.add(CarPart.ENGINE_4);
		carparts.add(CarPart.GEARBOX_5AUTO);
		CarSpecification spec = new CarSpecification(CarModel.MODEL1, carparts);
		g.placeOrder(spec);
	}
	
	@Test
	public void managerTest(){
		//login with manager username
		Manager m = (Manager) company.logIn("Wander");
		//check initial values
		assertEquals("Wander", m.getUserName());
		assertEquals(3, m.getWorkstations().length);
		ArrayList<CarOrder> orders = new ArrayList<CarOrder>();
		orders.add(null);
		orders.add(null);
		orders.add(null);
		assertEquals(orders, m.askFutureSchedule());
		//check if the assembly line can be moved if there are no car orders present
		assertEquals(true, m.moveAssemblyLine(60));
		//add a car order to the assembly line
		makeAndPlaceOrder();
		//check if the assembly line moves (the order from the queue to the first workstation)
		assertEquals(true, m.moveAssemblyLine(60));
		//check if the future schedule now contains a car order
		assertNotEquals(orders, m.askFutureSchedule());
		//check if the assembly line refuses to move before all tasks are done
		assertEquals(false, m.moveAssemblyLine(60));
		//complete all tasks and check if the assembly line moves again
		CarOrder c = m.getWorkstations()[0].getCurrentOrder();
		for(Task t : c.getTasks())
			t.perform();
		assertTrue(m.moveAssemblyLine(60));
	}
	
	@Test
	public void mechanicTest(){
		//login with mechanic account
		Mechanic m = (Mechanic) company.logIn("Joren");
		//check initial values
		assertEquals("Joren", m.getUserName());
		assertEquals(null, m.getActiveWorkstation());
		assertFalse(m.isPosted());
		
	}
	
	

}