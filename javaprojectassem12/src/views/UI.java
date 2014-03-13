package views;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import controllers.AssemAssistController;
import controllers.GarageHolderController;
import controllers.ManagerController;
import controllers.MechanicController;
import controllers.UserController;

/**
 * A command line interface class used to represent the main UI, which is used to let the users log in. 
 */
public class UI {
	
	/**
	 * The controller that offers this UI the methods to let a user log in.
	 */
	private AssemAssistController controller;
	
	/**
	 * A buffered reader that reads from the System's input stream.
	 */
	private BufferedReader reader;
	
	/**
	 * A buffered writer that writes to the System's output stream.
	 */
	private BufferedWriter writer;
	
	/**
	 * A command line interface that is used by garage holders to interact with the AssemAssist system.
	 */
	private UIGarageHolder garageHolderUI;
	
	/**
	 * A command line interface that is used by managers to interact with the AssemAssist system.
	 */
	private UIManager managerUI;
	
	/**
	 * A command line interface that is used by mechanics to interact with the AssemAssist system.
	 */
	private UIMechanic mechanicUI;
	
	/**
	 * Make a new UI with a given AssemAssistController.
	 * @param controller	The AssemAssistController that will be used by this UI to let users log into the 
	 * 						AssemAssist system.
	 */
	public UI(AssemAssistController controller, InputStream in, OutputStream out){
		this.controller = controller;
		this.reader= new BufferedReader(new InputStreamReader(in));
		this.writer= new BufferedWriter(new OutputStreamWriter(out));
		this.garageHolderUI = new UIGarageHolder(this.reader, this.writer);
		this.managerUI = new UIManager(this.reader, this.writer);
		this.mechanicUI = new UIMechanic(this.reader, this.writer);
		this.run();
	}
	
	
	/**
	 * A method that is called on startup of this UI, which asks the user for his username and presents
	 * the user with a CLI corresponding to the user's function in the company.
	 * After a user has finished his interactions with the system, this method will continue and another
	 * user can log in.
	 */
	private void run() {
		while(true){
			UserController userCont;
			while(true){
				userCont = login();
				if(userCont != null)
					break;
			}
			if(userCont instanceof GarageHolderController){
				garageHolderUI.run((GarageHolderController)userCont);
			}
			else if(userCont instanceof ManagerController){
				managerUI.run((ManagerController)userCont);
			}
			else if(userCont instanceof MechanicController){
				mechanicUI.run((MechanicController)userCont);
			}
			try {
				writer.write("\n\n");
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * A method that enables a log in attempt. It will prompt the user for his username and ask the controller
	 * to log this user in.
	 * @return	Returns null if the username that has been typed in by the user is not recognized as a valid
	 * 			username by the AssemAssist system. 
	 * 			Returns a UserController corresponding to the user's function within the company otherwise.
	 */
	private UserController login(){
		try{
			writer.write("====LOGIN====\nEnter username (Valid names: man, mech, gar): ");
			writer.flush();
			String userName = reader.readLine();
			return controller.logIn(userName);
		} catch(IOException e){
			return null;
		}
	}
}
