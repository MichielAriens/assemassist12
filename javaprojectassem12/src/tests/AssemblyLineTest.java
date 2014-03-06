package tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import logic.assemblyline.AssemblyLine;
import logic.assemblyline.AssemblyLine.Schedule;
import logic.car.CarModel;
import logic.car.CarOrder;
import logic.car.CarPart;
import logic.car.CarSpecification;

import org.junit.Before;
import org.junit.Test;

public class AssemblyLineTest {

	private AssemblyLine assemblyLine;
	private CarSpecification carSpecification;
	private CarOrder carOrder1;
	
	@Before
	public void prequel(){
		
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
		carSpecification = new CarSpecification(CarModel.MODEL1,new ArrayList<CarPart>(parts));
		Calendar carOrder1StartTime = Calendar.getInstance();
		carOrder1 = new CarOrder(carSpecification, carOrder1StartTime);
	}
	
	@Test
	public void scheduleCarOrderWithEmptyAssemblyLine(){
	}
}
