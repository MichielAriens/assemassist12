package views;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import controllers.AssemAssistController;
import controllers.GarageHolderController;
import controllers.ManagerController;
import controllers.MechanicController;
import controllers.UserController;
import logic.users.*;

/**
 * A command line interface class used to represent the main UI, which is used to let the users log in. 
 */
public class UI {
	
	/**
	 * The controller that offers this UI the methods to let a user log in.
	 */
	private AssemAssistController controller;
	private BufferedReader reader;
	private BufferedWriter writer;
	private UIGarageHolder garageHolderUI;
	private UIManager managerUI;
	private UIMechanic mechanicUI;
	
	public UI(AssemAssistController controller){
		this.controller = controller;
		this.controller.setGUI(this);
		this.reader= new BufferedReader(new InputStreamReader(System.in));
		this.writer= new BufferedWriter(new OutputStreamWriter(System.out));
		this.garageHolderUI = new UIGarageHolder(this.reader, this.writer);
		this.managerUI = new UIManager(this.reader, this.writer);
		this.mechanicUI = new UIMechanic(this.reader, this.writer);
		this.run();
	}
	
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
	
	private UserController login(){
		try{
			writer.write("====LOGIN====\nEnter username: ");
			writer.flush();
			String userName = reader.readLine();
			return controller.logIn(userName);
		} catch(IOException e){
			return null;
		}
	}

	class Communication{
		public void write(String input) throws IOException{
			writer.write(input);
			writer.flush();
		}
		public String readInput() throws IOException{
			return reader.readLine();
		}
	}

	public AssemAssistController getController() {
		
		return this.controller;
	}

}
