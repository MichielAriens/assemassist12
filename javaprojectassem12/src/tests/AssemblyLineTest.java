package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import logic.assemblyline.AssemblyLine;
import logic.car.CarModel;
import logic.car.CarOrder;
import logic.car.CarPart;
import logic.car.CarSpecification;
import logic.users.CarManufacturingCompany;
import logic.users.Mechanic;
import logic.workstation.Task;
import logic.workstation.Workstation;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

public class AssemblyLineTest {
	private CarManufacturingCompany cmcMotors;
	private AssemblyLine assemblyLine;
	private CarSpecification carSpecification;
	private List<CarOrder> orders = new ArrayList<CarOrder>();
	private Mechanic barry;
	
	@Before
	public void prequel(){
		cmcMotors = new CarManufacturingCompany();
		barry = new Mechanic(cmcMotors, "Barry");
		
		assemblyLine = new AssemblyLine();
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
		carSpecification = new CarSpecification(CarModel.MODEL1,parts);
		for(int i = 0; i < 10; i++){
			orders.add(new CarOrder(carSpecification));
		}
	}
	
	
	/**
	 * This test simulates a single CarOrder object propagating through the assemblyline.
	 * This is a really in depth test, constantly checking the states of al involved actors.
	 */
	@Test
	public void singleCarOrderPropagation(){
		DateTime startTime = assemblyLine.getCurrentTime();
		assertFalse(orders.get(0).done());
		assemblyLine.addCarOrder(orders.get(0));
		assemblyLine.moveAssemblyLine(0);
		assertFalse(orders.get(0).done());
		assertFalse(assemblyLine.moveAssemblyLine(60));
		assertTrue(assemblyLine.getWorkStations()[0].getCurrentOrder().equals(orders.get(0)));
		assertNull(assemblyLine.getWorkStations()[1].getCurrentOrder());
		assertNull(assemblyLine.getWorkStations()[2].getCurrentOrder());
		
		/* Now we progress the line */
		barry.setActiveWorkstation(assemblyLine.getWorkStations()[0]);
		assertFalse(assemblyLine.getWorkStations()[0].done());
		for(Task task : assemblyLine.getWorkStations()[0].getRequiredTasks()){
			barry.doTask(task);
		}
		assertTrue(assemblyLine.getWorkStations()[0].done());
		
		assertTrue(assemblyLine.moveAssemblyLine(60));
		assertNull(assemblyLine.getWorkStations()[0].getCurrentOrder());
		
		/* Now we progress the line */
		barry.setActiveWorkstation(assemblyLine.getWorkStations()[1]);
		assertFalse(assemblyLine.getWorkStations()[1].done());
		assertFalse(orders.get(0).done());
		for(Task task : assemblyLine.getWorkStations()[1].getRequiredTasks()){
			barry.doTask(task);
		}
		assertTrue(assemblyLine.getWorkStations()[1].done());
		assertTrue(assemblyLine.moveAssemblyLine(60));
		assertFalse(assemblyLine.moveAssemblyLine(60));
		assertNull(assemblyLine.getWorkStations()[0].getCurrentOrder());
		assertNull(assemblyLine.getWorkStations()[1].getCurrentOrder());
		
		barry.setActiveWorkstation(assemblyLine.getWorkStations()[2]);
		assertFalse(assemblyLine.getWorkStations()[2].done());
		assertFalse(orders.get(0).done());
		for(Task task : assemblyLine.getWorkStations()[2].getRequiredTasks()){
			barry.doTask(task);
		}
		assertTrue(assemblyLine.getWorkStations()[2].done());
		assertTrue(orders.get(0).done());
		assertTrue(assemblyLine.moveAssemblyLine(60));
		assertTrue(orders.get(0).done());
		
		assertTrue(assemblyLine.getCurrentTime().equals(startTime.plusMinutes(180)));
	}
	
	/**
	 * This tests the scheduling of 10 Orders in one infinite shift.
	 */
	@Test
	public void tenOrdersTest(){
		for(CarOrder order : orders){
			assemblyLine.addCarOrder(order);
			assertFalse(order.done());
		}
		for(int i = 0; i < 10; i++){
			for(Workstation station : assemblyLine.getWorkStations()){
				barry.setActiveWorkstation(station);
				for(Task task : station.getRequiredTasks()){
					barry.doTask(task);
				}
			}
			assemblyLine.moveAssemblyLine(60);
		}
		for(int i = 0; i < 10; i++){
			if(i < 8){
				assertTrue(orders.get(i).done());
			}else{
				assertFalse(orders.get(i).done());
			}
		}
	}
	
	
	
	
}
