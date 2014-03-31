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
					break;
				}else{
					if(checkDeadline(order, next))
						break;
				}
				index++;
			}
			queue.add(index, order);
			return;
		}
	}

	private boolean checkDeadline(Order order, Order next) {
		if(order.getDeadLine()==null)
			return false;
		if(next.getDeadLine()==null)
			return true;
		return order.getDeadLine().isBefore(next.getDeadLine());
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
