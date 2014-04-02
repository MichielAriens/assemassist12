package views;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

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
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
