package logic.users;

import logic.assemblyline.OrderRegistrator;
import logic.car.CarOrder;
import logic.car.CarSpecification;

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
