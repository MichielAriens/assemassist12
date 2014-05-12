package logic.assemblyline;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import logic.car.Order;

import org.joda.time.DateTime;

public class AssemblyLineScheduler {
	
	private Set<AssemblyLine> assemblyLines;
	
	/**
	 * 
	 */
	public AssemblyLineScheduler(){
		assemblyLines = new HashSet<>();
	}
	
	/**
	 * 
	 * @param lines
	 */
	public void addAllLines(Collection<AssemblyLine> lines){
		assemblyLines.addAll(lines);
	}
	
	/**
	 * Accepts an order and distributes it to the best assemblyline.
	 */
	public void addOrder(Order order){
		Map<AssemblyLine, DateTime> estimates = new HashMap<>();
		//Map every al to its estimate
		for(AssemblyLine al : assemblyLines){
			estimates.put(al, al.getEstimateFor(order));
		}
		//chose the best estimate
	}
	
	

}
