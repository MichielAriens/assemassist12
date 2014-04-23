package logic.assemblyline;

import java.util.ArrayList;
import java.util.Collections;
import logic.car.Order;

/**
 * Class handling the statistics of a car manufacturing company.
 */
public class Statistics {
	/**
	 * List of finished orders.
	 */
	private ArrayList<Order> finishedOrders = new ArrayList<Order>();
	
	/**
	 * List of the number of finished cars per day.
	 */
	private ArrayList<Integer> finishedCarOrdersPerDay = new ArrayList<Integer>();
	
	/**
	 * Number of cars finished today.
	 */
	private int finishedCarOrdersToday;
	
	/**
	 * Creates statistics object for an assembly line by initializing the number of
	 * finished car orders today to 0.
	 */
	public Statistics(){
		this.finishedCarOrdersToday = 0;
	}
	
	/**
	 * Adds the given order to the list of finished orders and adds one to the number
	 * of finished car orders for today.
	 * @param order	The finished car order that needs to be added.
	 */
	public void finishedCarOrder(Order order){
		finishedOrders.add(order);
		finishedCarOrdersToday++;
	}
	
	/**
	 * Adds the number of finished car orders for today to the list of finished car orders
	 * per day. Sets the number of finished car orders for today to zero.
	 */
	public void setNextDay(){
		finishedCarOrdersPerDay.add(finishedCarOrdersToday);
		finishedCarOrdersToday = 0;
	}
	
	/**
	 * Returns the average number of cars produced.
	 * @return the average number of cars produced.
	 */
	private int getAverageCarsProduced(){
		return getAverage(finishedCarOrdersPerDay);
	}

	/**
	 * Returns the median number of cars produced.
	 * @return the median number of cars produced.
	 */
	private int getMedianCarsProduced(){
		return getMedian(finishedCarOrdersPerDay);
	}
	
	/**
	 * Returns the average delay on an order.
	 * @return the average delay on an order.
	 */
	private int getAverageDelay(){
		ArrayList<Integer> delays = getAllDelays();
		return getAverage(delays);
	}
	
	/**
	 * Returns the median delay on an order.
	 * @return the median delay on an order.
	 */
	private int getMedianDelay(){
		ArrayList<Integer> delays = getAllDelays();
		return getMedian(delays);
	}

	/**
	 * Returns a new ArrayList with the delays of all orders.
	 * @return a new ArrayList with the delays of all orders.
	 */
	private ArrayList<Integer> getAllDelays(){
		ArrayList<Integer> delays = new ArrayList<Integer>();
		for(Order o : finishedOrders){
			delays.add(o.getDelay());
		}
		return delays;
	}
	
	/**
	 * Returns the average of the given list of integers.
	 * @param list	The list of integers of which the average needs to be calculated.
	 * @return	The average of the given list of integers.
	 */
	private int getAverage(ArrayList<Integer> list){
		int total = 0;
		for(int i : list){
			total += i;
		}
		int average;
		if(total == 0){
			average = 0;
		}
		else{
			average = total/list.size();
		}
		return average;
	}
	
	/**
	 * Returns the median of the given list of integers.
	 * @param list	The list of integers of which the median needs to be calculated.
	 * @return	The median of the given list of integers.
	 */
	private int getMedian(ArrayList<Integer> list){
		int median;
		if(list.size() == 0){
			median = 0;
		}
		else{
			ArrayList<Integer> sortedList = makeCopy(list);
			Collections.sort(sortedList);
			int middle = sortedList.size()/2;
			if(sortedList.size()%2 == 1){
				median = sortedList.get(middle);
			} 
			else{
				median = (sortedList.get(middle-1) + sortedList.get(middle))/2;
			}
		}
	    return median;
	}
	
	/**
	 * Returns a copy of the given list by making a new list and adding all object from the
	 * given list to the new list.
	 * @param list	The list to be copied.
	 * @return	A copy of the given list.
	 */
	private ArrayList<Integer> makeCopy(ArrayList<Integer> list) {
		ArrayList<Integer> copy = new ArrayList<Integer>();
		for(int i : list){
			copy.add(i);
		}
		return copy;
	}

	/**
	 * Returns a string representation of these statistics.
	 */
	@Override
	public String toString(){
		String statistics = "";
		statistics += "Average number of cars produced: " + getAverageCarsProduced() + "\n";
		statistics += "Mean number of cars produced: " + getMedianCarsProduced() + "\n";
		statistics += "Exact numbers two last days:\n";
		statistics += carsProducedXLastDays(2);
		statistics += "Average delay: " + getAverageDelay() +" minutes\n";
		statistics += "Mean delay: " + getMedianDelay() + " minutes\n";
		statistics += "Two last delays:\n";
		statistics += delayXLastDays(2);
		return statistics;
	}
	
	/**
	 * Returns a string representation of the number of cars produced for the last X days.
	 * @return a string representation of the number of cars produced for the last X days.
	 */
	private String carsProducedXLastDays(int days){
		String statistics = "";
		int maxIndex = finishedCarOrdersPerDay.size()-1;
		if(maxIndex >= 0){
			int numberOfCars = finishedCarOrdersPerDay.get(finishedCarOrdersPerDay.size()-1);
			statistics += "   1 day ago: " + numberOfCars + "\n";
			for(int i = 2; i <= days; i ++){
				if(i <= maxIndex){
					numberOfCars = finishedCarOrdersPerDay.get(finishedCarOrdersPerDay.size()-i);
					statistics += "   " + i + " days ago: " + numberOfCars + "\n";
				}
				else{
					statistics += "   No further records.\n";
					break;
				}
			}
		}
		else{
			statistics += "   No records.\n";
		}
		return statistics;
	}
	
	/**
	 * Returns a string representation of the last X delays and the day they occurred.
	 * @return a string representation of the last X delays and the day they occurred.
	 */
	private String delayXLastDays(int days){
		String statistics = "";
		int maxIndex = finishedOrders.size()-1;
		if(maxIndex >= 0){
			int delay = finishedOrders.get(finishedOrders.size()-1).getDelay();
			statistics += "   1) " + delay + " minutes\n";
			for(int i = 2; i <= days; i ++){
				if(i <= maxIndex){
					delay = finishedOrders.get(finishedOrders.size()-i).getDelay();
					statistics += "   " + i + ") " + delay + " minuts\n";
				}
				else{
					statistics += "   No further records.\n";
					break;
				}
			}
		}
		else{
			statistics += "   No records.\n";
		}
		return statistics;
	}
}
