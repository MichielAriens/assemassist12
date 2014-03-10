package tests;

import static org.junit.Assert.*;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
	Mechanic mechanic;
	
	Workstation carBodyPost;
	Workstation driveTrainPost;
	Workstation accessoriesPost;
	
	
	@Before
	public void prequel(){		
		carManufacturingCompany = new CarManufacturingCompany();
		mechanic = new Mechanic(carManufacturingCompany, "Barry");
		garageHolder = new GarageHolder(carManufacturingCompany, "Lando");//new GarageHolder(carManufacturingCompany);
		
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
		carOrder = new CarOrder(carSpecification);
		
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

	/**
	 * We define a universal post that can do any task. This tests the core logic involved.
	 */
	@Test
	public void testUniversalPost() {
		universalPost.setOrder(carOrder);
		assertFalse(carOrder.done());		
		universalPost.doTask(carOrder.getTasks().get(0));
		assertFalse(carOrder.done());
		assertFalse(universalPost.done());
		
		for(Task task : universalPost.getRequiredTasks()){
			universalPost.doTask(task);
		}
		assertTrue(carOrder.done());
		assertTrue(universalPost.done());
	}
	
	/**
	 * Tests to simulate the workflow on the assembly line (without Assemblyline) With repetitions. (edge conditions)
	 */
	@Test
	public void testSpecificPosts(){
		carBodyPost.setOrder(carOrder);
		driveTrainPost.setOrder(carOrder);
		accessoriesPost.setOrder(carOrder);
		assertFalse(carBodyPost.done());
		assertFalse(carOrder.done());
		for(Task task : carOrder.getTasks()){
			carBodyPost.doTask(task);
		}
		assertTrue(carBodyPost.done());
		assertFalse(carOrder.done());
		assertFalse(driveTrainPost.done());
		assertFalse(accessoriesPost.done());
		for(Task task : carOrder.getTasks()){
			carBodyPost.doTask(task);
		}
		assertTrue(carBodyPost.done());
		assertFalse(carOrder.done());
		assertFalse(driveTrainPost.done());
		assertFalse(accessoriesPost.done());
		for(Task task : carOrder.getTasks()){
			driveTrainPost.doTask(task);
		}
		assertTrue(carBodyPost.done());
		assertTrue(driveTrainPost.done());
		assertFalse(accessoriesPost.done());
		assertFalse(carOrder.done());
		for(Task task : carOrder.getTasks()){
			accessoriesPost.doTask(task);
		}
		assertTrue(carBodyPost.done());
		assertTrue(driveTrainPost.done());
		assertTrue(accessoriesPost.done());
		assertTrue(carOrder.done());
	}
	

}
