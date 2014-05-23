package init;

import interfaces.Printable;

import org.joda.time.DateTime;

import logic.assemblyline.AssemblyLine;
import logic.order.TaskOrderDetails;
import logic.order.TaskOrderDetailsMaker;
import logic.order.VehicleModel;
import logic.order.VehicleOrderDetails;
import logic.order.VehicleOrderDetailsMaker;
import logic.order.VehiclePart;
import logic.users.CarManufacturingCompany;
import logic.users.CustomsManager;
import logic.users.GarageHolder;
import logic.users.Mechanic;
import logic.workstation.Task;
import logic.workstation.Workstation;

/**
 * Class responsible for the data that needs to be loaded initially. 
 */
public class DataLoader {
	
	/**
	 * The car manufacturing company for which the data needs to be loaded.
	 */
	private CarManufacturingCompany company;
	
	/**
	 * Initializes the data loader with the given car manufacturing company.
	 * @param comp	The car manufacturing company for which the data needs to be loaded.
	 */
	public DataLoader(CarManufacturingCompany comp){
		this.company = comp;
	}
	
	/**
	 * Loads the initial data.
	 */
	public void loadData(){
		advanceDay();
		fillAssemblyLinesAndQueue();
	}
	
	/**
	 * Places the orders that need to be placed after the first day has passed.
	 */
	private void fillAssemblyLinesAndQueue(){
		GarageHolder holder = (GarageHolder) this.company.logIn("gar");
		for(int i = 0; i < 3; i++){
			holder.placeOrder(buildStandardOrderA());
		}
		CustomsManager cust = (CustomsManager) this.company.logIn("cust");
		for(int i = 0; i < 3; i++){
			cust.placeOrder(buildStandardCustomOrder());
		}
		holder.placeOrder(buildStandardOrderB());
		holder.placeOrder(buildStandardOrderC());
		holder.placeOrder(buildStandardOrderX());
		for(int i = 0; i < 3; i++){
			holder.placeOrder(buildStandardOrderA());
		}
	}
	
	/**
	 * Places standard orders of type A until the day moves
	 */
	private void advanceDay(){
		GarageHolder holder = (GarageHolder) this.company.logIn("gar");
		for(int i = 0; i < 50; i++){
			holder.placeOrder(buildStandardOrderA());
		}
		performAllTasks();
	}
	
	/**
	 * Performs all tasks until there are no unfinished tasks left.
	 */
	private void performAllTasks(){
		Mechanic mech = (Mechanic) this.company.logIn("mech");
		boolean taskPerformed = true;
		while(taskPerformed){
			taskPerformed = false;
			for(Printable<AssemblyLine> line : mech.getAssemblyLines()){
				mech.setActiveAssemblyLine(line);
				for(Printable<Workstation> station : mech.getWorkstationsFromAssemblyLine()){
					mech.setActiveWorkstation(station);
					for(Printable<Task> task : mech.getAvailableTasks()){
						int duration = ((Task) task).getEstimatedPhaseDuration();
						if(mech.doTask(task, duration))
							taskPerformed = true;
					}
				}
			}
		}
	}
	
	/**
	 * Build a standard VehicleOrderDetails of model A.
	 * @return	A standard VehicleOrderDetails of model A.
	 */
	private VehicleOrderDetails buildStandardOrderA(){
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
		return maker.getDetails();
	}
	
	/**
	 * Build a standard VehicleOrderDetails of model B. 
	 * @return	A standard VehicleOrderDetails of model B.
	 */
	private VehicleOrderDetails buildStandardOrderB(){
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
		return maker.getDetails();
	}
	
	/**
	 * Build a standard VehicleOrderDetails of model C.
	 * @return	A standard VehicleOrderDetails of model C.
	 */
	private VehicleOrderDetails buildStandardOrderC(){
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
		return maker.getDetails();
	}
	
	/**
	 * Build a standard VehicleOrderDetails of model X.
	 * @return	A standard VehicleOrderDetails of model X.
	 */
	private VehicleOrderDetails buildStandardOrderX(){
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
		return maker.getDetails();
	}
	
	/**
	 * Build standard TaskOrderDetails.
	 * @return A standard TaskOrderDetails.
	 */
	private TaskOrderDetails buildStandardCustomOrder(){
		TaskOrderDetailsMaker maker = new TaskOrderDetailsMaker();
		maker.choosePart(VehiclePart.COLOUR_RED);
		maker.chooseDeadline(new DateTime(2014, 1, 2, 15, 0));
		return maker.getDetails();
	}
}
