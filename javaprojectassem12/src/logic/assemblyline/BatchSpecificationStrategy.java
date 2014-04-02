package logic.assemblyline;

import java.util.LinkedList;

import logic.car.Order;

public class BatchSpecificationStrategy extends SchedulingStrategy{

	protected BatchSpecificationStrategy(){
		
	}
	
	@Override
	protected void addOrder(Order order, LinkedList<Order> queue) {
		int index = 0;
		if(!order.equals(example) || queue.isEmpty()){
			
			for(Order next : queue){
				if(!next.equals(example)){
					if(checkDeadline(order, next))
						break;
				}
				index++;
			}
		}else{
			
			for(Order next : queue){
				if(!next.equals(example)){
					break;
				}else{
					if(checkDeadline(order, next))
						break;
				}
				index++;
			}
			
		}
		queue.add(index, order);
		return;
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
	
	public String toString(){
		return "Specification Batch";
	}

}
