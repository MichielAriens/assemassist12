package views;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import logic.car.CarPart;
import logic.car.CarPartType;
import controllers.CustomsManagerController;

/**
 * A command line interface class used to represent the customs shop manager's UI, 
 * which is used by customs shop managers to place orders.
 */
public class UICustomsManager {
	/**
	 * A buffered reader that reads from the System's input stream.
	 */
	private BufferedReader reader;
	
	/**
	 * A buffered writer that writes to the System's output stream.
	 */
	private BufferedWriter writer;
	
	/**
	 * The controller that offers this UI the methods to let the current customs shop manager place orders.
	 */
	private CustomsManagerController cuController;
	
	/**
	 * Make a new customs shop manager UI with a given reader and writer.
	 * @param reader	The buffered reader made by the main UI, that reads from the System's input stream.
	 * @param writer	The buffered writer made by the main UI, that writes to the System's output stream.
	 */
	public UICustomsManager(BufferedReader reader, BufferedWriter writer) {
		this.reader = reader;
		this.writer = writer;
	}
	
	/**
	 * A method that is called by the main UI, when a customs shop manager has successfully logged in.
	 * @param cuController	The CustomsManagerController that offers this UI methods to place
	 * 						for the customs shop manager that is currently logged in.
	 */
	public void run(CustomsManagerController cuController){
		this.cuController = cuController;
		try {
			writer.write("Customs shop manager " + cuController.getUserName()+ " has logged in.\n\n");
			writer.flush();
			while(true){
				writer.write("==AVAILABLE TASKS==\n");
				writer.flush();
				writer.write("Choose the type of task you want to order:\n\n");
				String tasks = "Available tasks: ";
				for(String t : cuController.getAvailableTypes()){
					tasks += t + "; ";
				}
				writer.write("   " + tasks + "\n");
				writer.flush();
				String taskString = checkInput("   Type the number of the desired task type: ", cuController.getAvailableTypes());
				CarPartType tasktype = CarPartType.getTypefromString(taskString);
				
				writer.write("Choose the option of the task you want to order:\n\n");
				String options = "Available options: ";
				for(String o : cuController.getAvailableOptions(tasktype)){
					options += o + "; ";
				}
				writer.write("   " + options + "\n");
				writer.flush();
				String optionString = checkInput("   Type the number of the desired task option: ", cuController.getAvailableOptions(tasktype));
				CarPart taskoption = CarPart.getPartfromString(optionString);
				placeDeadline();
				if(!promptYesOrNo("\nDo you want to place this order? (y/n): "))
					continue;
				cuController.choosePart(taskoption);
				String info = cuController.placeOrder();
				writer.write("\nOrder placed,\n" + info + "\n\n");
				writer.flush();
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Prompts the user for a deadline.
	 * @throws IOException When IO fails.
	 */
	private void placeDeadline() throws IOException{
		while(true){
			writer.write("Type in the desired deadline in this format: 'dd-MM-yyyy HH:mm'\nAnswer: ");
			writer.flush();
			String answer = reader.readLine();
			if(cuController.chooseDeadLine(answer))
				return;
			writer.write("\nInvalid input, try again.\n");
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
				writer.write("   Invalid input, try again.\n");
			}
		}
		catch(IOException e){
			e.printStackTrace();
			return null;
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
}
