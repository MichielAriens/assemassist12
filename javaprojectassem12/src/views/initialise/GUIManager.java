package views.initialise;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

import logic.users.Manager;

public class GUIManager {
	
	private BufferedReader reader;
	private BufferedWriter writer;
	private ManagerController maController;
	
	public GUIManager(BufferedReader reader, BufferedWriter writer) {
		this.reader = reader;
		this.writer = writer;
	}
	
	public void run(ManagerController maController){
		this.maController = maController;
		try {
			writer.write("Manager " + maController.getUserName()+ " has logged in.\n\n");
			writer.write("Current status:\n\n");
			for(String s : maController.getTasksPerWorkstation()){
				writer.write(s + "\n");
			}
			writer.write("\n");
			writer.flush();

			writer.write("Future status:\n\n");
			for(String s : maController.getFutureStatus()){
				writer.write(s + "\n");
			}
			writer.write("\n");
			writer.flush();
			
			int time = getTimeSpent();
			if(this.maController.moveAssemblyLine(time)){
				writer.write("Assembly line moved forward successfully.\n\n");
				writer.write("Current status:\n\n");
				for(String s : maController.getTasksPerWorkstation()){
					writer.write(s + "\n");
				}
				writer.flush();
				waitForCompletion("Press enter to finish. ");
				return;
			}
			else{
				writer.write("Assembly line could not be moved forward successfully.\n\n");
				writer.write("Unfinished tasks:\n\n");
				for(String s : maController.getUnfinishedTasks()){
					writer.write(s + "\n");
				}
				writer.flush();
				waitForCompletion("Press enter to finish.\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private int getTimeSpent(){
		try{
			writer.write("Enter the time (in minutes) spent during the current phase: ");
			writer.flush();
			while(true){
				String answer = reader.readLine();
				try{
					int time = Integer.parseInt(answer);
					if(time < 0 || time > 180){
						writer.write("\nInvalid input, try again. ");
						writer.flush();
						continue;
					}
					writer.write("\n");
					writer.flush();
					return time;
				} catch(NumberFormatException e){
					writer.write("\nInvalid input, try again. ");
					writer.flush();
					continue;
				}
			}
		} catch(IOException e){
			e.printStackTrace();
			return 0;
		}
	}
	
	private void waitForCompletion(String query){
		try{
			writer.write(query);
			writer.flush();
			reader.readLine();
		} catch(IOException e){
			e.printStackTrace();
		}
	}

}
