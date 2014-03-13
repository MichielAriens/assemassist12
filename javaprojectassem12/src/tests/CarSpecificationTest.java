package tests;

import static org.junit.Assert.assertTrue;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import logic.car.CarModel;
import logic.car.CarPart;
import logic.car.CarSpecification;

import org.junit.Test;

/**
 * A test case used to test the CarSpecification class.
 */
public class CarSpecificationTest {
	
	/**
	 * Test a valid case of making a new car specification.
	 */
	@Test
	public void testCarSpecificationLegal(){
		ArrayList<CarPart> carparts = new ArrayList<CarPart>();
		carparts.add(CarPart.BODY_BREAK);
		carparts.add(CarPart.COLOUR_BLACK);
		carparts.add(CarPart.SEATS_LEATHER_BLACK);
		carparts.add(CarPart.AIRCO_AUTO);
		carparts.add(CarPart.WHEELS_COMFORT);
		carparts.add(CarPart.ENGINE_4);
		carparts.add(CarPart.GEARBOX_5AUTO);
		new CarSpecification(CarModel.MODEL1, carparts);
	}
	
	/**
	 * Test an invalid case of making a new car specification.
	 */
	@Test(expected=InvalidParameterException.class)
	public void testCarSpecificationIllegal1(){
		ArrayList<CarPart> carparts = new ArrayList<CarPart>();
		carparts.add(CarPart.BODY_BREAK);
		carparts.add(CarPart.BODY_SEDAN);
		carparts.add(CarPart.SEATS_LEATHER_BLACK);
		carparts.add(CarPart.AIRCO_AUTO);
		carparts.add(CarPart.WHEELS_COMFORT);
		carparts.add(CarPart.ENGINE_4);
		carparts.add(CarPart.GEARBOX_5AUTO);
		new CarSpecification(CarModel.MODEL1, carparts);
	}
	
	/**
	 * Test an invalid case of making a new car specification.
	 */
	@Test(expected=InvalidParameterException.class)
	public void testCarSpecificationIllegal2(){
		ArrayList<CarPart> carparts = new ArrayList<CarPart>();
		carparts.add(CarPart.BODY_BREAK);
		carparts.add(CarPart.BODY_SEDAN);
		carparts.add(CarPart.SEATS_LEATHER_BLACK);
		carparts.add(CarPart.AIRCO_AUTO);
		carparts.add(CarPart.WHEELS_COMFORT);
		carparts.add(CarPart.ENGINE_4);
		carparts.add(CarPart.GEARBOX_5AUTO);
		new CarSpecification(null, carparts);
	}
	
	/**
	 * Test an invalid case of making a new car specification.
	 */
	@Test(expected=InvalidParameterException.class)
	public void testCarSpecificationIllegal3(){
		ArrayList<CarPart> carparts = new ArrayList<CarPart>();
		carparts.add(CarPart.BODY_BREAK);
		carparts.add(CarPart.BODY_SEDAN);
		carparts.add(CarPart.BODY_SEDAN);
		carparts.add(CarPart.SEATS_LEATHER_BLACK);
		carparts.add(CarPart.AIRCO_AUTO);
		carparts.add(CarPart.WHEELS_COMFORT);
		carparts.add(CarPart.ENGINE_4);
		carparts.add(CarPart.GEARBOX_5AUTO);
		new CarSpecification(CarModel.MODEL1, carparts);
	}
	
	/**
	 * Test to whether it is possible to reverse a toString() using a getPartfromString().
	 */
	@Test
	public void testCarPartMethods(){
		CarPart someCarPart = CarPart.AIRCO_AUTO;
		assertTrue(CarPart.getPartfromString(someCarPart.toString()) == someCarPart);
	}
	
	/**
	 * Test the validPart(CarPart part)
	 */
	@Test
	public void testCarModel(){
		CarModel model = CarModel.MODEL1;
		assertTrue(model.validPart(CarPart.AIRCO_AUTO));
	}

}
