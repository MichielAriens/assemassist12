package logic.car;

public class CarOrderFactory extends OrderFactory{

	@Override
	public Order createOrder(OrderDetails details) {
		if(!(details instanceof CarOrderDetails))
			return null;
		CarOrderDetails 
		return null;
	}

}
