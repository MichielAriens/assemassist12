package logic.assemblyline;

import java.util.LinkedList;

import logic.car.Order;

public abstract class SchedulingStrategy{
	
	protected Order example;

	protected abstract void addOrder(Order order, LinkedList<Order> queue);

	protected abstract void refactorQueue(LinkedList<Order> queue, LinkedList<Order> copy);
	
	protected boolean checkDeadline(Order order, Order next) {
		if(order.getDeadLine()==null)
			return false;
		if(next.getDeadLine()==null)
			return true;
		return order.getDeadLine().isBefore(next.getDeadLine());
	}
	
	protected void setExample(Order order){
		if(order!=null)
			this.example = order;
	}
	
	protected abstract SchedulingStrategy getRawCopy();
}
