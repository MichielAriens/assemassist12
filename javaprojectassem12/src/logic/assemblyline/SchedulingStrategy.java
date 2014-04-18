package logic.assemblyline;

import java.util.LinkedList;

import logic.car.Order;

/**
 * A class which adds an order to a queue in a specific way and refactors queue in some way.
 */
public abstract class SchedulingStrategy{
	
	/**
	 * The example order used for batch processing, null if batch processing is not used.
	 */
	protected Order example;

	/**
	 * Adds an order to the given queue and places it in the right spot.
	 * @param order	The order that needs to be added.
	 * @param queue	The queue where the order needs to be added.
	 */
	protected abstract void addOrder(Order order, LinkedList<Order> queue);

	/**
	 * Refactors a given queue and a copy of it so it matches the requirements.
	 * @param queue	The queue that needs to be refactored.
	 * @param copy	The copy of the queue that needs to be refactored, used to build the given queue.
	 */
	protected abstract void refactorQueue(LinkedList<Order> queue, LinkedList<Order> copy);
	
	/**
	 * Returns true if the deadline of the given order is earlier than the deadline of the given next order.
	 * @param order The order for which we want to check.
	 * @param next	The order against which we want to check.
	 * @return	True if the first orders deadline is before the next orders deadline.
	 * 			False if first order deadline is null or later than deadline of the next order.
	 */
	protected boolean checkDeadline(Order order, Order next) {
		if(order.getDeadLine()==null)
			return false;
		if(next.getDeadLine()==null)
			return true;
		return order.getDeadLine().isBefore(next.getDeadLine());
	}
	
	/**
	 * Sets the example order of this class to the given example if it is not null.
	 * @param order	The example order that needs to be checked.
	 */
	protected void setExample(Order order){
		if(order!=null)
			this.example = order;
	}
	
	/**
	 * Returns a copy of this class.
	 * @return A copy of this class.
	 */
	protected abstract SchedulingStrategy getRawCopy();
	
	public abstract String toString();
}
