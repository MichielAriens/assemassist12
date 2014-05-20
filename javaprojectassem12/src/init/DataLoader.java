package init;

import org.joda.time.DateTime;

import logic.car.CarOrderDetails;
import logic.car.CarOrderDetailsMaker;
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
			holder.placeOrder(details);
		}
	}
	
	/**
	 * Build a standard order of model A.
	 * @return	A standard order of model A.
	 */
	private CarOrderDetails buildStandardOrderA(){
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
		
		CarOrderDetailsMaker maker = new CarOrderDetailsMaker(VehicleModel.CARMODELA);
		for(VehiclePart part : partsArray){
			maker.addPart(part);
		}
		return maker.getDetails();
	}
	
	/**
	 * Build a standard order of model B. 
	 * @return	A standard order of model B.
	 */
	private CarOrderDetails buildStandardOrderB(){
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
		
		CarOrderDetailsMaker maker = new CarOrderDetailsMaker(VehicleModel.CARMODELB);
		for(VehiclePart part : partsArray){
			maker.addPart(part);
		}
		return maker.getDetails();
	}
	
	/**
	 * Build a standard order of model C.
	 * @return	A standard order of model C.
	 */
	private CarOrderDetails buildStandardOrderC(){
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
		
		CarOrderDetailsMaker maker = new CarOrderDetailsMaker(VehicleModel.CARMODELC);
		for(VehiclePart part : partsArray){
			maker.addPart(part);
		}
		return maker.getDetails();
	}
	
	/**
	 * Build a standard truck order of model X.
	 * @return	A standard order of model X.
	 */
	private CarOrderDetails buildStandardOrderX(){
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
		
		CarOrderDetailsMaker maker = new CarOrderDetailsMaker(VehicleModel.TRUCKMODELX);
		for(VehiclePart part : partsArray){
			maker.addPart(part);
		}
		return maker.getDetails();
	}
	
	/**
	 * Build a standard truck order of model Y.
	 * @return	A standard order of model Y.
	 */
	private CarOrderDetails buildStandardOrderY(){
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
		
		CarOrderDetailsMaker maker = new CarOrderDetailsMaker(VehicleModel.TRUCKMODELY);
		for(VehiclePart part : partsArray){
			maker.addPart(part);
		}
		return maker.getDetails();
	}
	
	/**
	 * Build standard custom task order.
	 * @return A standard custom task order.
	 * @return
	 */
	private TaskOrderDetails buildStandardCustomOrder(){
		TaskOrderDetailsMaker maker = new TaskOrderDetailsMaker();
		maker.choosePart(VehiclePart.COLOUR_RED);
		maker.chooseDeadline(new DateTime(2014, 1, 2, 15, 0));
		return maker.getDetails();
	}
	
	
	
}
