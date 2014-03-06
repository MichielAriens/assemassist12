package views.initialise;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import logic.users.Manager;

public class GUIManager {
	
	private BufferedReader reader;
	private BufferedWriter writer;
	
	public GUIManager(BufferedReader reader, BufferedWriter writer) {
		this.reader = reader;
		this.writer = writer;
	}
	
	public void run(Manager manager){
		try {
			writer.write("Manager " + manager.getUserName()+ " has logged in.");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
