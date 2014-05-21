package tests;

import static org.junit.Assert.assertTrue;
import init.DataLoader;
import interfaces.Printable;

import java.util.ArrayList;
import java.util.List;

import logic.assemblyline.AssemblyLine;
import logic.car.VehicleOrderDetailsMaker;
import logic.car.Order;
import logic.car.VehicleModel;
import logic.car.VehicleOrder;
import logic.car.VehiclePart;
import logic.car.VehiclePartType;
import logic.users.CarManufacturingCompany;
import logic.workstation.Task;

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
		List<Order> orders = new ArrayList<Order>();
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
	 * Test the correct calculation of the time of the system when processing orders.
	 */
	@Test
	public void testCorrectOrderPlacing(){
		CarManufacturingCompany cmc = new CarManufacturingCompany();
		cmc.addOrder(buildStandardOrderX());
		
	}
	
	/**
	 * Method for extracting real objects from thei
	 * @param <T>
	 * @param printables
	 * @return
	 */
	private <T> List<T> extractPrintable(final List<Printable<T>> printables){
		List<T> lines = new ArrayList<>();
		for(Printable<T> printable : printables){
			lines.add((T) printable);
		}
		return lines;
	}

}
