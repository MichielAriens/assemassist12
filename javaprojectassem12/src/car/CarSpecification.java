package car;

import java.util.ArrayList;

public class CarSpecification {
		private CarModel model;
		private ArrayList<CarPart> parts;
		
		public CarSpecification(CarModel model, ArrayList<CarPart> parts){
			this.model = model;
			this.parts = parts;
		}
}
