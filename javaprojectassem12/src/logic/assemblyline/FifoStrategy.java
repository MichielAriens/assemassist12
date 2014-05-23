package logic.assemblyline;

import java.util.LinkedList;

import logic.order.Order;

/**
 * A class that uses FIFO strategy to add orders and refactors queues.
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
	 * Refactors a given queue using FIFO strategy. Just copies all orders from the copy queue into the given first queue.
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
	 * Returns a string which represents this scheduling strategy.
	 * @return	A string which represents this scheduling strategy.
	 */
	@Override
	public String toString(){
		return "FIFO";
	}

	/**
	 * Returns a copy of this scheduling strategy or null if an exception occurs.
	 * @return A copy of this scheduling strategy.
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

	@Override
	public String getStringRepresentation() {
		return this.toString();
	}

	@Override
	public String getExtraInformation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

}
