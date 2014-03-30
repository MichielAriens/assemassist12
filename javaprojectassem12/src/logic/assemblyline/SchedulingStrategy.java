package logic.assemblyline;

import java.util.LinkedList;

import logic.car.Order;

public abstract class SchedulingStrategy {

	protected abstract void addOrder(Order order, LinkedList<Order> queue);
	
}
