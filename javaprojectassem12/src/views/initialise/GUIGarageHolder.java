package views.initialise;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import logic.users.GarageHolder;

public class GUIGarageHolder {
	
	private BufferedReader reader;
	private BufferedWriter writer;
	
	public GUIGarageHolder(BufferedReader reader, BufferedWriter writer) {
		this.reader = reader;
		this.writer = writer;
	}
	
	public void run(GarageHolder garageholder){
		try {
			writer.write("Garage holder " + garageholder.getUserName()+ " has logged in.");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
