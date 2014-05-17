package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import logic.assemblyline.AssemblyLine;
import logic.car.CarOrderDetailsMaker;
import logic.car.Order;
import logic.car.VehicleModel;
import logic.car.VehicleOrder;
import logic.car.VehiclePart;
import logic.workstation.WorkstationChainBuilder;
import logic.workstation.WorkstationDirector;
import logic.workstation.WorkstationDirectorA;

import org.junit.Before;
import org.junit.Test;

public class SchedulingTest {
	
	private AssemblyLine line;
	
	private ArrayList<Order> orders = new ArrayList<>();
	
	/**
	 * Build a standard order: duration 50
	 * @return	A standard order with a duration of 50 minutes.
	 */
	private VehicleOrder buildStandardOrderA(){
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
		for(VehiclePart part : partsArray){
			maker.addPart(part);
		}
		return new VehicleOrder(maker.getDetails());
	}
	
	/**
	 * Build a standard order: duration 70
	 * @return	A standard order with a duration of 70 minutes.
	 */
	private VehicleOrder buildStandardOrderB(){
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
		
		CarOrderDetailsMaker maker = new CarOrderDetailsMaker(VehicleModel.CARMODELB);
		for(VehiclePart part : partsArray){
			maker.addPart(part);
		}
		return new VehicleOrder(maker.getDetails());
	}
	
	/**
	 * Build a standard order: duration 60
	 * @return	A standard order with a duration of 60 minutes.
	 */
	private VehicleOrder buildStandardOrderC(){
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
		for(VehiclePart part : partsArray){
			maker.addPart(part);
		}
		return new VehicleOrder(maker.getDetails());
	}
	
	private void printOrders(){
		for(Order o : orders){
			System.out.println(o.getExtraInformation());
		}
	}
	
	@Before
	public void buildAssemblyLine(){
		WorkstationChainBuilder builder = new WorkstationChainBuilder();	
		WorkstationDirector director = new WorkstationDirectorA(builder);
		director.construct();
		
		VehicleModel[] models = {VehicleModel.CARMODELA, VehicleModel.CARMODELB, VehicleModel.CARMODELC};
		this.line = new AssemblyLine(Arrays.asList(models), builder);
	}

	@Test
	public void schedulingTest1() {
		orders.add(buildStandardOrderA());
		orders.add(buildStandardOrderB());
		orders.add(buildStandardOrderB());
		orders.add(buildStandardOrderC());
		orders.add(buildStandardOrderA());
		orders.add(buildStandardOrderC());
		orders.add(buildStandardOrderB());
		orders.add(buildStandardOrderC());
		orders.add(buildStandardOrderA());
			
		for(int i = 0; i < orders.size(); i++){
			line.addOrder(orders.get(i));
		}
		printOrders();
	}

}
