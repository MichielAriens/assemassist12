package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.smartcardio.CardException;

import logic.assemblyline.AssemblyLine;
import logic.car.*;
import logic.users.CarManufacturingCompany;
import logic.users.Mechanic;
import logic.workstation.Task;
import logic.workstation.Workstation;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.junit.Before;
import org.junit.Test;

public class AssemblyLineTest {
	private CarManufacturingCompany cmcMotors;
	//private AssemblyLine assemblyLine;
	private List<CarOrder> orders = new ArrayList<CarOrder>();
	private Mechanic barry;
	
	/**
	 * Build a standard 
	 * @return
	 */
	private CarOrder buildStandardOrderA(){
		CarPart[] partsArray = {
				CarPart.BODY_BREAK, 
				CarPart.COLOUR_RED,
				CarPart.ENGINE_4,
				CarPart.GEARBOX_5AUTO,
				CarPart.SEATS_LEATHER_WHITE,
				CarPart.AIRCO_MANUAL,
				CarPart.WHEELS_COMFORT,
				CarPart.SPOILER_NONE
			};
		
		CarOrderDetailsMaker maker = new CarOrderDetailsMaker(CarModel.MODELA);
		for(CarPart part : partsArray){
			maker.addPart(part);
		}
		return new CarOrder(maker.getDetails());
	}
	
	private CarOrder buildStandardOrderB(){
		CarPart[] partsArray = {
				CarPart.BODY_BREAK, 
				CarPart.COLOUR_RED,
				CarPart.ENGINE_4,
				CarPart.GEARBOX_5AUTO,
				CarPart.SEATS_LEATHER_WHITE,
				CarPart.AIRCO_MANUAL,
				CarPart.WHEELS_COMFORT,
				CarPart.SPOILER_NONE
			};
		
		CarOrderDetailsMaker maker = new CarOrderDetailsMaker(CarModel.MODELA);
		for(CarPart part : partsArray){
			maker.addPart(part);
		}
		return new CarOrder(maker.getDetails());
	}
	
	private CarOrder buildStandardOrderC(){
		CarPart[] partsArray = {
				CarPart.BODY_SPORT, 
				CarPart.COLOUR_BLACK,
				CarPart.ENGINE_6,
				CarPart.GEARBOX_5AUTO,
				CarPart.SEATS_LEATHER_WHITE,
				CarPart.AIRCO_MANUAL,
				CarPart.WHEELS_SPORTS,
				CarPart.SPOILER_LOW
			};
		
		CarOrderDetailsMaker maker = new CarOrderDetailsMaker(CarModel.MODELA);
		for(CarPart part : partsArray){
			maker.addPart(part);
		}
		return new CarOrder(maker.getDetails());
	}
	
	/**
	 * A quick test to check whether the builders defined above work. (correct specification)
	 */
	@Test
	public void testOrdersValid(){
		assertTrue(buildStandardOrderA() != null);
		assertTrue(buildStandardOrderB() != null);
		assertTrue(buildStandardOrderC() != null);
	}
	
	/**
	 * We start by setting up an environment with assets commonly used by the test suite in the prequel.
	 * This contains an AssemblyLine object and a list of CarOrder objects ready to launch on the assemblyline.
	 * A mechanic is provided to work on the orders. A CarManufacturingCompany is included.  
	 */
	@Before
	public void prequel(){
		cmcMotors = new CarManufacturingCompany();
		barry = new Mechanic(cmcMotors, "Barry");
		for(int i = 0; i < 10; i++){
			if(i % 3 == 0)
				orders.add(buildStandardOrderA());
			else if(i % 3 == 1)
				orders.add(buildStandardOrderB());
			else
				orders.add(buildStandardOrderC());
		}
	}
	
