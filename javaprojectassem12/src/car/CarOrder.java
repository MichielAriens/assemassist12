package car;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import users.GarageHolder;


public class CarOrder {
	
	private final String DATE_FORMAT = "HH:mm dd/MM/yyyy";
		private boolean done;
		private String startTime;
		private String endTime;
		private String estimatedEndTime;
		private GarageHolder garageHolder;
		private DateFormat dateFormat;
		private CarSpecification carSpecification;
		
		public CarOrder(GarageHolder garageHolder, CarSpecification carSpecification){
			this.carSpecification=carSpecification;
			this.garageHolder=garageHolder;
			startTime = this.getTime();
			this.done = false;
			this.dateFormat= new SimpleDateFormat(DATE_FORMAT);
		}
		
		public boolean isDone() {
			return done;
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
		
		public void finished(){
			this.done = true;
			endTime=this.getTime();
			garageHolder.alertCompleted(this);
			
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
