package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import logic.assemblyline.AssemblyLine;
import logic.car.CarModel;
import logic.car.CarOrder;
import logic.car.CarPart;
import logic.car.CarSpecification;
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
	private AssemblyLine assemblyLine;
	private CarSpecification carSpecification;
	private List<CarOrder> orders = new ArrayList<CarOrder>();
	private Mechanic barry;
	
	/**
	 * We start by setting up an environment with assets commonly used by the test suite in the prequel.
	 * This contains an AssemblyLine object and a list of CarOrder objects ready to launch on the assemblyline.
	 * A mechanic is provided to work on the orders. A CarManufacturingCompany is included.  
	 */
	@Before
	public void prequel(){
		cmcMotors = new CarManufacturingCompany();
		barry = new Mechanic(cmcMotors, "Barry");
		
		assemblyLine = new AssemblyLine();
		CarPart[] partsArray = {
				CarPart.AIRCO_AUTO, 
				CarPart.BODY_BREAK, 
				CarPart.COLOUR_BLACK, 
				CarPart.ENGINE_4, 
				CarPart.GEARBOX_5AUTO, 
				CarPart.SEATS_LEATHER_BLACK, 
				CarPart.WHEELS_COMFORT
			};
		
		List<CarPart> parts = (List<CarPart>) Arrays.asList(partsArray);
		carSpecification = new CarSpecification(CarModel.MODEL1,parts);
		for(int i = 0; i < 10; i++){
			orders.add(new CarOrder(carSpecification));
		}
	}
	
	
	/**
	 * This test simulates a single CarOrder object propagating through the assemblyline.
	 * This is a really in depth test, constantly checking the states of all involved actors.
	 */
	@Test
	public void singleCarOrderPropagation(){
		DateTime startTime = assemblyLine.getCurrentTime();
		assertFalse(orders.get(0).done());
		assemblyLine.addCarOrder(orders.get(0));
		/* Now we progress the line */
		assemblyLine.moveAssemblyLine(0);
		assertFalse(orders.get(0).done());
		assertFalse(assemblyLine.moveAssemblyLine(60));
		assertTrue(assemblyLine.getWorkStations()[0].getCurrentOrder().equals(orders.get(0)));
		assertNull(assemblyLine.getWorkStations()[1].getCurrentOrder());
		assertNull(assemblyLine.getWorkStations()[2].getCurrentOrder());
		
		/* Now we progress the line */
		barry.setActiveWorkstation(assemblyLine.getWorkStations()[0]);
		assertFalse(assemblyLine.getWorkStations()[0].done());
		for(Task task : assemblyLine.getWorkStations()[0].getRequiredTasks()){
			barry.doTask(task);
		}
		assertTrue(assemblyLine.getWorkStations()[0].done());
		
		assertTrue(assemblyLine.moveAssemblyLine(60));
		assertNull(assemblyLine.getWorkStations()[0].getCurrentOrder());
		
		/* Now we progress the line */
		barry.setActiveWorkstation(assemblyLine.getWorkStations()[1]);
		assertFalse(assemblyLine.getWorkStations()[1].done());
		assertFalse(orders.get(0).done());
		for(Task task : assemblyLine.getWorkStations()[1].getRequiredTasks()){
			barry.doTask(task);
		}
		assertTrue(assemblyLine.getWorkStations()[1].done());
		assertTrue(assemblyLine.moveAssemblyLine(60));
		assertFalse(assemblyLine.moveAssemblyLine(60));
		assertNull(assemblyLine.getWorkStations()[0].getCurrentOrder());
		assertNull(assemblyLine.getWorkStations()[1].getCurrentOrder());
		
		barry.setActiveWorkstation(assemblyLine.getWorkStations()[2]);
		assertFalse(assemblyLine.getWorkStations()[2].done());
		assertFalse(orders.get(0).done());
		for(Task task : assemblyLine.getWorkStations()[2].getRequiredTasks()){
			barry.doTask(task);
		}
		assertTrue(assemblyLine.getWorkStations()[2].done());
		assertTrue(orders.get(0).done());
		assertTrue(assemblyLine.moveAssemblyLine(60));
		assertTrue(orders.get(0).done());
		
		assertTrue(assemblyLine.getCurrentTime().equals(startTime.plusMinutes(180)));
	}
	
	/**
	 * This tests the scheduling of 10 Orders in one infinite shift.
	 */
	@Test
	public void tenOrdersTest(){
		for(CarOrder order : orders){
			assemblyLine.addCarOrder(order);
			assertFalse(order.done());
		}
		for(int i = 0; i < 10; i++){
			for(Workstation station : assemblyLine.getWorkStations()){
				barry.setActiveWorkstation(station);
				for(Task task : station.getRequiredTasks()){
					barry.doTask(task);
				}
			}
			assemblyLine.moveAssemblyLine(60);
		}
		for(int i = 0; i < 10; i++){
			if(i < 8){
				assertTrue(orders.get(i).done());
			}else{
				assertFalse(orders.get(i).done());
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
		cmcMotors.progressTime(9 * 60 - 15);
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
		

		/**
		 * Now we'll test what happens when the shifts move up without 
		 */
		for(Task task : cmcMotors.getWorkStations()[0].getRequiredTasks()){
			barry.setActiveWorkstation(cmcMotors.getWorkStations()[0]);
			barry.doTask(task);
		}
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
		for(int i = 0; i < cmcMotors.getWorkStations().length; i++){
			Workstation ws = cmcMotors.getWorkStations()[i];
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
		barry.setActiveWorkstation(cmcMotors.getWorkStations()[0]);
		for(Task task : orders.get(0).getTasks()){
			barry.doTask(task);
		}
		cmcMotors.moveAssemblyLine(180);
		barry.setActiveWorkstation(cmcMotors.getWorkStations()[1]);
		for(Task task : orders.get(0).getTasks()){
			barry.doTask(task);
		}
		cmcMotors.moveAssemblyLine(180);
		barry.setActiveWorkstation(cmcMotors.getWorkStations()[2]);
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
