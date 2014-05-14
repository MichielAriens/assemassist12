package logic.assemblyline;

import org.joda.time.DateTime;

/**
 *
 */
public class Delay {
	/**
	 * 
	 */
	int delay;
	
	/**
	 * 
	 */
	DateTime dateOfDelay;
	
	/**
	 * 
	 * @param delay
	 * @param dateOfDelay
	 */
	public Delay (int delay, DateTime dateOfDelay){
		this.delay = delay;
		this.dateOfDelay = dateOfDelay;
	}

	/**
	 * 
	 * @return
	 */
	public int getDelay(){
		return delay;
	}
	
	/**
	 * 
	 * @return
	 */
	public DateTime getDateOfDelay() {
		return dateOfDelay;
	}
	
	
}
