package tests;

import static org.junit.Assert.*;
import interfaces.Printable;

import java.util.ArrayList;
import java.util.List;

import logic.car.VehicleModel;
import logic.car.VehicleOrder;
import logic.car.VehicleOrderDetails;
import logic.car.VehicleOrderDetailsMaker;
import logic.car.VehiclePart;
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
		VehicleOrderDetailsMaker maker = new VehicleOrderDetailsMaker(VehicleModel.CARMODELA);
		maker.addPart(VehiclePart.getPartfromString("Car Model A"));
		maker.addPart(VehiclePart.getPartfromString("Sedan"));
		maker.addPart(VehiclePart.getPartfromString("Red"));
		maker.addPart(VehiclePart.getPartfromString("Standard 2l v4"));
		maker.addPart(VehiclePart.getPartfromString("6 speed manual"));
		maker.addPart(VehiclePart.getPartfromString("Leather black"));
		maker.addPart(VehiclePart.getPartfromString("Manual"));
		maker.addPart(VehiclePart.getPartfromString("Comfort"));
		maker.addPart(VehiclePart.getPartfromString("No Spoiler"));
		maker.addPart(VehiclePart.getPartfromString("No Toolstorage"));
		maker.addPart(VehiclePart.getPartfromString("No Cargo Protection"));
		maker.addPart(VehiclePart.getPartfromString("No Certification"));
		
		//g.placeOrder(spec);
		g.placeOrder(maker.getDetails());
		//check for new pending order
		assertEquals(1,g.getPendingOrders().size());
		assertEquals(0,g.getCompletedOrders().size());
		//do all tasks for that order
		for(Printable<Task> t : ((Order) g.getPendingOrders().get(0)).getTasks())
			((Task) t).perform(((Task)t).getEstimatedPhaseDuration());
		//check if the order is now counted as completed
		assertEquals(0,g.getPendingOrders().size());
		assertEquals(1,g.getCompletedOrders().size());
	}
	
	/**
	 * Let's the GarageHolder make and place an order.
	 */
	private void makeAndPlaceOrder(){
		GarageHolder g = (GarageHolder) company.logIn("gar");
		ArrayList<VehiclePart> carparts = new ArrayList<VehiclePart>();
		carparts.add(VehiclePart.BODY_BREAK);
		carparts.add(VehiclePart.COLOUR_BLACK);
		carparts.add(VehiclePart.ENGINE_4);
		carparts.add(VehiclePart.GEARBOX_5AUTO);
		carparts.add(VehiclePart.SEATS_LEATHER_BLACK);
		carparts.add(VehiclePart.AIRCO_AUTO);
		carparts.add(VehiclePart.WHEELS_COMFORT);
		carparts.add(VehiclePart.SPOILER_NONE);
		carparts.add(VehiclePart.TOOLSTORAGE_NONE);
		carparts.add(VehiclePart.CARGO_NONE);
		carparts.add(VehiclePart.CERTIFICATION_NONE);
		VehicleOrderDetails det = new VehicleOrderDetails(VehicleModel.CARMODELA, carparts);
		g.placeOrder(det);
	}
	
	/**
	 * Test the manager class.
	 */
	@Test
	public void managerTest(){
		//login with manager username
		Manager m = (Manager) company.logIn("Wander");
		//check initial values
		assertEquals("Wander", m.getUserName());
		m.setActiveAssemblyLine(m.getAssemblyLines().get(0));
		assertEquals("FIFO", m.getStrategiesActiveLine().get(0).toString());
		
		ArrayList<VehicleOrder> orders = new ArrayList<VehicleOrder>();
		orders.add(null);
		orders.add(null);
		orders.add(null);
		assertTrue(m.getBatchListActiveLine().isEmpty());
		//check if the assembly line can be moved if there are no car orders present
		
		//add a car order to the assembly line
		makeAndPlaceOrder();
		makeAndPlaceOrder();
		makeAndPlaceOrder();
		makeAndPlaceOrder();
		makeAndPlaceOrder();
		List<Order> bach  = m.getBatchListAllLines();
		assertTrue(bach.isEmpty());
		makeAndPlaceOrder();
		makeAndPlaceOrder();
		makeAndPlaceOrder();
		makeAndPlaceOrder();
		makeAndPlaceOrder();
		bach = m.getBatchListActiveLine();
		assertFalse(bach.isEmpty());
		m.changeStrategyActiveAssemblyLine(bach.get(0));
		assertEquals("Specification Batch", m.getStrategiesActiveLine().get(0).toString());
		String stats = "";
		stats += "Statistics of Assembly Line 1:\n";
		stats += "Average number of cars produced: 0\n";
		stats += "Mean number of cars produced: 0\n";
		stats += "Exact numbers two last days:\n";
		stats += "   No records.\n";
		stats += "Average delay: 0 minutes\n";
		stats += "Mean delay: 0 minutes\n";
		stats += "Two last delays:\n";
		stats += "   No records.\n";
		stats += "\n";
		stats += "Statistics of Assembly Line 2:\n";
		stats += "Average number of cars produced: 0\n";
		stats += "Mean number of cars produced: 0\n";
		stats += "Exact numbers two last days:\n";
		stats += "   No records.\n";
		stats += "Average delay: 0 minutes\n";
		stats += "Mean delay: 0 minutes\n";
		stats += "Two last delays:\n";
		stats += "   No records.\n";
		stats += "\n";
		stats += "Statistics of Assembly Line 3:\n";
		stats += "Average number of cars produced: 0\n";
		stats += "Mean number of cars produced: 0\n";
		stats += "Exact numbers two last days:\n";
		stats += "   No records.\n";
		stats += "Average delay: 0 minutes\n";
		stats += "Mean delay: 0 minutes\n";
		stats += "Two last delays:\n";
		stats += "   No records.\n";
		stats += "\n";
		stats += "Statistics of Generality:\n";
		stats += "Average number of cars produced: 0\n";
		stats += "Mean number of cars produced: 0\n";
		stats += "Exact numbers two last days:\n";
		stats += "   No records.\n";
		stats += "Average delay: 0 minutes\n";
		stats += "Mean delay: 0 minutes\n";
		stats += "Two last delays:\n";
		stats += "   No records.\n";
		assertEquals(stats, m.getStatistics());
	}
	
	/**
	 * Make an order and advance the assembly line by completing all tasks.
	 */
	private void makeOrderAndAdvance(){
		GarageHolder g = (GarageHolder) company.logIn("gar");
		ArrayList<VehiclePart> carparts = new ArrayList<VehiclePart>();
		carparts.add(VehiclePart.BODY_BREAK);
		carparts.add(VehiclePart.COLOUR_BLACK);
		carparts.add(VehiclePart.SEATS_LEATHER_BLACK);
		carparts.add(VehiclePart.AIRCO_AUTO);
		carparts.add(VehiclePart.WHEELS_COMFORT);
		carparts.add(VehiclePart.ENGINE_4);
		carparts.add(VehiclePart.GEARBOX_5AUTO);
		VehicleOrderDetails spec = new VehicleOrderDetails(VehicleModel.CARMODELA, carparts);
		g.placeOrder(spec);
		g.placeOrder(spec.getRawCopy());
		g.placeOrder(spec.getRawCopy());
		g.placeOrder(spec.getRawCopy());
		g.placeOrder(spec.getRawCopy());
		g.placeOrder(spec.getRawCopy());
		g.placeOrder(spec.getRawCopy());
		g.placeOrder(spec.getRawCopy());
		Mechanic m = (Mechanic) company.logIn("mech");
		m.setActiveAssemblyLine(m.getAssemblyLines().get(0));
		m.setActiveWorkstation(m.getWorkstationsFromAssemblyLine().get(0));
		List<Printable<Task>> tasksss = m.getAvailableTasks();
		assertTrue(tasksss.size()==2);
		m.doTask(tasksss.get(0), 60);
		tasksss = m.getAvailableTasks();
		assertTrue(tasksss.size()==1);
		m.doTask(tasksss.get(0), 70);
		tasksss = m.getAvailableTasks();
		assertTrue(tasksss.size()==0);
	}
	
	/**
	 * Test the mechanic class.
	 */
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
	
	/**
	 * Test a login with an invalid username.
	 */
	@Test
	public void invalidUserTest(){
		//login with invalid username
		assertEquals(null, company.logIn("Jack"));
	}
	
	/**
	 * Test placing a null order.
	 */
	@Test
	public void nullOrderTest(){
		//try to place a null order
		company.addOrder(null);
	}
	
	/**
	 * Test a relogin.
	 */
	@Test
	public void reloginTest(){
		Manager m1 = (Manager) company.logIn("Wander");
		Manager m2 = (Manager) company.logIn("Wander");
		assertEquals(m1, m2);
	}
	
	/**
	 * Test the customs shop manager class.
	 */
	@Test
	public void testCustomShop(){
		CustomsManager c = (CustomsManager) company.logIn("Michiel");
		//check initial values
		assertEquals("Michiel", c.getUserName());

		c.placeOrder(null);
		//make carspecification to place an order
		TaskOrderDetailsMaker maker = new TaskOrderDetailsMaker();
		maker.choosePart(VehiclePart.getPartfromString("Comfort"));

		maker.chooseDeadline(new DateTime(2014,1,1,17,0));

		assertEquals(null,c.placeOrder(maker.getDetails()));
		
		maker = new TaskOrderDetailsMaker();
		maker.choosePart(VehiclePart.getPartfromString("Red"));

		maker.chooseDeadline(new DateTime(2014,1,1,17,0));
		assertEquals("Estimated completion: 01-01-2014 07:00",c.placeOrder(maker.getDetails()));
	}
	

}
