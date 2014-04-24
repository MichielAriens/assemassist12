package views;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import controllers.ManagerController;

/**
 * A command line interface class used to represent the manager's UI, which is used by managers
 * to look up the current statistics and change scheduling strategies.
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
	 * The controller that offers this UI methods to check statistics and change scheduling strategies.
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
	 * @param maController	The ManagerController that offers this UI methods to check statistics and change
	 * 						scheduling strategies.
	 */
	public void run(ManagerController maController){
		this.maController = maController;
		try {
			writer.write("Manager " + maController.getUserName()+ " has logged in.\n\n");			
			while(true){				
				int answer = chooseAction("Select your action:\n   1: Check statistics\n   2: Select alternative scheduling algorithm\n   3: Leave overview\nAnswer: ", 3);
				if(answer == 1){
					writeStatistics();
				}
				else if(answer == 2){
					newSchedulingAlgorithm();
				}
				else if(answer == 3){
					return;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes the current statistics to the screen.
	 * @throws IOException	When IO fails.
	 */
	private void writeStatistics()throws IOException {
		writer.write("===STATISTICS===\n");
		writer.flush();
		writer.write(maController.getStatistics() + "\n");
		writer.flush();
	}
	
	/**
	 * Lets the user choose an alternate scheduling algorithm.
	 * @throws IOException	When IO fails.
	 */
	private void newSchedulingAlgorithm() throws IOException {
		writer.write("===ALTERNATE SCHEDULING MECHANISM===\n");
		writer.flush();
		ArrayList<String> strategies = maController.getStrategies();
		writer.write("Current algorithm:\n   " + strategies.get(0) + "\n");
		
		String query = "Select your algorithm:\n";
		for(int i = 1; i < strategies.size(); i++){
			query += "   " + (i) + ": " + strategies.get(i) + "\n";
		}
		query += "   " + (strategies.size()) + ": Cancel\nAnswer: ";
		int algorithm = chooseAction(query, strategies.size());
		int indexOfCancel = strategies.size(); 
		if(algorithm != indexOfCancel){
			String chosenStrategy = strategies.get(algorithm);
			if(chosenStrategy.equals("FIFO")){
				chooseFIFO();
			}
			else if(chosenStrategy.equals("Specification Batch")){
				chooseSpecificationBatch();
			}
		}
	}
	
	/**
	 * Changes the strategy to FIFO and notifies the user of what happened.
	 */
	private void chooseFIFO() {
		if(maController.changeStrategyToFIFO()){
			waitForCompletion("Chosen strategy has been applied. Press enter to continue.\n");
		}
		else{
			waitForCompletion("Chosen strategy is the current strategy, nothing has changed. Press enter to continue.\n");
		}
	}
	
	/**
	 * Lets the user choose which set of options to use for the batch specifications strategy,
	 * and applies this strategy if possible.
	 */
	private void chooseSpecificationBatch() {
		String query;
		ArrayList<String> listCarOptions = maController.getBatchList();
		if(listCarOptions.size() < 1){
			waitForCompletion("No available sets of car options. Press enter to continue.\n");
		}
		else{
			query = "===Select desired set===\n";
			for(int i = 0; i < listCarOptions.size(); i++){
				query += listCarOptions.get(i);
			}
			query += "   " + (listCarOptions.size()+1) + ": Cancel\n";
			int carOption = chooseAction(query, listCarOptions.size()+1);
			if(carOption != listCarOptions.size()+1){
				maController.changeStrategyToBatchProcessing(carOption-1);
				waitForCompletion("Chosen strategy has been applied. Press enter to continue.\n");
			}
		}
	}
	
	/**
	 * Lets the user choose an option from a given query.
	 * @param query	The query that has to be printed out to the user.
	 * @param max	The greatest acceptable answer.
	 * @return	-1	If an IO exception occurs.
	 * 			The users answer otherwise.
	 */
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