	/**
	 * Try to do every single task in the system. The standard phase durations are used.
	 */
	private void tryAllTasks(){
		for(CarOrder order : orders){
			for(Task task : order.getTasks()){
				barry.doTask(task.toString(), order.getPhaseTime() );
			}
		}
	}
	
	
	/**
	 * This test tests the correct propagation of estimated completion times.
	 */
	@Test
	public void testEstimatesEasy(){
		// The day starts at 6:00. Let's pretend time passes to 14:45 without any orders.
		DateTime now = cmcMotors.getCurrentTime();
		cmcMotors.moveAssemblyLine(9 * 60 - 15);
		now = now.plusMinutes(9 * 60 - 15);
		assertTrue(eqiDateTime(cmcMotors.getCurrentTime(),now));
		
		//Lets start adding orders.
		cmcMotors.addOrder(orders.get(0));
		//the first car order is automatically added to the assemblyline. The estimated completion date should be in 3 hours.
		assertTrue(eqiDateTime(orders.get(0).getEstimatedEndTime(), now.plusHours(3)));//17:45
		//Let's keep adding
		cmcMotors.addOrder(orders.get(1));
		assertTrue(eqiDateTime(orders.get(1).getEstimatedEndTime(), now.plusHours(4)));//18:45
		cmcMotors.addOrder(orders.get(2));
		assertTrue(eqiDateTime(orders.get(2).getEstimatedEndTime(), now.plusHours(5)));//19:45
		cmcMotors.addOrder(orders.get(3));
		assertTrue(eqiDateTime(orders.get(3).getEstimatedEndTime(), now.plusHours(6)));//20:45
		cmcMotors.addOrder(orders.get(4));
		assertTrue(eqiDateTime(orders.get(4).getEstimatedEndTime(), now.plusHours(7)));//21:45
		cmcMotors.addOrder(orders.get(5));
		MutableDateTime mu = now.toMutableDateTime();
		mu.addDays(1);
		mu.setHourOfDay(9);mu.setMinuteOfHour(0);
		assertTrue(eqiDateTime(orders.get(5).getEstimatedEndTime(), mu.toDateTime()));//9:00 the next day.
	
		cmcMotors.addOrder(orders.get(6));
		assertTrue(eqiDateTime(orders.get(6).getEstimatedEndTime(), mu.toDateTime().plusHours(1)));//10:00 the next day.

		tryAllTasks();
		
		
		//The cycle took shorter than expected. We'll test whether this is reflected in the estimates.
		cmcMotors.moveAssemblyLine(45);
		now = cmcMotors.getCurrentTime();
		assertTrue(eqiDateTime(orders.get(0).getEstimatedEndTime(), now.plusHours(2)));
		assertTrue(eqiDateTime(orders.get(1).getEstimatedEndTime(), now.plusHours(3)));
		// orders for the next day should not have moved.
		assertTrue(eqiDateTime(orders.get(5).getEstimatedEndTime(), mu.toDateTime()));
		
		/**
		 * If a cycle is very short an order from the next day can 'jump forwards'
		 */
		// Do all tasks
		for(int i = 0; i < cmcMotors.getWorkStations().size(); i++){
			Workstation ws = cmcMotors.getWorkStations().get(i);
			barry.setActiveWorkstation(ws);
			for(Task task : ws.getRequiredTasks()){
				barry.doTask(task);
			}
		}
		//Progress the line
		cmcMotors.moveAssemblyLine(29);
		now = cmcMotors.getCurrentTime();
		assertTrue(eqiDateTime(orders.get(5).getEstimatedEndTime(), now.plusHours(6)));
		
		
	}
	
	/**
	 * 
	 */
	@Test
	public void testOvertime(){
		DateTime start = cmcMotors.getCurrentTime();
		cmcMotors.progressTime(14 * 60); // Should induce one hour of overtime & will set the time to the next day.
		cmcMotors.moveAssemblyLine(3 * 60);
		assertTrue(eqiDateTime(start.plusDays(1), cmcMotors.getCurrentTime()));
		//Today should end at 21:00 instead of 22:00. We'll set the time to 17:45 and plan two orders. The first can be completed today, the second tomorrow.
		cmcMotors.progressTime(11 * 60 + 45);
		DateTime now = cmcMotors.getCurrentTime();
		
		cmcMotors.addOrder(orders.get(0));
		cmcMotors.addOrder(orders.get(1));
		
		assertTrue(eqiDateTime(orders.get(0).getEstimatedEndTime(), now.plusHours(3)));
		assertFalse(eqiDateTime(orders.get(1).getEstimatedEndTime(), now.plusHours(4)));
	}
	
	/**
	 * 
	 */
	@Test
	public void testOvertimeOver2Hours(){
		//GO to 18:00
		cmcMotors.progressTime(12 * 60);
		cmcMotors.addOrder(orders.get(0));
		barry.setActiveWorkstation(cmcMotors.getWorkStations().get(0));
		for(Task task : orders.get(0).getTasks()){
			barry.doTask(task);
		}
		cmcMotors.moveAssemblyLine(180);
		barry.setActiveWorkstation(cmcMotors.getWorkStations().get(1));
		for(Task task : orders.get(0).getTasks()){
			barry.doTask(task);
		}
		cmcMotors.moveAssemblyLine(180);
		barry.setActiveWorkstation(cmcMotors.getWorkStations().get(2));
		for(Task task : orders.get(0).getTasks()){
			barry.doTask(task);
		}
		cmcMotors.moveAssemblyLine(180);
		//A new day has started. The overtime is 5 hours. The work day stops on 17:00
		//goto 13:59 the last moment to post an order.
		cmcMotors.progressTime(7 * 60 + 59);
		DateTime now = cmcMotors.getCurrentTime();
		cmcMotors.addOrder(orders.get(1));
		cmcMotors.addOrder(orders.get(2));
		
		assertTrue(eqiDateTime(orders.get(1).getEstimatedEndTime(), now.plusHours(3)));
		assertFalse(eqiDateTime(orders.get(2).getEstimatedEndTime(), now.plusHours(4)));
	}
	
