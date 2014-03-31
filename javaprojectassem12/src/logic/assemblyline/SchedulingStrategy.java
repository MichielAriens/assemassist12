package logic.assemblyline;

import java.util.LinkedList;

import logic.car.Order;

public abstract class SchedulingStrategy {

	protected abstract void addOrder(Order order, LinkedList<Order> queue);

	protected abstract void refactorQueue(LinkedList<Order> queue, LinkedList<Order> copy);
	
}
