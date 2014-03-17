package init;

import logic.users.CarManufacturingCompany;
import views.UI;
import controllers.AssemAssistController;

/**
 * A class used to start up the AssemAssist system.
 */
public class Main {
	
	/**
	 * The main method that starts up the AssemAssist system.
	 * @param args Not used.
	 */
	public static void main(String[] args) {
		CarManufacturingCompany company = new CarManufacturingCompany();
		AssemAssistController controller = new AssemAssistController(company);
		new UI(controller, System.in, System.out);
	}
}
