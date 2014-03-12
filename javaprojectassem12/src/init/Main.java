package init;


import logic.users.CarManufacturingCompany;
import views.UI;
import controllers.AssemAssistController;

public class Main {
	public static void main(String[] args) {
		CarManufacturingCompany company = new CarManufacturingCompany();
		AssemAssistController controller = new AssemAssistController(company);
		new UI(controller);
	}
}
