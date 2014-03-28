package logic.car;

public class TaskOrderFactory extends OrderFactory{

	@Override
	public Order createOrder(OrderDetails d) {
		if(!(d instanceof TaskOrderDetails))
			return null;
		TaskOrderDetails details = (TaskOrderDetails) d;
		return new TaskOrder(details);
	}

}
