package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import interfaces.Printable;

import java.util.ArrayList;
import java.util.List;

import logic.assemblyline.AssemblyLine;
import logic.car.VehicleModel;
import logic.car.VehicleOrder;
import logic.car.VehicleOrderDetails;
import logic.car.VehicleOrderDetailsMaker;
import logic.car.VehiclePart;
import logic.car.Order;
import logic.car.TaskOrder;
import logic.car.TaskOrderDetailsMaker;
import logic.users.CarManufacturingCompany;
import logic.users.Mechanic;
import logic.workstation.Task;
import logic.workstation.Workstation;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.junit.Before;
import org.junit.Test;

/**
 * A test case to fully test the assembly line class.
 */
public class AssemblyLineTest {
	private CarManufacturingCompany cmcMotors;
	private List<VehicleOrder> orders = new ArrayList<VehicleOrder>();
	private Mechanic barry;
	private List<VehicleOrder> simpleOrders;
	
	/**
	 * Build a standard task order: duration 60
	 * @return	A standard task order with a duration of 60 minutes.
	 */
	private TaskOrder buildStandardTaskOrder(DateTime deadline){
		TaskOrderDetailsMaker maker = new TaskOrderDetailsMaker();
		maker.chooseDeadline(deadline);
		maker.choosePart(VehiclePart.COLOUR_BLACK);
		return new TaskOrder(maker.getDetails());
	}
	
	/**
	 * Commits orders to the system so that lines 2 & 3 are each filled with a queue of 100 orders. 
	 * Line 1 is still available and is empty. It accepts orders A & B.
	 * @param cmc
	 */
	private void swampLine2n3(CarManufacturingCompany cmc){
		//Swamp line 3.
		for(int i = 0; i < 100 ; i++){
			cmc.addOrder(buildStandardOrderX());
		}
		//swamp line 2.
		for(int i = 0; i < 100 ; i++){
			cmc.addOrder(buildStandardOrderC());
		}
	}
	
	private void completeFirstWS(AssemblyLine al){
		barry.setActiveAssemblyLine(al);
		barry.setActiveWorkstation(al.getWorkStations().get(0));
		for(Printable<Task> ptask : barry.getAvailableTasks()){
			Task task = (Task) ptask;
			barry.doTask(task, task.getEstimatedPhaseDuration());
		}
		
	}
	
	/**
	 * A quick test to check whether the builders defined above work. (correct specification)
	 */
	@Test
	public void testOrdersValid(){
		VehicleOrder order;
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
			if((i % 2) == 0){
				orders.add(buildStandardOrderA());
			} else if((i % 2) == 1){
				orders.add(buildStandardOrderB());
			}
		}
		
