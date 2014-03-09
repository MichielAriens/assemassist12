package views.initialise;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import logic.users.Manager;

public class GUIManager {
	
	private BufferedReader reader;
	private BufferedWriter writer;
	private ManagerController maController;
	
	public GUIManager(BufferedReader reader, BufferedWriter writer) {
		this.reader = reader;
		this.writer = writer;
	}
	
	public void run(ManagerController maController){
		this.maController = maController;
		try {
			writer.write("Manager " + maController.getUserName()+ " has logged in.\n\n");
			writer.write("Current status:\n\n");
			for(String s : maController.getTasksPerWorkstation()){
				writer.write(s + "\n");
			}
			writer.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
