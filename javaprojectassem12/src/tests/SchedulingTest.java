package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import logic.assemblyline.AssemblyLine;
import logic.order.Order;
import logic.order.VehicleModel;
import logic.order.VehicleOrder;
import logic.order.VehicleOrderDetailsMaker;
import logic.order.VehiclePart;
import logic.workstation.WorkstationChainBuilder;
import logic.workstation.WorkstationDirector;
import logic.workstation.WorkstationDirectorA;
import logic.workstation.WorkstationDirectorB;

import org.joda.time.DateTime;
import org.junit.Test;

public class SchedulingTest {
	
	private AssemblyLine line;
	
	private ArrayList<Order> orders = new ArrayList<>();
	
	/**
	 * Build a standard order: duration 50, 50, 0
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
				VehiclePart.SPOILER_NONE,
				VehiclePart.TOOLSTORAGE_NONE,
				VehiclePart.CARGO_NONE,
				VehiclePart.CERTIFICATION_NONE
			};
		
		VehicleOrderDetailsMaker maker = new VehicleOrderDetailsMaker(VehicleModel.CARMODELA);
		for(VehiclePart part : partsArray){
			maker.addPart(part);
		}
		return new VehicleOrder(maker.getDetails());
	}
	
	/**
	 * Build a standard order: duration 70, 70, 0
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
				VehiclePart.SPOILER_NONE,
				VehiclePart.TOOLSTORAGE_NONE,
				VehiclePart.CARGO_NONE,
				VehiclePart.CERTIFICATION_NONE
			};
		
		VehicleOrderDetailsMaker maker = new VehicleOrderDetailsMaker(VehicleModel.CARMODELB);
		for(VehiclePart part : partsArray){
			maker.addPart(part);
		}
		return new VehicleOrder(maker.getDetails());
	}
	
	/**
	 * Build a standard order: duration 60, 60, 0
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
				VehiclePart.SPOILER_LOW,
				VehiclePart.TOOLSTORAGE_NONE,
				VehiclePart.CARGO_NONE,
				VehiclePart.CERTIFICATION_NONE
			};
		
		VehicleOrderDetailsMaker maker = new VehicleOrderDetailsMaker(VehicleModel.CARMODELC);
		for(VehiclePart part : partsArray){
			maker.addPart(part);
		}
		return new VehicleOrder(maker.getDetails());
	}
	
	/**
	 * Build a standard truck order: duration 60, 90, 30
	 * @return	A standard order with a duration of 60 minutes.
	 */
	private VehicleOrder buildStandardOrderX(){
		VehiclePart[] partsArray = {
				VehiclePart.BODY_PLATFORM, 
				VehiclePart.COLOUR_GREEN,
				VehiclePart.ENGINE_TRUCKSTANDARD,
				VehiclePart.GEARBOX_8MANUAL,
				VehiclePart.SEATS_VINYL_GRAY,
				VehiclePart.AIRCO_MANUAL,
				VehiclePart.WHEELS_HEAVY_DUTY,
				VehiclePart.SPOILER_NONE,
				VehiclePart.TOOLSTORAGE_STANDARD,
				VehiclePart.CARGO_STANDARD,
				VehiclePart.CERTIFICATION_STANDARD
			};
		
		VehicleOrderDetailsMaker maker = new VehicleOrderDetailsMaker(VehicleModel.TRUCKMODELX);
		for(VehiclePart part : partsArray){
			maker.addPart(part);
		}
		return new VehicleOrder(maker.getDetails());
	}
	
	/**
	 * Build a standard truck order: duration 60, 120, 45
	 * @return	A standard order with a duration of 60 minutes.
	 */
	private VehicleOrder buildStandardOrderY(){
		VehiclePart[] partsArray = {
				VehiclePart.BODY_PLATFORM, 
				VehiclePart.COLOUR_BLACK,
				VehiclePart.ENGINE_TRUCKSTANDARD,
				VehiclePart.GEARBOX_8MANUAL,
				VehiclePart.SEATS_VINYL_GRAY,
				VehiclePart.AIRCO_MANUAL,
				VehiclePart.WHEELS_HEAVY_DUTY,
				VehiclePart.SPOILER_NONE,
				VehiclePart.TOOLSTORAGE_STANDARD,
				VehiclePart.CARGO_STANDARD,
				VehiclePart.CERTIFICATION_STANDARD
			};
		
		VehicleOrderDetailsMaker maker = new VehicleOrderDetailsMaker(VehicleModel.TRUCKMODELY);
		for(VehiclePart part : partsArray){
			maker.addPart(part);
		}
		return new VehicleOrder(maker.getDetails());
	}
	
	private void buildAssemblyLineA(){
		WorkstationChainBuilder builder = new WorkstationChainBuilder();	
		WorkstationDirector director = new WorkstationDirectorA(builder);
		director.construct();
		
		VehicleModel[] models = {VehicleModel.CARMODELA, VehicleModel.CARMODELB, VehicleModel.CARMODELC};
		this.line = new AssemblyLine(Arrays.asList(models), builder, new DateTime(2014, 1, 1, 6, 0), "Assembly Line 1");
	}
	
