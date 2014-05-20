package tests;

import static org.junit.Assert.assertTrue;
import interfaces.Printable;

import java.util.ArrayList;
import java.util.List;

import logic.assemblyline.AssemblyLine;
import logic.car.VehicleModel;
import logic.car.VehicleOrder;
import logic.car.VehicleOrderDetailsMaker;
import logic.car.VehiclePart;
import logic.car.Order;
import logic.users.CarManufacturingCompany;
import logic.users.Mechanic;
import logic.workstation.Task;
import logic.workstation.Workstation;

import org.junit.Before;
import org.junit.Test;

/**
 * A test case to test the statistics.
 */
public class StatisticsTests {
	
	private CarManufacturingCompany cmc;
	private List<Mechanic> mechs = new ArrayList<Mechanic>();
	private List<Order> orders = new ArrayList<Order>();
	
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
	 * Build a standard VehicleOrderDetails of model A.
	 * @return	A standard VehicleOrderDetails of model A.
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
	 * n = 1000
	 * Set up an environment where: 
	 * 		n orders have been submitted
	 * 		n orders have been done
	 * 		each order has duration 60.
	 */
	@Before
	public void prequel(){
		//initialize the required actors
		cmc = new CarManufacturingCompany();
		for(int i = 0; i < 3; i++){
			mechs.add(new Mechanic(cmc, "SuperMech2014Nr" + (i+1)));
		}
		
		//Build n orders
		Order curr;
		//for(int i = 0; i < 19; i++){ dag gaat vooruit; 1 day ago == 9
		//for(int i = 0; i < 21; i++){ 1 day ago == 10
		//for(int i = 0; i < 22; i++){ 1 day ago == 11
		//for(int i = 0; i < 23; i++){ 1 day ago == 23
		//for(int i = 0; i < 70; i++){ 1 day ago: 9; 2 days ago: 26
		//ORDER C
		int count = 0;
//		for(int i = 0; i < 40; i++){ checkEndOfDay() in schedule is fout???
		for(int i = 0; i < 37; i++){
			curr = buildStandardOrderA();
			orders.add(curr);
			cmc.addOrder(curr);
			count++;
		}
		System.out.println("Total number of orders == " + count);
		
		//Do all the orders
		int tasksPerformed = 0;
		int totalTasksPerformed = 0;
		while(!orders.get(orders.size()-1).done()){
			for(Mechanic mech : mechs){
				for(Printable<AssemblyLine> line : mech.getAssemblyLines()){
					mech.setActiveAssemblyLine(line);
					for(Printable<Workstation> station : mech.getWorkstationsFromAssemblyLine()){
						mech.setActiveWorkstation(station);
						for(Printable<Task> task : mech.getAvailableTasks()){
							mech.doTask(task, 50);
							tasksPerformed++;
							totalTasksPerformed++;
						}
					}
				}
			}
//			if(tasksPerformed == 0){
//				System.err.println("SOMETING WONG? cause 0 tasks performed");
//				break;
//			}
			System.out.println("   Tasks performed: " + tasksPerformed);
			tasksPerformed = 0;
		}
		System.out.println("   Total tasks performed: " + totalTasksPerformed);
	}
	
	
	/**
	 * The main test.
	 */
	@Test
	public void testStat1(){
		String stats = cmc.getStatistics();
		System.out.println(stats);
		
		assertTrue(stats.contains("Average number of cars produced: 25"));
		assertTrue(stats.contains("Mean number of cars produced: 25"));
	}

}
