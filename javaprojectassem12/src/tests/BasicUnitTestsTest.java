package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import logic.car.CarModel;
import logic.car.CarPart;
import logic.car.CarSpecification;
import java.security.InvalidParameterException;

import org.junit.Before;
import org.junit.Test;

public class BasicUnitTestsTest {
	
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
		CarSpecification cars = new CarSpecification(CarModel.MODEL1, carparts);
	}
	
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
		CarSpecification cars = new CarSpecification(CarModel.MODEL1, carparts);
	}
	
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
		CarSpecification cars = new CarSpecification(null, carparts);
	}
	
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
		CarSpecification cars = new CarSpecification(CarModel.MODEL1, carparts);
	}

}
