package views.initialise;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import logic.users.GarageHolder;

public class GUIGarageHolder {
	
	private BufferedReader reader;
	private BufferedWriter writer;
	private GarageHolderController controller;
	
	public GUIGarageHolder(BufferedReader reader, BufferedWriter writer, GarageHolderController controller) {
		this.reader = reader;
		this.writer = writer;
		this.controller = controller;
	}
	
	public void run(GarageHolder garageholder){
		try {
			writer.write("Garage holder " + garageholder.getUserName()+ " has logged in.\n\n");
			writer.flush();
			printPendingOrders(garageholder.getPendingOrderStrings());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void printPendingOrders(ArrayList<String> orders){
		try {
			writer.write("Pending orders:\n");
			if(orders.size() == 0){
				writer.write("None. \n");
			}
			else{
				for(String order : orders){
					writer.write("- " + order + "; \n");
				}
			}
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
