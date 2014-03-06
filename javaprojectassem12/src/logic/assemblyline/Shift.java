package logic.assemblyline;

import java.sql.Time;
import java.util.Calendar;

public class Shift {
	private final Calendar normalStart;
	private final Calendar normalEnd;
	
	private long overtime;
	
	
	public Shift(Calendar start, Calendar end){
		this.normalStart = start;
		this.normalEnd = end;
	}
	
	public Calendar getAdjustedEnd(){
		return null;
	}
	
	
	
}
