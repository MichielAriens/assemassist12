package logic.assemblyline;

import java.util.ArrayList;
import java.util.Collections;

import org.joda.time.DateTime;

/**
 * Class handling the statistics of an assembly line car manufacturing company.
 */
public class StatisticsAssemblyLine extends Statistics{
	/**
	 * Number of cars finished today.
	 */
	private int finishedCarOrdersToday;
	
	
	/**
	 * Creates statistics object for an assembly line by initializing the number of
	 * finished car orders today to 0.
	 * @param name Name to identify these statistics.
	 */
	public StatisticsAssemblyLine(String name){
		super(name);
		delays = new ArrayList<Delay>();
		finishedCarOrdersPerDay = new ArrayList<Integer>();
		this.finishedCarOrdersToday = 0;
	}
	
	/**
	 * Adds the given order to the list of finished orders and adds one to the number
	 * of finished car orders for today.
	 * @param delay	The delay of the finished order.
	 */
	public void finishedCarOrder(int delay, DateTime dateOfDelay){
		delays.add(new Delay(delay, dateOfDelay));
		Collections.sort(delays);
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
}