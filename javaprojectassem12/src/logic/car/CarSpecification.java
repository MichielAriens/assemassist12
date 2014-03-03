package logic.car;

import java.security.InvalidParameterException;
import java.util.ArrayList;
<<<<<<< HEAD
import java.util.EnumMap;
=======
import java.util.List;
>>>>>>> e9618652ba060ffa50f392f6f5a7df9c054a7cc3

public class CarSpecification {
		private CarModel model;
		private ArrayList<CarPart> parts;
		
		public CarSpecification(CarModel model, ArrayList<CarPart> parts){
			this.model = model;
			this.parts = parts;
			if(!isValidSpecification())
				throw new InvalidParameterException();
		}
		
		private boolean isValidSpecification(){
			if(this.model == null)
				return false;
			if(!containsEveryPartTypeOnce()){
				return false;
			}
			return true;
		}
		
		private boolean containsEveryPartTypeOnce(){
			if(parts.size() > 7)
				return false;
			EnumMap<CarPartType, Boolean> flags = new EnumMap<CarPartType, Boolean>(CarPartType.class);
			for(int i = 0; i < 7; i++){
				CarPartType type = parts.get(i).type;
				if(!flags.get(type)){
					flags.put(type, true);
				}
				else{
					return false;
				}
			}
			return true;
		}

		public List<CarPart> getParts(){
			return parts;
		}
		
}
