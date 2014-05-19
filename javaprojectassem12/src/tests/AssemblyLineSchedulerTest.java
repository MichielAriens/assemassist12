package tests;

import logic.car.CarOrderDetailsMaker;
import logic.car.Order;
import logic.car.VehicleModel;
import logic.car.VehicleOrder;
import logic.car.VehiclePart;

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
				VehiclePart.SPOILER_NONE
			};
		
		CarOrderDetailsMaker maker = new CarOrderDetailsMaker(VehicleModel.CARMODELA);
		for(VehiclePart part : partsArray){
			maker.addPart(part);
		}
		return new VehicleOrder(maker.getDetails());
	}
	
	
	@Test
	public void testBasicOrder(){
		Order order = buildStandardOrderA();
		assertTrue(order != null);
	}
	

}
