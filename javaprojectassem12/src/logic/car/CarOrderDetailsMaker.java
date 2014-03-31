package logic.car;

import java.util.ArrayList;
import java.util.List;

public class CarOrderDetailsMaker {
	
	private CarModel model;
	private ArrayList<CarPart> chosenParts;
	private ArrayList<CarPartType> chosenTypes;
	
	public CarOrderDetailsMaker(CarModel model){
		this.model = model;
		this.chosenParts = new ArrayList<CarPart>();
		this.chosenTypes = new ArrayList<CarPartType>();
	}
	
	public void addPart(CarPart part){
		if(isCompatiblePart(part)){
			chosenParts.add(part);
			chosenTypes.add(part.type);
		}
	}
	
	private boolean isCompatiblePart(CarPart part){
		//check if it is valid for the car model
		if(!model.validPart(part))
			return false;
		//the part can not be of a type that has already been chosen
		if(chosenTypes.contains(part.type))
			return false;
		//the parts that have no restrictions are valid
		if(part.type != CarPartType.Airco && part.type != CarPartType.Engine && part.type != CarPartType.Spoiler)
			return true;
		if(part.type == CarPartType.Airco){
			if(chosenParts.contains(CarPart.ENGINE_8) && part == CarPart.AIRCO_AUTO)
				return false;
		}
		if(part.type == CarPartType.Engine){
			if(chosenParts.contains(CarPart.BODY_SPORT) && part == CarPart.ENGINE_4)
				return false;
		}
		if(part.type == CarPartType.Spoiler){
			if(chosenParts.contains(CarPart.BODY_SPORT) && part == CarPart.SPOILER_NONE)
				return false;
		}
		return true;
	}
	
	//volgorde ligt vast: body, color, engine, gearbox, seats, airco, wheels, spoiler
	public List<CarPart> getAvailableParts(CarPartType type){
		ArrayList<CarPart> result = new ArrayList<CarPart>();
		for(CarPart part: model.possibleParts){
			if(part.type == type && isCompatiblePart(part))
				result.add(part);
		}
		return result;
	}
	
	public CarOrderDetails getDetails(){
		if(chosenParts.size() == 8){
			return new CarOrderDetails(model, chosenParts);
		}
		return null;
	}
}
