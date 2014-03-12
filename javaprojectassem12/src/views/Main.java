package views;

import controllers.AssemAssistController;
import logic.users.CarManufacturingCompany;

public class Main {
	public static void main(String[] args) {
		CarManufacturingCompany company = new CarManufacturingCompany();
		AssemAssistController controller = new AssemAssistController(company);
		new UI(controller);
	}
}
