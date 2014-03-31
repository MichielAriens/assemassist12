package logic.assemblyline;

import java.util.LinkedList;

import logic.car.Order;

public class BatchSpecificationStrategy extends SchedulingStrategy{

	private Order example;
	
	protected BatchSpecificationStrategy(Order order){
		example = order;
	}

	@Override
	protected void addOrder(Order order, LinkedList<Order> queue) {
		if(!order.equals(example) || queue.isEmpty()){
			queue.add(order); 
			return;
		}else{
			int index=0;
			for(Order next : queue){
				if(!next.equals(example)){
					queue.add(index, order);
					return;
				}
				index++;
			}
		}
	}

	@Override
	protected void refactorQueue(LinkedList<Order> queue, LinkedList<Order> copy) {
		if(copy.isEmpty())
			return;
		
		LinkedList<Order> otherQueue = new LinkedList<Order>();
		for(Order next : copy){
			if(next.equals(example))
				queue.add(next);
			else
				otherQueue.add(next);
		}
		
		for(Order next : otherQueue){
			queue.add(next);
		}
		
	}

}
