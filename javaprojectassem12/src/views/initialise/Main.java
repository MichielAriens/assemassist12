package views.initialise;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import logic.users.CarManufacturingCompany;

public class Main {
	public static void main(String[] args) {
		CarManufacturingCompany company = new CarManufacturingCompany();
		AssemAssistController controller = new AssemAssistController(company);
		GUI gui = new GUI(controller);
		
		
		
	}
    public static String getTime(){
		DateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}
}
