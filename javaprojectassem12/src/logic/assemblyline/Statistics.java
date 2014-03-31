package logic.assemblyline;

import java.util.ArrayList;
import java.util.Collections;
import logic.car.Order;

/**
 * Class handling the statistics of a car manufacturing company.
 */
public class Statistics {
	//TODO: er voor zorgen dat als er nog geen finishedcarorders zijn dat er geen indexoutofboundexception worden gegeven
	/**
	 * 
	 */
	private ArrayList<Order> finishedOrders = new ArrayList<Order>();
	
	/**
	 * 
	 */
	private ArrayList<Integer> finishedCarOrdersPerDay = new ArrayList<Integer>();
	
	/**
	 * 
	 */
	private int finishedCarOrdersToday;
	
	/**
	 * 
	 */
	private int numberOfPhases;
	
	/**
	 * 
	 */
	public Statistics(int numberOfPhases){
		this.numberOfPhases = numberOfPhases;
		this.finishedCarOrdersToday = 0;
	}
	
	//TODO: in assemblyline, als een order gefinished is deze methode oproepen
	/**
	 * 
	 */
	public void finishedCarOrder(Order order){
		finishedOrders.add(order);
		finishedCarOrdersToday++;
	}
	
	//TODO: in assemblyline, als er naar de volgende dag wordt gegaan deze methode oproepen
	/**
	 * 
	 */
	public void setNextDay(){
		finishedCarOrdersPerDay.add(finishedCarOrdersToday);
		finishedCarOrdersToday = 0;
	}
	
	/**
	 * 
	 * @return
	 */
	private int getDaysPassed() {
		return finishedCarOrdersPerDay.size();
	}
	
	/**
	 * 
	 * @return
	 */
	private int getAverageCarsProduced(){
		return getAverage(finishedCarOrdersPerDay);
	}

	/**
	 * 
	 * @return
	 */
	private int getMedianCarsProduced(){
		return getMedian(finishedCarOrdersPerDay);
	}
	
	/**
	 * 
	 * @return
	 */
	private int getAverageDelay(){
		ArrayList<Integer> delays = getAllDelays();
		return getAverage(delays);
	}
	
	/**
	 * 
	 * @return
	 */
	private int getMedianDelay(){
		ArrayList<Integer> delays = getAllDelays();
		return getMedian(delays);
	}

	/**
	 * 
	 * @return
	 */
	private ArrayList<Integer> getAllDelays(){
		ArrayList<Integer> delays = new ArrayList<Integer>();
		for(Order o : finishedOrders){
			delays.add(o.getDelay());
		}
		return delays;
	}
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	private int getAverage(ArrayList<Integer> list){
		int total = 0;
		for(int i : list){
			total += i;
		}
		int average = total/list.size();
		return average;
	}
	
	/**
	 * 
	 * @return
	 */
	private int getMedian(ArrayList<Integer> list){
		int median;
		ArrayList<Integer> sortedList = makeCopy(list);
		Collections.sort(sortedList);
		int middle = sortedList.size()/2;
	    if(sortedList.size()%2 == 1){
	        median = sortedList.get(middle);
	    } 
	    else{
	        median = (sortedList.get(middle-1) + sortedList.get(middle))/2;
	    }
	    return median;
	}
	
	/**
	 * 
	 * @param list
	 * @return
	 */
	private ArrayList<Integer> makeCopy(ArrayList<Integer> list) {
		ArrayList<Integer> copy = new ArrayList<Integer>();
		for(int i : list){
			copy.add(i);
		}
		return copy;
	}
	
	//TODO: aanpassen
	/**
	 * 
	 */
	public String toString(){
		String statistics = "";
		statistics += "Average number of cars produced: " + getAverageCarsProduced() + "/n";
		statistics += "Mean number of cars produced: " + getMedianCarsProduced() + "/n";
		statistics += "Exact numbers two last days:/n";
		statistics += "One day ago: " + finishedCarOrdersPerDay.get(finishedCarOrdersPerDay.size()-1);
		statistics += "Two days ago: " + finishedCarOrdersPerDay.get(finishedCarOrdersPerDay.size()-2);
		
		
		statistics += "Average delay: " + getAverageDelay() +" minutes/n";
		statistics += "Mean delay: " + getMedianDelay() + " minutes/n";
		statistics += "Two last delays:/n";
		statistics += "One day ago: " + finishedCarOrdersPerDay.get(finishedCarOrdersPerDay.size()-1);
		statistics += "Two days ago: " + finishedCarOrdersPerDay.get(finishedCarOrdersPerDay.size()-2);
				
		return statistics;
	}
	
	/**
	 * 
	 * @return
	 */
	private String carsProducedXLastDays(int days){
		String statistics = "";
		statistics += "1 day ago: " + finishedCarOrdersPerDay.get(finishedCarOrdersPerDay.size()-1);
		for(int i = 2; i < days; i ++){
			statistics += i + " days ago: " + finishedCarOrdersPerDay.get(finishedCarOrdersPerDay.size()-i);
		}
		return statistics;
	}
	
	/**
	 * 
	 * @return
	 */
	private String delayXLastDays(int days){
		String statistics = "";
		int delay = finishedOrders.get(finishedOrders.size()-1).getDelay();
		statistics += "1 day ago: " + delay + " minutes/n";
		for(int i = 2; i < days; i ++){
			delay = finishedOrders.get(finishedOrders.size()-i).getDelay();
			statistics += i + " days ago: " + delay + " minuts/n";
		}
		return statistics;
	}
}
