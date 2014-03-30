package logic.assemblyline;

import logic.car.Order;

public abstract class SchedulingStrategy {

	protected abstract void addOrder(Order order);
	
}
