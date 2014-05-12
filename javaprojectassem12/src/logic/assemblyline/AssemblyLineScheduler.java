package logic.assemblyline;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import logic.car.Order;

import org.joda.time.DateTime;

public class AssemblyLineScheduler {
	
	private Collection<AssemblyLine> assemblyLines;
	
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
	 * All elegible assemblylines will calculate an estimated completiontime
	 */
	public void addOrder(Order order){
		
		Map<AssemblyLine, Integer> estimates = new HashMap<>();
		//Map every al to its estimate
		for(AssemblyLine al : assemblyLines){
			if(al.accepts(order));
			estimates.put(al, al.getHeuristicFor(order));
		}
		
		//chose the best estimate
		AssemblyLine best = null;
		for(AssemblyLine is : estimates.keySet()){
			if(best == null){
				best = is;
			}else{
				if(estimates.get(is).compareTo(estimates.get(best)) <= 0){
					best = is;
				}
			}
		}
		
		best.addOrder(order);
	}
	
	

}
