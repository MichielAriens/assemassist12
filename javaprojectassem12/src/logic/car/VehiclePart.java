package logic.car;

/**
 * An enum that represents the possible car parts.
 */
public enum VehiclePart {
	BODY_SEDAN 			(VehiclePartType.Body, 		"Sedan"),
	BODY_BREAK			(VehiclePartType.Body, 		"Break"),
	BODY_SPORT			(VehiclePartType.Body,		"Sport"),
	BODY_PLATFORM		(VehiclePartType.Body,		"Platform"),
	BODY_CLOSED			(VehiclePartType.Body, 		"Closed"),
	COLOUR_RED			(VehiclePartType.Colour, 	"Red"),							
	COLOUR_BLUE			(VehiclePartType.Colour, 	"Blue"),
	COLOUR_BLACK		(VehiclePartType.Colour, 	"Black"),
	COLOUR_WHITE		(VehiclePartType.Colour, 	"White"),
	COLOUR_GREEN		(VehiclePartType.Colour, 	"Green"),
	COLOUR_YELLOW		(VehiclePartType.Colour, 	"Yellow"),
	ENGINE_4			(VehiclePartType.Engine, 	"Standard 2l v4"),
	ENGINE_6			(VehiclePartType.Engine, 	"Performance 2.5l v6"),
	ENGINE_8			(VehiclePartType.Engine,	"Ultra 3l v8"),
	ENGINE_TRUCKSTANDARD(VehiclePartType.Engine, 	"Truck Standard"),
	GEARBOX_6MANUAL		(VehiclePartType.Gearbox, 	"6 speed manual"),
	GEARBOX_5MANUAL		(VehiclePartType.Gearbox, 	"5 speed manual"),
	GEARBOX_5AUTO		(VehiclePartType.Gearbox, 	"5 speed automatic"),
	GEARBOX_8MANUAL		(VehiclePartType.Gearbox, 	"8 speed manual"),
	GEARBOX_TRUCKAUTO	(VehiclePartType.Gearbox, 	"Truck Automatic"),
	SEATS_LEATHER_BLACK	(VehiclePartType.Seats, 	"Leather black"),
	SEATS_LEATHER_WHITE	(VehiclePartType.Seats,		"Leather white"),
	SEATS_VINYL_GRAY	(VehiclePartType.Seats, 	"Vinyl grey"),
	SEATS_VINYL_BLACK	(VehiclePartType.Seats, 	"Vinyl black"),	
	AIRCO_MANUAL		(VehiclePartType.Airco,		"Manual"),	
	AIRCO_AUTO			(VehiclePartType.Airco,		"Automatic"),
	AIRCO_NONE			(VehiclePartType.Airco,		"No Airco"),	
	WHEELS_COMFORT		(VehiclePartType.Wheels,	"Comfort"),
	WHEELS_WINTER		(VehiclePartType.Wheels,	"Winter"), 
	WHEELS_SPORTS		(VehiclePartType.Wheels, 	"Sports"),
	WHEELS_TRUCKSTANDARD(VehiclePartType.Wheels,	"Truck standard"),
	WHEELS_HEAVY_DUTY	(VehiclePartType.Wheels, 	"Heavy-duty"),
	SPOILER_LOW			(VehiclePartType.Spoiler,	"Low"),
	SPOILER_NONE		(VehiclePartType.Spoiler,	"No Spoiler"),
	SPOILER_HIGH		(VehiclePartType.Spoiler,	"High");
	
	
	/**
	 * The car part type of this car part.
	 */
	public final VehiclePartType type;
	
	/**
	 * The full name of this car part.
	 */
	public final String fullName;
	
	/**
	 * Make a CarPart with given car part type and full name.
	 * @param type		The type of this car part.
	 * @param fullName	The full name of this car part.
	 */
	VehiclePart(VehiclePartType type, String fullName){
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
	public static VehiclePart getPartfromString(String name){
		VehiclePart part = null;
		for(VehiclePart p : VehiclePart.values()){
			if(p.toString().equals(name)){
				part = p;
				break;
			}
		}
		return part;
	}
}
