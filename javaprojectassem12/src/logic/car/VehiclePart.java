package logic.car;

/**
 * An enum that represents the possible car parts.
 */
public enum VehiclePart {
	BODY_SEDAN 				(VehiclePartType.Body, 				"Sedan",						false),
	BODY_BREAK				(VehiclePartType.Body, 				"Break",						false),
	BODY_SPORT				(VehiclePartType.Body,				"Sport",						false),
	BODY_PLATFORM			(VehiclePartType.Body,				"Platform",						false),
	BODY_CLOSED				(VehiclePartType.Body, 				"Closed",						false),
	COLOUR_RED				(VehiclePartType.Colour, 			"Red",							false),							
	COLOUR_BLUE				(VehiclePartType.Colour, 			"Blue",							false),
	COLOUR_BLACK			(VehiclePartType.Colour, 			"Black",						false),
	COLOUR_WHITE			(VehiclePartType.Colour, 			"White", 						false),
	COLOUR_GREEN			(VehiclePartType.Colour, 			"Green",						false),
	COLOUR_YELLOW			(VehiclePartType.Colour, 			"Yellow",						false),
	ENGINE_4				(VehiclePartType.Engine, 			"Standard 2l v4",				false),
	ENGINE_6				(VehiclePartType.Engine, 			"Performance 2.5l v6",			false),
	ENGINE_8				(VehiclePartType.Engine,			"Ultra 3l v8",					false),
	ENGINE_TRUCKSTANDARD	(VehiclePartType.Engine, 			"Truck Standard",				false),
	ENGINE_HYBRID			(VehiclePartType.Engine, 			"Hybrid",						false),
	GEARBOX_6MANUAL			(VehiclePartType.Gearbox, 			"6 speed manual",				false),
	GEARBOX_5MANUAL			(VehiclePartType.Gearbox, 			"5 speed manual",				false),
	GEARBOX_5AUTO			(VehiclePartType.Gearbox, 			"5 speed automatic",			false),
	GEARBOX_8MANUAL			(VehiclePartType.Gearbox, 			"8 speed manual",				false),
	GEARBOX_TRUCKAUTO		(VehiclePartType.Gearbox, 			"Truck Automatic",				false),
	SEATS_LEATHER_BLACK		(VehiclePartType.Seats, 			"Leather black",				false),
	SEATS_LEATHER_WHITE		(VehiclePartType.Seats,				"Leather white",				false),
	SEATS_VINYL_GRAY		(VehiclePartType.Seats, 			"Vinyl grey",					false),
	SEATS_VINYL_BLACK		(VehiclePartType.Seats, 			"Vinyl black",					false),	
	AIRCO_MANUAL			(VehiclePartType.Airco,				"Manual",						false),	
	AIRCO_AUTO				(VehiclePartType.Airco,				"Automatic",					false),
	AIRCO_NONE				(VehiclePartType.Airco,				"No Airco",						true),	
	WHEELS_COMFORT			(VehiclePartType.Wheels,			"Comfort",						false),
	WHEELS_WINTER			(VehiclePartType.Wheels,			"Winter",						false), 
	WHEELS_SPORTS			(VehiclePartType.Wheels, 			"Sports",						false),
	WHEELS_TRUCKSTANDARD	(VehiclePartType.Wheels,			"Truck standard",				false),
	WHEELS_HEAVY_DUTY		(VehiclePartType.Wheels, 			"Heavy-duty",					false),
	SPOILER_LOW				(VehiclePartType.Spoiler,			"Low",							false),
	SPOILER_NONE			(VehiclePartType.Spoiler,			"No Spoiler",					true),
	SPOILER_HIGH			(VehiclePartType.Spoiler,			"High",							false),
	TOOLSTORAGE_NONE		(VehiclePartType.ToolStorage,		"No Toolstorage",				true),
	TOOLSTORAGE_STANDARD	(VehiclePartType.ToolStorage,		"Standard Toolstorage",			false),
	CARGO_NONE				(VehiclePartType.CargoProtection,	"No Cargo Protection",			true),
	CARGO_STANDARD			(VehiclePartType.CargoProtection,	"Standard Cargo Protection",	false),
	CERTIFICATION_NONE		(VehiclePartType.Certification,		"No Certification", 			true),
	CERTIFICATION_STANDARD	(VehiclePartType.Certification,		"Standard Certification",		false),;
	
	
	/**
	 * The car part type of this car part.
	 */
	public final VehiclePartType type;
	
	/**
	 * The full name of this car part.
	 */
	public final String fullName;
	
	public final boolean autocompl;
	
	/**
	 * Make a CarPart with given car part type and full name.
	 * @param type		The type of this car part.
	 * @param fullName	The full name of this car part.
	 */
	VehiclePart(VehiclePartType type, String fullName, boolean autocompl){
		this.autocompl = autocompl;
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
