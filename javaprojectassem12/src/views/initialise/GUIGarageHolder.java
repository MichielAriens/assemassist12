package views.initialise;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import logic.users.GarageHolder;

public class GUIGarageHolder {
	
	private BufferedReader reader;
	private BufferedWriter writer;
	public GUIGarageHolder(BufferedReader reader, BufferedWriter writer) {
		this.reader = reader;
		this.writer = writer;
	}
	
	public void run(GarageHolderController ghController){
		try {
			writer.write("Garage holder " + ghController.getUserName()+ " has logged in.\n\n");
			writer.flush();
			printOrders("Pending Orders: ", ghController.getPendingOrders());
			printOrders("Completed Orders: ", ghController.getCompletedOrders());
			if(!promptNewOrder())
				return;
			writer.write("new car order placed");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean promptNewOrder(){
		try{
			while(true){
				writer.write("Do you want to place a new order? (y/n): ");
				writer.flush();
				String answer = reader.readLine();
				if(answer.equals("y"))
					return true;
				if(answer.equals("n"))
					return false;
			}
		}
		catch(IOException e){
			e.printStackTrace();
			return false;
		}
	}
	
	private void printOrders(String title, ArrayList<String> orders){
		try {
			writer.write(title + "\n");
			if(orders.size() == 0){
				writer.write("None. \n");
			}
			else{
				for(String order : orders){
					writer.write("- " + order + "; \n");
				}
			}
			writer.write("\n");
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
