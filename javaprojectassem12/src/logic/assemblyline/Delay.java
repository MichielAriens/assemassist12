package logic.assemblyline;

import org.joda.time.DateTime;

/**
 *
 */
public class Delay implements Comparable<Delay> {
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

	@Override
	public int compareTo(Delay other) {
		if(other == null){
			return -1;
		}
		return this.dateOfDelay.compareTo(other.dateOfDelay);
	}
}
