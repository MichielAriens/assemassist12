package logic.assemblyline;

import java.util.LinkedList;

import logic.car.Order;

public class FifoStrategy extends SchedulingStrategy{

	@Override
	protected void addOrder(Order order, LinkedList<Order> queue) {
		queue.add(order);
		
	}

}
