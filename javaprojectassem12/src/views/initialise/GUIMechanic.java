package views.initialise;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import logic.users.Mechanic;

public class GUIMechanic {
	
	private BufferedReader reader;
	private BufferedWriter writer;
	
	public GUIMechanic(BufferedReader reader, BufferedWriter writer) {
		this.reader = reader;
		this.writer = writer;
	}
	
	public void run(Mechanic mechanic){
		try {
			writer.write("Mechanic " + mechanic.getUserName()+ " has logged in.");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
