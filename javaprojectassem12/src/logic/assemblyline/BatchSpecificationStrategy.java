package logic.assemblyline;

import java.util.LinkedList;

import logic.car.Order;

/**
 * A class that uses batch processing to add orders and refactor queues.
 */
public class BatchSpecificationStrategy extends SchedulingStrategy{

	/**
	 * A protected empty constructor.
	 */
	protected BatchSpecificationStrategy(){
		
	}
	
	/**
	 * Adds an order to the given queue. The order is placed at the front if it is the same as 
	 * the example order, otherwise added using the FIFO strategy.
	 * @param order	The order that needs to be added.
	 * @param queue	The queue where the order needs to be added.
	 */
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
		}
		else{
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
	}

	
	/**
	 * Refactors a given queue using batch processing and the example order variable. 
	 * Places all orders that are equal to the example order at the front of the queue.
	 * @param queue	The queue that needs to be refactored.
	 * @param copy	The copy of the queue that needs to be refactored, used to build the given queue.
	 */
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
	
	/**
	 * Returns a string which represents this scheduling strategy.
	 * @return	A string which represents this scheduling strategy.
	 */
	@Override
	public String toString(){
		return "Specification Batch";
	}

	/**
	 * Returns a copy of this scheduling strategy or null if an exception occurs.
	 * @return A copy of this scheduling strategy.
	 * @InstantionException If this class can't be instantiated.
	 * @IllegalAccessException	If there is no access.
	 */
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
