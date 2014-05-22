package logic.assemblyline;

import interfaces.Printable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import logic.car.Order;
import logic.car.VehicleModel;
import logic.workstation.Task;
import logic.workstation.Workstation;
import logic.workstation.WorkstationChainBuilder;
import logic.workstation.WorkstationDirector;
import logic.workstation.WorkstationDirectorA;
import logic.workstation.WorkstationDirectorB;

import org.joda.time.DateTime;

/**
 * A class which is responsible for scheduling orders on the different assembly lines.
 */
public class AssemblyLineScheduler {
	
	/**
	 * The list of all the assembly lines.
	 */
	private List<AssemblyLine> assemblyLines;
	
	/**
	 * The current time of the system.
	 */
	private DateTime currentTime;
	
	/**
	 * The queue which holds orders if all the assembly lines are broken.
	 */
	private LinkedList<Order> overflowQueue;
	
	/**
	 * The statistics of the assembly line scheduler, which has the statistics of all the assembly lines in this assembly line scheduler.
	 */
	private StatisticsGeneral stats;
	
	/**
	 * Initializes the current time, the assembly lines, the overflow queue and the statistics.
	 */
	public AssemblyLineScheduler(){
		currentTime = new DateTime(2014, 1, 1, 6, 0);
		assemblyLines = new ArrayList<>();
		overflowQueue = new LinkedList<>();
		initializeAssemblylines();
		stats = new StatisticsGeneral("Generality");
	}
	
	/**
	 * Returns the current time of the system.
	 * @return	The current time of the system.
	 */
	public DateTime getCurrentTime(){
		return currentTime;
	}
	
