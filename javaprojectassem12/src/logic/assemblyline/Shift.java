package logic.assemblyline;

import java.sql.Time;

public class Shift {
	private final Time normalStart;
	private final Time normalEnd;
	
	private int overtime;
	
	
	public Shift(Time start, Time end){
		this.normalStart = start;
		this.normalEnd = end;
	}
	
	
	
}