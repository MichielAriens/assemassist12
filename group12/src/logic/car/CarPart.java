package logic.car;

/**
 * An enum that represents the possible car parts.
 */
public enum CarPart {
	BODY_SEDAN 			(CarPartType.Body, 		"Sedan"),
	BODY_BREAK			(CarPartType.Body, 		"Break"),
	COLOUR_RED			(CarPartType.Colour, 	"Red"),							
	COLOUR_BLUE			(CarPartType.Colour, 	"Blue"),
	COLOUR_BLACK		(CarPartType.Colour, 	"Black"),
	COLOUR_WHITE		(CarPartType.Colour, 	"White"),
	ENGINE_4			(CarPartType.Engine, 	"Standard 2l 4 cilinders"),
	ENGINE_6			(CarPartType.Engine, 	"performance 2.5l 6 cilinders"),
	GEARBOX_6MANUAL		(CarPartType.Gearbox, 	"6 speed manual"),
	GEARBOX_5AUTO		(CarPartType.Gearbox, 	"5 speed automatic"),
	SEATS_LEATHER_BLACK	(CarPartType.Seats, 	"leather black"),
	SEATS_LEATHER_WHITE	(CarPartType.Seats,		"leather white"),
	SEATS_VINYL_GRAY	(CarPartType.Seats, 	"vinyl grey"),
	AIRCO_MANUAL		(CarPartType.Airco,		"manual"),	
	AIRCO_AUTO			(CarPartType.Airco,		"automatic climate control"),
	WHEELS_COMFORT		(CarPartType.Wheels,	"comfort"), 
	WHEELS_SPORTS		(CarPartType.Wheels, 	"sports (low profile)");
	
	
	/**
	 * The car part type of this car part.
	 */
	public final CarPartType type;
	
	/**
	 * The full name of this car part.
	 */
	public final String fullName;
	
	/**
	 * Make a CarPart with given car part type and full name.
	 * @param type		The type of this car part.
	 * @param fullName	The full name of this car part.
	 */
	CarPart(CarPartType type, String fullName){
		this.type = type;
		this.fullName = fullName;
	}
	
	/**
	 * Returns a string representation of this car part.
	 */
	@Override
	public String toString(){
		return this.fullName;
	}
	
	/**
	 * Returns the CarPart that corresponds with a given name.
	 * @param name	The name of the car part that has to be searched.
	 * @return	Null if the given name does not correspond with the full name of any car part.
	 * 			The car part whose full name corresponds with the given name otherwise.
	 */
	public static CarPart getPartfromString(String name){
		CarPart part = null;
		for(CarPart p : CarPart.values()){
			if(p.toString().equals(name)){
				part = p;
				break;
			}
		}
		return part;
	}

}
