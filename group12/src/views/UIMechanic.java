package views;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import controllers.MechanicController;


public class UIMechanic {
	
	/**
	 * A buffered reader that reads from the System's input stream.
	 */
	private BufferedReader reader;
	
	/**
	 * A buffered writer that writes to the System's output stream.
	 */
	private BufferedWriter writer;
	
	/**
	 * The controller that offers this UI the methods to let the current mechanic perform tasks.
	 */
	private MechanicController meController;
	
	/**
	 * Make a new mechanic UI with a given reader and writer.
	 * @param reader	The buffered reader made by the main UI, that reads from the System's input stream.
	 * @param writer	The buffered writer made by the main UI, that writes to the System's output stream.
	 */
	public UIMechanic(BufferedReader reader, BufferedWriter writer) {
		this.reader = reader;
		this.writer = writer;
	}
	
	/**
	 * A method that is called by the main UI, when a mechanic has successfully logged in.
	 * @param meController	The MechanicController that offers this UI methods to check workstations and
	 * 						perform tasks.
	 */
	public void run(MechanicController meController){
		this.meController = meController;
		try {
			writer.write("Mechanic " + meController.getUserName()+ " has logged in.\n\n");
			String stat = "Available work stations: ";
			for(String station : meController.getWorkStations()){
				stat += station + "; ";
			}
			writer.write(stat + "\n");
			writer.flush();
			String answer = checkInput("Type the number of your current work station: ", this.meController.getWorkStations());
			this.meController.setWorkStation(answer);
			writer.flush();
			while(true){
				selectTask();
				if(!promptYesOrNo("Do you want to perform another task? (y/n): "))
					return;
			}
		} catch (IOException e) {
			e.printStackTrace();
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
	 * A method that provides the current mechanic with the available tasks at his current workstation,
	 * and lets the mechanic pick a task to perform if there are tasks available.
	 */
	private void selectTask(){
		try{
			if(meController.getTasks().size() == 0){
				writer.write("There are no available tasks at this workstation.\n\n");
				writer.flush();
				return;
			}
			String tasks = "Available tasks: ";
			for(String t : meController.getTasks()){
				tasks += t + "; ";
			}
			writer.write(tasks + "\n");
			writer.flush();
			String answer = checkInput("Type the number of your current task: ", this.meController.getTasks());
			writer.write("\n" + meController.getTaskInformation(answer) + "\n");
			waitForCompletion();
			this.meController.doTask(answer);
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	
	/**
	 * A method that prints out the given query and waits until the user presses enter to continue.
	 * @param query	The query that has to be printed out.
	 */
	private void waitForCompletion(){
		try{
			writer.write("Press enter when the task is done.\n");
			writer.flush();
			reader.readLine();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * A method that asks the user for input with a given query and then checks the user's answer with
	 * a list of valid answers, and keeps repeating this until the user has given a valid answer.
	 * @param query		The query that has to be printed out.
	 * @param answers	The list of valid answers.
	 * @return	The answer from the list that corresponds with the user's valid answer.
	 */
	private String checkInput(String query, ArrayList<String> answers){
		try{
			while(true){
				writer.write(query);
				writer.flush();
				String answer = reader.readLine();
				writer.write("\n");
				writer.flush();
				for(String s : answers){
					if(s.endsWith(answer))
						return s.substring(0, s.indexOf(":"));
				}
				writer.write("Invalid input, try again.\n");
			}
		}
		catch(IOException e){
			e.printStackTrace();
			return null;
		}
	}
	

}