	private void buildAssemblyLineB(){
		WorkstationChainBuilder builder = new WorkstationChainBuilder();	
		WorkstationDirector director = new WorkstationDirectorB(builder);
		director.construct();
		
		VehicleModel[] models = {VehicleModel.CARMODELA, VehicleModel.CARMODELB, VehicleModel.CARMODELC, VehicleModel.TRUCKMODELX, VehicleModel.TRUCKMODELY};
		this.line = new AssemblyLine(Arrays.asList(models), builder, new DateTime(2014, 1, 1, 6, 0), "Assembly Line 3");
	}

	@Test
	public void schedulingTest1() {
		buildAssemblyLineA();
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
		ArrayList<String> expected = new ArrayList<>();
		expected.add("01-01-2014 09:10");
		expected.add("01-01-2014 10:20");
		expected.add("01-01-2014 11:30");
		expected.add("01-01-2014 12:30");
		expected.add("01-01-2014 13:40");
		expected.add("01-01-2014 14:50");
		expected.add("01-01-2014 16:00");
		expected.add("01-01-2014 17:00");
		expected.add("01-01-2014 17:50");
		for(int i = 0; i < orders.size(); i++){
			assertEquals(expected.get(i), orders.get(i).toString());
		}
	}
	
	@Test
	public void schedulingTest2() {
		buildAssemblyLineB();
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
		ArrayList<String> expected = new ArrayList<>();
		expected.add("01-01-2014 11:30");
		expected.add("01-01-2014 12:40");
		expected.add("01-01-2014 13:50");
		expected.add("01-01-2014 14:50");
		expected.add("01-01-2014 16:00");
		expected.add("01-01-2014 17:10");
		expected.add("01-01-2014 18:10");
		expected.add("01-01-2014 19:00");
		expected.add("01-01-2014 19:00");
		for(int i = 0; i < orders.size(); i++){
			assertEquals(expected.get(i), orders.get(i).toString());
		}
	}
	
	@Test
	public void schedulingTest3() {
		buildAssemblyLineB();
		orders.add(buildStandardOrderA());
		orders.add(buildStandardOrderB());
		orders.add(buildStandardOrderX());
		orders.add(buildStandardOrderC());
		orders.add(buildStandardOrderA());
		orders.add(buildStandardOrderY());
		for(int i = 0; i < orders.size(); i++){
			line.addOrder(orders.get(i));
		}
		ArrayList<String> expected = new ArrayList<>();
		expected.add("01-01-2014 11:50");
		expected.add("01-01-2014 13:50");
		expected.add("01-01-2014 14:50");
		expected.add("01-01-2014 15:50");
		expected.add("01-01-2014 16:50");
		expected.add("01-01-2014 17:35");
		for(int i = 0; i < orders.size(); i++){
			assertEquals(expected.get(i), orders.get(i).toString());
		}
	}
	
	@Test
	public void schedulingTest4() {
		buildAssemblyLineB();
		orders.add(buildStandardOrderA());
		for(int i = 0; i < orders.size(); i++){
			line.addOrder(orders.get(i));
		}
		ArrayList<String> expected = new ArrayList<>();
		expected.add("01-01-2014 08:30");
		for(int i = 0; i < orders.size(); i++){
			assertEquals(expected.get(i), orders.get(i).toString());
		}
	}
	
	@Test
	public void schedulingTest5() {
		buildAssemblyLineB();
		orders.add(buildStandardOrderB());
		for(int i = 0; i < orders.size(); i++){
			line.addOrder(orders.get(i));
		}
		ArrayList<String> expected = new ArrayList<>();
		expected.add("01-01-2014 09:30");
		for(int i = 0; i < orders.size(); i++){
			assertEquals(expected.get(i), orders.get(i).toString());
		}
	}
	
	@Test
	public void schedulingTest6() {
		buildAssemblyLineB();
		orders.add(buildStandardOrderC());
		for(int i = 0; i < orders.size(); i++){
			line.addOrder(orders.get(i));
		}
		ArrayList<String> expected = new ArrayList<>();
		expected.add("01-01-2014 09:00");
		for(int i = 0; i < orders.size(); i++){
			assertEquals(expected.get(i), orders.get(i).toString());
		}
	}
	
	@Test
	public void schedulingTest7() {
		buildAssemblyLineB();
		orders.add(buildStandardOrderX());
		for(int i = 0; i < orders.size(); i++){
			line.addOrder(orders.get(i));
		}
		ArrayList<String> expected = new ArrayList<>();
		expected.add("01-01-2014 11:00");
		for(int i = 0; i < orders.size(); i++){
			assertEquals(expected.get(i), orders.get(i).toString());
		}
	}
	
	@Test
	public void schedulingTest8() {
		buildAssemblyLineB();
		orders.add(buildStandardOrderY());
		for(int i = 0; i < orders.size(); i++){
			line.addOrder(orders.get(i));
		}
		ArrayList<String> expected = new ArrayList<>();
		expected.add("01-01-2014 11:45");
		for(int i = 0; i < orders.size(); i++){
			assertEquals(expected.get(i), orders.get(i).toString());
		}
	}

}
