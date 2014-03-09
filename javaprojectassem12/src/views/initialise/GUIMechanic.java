package views.initialise;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import logic.users.Mechanic;

public class GUIMechanic {
	
	private BufferedReader reader;
	private BufferedWriter writer;
	
	public GUIMechanic(BufferedReader reader, BufferedWriter writer) {
		this.reader = reader;
		this.writer = writer;
	}
	
	public void run(MechanicController meController){
		try {
			writer.write("Mechanic " + meController.getUserName()+ " has logged in.");
			writer.flush();
			while(true){
				printOrders("Pending Orders: ", ghController.getPendingOrders());
				printOrders("Completed Orders: ", ghController.getCompletedOrders());
				if(!promptYesOrNo("Do you want to place a new order? (y/n): "))
					return;
				ArrayList<String> spec = orderingForm();
				if(!promptYesOrNo("Do you want to submit this car order? (y/n): "))
					continue;
				ghController.placeOrder(spec);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
