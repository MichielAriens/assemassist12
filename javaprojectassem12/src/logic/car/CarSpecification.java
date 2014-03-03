package logic.car;

import java.util.ArrayList;
import java.util.List;

public class CarSpecification {
		private CarModel model;
		private ArrayList<CarPart> parts;
		
		public CarSpecification(CarModel model, ArrayList<CarPart> parts){
			this.model = model;
			this.parts = parts;
		}

		public List<CarPart> getParts(){
			return parts;
		}
		
}
