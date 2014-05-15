package logic.assemblyline;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class handling the statistics of multiple assembly lines of a car manufacturing company.
 */
public class StatisticsGeneral extends Statistics{
	
	/**
	 * Creates statistics object to analyze the statistics for multiple assembly lines.
	 * @param name Name to identify these statistics.
	 */
	public StatisticsGeneral(String name){
		super(name);
	}

	/**
	 * Updates the records with the given statistics of assembly lines.
	 * @param stats	The statistics of the assembly lines that are needed for the update.
	 */
	public void updateRecords(List<StatisticsAssemblyLine> stats){
		delays = new ArrayList<Delay>();
		finishedCarOrdersPerDay = new ArrayList<Integer>();
		for(StatisticsAssemblyLine s : stats){
			for(Delay d : s.getDelays()){
				delays.add(d);
			}
			for(int i = 0; i < s.getFinishedCarOrdersPerDay().size(); i++){
				this.finishedCarOrdersPerDay.add(i, s.getFinishedCarOrdersPerDay().get(i));
			}
		}
		Collections.sort(delays);
	}
}
