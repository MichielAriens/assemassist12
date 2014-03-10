package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import logic.assemblyline.AssemblyLine;
import logic.car.CarModel;
import logic.car.CarOrder;
import logic.car.CarPart;
import logic.car.CarPartType;
import logic.car.CarSpecification;
import logic.users.CarManufacturingCompany;
import logic.users.Mechanic;
import logic.workstation.Task;

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
	
	@Test
	public void scheduleOneCarOrderWithEmptyAssemblyLine(){
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
		
	}
	
	/**
	@Test
	public void scheduleTenCarOrderWithEmptyAssemblyLine(){
		for(CarOrder order : orders){
			assemblyLine.addCarOrder(order);
			assertFalse(order.done());
		}
		for(int i = 0; i < 10; i++){
			assemblyLine.moveAssemblyLine(60);
		}
		for(int i = 0; i < )
		
		
		
		
	}
	*/
	
	
	
	
}