		//Build a list of orders of duration 60.
		simpleOrders = new ArrayList<VehicleOrder>();
		for(int i = 0; i < 10; i++){
			simpleOrders.add(buildStandardOrderC());
		}
	}
	

	/**
	 * This test tests the correct propagation of estimated completion times on one assemblyline. 
	 * We start by filling line 2 & 3 so that we can reuse tests to test correct estimations on one assemblyline. 
	 * 50 - 70 - 50 - 70 - ...
	 */
	@Test
	public void testEstimatesEasy(){
		// The day starts at 6:00.
		DateTime now = cmcMotors.getCurrentTime();
		
		//Add a load of orders to lines 2&3.
		swampLine2n3(cmcMotors);
		
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
		
		//Let's add the third order (50 mins cycle):
		cmcMotors.addOrder(orders.get(2));
		//The first & second order should be unaffected.
		assertTrue(eqiDateTime(orders.get(0).getEstimatedEndTime(), now.plusMinutes(190)));
		assertTrue(eqiDateTime(orders.get(1).getEstimatedEndTime(), now.plusMinutes(260)));
		//The thirs order will be done after 50 + 70 + 70 + 70 + 50 mins = 310 mins
		assertTrue(eqiDateTime(orders.get(2).getEstimatedEndTime(), now.plusMinutes(310)));
		//Lets to all tasks in workstation 1, thus progressing the line. 
		//We'll complete this phase 20 minutes early to check that the estimates come forwards too. 
		
		
		//Complete some tasks in lines 2 & 3 to allow line 1 to advance.
		completeFirstWS((AssemblyLine) cmcMotors.getAssemblyLines().get(1));
		completeFirstWS((AssemblyLine) cmcMotors.getAssemblyLines().get(2));
		
		barry.setActiveAssemblyLine(cmcMotors.getAssemblyLines().get(0));
		barry.setActiveWorkstation(cmcMotors.getWorkStationsFromAssemblyLine(cmcMotors.getAssemblyLines().get(0)).get(0));
		
		for(int i = 0; i < 3; i++){
			System.out.println(orders.get(i));
		}		
		
		for(Task task : orders.get(0).getTasks()){
			Workstation ws = (Workstation) cmcMotors.getWorkStationsFromAssemblyLine(cmcMotors.getAssemblyLines().get(1)).get(0);
			if(ws.getCapabilities().contains(task.getVehiclePart().type)){
				barry.doTask(task, task.getEstimatedPhaseDuration()-20);
			}
		}
		//assert that 30 mins have passed.
		//assertTrue(eqiDateTime(now.plusMinutes(30), cmcMotors.getCurrentTime()));
		//assert that the estimates cascade as expected.
		for(int i = 0; i < 3; i++){
			System.out.println(orders.get(i));
		}
		
		
		
		assertTrue(eqiDateTime(orders.get(0).getEstimatedEndTime(), now.plusMinutes(170)));
		assertTrue(eqiDateTime(orders.get(1).getEstimatedEndTime(), now.plusMinutes(240)));
		assertTrue(eqiDateTime(orders.get(2).getEstimatedEndTime(), now.plusMinutes(290)));

	}
	
	/**
	 * This test tests whether orders are correctly scheduled to the next day, when they can't be completed
	 * on the first day anymore.
	 */
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
		barry.setActiveWorkstation(cmcMotors.getWorkStations().get(0));
		for(Task task : simpleOrders.get(0).getTasks()){
			barry.doTask(task, 180);
		}
		barry.setActiveWorkstation(cmcMotors.getWorkStations().get(1));
		for(Task task : simpleOrders.get(0).getTasks()){
			barry.doTask(task, 180);
		}
		barry.setActiveWorkstation(cmcMotors.getWorkStations().get(2));
		for(Task task : simpleOrders.get(0).getTasks()){
			barry.doTask(task, 180);
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
		List<VehicleOrder> simpleOrders = new ArrayList<VehicleOrder>();
		for(int i = 0; i < 10; i++){
			simpleOrders.add(buildStandardOrderB());
		}
		
		//GO to 18:00
		cmcMotors.moveAssemblyLine(12 * 60);
		cmcMotors.addOrder(simpleOrders.get(0));
		barry.setActiveWorkstation(cmcMotors.getWorkStations().get(0));
		for(Task task : simpleOrders.get(0).getTasks()){
			barry.doTask(task, 180);
		}
		barry.setActiveWorkstation(cmcMotors.getWorkStations().get(1));
		for(Task task : simpleOrders.get(0).getTasks()){
			barry.doTask(task, 180);
		}
		barry.setActiveWorkstation(cmcMotors.getWorkStations().get(2));
		for(Task task : simpleOrders.get(0).getTasks()){
			barry.doTask(task, 180);
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
	 * A basic test of the batch scheduling strategy.
	 * We test correct refactoring of the queue as required. See code for detailed flow.
	 */
	@Test
	public void testPrioritySchedulingEasy(){
		List<Order> ords = new ArrayList<Order>();
		ords.add(buildStandardOrderA());ords.add(buildStandardOrderA());ords.add(buildStandardOrderB());ords.add(buildStandardOrderC());
		//add a basic order for models A, B & C
		for(Order ord : ords){
			cmcMotors.addOrder(ord);
		}
		
		//SCHED: <stuff in the workstations>|<stuff in the queue>
		//SCHED: A|A-B-C
		//		 0|1-2-3
		
		//Set priority for specification defined by standard order C.
		cmcMotors.changeStrategyActiveAssemblyLine(buildStandardOrderC());
		
		//SCHED: A|C-A-B 
		//		 0|3-1-2
		//check that that C is scheduled after ords.get(0). ords.get(0) is automatically added to the assemblyline regardless of scheduling.
		assertTrue(ords.get(3).getEstimatedEndTime().isBefore(ords.get(1).getEstimatedEndTime()));
		assertTrue(ords.get(3).getEstimatedEndTime().isBefore(ords.get(2).getEstimatedEndTime()));
		assertTrue(ords.get(1).getEstimatedEndTime().isBefore(ords.get(2).getEstimatedEndTime()));
		
		//Now we'll add another order of type C (priority)
		ords.add(buildStandardOrderC());
		cmcMotors.addOrder(ords.get(4));
		//SCHED: A|C-C-A-B 
		//		 0|3-4-1-2
		
		assertTrue(ords.get(3).getEstimatedEndTime().isBefore(ords.get(1).getEstimatedEndTime()));
		assertTrue(ords.get(3).getEstimatedEndTime().isBefore(ords.get(2).getEstimatedEndTime()));
		assertTrue(ords.get(1).getEstimatedEndTime().isBefore(ords.get(2).getEstimatedEndTime()));
		assertTrue(ords.get(4).getEstimatedEndTime().isBefore(ords.get(2).getEstimatedEndTime()));
		assertTrue(ords.get(4).getEstimatedEndTime().isBefore(ords.get(1).getEstimatedEndTime()));
		assertTrue(ords.get(3).getEstimatedEndTime().isBefore(ords.get(4).getEstimatedEndTime()));
		
		//Finally we'll add a non priority order.
		ords.add(buildStandardOrderB());
		cmcMotors.addOrder(ords.get(5));
		//SCHED: A|C-C-A-B-B 
		//		 0|3-4-1-2-5
		
		assertTrue(ords.get(3).getEstimatedEndTime().isBefore(ords.get(1).getEstimatedEndTime()));
		assertTrue(ords.get(3).getEstimatedEndTime().isBefore(ords.get(2).getEstimatedEndTime()));
		assertTrue(ords.get(1).getEstimatedEndTime().isBefore(ords.get(2).getEstimatedEndTime()));
		assertTrue(ords.get(4).getEstimatedEndTime().isBefore(ords.get(2).getEstimatedEndTime()));
		assertTrue(ords.get(4).getEstimatedEndTime().isBefore(ords.get(1).getEstimatedEndTime()));
		assertTrue(ords.get(3).getEstimatedEndTime().isBefore(ords.get(4).getEstimatedEndTime()));
		for(int i = 0; i < 5; i++){
			assertTrue(ords.get(i).getEstimatedEndTime().isBefore(ords.get(5).getEstimatedEndTime()));
		}
	}
	
	/**
	 * Test the correct estimated assemblytimes for TaskOrders. Using deadlines
	 */
	@Test
	public void testSingleTasks(){
		//Fill the schedule with phase 60 orders 
		for(int i = 0; i < simpleOrders.size(); i++){
			cmcMotors.addOrder(simpleOrders.get(i));
		}
		
		//Add a taskOrder with a dealine 4 hours after the startup of the system
		TaskOrder taskOrder = buildStandardTaskOrder(cmcMotors.getCurrentTime().plusHours(4));
		cmcMotors.addOrder(taskOrder);
		
		//simpleOrder 0 was placed on the lane and will be completed first. 
		assertTrue(simpleOrders.get(0).getEstimatedEndTime().isBefore(taskOrder.getEstimatedEndTime()));
		assertTrue(taskOrder.getEstimatedEndTime().isBefore(simpleOrders.get(1).getEstimatedEndTime()));
		assertTrue(simpleOrders.get(1).getEstimatedEndTime().isBefore(simpleOrders.get(2).getEstimatedEndTime()));
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
	 * @param now	The current time.
	 * @return		The datetime indicating "tomorrow" at 6:00 AM
	 */
	private DateTime getNextDayStart(DateTime now){
		MutableDateTime mu = now.toMutableDateTime();
		mu.addDays(1);
		mu.setHourOfDay(6);mu.setMinuteOfHour(0);
		return mu.toDateTime();
	}

	/**
	 * Build a standard order: duration 50, 50, 0
	 * @return	A standard order with a duration of 50 minutes.
	 */
	private VehicleOrder buildStandardOrderA(){
		VehiclePart[] partsArray = {
				VehiclePart.BODY_BREAK, 
				VehiclePart.COLOUR_RED,
				VehiclePart.ENGINE_4,
				VehiclePart.GEARBOX_5AUTO,
				VehiclePart.SEATS_LEATHER_WHITE,
				VehiclePart.AIRCO_MANUAL,
				VehiclePart.WHEELS_COMFORT,
				VehiclePart.SPOILER_NONE,
				VehiclePart.TOOLSTORAGE_NONE,
				VehiclePart.CARGO_NONE,
				VehiclePart.CERTIFICATION_NONE
			};
		
		VehicleOrderDetailsMaker maker = new VehicleOrderDetailsMaker(VehicleModel.CARMODELA);
		for(VehiclePart part : partsArray){
			maker.addPart(part);
		}
		return new VehicleOrder(maker.getDetails());
	}

	/**
	 * Build a standard order: duration 70, 70, 0
	 * @return	A standard order with a duration of 70 minutes.
	 */
	private VehicleOrder buildStandardOrderB(){
		VehiclePart[] partsArray = {
				VehiclePart.BODY_BREAK, 
				VehiclePart.COLOUR_RED,
				VehiclePart.ENGINE_4,
				VehiclePart.GEARBOX_5AUTO,
				VehiclePart.SEATS_LEATHER_WHITE,
				VehiclePart.AIRCO_MANUAL,
				VehiclePart.WHEELS_COMFORT,
				VehiclePart.SPOILER_NONE,
				VehiclePart.TOOLSTORAGE_NONE,
				VehiclePart.CARGO_NONE,
				VehiclePart.CERTIFICATION_NONE
			};
		
		VehicleOrderDetailsMaker maker = new VehicleOrderDetailsMaker(VehicleModel.CARMODELB);
		for(VehiclePart part : partsArray){
			maker.addPart(part);
		}
		return new VehicleOrder(maker.getDetails());
	}

	/**
	 * Build a standard order: duration 60, 60, 0
	 * @return	A standard order with a duration of 60 minutes.
	 */
	private VehicleOrder buildStandardOrderC(){
		VehiclePart[] partsArray = {
				VehiclePart.BODY_SPORT, 
				VehiclePart.COLOUR_BLACK,
				VehiclePart.ENGINE_8,
				VehiclePart.GEARBOX_6MANUAL,
				VehiclePart.SEATS_LEATHER_WHITE,
				VehiclePart.AIRCO_NONE,
				VehiclePart.WHEELS_SPORTS,
				VehiclePart.SPOILER_LOW,
				VehiclePart.TOOLSTORAGE_NONE,
				VehiclePart.CARGO_NONE,
				VehiclePart.CERTIFICATION_NONE
			};
		
		VehicleOrderDetailsMaker maker = new VehicleOrderDetailsMaker(VehicleModel.CARMODELC);
		for(VehiclePart part : partsArray){
			maker.addPart(part);
		}
		return new VehicleOrder(maker.getDetails());
	}

	/**
	 * Build a standard truck order: duration 60, 90, 30
	 * @return	A standard order with a duration of 60 minutes.
	 */
	private VehicleOrder buildStandardOrderX(){
		VehiclePart[] partsArray = {
				VehiclePart.BODY_PLATFORM, 
				VehiclePart.COLOUR_GREEN,
				VehiclePart.ENGINE_TRUCKSTANDARD,
				VehiclePart.GEARBOX_8MANUAL,
				VehiclePart.SEATS_VINYL_GRAY,
				VehiclePart.AIRCO_MANUAL,
				VehiclePart.WHEELS_HEAVY_DUTY,
				VehiclePart.SPOILER_NONE,
				VehiclePart.TOOLSTORAGE_STANDARD,
				VehiclePart.CARGO_STANDARD,
				VehiclePart.CERTIFICATION_STANDARD
			};
		
		VehicleOrderDetailsMaker maker = new VehicleOrderDetailsMaker(VehicleModel.TRUCKMODELX);
		for(VehiclePart part : partsArray){
			maker.addPart(part);
		}
		return new VehicleOrder(maker.getDetails());
	}

	/**
	 * Build a standard truck order: duration 60, 120, 45
	 * @return	A standard order with a duration of 60 minutes.
	 */
	private VehicleOrder buildStandardOrderY(){
		VehiclePart[] partsArray = {
				VehiclePart.BODY_PLATFORM, 
				VehiclePart.COLOUR_BLACK,
				VehiclePart.ENGINE_TRUCKSTANDARD,
				VehiclePart.GEARBOX_8MANUAL,
				VehiclePart.SEATS_VINYL_GRAY,
				VehiclePart.AIRCO_MANUAL,
				VehiclePart.WHEELS_HEAVY_DUTY,
				VehiclePart.SPOILER_NONE,
				VehiclePart.TOOLSTORAGE_STANDARD,
				VehiclePart.CARGO_STANDARD,
				VehiclePart.CERTIFICATION_STANDARD
			};
		
		VehicleOrderDetailsMaker maker = new VehicleOrderDetailsMaker(VehicleModel.TRUCKMODELY);
		for(VehiclePart part : partsArray){
			maker.addPart(part);
		}
		return new VehicleOrder(maker.getDetails());
	}
	
	
}
