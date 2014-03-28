package logic.car;

public class CarOrderFactory extends OrderFactory{

	@Override
	public Order createOrder(OrderDetails d) {
		if(!(d instanceof CarOrderDetails))
			return null;
		CarOrderDetails details = (CarOrderDetails) d;
		return new CarOrder(details);
	}

}
