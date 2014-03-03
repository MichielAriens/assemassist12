package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import logic.car.CarModel;
import logic.car.CarOrder;
import logic.car.CarPart;
import logic.car.CarPartType;
import logic.car.CarSpecification;
import logic.users.CarManufacturingCompany;
import logic.users.GarageHolder;
import logic.workstation.CarBodyPost;

import org.junit.Before;
import org.junit.Test;

public class WorkStationTests {
	CarBodyPost carBodyPost;
	GarageHolder garageHolder;
	CarManufacturingCompany carManufacturingCompany;
	CarSpecification carSpecification;
	CarOrder carOrder;
	
	@Before
	public void prequel(){
		carBodyPost = new CarBodyPost();
		
		carManufacturingCompany = new CarManufacturingCompany();
		garageHolder = new GarageHolder(carManufacturingCompany);
		
		CarPart[] partsArray = 
				{CarPart.AIRCO_AUTO, 
				CarPart.BODY_BREAK, 
				CarPart.COLOUR_BLACK, 
				CarPart.ENGINE_4, 
				CarPart.GEARBOX_5AUTO, 
				CarPart.SEATS_LEATHER_BLACK, 
				CarPart.WHEELS_COMFORT};
		
		ArrayList<CarPart> parts = Arrays.asList(partsArray);
		
		carSpecification = new CarSpecification(CarModel.MODEL1,parts);
		
		carOrder = new CarOrder(garageHolder, carSpecification)
		
	}

	@Test
	public void test() {
		
		
		
		fail("Not yet implemented");
	}

}