	/**
	 * Accepts an order and distributes it to the best assembly line. 
	 * All eligible assembly lines will calculate an estimated completion time.
	 * If no assembly lines are available for queuing then the order is placed on the overflow queue.
	 * @param	order	The order that needs to be scheduled.
	 */
	public void addOrder(Order order){
		Map<AssemblyLine, DateTime> estimates = new HashMap<>();
		//Map every al to its estimate
		for(AssemblyLine al : getNonBrokenLines()){
			if(al.accepts(order)){
				estimates.put(al, al.getEstimate(order,currentTime));
			}
		}
		//choose the best estimate
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
	 * Repeatedly looks at the state of all the assembly lines and determines which assembly lines can be moved forwards.
	 * When moving, the current time is synchronized with the last known time information from the assembly lines. 
	 */
	private void advance(){
		while(advanceOnce());
	}
	
	/**
	 * Looks at the state of all the assembly lines and determines which assembly lines can be moved forward.
	 * Will move exactly one or no assembly lines.
	 * @return		true if a line was moved
	 * 				false if no lines were moved
	 */
	private boolean advanceOnce(){
		List<AssemblyLine> emptyLines = new ArrayList<AssemblyLine>();		
		if(linesReadyToMove()){
			AssemblyLine bestLine = null;
			for(AssemblyLine al :  getNonBrokenLines()){
				if(al.empty()){
					emptyLines.add(al);
				}
				else{
					if(bestLine == null && al.ready()){
						bestLine = al;
					}
					if(al.ready() && al.getCycleEnd().isBefore(bestLine.getcycleStartTime())){
						bestLine = al;
					}
				}
			}
			if(bestLine == null){
				return false;
			}
			this.currentTime = bestLine.getCycleEnd();
			bestLine.moveAssemblyLine(this.currentTime);
			checkDayEnds();
			for(AssemblyLine al : emptyLines){
				if(al.getcycleStartTime().getDayOfYear()==currentTime.getDayOfYear())
					al.setCycleStartTime(currentTime);
			}
			return true;
		}return false;
	}
	
	/**
	 * Checks whether all assembly lines are ready to start a new day. If so the current time is progressed and the assembly lines are informed. 
	 */
	private void checkDayEnds() {
		for(AssemblyLine al : this.assemblyLines){
			if(!al.readyForNextDay()){
				return;
			}
		}
		
		//Inform assembly lines
		for (AssemblyLine al : this.assemblyLines){
			al.setNewDay();
		}
		
		this.currentTime = assemblyLines.get(0).getcycleStartTime();
	}
	
	/**
	 * Will attempt to redistribute orders from the overflow queue to the assembly lines. 
	 * If this fails the orders will remain in the overflow queue.
	 */
	private void scheduleOverflowQueue(){	
		for(Order order : this.overflowQueue){
			this.addOrder(order);
		}
		this.overflowQueue = new LinkedList<>();
	}

	/**
	 * Returns all non-broken assembly lines.
	 * @return All the non-broken assembly lines.
	 */
	private List<AssemblyLine> getNonBrokenLines(){
		List<AssemblyLine> retval = new ArrayList<>();
		for(AssemblyLine al : this.assemblyLines){
			if (al.getOperationalStatus() != OperationalStatus.BROKEN){
				retval.add(0,al);
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
	public boolean doTask(Printable<Task> Task, Printable<AssemblyLine> assemblyLine, int minutes){
		AssemblyLine line = this.get(assemblyLine);
		boolean completed = line.doTask(Task, minutes);
		this.advance();
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
	private AssemblyLine get(Printable<AssemblyLine> line){
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
		this.addLine(new AssemblyLine(Arrays.asList(models), builder, this.currentTime, "Assembly Line 1"));
	}
	
	private void buildLine2() {
		WorkstationChainBuilder builder = new WorkstationChainBuilder();	
		WorkstationDirector director = new WorkstationDirectorA(builder);
		director.construct();
		
		VehicleModel[] models = {VehicleModel.CARMODELA, VehicleModel.CARMODELB, VehicleModel.CARMODELC};
		this.addLine(new AssemblyLine(Arrays.asList(models), builder, this.currentTime, "Assembly Line 2"));
	}
	
	private void buildLine3() {
		WorkstationChainBuilder builder = new WorkstationChainBuilder();	
		WorkstationDirector director = new WorkstationDirectorB(builder);
		director.construct();
		
		VehicleModel[] models = {VehicleModel.CARMODELA, VehicleModel.CARMODELB, VehicleModel.CARMODELC, VehicleModel.TRUCKMODELX, VehicleModel.TRUCKMODELY};
		this.addLine(new AssemblyLine(Arrays.asList(models), builder, this.currentTime, "Assembly Line 3"));
	}

	/**
	 * Returns the workstations of a given assembly line wrapped in the printable interface.
	 * @param 		assemblyLine wrapped in the printable interface.
	 * @return		A list of workstations wrapped in the printable interface
	 */
	public List<Printable<Workstation>> getWorkStationsFromAssemblyLine(Printable<AssemblyLine> assemblyLine) {
		return this.get(assemblyLine).getWorkStations();
	}
	
	/**
	 * Breaks an assemblyline: The assemblyline will be inhibited from moving and orders on its queue will be rescheduled to other
	 * lines where possible. 
	 * @param line
	 */
	public void breakAssemblyLine(Printable<AssemblyLine> line){
		AssemblyLine al = get(line);
		this.overflowQueue.addAll(al.changeStatus(OperationalStatus.BROKEN));
		this.scheduleOverflowQueue();
	}
	
	/**
	 * Brings a broken line back to the operational state. Orders on the line will be able to advance again.
	 * An attempt will be made to redistribute the overflow queue. 
	 * @param line
	 */
	public void fixAssemAssemblyLine(Printable<AssemblyLine> line){
		AssemblyLine al = get(line);
		if(al.getOperationalStatus() == OperationalStatus.BROKEN){
			al.fix(currentTime);
			this.scheduleOverflowQueue();
		}
	}
	
	/**
	 * Starts the preparation for maintenance on an assemblyline. The first workstation will reject all orders. The orders on 
	 * the line are still able to be completed. Once the line is empty the four-hour maintenance cycle starts. 
	 * The queue for this line is rescheduled with the 4 hour delay in mind.
	 * @param line
	 */
	public void startMaintenace(Printable<AssemblyLine> line){
		AssemblyLine al = this.get(line);
		this.overflowQueue.addAll(al.changeStatus(OperationalStatus.PREMAINTENANCE));
		this.scheduleOverflowQueue();
	}

	/**
	 * Returns all workstations in the system.
	 * @return		A list of all workstations wrapped in the printable interface.
	 */
	public List<Printable<Workstation>> getAllWorkstations() {
		List<Printable<Workstation>> retval = new LinkedList<>();
		for(AssemblyLine al : this.assemblyLines){
			retval.addAll(al.getWorkStations());
		}
		return retval;
	}

	/**
	 * Returns a list of printable objects of the list of assembly lines.
	 * @return a list of printable objects of the list of assembly lines.
 	 */
	public List<Printable<AssemblyLine>> getAssemblyLines() {
		List<Printable<AssemblyLine>> retval = new LinkedList<>();
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
	public boolean checkPhaseDuration(int duration, Printable<AssemblyLine> assemblyLine) {
		AssemblyLine al = this.get(assemblyLine);
		return al.checkPhaseDuration(duration);
	}

	/**
	 * Returns a map mapping each assembly line to it's status.
	 * @return a map mapping each assembly line to it's status.
	 */
	public Map<Printable<AssemblyLine>, Printable<OperationalStatus>> getAssemblyLinesStatuses() {
		Map<Printable<AssemblyLine>, Printable<OperationalStatus>> retval = new HashMap<>();
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
	public List<Printable<Task>> getAllTasksAt(Printable<Workstation> station, Printable<AssemblyLine> assemblyLine) {
		return this.get(assemblyLine).getAllTasks(station);
	}

	/**
	 * Returns a list of all required tasks at a given workstation.
	 * @param station			The a copy of the workstation for which the tasks are needed.
	 * @param assemblyLine		The assembly line of the workstation. 
	 * @return	A list of tasks at the given workstation.
	 */
	public List<Printable<Task>> getRequiredTasks(Printable<Workstation> station, Printable<AssemblyLine> assemblyLine) {
		return this.get(assemblyLine).getRequiredTasks(station);
	}

	/**
	 * Returns a string representation of the statistics of all the assembly lines of this
	 * assembly line scheduler.
	 * @return a string representation of the statistics of all the assembly lines of this
	 * assembly line scheduler.
	 */
	public String getStatistics() {
		String returnValue = "";
		ArrayList<StatisticsAssemblyLine> statsList = new ArrayList<StatisticsAssemblyLine>();
		for(AssemblyLine line : assemblyLines){
			returnValue += line.getStatistics() +"\n";
			statsList.add(line.getStatistics());
		}
		stats.updateRecords(statsList);
		returnValue += stats.toString();
		return returnValue;
	}

	/**
	 * Returns the strategies list from the given assembly line.
	 * @param activeAssemblyLine	The assembly line of which the strategies list must be returned.
	 * @return the strategies list from the given assembly line.
	 */
	public List<Printable<SchedulingStrategy>> getStrategies(Printable<AssemblyLine> activeAssemblyLine) {
		AssemblyLine active = get(activeAssemblyLine);
		if(active == null)
			return new ArrayList<Printable<SchedulingStrategy>>();
		return active.getStrategies();
	}
	
	
	/**
	 * Returns a map mapping each assembly line to it's strategies list.
	 * @return a map mapping each assembly line to it's strategies list.
	 */
	public Map<Printable<AssemblyLine>, List<Printable<SchedulingStrategy>>> getAssemblyLinesStrategies() {
		Map<Printable<AssemblyLine>, List<Printable<SchedulingStrategy>>> retval = new HashMap<>();
		for(AssemblyLine al : this.assemblyLines){
			retval.put(al, al.getStrategies());
		}
		return retval;
	}
	
	/**
	 * Returns the batch list for the given assembly line.
	 * @param assemblyline	The assembly line for which the batch list needs to be returned.
	 * @return the batch list for the given assembly line.
	 */
	public List<Order> getBatchList(Printable<AssemblyLine> assemblyline){
		return this.get(assemblyline).getBachList();
	}
	
	/**
	 * Returns the combined batch list of all assembly lines.
	 * @return the combined batch list of all assembly lines.
	 */
	public List<Order> getBatchList(){
		ArrayList<Order> returnValue = new ArrayList<Order>();
		for(AssemblyLine al : assemblyLines){
			for(Order order : al.getBachList()){
				returnValue.add(order);
			}
		}
		return returnValue;
	}
	
	/**
	 * Changes the strategy of the given assembly line according to the given order.
	 * @param order	The order that has to be used as a template for the strategy.
	 * @param assemblyLine	The assembly line of which the strategy needs to be changed.
	 */
	public void changeStrategy(Order order, Printable<AssemblyLine> assemblyline){
		this.get(assemblyline).changeStrategy(order);
	}
	
	/**
	 * Changes the strategy of all assembly lines according to the given order.
	 * @param order	The order that has to be used as a template for the strategy.
	 */
	public void changeStrategyAllLines(Order order) {
		for(AssemblyLine al : assemblyLines){
			al.changeStrategy(order);
		}
	}

	/**
	 * Changes the status of the given assembly line to the given status if the given status is different from the current status.
	 * @param activeAssemblyLine	The assembly line that receives a new status.
	 * @param newStatus	The new status of the given assembly line.
	 * @return	True if the new status could be set.
	 * 			False otherwise.
	 */
	public boolean changeAssemblyLineStatus(Printable<AssemblyLine> activeAssemblyLine,
			OperationalStatus newStatus) {
		AssemblyLine al = this.get(activeAssemblyLine);
		if(al.getOperationalStatus() == OperationalStatus.OPERATIONAL){
			if(newStatus == OperationalStatus.BROKEN){
				this.breakAssemblyLine(activeAssemblyLine);
				return true;
			}else if(newStatus == OperationalStatus.PREMAINTENANCE || newStatus == OperationalStatus.MAINTENANCE){
				this.startMaintenace(activeAssemblyLine);
				return true;
			}else{
				//no effect
				return false;
			}
		}else if(al.getOperationalStatus() == OperationalStatus.BROKEN){
			if(newStatus == OperationalStatus.OPERATIONAL){
				this.fixAssemAssemblyLine(activeAssemblyLine);
				return true;
			}else{
				return false;
			}
		}else if(al.getOperationalStatus() == OperationalStatus.MAINTENANCE){
			return false;
		}
		return false;

	}
}
