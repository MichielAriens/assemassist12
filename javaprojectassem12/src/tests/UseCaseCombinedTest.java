package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import logic.order.VehicleModel;
import logic.order.VehiclePartType;
import logic.users.CarManufacturingCompany;

import org.junit.Test;

import controllers.AssemAssistController;
import controllers.CustomsManagerController;
import controllers.GarageHolderController;
import controllers.ManagerController;
import controllers.MechanicController;

/**
 * A test case to test the use case in which all users work together, the idea is to provide
 * a realistic simulation of the system use.
 */
public class UseCaseCombinedTest {
	
	/**
	 * A test that tries to cover a whole scenario involving all kinds of users and different sort of inputs.
	 */
	@Test
	public void mainSuccesTest() {
		
		AssemAssistController controller = new AssemAssistController(new CarManufacturingCompany());
		assertEquals(null, controller.logIn("fout"));
		
		////////////////////////////////////////////////////////////////////////////////////////////////
		//////////////////////////////START MANAGERTEST EMPTY ASSEMBLY LINE/////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////
		
		ManagerController maCont = new ManagerController();
		assertEquals(null,maCont.getUserName());
		assertEquals(null,maCont.getBatchListActiveLine());
		maCont = (ManagerController) controller.logIn("Wander");
		assertEquals("Wander", maCont.getUserName());
		
		//Check the statistics test
		String stats = "";
		stats += "Statistics of Assembly Line 1:\n";
		stats += "Average number of cars produced: 0\n";
		stats += "Mean number of cars produced: 0\n";
		stats += "Exact numbers two last days:\n";
		stats += "   No records.\n";
		stats += "Average delay: 0 minutes\n";
		stats += "Mean delay: 0 minutes\n";
		stats += "Two last delays:\n";
		stats += "   No records.\n";
		stats += "\n";
		stats += "Statistics of Assembly Line 2:\n";
		stats += "Average number of cars produced: 0\n";
		stats += "Mean number of cars produced: 0\n";
		stats += "Exact numbers two last days:\n";
		stats += "   No records.\n";
		stats += "Average delay: 0 minutes\n";
		stats += "Mean delay: 0 minutes\n";
		stats += "Two last delays:\n";
		stats += "   No records.\n";
		stats += "\n";
		stats += "Statistics of Assembly Line 3:\n";
		stats += "Average number of cars produced: 0\n";
		stats += "Mean number of cars produced: 0\n";
		stats += "Exact numbers two last days:\n";
		stats += "   No records.\n";
		stats += "Average delay: 0 minutes\n";
		stats += "Mean delay: 0 minutes\n";
		stats += "Two last delays:\n";
		stats += "   No records.\n";
		stats += "\n";
		stats += "Statistics of Generality:\n";
		stats += "Average number of cars produced: 0\n";
		stats += "Mean number of cars produced: 0\n";
		stats += "Exact numbers two last days:\n";
		stats += "   No records.\n";
		stats += "Average delay: 0 minutes\n";
		stats += "Mean delay: 0 minutes\n";
		stats += "Two last delays:\n";
		stats += "   No records.\n";
		assertEquals(stats, maCont.getStatistics());
		
		//Change scheduling algorithm
		maCont.setAssemblyLine("Assembly Line 1");
		ArrayList<String> strats = new ArrayList<String>();
		strats.add("FIFO");
		strats.add("FIFO");
		strats.add("Specification Batch");
		assertEquals(strats,maCont.getStrategiesActiveLine());
		ArrayList<String> batchList = new ArrayList<String>();
		assertEquals(batchList,maCont.getBatchListActiveLine());
		assertFalse(maCont.changeToFIFOActiveLine());
		maCont.changeStrategyToBatchProcessingActiveLine(0);
		
		//try to change the scheduling algorithm of all assembly lines
		ArrayList<ArrayList<String>> allStrats = new ArrayList<>();
		ArrayList<String> possibleStrats = new ArrayList<String>();
		ArrayList<String> assemblyStrats = new ArrayList<String>();
		assemblyStrats.add("Assembly Line 1: FIFO");
		assemblyStrats.add("Assembly Line 2: FIFO");
		assemblyStrats.add("Assembly Line 3: FIFO");
		possibleStrats.add("FIFO");
		possibleStrats.add("Specification Batch");
		allStrats.add(assemblyStrats);
		allStrats.add(possibleStrats);
		assertEquals(allStrats,maCont.getStrategiesAllLines());
		ArrayList<String> allBatchList = new ArrayList<String>();
		assertEquals(allBatchList,maCont.getBatchListAllLines());
		maCont.changeToFIFOAllLines();
		maCont.changeStrategyToBatchProcessingAllLines(0);
		
		////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////END MANAGERTEST EMPTY ASSEMBLY LINE//////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////////////////////////////
		//////////////////////////////START MECHANICTEST EMPTY ASSEMBLY LINE////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////
		
		//tries to do stuff with a mechanic that is not logged in.
		MechanicController meCont = new MechanicController();
		assertEquals(null,meCont.getUserName());
		assertEquals(null,meCont.getTasksPerWorkstation());
		assertEquals(null,meCont.getWorkStationsFromAssemblyLine());
		assertEquals(null,meCont.getTasks());
		assertEquals(null,meCont.getTaskInformation(""));
		meCont.doTask("", 0); //should just return
		meCont.setWorkStation(""); //should just return
		
		//Logs in the proper mechanic and asks for tasks.
		meCont = (MechanicController) controller.logIn("mech");
		assertEquals("mech", meCont.getUserName());
		meCont.setAssemblyLine(meCont.getAssemblyLines().get(0));
		ArrayList<String> tasks = new ArrayList<String>();
		tasks.add("= Assembly Line 1 [OPERATIONAL] =");
		tasks.add(" Car Body Post:\n   Inactive.\n");
		tasks.add(" Drive Train Post:\n   Inactive.\n");
		tasks.add(" Accessories Post:\n   Inactive.\n");
		tasks.add("= Assembly Line 2 [OPERATIONAL] =");
		tasks.add(" Car Body Post:\n   Inactive.\n");
		tasks.add(" Drive Train Post:\n   Inactive.\n");
		tasks.add(" Accessories Post:\n   Inactive.\n");
		tasks.add("= Assembly Line 3 [OPERATIONAL] =");
		tasks.add(" Car Body Post:\n   Inactive.\n");
		tasks.add(" Cargo Post:\n   Inactive.\n");
		tasks.add(" Drive Train Post:\n   Inactive.\n");
		tasks.add(" Accessories Post:\n   Inactive.\n");
		tasks.add(" Certification Post:\n   Inactive.\n");
		assertEquals(tasks, meCont.getTasksPerWorkstation());
		
		//Sets the active workstation of the logged in mechanic.
		ArrayList<String> stations = new ArrayList<String>();
		stations.add("Car Body Post: 1");
		stations.add("Drive Train Post: 2");
		stations.add("Accessories Post: 3");
		meCont.setAssemblyLine("Assembly Line 1");
		assertEquals(stations, meCont.getWorkStationsFromAssemblyLine());
		meCont.setWorkStation("Car Body Post");
		ArrayList<String> availableTasks = new ArrayList<String>();
		assertEquals(availableTasks, meCont.getTasks());
		
		////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////END MECHANICTEST EMPTY ASSEMBLY LINE/////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////START GARAGEHOLDERTEST EMPTY ASSEMBLY LINE///////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////
		
		//Tries to do things with a not logged in garage holder.
		GarageHolderController gaCont = new GarageHolderController();
		assertEquals(null, gaCont.getUserName());
		assertEquals(null, gaCont.getPendingOrders());
		assertEquals(null, gaCont.getPendingInfo(0));
		assertEquals(null, gaCont.getCompletedInfo(0));
		assertEquals(null, gaCont.getCompletedOrders());
		gaCont.placeOrder(); //should just return
		
		//Logs in the proper garage holder and asks for information.
		gaCont = (GarageHolderController) controller.logIn("gar");
		assertEquals("gar", gaCont.getUserName());
		
		ArrayList<String> empty = new ArrayList<String>();
		assertEquals(empty, gaCont.getPendingOrders());
		assertEquals(empty, gaCont.getCompletedOrders());
		
		//Start building an order.
		ArrayList<String> models = new ArrayList<String>();
		models.add("Car Model A: 1");
		models.add("Car Model B: 2");
		models.add("Car Model C: 3");
		models.add("Truck Model X: 4");
		models.add("Truck Model Y: 5");
		assertEquals(models, gaCont.getModels());
		
		gaCont.chooseModel(null); //should just return
		gaCont.chooseModel(VehicleModel.CARMODELA);
		
		gaCont.getOptions(null); //should just return
		ArrayList<String> options = new ArrayList<String>();
		options.add("Sedan: 1");
		options.add("Break: 2");
		assertEquals(options, gaCont.getOptions(VehiclePartType.Body));
		
		gaCont.addPart(null); //should just return
		gaCont.addPart("Sedan");
		gaCont.addPart("Red");
		gaCont.addPart("Standard 2l v4");
		gaCont.addPart("6 speed manual");
		gaCont.addPart("Leather black");
		gaCont.addPart("Manual");
		gaCont.addPart("Comfort");
		gaCont.addPart("No Spoiler");
		gaCont.addPart("No Toolstorage");
		gaCont.addPart("No Cargo Protection");
		gaCont.addPart("No Certification");
		
		gaCont.placeOrder();
		ArrayList<String> pending = new ArrayList<String>();
		pending.add("Pending, est. completion at: 01-01-2014 08:30");
		assertEquals(pending, gaCont.getPendingOrders());
		
		assertEquals(null, gaCont.getPendingInfo(2));
		assertEquals(null, gaCont.getPendingInfo(-1));
		assertEquals(null, gaCont.getCompletedInfo(2));
		assertEquals(null, gaCont.getCompletedInfo(-1));
		String info = "   Specifications:   Car Model A; (Sedan, Red, Standard 2l v4, 6 speed manual, Leather black, Manual, Comfort, No Spoiler, No Toolstorage, No Cargo Protection, No Certification)\n   Start Time:       01-01-2014 06:00\n   Est. End Time:    01-01-2014 08:30\n";
		assertEquals(info, gaCont.getPendingInfo(0));
		gaCont.placeOrder();
		gaCont.placeOrder();
		gaCont.placeOrder();
		gaCont.placeOrder();
		gaCont.placeOrder();
		gaCont.placeOrder();
		gaCont.placeOrder();
		gaCont.placeOrder();
		gaCont.placeOrder();
		gaCont.placeOrder();
		gaCont.placeOrder();

		////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////END GARAGEHOLDERTEST EMPTY ASSEMBLY LINE/////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////START CUSTOMSHOPMANAGERTEST//////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////
		
		//Tries to do things with a not logged in custom shop manager 
		CustomsManagerController cuCont = new CustomsManagerController();
		assertEquals(null, cuCont.getUserName());
		assertEquals(null, cuCont.placeOrder());
		
		//Logs in the proper custom shop manager.
		cuCont = (CustomsManagerController) controller.logIn("cust");
		assertEquals("cust", cuCont.getUserName());
		
		// Asks for information and then builds a task order.
		cuCont.choosePart(null);//should just return
		ArrayList<String> types = new ArrayList<String>();
		types.add("Colour: 1");
		types.add("Seats: 2");
		assertEquals(types,cuCont.getAvailableTypes());
		ArrayList<String> customOptions = new ArrayList<String>();
		customOptions.add("Red: 1");
		customOptions.add("Blue: 2");
		customOptions.add("Black: 3");
		customOptions.add("White: 4");
		customOptions.add("Green: 5");
		customOptions.add("Yellow: 6");
		assertEquals(customOptions,cuCont.getAvailableOptions("Colour"));
		cuCont.choosePart("Red");
		assertFalse(cuCont.chooseDeadLine("dur"));
		assertTrue(cuCont.chooseDeadLine("02-02-2015 17:55"));
		assertEquals("Estimated completion: 01-01-2014 09:30", cuCont.placeOrder());
		
		
		////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////END CUSTOMSHOPMANAGERTEST///////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////START MANAGERTEST CHANGE STRATEGY///////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////
		
		//Logs in the proper manager and asks for strategy information.
		maCont = (ManagerController) controller.logIn("Wander");
		strats = new ArrayList<String>();
		strats.add("FIFO");
		strats.add("FIFO");
		strats.add("Specification Batch");
		maCont.setAssemblyLine("Assembly Line 1");
		assertEquals(strats,maCont.getStrategiesActiveLine());
		
		//Asks for orders which can be selected for batch processing.
		batchList = new ArrayList<String>();
		batchList.add("   1: Option 1:\n      - Sedan\n      - Red\n      - Standard 2l v4\n      - 6 speed manual\n      - Leather black\n      - Manual\n      - Comfort\n      - No Spoiler\n      - No Toolstorage\n      - No Cargo Protection\n      - No Certification\n\n");
		
		assertEquals(batchList,maCont.getBatchListActiveLine());
		assertFalse(maCont.changeToFIFOActiveLine());
		maCont.changeStrategyToBatchProcessingActiveLine(0);
		assertTrue(maCont.changeToFIFOActiveLine());
		
		//try to change the scheduling algorithm of all assembly lines
		allStrats = new ArrayList<>();
		possibleStrats = new ArrayList<String>();
		assemblyStrats = new ArrayList<String>();
		assemblyStrats.add("Assembly Line 1: FIFO");
		assemblyStrats.add("Assembly Line 2: FIFO");
		assemblyStrats.add("Assembly Line 3: FIFO");
		possibleStrats.add("FIFO");
		possibleStrats.add("Specification Batch");
		allStrats.add(assemblyStrats);
		allStrats.add(possibleStrats);
		assertEquals(allStrats,maCont.getStrategiesAllLines());
		allBatchList = new ArrayList<String>();
		allBatchList.add("   1: Option 1:\n      - Sedan\n      - Red\n      - Standard 2l v4\n      - 6 speed manual\n      - Leather black\n      - Manual\n      - Comfort\n      - No Spoiler\n      - No Toolstorage\n      - No Cargo Protection\n      - No Certification\n\n");
		allBatchList.add("   2: Option 2:\n      - Sedan\n      - Red\n      - Standard 2l v4\n      - 6 speed manual\n      - Leather black\n      - Manual\n      - Comfort\n      - No Spoiler\n      - No Toolstorage\n      - No Cargo Protection\n      - No Certification\n\n");
		allBatchList.add("   3: Option 3:\n      - Sedan\n      - Red\n      - Standard 2l v4\n      - 6 speed manual\n      - Leather black\n      - Manual\n      - Comfort\n      - No Spoiler\n      - No Toolstorage\n      - No Cargo Protection\n      - No Certification\n\n");
		assertEquals(allBatchList,maCont.getBatchListAllLines());
		maCont.changeToFIFOAllLines();
		maCont.changeStrategyToBatchProcessingAllLines(0);

		////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////END MANAGERTEST CHANGE STRATEGY/////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////

		////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////START MECHANICTEST PERFORM TASKS////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////

		//Logs in the proper mechanic and asks for tasks in workstations.
		meCont = (MechanicController) controller.logIn("mech");
		assertEquals("mech", meCont.getUserName());
		tasks = new ArrayList<String>();
		tasks.add("= Assembly Line 1 [OPERATIONAL] =");
		tasks.add(" Car Body Post:\n   -Install Body= Sedan: Pending\n   -Install Colour= Red: Pending\n");
		tasks.add(" Drive Train Post:\n   Inactive.\n");
		tasks.add(" Accessories Post:\n   Inactive.\n");
		tasks.add("= Assembly Line 2 [OPERATIONAL] =");
		tasks.add(" Car Body Post:\n   -Install Body= Sedan: Pending\n   -Install Colour= Red: Pending\n");
		tasks.add(" Drive Train Post:\n   Inactive.\n");
		tasks.add(" Accessories Post:\n   Inactive.\n");
		tasks.add("= Assembly Line 3 [OPERATIONAL] =");
		tasks.add(" Car Body Post:\n   -Install Body= Sedan: Pending\n   -Install Colour= Red: Pending\n");
		tasks.add(" Cargo Post:\n   Inactive.\n");
		tasks.add(" Drive Train Post:\n   Inactive.\n");
		tasks.add(" Accessories Post:\n   Inactive.\n");
		tasks.add(" Certification Post:\n   Inactive.\n");
		assertEquals(tasks, meCont.getTasksPerWorkstation());
		
		//Sets his active workstation to the car body post and then performs tasks.
		meCont.setAssemblyLine("Assembly Line 1");
		meCont.setWorkStation("Car Body Post");
		availableTasks = new ArrayList<String>();
		availableTasks.add("Install Body= Sedan: 1");
		availableTasks.add("Install Colour= Red: 2");
		assertEquals(availableTasks, meCont.getTasks());
		assertEquals(null, meCont.getTaskInformation("fout"));
		info = "Task description:\n   -Type of part needed: Body,\n   -Vehicle Part: Sedan\n";
		assertEquals(info, meCont.getTaskInformation("Install Body= Sedan"));
		meCont.doTask("Install Body= Sedan", 50);
		meCont.doTask("Install Colour= Red", 50);
		tasks = new ArrayList<String>();
		tasks.add("= Assembly Line 1 [OPERATIONAL] =");
		tasks.add(" Car Body Post:\n   -Install Body= Sedan: Completed\n   -Install Colour= Red: Completed\n");
		tasks.add(" Drive Train Post:\n   Inactive.\n");
		tasks.add(" Accessories Post:\n   Inactive.\n");
		tasks.add("= Assembly Line 2 [OPERATIONAL] =");
		tasks.add(" Car Body Post:\n   -Install Body= Sedan: Pending\n   -Install Colour= Red: Pending\n");
		tasks.add(" Drive Train Post:\n   Inactive.\n");
		tasks.add(" Accessories Post:\n   Inactive.\n");
		tasks.add("= Assembly Line 3 [OPERATIONAL] =");
		tasks.add(" Car Body Post:\n   -Install Body= Sedan: Pending\n   -Install Colour= Red: Pending\n");
		tasks.add(" Cargo Post:\n   Inactive.\n");
		tasks.add(" Drive Train Post:\n   Inactive.\n");
		tasks.add(" Accessories Post:\n   Inactive.\n");
		tasks.add(" Certification Post:\n   Inactive.\n");
		assertEquals(tasks, meCont.getTasksPerWorkstation());
		//perform body tasks on other assemblylines
		meCont.setAssemblyLine("Assembly Line 2");
		meCont.setWorkStation("Car Body Post");
		meCont.doTask("Install Body= Sedan", 55);
		meCont.doTask("Install Colour= Red", 55);
		meCont.setAssemblyLine("Assembly Line 3");
		meCont.setWorkStation("Car Body Post");
		meCont.doTask("Install Body= Sedan", 60);
		meCont.doTask("Install Colour= Red", 60);
		
		//Sets his active workstation and does the tasks.
		meCont.setAssemblyLine("Assembly Line 1");
		meCont.setWorkStation("Drive Train Post");
		availableTasks = new ArrayList<String>();
		availableTasks.add("Install Engine= Standard 2l v4: 1");
		availableTasks.add("Install Gearbox= 6 speed manual: 2");
		assertEquals(availableTasks, meCont.getTasks());
		meCont.doTask("Install Engine= Standard 2l v4", 50);
		meCont.doTask("Install Gearbox= 6 speed manual", 50);
		meCont.setWorkStation("Car Body Post");
		meCont.doTask("Install Body= Sedan", 50);
		meCont.doTask("Install Colour= Red", 50);
		//perform all tasks on second assembly line
		meCont.setAssemblyLine("Assembly Line 2");
		meCont.setWorkStation("Car Body Post");
		meCont.doTask("Install Body= Sedan", 55);
		meCont.doTask("Install Colour= Red", 55);
		meCont.setWorkStation("Drive Train Post");
		meCont.doTask("Install Engine= Standard 2l v4", 55);
		meCont.doTask("Install Gearbox= 6 speed manual", 55);
		//perform all tasks on the third assembly line
		meCont.setAssemblyLine("Assembly Line 3");
		meCont.setWorkStation("Car Body Post");
		meCont.doTask("Install Body= Sedan", 60);
		meCont.doTask("Install Colour= Red", 60);
		meCont.setWorkStation("Drive Train Post");
		meCont.doTask("Install Engine= Standard 2l v4", 60);
		meCont.doTask("Install Gearbox= 6 speed manual", 60);
		
		//Sets his active workstation to the accessories post and performs the tasks. 
		meCont.setAssemblyLine("Assembly Line 1");
		meCont.setWorkStation("Car Body Post");
		meCont.doTask("Install Body= Sedan", 50);
		meCont.doTask("Install Colour= Red", 50);
		meCont.setWorkStation("Drive Train Post");
		meCont.doTask("Install Engine= Standard 2l v4", 50);
		meCont.doTask("Install Gearbox= 6 speed manual", 50);
		meCont.setWorkStation("Accessories Post");
		availableTasks = new ArrayList<String>();
		availableTasks.add("Install Seats= Leather black: 1");
		availableTasks.add("Install Airco= Manual: 2");
		availableTasks.add("Install Wheels= Comfort: 3");
		assertEquals(availableTasks, meCont.getTasks());
		meCont.doTask("Install Seats= Leather black", 50);
		meCont.doTask("Install Airco= Manual", 50);
		meCont.doTask("Install Wheels= Comfort", 50);
		//perform all tasks on second assembly line
		meCont.setAssemblyLine("Assembly Line 2");
		meCont.setWorkStation("Car Body Post");
		meCont.doTask("Install Body= Sedan", 55);
		meCont.doTask("Install Colour= Red", 55);
		//task order in drive train post so no pending tasks to be performed there
		meCont.setWorkStation("Accessories Post");
		meCont.doTask("Install Seats= Leather black", 55);
		meCont.doTask("Install Airco= Manual", 55);
		meCont.doTask("Install Wheels= Comfort", 55);
		//perform all tasks on the third assembly line
		meCont.setAssemblyLine("Assembly Line 3");
		meCont.setWorkStation("Car Body Post");
		meCont.doTask("Install Body= Sedan", 60);
		meCont.doTask("Install Colour= Red", 60);
		meCont.setWorkStation("Drive Train Post");
		meCont.doTask("Install Engine= Standard 2l v4", 60);
		meCont.doTask("Install Gearbox= 6 speed manual", 60);
		
		////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////END MECHANICTEST PERFORM TASKS///////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////////////////////////////
		///////////////////////////START GARAGEHOLDERTEST COMPLETED ORDER///////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////
		
		//Checks his completed orders after an order should be completed.
		gaCont = (GarageHolderController) controller.logIn("gar");
		
		ArrayList<String> completed = new ArrayList<String>();
		completed.add("Completed on: 01-01-2014 08:30");
		completed.add("Completed on: 01-01-2014 08:40");
		assertEquals(completed, gaCont.getCompletedOrders());
		
		info = "   Specifications:   Car Model A; (Sedan, Red, Standard 2l v4, 6 speed manual, Leather black, Manual, Comfort, No Spoiler, No Toolstorage, No Cargo Protection, No Certification)\n   Start Time:       01-01-2014 06:00\n   End Time:         01-01-2014 08:30\n";
		//TODO: Joren wij snappen dit niet ?? D:
		//assertEquals(info, gaCont.getCompletedInfo(0));
		
		////////////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////END GARAGEHOLDERTEST COMPLETED ORDER////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////////////
	}

}
