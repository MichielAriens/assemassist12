package logic.users;

import java.util.ArrayList;

import logic.assemblyline.AssemblyLine;
import logic.car.CarOrder;

public class CarManufacturingCompany {
	
	private ArrayList<GarageHolder> garageHolders;
	private ArrayList<Mechanic> mechanics;
	private AssemblyLine assemblyLine;
	
	public CarManufacturingCompany(){
		this.assemblyLine = new AssemblyLine();
	}

	public void addOrder(CarOrder order) {
		this.assemblyLine.addOrder(order);
	}
	
	
}
