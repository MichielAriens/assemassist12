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
import logic.users.GarageHolder;
import logic.users.Manager;
import logic.users.Mechanic;
import logic.workstation.Task;
import logic.workstation.Workstation;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.junit.Before;
import org.junit.Test;

import sun.security.acl.WorldGroupImpl;

public class AssemblyLineTest {
	private CarManufacturingCompany cmcMotors;
	//private AssemblyLine assemblyLine;
	private List<CarOrder> orders = new ArrayList<CarOrder>();
	private Mechanic barry;
	private List<CarOrder> simpleOrders;
	
	/**
	 * Build a standard order: duration 50
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
	
	/**
	 * Build a standard order: duration 70
	 * @return
	 */
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
		
		CarOrderDetailsMaker maker = new CarOrderDetailsMaker(CarModel.MODELB);
		for(CarPart part : partsArray){
			maker.addPart(part);
		}
		return new CarOrder(maker.getDetails());
	}
	
	/**
	 * Build a standard order: duration 60
	 * @return
	 */
	private CarOrder buildStandardOrderC(){
		CarPart[] partsArray = {
				CarPart.BODY_SPORT, 
				CarPart.COLOUR_BLACK,
				CarPart.ENGINE_8,
				CarPart.GEARBOX_6MANUAL,
				CarPart.SEATS_LEATHER_WHITE,
				CarPart.AIRCO_NONE,
				CarPart.WHEELS_SPORTS,
				CarPart.SPOILER_LOW
			};
		
		CarOrderDetailsMaker maker = new CarOrderDetailsMaker(CarModel.MODELC);
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
		CarOrder order;
		order = buildStandardOrderA();
		assertFalse(order.getTasks().isEmpty());
		order = buildStandardOrderB();
		assertFalse(order.getTasks().isEmpty());
		order = buildStandardOrderC();
		assertFalse(order.getTasks().isEmpty());
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
			if((i % 3) == 0){
				orders.add(buildStandardOrderA());
			} else if((i % 3) == 1){
				orders.add(buildStandardOrderB());
			}else{
				orders.add(buildStandardOrderC());
			}
		}
		
		//Build a list of orders of duration 60.
		simpleOrders = new ArrayList<CarOrder>();
		for(int i = 0; i < 10; i++){
			simpleOrders.add(buildStandardOrderC());
		}
	}
	
	/**
	 * Try to do every single task in the system. The standard phase durations are used.
	 */
	private void tryAllTasks(){
		for(Workstation ws : cmcMotors.getWorkStations()){
			tryAllTasks(ws);
		}
	}
	private void tryAllTasks(Workstation ws){tryAllTasks(ws,0);}
	private void tryAllTasks(Workstation ws, int offset){
		barry.setActiveWorkstation(ws.toString());
		for(CarOrder order : orders){
			for(Task task : order.getTasks()){
				barry.doTask(task.toString(), order.getPhaseTime() + offset );
			}
		}
	}
	

	/**
	 * This test tests the correct propagation of estimated completion times.
	 * 50 - 70 - 60 - 50 - 70 - 60 - ...
	 */
	@Test
	public void testEstimatesEasy(){
		// The day starts at 6:00.
		DateTime now = cmcMotors.getCurrentTime();
		
		//Lets start adding orders.
		cmcMotors.addOrder(orders.get(0));
		//the first car order is automatically added to the assemblyline. The estimated completion date should be in 150 minutes.
		assertTrue(eqiDateTime(orders.get(0).getEstimatedEndTime(), now.plusMinutes(150)));
		
		//Let's add the second order (70 mins cycle): this will have a total duration of 210. 
		cmcMotors.addOrder(orders.get(1));
		//This means that the first order will have to wait 20 minutes extra on two of the workstations inducing a total duration of 190 minutes.
		assertTrue(eqiDateTime(orders.get(0).getEstimatedEndTime(), now.plusMinutes(190)));
		//the second order is comleted after 50 + 3* 70 mins
		assertTrue(eqiDateTime(orders.get(1).getEstimatedEndTime(), now.plusMinutes(260)));
		
		//Let's add the third order (60 mins cycle):
		cmcMotors.addOrder(orders.get(2));
		//The first & second order should be unaffected.
		assertTrue(eqiDateTime(orders.get(0).getEstimatedEndTime(), now.plusMinutes(190)));
		assertTrue(eqiDateTime(orders.get(1).getEstimatedEndTime(), now.plusMinutes(260)));
		//The thirs order will be done after 50 + 70 + 70 + 70 + 60 mins = 320 mins
		assertTrue(eqiDateTime(orders.get(2).getEstimatedEndTime(), now.plusMinutes(320)));
		//Lets to all tasks in workstation 1, thus progressing the line. 
		//We'll complete this phase 20 minutes early to check that the estimates come forwards too. 
		barry.setActiveWorkstation(cmcMotors.getWorkStations().get(0).toString());
		for(Task task : orders.get(0).getTasks()){
			if(cmcMotors.getWorkStations().get(0).getCapabilities().contains(task.getCarPart().type)){
				barry.doTask(task.toString(), orders.get(0).getPhaseTime() - 20);
			}
		}
		//assert that 30 mins have passed.
		assertTrue(eqiDateTime(now.plusMinutes(30), cmcMotors.getCurrentTime()));
		//assert that the estimates cascade as expected.
		assertTrue(eqiDateTime(orders.get(0).getEstimatedEndTime(), now.plusMinutes(170)));
		assertTrue(eqiDateTime(orders.get(1).getEstimatedEndTime(), now.plusMinutes(240)));
		assertTrue(eqiDateTime(orders.get(2).getEstimatedEndTime(), now.plusMinutes(300)));

	}
	
	
	@Test
	public void testDayOverflow(){
		//The day takes from 6:00 to 22:00
		// The day starts at 6:00. Let's pretend time passes to 151 minutes before closing time. This allows us to produce one order of model A
		DateTime now = cmcMotors.getCurrentTime();
		cmcMotors.moveAssemblyLine(16*60 - 151);
		now = now.plusMinutes(16 * 60 - 151);
		assertTrue(eqiDateTime(cmcMotors.getCurrentTime(),now));
		
		now = cmcMotors.getCurrentTime();
		
		//Lets start adding orders.
		cmcMotors.addOrder(orders.get(0));
		//the first car order is automatically added to the assemblyline. The estimated completion date should be in 1 minute before closing time.
		assertTrue(eqiDateTime(orders.get(0).getEstimatedEndTime(), now.plusMinutes(150)));//17:45
		//Adding order 2 (phase duration 70) should only start tomorrow.
		cmcMotors.addOrder(orders.get(1));
		MutableDateTime mu = now.toMutableDateTime();
		mu.addDays(1);
		mu.setHourOfDay(6);mu.setMinuteOfHour(0);
		mu.addMinutes(210);
		assertTrue(eqiDateTime(orders.get(1).getEstimatedEndTime(), mu.toDateTime()));//210 mins after 6:00 the next day.
	}
	
	/**
	 * Test the correct calculation of estimated completion times in case of overtime. 
	 * The overtime is set to 5 hours and the orders on the line have a phase duration of 60 minutes
	 */
	@Test
	public void testOvertimeOver2Hours(){
		//GO to 18:00
		cmcMotors.moveAssemblyLine(12 * 60);
		cmcMotors.addOrder(simpleOrders.get(0));
		barry.setActiveWorkstation(cmcMotors.getWorkStations().get(0).toString());
		for(Task task : simpleOrders.get(0).getTasks()){
			barry.doTask(task.toString(), 180);
		}
		barry.setActiveWorkstation(cmcMotors.getWorkStations().get(1).toString());
		for(Task task : simpleOrders.get(0).getTasks()){
			barry.doTask(task.toString(), 180);
		}
		barry.setActiveWorkstation(cmcMotors.getWorkStations().get(2).toString());
		for(Task task : simpleOrders.get(0).getTasks()){
			barry.doTask(task.toString(), 180);
		}
		//A new day has started. The overtime is 5 hours. The work day stops on 17:00
		//goto 13:59 the last moment to post an order
		assertTrue(cmcMotors.getCurrentTime().getHourOfDay() == 6 && cmcMotors.getCurrentTime().getMinuteOfHour() == 00);
		cmcMotors.moveAssemblyLine(7 * 60 + 59);
		DateTime now = cmcMotors.getCurrentTime();
		
		//Add 2 orders.
		cmcMotors.addOrder(simpleOrders.get(1));
		cmcMotors.addOrder(simpleOrders.get(2));
		
		assertTrue(eqiDateTime(simpleOrders.get(1).getEstimatedEndTime(), now.plusHours(3)));
		assertFalse(eqiDateTime(simpleOrders.get(2).getEstimatedEndTime(), now.plusHours(4)));
	}
	
	/**
	 * Test the correct calculation of estimated completion times in case of overtime. 
	 * The overtime is set to 5 hours and the orders on the line have a phase duration of 70 minutes
	 */
	@Test
	public void testOvertimeOver2HoursPhase70(){
		//Build a list of orders of duration 60.
		List<CarOrder> simpleOrders = new ArrayList<CarOrder>();
		for(int i = 0; i < 10; i++){
			simpleOrders.add(buildStandardOrderB());
		}
		
		//GO to 18:00
		cmcMotors.moveAssemblyLine(12 * 60);
		cmcMotors.addOrder(simpleOrders.get(0));
		barry.setActiveWorkstation(cmcMotors.getWorkStations().get(0).toString());
		for(Task task : simpleOrders.get(0).getTasks()){
			barry.doTask(task.toString(), 180);
		}
		barry.setActiveWorkstation(cmcMotors.getWorkStations().get(1).toString());
		for(Task task : simpleOrders.get(0).getTasks()){
			barry.doTask(task.toString(), 180);
		}
		barry.setActiveWorkstation(cmcMotors.getWorkStations().get(2).toString());
		for(Task task : simpleOrders.get(0).getTasks()){
			barry.doTask(task.toString(), 180);
		}
		//A new day has started. The overtime is 5 hours. The work day stops on 17:00
		//goto 13:59 like in the previous test. However this time the order can't be completed anymore.
		assertTrue(cmcMotors.getCurrentTime().getHourOfDay() == 6 && cmcMotors.getCurrentTime().getMinuteOfHour() == 00);
		cmcMotors.moveAssemblyLine(7 * 60 + 59);
		DateTime tomorrow = getNextDayStart(cmcMotors.getCurrentTime());
		
		//Add 2 orders.
		cmcMotors.addOrder(simpleOrders.get(1));
		cmcMotors.addOrder(simpleOrders.get(2));
		
		assertTrue(eqiDateTime(simpleOrders.get(1).getEstimatedEndTime(), tomorrow.plusMinutes(3 * 70)));
		assertTrue(eqiDateTime(simpleOrders.get(2).getEstimatedEndTime(), tomorrow.plusMinutes(4 * 70)));
	}
	
	/**
	 * Test the effect of negative overtime on the system. It should reset overtime to zero if a day ends early.
	 */
	@Test
	public void testNegativeOvertime(){
		int day1 = cmcMotors.getCurrentTime().getDayOfYear();
		cmcMotors.moveAssemblyLine(12 * 60 + 30); //18:30
		cmcMotors.moveAssemblyLine(60);//This will end the day and result in a theoretical overtime of -30. The overtime will be set to 0.
		//We can't access the overtime directly, however progressing time to 21:59 should not result in a day switch.
		assertTrue(cmcMotors.getCurrentTime().getDayOfYear() == day1 + 1);
		cmcMotors.moveAssemblyLine(16 * 60 - 151);
		assertFalse(cmcMotors.getCurrentTime().getDayOfYear() == day1 + 2);
	}
	
	/**
	 * A basic test of the assembly line functioning usinf .
	 */
	@Test
	public void testPrioritySchedulingEasy(){
		List<Order> ords = new ArrayList<Order>();
		ords.add(buildStandardOrderA());ords.add(buildStandardOrderA());ords.add(buildStandardOrderB());ords.add(buildStandardOrderC());
		//add the orders
		for(Order ord : ords){
			cmcMotors.addOrder(ord);
		}
		
		//SCHED: <stuff in the workstations>|<stuff in the queue>
		//SCHED: A|A-B-C
		
		//Set all with specification defined by standard order C with priority.
		cmcMotors.changeStrategy(buildStandardOrderC());
		
		//SCHED: A|C-A-B 
		//check that that C is scheduled after ords.get(0). ords.get(0) is automatically added to the assemblyline regardless of scheduling.
		assertTrue(ords.get(3).getEstimatedEndTime().isBefore(ords.get(1).getEstimatedEndTime()));
		assertTrue(ords.get(3).getEstimatedEndTime().isBefore(ords.get(2).getEstimatedEndTime()));
		assertTrue(ords.get(1).getEstimatedEndTime().isBefore(ords.get(2).getEstimatedEndTime()));
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
	
	/**
	 * Given the datetime indicating "now" this method returns the datetime indicating "tomorrow" at 6:00 AM
	 * @param now
	 * @return
	 */
	private DateTime getNextDayStart(DateTime now){
		MutableDateTime mu = now.toMutableDateTime();
		mu.addDays(1);
		mu.setHourOfDay(6);mu.setMinuteOfHour(0);
		return mu.toDateTime();
	}
	
	
}
