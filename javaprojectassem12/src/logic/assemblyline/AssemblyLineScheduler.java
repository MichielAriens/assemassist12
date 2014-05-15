package logic.assemblyline;

import interfaces.Printable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import logic.car.Order;
import logic.car.VehicleModel;
import logic.workstation.WorkstationChainBuilder;
import logic.workstation.WorkstationDirector;
import logic.workstation.WorkstationDirectorA;
import logic.workstation.WorkstationDirectorB;

import org.joda.time.DateTime;

import com.sun.xml.internal.bind.v2.model.core.Adapter;

public class AssemblyLineScheduler {
	
	private Set<AssemblyLine> assemblyLines;
	private DateTime currentTime;
	
	/**
	 * 
	 */
	public AssemblyLineScheduler(){
		assemblyLines = new HashSet<>();
		initializeAssemblylines();
	}
	
	public DateTime getCurrentTime(){
		return currentTime;
	}
	
	/**
	 * Accepts an order and distributes it to the best assemblyline. 
	 * All elegible assemblylines will calculate an estimated completiontime
	 * @param	order	...
	 */
	public void addOrder(Order order){
		
		Map<AssemblyLine, DateTime> estimates = new HashMap<>();
		//Map every al to its estimate
		for(AssemblyLine al : assemblyLines){
			if(al.accepts(order));
			estimates.put(al, al.getEstimate(order));
		}
		
		//chose the best estimate
		AssemblyLine best = null;
		for(AssemblyLine is : estimates.keySet()){
			if(best == null){
				best = is;
			}else{
				if(estimates.get(is).isBefore(estimates.get(best))){
					best = is;
				}
			}
		}
		
		best.addOrder(order);
	}
	
	/**
	 * Looks at the state of all the assembly lines at determines which assemblylines can be moved forwards.
	 * @return		true if a line was moved
	 * 				false if no lines were moved
	 */
	private boolean advance(){
		if(linesReadyToMove()){
			AssemblyLine bestLine = null;
			for(AssemblyLine al :  getNonBrokenLines()){
				if(bestLine == null){
					bestLine = al;
				}
				if(al.getCycleEnd().isBefore(bestLine.getcycleStartTime())){
					bestLine = al;
				}
				
			}
			this.currentTime = bestLine.getCycleEnd();
			bestLine.moveAssemblyLine(this.currentTime);
			
		}return false;
	}
	
	/**
	 * Returns all non-broken assemblylines.
	 * @return
	 */
	private Set<AssemblyLine> getNonBrokenLines(){
		Set<AssemblyLine> retval = new HashSet<>();
		for(AssemblyLine al : this.assemblyLines){
			if (al.getOperationalStatus() != OperationalStatus.BROKEN){
				retval.add(al);
			}
		}
		return retval;
	}
	
	/**
	 * Do the task corresponding to the given task on the given assemblyline.
	 * @param Task				The task that needs to be completed wrapped in the printable interface.
	 * @param assemblyLine		The assemblyline that the task is on wrapped in the printable interface.
	 * @return
	 */
	public boolean doTask(Printable Task, Printable assemblyLine, int minutes){
		AssemblyLine line = this.get(assemblyLine);
		return line.doTask(Task, minutes);
	}
	
	
	/**
	 * Checks whether all non-broken lines are ready to move.
	 * If all lines are broken the system is not ready to move. 
	 * @return
	 */
	private boolean linesReadyToMove(){		
		int brokenLines = 0;
		for(AssemblyLine al : assemblyLines){
			if(al.getOperationalStatus() != OperationalStatus.BROKEN){
				if(!al.tryMoveAssemblyLine()){
					return false;
				}
			}else{
				brokenLines ++;
			}
		}
		if(brokenLines >= assemblyLines.size()){
			return false;
		}
		return true;
	}
	
	/**
	 * Will get the correct assemblyline matching the one encapsulated within the parameter or null 
	 * @param line
	 * @return
	 */
	private AssemblyLine get(Printable line){
		for(AssemblyLine al : assemblyLines){
			if(al.equals(line)){
				return al;
			}
		}
		return null;
	}
	
	/**
	 * Add an assembly line to this scheduler.
	 * @param line
	 */
	private void addLine(AssemblyLine line){
		assemblyLines.add(line);
	}

	private void initializeAssemblylines(){
		buildLine1();
		buildLine2();
		buildLine3();
	}
	
	/**
	 * NOTE: BUILDER PATTERN MUCH?
	 */
	

	private void buildLine1(){
		WorkstationChainBuilder builder = new WorkstationChainBuilder();	
		WorkstationDirector director = new WorkstationDirectorA(builder);
		director.construct();
		
		VehicleModel[] models = {VehicleModel.CARMODELA, VehicleModel.CARMODELB};
		this.addLine(new AssemblyLine(Arrays.asList(models), builder));
	}
	
	private void buildLine2() {
		WorkstationChainBuilder builder = new WorkstationChainBuilder();	
		WorkstationDirector director = new WorkstationDirectorA(builder);
		director.construct();
		
		VehicleModel[] models = {VehicleModel.CARMODELA, VehicleModel.CARMODELB, VehicleModel.CARMODELC};
		this.addLine(new AssemblyLine(Arrays.asList(models), builder));
	}
	
	private void buildLine3() {
		WorkstationChainBuilder builder = new WorkstationChainBuilder();	
		WorkstationDirector director = new WorkstationDirectorB(builder);
		director.construct();
		
		VehicleModel[] models = {VehicleModel.CARMODELA, VehicleModel.CARMODELB, VehicleModel.CARMODELC, VehicleModel.TRUCKMODELX, VehicleModel.TRUCKMODELY};
		this.addLine(new AssemblyLine(Arrays.asList(models), builder));
	}
	
	

}
