package views;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

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

			writer.write("Future status:\n\n");
			for(String s : maController.getFutureStatus()){
				writer.write(s + "\n");
			}
			writer.write("\n");
			writer.flush();
			
			int time = getTimeSpent();
			if(this.maController.moveAssemblyLine(time)){
				writer.write("Assembly line moved forward successfully.\n\n");
				writer.write("Current status:\n\n");
				for(String s : maController.getTasksPerWorkstation()){
					writer.write(s + "\n");
				}
				writer.flush();
				waitForCompletion("Press enter to finish. ");
				return;
			}
			else{
				writer.write("Assembly line could not be moved forward successfully.\n\n");
				writer.write("Unfinished tasks:\n\n");
				for(String s : maController.getUnfinishedTasks()){
					writer.write(s + "\n");
				}
				writer.flush();
				waitForCompletion("Press enter to finish.\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
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
