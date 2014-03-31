package logic.car;

/**
 * An enum that represents the possible car parts.
 */
public enum CarPart {
	BODY_SEDAN 			(CarPartType.Body, 		"Sedan"),
	BODY_BREAK			(CarPartType.Body, 		"Break"),
	BODY_SPORT			(CarPartType.Body,		"Sport"),
	COLOUR_RED			(CarPartType.Colour, 	"Red"),							
	COLOUR_BLUE			(CarPartType.Colour, 	"Blue"),
	COLOUR_BLACK		(CarPartType.Colour, 	"Black"),
	COLOUR_WHITE		(CarPartType.Colour, 	"White"),
	COLOUR_GREEN		(CarPartType.Colour, 	"Green"),
	COLOUR_YELLOW		(CarPartType.Colour, 	"Yellow"),
	ENGINE_4			(CarPartType.Engine, 	"Standard 2l v4"),
	ENGINE_6			(CarPartType.Engine, 	"Performance 2.5l v6"),
	ENGINE_8			(CarPartType.Engine,	"Ultra 3l v8"),	
	GEARBOX_6MANUAL		(CarPartType.Gearbox, 	"6 speed manual"),
	GEARBOX_5MANUAL		(CarPartType.Gearbox, 	"5 speed manual"),
	GEARBOX_5AUTO		(CarPartType.Gearbox, 	"5 speed automatic"),
	SEATS_LEATHER_BLACK	(CarPartType.Seats, 	"Leather black"),
	SEATS_LEATHER_WHITE	(CarPartType.Seats,		"Leather white"),
	SEATS_VINYL_GRAY	(CarPartType.Seats, 	"Vinyl grey"),
	AIRCO_MANUAL		(CarPartType.Airco,		"Manual"),	
	AIRCO_AUTO			(CarPartType.Airco,		"Automatic"),
	AIRCO_NONE			(CarPartType.Airco,		"None"),	
	WHEELS_COMFORT		(CarPartType.Wheels,	"Comfort"),
	WHEELS_WINTER		(CarPartType.Wheels,	"Winter"), 
	WHEELS_SPORTS		(CarPartType.Wheels, 	"Sports"),
	SPOILER_LOW			(CarPartType.Spoiler,	"Low"),
	SPOILER_NONE		(CarPartType.Spoiler,	"None"),
	SPOILER_HIGH		(CarPartType.Spoiler,	"High");
	
	
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
