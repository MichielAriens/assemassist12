package tests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import logic.car.CarModel;
import logic.car.CarOrder;
import logic.car.CarOrderDetailsMaker;
import logic.car.CarPart;
import logic.car.Order;
import logic.users.CarManufacturingCompany;
import logic.users.Mechanic;
import logic.workstation.Task;

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
	 * Build a standard order: duration 60
	 * @return	A standard order with a duration of 60 minutes.
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
			mechs.add(new Mechanic(cmc, "SuperMech2014"));
			mechs.get(i).setActiveWorkstation(cmc.getWorkStations().get(i));
		}
		
		//Build n orders
		Order curr;
		for(int i = 0; i < 200; i++){
			curr = buildStandardOrderC();
			orders.add(curr);
			cmc.addOrder(curr);
		}
		
		//Do all the orders
		while(!orders.get(orders.size()-1).done()){
			for(Mechanic mech : mechs){
				for(Task task : mech.getAvailableTasks()){
					mech.doTask(task, 60);
				}
			}
		}
	}
	
	
	/**
	 * The main test.
	 */
	@Test
	public void testStat1(){
		String stats = cmc.getStatistics();
		assertTrue(stats.contains("Average number of cars produced: 13"));
		assertTrue(stats.contains("Mean number of cars produced: 13"));
	}

}
