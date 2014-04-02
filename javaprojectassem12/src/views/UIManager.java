package views;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import controllers.ManagerController;

/**
 * A command line interface class used to represent the manager's UI, which is used by managers
 * to look up the status of the assembly line and advance it if possible.
 */
public class UIManager {
	
	/**
	 * A buffered reader that reads from the System's input stream.
	 */
	private BufferedReader reader;
	
	/**
	 * A buffered writer that writes to the System's output stream.
	 */
	private BufferedWriter writer;
	
	/**
	 * The controller that offers this UI the methods to let the current manager check and advance the assembly line.
	 */
	private ManagerController maController;
	
	/**
	 * Make a new manager UI with a given reader and writer.
	 * @param reader	The buffered reader made by the main UI, that reads from the System's input stream.
	 * @param writer	The buffered writer made by the main UI, that writes to the System's output stream.
	 */
	public UIManager(BufferedReader reader, BufferedWriter writer) {
		this.reader = reader;
		this.writer = writer;
	}
	
	/**
	 * A method that is called by the main UI, when a manager has successfully logged in.
	 * @param maController	The ManagerController that offers this UI methods to check and advance the assembly line.
	 */
	public void run(ManagerController maController){
		this.maController = maController;
		try {
			writer.write("Manager " + maController.getUserName()+ " has logged in.\n\n");
			writer.write("Current status:\n\n");
			for(String s : maController.getTasksPerWorkstation()){
				writer.write(s + "\n");
			}
			writer.write("\n");
			writer.flush();
			
			while(true){				
				int answer = chooseAction("Select your action:\n   1: Check statistics\n   2: Select alternative scheduling algorithm\nAnswer: ", 2);
				if(answer == 1){
					writeStatistics();
				}
				else if(answer == 2){
					if(!promptYesOrNo("Do you want to select a new scheduling algorithm? (y/n): "))
						return;
					newSchedulingAlgorithm();
				}
			}

//			writer.write("Future status:\n\n");
//			for(String s : maController.getFutureStatus()){
//				writer.write(s + "\n");
//			}
//			writer.write("\n");
//			writer.flush();
//			
//			int time = getTimeSpent();
//			if(this.maController.moveAssemblyLine(time)){
//				writer.write("Assembly line moved forward successfully.\n\n");
//				writer.write("Current status:\n\n");
//				for(String s : maController.getTasksPerWorkstation()){
//					writer.write(s + "\n");
//				}
//				writer.flush();
//				waitForCompletion("Press enter to finish. ");
//				return;
//			}
//			else{
//				writer.write("Assembly line could not be moved forward successfully.\n\n");
//				writer.write("Unfinished tasks:\n\n");
//				for(String s : maController.getUnfinishedTasks()){
//					writer.write(s + "\n");
//				}
//				writer.flush();
//				waitForCompletion("Press enter to finish.\n");
//			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeStatistics()throws IOException {
		writer.write("===STATISTICS===\n");
		writer.flush();
		writer.write(maController.getStatistics());
		writer.flush();
	}
	
	private void newSchedulingAlgorithm() throws IOException {
		writer.write("ALTERNATIVE SCHEDULING\n");
		writer.flush();
		writer.write("Current algorithm:\n   " + maController.getCurrentStrategy() + "\n");
		ArrayList<String> strategies = maController.getAvailableStrategies();
		String query = "Select your algorithm:\n";
		for(int i = 0; i < strategies.size(); i++){
			query += "   " + (i+1) + ": " + strategies.get(i) + "\n";
		}
		query += "   " + (strategies.size()+1) + ": Cancel\nAnswer: ";
		int algorithm = chooseAction(query, strategies.size()+1);
		String chosenStrategy = strategies.get(algorithm-1);
		if(chosenStrategy.equals("FIFO")){
			//TODO FIFO: select FIFO queue algorithm
		}
		else if(chosenStrategy.equals("Specification Batch")){
			ArrayList<String> listCarOptions = maController.getCarOptionsBatchProcessing();
			if(listCarOptions.isEmpty()){
				writer.write("No available sets of car options!\n");
				writer.flush();
				waitForCompletion("Press enter to continue.");
			}
			else{
				writer.write("Available sets of car options:\n");
				query = "Select desired set:\n";
				for(int i = 0; i < listCarOptions.size(); i++){
					query += "Set " + (i+1) + ":\n"+listCarOptions.get(i);
				}
				int carOption = chooseAction(query, strategies.size()+1);
				//TODO batch processing opstarten met de gekozen car options
				
			}
		}
		else if(algorithm == 3){
			//cancel == do nothing
		}
	}
	
	private int chooseAction(String query, int max){
		try{
			while(true){
				writer.write(query);
				writer.flush();
				String answer = reader.readLine();
				writer.write("\n");
				writer.flush();
				try{
					int result = Integer.parseInt(answer);
					if(result > 0 && result <= max)
						return result;
				}
				catch(NumberFormatException e){};
			}
		}
		catch(IOException e){
			e.printStackTrace();
			return -1;
		}
	}
	
	/**
	 * A method that prompts the user with the given query and waits for a 'y' or 'n' answer from the user.
	 * @param query	The query that has to be printed out.
	 * @return		Returns true if the user has responded with a 'y' meaning yes.
	 * 				Returns false if the user has responded with a 'n' meaning no.
	 */
	private boolean promptYesOrNo(String query){
		try{
			while(true){
				writer.write(query);
				writer.flush();
				String answer = reader.readLine();
				writer.write("\n");
				writer.flush();
				if(answer.equals("y"))
					return true;
				if(answer.equals("n"))
					return false;
			}
		}
		catch(IOException e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * A method that prompts the user for the time spent in minutes in the current phase.
	 * @return	An int that holds the time in minutes spent in the current phase according to the current manager.
	 */
	private int getTimeSpent(){
		try{
			writer.write("Enter the time (in minutes) spent during the current phase: ");
			writer.flush();
			while(true){
				String answer = reader.readLine();
				try{
					int time = Integer.parseInt(answer);
					if(time < 0 || time > 180){
						writer.write("\nInvalid input, try again. ");
						writer.flush();
						continue;
					}
					writer.write("\n");
					writer.flush();
					return time;
				} catch(NumberFormatException e){
					writer.write("\nInvalid input, try again. ");
					writer.flush();
					continue;
				}
			}
		} catch(IOException e){
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * A method that prints out the given query and waits until the user presses enter to continue.
	 * @param query	The query that has to be printed out.
	 */
	private void waitForCompletion(String query){
		try{
			writer.write(query);
			writer.flush();
			reader.readLine();
		} catch(IOException e){
			e.printStackTrace();
		}
	}

}
