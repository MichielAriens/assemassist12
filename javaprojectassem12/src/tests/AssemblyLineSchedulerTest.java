package tests;

import static org.junit.Assert.*;
import init.DataLoader;
import interfaces.Printable;

import java.util.ArrayList;
import java.util.List;

import logic.assemblyline.AssemblyLine;
import logic.assemblyline.OperationalStatus;
import logic.order.Order;
import logic.order.VehicleModel;
import logic.order.VehicleOrder;
import logic.order.VehicleOrderDetailsMaker;
import logic.order.VehiclePart;
import logic.order.VehiclePartType;
import logic.users.CarManufacturingCompany;
import logic.users.Mechanic;
import logic.workstation.Task;
import logic.workstation.Workstation;

import org.joda.time.DateTime;
import org.junit.Test;

public class AssemblyLineSchedulerTest {
	
	/**
	 * Build a standard order: duration 50
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
				VehiclePart.CERTIFICATION_NONE,
				VehiclePart.CARGO_NONE
			};
		
		VehicleOrderDetailsMaker maker = new VehicleOrderDetailsMaker(VehicleModel.CARMODELA);
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


	/**
	 * Test the basic case where 3 A orders are placed into the empty system. 
	 * We expect the estimated completiontimes to fall together as they aredistibuted over the lines.
	 * A fourth order will be scheduled later.
	 */
	@Test
	public void testBasicOrder(){
		//First three orders have the same est completiontime
		List<Order> orders = new ArrayList<>(3);
		for(int i = 0; i < 3 ; i++){
			orders.add(buildStandardOrderA());
		}
		CarManufacturingCompany cmc = new CarManufacturingCompany();
		for(Order order : orders){
			cmc.addOrder(order);
		}
		DateTime est = cmc.getCurrentTime();
		est = est.plusMinutes(150);
		for(Order order : orders){
			assertTrue(eqiDateTime(order.getEstimatedEndTime(),est));
		}
		
		//One more order which should finish 50 mins later.
		orders.add(buildStandardOrderA());
		cmc.addOrder(orders.get(orders.size()-1));
		assertTrue(eqiDateTime(orders.get(orders.size()-1).getEstimatedEndTime(),est.plusMinutes(50)));
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
	
	@Test
	public void testDifficultCase(){
		CarManufacturingCompany cmc = new CarManufacturingCompany();
		List<Order> orders = new ArrayList<Order>();
		orders.add(buildStandardOrderX());
		orders.add(buildStandardOrderY());
		orders.add(buildStandardOrderX());
		orders.add(buildStandardOrderA());
		
		for(Order order : orders){
			cmc.addOrder(order);
		}
	}
	
	@Test
	public void testDoTask(){
		CarManufacturingCompany cmc = new CarManufacturingCompany();
		Order order = buildStandardOrderA();
		cmc.addOrder(order);
		AssemblyLine line = null;
		for(Printable<AssemblyLine> pr : cmc.getAssemblyLines()){
			AssemblyLine al = (AssemblyLine) pr;
			if(!al.empty()){
				line = al;
			}
		}
		
		cmc.getAssemblyLines();
		for(Task task : order.getTasks()){
			if(task.getVehiclePart().type == VehiclePartType.Body || task.getVehiclePart().type == VehiclePartType.Colour){
				cmc.doTask(task , line, 50);
			}
		}
		
	}
	
	/**
	 * Assert that the data loaders leaves the system at the start of a new day.
	 */
	@Test 
	public void testDataLoaderStartsNewDay(){
		//Initialize the startup sequence.
		CarManufacturingCompany cmc = new CarManufacturingCompany();
		DataLoader loader = new DataLoader(cmc);
		loader.loadData();
		
		//Check next day
		assertTrue(AssemblyLineTest.eqiDateTime(cmc.getCurrentTime(), new DateTime(2014,1,2,6,0)));
		// Check that 50 cars have been produced.
		assertTrue(cmc.getStatistics().contains("Statistics of Generality:\nAverage number of cars produced: 50"));
	}
	
	/**
	 * Test the correct placement of orders: Does each order get scheduled onto the expected assemblyline?
	 */
	@Test
	public void testCorrectOrderPlacing(){
		//Model X can only be scheduled on line 3.
		CarManufacturingCompany cmc = new CarManufacturingCompany();
		Order order = buildStandardOrderX();
		cmc.addOrder(order);
		
		List<AssemblyLine> lines = extractPrintables(cmc.getAssemblyLines());
		assertTrue(lines.get(0).empty());
		assertTrue(lines.get(1).empty());
		List<Workstation> stations = extractPrintables(lines.get(2).getWorkStations());
		assertTrue(stations.get(0).getCurrentOrder().equals(order));
		assertTrue(AssemblyLineTest.eqiDateTime(order.getEstimatedEndTime(), new DateTime(2014,1,1,11,0)));
		
		//Model C can be scheduled on line 2 & 3 but will be scheduled on 2 because of the better estimated completion time.
		order = buildStandardOrderC();
		cmc.addOrder(order);
		assertTrue(lines.get(0).empty());
		stations = extractPrintables(lines.get(1).getWorkStations());
		assertTrue(stations.get(0).getCurrentOrder().equals(order));
		assertTrue(AssemblyLineTest.eqiDateTime(order.getEstimatedEndTime(), new DateTime(2014,1,1,9,0)));
		
		//Model A can be scheduled on all lines but will be scheduled on 1 because of the better estimated completion time.
		order = buildStandardOrderA();
		cmc.addOrder(order);
		stations = extractPrintables(lines.get(0).getWorkStations());
		assertTrue(stations.get(0).getCurrentOrder().equals(order));
		assertTrue(AssemblyLineTest.eqiDateTime(order.getEstimatedEndTime(), new DateTime(2014,1,1,8,30)));
	}
	
	/**
	 * We'll commit 30 orders the system. Then we'll break assemblyline 1. We'll see if the orders in the queue on line 1 are redistributed correctly.
	 * We'll try to complete all completable work on the system and assert that all orders except the order on line 1 are complete and that
	 * th order on line 1 is still stuck int line 1 station 1.
	 */
	@Test
	public void testBrokenLines(){
		CarManufacturingCompany cmc = new CarManufacturingCompany();
		for(int i = 0; i < 15 ; i++){//30
			cmc.addOrder(buildStandardOrderA());
		}
		List<AssemblyLine> lines = extractPrintables(cmc.getAssemblyLines());
		cmc.changeAssemblyLineStatus(cmc.getAssemblyLines().get(0), OperationalStatus.BROKEN);
		performAllTasks(cmc);
		List<Workstation> stations = extractPrintables(lines.get(0).getWorkStations());
		assertTrue(stations.get(0).getCurrentOrder() != null);
		assertTrue(lines.get(1).empty());
		assertTrue(lines.get(2).empty());
		cmc.changeAssemblyLineStatus(cmc.getAssemblyLines().get(0), OperationalStatus.OPERATIONAL);
		for(Workstation ws : extractPrintables(lines.get(0).getWorkStations())){
			for(Task task : extractPrintables(cmc.getRequiredTasks(ws, lines.get(0)))){
				cmc.doTask(task, lines.get(0), task.getEstimatedPhaseDuration());
			}
		}
		
		
		assertTrue(lines.get(0).empty());
		assertTrue(lines.get(1).empty());
		assertTrue(lines.get(2).empty());		
	}
	
	
	@Test
	public void testMaintenanceEasy(){
		
	}
	
	/**
	 * Tests the correct processing of maintenance on one assemblyline. Two lines are dissabled (by setting them to BROKEN).
	 * The other line recieves an order and is set to maintence. That order is completed and we test than maintence completes with the right timing.
	 * We also test the correct continuation of the system afterwards. 
	 */
	@Test
	public void testMaintenace(){
		//init
		CarManufacturingCompany cmc = new CarManufacturingCompany();
		DateTime now = cmc.getCurrentTime();
		cmc.changeAssemblyLineStatus(cmc.getAssemblyLines().get(1), OperationalStatus.BROKEN);
		cmc.changeAssemblyLineStatus(cmc.getAssemblyLines().get(2), OperationalStatus.BROKEN);
		
		for(int i = 0; i < 30; i++){
			cmc.addOrder(buildStandardOrderA());
		}

		cmc.changeAssemblyLineStatus(cmc.getAssemblyLines().get(0), OperationalStatus.MAINTENANCE);
		assertTrue(AssemblyLineTest.eqiDateTime(now, cmc.getCurrentTime()));
		performAllTasks(cmc);
		
		cmc.addOrder(buildStandardOrderA());
		
		assertTrue(AssemblyLineTest.eqiDateTime(now.plusMinutes(390), cmc.getCurrentTime()));
		
		performAllTasks(cmc);

		assertTrue(AssemblyLineTest.eqiDateTime(now.plusMinutes(390 + 150), cmc.getCurrentTime()));
		
	}
	
	/**
	 * Tests that maintenace is postponed to the next day when end of day occurs at the point of the maintenance.
	 */
	@Test
	public void testMaintenaceEndOfDay(){
		CarManufacturingCompany cmc = new CarManufacturingCompany();
		DateTime now = cmc.getCurrentTime();
		for(int i = 0; i < 43; i++){
			cmc.addOrder(buildStandardOrderA());
		}
		performAllTasks(cmc);
		
		
		cmc.changeAssemblyLineStatus(cmc.getAssemblyLines().get(1), OperationalStatus.BROKEN);
		cmc.changeAssemblyLineStatus(cmc.getAssemblyLines().get(2), OperationalStatus.BROKEN);
		cmc.changeAssemblyLineStatus(cmc.getAssemblyLines().get(0), OperationalStatus.MAINTENANCE);
		
		assertTrue(AssemblyLineTest.eqiDateTime(now.plusDays(1).plusHours(4), cmc.getCurrentTime()));
	}
	
	/**
	 * Test the correct opperation of the assemblylinecheduler when preempting maintenance.
	 * This both in coming from the pre-maintenace pahse as the actual maintenace phase.
	 */
	@Test
	public void testPreemtMaintenace(){
		CarManufacturingCompany cmc = new CarManufacturingCompany();
		//Place an order on any line 2.
		cmc.changeAssemblyLineStatus(cmc.getAssemblyLines().get(2), OperationalStatus.BROKEN);
		cmc.changeAssemblyLineStatus(cmc.getAssemblyLines().get(0), OperationalStatus.BROKEN);
		cmc.addOrder(buildStandardOrderA());
		cmc.changeAssemblyLineStatus(cmc.getAssemblyLines().get(0), OperationalStatus.OPERATIONAL);
		
		//Set line 1 to maintenance (maintenance)
		cmc.changeAssemblyLineStatus(cmc.getAssemblyLines().get(0), OperationalStatus.MAINTENANCE);
		//Set line 2 to maintenance (premaintenance)
		cmc.changeAssemblyLineStatus(cmc.getAssemblyLines().get(1), OperationalStatus.MAINTENANCE);
		
		//Check
		assertEquals(OperationalStatus.MAINTENANCE, cmc.getAssemblyLinesStatuses().get(cmc.getAssemblyLines().get(0)));
		assertEquals(OperationalStatus.PREMAINTENANCE, cmc.getAssemblyLinesStatuses().get(cmc.getAssemblyLines().get(1)));
		
		//Do one workstation on line 2 to advance some time.
		Workstation ws = extractPrintable(extractPrintable(cmc.getAssemblyLines().get(1)).getWorkStations().get(0));
		for(Task task : extractPrintables(ws.getRequiredTasks(ws))){
			cmc.doTask(task, cmc.getAssemblyLines().get(1), task.getEstimatedPhaseDuration());
		}
		
		//Change all lines to operational
		cmc.changeAssemblyLineStatus(cmc.getAssemblyLines().get(0), OperationalStatus.OPERATIONAL);
		assertEquals(OperationalStatus.OPERATIONAL, cmc.getAssemblyLinesStatuses().get(cmc.getAssemblyLines().get(0)));
		
		cmc.changeAssemblyLineStatus(cmc.getAssemblyLines().get(1), OperationalStatus.OPERATIONAL);
		assertEquals(OperationalStatus.OPERATIONAL, cmc.getAssemblyLinesStatuses().get(cmc.getAssemblyLines().get(1)));	
		
		cmc.changeAssemblyLineStatus(cmc.getAssemblyLines().get(2), OperationalStatus.OPERATIONAL);
		assertEquals(OperationalStatus.OPERATIONAL, cmc.getAssemblyLinesStatuses().get(cmc.getAssemblyLines().get(2)));	
	}
	
	/**
	 * Extract the underlying objects from it's printable wrapper in a list.
	 * @param <T>
	 * @param printables
	 * @return
	 */
	private static <T> List<T> extractPrintables(final List<Printable<T>> printables){
		List<T> lines = new ArrayList<>();
		for(Printable<T> printable : printables){
			lines.add(extractPrintable(printable));
		}
		return lines;
	}
	
	/**
	 * Extract the underlying object form it's printable wrapper.
	 * @param printable
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static <T> T extractPrintable(final Printable<T> printable){
		return (T) printable;
	}
	
	/**
	 * Do all task on a carManufacturingCompany for testing purposes.
	 * @param company
	 */
	private void performAllTasks(CarManufacturingCompany company){
		Mechanic mech = (Mechanic) company.logIn("mech");
		boolean taskPerformed = true;
		while(taskPerformed){
			taskPerformed = false;
			for(Printable<AssemblyLine> line : mech.getAssemblyLines()){
				mech.setActiveAssemblyLine(line);
				for(Printable<Workstation> station : mech.getWorkstationsFromAssemblyLine()){
					mech.setActiveWorkstation(station);
					for(Printable<Task> task : mech.getAvailableTasks()){
						int duration = ((Task) task).getEstimatedPhaseDuration();
						if(mech.doTask(task, duration))
							taskPerformed = true;
					}
				}
			}
		}
	}
}
