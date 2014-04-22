package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import logic.car.CarModel;
import logic.car.CarOrder;
import logic.car.CarOrderDetails;
import logic.car.CarOrderDetailsMaker;
import logic.car.CarPart;
import logic.car.Order;
import logic.car.TaskOrderDetailsMaker;
import logic.users.CarManufacturingCompany;
import logic.users.CustomsManager;
import logic.users.GarageHolder;
import logic.users.Manager;
import logic.users.Mechanic;
import logic.workstation.Task;

import org.joda.time.DateTime;
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
		GarageHolder g = (GarageHolder) company.logIn("Jeroen");
		//check initial values
		assertEquals("Jeroen", g.getUserName());
		assertEquals(0,g.getPendingOrders().size());
		assertEquals(0,g.getCompletedOrders().size());
		//try to add a car order with null as carspecification
		g.placeOrder(null);
		//make carspecification to place an order
		CarOrderDetailsMaker maker = new CarOrderDetailsMaker(CarModel.MODELA);
		maker.addPart(CarPart.getPartfromString("Model A"));
		maker.addPart(CarPart.getPartfromString("Sedan"));
		maker.addPart(CarPart.getPartfromString("Red"));
		maker.addPart(CarPart.getPartfromString("Standard 2l v4"));
		maker.addPart(CarPart.getPartfromString("6 speed manual"));
		maker.addPart(CarPart.getPartfromString("Leather black"));
		maker.addPart(CarPart.getPartfromString("Manual"));
		maker.addPart(CarPart.getPartfromString("Comfort"));
		maker.addPart(CarPart.getPartfromString("No Spoiler"));

//		ArrayList<CarPart> carparts = new ArrayList<CarPart>();
//		carparts.add(CarPart.BODY_BREAK);
//		carparts.add(CarPart.COLOUR_BLACK);
//		carparts.add(CarPart.SEATS_LEATHER_BLACK);
//		carparts.add(CarPart.AIRCO_AUTO);
//		carparts.add(CarPart.WHEELS_COMFORT);
//		carparts.add(CarPart.ENGINE_4);
//		carparts.add(CarPart.GEARBOX_5AUTO);
//		CarSpecification spec = new CarSpecification(CarModel.MODELA, carparts);
		//g.placeOrder(spec);
		g.placeOrder(maker.getDetails());
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
		GarageHolder g = (GarageHolder) company.logIn("gar");
		ArrayList<CarPart> carparts = new ArrayList<CarPart>();
		carparts.add(CarPart.BODY_BREAK);
		carparts.add(CarPart.COLOUR_BLACK);
		carparts.add(CarPart.SEATS_LEATHER_BLACK);
		carparts.add(CarPart.AIRCO_AUTO);
		carparts.add(CarPart.WHEELS_COMFORT);
		carparts.add(CarPart.ENGINE_4);
		carparts.add(CarPart.GEARBOX_5AUTO);
		CarOrderDetails det = new CarOrderDetails(CarModel.MODELA, carparts);
		g.placeOrder(det);
	}
	
	@Test
	public void managerTest(){
		//login with manager username
		Manager m = (Manager) company.logIn("Wander");
		//check initial values
		assertEquals("Wander", m.getUserName());
		
		assertEquals("FIFO", m.getStrategies().get(0).toString());
		
		ArrayList<CarOrder> orders = new ArrayList<CarOrder>();
		orders.add(null);
		orders.add(null);
		orders.add(null);
		assertTrue(m.getBatchList().isEmpty());
		//check if the assembly line can be moved if there are no car orders present
		
		//add a car order to the assembly line
		makeAndPlaceOrder();
		makeAndPlaceOrder();
		List<Order> bach  = m.getBatchList();
		assertTrue(bach.isEmpty());
		makeAndPlaceOrder();
		bach = m.getBatchList();
		assertFalse(bach.isEmpty());
		m.changeStrategy(bach.get(0));
		assertEquals("Specification Batch", m.getStrategies().get(0).toString());
		assertEquals("Average number of cars produced: 0\nMean number of cars produced: 0\nExact numbers two last days:\n   No records.\nAverage delay: 0 minutes\nMean delay: 0 minutes\nTwo last delays:\n   No records.\n", m.getStatistics());
	}
	
	private void makeOrderAndAdvance(){
		
		GarageHolder g = (GarageHolder) company.logIn("gar");
		ArrayList<CarPart> carparts = new ArrayList<CarPart>();
		carparts.add(CarPart.BODY_BREAK);
		carparts.add(CarPart.COLOUR_BLACK);
		carparts.add(CarPart.SEATS_LEATHER_BLACK);
		carparts.add(CarPart.AIRCO_AUTO);
		carparts.add(CarPart.WHEELS_COMFORT);
		carparts.add(CarPart.ENGINE_4);
		carparts.add(CarPart.GEARBOX_5AUTO);
		CarOrderDetails spec = new CarOrderDetails(CarModel.MODELA, carparts);
		g.placeOrder(spec);
		g.placeOrder(spec.getRawCopy());
		Mechanic m = (Mechanic) company.logIn("mech");
		List<Task> tasksss = m.getAvailableTasks();
		assertTrue(tasksss.size()==2);
		m.doTask(tasksss.get(0), 60);
		tasksss = m.getAvailableTasks();
		assertTrue(tasksss.size()==1);
		m.doTask(tasksss.get(0), 70);
		tasksss = m.getAvailableTasks();
		assertTrue(tasksss.size()==2);
		
		
	}
	
	@Test
	public void mechanicTest(){
		//login with mechanic account
		Mechanic m = (Mechanic) company.logIn("mech");
		//check initial values
		assertEquals("mech", m.getUserName());
		assertEquals(null, m.getActiveWorkstation());
		assertFalse(m.isPosted());
		assertEquals(null, m.getAvailableTasks());
		//set a workstation
		
		m.setActiveWorkstation(m.getWorkstations().get(0));
		assertTrue(m.isPosted());
		//place a car order and advance the assembly line
		makeOrderAndAdvance();
		
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
	@Test
	public void testCustomShop(){

		CustomsManager c = (CustomsManager) company.logIn("Michiel");
		//check initial values
		assertEquals("Michiel", c.getUserName());

		c.placeOrder(null);
		//make carspecification to place an order
		TaskOrderDetailsMaker maker = new TaskOrderDetailsMaker();
		maker.choosePart(CarPart.getPartfromString("Comfort"));

		maker.chooseDeadline(new DateTime(2014,1,1,17,0));


		assertEquals(null,c.placeOrder(maker.getDetails()));
		
		maker = new TaskOrderDetailsMaker();
		maker.choosePart(CarPart.getPartfromString("Red"));

		maker.chooseDeadline(new DateTime(2014,1,1,17,0));
		assertEquals("Estimated completion: 01-01-2014 09:00",c.placeOrder(maker.getDetails()));
	}
	

}
