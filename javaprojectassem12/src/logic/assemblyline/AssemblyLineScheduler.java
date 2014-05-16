package logic.assemblyline;

import interfaces.Printable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
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
	
	private List<AssemblyLine> assemblyLines;
	private DateTime currentTime;
	private LinkedList<Order> overflowQueue;
	private StatisticsGeneral stats;
	
	/**
	 * 
	 */
	public AssemblyLineScheduler(){
		assemblyLines = new ArrayList<>();
		overflowQueue = new LinkedList<>();
		initializeAssemblylines();
		stats = new StatisticsGeneral("General statistics");
	}
	
	public DateTime getCurrentTime(){
		return currentTime;
	}
	
	/**
	 * Accepts an order and distributes it to the best assembly line. 
	 * All eligible assembly lines will calculate an estimated completion time
	 * If no assembly lines are available for queuing then the order is placed on the overflow queue.
	 * @param	order	...
	 */
	public void addOrder(Order order){
		Map<AssemblyLine, DateTime> estimates = new HashMap<>();
		//Map every al to its estimate
		for(AssemblyLine al : getNonBrokenLines()){
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
		if(best == null){
			overflowQueue.add(order);
		}else{
			best.addOrder(order);
		}
	}
	
	/**
	 * Looks at the state of all the assembly lines at determines which assembly lines can be moved forwards.
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
	 * Returns all non-broken assembly lines.
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
	 * Do the task corresponding to the given task on the given assembly line.
	 * If the task is completed the given assembly line is checked for advancement
	 * If the line advances this scheduler is advanced
	 * @param Task				The task that needs to be completed wrapped in the printable interface.
	 * @param assemblyLine		The assembly line that the task is on wrapped in the printable interface.
	 * @return
	 */
	public boolean doTask(Printable Task, Printable assemblyLine, int minutes){
		AssemblyLine line = this.get(assemblyLine);
		boolean completed = line.doTask(Task, minutes);
		if(completed){
			//try to advance the system.
			this.advance();
		}
		return completed;
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
	 * Will get the correct assembly line matching the one encapsulated within the parameter or null 
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

	/**
	 * Returns the workstations of a given assembly line wrapped in the printable interface.
	 * @param 		assemblyLine wrapped in the printable interface.
	 * @return		A list of workstations wrapped in the printable interface
	 */
	public List<Printable> getWorkStationsFromAssemblyLine(Printable assemblyLine) {
		return this.get(assemblyLine).getWorkStations();
	}

	/**
	 * Returns all workstations in the system.
	 * @return		A list of all workstations wrapped in the printable interface.
	 */
	public List<Printable> getAllWorkstations() {
		List<Printable> retval = new LinkedList<>();
		for(AssemblyLine al : this.assemblyLines){
			retval.addAll(al.getWorkStations());
		}
		return retval;
	}

	/**
	 * Returns a list of printable objects of the list of assembly lines.
	 * @return a list of printable objects of the list of assembly lines.
 	 */
	public List<Printable> getAssemblyLines() {
		List<Printable> retval = new LinkedList<Printable>();
		for(AssemblyLine al : assemblyLines){
			retval.add(al);
		}
		return retval;
	}

	
	/**
	 * Asks the assembly line to check the given duration, if the duration is allowed it returns true, otherwise false.
	 * @param duration	The duration that needs to be checked.
	 * @return 	True if the duration is allowed.
	 * 			False otherwise
	 */
	public boolean checkPhaseDuration(int duration, Printable assemblyLine) {
		AssemblyLine al = this.get(assemblyLine);
		return al.checkPhaseDuration(duration);
	}

	/**
	 * Returns a map mapping each assembly line to it's status.
	 * @return a map mapping each assembly line to it's status.
	 */
	public Map<Printable, Printable> getAssemblyLinesStatuses() {
		Map<Printable,Printable> retval = new HashMap<>();
		for(AssemblyLine al : this.assemblyLines){
			retval.put(al, al.getOperationalStatus());
		}
		return retval;
	}

	/**
	 * Returns a list of all tasks at a given workstation.
	 * @param station			The a copy of the workstation for which the tasks are needed.
	 * @param assemblyLine		The assembly line of the workstation. 
	 * @return	A list of tasks at the given workstation.
	 */
	public List<Printable> getAllTasksAt(Printable station, Printable assemblyLine) {
		return this.get(assemblyLine).getAllTasks(station);
	}

	/**
	 * Returns a string representation of the statistics of all the assembly lines of this
	 * assembly line scheduler.
	 * @return a string representation of the statistics of all the assembly lines of this
	 * assembly line scheduler.
	 */
	public String getStatistics() {
		ArrayList<StatisticsAssemblyLine> statsList = new ArrayList<StatisticsAssemblyLine>();
		for(AssemblyLine line : assemblyLines){
			statsList.add(line.getStatistics());
		}
		stats.updateRecords(statsList);
		return stats.toString();
	}
}
