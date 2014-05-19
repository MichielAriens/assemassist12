package tests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import logic.car.CarOrderDetailsMaker;
import logic.car.Order;
import logic.car.VehicleModel;
import logic.car.VehicleOrder;
import logic.car.VehiclePart;
import logic.users.CarManufacturingCompany;

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
		
		CarOrderDetailsMaker maker = new CarOrderDetailsMaker(VehicleModel.CARMODELA);
		for(VehiclePart part : partsArray){
			maker.addPart(part);
		}
		return new VehicleOrder(maker.getDetails());
	}
	
	
	@Test
	public void testBasicOrder(){
		List<Order> orders = new ArrayList<>(3);
		for(int i = 0; i < 4 ; i++){
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
	

}
