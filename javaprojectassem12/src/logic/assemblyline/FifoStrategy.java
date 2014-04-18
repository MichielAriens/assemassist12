package logic.assemblyline;

import java.util.LinkedList;

import logic.car.Order;

/**
 * A class that uses fifo strategy to add orders and refactor queues.
 */
public class FifoStrategy extends SchedulingStrategy{

	/**
	 * Adds an order to the given queue and places it at the end of the queue if it has no deadline,
	 * else places it in between orders so the deadline can be achieved.
	 * @param order	The order that needs to be added.
	 * @param queue	The queue where the order needs to be added.
	 */
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

	/**
	 * Refactors a given queue using fifo strategy. Just copies all orders in the copy queue to the given first queue.
	 * @param queue	The queue that needs to be refactored.
	 * @param copy	The copy of the queue that needs to be refactored, used to build the given queue.
	 */
	@Override
	protected void refactorQueue(LinkedList<Order> queue, LinkedList<Order> copy) {
		for(Order next:copy){
			queue.add(next);
		}
	}
	
	/**
	 * Returns a string which represents this class.
	 * @return	A string which represents this class.
	 */
	@Override
	public String toString(){
		return "FIFO";
	}

	/**
	 * Returns a copy of this class or null if an exception occurs.
	 * @return A copy of this class.
	 * @InstantionException If this class cann't be instantiated.
	 * @IllegalAccessException	If there is no access.
	 */
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
