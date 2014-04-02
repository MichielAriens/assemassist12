package logic.assemblyline;

import java.util.LinkedList;

import logic.car.Order;

public class FifoStrategy extends SchedulingStrategy{

	@Override
	protected void addOrder(Order order, LinkedList<Order> queue) {
		int index = 0;
		for(Order next:queue){
			if(checkDeadline(order, next))
				break;
			
			index++;
		}
		queue.add(index,order);
		
	}

	@Override
	protected void refactorQueue(LinkedList<Order> queue, LinkedList<Order> copy) {
		for(Order next:copy){
			queue.add(next);
		}
	}
	
	public String toString(){
		return "FIFO";
	}

	@Override
	protected SchedulingStrategy getRawCopy() {
		try {
			return this.getClass().newInstance();
		} catch (InstantiationException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		}
	}

}
