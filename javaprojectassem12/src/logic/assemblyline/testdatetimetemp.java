package logic.assemblyline;

import org.joda.time.DateTime;

public class testdatetimetemp {
	public static void main(String[] args) {
		DateTime derp = new DateTime(2014, 1, 1, 23, 8);
//		DateTime derp1 = new DateTime(derp);
//		derp1 = derp1.plusMinutes(5);
//		System.out.println(derp.getDayOfMonth() + " "+ derp.getHourOfDay());
//		derp = derp.plusHours(1);
//		System.out.println(derp.getDayOfMonth()+ " "+ derp.getHourOfDay());
		System.out.println(derp.getMinuteOfDay());
	}
}
