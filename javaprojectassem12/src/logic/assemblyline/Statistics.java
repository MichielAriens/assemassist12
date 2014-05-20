package logic.assemblyline;

import java.util.ArrayList;
import java.util.Collections;
import org.joda.time.DateTime;

/**
 * Class handling the statistics of a car manufacturing company.
 */
public class Statistics {
	/**
	 * List of delays of the orders.
	 */
	protected ArrayList<Delay> delays;
	
	/**
	 * List of the number of finished cars per day.
	 */
	protected ArrayList<Integer> finishedCarOrdersPerDay;
	
	/**
	 * Name to identify these statistics.
	 */
	protected String name;
	
	/**
	 * Creates statistics object.
	 * @param name Name to identify these statistics.
	 */
	public Statistics(String name) {
		this.name = name;
	}
	
	/**
	 * Returns the list of all the delays.
	 * @return the list of all the delays.
	 */
	public ArrayList<Delay> getDelays(){
		return delays;
	}
	
	/**
	 * Returns the list of the number of finished cars per day.
	 * @return the list of the number of finished cars per day.
	 */
	public ArrayList<Integer> getFinishedCarOrdersPerDay(){
		return finishedCarOrdersPerDay;
	}
	
	/**
	 * Returns the average number of cars produced.
	 * @return the average number of cars produced.
	 */
	protected int getAverageCarsProduced(){
		return getAverage(finishedCarOrdersPerDay);
	}

	/**
	 * Returns the median number of cars produced.
	 * @return the median number of cars produced.
	 */
	protected int getMedianCarsProduced(){
		return getMedian(finishedCarOrdersPerDay);
	}
	
	/**
	 * Returns the average delay on an order.
	 * @return the average delay on an order.
	 */
	protected int getAverageDelay(){
		ArrayList<Integer> delaysTimes = getDelaysTimes();
		return getAverage(delaysTimes);
	}
	
	/**
	 * Returns the median delay on an order.
	 * @return the median delay on an order.
	 */
	protected int getMedianDelay(){
		ArrayList<Integer> delaysTimes = getDelaysTimes();
		return getMedian(delaysTimes);
	}
	
	/**
	 * Returns a list of all the delay times.
	 * @return a list of all the delay times.
	 */
	protected ArrayList<Integer> getDelaysTimes() {
		ArrayList<Integer> delaysTimes = new ArrayList<Integer>();
		for(Delay d : delays){
			delaysTimes.add(d.getDelayTime());
		}
		return delaysTimes;
	}
	
	/**
	 * Returns the average of the given list of integers.
	 * @param list	The list of integers of which the average needs to be calculated.
	 * @return	The average of the given list of integers.
	 */
	protected int getAverage(ArrayList<Integer> list){
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
	protected int getMedian(ArrayList<Integer> list){
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
	protected ArrayList<Integer> makeCopy(ArrayList<Integer> list) {
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
		String statistics = "Statistics of " + name + ":\n";
		statistics += "Average number of cars produced: " + getAverageCarsProduced() + "\n";
		statistics += "Mean number of cars produced: " + getMedianCarsProduced() + "\n";
		statistics += "Exact numbers two last days:\n";
		statistics += carsProducedXLastDays(2);
		statistics += "Average delay: " + getAverageDelay() +" minutes\n";
		statistics += "Mean delay: " + getMedianDelay() + " minutes\n";
		statistics += "Two last delays:\n";
		statistics += xLastDelays(2);
		return statistics;
	}
	
	/**
	 * Returns a string representation of the number of cars produced for the last X days.
	 * @param days	The number of  days of which the number of cars produced need to be returned.
	 * @return a string representation of the number of cars produced for the last X days.
	 */
	protected String carsProducedXLastDays(int days){
		String statistics = "";
		if(finishedCarOrdersPerDay.size() > 0){
			int numberOfCars;
			for(int i = days; i > 1 ; i--){
				if(finishedCarOrdersPerDay.size()-i > 0){
					numberOfCars = finishedCarOrdersPerDay.get(finishedCarOrdersPerDay.size()-i);
					statistics += "   " + i + " days ago: " + numberOfCars + "\n";
				}
				else{
					statistics += "   " + i + " days ago: No records.\n";
				}
			}
			numberOfCars = finishedCarOrdersPerDay.get(finishedCarOrdersPerDay.size()-1);
			statistics += "   1 day ago: " + numberOfCars + "\n";
		}
		else{
			statistics += "   No records.\n";
		}
		return statistics;
	}
	
	/**
	 * Returns a string representation of the last X delays and the day they occurred.
	 * @param number	The number of last delays that need to be returned.
	 * @return a string representation of the last X delays and the day they occurred.
	 */
	protected String xLastDelays(int number){
		String statistics = "";
		if(delays.size() > 0){
			int delayTime = 0;
			DateTime dateOfDelay = null;
			for(int i = number; i > 1 ; i--){
				if(delays.size() - i > 0){
					delayTime = delays.get(delays.size()-i).getDelayTime();
					dateOfDelay = delays.get(delays.size()-i).getDateOfDelay();
					statistics += "   " + i + ") " + delayTime + " minutes on " + dateOfDelay + "\n";
				}
				else{
					statistics += "   " + i + ") No records.\n";
					break;
				}
			}
			delayTime = delays.get(delays.size()-1).getDelayTime();
			dateOfDelay = delays.get(delays.size()-1).getDateOfDelay();
			statistics += "   1) " + delayTime + " minutes on " + dateOfDelay + "\n";
		}
		else{
			statistics += "   No records.\n";
		}
		return statistics;
	}
}
