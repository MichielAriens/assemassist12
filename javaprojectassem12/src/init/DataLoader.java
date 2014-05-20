package init;

import org.joda.time.DateTime;

import logic.car.VehicleOrderDetails;
import logic.car.VehicleOrderDetailsMaker;
import logic.car.TaskOrder;
import logic.car.TaskOrderDetails;
import logic.car.TaskOrderDetailsMaker;
import logic.car.VehicleModel;
import logic.car.VehicleOrder;
import logic.car.VehiclePart;
import logic.users.CarManufacturingCompany;
import logic.users.GarageHolder;

public class DataLoader {
	
	private CarManufacturingCompany company;
	
	public DataLoader(CarManufacturingCompany comp){
		this.company = comp;
	}
	
	public void loadData(){
		
	}
	
	private void placeOrders(){
		GarageHolder holder = (GarageHolder) this.company.logIn("gar");
		for(int i = 0; i < 10; i++){
			holder.placeOrder(buildStandardOrderA());
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
	 * Build a standard VehicleOrderDetails of model Y.
	 * @return	A standard VehicleOrderDetails of model Y.
	 */
	private VehicleOrderDetails buildStandardOrderY(){
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
		return maker.getDetails();
	}
	
	/**
	 * Build standard TaskOrderDetails.
	 * @return A standard TaskOrderDetails.
	 * @return
	 */
	private TaskOrderDetails buildStandardCustomOrder(){
		TaskOrderDetailsMaker maker = new TaskOrderDetailsMaker();
		maker.choosePart(VehiclePart.COLOUR_RED);
		maker.chooseDeadline(new DateTime(2014, 1, 2, 15, 0));
		return maker.getDetails();
	}
	
	
	
}
