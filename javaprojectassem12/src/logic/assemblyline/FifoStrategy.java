package logic.assemblyline;

import java.util.LinkedList;

import logic.car.Order;

public class FifoStrategy extends SchedulingStrategy{

	@Override
	protected void addOrder(Order order, LinkedList<Order> queue) {
		queue.add(order);
		
	}

	@Override
	protected void refactorQueue(LinkedList<Order> queue, LinkedList<Order> copy) {
		for(Order next:copy){
			queue.add(next);
		}
	}

}
