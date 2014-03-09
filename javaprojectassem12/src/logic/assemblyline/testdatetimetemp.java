package logic.assemblyline;

import org.joda.time.DateTime;

public class testdatetimetemp {
	public static void main(String[] args) {
		DateTime derp = new DateTime(2014, 1, 1, 6, 0);
		DateTime derp1 = new DateTime(derp);
		derp1 = derp1.plusMinutes(5);
		System.out.println(derp.getMinuteOfDay());
		System.out.println(derp1.getMinuteOfDay());
	}
}
