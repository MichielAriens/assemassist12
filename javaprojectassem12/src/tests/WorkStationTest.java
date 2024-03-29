package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import logic.order.TaskOrder;
import logic.order.TaskOrderDetailsMaker;
import logic.order.VehicleModel;
import logic.order.VehicleOrder;
import logic.order.VehicleOrderDetailsMaker;
import logic.order.VehiclePart;
import logic.order.VehiclePartType;
import logic.users.CarManufacturingCompany;
import logic.users.GarageHolder;
import logic.users.Mechanic;
import logic.workstation.AccessoriesPost;
import logic.workstation.CarBodyPost;
import logic.workstation.DriveTrainPost;
import logic.workstation.Task;
import logic.workstation.Workstation;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class WorkStationTest {
	GarageHolder garageHolder;
	CarManufacturingCompany carManufacturingCompany;
	VehicleOrder carOrder;
	
	Workstation universalPost;
	Mechanic mechanic;
	
	Workstation carBodyPost;
	Workstation driveTrainPost;
	Workstation accessoriesPost;
	
	/**
	 * We start by setting up an environment with assets commonly used by the test suite in the prequel.
	 * This contains a carBodyPost, a driveTrainPost, an acceddoriesPost and a universalPost. The latter is capable of 
	 * performing any task. The prequel also provides a mechanic to do tasks and a car order on which to perform tasks.
	 * It also offers all objects required to instantiate the objects discussed above although they may not play an active role in the tests.
	 * Examples are the CarManufacturingCompany and the GarageHolder
	 */
	@Before
	public void prequel(){		
		carManufacturingCompany = new CarManufacturingCompany();
		mechanic = new Mechanic(carManufacturingCompany, "Barry");
		garageHolder = new GarageHolder(carManufacturingCompany, "Lando");//new GarageHolder(carManufacturingCompany);
		
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
		
		VehicleOrderDetailsMaker maker = new VehicleOrderDetailsMaker(VehicleModel.CARMODELA);
		for(VehiclePart part : partsArray){
			maker.addPart(part);
		}
		carOrder = new VehicleOrder(maker.getDetails());
		
		universalPost = new Workstation() {
			@Override
			public List<VehiclePartType> getCapabilities() {
				return Arrays.asList(VehiclePartType.values());
			}

			@Override
			public String getStringRepresentation() {
				// TODO Auto-generated method stub
				return null;
			}
		};
		
		carBodyPost = new CarBodyPost();
		driveTrainPost = new DriveTrainPost();
		accessoriesPost = new AccessoriesPost();
		
	}

	/**
	 * Tests the correct functioning of one Workstations in tandem with the CarOrders they're working on. 
	 * 
	 * The workstation tested is a universal workstation: It can do any task.
	 * At the start we have a new CarOrder placed on the workstation. We assert that neither the workstation nor the order are done (.done()). 
	 * We iterate through the tasks required completing them.
	 * We assert that both the workstation and the CarOrder now have the status of done.
	 */
	@Test
	public void testUniversalPost() {
		universalPost.setOrder(carOrder);
		assertFalse(carOrder.done());		
		universalPost.doTask(carOrder.getTasks().get(0),0);
		assertFalse(carOrder.done());
		assertFalse(universalPost.done());
		
		for(Task task : carOrder.getTasks()){
			universalPost.doTask(task,0);
		}
		assertTrue(carOrder.done());
		assertTrue(universalPost.done());
	}
	
	/**
	 * Tests to simulate the workflow on the assembly line (without Assemblyline) With repetitions. (edge conditions)
	 * 
	 * We start by placing the order defined in the prequel on the carBody post and to the tasks on this post all the while asserting
	 * the correct states (.done()) of both the car order and the workstation. We continue to progress the order through the driveTrain post
	 * and the accessoriesPost
	 */
	@Test
	public void testSpecificPosts(){
		carBodyPost.setOrder(carOrder);
		driveTrainPost.setOrder(carOrder);
		accessoriesPost.setOrder(carOrder);
		assertFalse(carBodyPost.done());
		assertFalse(carOrder.done());
		for(Task task : carOrder.getTasks()){
			carBodyPost.doTask(task,0);
		}
		assertTrue(carBodyPost.done());
		assertFalse(carOrder.done());
		assertFalse(driveTrainPost.done());
		assertFalse(accessoriesPost.done());
		for(Task task : carOrder.getTasks()){
			carBodyPost.doTask(task,0);
		}
		assertTrue(carBodyPost.done());
		assertFalse(carOrder.done());
		assertFalse(driveTrainPost.done());
		assertFalse(accessoriesPost.done());
		for(Task task : carOrder.getTasks()){
			driveTrainPost.doTask(task,0);
		}
		assertTrue(carBodyPost.done());
		assertTrue(driveTrainPost.done());
		assertFalse(accessoriesPost.done());
		assertFalse(carOrder.done());
		for(Task task : carOrder.getTasks()){
			accessoriesPost.doTask(task,0);
		}
		assertTrue(carBodyPost.done());
		assertTrue(driveTrainPost.done());
		assertTrue(accessoriesPost.done());
		assertTrue(carOrder.done());
	}
	
	/**
	 * Test that taskorders are completed correctly by the workstations.
	 */
	@Test
	public void testTaskOrder(){
		TaskOrderDetailsMaker maker = new TaskOrderDetailsMaker();
		maker.choosePart(VehiclePart.COLOUR_BLACK);
		maker.chooseDeadline(new DateTime().plusHours(5));
		TaskOrder order = new TaskOrder(maker.getDetails());
		
		carBodyPost.setOrder(order);
		for(Task task : order.getTasks()){
			carBodyPost.doTask(task,0);
		}
		
		assertTrue(carBodyPost.done());
		assertTrue(order.done());
	}
	
	/**
	 * Test idle(). A workstation is idle if it isn't currently working on a car order. We can add a car order by using .setOrder(...)
	 * When calling .setOrder(null) we need to assert that the workstation is idle and also that the list of required tasks is empty.
	 * No link may remain to the order previously on the workstation or any of it's elements. 
	 */
	@Test
	public void testIdleWorkstation(){
		assertTrue(universalPost.idle());
		universalPost.setOrder(carOrder);
		assertFalse(universalPost.idle());
		
		universalPost.setOrder(null);
		assertTrue(universalPost.idle());
	}

}
