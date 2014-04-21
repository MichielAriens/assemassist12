package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import logic.car.CarModel;
import logic.car.CarPart;

import org.joda.time.DateTime;
import org.junit.Test;

import logic.car.*;
import logic.users.CarManufacturingCompany;
import logic.users.CustomsManager;
import logic.workstation.Task;
public class OrderDetailsTests {

	/**
	 * Tests a simple testcase: ModelA set up propperly.
	 */
	@Test
	public void testValidEasy() {
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
		
		for(CarPart cp : partsArray){
			maker.addPart(cp);
		}
		
		CarOrderDetails details = maker.getDetails();
		List<CarPart> detailsParts = new ArrayList<CarPart>();
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
		CarPart[] partsArray = {
				CarPart.BODY_BREAK, 
				CarPart.COLOUR_RED,
				CarPart.ENGINE_4,
				CarPart.AIRCO_MANUAL,
				CarPart.WHEELS_COMFORT,
				CarPart.SPOILER_NONE
			};
		
		CarOrderDetailsMaker maker = new CarOrderDetailsMaker(CarModel.MODELA);
		
		for(CarPart cp : partsArray){
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
		
		for(CarPart cp : partsArray){
			maker.addPart(cp);
		}
		
		CarOrderDetails details = maker.getDetails();
		List<CarPart> detailsParts = new ArrayList<CarPart>();
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
		CarPart[] partsArray = {
				CarPart.BODY_SPORT, 
				CarPart.COLOUR_RED,
				CarPart.ENGINE_8,
				CarPart.GEARBOX_6MANUAL,
				CarPart.SEATS_LEATHER_WHITE,
				CarPart.AIRCO_NONE,
				CarPart.WHEELS_SPORTS,
				CarPart.SPOILER_NONE
			};
		
		CarOrderDetailsMaker maker = new CarOrderDetailsMaker(CarModel.MODELC);
		
		for(CarPart cp : partsArray){
			maker.addPart(cp);
		}
		
		CarOrderDetails details = maker.getDetails();
		assertNull(details);
	}
	
	/**
	 * 
	 */
	@Test
	public void testTaskOrderDetatilsMaker(){
		TaskOrderDetailsMaker maker = new TaskOrderDetailsMaker();
		maker.choosePart(maker.getAvailableParts(CarPartType.Colour).get(0));
		maker.chooseDeadline(DateTime.now().plusHours(5));
		TaskOrderDetails details = maker.getDetails();
		assertNotNull(details);
		assertTrue(details.getPendingTasks().get(0).getCarPart().type == CarPartType.Colour);
	}
}
