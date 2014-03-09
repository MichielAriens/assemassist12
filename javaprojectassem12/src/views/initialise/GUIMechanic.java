package views.initialise;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

import logic.users.Mechanic;

public class GUIMechanic {
	
	private BufferedReader reader;
	private BufferedWriter writer;
	private MechanicController meController;
	
	public GUIMechanic(BufferedReader reader, BufferedWriter writer) {
		this.reader = reader;
		this.writer = writer;
	}
	
	public void run(MechanicController meController){
		this.meController = meController;
		try {
			writer.write("Mechanic " + meController.getUserName()+ " has logged in.\n\n");
			String stat = "Available work stations: ";
			for(String station : meController.getWorkStations()){
				stat += station + "; ";
			}
			writer.write(stat + "\n");
			writer.flush();
			String answer = checkInput("Type the number of your current work station: ", this.meController.getWorkStations());
			this.meController.setWorkStation(answer);
			writer.write("\n");
			writer.flush();
			while(true){
				selectTask();
				if(!promptYesOrNo("Do you want to perform another taks? (y/n): "))
					return;
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
	
	private void selectTask(){
		try{
			String tasks = "Available tasks: ";
			for(String t : meController.getTasks()){
				tasks += t + "; ";
			}
			writer.write(tasks + "\n");
			writer.flush();
			String answer = checkInput("Type the number of your current task: ", this.meController.getTasks());
			writer.write(meController.getTaskInformation(answer));
			waitForCompletion();
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private void waitForCompletion(){
		try{
			writer.write("Press enter when the task is done.");
			writer.flush();
			reader.readLine();
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
	

}
