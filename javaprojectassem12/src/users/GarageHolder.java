package users;

import assemblyline.OrderRegistrator;
import car.CarOrder;
import car.CarSpecification;

public class GarageHolder {
	private OrderRegistrator orderRegistrator;
	public GarageHolder(){
		orderRegistrator = OrderRegistrator.getInstance();
	}
	public void alertCompleted(CarOrder carOrder) {
		
		
	}
	public void placeOrder(CarSpecification specification){
		CarOrder order = new CarOrder(this,specification);
		
	}

}
