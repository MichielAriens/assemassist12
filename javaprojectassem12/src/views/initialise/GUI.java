package views.initialise;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import logic.users.*;

public class GUI {
	private AssemAssistController controller;
	private BufferedReader reader;
	private BufferedWriter writer;
	private GUIGarageHolder garageHolderGui;
	private GUIManager managerGui;
	private GUIMechanic mechanicGui;
	
	public GUI(AssemAssistController controller){
		this.controller = controller;
		this.controller.setGUI(this);
		this.reader= new BufferedReader(new InputStreamReader(System.in));
		this.writer= new BufferedWriter(new OutputStreamWriter(System.out));
		this.garageHolderGui = new GUIGarageHolder(this.reader, this.writer);
		this.managerGui = new GUIManager(this.reader, this.writer);
		this.mechanicGui = new GUIMechanic(this.reader, this.writer);
		this.run();
	}
	
	private void run() {
		UserController userCont;
		while(true){
			userCont = login();
			if(userCont != null)
				break;
		}
		if(userCont instanceof GarageHolderController){
			garageHolderGui.run((GarageHolderController)userCont);
		}
		else if(userCont instanceof ManagerController){
			managerGui.run((ManagerController)userCont);
		}
		else if(userCont instanceof MechanicController){
			mechanicGui.run((MechanicController)userCont);
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
