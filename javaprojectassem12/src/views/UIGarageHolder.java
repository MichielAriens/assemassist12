package views;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import controllers.GarageHolderController;
import logic.car.CarModel;
import logic.car.CarPartType;

/**
 * A command line interface class used to represent the garage holder UI, which is used by garage holders
 * to place orders and look up the status of placed orders.
 */
public class UIGarageHolder {
	
	/**
	 * A buffered reader that reads from the System's input stream.
	 */
	private BufferedReader reader;
	
	/**
	 * A buffered writer that writes to the System's output stream.
	 */
	private BufferedWriter writer;
	
	/**
	 * The controller that offers this UI the methods to let the current garage holder place and review orders.
	 */
	private GarageHolderController ghController;
	
	/**
	 * Make a new garage holder UI with a given reader and writer.
	 * @param reader	The buffered reader made by the main UI, that reads from the System's input stream.
	 * @param writer	The buffered writer made by the main UI, that writes to the System's output stream.
	 */
	public UIGarageHolder(BufferedReader reader, BufferedWriter writer) {
		this.reader = reader;
		this.writer = writer;
	}
	
	/**
	 * A method that is called by the main UI, when a garage holder has successfully logged in.
	 * @param ghController	The GarageHolderController that offers this UI methods to place and review orders
	 * 						for the garage holder that is currently logged in.
	 */
	public void run(GarageHolderController ghController){
		this.ghController = ghController;
		try {
			writer.write("Garage holder " + ghController.getUserName()+ " has logged in.\n\n");
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
	
	/**
	 * A method that prompts the user with the given query and waits for a 'y' or 'n' answer from the user.
	 * @param query	The query that has to be printed out.
	 * @return		Returns true if the user has responded with a 'y' meaning yes.
	 * 				Returns false if the user has responded with a 'n' meaning no.
	 */
	private boolean promptYesOrNo(String query){
		try{
			while(true){
				writer.write(query);
				writer.flush();
				String answer = reader.readLine();
				writer.write("\n");
				writer.flush();
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
	
	/**
	 * A method that creates
	 * @return
	 */
	private ArrayList<String> orderingForm(){
		ArrayList<String> spec = new ArrayList<String>();
		try{
			writer.write("Fill in the Car details:\n\n");
			String models = "Available models: ";
			for(String mod : ghController.getModels()){
				models += mod + "; ";
			}
			writer.write(models + "\n");
			writer.flush();
			spec.add(checkInput("Type the number of the desired car model: ", ghController.getModels()));
			
			CarModel  model = CarModel.getModelFromString(spec.get(0));
			for(CarPartType partType : CarPartType.values()){
				String typeString = "Select " + partType.toString() + "-type: ";
				for(String part : ghController.getOptions(partType, model))
					typeString += part + "; ";
				writer.write(typeString+"\n");
				spec.add(checkInput("Type the number of the desired car part: ", ghController.getOptions(partType, model))); 
			}
		} catch(IOException e){
			e.printStackTrace();
		}
		return spec;
	}
	
	private String checkInput(String query, ArrayList<String> answers){
		try{
			while(true){
				writer.write(query);
				writer.flush();
				String answer = reader.readLine();
				writer.write("\n");
				writer.flush();
				for(String s : answers){
					if(s.endsWith(answer))
						return s.substring(0, s.indexOf(":"));
				}
				writer.write("Invalid input, try again.\n");
			}
		}
		catch(IOException e){
			e.printStackTrace();
			return null;
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
