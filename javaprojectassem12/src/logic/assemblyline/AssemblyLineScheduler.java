package logic.assemblyline;

import interfaces.Printable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import logic.car.Order;
import logic.car.VehicleModel;
import logic.workstation.WorkstationChainBuilder;
import logic.workstation.WorkstationDirector;
import logic.workstation.WorkstationDirectorA;
import logic.workstation.WorkstationDirectorB;

import org.joda.time.DateTime;

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
	 * Do the task corresponding to the given task on the given assemblyline.
	 * @param Task				The task that needs to be completed wrapped in the printable interface.
	 * @param assemblyLine		The assemblyline that the task is on wrapped in the printable interface.
	 * @return
	 */
	public boolean doTask(Printable Task, Printable assemblyLine){
		AssemblyLine line = this.get(assemblyLine);
		return line.doTask(Task);
	}

	
	
	public boolean advance(){
		for(AssemblyLine al : assemblyLines){
			
		}
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