	/**
	 * Tests whether progressTime() starts a new day when the invokation would result in a time outside working hours.
	 */
	@Test
	public void testProgressTime(){
		DateTime now = cmcMotors.getCurrentTime();
		cmcMotors.progressTime(22 * 60);
		assertTrue(eqiDateTime(cmcMotors.getCurrentTime(), now.plusDays(1)));
	}
	
	/**
	 * 
	 */
	@Test
	public void testNegativeOvertime(){
		int day1 = cmcMotors.getCurrentTime().getDayOfYear();
		cmcMotors.progressTime(12 * 60 + 30); //18:30
		cmcMotors.moveAssemblyLine(60);//This will end the day and result in a theoretical overtime of -30. The overtime will be set to 0.
		//We can't access the overtime directly, however progressing time to 21:59 should not result in a day switch.
		assertTrue(cmcMotors.getCurrentTime().getDayOfYear() == day1 + 1);
	}
	
	
	
	/**
	 * Tests whether the two DateTime object provided describe the same moment in time accurate to the minute. 
	 */
	public static boolean eqiDateTime(DateTime a, DateTime b){
		if(a.getYear() == b.getYear()){
			if(a.getDayOfYear() == b.getDayOfYear()){
				if(a.getMinuteOfDay() == b.getMinuteOfDay()){
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Tests the correctness of eqiDateTime(...)
	 */
	@Test
	public void testEqiDateTime(){
		DateTime a = DateTime.now();
		DateTime b = a.plusMinutes(1);
		assertFalse(eqiDateTime(a, b));
		b = b.minusMinutes(1);
		assertTrue(eqiDateTime(a, b));
	}
	
	
	
	
}

/**
 * 
 * 
 * 
	@Test
	public void testEstimatesEasy(){
		// The day starts at 6:00. Let's pretend time passes to 14:45 without any orders.
		DateTime now = cmcMotors.getCurrentTime();
		cmcMotors.moveAssemblyLine(9 * 60 - 15);
		now = now.plusMinutes(9 * 60 - 15);
		assertTrue(eqiDateTime(cmcMotors.getCurrentTime(),now));
		
		//Lets start adding orders.
		cmcMotors.addOrder(orders.get(0));
		//the first car order is automatically added to the assemblyline. The estimated completion date should be in 3 hours.
		assertTrue(eqiDateTime(orders.get(0).getEstimatedEndTime(), now.plusHours(3)));//17:45
		//Let's keep adding
		cmcMotors.addOrder(orders.get(1));
		assertTrue(eqiDateTime(orders.get(1).getEstimatedEndTime(), now.plusHours(4)));//18:45
		cmcMotors.addOrder(orders.get(2));
		assertTrue(eqiDateTime(orders.get(2).getEstimatedEndTime(), now.plusHours(5)));//19:45
		cmcMotors.addOrder(orders.get(3));
		assertTrue(eqiDateTime(orders.get(3).getEstimatedEndTime(), now.plusHours(6)));//20:45
		cmcMotors.addOrder(orders.get(4));
		assertTrue(eqiDateTime(orders.get(4).getEstimatedEndTime(), now.plusHours(7)));//21:45
		cmcMotors.addOrder(orders.get(5));
		MutableDateTime mu = now.toMutableDateTime();
		mu.addDays(1);
		mu.setHourOfDay(9);mu.setMinuteOfHour(0);
		assertTrue(eqiDateTime(orders.get(5).getEstimatedEndTime(), mu.toDateTime()));//9:00 the next day.
	
		cmcMotors.addOrder(orders.get(6));
		assertTrue(eqiDateTime(orders.get(6).getEstimatedEndTime(), mu.toDateTime().plusHours(1)));//10:00 the next day.

		tryAllTasks();
		
		
		//The cycle took shorter than expected. We'll test whether this is reflected in the estimates.
		cmcMotors.moveAssemblyLine(45);
		now = cmcMotors.getCurrentTime();
		assertTrue(eqiDateTime(orders.get(0).getEstimatedEndTime(), now.plusHours(2)));
		assertTrue(eqiDateTime(orders.get(1).getEstimatedEndTime(), now.plusHours(3)));
		// orders for the next day should not have moved.
		assertTrue(eqiDateTime(orders.get(5).getEstimatedEndTime(), mu.toDateTime()));
		
		
		// Do all tasks
		for(int i = 0; i < cmcMotors.getWorkStations().size(); i++){
			Workstation ws = cmcMotors.getWorkStations().get(i);
			barry.setActiveWorkstation(ws);
			for(Task task : ws.getRequiredTasks()){
				barry.doTask(task);
			}
		}
		//Progress the line
		cmcMotors.moveAssemblyLine(29);
		now = cmcMotors.getCurrentTime();
		assertTrue(eqiDateTime(orders.get(5).getEstimatedEndTime(), now.plusHours(6)));
		
		
	}
 * 
 * 
 * */
