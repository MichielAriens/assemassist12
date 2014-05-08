package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import logic.car.VehicleModel;
import logic.car.VehiclePart;

import org.joda.time.DateTime;
import org.junit.Test;

import logic.car.*;
import logic.workstation.Task;

/**
 * A class that tests the order details from top to bottom.
 */
public class OrderDetailsTests {

	/**
	 * Tests a simple testcase: ModelA set up propperly.
	 */
	@Test
	public void testValidEasy() {
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
		
		for(VehiclePart cp : partsArray){
			maker.addPart(cp);
		}
		
		CarOrderDetails details = maker.getDetails();
		List<VehiclePart> detailsParts = new ArrayList<VehiclePart>();
		for(Task task : details.getPendingTasks()){
			detailsParts.add(task.getCarPart());
		}
		assertTrue(detailsParts.containsAll(Arrays.asList(partsArray)));
	}
	
	/**
	 * Tests ModelA being set up incorrectly. The details produced should be null.
	 */
	@Test
	public void testINValidEasy() {
		VehiclePart[] partsArray = {
				VehiclePart.BODY_BREAK, 
				VehiclePart.COLOUR_RED,
				VehiclePart.ENGINE_4,
				VehiclePart.AIRCO_MANUAL,
				VehiclePart.WHEELS_COMFORT,
				VehiclePart.SPOILER_NONE
			};
		
		CarOrderDetailsMaker maker = new CarOrderDetailsMaker(VehicleModel.CARMODELA);
		
		for(VehiclePart cp : partsArray){
			maker.addPart(cp);
		}
		
		CarOrderDetails details = maker.getDetails();
		assertNull(details);
	}
	
	/**
	 * Tests a model with sports body with mandatory spoiler
	 */
	@Test
	public void testSportsBodyWithSpoiler() {
		VehiclePart[] partsArray = {
				VehiclePart.BODY_SPORT, 
				VehiclePart.COLOUR_BLACK,
				VehiclePart.ENGINE_8,
				VehiclePart.GEARBOX_6MANUAL,
				VehiclePart.SEATS_LEATHER_WHITE,
				VehiclePart.AIRCO_NONE,
				VehiclePart.WHEELS_SPORTS,
				VehiclePart.SPOILER_LOW
			};
		
		CarOrderDetailsMaker maker = new CarOrderDetailsMaker(VehicleModel.CARMODELC);
		
		for(VehiclePart cp : partsArray){
			maker.addPart(cp);
		}
		
		CarOrderDetails details = maker.getDetails();
		List<VehiclePart> detailsParts = new ArrayList<VehiclePart>();
		for(Task task : details.getPendingTasks()){
			detailsParts.add(task.getCarPart());
		}
		assertTrue(detailsParts.containsAll(Arrays.asList(partsArray)));
	}
	
	/**
	 * Tests a model with sports body without the mandatory spoiler
	 * The details produced should be null.
	 */
	@Test
	public void testSportsBodyWithoutSpoiler() {
		VehiclePart[] partsArray = {
				VehiclePart.BODY_SPORT, 
				VehiclePart.COLOUR_RED,
				VehiclePart.ENGINE_8,
				VehiclePart.GEARBOX_6MANUAL,
				VehiclePart.SEATS_LEATHER_WHITE,
				VehiclePart.AIRCO_NONE,
				VehiclePart.WHEELS_SPORTS,
				VehiclePart.SPOILER_NONE
			};
		
		CarOrderDetailsMaker maker = new CarOrderDetailsMaker(VehicleModel.CARMODELC);
		
		for(VehiclePart cp : partsArray){
			maker.addPart(cp);
		}
		
		CarOrderDetails details = maker.getDetails();
		assertNull(details);
	}
	
	/**
	 * Tests the builder of the task orders and the task details.
	 */
	@Test
	public void testTaskOrderDetatilsMaker(){
		TaskOrderDetailsMaker maker = new TaskOrderDetailsMaker();
		maker.choosePart(maker.getAvailableParts(VehiclePartType.Colour).get(0));
		maker.chooseDeadline(DateTime.now().plusHours(5));
		TaskOrderDetails details = maker.getDetails();
		assertNotNull(details);
		assertTrue(details.getPendingTasks().get(0).getCarPart().type == VehiclePartType.Colour);
	}
}
