package views.initialise;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import logic.car.CarModel;
import logic.car.CarPart;
import logic.car.CarPartType;
import logic.users.GarageHolder;

public class GUIGarageHolder {
	
	private BufferedReader reader;
	private BufferedWriter writer;
	private GarageHolderController ghController;
	public GUIGarageHolder(BufferedReader reader, BufferedWriter writer) {
		this.reader = reader;
		this.writer = writer;
	}
	
	public void run(GarageHolderController ghController){
		this.ghController = ghController;
		try {
			writer.write("Garage holder " + ghController.getUserName()+ " has logged in.\n\n");
			writer.flush();
			printOrders("Pending Orders: ", ghController.getPendingOrders());
			printOrders("Completed Orders: ", ghController.getCompletedOrders());
			if(!promptNewOrder())
				return;
			orderingForm();
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

	private void orderingForm(){
		try{
			writer.write("Fill in the Car details:\n");
			String models = "Available models: ";
			for(CarModel mod : CarModel.values()){
				models += mod.toString() + "; ";
			}
			writer.write(models + "\n");
			writer.flush();
			String modelName = checkInput("Type the name of the desired car model: ", ghController.getModels());
			CarModel  model = CarModel.MODEL1;
			for(CarModel m : CarModel.values()){
				if(m.toString().equals(modelName)){
					model = m;
					break;
				}
			}
			ArrayList<String> parts = new ArrayList<String>();
			for(CarPartType partType : CarPartType.values()){
				String typeString = "Select " + partType.toString() + "-type: ";
				for(String part : ghController.getOptions(partType, model))
					typeString += part + "; ";
				writer.write(typeString+"\n");
				parts.add(checkInput("Type the name of the desired car part: ", ghController.getOptions(partType, model))); 
			}
			ghController.placeOrder(model, parts);
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private String checkInput(String query, ArrayList<String> answers){
		try{
			while(true){
				writer.write(query);
				writer.flush();
				String answer = reader.readLine();
				if(answers.contains(answer))
					return answer;
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
