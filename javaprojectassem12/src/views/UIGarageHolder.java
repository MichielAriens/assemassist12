package views;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import controllers.GarageHolderController;
import logic.car.CarModel;
import logic.car.CarPart;
import logic.car.CarPartType;
import logic.users.GarageHolder;

public class UIGarageHolder {
	
	private BufferedReader reader;
	private BufferedWriter writer;
	private GarageHolderController ghController;
	public UIGarageHolder(BufferedReader reader, BufferedWriter writer) {
		this.reader = reader;
		this.writer = writer;
	}
	
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
