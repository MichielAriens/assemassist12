package tests;

import static org.junit.Assert.*;

import java.sql.Time;
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
import logic.users.Mechanic;
import logic.workstation.AccessoriesPost;
import logic.workstation.CarBodyPost;
import logic.workstation.DriveTrainPost;
import logic.workstation.Task;
import logic.workstation.Workstation;

import org.junit.Before;
import org.junit.Test;

public class WorkStationTests {
	GarageHolder garageHolder;
	CarManufacturingCompany carManufacturingCompany;
	CarSpecification carSpecification;
	CarOrder carOrder;
	
	Workstation universalPost;
	
	Workstation carBodyPost;
	Workstation driveTrainPost;
	Workstation accessoriesPost;
	
	
	@Before
	public void prequel(){		
		carManufacturingCompany = new CarManufacturingCompany();
		garageHolder = new GarageHolder(carManufacturingCompany);
		
		CarPart[] partsArray = {
				CarPart.AIRCO_AUTO, 
				CarPart.BODY_BREAK, 
				CarPart.COLOUR_BLACK, 
				CarPart.ENGINE_4, 
				CarPart.GEARBOX_5AUTO, 
				CarPart.SEATS_LEATHER_BLACK, 
				CarPart.WHEELS_COMFORT
			};
		
		List<CarPart> parts = (List<CarPart>) Arrays.asList(partsArray);
		carSpecification = new CarSpecification(CarModel.MODEL1,new ArrayList<CarPart>(parts));
		carOrder = new CarOrder(garageHolder, carSpecification);
		
		universalPost = new Workstation() {
			@Override
			public List<CarPartType> getCapabilities() {
				return Arrays.asList(CarPartType.values());
			}
		};
		
		carBodyPost = new CarBodyPost();
		driveTrainPost = new DriveTrainPost();
		accessoriesPost = new AccessoriesPost();
		
	}

	@Test
	public void test() {
		universalPost.setOrder(carOrder);
		assertFalse(carOrder.done());		
		universalPost.doTask(carOrder.getTasks().get(0), new Time(0), new Mechanic());
		assertFalse(carOrder.done());
		assertFalse(universalPost.done());
		
		for(Task task : universalPost.getRequiredTasks()){
			universalPost.doTask(task, new Time(0), new Mechanic());
		}
		assertTrue(carOrder.done());
		assertTrue(universalPost.done());
	}

}
