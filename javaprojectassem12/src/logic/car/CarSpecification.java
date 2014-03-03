package logic.car;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.EnumMap;

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
