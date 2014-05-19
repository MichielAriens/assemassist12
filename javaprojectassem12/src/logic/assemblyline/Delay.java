package logic.assemblyline;

import org.joda.time.DateTime;

/**
 * A class representing delays that happened on certain dates. 
 */
public class Delay implements Comparable<Delay> {
	/**
	 * How long the delay was in minutes.
	 */
	int delayTime;
	
	/**
	 * The date the delay took place.
	 */
	DateTime dateOfDelay;
	
	/**
	 * Constructs a new delay with the given delay time and date of the delay.
	 * @param delayTime	How long the delay was in minutes.
	 * @param dateOfDelay	The date the delay took place.
	 */
	public Delay (int delayTime, DateTime dateOfDelay){
		this.delayTime = delayTime;
		this.dateOfDelay = dateOfDelay;
	}

	/**
	 * Returns how long the delay was in minutes.
	 * @return how long the delay was in minutes
	 */
	public int getDelayTime(){
		return delayTime;
	}
	
	/**
	 * Returns the date of when the delay took place.
	 * @return the date of when the delay took place.
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
