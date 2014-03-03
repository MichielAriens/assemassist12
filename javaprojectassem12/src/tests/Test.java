package tests;

import java.util.ArrayList;
import java.util.EnumMap;

import logic.car.CarPart;
import logic.car.CarPartType;

public class Test {

	public static void main(String[] args) {
		ArrayList<CarPart> carparts = new ArrayList<CarPart>();
		carparts.add(CarPart.BODY_BREAK);
		carparts.add(CarPart.COLOUR_BLACK);
		carparts.add(CarPart.SEATS_LEATHER_BLACK);
		carparts.add(CarPart.AIRCO_AUTO);
		carparts.add(CarPart.WHEELS_COMFORT);
		carparts.add(CarPart.ENGINE_4);
		carparts.add(CarPart.GEARBOX_5AUTO);
		if(carparts.size() > 7)
			System.out.println("false");
		EnumMap<CarPartType, Boolean> flags = new EnumMap<CarPartType, Boolean>(CarPartType.class);
		flags.put(CarPartType.Body, false);
		flags.put(CarPartType.Airco, false);
		flags.put(CarPartType.Colour, false);
		flags.put(CarPartType.Engine, false);
		flags.put(CarPartType.Gearbox, false);
		flags.put(CarPartType.Seats, false);
		flags.put(CarPartType.Wheels, false);
		
		for(int i = 0; i < 7; i++){
			CarPartType type = carparts.get(i).type;
			if(!flags.get(type)){
				flags.put(type, true);
			}
			else{
					System.out.println("false");
			}
		}
		System.out.println("true");
	}
}
