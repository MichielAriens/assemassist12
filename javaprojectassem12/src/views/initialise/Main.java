package views.initialise;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Main {
	public static void main(String[] args) {
		//CLI cli = new CLI();
		System.out.println(getTime());
	}
    public static String getTime(){
		DateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime());
	}
}
