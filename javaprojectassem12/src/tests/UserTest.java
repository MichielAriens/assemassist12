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

import org.junit.Test;

/**
 * A test case to test the user classes.
 */
public class UserTest {
	
	CarManufacturingCompany company = new CarManufacturingCompany();
	
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
		assertEquals(3, m.getWorkstations().size());
		ArrayList<CarOrder> orders = new ArrayList<CarOrder>();
		orders.add(null);
		orders.add(null);
		orders.add(null);
		assertEquals(orders, m.askFutureSchedule());
		//check if the assembly line can be moved if there are no car orders present
		assertEquals(true, m.moveAssemblyLine(60));
		//add a car order to the assembly line
		makeAndPlaceOrder();
		//check if the future schedule now contains a car order
		assertFalse(orders.equals(m.askFutureSchedule()));
		//check if the assembly line refuses to move before all tasks are done
		assertEquals(false, m.moveAssemblyLine(60));
		//complete all tasks and check if the assembly line moves again
		CarOrder c = m.getWorkstations().get(0).getCurrentOrder();
		for(Task t : c.getTasks())
			t.perform();
		assertTrue(m.moveAssemblyLine(60));
	}
	
	private void makeOrderAndAdvance(){
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
		Manager m = (Manager) company.logIn("Wander");
		m.moveAssemblyLine(60);
	}
	
	@Test
	public void mechanicTest(){
		//login with mechanic account
		Mechanic m = (Mechanic) company.logIn("Joren");
		//check initial values
		assertEquals("Joren", m.getUserName());
		assertEquals(null, m.getActiveWorkstation());
		assertFalse(m.isPosted());
		assertEquals(null, m.getAvailableTasks());
		//set a workstation
		m.setActiveWorkstation(m.getAvailableWorkstations().get(0));
		assertTrue(m.isPosted());
		//place a car order and advance the assembly line
		makeOrderAndAdvance();
		assertTrue(m.doTask(m.getAvailableTasks().get(0)));
		assertFalse(m.doTask(new Task(CarPart.AIRCO_AUTO)));
	}
	
	@Test
	public void invalidUserTest(){
		//login with invalid username
		assertEquals(null, company.logIn("Jack"));
	}
	
	@Test
	public void nullOrderTest(){
		//try to place a null order
		company.addOrder(null);
	}
	
	@Test
	public void reloginTest(){
		Manager m1 = (Manager) company.logIn("Wander");
		Manager m2 = (Manager) company.logIn("Wander");
		assertEquals(m1, m2);
	}
	
	

}
