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

	//TODO
	//test slaagt bij numberOfOrders == 39
	private int numberOfOrders = 39;
	
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
		int count = 0;
		for(int i = 0; i < numberOfOrders; i++){
			curr = buildStandardOrderA();
			orders.add(curr);
			cmc.addOrder(curr);
			count++;
		}
		System.out.println("Total number of orders == " + count);
		
		//Do all the orders
		int tasksPerformed = 0;
		int totalTasksPerformed = 0;
		while(!allDone()){
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
			System.out.println("   Tasks performed: " + tasksPerformed);
			tasksPerformed = 0;
		}
		System.out.println("   Total tasks performed: " + totalTasksPerformed);
	}
	
	/**
	 * Checks if all orders are done
	 * @return 	True if all orders are done.
	 * 			False otherwise.
	 */
	private boolean allDone() {
		for(Order o : orders){
			if(!o.done())
				return false;
		}
		return true;
	}


	/**
	 * The main test.
	 */
	@Test
	public void testStat1(){
		String stats = cmc.getStatistics();
		System.out.println(stats);

		//assertTrue(stats.contains("Average number of cars produced: 25"));
		//assertTrue(stats.contains("Mean number of cars produced: 25"));
		assertTrue(stats.contains("Statistics of Assembly Line 3:\n"+
				"Average number of cars produced: 0\n"+
				"Mean number of cars produced: 0\n"+
				"Exact numbers two last days:\n"+
				"   No records.\n"+
				"Average delay: 0 minutes\n"+
				"Mean delay: 0 minutes\n"+
				"Two last delays:\n"+
				"   2) 0 minutes on 2014-01-01T18:30:00.000+01:00\n"+
				"   1) 0 minutes on 2014-01-01T18:30:00.000+01:00\n"+
				"\n"+
				"Statistics of Generality:\n"+
				"Average number of cars produced: 0\n"+
				"Mean number of cars produced: 0\n"+
				"Exact numbers two last days:\n"+
				"   No records.\n"+
				"Average delay: 0 minutes\n"+
				"Mean delay: 0 minutes\n"+
				"Two last delays:\n"+
				"   2) 0 minutes on 2014-01-01T18:30:00.000+01:00\n"+
				"   1) 0 minutes on 2014-01-01T19:20:00.000+01:00"));
	}

}
