package logic.car;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import logic.users.GarageHolder;
import logic.workstation.Task;


public class CarOrder {
	
	private final String DATE_FORMAT = "HH:mm dd/MM/yyyy";
		private String startTime;
		private String endTime;
		private String estimatedEndTime;
		private GarageHolder garageHolder;
		private DateFormat dateFormat;
		private CarSpecification carSpecification;
		private List<Task> tasks = new ArrayList<Task>();
		
		public CarOrder(GarageHolder garageHolder, CarSpecification carSpecification){
			this.carSpecification=carSpecification;
			this.garageHolder=garageHolder;
			startTime = this.getTime();
			this.dateFormat= new SimpleDateFormat(DATE_FORMAT);
		}
		
		
		/**
		 * Build a list of tasks to be done. For use on the production line.
		 */
		private void buildTasks() {
			for(CarPart part : this.getCarSpecification().getParts()){
				this.tasks.add(new Task(part));
			}
		}

		
		public List<Task> getTasks(){
			return this.tasks;
		}

		
		/**
		 * 
		 * @return
		 */
		public boolean done(){
			boolean retVal = true;
			for(Task task : this.tasks){
				if(!task.isComplete()){
					retVal = false;
					break;
				}
			}
			return retVal;
		}
		

		public String getStartTime() {
			return startTime;
		}

		public String getEndTime() {
			return endTime;
		}

		public String getEstimatedEndTime() {
			return estimatedEndTime;
		}

		public CarSpecification getCarSpecification() {
			return carSpecification;
		}

		private String getTime(){
			Calendar cal = Calendar.getInstance();
			return dateFormat.format(cal.getTime());
		}
		
		public boolean setEstimatedEndTime(String time){
			if(this.isValidTime(time)){
				this.estimatedEndTime=time;
				return true;
			}
			return false;
		}

		private boolean isValidTime(String time) {
			try{
				dateFormat.parse(time);
				return true;
			}catch(Exception e){
				return false;
			}
		}
}

